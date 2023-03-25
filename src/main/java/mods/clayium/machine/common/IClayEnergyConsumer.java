package mods.clayium.machine.common;

import mods.clayium.item.common.IClayEnergy;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IClayEnergyConsumer extends IInventory {
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

    /**
     * Change Compressed Clay to Clay Energy
     */
    static boolean produceClayEnergy(IClayEnergyConsumer consumer) {
        if (consumer.getEnergySlot() < 0 || consumer.getEnergySlot() >= consumer.getSizeInventory()) return false;
        ItemStack itemstack = consumer.getStackInSlot(consumer.getEnergySlot());
        if (itemstack.isEmpty()) return false;

        if (!IClayEnergy.hasClayEnergy(itemstack)) return false;
        consumer.setContainEnergy(consumer.getContainEnergy() + IClayEnergy.getClayEnergy(itemstack));
        itemstack.shrink(1);
        consumer.markDirty();

        return true;
    }

    static boolean compensateClayEnergy(IClayEnergyConsumer consumer, long debt) {
        return compensateClayEnergy(consumer, debt, true);
    }

    static boolean compensateClayEnergy(IClayEnergyConsumer consumer, long debt, boolean doConsume) {
        if (debt > consumer.getContainEnergy()) {
            if (!produceClayEnergy(consumer)) return false;

            return compensateClayEnergy(consumer, debt, doConsume);
        }

        if (doConsume) consumer.setContainEnergy(consumer.getContainEnergy() - debt);
        return true;
    }

    /**
     * TRUE ensures that the TileEntity inherits {@link IClayEnergyConsumer}
     */
    static boolean hasClayEnergy(TileEntity te) {
        return te instanceof IClayEnergyConsumer
                && ((IClayEnergyConsumer) te).getEnergySlot() != -1
                && ((IClayEnergyConsumer) te).verifyClayEnergy();
    }

    default boolean verifyClayEnergy() {
        return true;
    }
}
