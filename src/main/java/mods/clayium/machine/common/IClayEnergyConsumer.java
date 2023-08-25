package mods.clayium.machine.common;

import mods.clayium.item.common.IClayEnergy;
import mods.clayium.util.RangeCheck;
import mods.clayium.util.UsedFor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

@UsedFor(UsedFor.Type.TileEntity)
public interface IClayEnergyConsumer extends IInventory, ClayEnergyHolder {
    int NIL_ENERGY_SLOT = -1;

    int getClayEnergyStorageSize();
    void setClayEnergyStorageSize(int size);
    static void growCEStorageSize(IClayEnergyConsumer consumer, int dist) {
        dist += consumer.getClayEnergyStorageSize();
        if (dist > 64) {
            dist = 64;
        }
        consumer.setClayEnergyStorageSize(dist);
    }

    /**
     * -1 means the machine doesn't have relationship between IClayEnergyConsumer
     */
    int getEnergySlot();

    default ItemStack getEnergyStack() {
        if (this.getEnergySlot() == NIL_ENERGY_SLOT) return ItemStack.EMPTY;
        return this.getStackInSlot(this.getEnergySlot());
    }

    default boolean acceptClayEnergy() {
        return this.getEnergySlot() != NIL_ENERGY_SLOT;
    }

    static boolean isItemValidForSlot(IClayEnergyConsumer inv, int index, ItemStack stack) {
        if (inv != null && index == inv.getEnergySlot()) {
            return inv.acceptClayEnergy() && IClayEnergy.hasClayEnergy(stack)
                    && (inv.getEnergyStack().isEmpty()
                    || inv.getEnergyStack().getCount() < inv.getClayEnergyStorageSize());
        }

        return false;
    }

    /**
     * Change Compressed Clay to Clay Energy
     */
    static boolean produceClayEnergy(IClayEnergyConsumer consumer) {
        if (RangeCheck.isInOutOfInclusive(consumer.getEnergySlot(), 0, consumer.getSizeInventory() - 1)) return false;

        ItemStack itemstack = consumer.getStackInSlot(consumer.getEnergySlot());
        if (itemstack.isEmpty()) return false;

        if (!IClayEnergy.hasClayEnergy(itemstack)) return false;
        consumer.containEnergy().increase(IClayEnergy.getClayEnergy(itemstack));
        itemstack.shrink(1);
        consumer.markDirty();

        return true;
    }

    static boolean compensateClayEnergy(IClayEnergyConsumer consumer, long debt) {
        return compensateClayEnergy(consumer, debt, true);
    }

    static boolean compensateClayEnergy(IClayEnergyConsumer consumer, long debt, boolean doConsume) {
        if (!consumer.containEnergy().hasEnough(debt)) {
            if (!produceClayEnergy(consumer)) return false;

            return compensateClayEnergy(consumer, debt, doConsume);
        }

        if (doConsume) consumer.containEnergy().decrease(debt);
        return true;
    }

    /**
     * TRUE ensures that the Object inherits {@link IClayEnergyConsumer}
     * <br>TileEntityを保証していると冗長になる場合があるので、Objectとした。
     */
    static boolean hasClayEnergy(Object te) {
        return te instanceof IClayEnergyConsumer && ((IClayEnergyConsumer) te).acceptClayEnergy();
    }
}
