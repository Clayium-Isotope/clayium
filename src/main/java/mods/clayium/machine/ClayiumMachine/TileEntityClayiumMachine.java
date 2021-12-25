package mods.clayium.machine.ClayiumMachine;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.common.IClicker;
import mods.clayium.machine.common.TileEntitySidedClayContainer;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.RecipeElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayiumMachine extends TileEntitySidedClayContainer implements IClicker, ITickable {
    private enum MachineSlots {
        MATERIAL,
        PRODUCT,
        ENERGY
    }

    private final int tier;
    private final ClayiumRecipe recipeCards;
    private RecipeElement doingRecipe = RecipeElement.FLAT;

    private long craftTime;
    private long timeToCraft;
    private long debtEnergy;

    public long containEnergy;
    public final int clayEnergySlot = MachineSlots.ENERGY.ordinal();

    public TileEntityClayiumMachine(int tier, ClayiumBlocks.MachineKind kind) {
        super(MachineSlots.values().length);

        this.tier = tier;
        this.recipeCards = kind.getRecipe();
    }

    public ItemStack getStackInSlot(MachineSlots index) {
        return super.getStackInSlot(index.ordinal());
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
        ClayiumMachine.updateBlockState(world, pos);

        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        craftTime = tagCompound.getLong("CraftTime");
        timeToCraft = tagCompound.getLong("TimeToCraft");
        debtEnergy = tagCompound.getLong("ConsumingEnergy");

        containEnergy = tagCompound.getLong("ClayEnergy");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setLong("CraftTime", craftTime);
        tagCompound.setLong("TimeToCraft", timeToCraft);
        tagCompound.setLong("ConsumingEnergy", debtEnergy);

        tagCompound.setLong("ClayEnergy", containEnergy);

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
            machineInventory.set(MachineSlots.PRODUCT.ordinal(), itemstack.copy());
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

    static boolean hasClayEnergy(ItemStack itemstack) {
        return getClayEnergy(itemstack) > 0L;
    }

    static long getClayEnergy(ItemStack itemstack) {
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof IClayEnergy) {
            return ((IClayEnergy) itemstack.getItem()).getClayEnergy();
        }

        return 0L;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index != MachineSlots.ENERGY.ordinal();
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
