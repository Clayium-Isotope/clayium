package mods.clayium.machine.common;

import mods.clayium.item.common.IClayEnergy;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IClayEnergyConsumer extends IInventory {
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
}
