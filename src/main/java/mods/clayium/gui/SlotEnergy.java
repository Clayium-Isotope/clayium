package mods.clayium.gui;

import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEnergy extends Slot {
    public SlotEnergy(TileEntityClayContainer inventoryIn, int index, int machineGuiSizeY) {
        super(inventoryIn, index, -12, machineGuiSizeY - 16);
    }

    @Override
    public int getSlotStackLimit() {
        if (!(this.inventory instanceof IClayEnergyConsumer)) return 0;

        return ((IClayEnergyConsumer) this.inventory).getClayEnergyStorageSize();
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.inventory instanceof IClayEnergyConsumer && IClayEnergy.hasClayEnergy(stack)
                && (((IClayEnergyConsumer) this.inventory).getEnergyStack().isEmpty()
                    || ((IClayEnergyConsumer) this.inventory).getEnergyStack().getCount() < ((IClayEnergyConsumer) this.inventory).getClayEnergyStorageSize());
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public boolean isEnabled() {
        assert this.inventory instanceof TileEntityClayContainer;
        return IClayEnergyConsumer.hasClayEnergy((TileEntityClayContainer) this.inventory);
    }
}
