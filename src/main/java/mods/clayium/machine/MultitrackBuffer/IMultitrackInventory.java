package mods.clayium.machine.MultitrackBuffer;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface IMultitrackInventory extends ISidedInventory {

    int[] getSlotsForFace(EnumFacing side, int track);

    boolean canInsertItem(int slot, ItemStack stack, EnumFacing side, int track);

    boolean canExtractItem(int slot, ItemStack stack, EnumFacing side, int track);
}
