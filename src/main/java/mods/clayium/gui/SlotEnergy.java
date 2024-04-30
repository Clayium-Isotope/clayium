package mods.clayium.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import mods.clayium.machine.common.IClayEnergyConsumer;

public class SlotEnergy extends Slot {

    public SlotEnergy(IClayEnergyConsumer inventoryIn, int machineGuiSizeY) {
        super(inventoryIn, inventoryIn.getEnergySlot(), -12, machineGuiSizeY - 16);
    }

    @Override
    public int getSlotStackLimit() {
        assert this.inventory instanceof IClayEnergyConsumer;

        return ((IClayEnergyConsumer) this.inventory).getClayEnergyStorageSize();
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        assert this.inventory instanceof IClayEnergyConsumer;

        return IClayEnergyConsumer.isItemValidForSlot((IClayEnergyConsumer) this.inventory, this.slotNumber, stack);
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return IClayEnergyConsumer.hasClayEnergy(this.inventory);
    }
}
