package mods.clayium.machine.ClayContainerTest;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TEClayContainerTest extends TileEntityClayContainer {
    public TEClayContainerTest() {
        super(1);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }
}
