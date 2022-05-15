package mods.clayium.block.tile;

import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.List;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilTransfer;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class TileAutoTrader
        extends TileClayContainerTiered {
    public IMerchant merchant;
    public MerchantRecipeList merchantRecipeList;
    public MerchantRecipe currentRecipe;
    protected PacketBuffer oldRecipeListBuffer;
    public int currentRecipeIndex = 0;
    private int toSellSlotIndex = 2;


    public static long energyPerTrade = 10000000L;

    public void initParams() {
        super.initParams();
        this.containerItemStacks = new ItemStack[4];
        this.clayEnergySlot = 3;
        this.listSlotsInsert.add(new int[] {0});
        this.listSlotsInsert.add(new int[] {1});
        this.listSlotsInsert.add(new int[] {0, 1});
        this.listSlotsInsert.add(new int[] {3});
        this.listSlotsExtract.add(new int[] {2});
        this.insertRoutes = new int[] {-1, -1, -1, 3, 0, 1};
        this.maxAutoExtract = new int[] {-1, -1, -1, 1};
        this.extractRoutes = new int[] {-1, -1, 0, -1, -1, -1};
        this.maxAutoInsert = new int[] {-1};
        this.slotsDrop = new int[] {0, 1, 2, 3};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    public void initParamsByTier(int tier) {
        setDefaultTransportationParamsByTier(tier, ParamMode.MACHINE);
    }

    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            List<IMerchant> list = this.worldObj.selectEntitiesWithinAABB(IMerchant.class, AxisAlignedBB.getBoundingBox(this.xCoord, (this.yCoord + 1), this.zCoord, (this.xCoord + 1), (this.yCoord + 3), (this.zCoord + 1)), IEntitySelector.selectAnything);
            if (!list.isEmpty()) {
                this.merchant = list.get(0);
                this.merchantRecipeList = this.merchant.getRecipes(null);
                PacketBuffer newRecipeListBuffer = new PacketBuffer(Unpooled.buffer());
                try {
                    this.merchantRecipeList.func_151391_a(newRecipeListBuffer);
                } catch (IOException e) {
                    ClayiumCore.logger.catching(e);
                }
                if (this.oldRecipeListBuffer == null || !newRecipeListBuffer.equals(this.oldRecipeListBuffer)) {
                    setSyncFlag();
                }
                this.oldRecipeListBuffer = newRecipeListBuffer;
                this.currentRecipeIndex = Math.max(Math.min(this.currentRecipeIndex, this.merchantRecipeList.size() - 1), 0);
            } else {
                this.merchant = null;
                this.merchantRecipeList = null;
                if (this.oldRecipeListBuffer != null) {
                    setSyncFlag();
                }
                this.oldRecipeListBuffer = null;
            }

            resetRecipeAndSlots();

            if (this.currentRecipe != null) {
                ItemStack itemToSell = this.currentRecipe.getItemToSell().copy();
                if (UtilTransfer.canProduceItemStack(itemToSell, this.containerItemStacks, this.toSellSlotIndex, getInventoryStackLimit()) >= itemToSell.stackSize &&
                        consumeClayEnergy(energyPerTrade)) {
                    UtilTransfer.produceItemStack(itemToSell, this.containerItemStacks, this.toSellSlotIndex, getInventoryStackLimit());
                    onPickupFromMerchantSlot(this.currentRecipe);
                    setSyncFlag();

                    this.merchant.func_110297_a_(getStackInSlot(this.toSellSlotIndex));
                }
            }
        }
    }


    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        super.setInventorySlotContents(slot, itemstack);
        if (inventoryResetNeededOnSlotChange(slot)) {
            resetRecipeAndSlots();
        }
    }

    public void resetRecipeAndSlots() {
        resetRecipeAndSlots(this.currentRecipeIndex, this.containerItemStacks[0], this.containerItemStacks[1]);
    }

    public void resetRecipeAndSlots(int currentRecipeIndex, ItemStack itemstack, ItemStack itemstack1) {
        if (this.merchantRecipeList != null) {
            this.currentRecipe = null;

            if (itemstack == null) {

                itemstack = itemstack1;
                itemstack1 = null;
            }
            if (itemstack != null) {

                if (this.merchantRecipeList != null) {

                    MerchantRecipe merchantrecipe = this.merchantRecipeList.canRecipeBeUsed(itemstack, itemstack1, currentRecipeIndex);
                    if (merchantrecipe == null) ;

                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {

                        this.currentRecipe = merchantrecipe;
                    } else if (itemstack1 != null) {

                        merchantrecipe = this.merchantRecipeList.canRecipeBeUsed(itemstack1, itemstack, currentRecipeIndex);

                        if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                            this.currentRecipe = merchantrecipe;
                        }
                    }
                }
            }
        } else {
            this.currentRecipe = null;
        }
    }

    public void setCurrentRecipeIndex(int p_70471_1_) {
        this.currentRecipeIndex = p_70471_1_;
        if (this.merchantRecipeList != null)
            this.currentRecipeIndex = Math.max(Math.min(this.currentRecipeIndex, this.merchantRecipeList.size() - 1), 0);
        resetRecipeAndSlots();
    }

    public boolean inventoryResetNeededOnSlotChange(int par1) {
        return (par1 == 0 || par1 == 1);
    }


    public void onPickupFromMerchantSlot(MerchantRecipe currentRecipe) {
        if (this.merchant != null) {
            MerchantRecipe merchantrecipe = currentRecipe;
            if (merchantrecipe != null) {

                ItemStack itemstack1 = this.containerItemStacks[0];
                ItemStack itemstack2 = this.containerItemStacks[1];

                if (checkAndConsumeIngredents(merchantrecipe, itemstack1, itemstack2) || checkAndConsumeIngredents(merchantrecipe, itemstack2, itemstack1)) {

                    this.merchant.useRecipe(merchantrecipe);

                    if (itemstack1 != null && itemstack1.stackSize <= 0) {
                        itemstack1 = null;
                    }

                    if (itemstack2 != null && itemstack2.stackSize <= 0) {
                        itemstack2 = null;
                    }

                    setInventorySlotContents(0, itemstack1);
                    setInventorySlotContents(1, itemstack2);
                }
            }
            markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }


    private boolean checkAndConsumeIngredents(MerchantRecipe p_75230_1_, ItemStack p_75230_2_, ItemStack p_75230_3_) {
        ItemStack itemstack2 = p_75230_1_.getItemToBuy();
        ItemStack itemstack3 = p_75230_1_.getSecondItemToBuy();

        if (p_75230_2_ != null && p_75230_2_.getItem() == itemstack2.getItem()) {

            if (itemstack3 != null && p_75230_3_ != null && itemstack3.getItem() == p_75230_3_.getItem()) {

                p_75230_2_.stackSize -= itemstack2.stackSize;
                p_75230_3_.stackSize -= itemstack3.stackSize;
                return true;
            }
            if (itemstack3 == null && p_75230_3_ == null) {

                p_75230_2_.stackSize -= itemstack2.stackSize;
                return true;
            }
        }
        return false;
    }

    public void pushButton(EntityPlayer player, int action) {
        switch (action) {
            case 1:
                setCurrentRecipeIndex(this.currentRecipeIndex + 1);
                break;
            case 2:
                setCurrentRecipeIndex(this.currentRecipeIndex - 1);
                break;
        }

        setInstantSyncFlag();
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        if (tagCompound.getBoolean("HasMerchantRecipeList")) {

            this.merchantRecipeList = new MerchantRecipeList(tagCompound.getCompoundTag("MerchantRecipeList"));

        } else {

            this.merchantRecipeList = null;
        }
        setCurrentRecipeIndex(tagCompound.getInteger("MerchantRecipeIndex"));
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("HasMerchantRecipeList", (this.merchantRecipeList != null));
        if (this.merchantRecipeList != null) {
            tagCompound.setTag("MerchantRecipeList", (NBTBase) this.merchantRecipeList.getRecipiesAsTags());
        }
        tagCompound.setInteger("MerchantRecipeIndex", this.currentRecipeIndex);
    }

    public void openInventory() {}

    public void closeInventory() {}
}
