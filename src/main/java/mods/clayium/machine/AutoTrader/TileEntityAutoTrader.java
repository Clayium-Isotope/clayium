package mods.clayium.machine.AutoTrader;

import io.netty.buffer.Unpooled;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IButtonProvider;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.util.ContainClayEnergy;
import mods.clayium.util.UtilPlayer;
import mods.clayium.util.UtilTransfer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAutoTrader extends TileEntityClayContainer implements IButtonProvider, IClayEnergyConsumer {
    protected static final AxisAlignedBB villagerRange = new AxisAlignedBB(0, 1, 0, 1,3, 1);
    public IMerchant merchant;
    public final MerchantRecipeList merchantRecipeList = new MerchantRecipeList();
    public MerchantRecipe currentRecipe;
    protected PacketBuffer oldRecipeListBuffer;
    public int currentRecipeIndex = 0;
    private int toSellSlotIndex = 2;
    public static long energyPerTrade = 10000000L;
    protected final ContainClayEnergy ce = new ContainClayEnergy();

    public void initParams() {
        super.initParams();
        this.containerItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
        this.listSlotsImport.add(new int[]{0});
        this.listSlotsImport.add(new int[]{1});
        this.listSlotsImport.add(new int[]{0, 1});
        this.listSlotsImport.add(new int[]{3});
        this.listSlotsExport.add(new int[]{2});
        this.setImportRoutes(-1, -1, -1, 3, 0, 1);
        this.maxAutoExtract = new int[]{-1, -1, -1, 1};
        this.setExportRoutes(-1, -1, 0, -1, -1, -1);
        this.maxAutoInsert = new int[]{-1};
        this.slotsDrop = new int[]{0, 1, 2, 3};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    public void update() {
        super.update();
        if (this.world.isRemote) {
            return;
        }

        List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, villagerRange.offset(this.pos), e -> e instanceof IMerchant);

        if (list.isEmpty()) {
            this.merchant = null;
            this.merchantRecipeList.clear();
            if (this.oldRecipeListBuffer != null) {
//                this.setSyncFlag();
                this.markDirty();
            }

            this.oldRecipeListBuffer = null;
        } else {
            this.merchant = (IMerchant)list.get(0);
            this.merchantRecipeList.clear();
            this.merchantRecipeList.addAll(this.merchant.getRecipes(UtilPlayer.getDefaultFake()));
            PacketBuffer newRecipeListBuffer = new PacketBuffer(Unpooled.buffer());

            this.merchantRecipeList.writeToBuf(newRecipeListBuffer);

            if (!newRecipeListBuffer.equals(this.oldRecipeListBuffer)) {
//                this.setSyncFlag();
                this.markDirty();
            }

            this.oldRecipeListBuffer = newRecipeListBuffer;
            this.currentRecipeIndex = Math.max(Math.min(this.currentRecipeIndex, this.merchantRecipeList.size() - 1), 0);
        }

        this.resetRecipeAndSlots();
        if (this.currentRecipe != null) {
            ItemStack itemToSell = this.currentRecipe.getItemToSell().copy();
            if (UtilTransfer.canProduceItemStack(itemToSell, this.containerItemStacks, this.toSellSlotIndex, this.getInventoryStackLimit()) >= itemToSell.getCount() && IClayEnergyConsumer.consumeClayEnergy(this, energyPerTrade)) {
                UtilTransfer.produceItemStack(itemToSell, this.containerItemStacks, this.toSellSlotIndex, this.getInventoryStackLimit());
                this.onPickupFromMerchantSlot(this.currentRecipe);
//                this.setSyncFlag();
                this.merchant.verifySellingItem(this.getStackInSlot(this.toSellSlotIndex));
            }
        }

    }

    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        super.setInventorySlotContents(slot, itemstack);
        if (this.inventoryResetNeededOnSlotChange(slot)) {
            this.resetRecipeAndSlots();
        }

    }

    public void resetRecipeAndSlots() {
        this.resetRecipeAndSlots(this.currentRecipeIndex, this.getStackInSlot(0), this.getStackInSlot(1));
    }

    public void resetRecipeAndSlots(int currentRecipeIndex, ItemStack itemstack, ItemStack itemstack1) {
        if (this.merchantRecipeList != null) {
            this.currentRecipe = null;
            if (itemstack == null) {
                itemstack = itemstack1;
                itemstack1 = null;
            }

            if (itemstack != null && this.merchantRecipeList != null) {
                MerchantRecipe merchantrecipe = this.merchantRecipeList.canRecipeBeUsed(itemstack, itemstack1, currentRecipeIndex);
                if (merchantrecipe == null) {
                }

                if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                    this.currentRecipe = merchantrecipe;
                } else if (!itemstack1.isEmpty()) {
                    merchantrecipe = this.merchantRecipeList.canRecipeBeUsed(itemstack1, itemstack, currentRecipeIndex);
                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                        this.currentRecipe = merchantrecipe;
                    }
                }
            }
        } else {
            this.currentRecipe = null;
        }

    }

    public void setCurrentRecipeIndex(int p_70471_1_) {
        this.currentRecipeIndex = p_70471_1_;
        if (this.merchantRecipeList != null) {
            this.currentRecipeIndex = Math.max(Math.min(this.currentRecipeIndex, this.merchantRecipeList.size() - 1), 0);
        }

        this.resetRecipeAndSlots();
    }

    public boolean inventoryResetNeededOnSlotChange(int par1) {
        return par1 == 0 || par1 == 1;
    }

    public void onPickupFromMerchantSlot(MerchantRecipe currentRecipe) {
        if (this.merchant != null) {
            if (currentRecipe != null) {
                ItemStack itemstack1 = this.getStackInSlot(0);
                ItemStack itemstack2 = this.getStackInSlot(1);
                if (this.checkAndConsumeIngredents(currentRecipe, itemstack1, itemstack2) || this.checkAndConsumeIngredents(currentRecipe, itemstack2, itemstack1)) {
                    this.merchant.useRecipe(currentRecipe);
                    if (!itemstack1.isEmpty() && itemstack1.getCount() <= 0) {
                        itemstack1 = ItemStack.EMPTY;
                    }

                    if (!itemstack2.isEmpty() && itemstack2.getCount() <= 0) {
                        itemstack2 = ItemStack.EMPTY;
                    }

                    this.setInventorySlotContents(0, itemstack1);
                    this.setInventorySlotContents(1, itemstack2);
                }
            }

            this.markDirty();
            this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
        }

    }

    private boolean checkAndConsumeIngredents(MerchantRecipe p_75230_1_, ItemStack p_75230_2_, ItemStack p_75230_3_) {
        ItemStack itemstack2 = p_75230_1_.getItemToBuy();
        ItemStack itemstack3 = p_75230_1_.getSecondItemToBuy();
        if (!p_75230_2_.isEmpty() && p_75230_2_.getItem() == itemstack2.getItem()) {
            if (!itemstack3.isEmpty() && !p_75230_3_.isEmpty() && itemstack3.getItem() == p_75230_3_.getItem()) {
                p_75230_2_.shrink(itemstack2.getCount());
                p_75230_3_.shrink(itemstack3.getCount());
                return true;
            }

            if (itemstack3.isEmpty() && p_75230_3_.isEmpty()) {
                p_75230_2_.shrink(itemstack2.getCount());
                return true;
            }
        }

        return false;
    }

    @Override
    public ButtonProperty canPushButton(int button) {
        return ButtonProperty.PERMIT;
    }

    @Override
    public boolean isButtonEnable(int button) {
        return true;
    }

    @Override
    public void pushButton(EntityPlayer player, int action) {
        switch (action) {
            case 1:
                this.setCurrentRecipeIndex(this.currentRecipeIndex + 1);
                break;
            case 2:
                this.setCurrentRecipeIndex(this.currentRecipeIndex - 1);
        }

//        this.setInstantSyncFlag();
    }

    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);
        this.merchantRecipeList.clear();
        if (tagCompound.getBoolean("HasMerchantRecipeList")) {
            this.merchantRecipeList.readRecipiesFromTags(tagCompound.getCompoundTag("MerchantRecipeList"));
        }

        this.setCurrentRecipeIndex(tagCompound.getInteger("MerchantRecipeIndex"));
    }

    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);
        tagCompound.setBoolean("HasMerchantRecipeList", !this.merchantRecipeList.isEmpty());
        if (!this.merchantRecipeList.isEmpty()) {
            tagCompound.setTag("MerchantRecipeList", this.merchantRecipeList.getRecipiesAsTags());
        }

        tagCompound.setInteger("MerchantRecipeIndex", this.currentRecipeIndex);
        return tagCompound;
    }

    @Override
    public ContainClayEnergy containEnergy() {
        return this.ce;
    }

    @Override
    public int getClayEnergyStorageSize() {
        return 0;
    }

    @Override
    public void setClayEnergyStorageSize(int size) {

    }

    @Override
    public int getEnergySlot() {
        return 3;
    }

    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        this.registerInsertIcons("import_1", "import_2", "import", "import_energy");
        this.registerExtractIcons("export");
    }
}
