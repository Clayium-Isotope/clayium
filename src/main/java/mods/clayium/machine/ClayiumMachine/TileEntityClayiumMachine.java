package mods.clayium.machine.ClayiumMachine;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.IHasButton;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.RecipeElement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayiumMachine extends TileEntityClayContainer implements IHasButton, ITickable {
    public enum MachineSlots {
        MATERIAL,
        PRODUCT,
        ENERGY
    }

    protected EnumMachineKind kind;
    private ClayiumRecipe recipeCards;
    private RecipeElement doingRecipe = RecipeElement.FLAT;

    private long craftTime;
    private long timeToCraft;
    private long debtEnergy;

    public long containEnergy;

    public TileEntityClayiumMachine() {
        this.containerItemStacks = NonNullList.withSize(MachineSlots.values().length, ItemStack.EMPTY);

        this.listSlotsImport.add(new int[] {MachineSlots.MATERIAL.ordinal()});
        this.listSlotsExport.add(new int[] {MachineSlots.PRODUCT.ordinal()});

        this.maxAutoExtract = new int[] {-1, 1};
        this.maxAutoInsert = new int[] {-1};

        this.autoInsert = true;
        this.autoExtract = true;
        this.clayEnergySlot = MachineSlots.ENERGY.ordinal();
    }

    public void setKind(EnumMachineKind kind) {
        this.kind = kind;
        if (kind != null) {
            this.recipeCards = kind.getRecipe();
        }
    }

    public ItemStack getStackInSlot(MachineSlots index) {
        return super.getStackInSlot(index.ordinal());
    }

    @Override
    public boolean hasSpecialDrops() {
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);

        drops.add(getStackInSlot(MachineSlots.MATERIAL));
        drops.add(getStackInSlot(MachineSlots.PRODUCT));
        drops.add(getStackInSlot(this.clayEnergySlot));
    }

    @Override
    public void update() {
        if (doingRecipe == RecipeElement.FLAT) {
            RecipeElement _recipe = getRecipe(getStackInSlot(MachineSlots.MATERIAL));

            if (canCraft(_recipe) && compensateClayEnergy(_recipe.getResult().getEnergy())) {
                doingRecipe = _recipe;

                debtEnergy = doingRecipe.getResult().getEnergy();
                timeToCraft = doingRecipe.getResult().getTime();
                getStackInSlot(MachineSlots.MATERIAL).shrink(doingRecipe.getCondition()
                        .getStackSizes(getStackInSlot(MachineSlots.MATERIAL))[0]);

                proceedCraft();
            }
        } else if (compensateClayEnergy(debtEnergy)) {
            proceedCraft();
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);

        sendUpdate();
    }

    private void sendUpdate() {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        mods.clayium.machine.ClayiumMachine.ClayiumMachine.updateBlockState(world, pos);

        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        craftTime = tagCompound.getLong("CraftTime");
        timeToCraft = tagCompound.getLong("TimeToCraft");
        debtEnergy = tagCompound.getLong("ConsumingEnergy");

        containEnergy = tagCompound.getLong("ClayEnergy");

        setKind(EnumMachineKind.fromName(tagCompound.getString("MachineId")));

        super.readFromNBT(tagCompound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setLong("CraftTime", craftTime);
        tagCompound.setLong("TimeToCraft", timeToCraft);
        tagCompound.setLong("ConsumingEnergy", debtEnergy);

        tagCompound.setLong("ClayEnergy", containEnergy);

        tagCompound.setString("MachineId", this.kind.getRegisterName());

        return tagCompound;
    }

    @SideOnly(Side.CLIENT)
    public int getCraftProgressScaled(int par1) {
        if (timeToCraft == 0L) return 0;
        return (int) (craftTime * par1 / timeToCraft);
    }

    public RecipeElement getRecipe(ItemStack stack) {
        return recipeCards.getRecipe(stack, tier);
    }

    protected boolean canCraft(ItemStack material) {
        if (material.isEmpty()) return false;

        return canCraft(getRecipe(material));
    }

    protected boolean canCraft(RecipeElement recipe) {
        ItemStack itemstack = recipe.getResult().getResults().get(0);
        if (itemstack.isEmpty()) return false;
        if (getStackInSlot(MachineSlots.PRODUCT).isEmpty()) return true;
        if (!getStackInSlot(MachineSlots.PRODUCT).isItemEqual(itemstack)) return false;

        int result = getStackInSlot(MachineSlots.PRODUCT).getCount() + itemstack.getCount();
        return result <= getInventoryStackLimit()
                && result <= getStackInSlot(MachineSlots.PRODUCT).getMaxStackSize();
    }

    public boolean canProceedCraft() {
        return canCraft(getStackInSlot(MachineSlots.MATERIAL));
    }

    @Override
    public ButtonProperty canPushButton(int button) {
        if (doingRecipe != RecipeElement.FLAT) return ButtonProperty.PERMIT;
        return canProceedCraft() ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
    }

    @Override
    public boolean isButtonEnable(int button) {
        return button == 0;
    }

    @Override
    public void pushButton(EntityPlayer playerIn, int id) {
        if (canPushButton(0) == ButtonProperty.PERMIT) {
            containEnergy += 5L;
        }

//        setSyncFlag();
    }

    public void proceedCraft() {
        craftTime++;
        if (craftTime < timeToCraft) return;

        ItemStack itemstack = doingRecipe.getResult().getResults().get(0);

        if (getStackInSlot(MachineSlots.PRODUCT).isEmpty()) {
            containerItemStacks.set(MachineSlots.PRODUCT.ordinal(), itemstack.copy());
        } else if (getStackInSlot(MachineSlots.PRODUCT).isItemEqual(itemstack)) {
            getStackInSlot(MachineSlots.PRODUCT).grow(itemstack.getCount());
        }

        craftTime = 0L;
        timeToCraft = 0L;
        debtEnergy = 0L;
        doingRecipe = RecipeElement.FLAT;

        sendUpdate();
    }

    public boolean compensateClayEnergy(long debt) {
        return compensateClayEnergy(debt, true);
    }

    public boolean compensateClayEnergy(long debt, boolean doConsume) {
        if (debt > containEnergy) {
            if (produceClayEnergy()) return compensateClayEnergy(debt, true);
            return false;
        }

        if (doConsume) containEnergy -= debt;
        return true;
    }

    public boolean produceClayEnergy() {
        if (clayEnergySlot < 0 || clayEnergySlot >= getSizeInventory()) return false;
        ItemStack itemstack = getStackInSlot(clayEnergySlot);
        if (itemstack.isEmpty()) return false;

        if (!hasClayEnergy(itemstack)) return false;
        containEnergy += getClayEnergy(itemstack);
        itemstack.shrink(1);
        return true;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: return (int) craftTime;
            case 1: return (int) timeToCraft;
            case 2: return (int) debtEnergy;
            case 3: return (int) containEnergy;
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: craftTime = value; break;
            case 1: timeToCraft = value; break;
            case 2: debtEnergy = value; break;
            case 3: containEnergy = value; break;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }
}
