package mods.clayium.util;

import mods.clayium.block.tile.IInventoryFlexibleStackLimit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class UtilTransfer {
    private static final IInventorySelector defaultSelector = new InventorySelector();

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

    public static int insert(TileEntity from, int[] fromSlots, EnumFacing direction, int maxTransfer, IInventorySelector selector) {
        TileEntity to = from.getWorld().getTileEntity(from.getPos().offset(direction));
        if (!(from instanceof IInventory) || !(to instanceof IInventory)) return maxTransfer;

        return transfer((IInventory) from, fromSlots, direction, (IInventory) to, getSlots(to, direction.getOpposite()), maxTransfer);
    }

    public static int insert(TileEntity from, int[] fromSlots, EnumFacing direction, int maxTransfer) {
        return insert(from, fromSlots, direction, maxTransfer, defaultSelector);
    }

    public static int extract(TileEntity to, int[] toSlots, EnumFacing direction, int maxTransfer, IInventorySelector selector) {
        TileEntity from = to.getWorld().getTileEntity(to.getPos().offset(direction));
        if (!(to instanceof IInventory) || !(from instanceof IInventory)) return maxTransfer;

        return transfer((IInventory) from, getSlots(from, direction.getOpposite()), direction.getOpposite(), (IInventory) to, toSlots, maxTransfer);
    }

    public static int extract(TileEntity to, int[] toSlots, EnumFacing direction, int maxTransfer) {
        return extract(to, toSlots, direction, maxTransfer, defaultSelector);
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

    public static ItemStack produceItemStack(ItemStack itemstack, List<ItemStack> inventory, int index, int inventoryStackLimit) {
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack res = itemstack.copy();
        if (inventory.get(index).isEmpty()) {
            inventory.set(index, res.splitStack(Math.min(res.getCount(), inventoryStackLimit)));
        } else if (UtilItemStack.areTypeEqual(inventory.get(index), res)) {
            int a = Math.min(res.getCount(), inventory.get(index).getMaxStackSize() - inventory.get(index).getCount());
            inventory.get(index).grow(a);
            res.shrink(a);
        }

        if (res.getCount() <= 0) {
            res = ItemStack.EMPTY;
        }

        return res;
    }

    public static ItemStack produceItemStack(ItemStack itemstack, List<ItemStack> inventory, int i, int j, int inventoryStackLimit) {
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack res = itemstack.copy();

        int k;
        for(k = i; k < j; ++k) {
            if (!inventory.get(k).isEmpty()) {
                res = produceItemStack(res, inventory, k, inventoryStackLimit);
            }
        }

        for(k = i; k < j; ++k) {
            if (inventory.get(k).isEmpty()) {
                res = produceItemStack(res, inventory, k, inventoryStackLimit);
            }
        }

        return res;
    }

    public static List<ItemStack> produceItemStacks(List<ItemStack> itemstacks, List<ItemStack> inventory, int i, int j, int inventoryStackLimit) {
        if (itemstacks.isEmpty()) {
            return Collections.emptyList();
        }

        for(int k = 0; k < itemstacks.size(); ++k) {
            itemstacks.set(k, produceItemStack(itemstacks.get(k), inventory, i, j, inventoryStackLimit));
        }

        return itemstacks;
    }

    public static int canProduceItemStack(ItemStack itemstack, List<ItemStack> inventory, int index, int inventoryStackLimit) {
        if (itemstack.isEmpty()) {
            return 0;
        }

        ItemStack res = itemstack.copy();
        int stackSize = itemstack.getCount();
        if (inventory.get(index).isEmpty()) {
            return inventoryStackLimit;
        }

        return UtilItemStack.areTypeEqual(inventory.get(index), itemstack) ? inventory.get(index).getMaxStackSize() - inventory.get(index).getCount() : 0;
    }

    /**
     * @return How many space <B>inventory</B> has in {@code [startIncl, endExcl)} to insert <B>itemstack</B>
     */
    public static int canProduceItemStack(ItemStack itemstack, List<ItemStack> inventory, int startIncl, int endExcl, int inventoryStackLimit) {
        int rest = 0;

        for(int k = startIncl; k < endExcl; ++k) {
            rest += canProduceItemStack(itemstack, inventory, k, inventoryStackLimit);
        }

        return rest;
    }

    public static boolean canProduceItemStacks(List<ItemStack> itemstacks, List<ItemStack> inventory, int startIncl, int endExcl, int inventoryStackLimit) {
        if (!itemstacks.isEmpty()) {
            List<ItemStack> copyItemstacks = getHardCopy(itemstacks);
            List<ItemStack> copyInventory = getHardCopy(inventory);

            for (ItemStack copyItemstack : copyItemstacks) {
                if (!produceItemStack(copyItemstack, copyInventory, startIncl, endExcl, inventoryStackLimit).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<ItemStack> getHardCopy(List<ItemStack> itemstacks) {
        if (itemstacks == null || itemstacks.isEmpty()) {
            return Collections.emptyList();
        }

        List<ItemStack> res = new ArrayList<>(itemstacks.size());

        for(int i = 0; i < itemstacks.size(); ++i) {
            if (!itemstacks.get(i).isEmpty()) {
                res.set(i, itemstacks.get(i).copy());
            }
        }

        return res;
    }

    /**
     * Sum each stack size of inv [start, end)
     */
    public static int countItemStack(ItemStack itemstack, List<ItemStack> inventory, int start, int end) {
        if (start < 0 || end <= start || end >= inventory.size()) return 0;

        int res = 0;

        for (int k = start; k < end; ++k) {
            if (!inventory.get(k).isEmpty() && UtilItemStack.areTypeEqual(inventory.get(k), itemstack)) {
                res += inventory.get(k).getCount();
            }
        }

        return res;
    }

    public static int countItemStack(Predicate<ItemStack> predicate, List<ItemStack> inventory, int start, int end) {
        if (start < 0 || end <= start || end >= inventory.size()) return 0;

        int res = 0;

        for (int k = start; k < end; ++k) {
            if (!inventory.get(k).isEmpty() && predicate.test(inventory.get(k))) {
                res += inventory.get(k).getCount();
            }
        }

        return res;
    }

    public static ItemStack consumeItemStack(ItemStack itemstack, List<ItemStack> inventory, int index) {
        ItemStack res = itemstack.copy();

        if (!inventory.get(index).isEmpty() && UtilItemStack.areTypeEqual(inventory.get(index), res)) {
            int n = Math.min(res.getCount(), inventory.get(index).getCount());
            res.shrink(n);
            inventory.get(index).shrink(n);
            if (inventory.get(index).getCount() <= 0) {
                inventory.set(index, ItemStack.EMPTY);
            }
        }

        return res;
    }

    public static ItemStack consumeItemStack(ItemStack itemstack, List<ItemStack> inventory, int i, int j) {
        ItemStack stack = itemstack.copy();

        for(int k = i; k < j; ++k) {
            stack = consumeItemStack(stack, inventory, k);
        }

        if (stack.getCount() <= 0) {
            return ItemStack.EMPTY;
        } else {
            return stack;
        }
    }

    public static List<ItemStack> consumeItemStack(List<ItemStack> itemstack, List<ItemStack> inventory, int i, int j) {
        List<ItemStack> res = new ArrayList<>(itemstack.size());

        for (ItemStack stack : itemstack) {
            res.add(consumeItemStack(stack, inventory, i, j));
        }

        return res;
    }

    // TODO will substitute ItemStackHandler for this
    public static class InventorySelector implements UtilTransfer.IInventorySelector {
        protected IInventory selected = null;

        public InventorySelector() {
        }

        public IInventory getSelectedInventory() {
            return this.selected;
        }

        public boolean selectInventoryToInsertTo(TileEntity from, EnumFacing direction) {
            IInventory to = this.selectInventoryToInsertTo(from.getWorld(), from.getPos(), direction);
            if (to == null) {
                return false;
            } else {
                this.selected = to;
                return true;
            }
        }

        public IInventory selectInventoryToInsertTo(World world, BlockPos pos, EnumFacing direction) {
            TileEntity te = world.getTileEntity(pos.offset(direction));
            if (!(te instanceof IInventory)) {
                return null;
            } else {
                IInventory to = (IInventory)te;
                Block block = world.getBlockState(pos.offset(direction)).getBlock();
                if (block instanceof BlockChest) {
                    IInventory chest = ((BlockChest)block).getContainer(world, pos.offset(direction), true);
                    if (chest != null) {
                        to = chest;
                    }
                }

                return to;
            }
        }

        public int[] getSlotToInsertTo(EnumFacing direction) {
            if (this.selected == null) {
                return null;
            } else {
                EnumFacing toSide = direction.getOpposite();
                int[] toSlots;
                if (!(this.selected instanceof ISidedInventory)) {
                    toSlots = new int[this.selected.getSizeInventory()];

                    for(int i = 0; i < this.selected.getSizeInventory(); toSlots[i] = i++);
                } else {
                    toSlots = ((ISidedInventory)this.selected).getSlotsForFace(toSide);
                }

                return toSlots;
            }
        }

        public boolean selectInventoryToExtractFrom(TileEntity to, EnumFacing direction) {
            IInventory from = this.selectInventoryToExtractFrom(to.getWorld(), to.getPos(), direction);
            if (from == null) {
                return false;
            } else {
                this.selected = from;
                return true;
            }
        }

        public IInventory selectInventoryToExtractFrom(World world, BlockPos pos, EnumFacing direction) {
            TileEntity te = world.getTileEntity(pos.offset(direction));
            if (!(te instanceof IInventory)) {
                return null;
            } else {
                IInventory from = (IInventory)te;
                Block block = world.getBlockState(pos.offset(direction)).getBlock();
                if (block instanceof BlockChest) {
                    IInventory chest = ((BlockChest)block).getContainer(world, pos.offset(direction), true);
                    if (chest != null) {
                        from = chest;
                    }
                }

                return from;
            }
        }

        public int[] getSlotToExtractFrom(EnumFacing direction) {
            if (this.selected == null) {
                return null;
            } else {
                EnumFacing fromSide = direction.getOpposite();
                int[] fromSlots;
                if (!(this.selected instanceof ISidedInventory)) {
                    fromSlots = new int[this.selected.getSizeInventory()];

                    for(int i = 0; i < this.selected.getSizeInventory(); fromSlots[i] = i++);
                } else {
                    fromSlots = ((ISidedInventory)this.selected).getSlotsForFace(fromSide);
                }

                return fromSlots;
            }
        }
    }

    interface IInventorySelector {
        IInventory getSelectedInventory();

        boolean selectInventoryToInsertTo(TileEntity var1, EnumFacing var2);

        int[] getSlotToInsertTo(EnumFacing var1);

        boolean selectInventoryToExtractFrom(TileEntity var1, EnumFacing var2);

        int[] getSlotToExtractFrom(EnumFacing var1);
    }
}
