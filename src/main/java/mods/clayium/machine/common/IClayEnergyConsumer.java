package mods.clayium.machine.common;

import mods.clayium.item.common.IClayEnergy;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IClayEnergyConsumer extends IInventory {
    @Deprecated // I don't know what does it mean.
    default boolean relyingCheckStrictly() {
        return this.getEnergySlot() != -1;
    }

    long getContainEnergy();
    void setContainEnergy(long energy);

    default int getEnergySlot() {
        return -1;
    }
    int getEnergyStorageSize();

    default ItemStack getEnergyStack() {
        if (this.getEnergySlot() == -1) return ItemStack.EMPTY;
        return this.getStackInSlot(this.getEnergySlot());
    }

    default boolean produceClayEnergy() {
        if (this.getEnergySlot() < 0 || this.getEnergySlot() >= getSizeInventory()) return false;
        ItemStack itemstack = getStackInSlot(this.getEnergySlot());
        if (itemstack.isEmpty()) return false;

        if (!IClayEnergy.hasClayEnergy(itemstack)) return false;
        setContainEnergy(this.getContainEnergy() + IClayEnergy.getClayEnergy(itemstack));
        itemstack.shrink(1);

        return true;
    }

    default boolean compensateClayEnergy(long debt) {
        return this.compensateClayEnergy(debt, true);
    }

    default boolean compensateClayEnergy(long debt, boolean doConsume) {
        if (debt > this.getContainEnergy()) {
            if (!this.produceClayEnergy()) return false;

            return this.compensateClayEnergy(debt, doConsume);
        }

        if (doConsume) this.setContainEnergy(this.getContainEnergy() - debt);
        return true;
    }

    /**
     * TRUE ensures that the TileEntity inherits {@link IClayEnergyConsumer}
     */
    static boolean hasClayEnergy(TileEntity te) {
        return te instanceof IClayEnergyConsumer && ((IClayEnergyConsumer) te).getEnergySlot() != -1;
    }
}
