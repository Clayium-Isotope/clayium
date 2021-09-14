package mods.clayium.block.tile;

import mods.clayium.block.itemblock.IOverridableBlock;
import net.minecraft.inventory.ISidedInventory;

public abstract class TileEntityClayContainer extends TileGeneric implements ISidedInventory, IInventoryFlexibleStackLimit, IOverridableBlock {
    private int[] slotsDrop = new int[0];

    public int[] getSlotsDrop() {
        return slotsDrop;
    }
}
