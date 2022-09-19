package mods.clayium.gui;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEnergy extends Slot {
    public SlotEnergy(TileEntityClayContainer inventoryIn, int index, int machineGuiSizeY) {
        super(inventoryIn, index, -12, machineGuiSizeY - 16);
    }

    @Override
    public int getSlotStackLimit() {
        return ((TileEntityClayContainer) this.inventory).getClayEnergyStorageSize();
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return ClayiumBlocks.compressedClay.contains(stack.getItem());
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return true;
    }
}
