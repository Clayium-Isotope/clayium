package mods.clayium.block.tile;

import net.minecraft.item.ItemStack;

import mods.clayium.util.UsedFor;

/**
 * 各スロット、各ItemStackに対して、特定のMaxStackSizeを持っている。
 */
@UsedFor(UsedFor.Type.TileEntity)
public interface FlexibleStackLimit {

    int getInventoryStackLimit(int slot, ItemStack stack);

    default int getInventoryStackLimit() {
        return this.getInventoryStackLimit(0, ItemStack.EMPTY);
    }
}
