package mods.clayium.block.tile;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface IInventoryFlexibleStackLimit {
    int getInventoryStackLimit(int slot);
}
