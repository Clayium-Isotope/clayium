package mods.clayium.util;

import mods.clayium.block.tile.IInventoryFlexibleStackLimit;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.stream.IntStream;

public class UtilTransfer {
    /**
     * Transfer item(s) from a tile to another tile.
     *
     * @param from wants to carry out
     * @param facing FROM is looking at TO
     * @param to wants to carry in
     * @param maxTransfer the operation stops when transferred-items' count became greater than this.
     */
    public static int transfer(IInventory from, int[] outSlots, EnumFacing facing, IInventory to, int[] inSlots, int maxTransfer) {
        ISidedInventory sidedFrom = from instanceof ISidedInventory ? (ISidedInventory) from : null;
        ISidedInventory sidedTo = to instanceof ISidedInventory ? (ISidedInventory) to : null;
        int transferred = maxTransfer;
        // reassigned many times
        ItemStack luggage;
        ItemStack receiver;

        for (int outSlot : outSlots) {
            luggage = from.getStackInSlot(outSlot);

            if (luggage.isEmpty()) continue;
            if (sidedFrom != null && !sidedFrom.canExtractItem(outSlot, luggage, facing)) continue;

            // some items exist on the slot wanna put
            if (luggage.isStackable()) {
                for (int inSlot : inSlots) {
                    receiver = to.getStackInSlot(inSlot);

                    if (receiver.isEmpty()) continue;
                    if (sidedTo != null && !sidedTo.canInsertItem(inSlot, luggage, facing.getOpposite())) continue;
                    if (!ItemStack.areItemsEqual(luggage, receiver)) continue;

                    int stackLimit = to instanceof IInventoryFlexibleStackLimit ? ((IInventoryFlexibleStackLimit) to).getInventoryStackLimit(inSlot) : to.getInventoryStackLimit();
                    int maxSize = Math.min(receiver.getMaxStackSize(), stackLimit);
                    int moves = Math.max(Math.min(maxSize - receiver.getCount(), Math.min(transferred, luggage.getCount())), 0);

                    receiver.grow(moves);
                    transferred -= moves;
                    luggage.shrink(moves);

                    if (luggage.isEmpty()) from.setInventorySlotContents(outSlot, ItemStack.EMPTY);
                    if (transferred <= 0) {
                        to.markDirty();
                        from.markDirty();
                        return 0;
                    }
                    if (luggage.isEmpty()) break;
                }
            }

            // case: the slot wanna put is empty
            if (!luggage.isEmpty()) {
                for (int inSlot : inSlots) {
                    receiver = to.getStackInSlot(inSlot);

                    if (!receiver.isEmpty() || !to.isItemValidForSlot(inSlot, luggage)) continue;
                    if (to instanceof ISidedInventory && !((ISidedInventory) to).canInsertItem(inSlot, luggage, facing.getOpposite())) continue;

                    receiver = luggage.copy();

                    int stackLimit = to instanceof IInventoryFlexibleStackLimit ? ((IInventoryFlexibleStackLimit) to).getInventoryStackLimit(inSlot) : to.getInventoryStackLimit();
                    int stackSize = Math.min(Math.min(transferred, luggage.getCount()), stackLimit);
                    receiver.setCount(stackSize);
                    to.setInventorySlotContents(inSlot, receiver);
                    transferred -= stackSize;
                    luggage.shrink(stackSize);

                    if (luggage.isEmpty()) from.setInventorySlotContents(outSlot, ItemStack.EMPTY);
                    if (transferred <= 0) {
                        to.markDirty();
                        from.markDirty();
                        return 0;
                    }
                    if (luggage.isEmpty()) break;
                }
            }
        }

        if (transferred != maxTransfer)
        {
            to.markDirty();
            from.markDirty();
        }

        return transferred;
    }

    public static int insert(TileEntity from, int[] fromSlots, EnumFacing direction, int maxTransfer) {
        TileEntity to = from.getWorld().getTileEntity(from.getPos().offset(direction));
        if (!(from instanceof IInventory) || !(to instanceof IInventory)) return maxTransfer;

        return transfer((IInventory) from, fromSlots, direction, (IInventory) to, getSlots(to, direction.getOpposite()), maxTransfer);
    }

    public static int extract(TileEntity to, int[] toSlots, EnumFacing direction, int maxTransfer) {
        TileEntity from = to.getWorld().getTileEntity(to.getPos().offset(direction));
        if (!(to instanceof IInventory) || !(from instanceof IInventory)) return maxTransfer;

        return transfer((IInventory) from, getSlots(from, direction.getOpposite()), direction.getOpposite(), (IInventory) to, toSlots, maxTransfer);
    }

    private static int[] getSlots(TileEntity te, EnumFacing facing) {
        if (te instanceof ISidedInventory) {
            return ((ISidedInventory) te).getSlotsForFace(facing);
        }
        if (!(te instanceof IInventory)) {
            return new int[0];
        }

        return IntStream.range(0, ((IInventory) te).getSizeInventory()).toArray();
    }
}
