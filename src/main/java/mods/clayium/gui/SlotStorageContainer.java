package mods.clayium.gui;

import mods.clayium.machine.StorageContainer.TileEntityStorageContainer;
import net.minecraft.item.ItemStack;

public class SlotStorageContainer extends SlotWithTexture {

    public SlotStorageContainer(TileEntityStorageContainer tile, int index, int x, int y, ITexture texture) {
        super(tile, index, x, y, texture);
    }

    @Override
    public void putStack(ItemStack stack) {
        ((TileEntityStorageContainer) this.inventory).putItemStack(this.slotNumber, stack);
        this.onSlotChanged();
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return ((TileEntityStorageContainer) this.inventory).getInventoryStackLimit(this.slotNumber, stack);
    }
}
