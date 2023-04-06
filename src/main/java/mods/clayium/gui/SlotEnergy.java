package mods.clayium.gui;

import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

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

        return IClayEnergy.hasClayEnergy(stack)
                && (((IClayEnergyConsumer) this.inventory).getEnergyStack().isEmpty()
                    || ((IClayEnergyConsumer) this.inventory).getEnergyStack().getCount() < ((IClayEnergyConsumer) this.inventory).getClayEnergyStorageSize());
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.inventory instanceof TileEntity && IClayEnergyConsumer.hasClayEnergy((TileEntity) this.inventory);
    }
}
