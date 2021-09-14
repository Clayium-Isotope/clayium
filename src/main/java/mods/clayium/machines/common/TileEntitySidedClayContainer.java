package mods.clayium.machines.common;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public abstract class TileEntitySidedClayContainer extends TileEntityClayContainer implements ISidedInventory {
    protected TileEntitySidedClayContainer() {
        super();
    }

    protected void initTileEntity(int invSize) {
        super.initTileEntity(invSize);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return true;
    }
}
