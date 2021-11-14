package mods.clayium.util;

import java.util.*;
/*      */
/*      */
/*      */
import mods.clayium.block.tile.IInventoryFlexibleStackLimit;
import mods.clayium.item.CItems;
import mods.clayium.item.ItemCapsule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class UtilTransfer {
    interface IInventorySelector {
        IInventory getSelectedInventory();

        boolean selectInventoryToInsertTo(TileEntity param1TileEntity, ForgeDirection param1ForgeDirection);

        int[] getSlotToInsertTo(ForgeDirection param1ForgeDirection);

        boolean selectInventoryToExtractFrom(TileEntity param1TileEntity, ForgeDirection param1ForgeDirection);

        int[] getSlotToExtractFrom(ForgeDirection param1ForgeDirection);
    }

    public static class InventorySelector implements IInventorySelector {
        protected IInventory selected = null;

        public IInventory getSelectedInventory() {
            return this.selected;
        }

        public boolean selectInventoryToInsertTo(TileEntity from, ForgeDirection direction) {
            IInventory to = selectInventoryToInsertTo(from.getWorldObj(), from.xCoord, from.yCoord, from.zCoord, direction);
            if (to == null)
                return false;
            this.selected = to;
            return true;
        }

        public IInventory selectInventoryToInsertTo(World world, int fromX, int fromY, int fromZ, ForgeDirection direction) {
            TileEntity te = UtilDirection.getTileEntity(world, fromX, fromY, fromZ, direction);
            if (!(te instanceof IInventory)) return null;

            IInventory to = (IInventory) te;
            Block block = UtilDirection.getBlock(world, fromX, fromY, fromZ, direction);
            if (block instanceof BlockChest) {
                IInventory chest = ((BlockChest) block).func_149951_m(world, fromX + direction.offsetX, fromY + direction.offsetY, fromZ + direction.offsetZ);
                if (chest != null) to = chest;
            }

            return to;
        }

        public int[] getSlotToInsertTo(ForgeDirection direction) {
            if (this.selected == null) return null;

            int toSide = direction.getOpposite().ordinal();
            int[] toSlots = null;

            if (!(this.selected instanceof ISidedInventory)) {
                toSlots = new int[this.selected.getSizeInventory()];
                for (int i = 0; i < this.selected.getSizeInventory(); i++) {
                    toSlots[i] = i;
                }
            } else {
                toSlots = ((ISidedInventory) this.selected).getAccessibleSlotsFromSide(toSide);
            }

            return toSlots;
        }

        public boolean selectInventoryToExtractFrom(TileEntity to, ForgeDirection direction) {
            IInventory from = selectInventoryToExtractFrom(to.getWorldObj(), to.xCoord, to.yCoord, to.zCoord, direction);
            if (from == null) return false;

            this.selected = from;
            return true;
        }

        public IInventory selectInventoryToExtractFrom(World world, int toX, int toY, int toZ, ForgeDirection direction) {
            TileEntity te = UtilDirection.getTileEntity(world, toX, toY, toZ, direction);
            if (!(te instanceof IInventory)) return null;

            IInventory from = (IInventory) te;
            Block block = UtilDirection.getBlock(world, toX, toY, toZ, direction);
            if (block instanceof BlockChest) {
                IInventory chest = ((BlockChest) block).func_149951_m(world, toX + direction.offsetX, toY + direction.offsetY, toZ + direction.offsetZ);
                if (chest != null) from = chest;
            }

            return from;
        }

        public int[] getSlotToExtractFrom(ForgeDirection direction) {
            if (this.selected == null) return null;

            int fromSide = direction.getOpposite().ordinal();
            int[] fromSlots = null;
            if (!(this.selected instanceof ISidedInventory)) {
                fromSlots = new int[this.selected.getSizeInventory()];
                for (int i = 0; i < this.selected.getSizeInventory(); i++) {
                    fromSlots[i] = i;
                }
            } else {
                fromSlots = ((ISidedInventory) this.selected).getAccessibleSlotsFromSide(fromSide);
            }

            return fromSlots;
        }
    }

    public static IInventorySelector defaultSelector = new InventorySelector();

    public static int insert(TileEntity from, int[] fromSlots, ForgeDirection direction, int maxTransfer, Object... metadata) {
        IInventorySelector selector = defaultSelector;

        for (Object obj : metadata) {
            if (obj instanceof IInventorySelector) {
                selector = (IInventorySelector) obj;
            }
        }

        if (!selector.selectInventoryToInsertTo(from, direction)) {
            return maxTransfer;
        }

        int fromSide = direction.ordinal();
        int toSide = direction.getOpposite().ordinal();
        IInventory to = selector.getSelectedInventory();
        int[] toSlots = selector.getSlotToInsertTo(direction);

        return transfer((IInventory) from, to, fromSlots, toSlots, fromSide, toSide, maxTransfer);
    }

    public static int transfer(IInventory from, IInventory to, int[] fromSlots, int[] toSlots, int fromSide, int toSide, int maxTransfer) {
        int oldTransfer = maxTransfer;
        ISidedInventory fromSided = (from instanceof ISidedInventory) ? (ISidedInventory) from : null;
        ISidedInventory toSided = (to instanceof ISidedInventory) ? (ISidedInventory) to : null;
        try {
            for (int fromSlot : fromSlots) {
                ItemStack fromItem = from.getStackInSlot(fromSlot);

                if (fromItem != null && fromItem.stackSize > 0
                        && (fromSided == null || fromSided.canExtractItem(fromSlot, fromItem, fromSide))) {
                    if (fromItem.isStackable()) {
                        for (int toSlot : toSlots) {
                            ItemStack toItem = to.getStackInSlot(toSlot);
                            if (toItem != null && toItem.stackSize > 0
                                    && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide))
                                    && fromItem.isItemEqual(toItem) && ItemStack.areItemStackTagsEqual(toItem, fromItem)) {

                                int stackLimit = (to instanceof IInventoryFlexibleStackLimit) ? ((IInventoryFlexibleStackLimit) to).getInventoryStackLimit(toSlot) : to.getInventoryStackLimit();
                                int maxSize = Math.min(toItem.getMaxStackSize(), stackLimit);
                                int maxMove = Math.max(Math.min(maxSize - toItem.stackSize, Math.min(maxTransfer, fromItem.stackSize)), 0);
                                toItem.stackSize += maxMove;
                                maxTransfer -= maxMove;
                                fromItem.stackSize -= maxMove;

                                if (fromItem.stackSize == 0) from.setInventorySlotContents(fromSlot, null);
                                if (maxTransfer == 0) return 0;
                                if (fromItem.stackSize == 0) break;
                            }
                        }
                    }
                    if (fromItem.stackSize > 0) {
                        for (int toSlot : toSlots) {
                            ItemStack toItem = to.getStackInSlot(toSlot);

                            if (toItem == null && to.isItemValidForSlot(toSlot, fromItem)
                                    && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide))) {
                                toItem = fromItem.copy();

                                int stackLimit = (to instanceof IInventoryFlexibleStackLimit) ? ((IInventoryFlexibleStackLimit) to).getInventoryStackLimit(toSlot) : to.getInventoryStackLimit();
                                int stackSize = Math.min(Math.min(maxTransfer, fromItem.stackSize), stackLimit);
                                toItem.stackSize = stackSize;
                                to.setInventorySlotContents(toSlot, toItem);
                                maxTransfer -= stackSize;
                                fromItem.stackSize -= stackSize;

                                if (fromItem.stackSize == 0) from.setInventorySlotContents(fromSlot, null);
                                if (maxTransfer == 0) return 0;
                                if (fromItem.stackSize == 0) break;
                            }
                        }
                    }
                }
            }

            if (oldTransfer != maxTransfer) {
                to.markDirty();
                from.markDirty();
            }
        } finally {
            if (oldTransfer != maxTransfer) {
                to.markDirty();
                from.markDirty();
            }
        }
        return maxTransfer;
    }

    public static int insert(TileEntity from, int[] fromSlots, UtilDirection front, UtilDirection side, int maxTransfer, Object... metadata) {
        ForgeDirection direction = ForgeDirection.getOrientation(front.getSide(side).ordinal());

        return insert(from, fromSlots, direction, maxTransfer, metadata);
    }

    public static int transfer(TileEntity from, int[] fromSlots, UtilDirection front, UtilDirection side, int maxTransfer, Object... metadata) {
        return insert(from, fromSlots, front, side, maxTransfer, metadata);
    }

    public static int transfer(TileEntity from, int[] fromSlots, ForgeDirection direction, int maxTransfer, Object... metadata) {
        return insert(from, fromSlots, direction, maxTransfer, metadata);
    }

    public static int extract(TileEntity to, int[] toSlots, ForgeDirection direction, int maxTransfer, Object... metadata) {
        IInventorySelector selector = defaultSelector;
        for (Object obj : metadata) {
            if (obj instanceof IInventorySelector) {
                selector = (IInventorySelector) obj;
            }
        }

        if (!selector.selectInventoryToExtractFrom(to, direction)) {
            return maxTransfer;
        }

        int toSide = direction.ordinal();
        int fromSide = direction.getOpposite().ordinal();
        IInventory from = selector.getSelectedInventory();
        int[] fromSlots = selector.getSlotToExtractFrom(direction);

        return transfer(from, (IInventory) to, fromSlots, toSlots, fromSide, toSide, maxTransfer);
    }

    public static int extract(TileEntity from, int[] fromSlots, UtilDirection front, UtilDirection side, int maxTransfer, Object... metadata) {
        ForgeDirection direction = ForgeDirection.getOrientation(front.getSide(side).ordinal());
        return extract(from, fromSlots, direction, maxTransfer, metadata);
    }

    public static ItemStack produceItemStack(ItemStack itemstack, ItemStack[] inventory, int index, int inventoryStackLimit) {
        if (itemstack == null) return null;

        ItemStack res = itemstack.copy();
        int stackSize = itemstack.stackSize;

        if (inventory[index] == null) {
            inventory[index] = itemstack.copy();
            (inventory[index]).stackSize = Math.min(itemstack.stackSize, inventoryStackLimit);
            res.stackSize -= (inventory[index]).stackSize;
        } else if (inventory[index].isItemEqual(itemstack)
                && ItemStack.areItemStackTagsEqual(inventory[index], itemstack)) {
            int a = Math.min(itemstack.stackSize, inventory[index].getMaxStackSize() - (inventory[index]).stackSize);
            (inventory[index]).stackSize += a;
            res.stackSize -= a;
        }

        if (res.stackSize <= 0) {
            res = null;
        }

        return res;
    }

    public static ItemStack produceItemStack(ItemStack itemstack, ItemStack[] inventory, int i, int j, int inventoryStackLimit) {
        if (itemstack == null) return null;
        ItemStack res = itemstack.copy();
        int k;

        for (k = i; k < j; k++) {
            if (inventory[k] != null)
                res = produceItemStack(res, inventory, k, inventoryStackLimit);
        }

        for (k = i; k < j; k++) {
            if (inventory[k] == null)
                res = produceItemStack(res, inventory, k, inventoryStackLimit);
        }

        return res;
    }

    public static ItemStack[] produceItemStacks(ItemStack[] itemstacks, ItemStack[] inventory, int i, int j, int inventoryStackLimit) {
        if (itemstacks == null) return null;

        for (int k = 0; k < itemstacks.length; k++) {
            itemstacks[k] = produceItemStack(itemstacks[k], inventory, i, j, inventoryStackLimit);
        }

        return itemstacks;
    }

    public static int canProduceItemStack(ItemStack itemstack, ItemStack[] inventory, int index, int inventoryStackLimit) {
        if (itemstack == null) return 0;

        ItemStack res = itemstack.copy();
        int stackSize = itemstack.stackSize;
        if (inventory[index] == null) return inventoryStackLimit;
        if (inventory[index].isItemEqual(itemstack)
                && ItemStack.areItemStackTagsEqual(inventory[index], itemstack)) {
            return inventory[index].getMaxStackSize() - (inventory[index]).stackSize;
        }

        return 0;
    }

    public static int canProduceItemStack(ItemStack itemstack, ItemStack[] inventory, int i, int j, int inventoryStackLimit) {
        int rest = 0;
        for (int k = i; k < j; k++) {
            rest += canProduceItemStack(itemstack, inventory, k, inventoryStackLimit);
        }

        return rest;
    }


    public static boolean canProduceItemStacks(ItemStack[] itemstacks, ItemStack[] inventory, int i, int j, int inventoryStackLimit) {
        if (itemstacks == null) return true;

        ItemStack[] copyItemstacks = getHardCopy(itemstacks);
        ItemStack[] copyInventory = getHardCopy(inventory);
        for (ItemStack copyItemstack : copyItemstacks) {
            if (produceItemStack(copyItemstack, copyInventory, i, j, inventoryStackLimit) != null)
                return false;
        }

        return true;
    }

    public static ItemStack[] getHardCopy(ItemStack[] itemstacks) {
        if (itemstacks == null) return null;
        ItemStack[] res = new ItemStack[itemstacks.length];
        for (int i = 0; i < res.length; i++) {
            if (itemstacks[i] != null)
                res[i] = itemstacks[i].copy();
        }
        return res;
    }

    public static int countItemStack(ItemStack itemstack, ItemStack[] inventory, int i, int j) {
        int res = 0;
        for (int k = i; k < j; k++) {
            if (inventory[k] != null) {
                if (inventory[k].isItemEqual(itemstack) &&
                        ItemStack.areItemStackTagsEqual(inventory[k], itemstack))
                    res += (inventory[k]).stackSize;
            }
        }

        return res;
    }

    public static ItemStack consumeItemStack(ItemStack itemstack, ItemStack[] inventory, int i, int j) {
        int stackSize = itemstack.stackSize;
        for (int k = i; k < j; k++) {
            if (inventory[k] != null) {
                if (inventory[k].isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(inventory[k], itemstack)) {
                    int n = Math.min(stackSize, (inventory[k]).stackSize);
                    stackSize -= n;
                    (inventory[k]).stackSize -= n;
                    if ((inventory[k]).stackSize <= 0) inventory[k] = null;
                }
            }
        }

        if (stackSize <= 0) return null;

        ItemStack res = itemstack.copy();
        res.stackSize = stackSize;
        return res;
    }

    public static ItemStack[] consumeItemStack(ItemStack[] itemstack, ItemStack[] inventory, int i, int j) {
        ItemStack[] res = new ItemStack[itemstack.length];

        for (int k = 0; k < itemstack.length; k++) {
            res[k] = consumeItemStack(itemstack[k], inventory, i, j);
        }

        return res;
    }

    private static ItemCapsule[] capsules = CItems.itemsCapsule;
    private static int[] capacities = new int[] {1000, 125, 25, 5, 1};

    public static int insertToTank(TileEntity fromTe, int[] fromSlots, ForgeDirection direction, int maxTransfer) {
        int oldTransfer = maxTransfer;

        int fromSide = direction.ordinal();
        int toSide = direction.getOpposite().ordinal();
        ForgeDirection toDirection = direction.getOpposite();

        World world = fromTe.getWorldObj();
        IInventory from = (IInventory) fromTe;
        ISidedInventory fromSided = (from instanceof ISidedInventory) ? (ISidedInventory) from : null;

        TileEntity te = UtilDirection.getTileEntity(world, fromTe.xCoord, fromTe.yCoord, fromTe.zCoord, direction);
        if (!(te instanceof IFluidHandler)) return maxTransfer;
        IFluidHandler to = (IFluidHandler) te;

        Block block = UtilDirection.getBlock(world, fromTe.xCoord, fromTe.yCoord, fromTe.zCoord, direction);

        for (int fromSlot : fromSlots) {
            ItemStack fromItem = from.getStackInSlot(fromSlot);
            if (fromItem != null && fromItem.stackSize > 0 && fromItem.getItem() instanceof ItemCapsule
                    && (fromSided == null || fromSided.canExtractItem(fromSlot, fromItem, fromSide))) {
                ItemCapsule capsule = (ItemCapsule) fromItem.getItem();
                FluidStack fluidStack = capsule.getFluidStackFromItemStack(fromItem);
                if (fluidStack != null) {
                    int capacity = fluidStack.amount;
                    if (fromItem.hasTagCompound()) fluidStack.tag = fromItem.getTagCompound();

                    if (to.canFill(toDirection, fluidStack.getFluid())) {
                        if (fromItem.isStackable()) {
                            int result = to.fill(toDirection, fluidStack, false);

                            if (result >= capacity) {
                                int maxMove = Math.max(Math.min(result / capacity, Math.min(maxTransfer, fromItem.stackSize)), 0);
                                fluidStack.amount = maxMove * capacity;
                                result = to.fill(toDirection, fluidStack, false);

                                if (result >= maxMove * capacity) {
                                    to.fill(toDirection, fluidStack, true);

                                    maxTransfer -= maxMove;
                                    fromItem.stackSize -= maxMove;
                                    if (fromItem.stackSize == 0) from.setInventorySlotContents(fromSlot, null);
                                    if (maxTransfer == 0) return 0;
                                    if (fromItem.stackSize == 0) break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (oldTransfer != maxTransfer) {
            ((TileEntity) to).markDirty();
            from.markDirty();
        }

        return maxTransfer;
    }

    public static int insertToTank(TileEntity from, int[] fromSlots, UtilDirection front, UtilDirection side, int maxTransfer) {
        ForgeDirection direction = ForgeDirection.getOrientation(front.getSide(side).ordinal());
        return insertToTank(from, fromSlots, direction, maxTransfer);
    }

    public static int extractFromTank(TileEntity toTe, int[] toSlots, ForgeDirection direction, int maxTransfer) {
        int oldTransfer = maxTransfer;

        int toSide = direction.ordinal();
        int fromSide = direction.getOpposite().ordinal();
        ForgeDirection fromDirection = direction.getOpposite();

        World world = toTe.getWorldObj();
        IInventory to = (IInventory) toTe;
        ISidedInventory toSided = (to instanceof ISidedInventory) ? (ISidedInventory) to : null;

        TileEntity te = UtilDirection.getTileEntity(world, toTe.xCoord, toTe.yCoord, toTe.zCoord, direction);
        if (!(te instanceof IFluidHandler)) return 0;
        IFluidHandler from = (IFluidHandler) te;

        FluidTankInfo[] infos = from.getTankInfo(fromDirection);
        if (infos == null) return 0;

        for (FluidTankInfo info : infos) {
            FluidStack fluidStack = info.fluid;
            if (fluidStack != null) {
                Fluid fluid = fluidStack.getFluid();
                int fluidId = UtilFluid.getFluidID(fluidStack);

                if (from.canDrain(direction.getOpposite(), fluid)) {
                    FluidStack toSimulate = fluidStack.copy();
                    toSimulate.amount = Integer.MAX_VALUE;

                    FluidStack result = from.drain(fromDirection, toSimulate, false);
                    int amount = (result == null) ? fluidStack.amount : result.amount;

                    for (int i = 0; i < capsules.length; i++) {
                        if (amount >= capacities[i]) {
                            ItemStack fromItem = new ItemStack(capsules[i], 1, fluidId);
                            if (fluidStack.tag != null) fromItem.setTagCompound((NBTTagCompound) fluidStack.tag.copy());
                            for (int toSlot : toSlots) {
                                ItemStack toItem = to.getStackInSlot(toSlot);
                                if (toItem != null && toItem.stackSize > 0
                                        && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide))
                                        && fromItem.isItemEqual(toItem) && ItemStack.areItemStackTagsEqual(toItem, fromItem)) {
                                    int stackLimit = (to instanceof IInventoryFlexibleStackLimit) ? ((IInventoryFlexibleStackLimit) to).getInventoryStackLimit(toSlot) : to.getInventoryStackLimit();
                                    int maxSize = Math.min(toItem.getMaxStackSize(), stackLimit);
                                    int maxMove = Math.max(Math.min(maxSize - toItem.stackSize, Math.min(maxTransfer, amount / capacities[i])), 0);

                                    toSimulate.amount = maxMove * capacities[i];
                                    result = from.drain(fromDirection, toSimulate, false);
                                    if (result != null && result.amount >= maxMove * capacities[i]) {
                                        from.drain(fromDirection, result, true);

                                        toItem.stackSize += maxMove;
                                        maxTransfer -= maxMove;
                                        amount -= maxMove * capacities[i];
                                        if (maxTransfer == 0) return 0;
                                        if (amount < capacities[i]) break;
                                    }
                                }
                            }

                            if (amount >= capacities[i]) {
                                for (int toSlot : toSlots) {
                                    ItemStack toItem = to.getStackInSlot(toSlot);

                                    if (toItem == null && to.isItemValidForSlot(toSlot, fromItem)
                                            && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide))) {
                                        toItem = fromItem.copy();

                                        int stackLimit = (to instanceof IInventoryFlexibleStackLimit) ? ((IInventoryFlexibleStackLimit) to).getInventoryStackLimit(toSlot) : to.getInventoryStackLimit();
                                        int stackSize = Math.min(Math.min(maxTransfer, amount / capacities[i]), stackLimit);

                                        toSimulate.amount = stackSize * capacities[i];
                                        result = from.drain(fromDirection, toSimulate, false);
                                        if (result != null && result.amount >= stackSize * capacities[i]) {
                                            from.drain(fromDirection, result, true);

                                            toItem.stackSize = stackSize;
                                            to.setInventorySlotContents(toSlot, toItem);
                                            maxTransfer -= stackSize;
                                            amount -= stackSize * capacities[i];

                                            if (maxTransfer == 0) return 0;
                                            if (amount < capacities[i]) break;
                                        }
                                    }
                                }
                            }
                        }

                        if (oldTransfer != maxTransfer) {
                            to.markDirty();
                            ((TileEntity) from).markDirty();
                        }
                    }
                }
            }
        }
        return maxTransfer;
    }

    public static int extractFromTank(TileEntity to, int[] toSlots, UtilDirection front, UtilDirection side, int maxTransfer) {
        ForgeDirection direction = ForgeDirection.getOrientation(front.getSide(side).ordinal());
        return extractFromTank(to, toSlots, direction, maxTransfer);
    }

    private static ItemCapsule[] aCapsules = CItems.itemsCapsule;

    private static Map<ItemCapsule, Integer> capacityMap;
    private static Map<Integer, ItemCapsule> capsuleMap;
    private static ItemCapsule[] sortedCapsules;
    private static int[] sortedCapacities;
    private static int[] sortedStackLimits;

    public static void initFluidParams() {
        capacityMap = new HashMap<ItemCapsule, Integer>();
        capsuleMap = new HashMap<Integer, ItemCapsule>();

        int[] capacities = new int[aCapsules.length];
        int i;
        for (i = 0; i < aCapsules.length; i++) {
            capacities[i] = aCapsules[i].getCapacity();
            capacityMap.put(aCapsules[i], capacities[i]);
            capsuleMap.put(capacities[i], aCapsules[i]);
        }

        Arrays.sort(capacities);
        sortedCapacities = new int[capacities.length];

        for (i = 0; i < sortedCapacities.length; i++) {
            sortedCapacities[i] = capacities[sortedCapacities.length - 1 - i];
        }

        sortedCapsules = new ItemCapsule[sortedCapacities.length];
        sortedStackLimits = new int[sortedCapacities.length];

        for (i = 0; i < sortedCapsules.length; i++) {
            sortedCapsules[i] = capsuleMap.get(sortedCapacities[i]);
            sortedStackLimits[i] = sortedCapsules[i].getItemStackLimit();
        }
    }

    public static void sortFluid(ItemStack[] inventory, int[] slots) {
        if (capacityMap == null) {
            initFluidParams();
        }

        List<Integer> emptySlots = new ArrayList<Integer>();
        Map<CapsuleKey, List<Integer>> capsuleSlots = new HashMap<CapsuleKey, List<Integer>>();
        Map<CapsuleKey, Integer> amounts = new HashMap<CapsuleKey, Integer>();

        boolean slotsIsNull = (slots == null);
        int iMax = slotsIsNull ? inventory.length : slots.length;
        ItemStack itemstack;
        int slot;
        Item item;

        for (int i = 0; i < iMax; i++) {
            if (slotsIsNull) {
                slot = i;
                itemstack = inventory[i];
            } else {
                slot = slots[i];
                itemstack = inventory[slot];
            }

            if (itemstack == null) {
                emptySlots.add(slot);
            } else {
                item = itemstack.getItem();
                if (item instanceof ItemCapsule) {
                    CapsuleKey key = new CapsuleKey(itemstack);
                    List<Integer> list = capsuleSlots.get(key);
                    int amount = 0;
                    if (list == null) {
                        list = new ArrayList<Integer>();
                        capsuleSlots.put(key, list);
                    } else {
                        amount = amounts.get(key);
                    }
                    list.add(slot);
                    amount += itemstack.stackSize * capacityMap.get(item);
                    amounts.put(key, amount);
                }
            }
        }

        for (Map.Entry<CapsuleKey, List<Integer>> entry : capsuleSlots.entrySet()) {
            CapsuleKey key = entry.getKey();
            int amount = amounts.get(key);
            List<Integer> slots1 = entry.getValue();
            int[] stackSizes = new int[sortedCapsules.length];
            int stackNum = 0;

            for (int j = 0; j < sortedCapsules.length; j++) {
                stackSizes[j] = amount / sortedCapacities[j];
                amount -= stackSizes[j] * sortedCapacities[j];
                stackNum += (stackSizes[j] + sortedStackLimits[j] - 1) / sortedStackLimits[j];
            }

            if (stackNum > slots1.size() + emptySlots.size()) continue;

            boolean tagIsNull = (key.tag == null);
            int slotsSize = slots1.size(), emptySlotsSize = emptySlots.size();
            int slotMax = slotsSize + emptySlotsSize, k = 0;
            List<Integer> toAdd = new ArrayList<Integer>();
            List<Integer> toRemove = new ArrayList<Integer>();
            int m = 0;

            for (int sloti = 0; sloti < slotMax; sloti++) {
                while (m < stackSizes.length && stackSizes[m] == 0) m++;

                if (sloti < slotsSize) {
                    k = slots1.get(sloti);
                } else {
                    k = emptySlots.get(sloti - slotsSize);
                }

                if (m >= stackSizes.length) {
                    inventory[k] = null;
                    if (sloti < slotsSize) toAdd.add(k);
                } else {
                    if (sloti >= slotsSize) toRemove.add(k);

                    if (stackSizes[m] > sortedStackLimits[m]) {
                        inventory[k] = new ItemStack(sortedCapsules[m], sortedStackLimits[m], key.damage);
                        if (!tagIsNull) inventory[k].setTagCompound(key.tag);
                        stackSizes[m] = stackSizes[m] - sortedStackLimits[m];
                    } else {
                        inventory[k] = new ItemStack(sortedCapsules[m], stackSizes[m], key.damage);
                        if (!tagIsNull) inventory[k].setTagCompound(key.tag);
                        m++;
                    }
                }
            }

            emptySlots.addAll(toAdd);
            emptySlots.removeAll(toRemove);
        }
    }

    public static class CapsuleKey {
        int damage = -1;
        NBTTagCompound tag = null;

        public CapsuleKey(ItemStack itemstack) {
            this.damage = itemstack.getItemDamage();
            this.tag = itemstack.hasTagCompound() ? itemstack.getTagCompound() : null;
        }

        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + this.damage;
            result = 31 * result + ((this.tag == null) ? 0 : this.tag.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;

            CapsuleKey other = (CapsuleKey) obj;
            if (this.damage != other.damage) return false;
            if (this.tag == null) {
                return other.tag == null;
            } else return this.tag.equals(other.tag);
        }
    }

    public static int fillFluid(ItemStack[] inventory, int[] slots, FluidStack resource, boolean doFill) {
        if (capacityMap == null) initFluidParams();
        if (resource == null) return 0;

        Fluid resourceFluid = resource.getFluid();
        int resourceFluidId = UtilFluid.getFluidID(resourceFluid);

        List<Integer> emptySlots = new ArrayList<Integer>();
        List<Integer> capsuleSlots = new ArrayList<Integer>();
        int amount = 0;

        boolean slotsIsNull = (slots == null);
        int iMax = slotsIsNull ? inventory.length : slots.length;
        ItemStack itemstack = null;
        int slot = -1;
        Item item = null;
        boolean resourceHasTag = (resource.tag != null);

        for (int j = 0; j < iMax; j++) {
            if (slotsIsNull) {
                slot = j;
                itemstack = inventory[j];
            } else {
                slot = slots[j];
                itemstack = inventory[slot];
            }

            if (itemstack == null) {
                emptySlots.add(slot);
            } else {
                item = itemstack.getItem();
                if (item instanceof ItemCapsule && resourceFluidId == itemstack.getItemDamage()
                        && ((!resourceHasTag && !itemstack.hasTagCompound())
                        || (resourceHasTag && resource.tag.equals(itemstack.getTagCompound())))) {
                    capsuleSlots.add(slot);
                    amount += itemstack.stackSize * capacityMap.get(item);
                }
            }
        }

        int oldAmount = amount;
        amount += resource.amount;

        int[] stackSizes = new int[sortedCapsules.length];
        int stackNum = 0;

        for (int i = 0; i < sortedCapsules.length; i++) {
            stackSizes[i] = amount / sortedCapacities[i];
            int stackN = (stackSizes[i] + sortedStackLimits[i] - 1) / sortedStackLimits[i];

            if (stackN + stackNum > capsuleSlots.size() + emptySlots.size()) {
                stackSizes[i] = (capsuleSlots.size() + emptySlots.size() - stackNum) * sortedStackLimits[i];
                amount -= stackSizes[i] * sortedCapacities[i];
                break;
            }

            amount -= stackSizes[i] * sortedCapacities[i];
            stackNum += (stackSizes[i] + sortedStackLimits[i] - 1) / sortedStackLimits[i];
        }

        if (!doFill) return resource.amount - amount;

        boolean tagIsNull = (resource.tag == null);
        int slotsSize = capsuleSlots.size(), emptySlotsSize = emptySlots.size();
        int slotMax = slotsSize + emptySlotsSize, k = 0;
        int m = 0;

        for (int sloti = 0; sloti < slotMax; sloti++) {
            while (m < stackSizes.length && stackSizes[m] == 0) m++;

            if (sloti < slotsSize) k = capsuleSlots.get(sloti);
            else k = emptySlots.get(sloti - slotsSize);

            if (m >= stackSizes.length) inventory[k] = null;
            else if (stackSizes[m] > sortedStackLimits[m]) {
                inventory[k] = new ItemStack(sortedCapsules[m], sortedStackLimits[m], resourceFluidId);
                if (!tagIsNull) inventory[k].setTagCompound(resource.tag);
                stackSizes[m] = stackSizes[m] - sortedStackLimits[m];
            } else {
                inventory[k] = new ItemStack(sortedCapsules[m], stackSizes[m], resourceFluidId);
                if (!tagIsNull) inventory[k].setTagCompound(resource.tag);
                m++;
            }
        }

        return resource.amount - amount;
    }

    public static FluidStack drainFluid(ItemStack[] inventory, int[] slots, FluidStack resource, boolean doDrain) {
        if (resource == null) {
            return null;
        }

        Fluid resourceFluid = resource.getFluid();
        int resourceFluidId = UtilFluid.getFluidID(resourceFluid);

        FluidStack ret = resource.copy();

        List<Integer> emptySlots = new ArrayList<Integer>();
        List<Integer> capsuleSlots = new ArrayList<Integer>();
        int amount = 0;
        boolean resourceHasTag = (resource.tag != null);

        boolean slotsIsNull = (slots == null);
        int iMax = slotsIsNull ? inventory.length : slots.length;
        ItemStack itemstack;
        int slot = -1;
        Item item;

        for (int j = 0; j < iMax; j++) {
            if (slotsIsNull) {
                slot = j;
                itemstack = inventory[j];
            } else {
                slot = slots[j];
                itemstack = inventory[slot];
            }

            if (itemstack == null) {
                emptySlots.add(slot);
            } else {
                item = itemstack.getItem();
                if (item instanceof ItemCapsule && resourceFluidId == itemstack.getItemDamage()
                        && ((!resourceHasTag && !itemstack.hasTagCompound())
                        || (resourceHasTag && resource.tag.equals(itemstack.getTagCompound())))) {
                    capsuleSlots.add(slot);
                    amount += itemstack.stackSize * capacityMap.get(item);
                }
            }
        }

        int oldAmount = amount;
        ret.amount = Math.min(resource.amount, oldAmount);
        amount -= ret.amount;

        int[] stackSizes = new int[sortedCapsules.length];
        int stackNum = 0;
        for (int i = 0; i < sortedCapsules.length; i++) {
            stackSizes[i] = amount / sortedCapacities[i];
            int stackN = (stackSizes[i] + sortedStackLimits[i] - 1) / sortedStackLimits[i];
            if (stackN + stackNum > capsuleSlots.size() + emptySlots.size()) {
                return null;
            }

            amount -= stackSizes[i] * sortedCapacities[i];
            stackNum += (stackSizes[i] + sortedStackLimits[i] - 1) / sortedStackLimits[i];
        }

        if (!doDrain) return ret;

        int slotsSize = capsuleSlots.size(), emptySlotsSize = emptySlots.size();
        int slotMax = slotsSize + emptySlotsSize, k = 0;
        int m = 0;

        for (int sloti = 0; sloti < slotMax; sloti++) {
            while (m < stackSizes.length && stackSizes[m] == 0) m++;
            if (sloti < slotsSize) k = capsuleSlots.get(sloti);
            else k = emptySlots.get(sloti - slotsSize);

            if (m >= stackSizes.length) inventory[k] = null;
            else if (stackSizes[m] > sortedStackLimits[m]) {
                inventory[k] = new ItemStack(sortedCapsules[m], sortedStackLimits[m], resourceFluidId);
                if (resourceHasTag) inventory[k].setTagCompound(resource.tag);
                stackSizes[m] = stackSizes[m] - sortedStackLimits[m];
            } else {
                inventory[k] = new ItemStack(sortedCapsules[m], stackSizes[m], resourceFluidId);
                if (resourceHasTag) inventory[k].setTagCompound(resource.tag);
                m++;
            }
        }

        return ret;
    }

    public static FluidStack drainFluid(ItemStack[] inventory, int[] slots, int maxDrain, boolean doDrain) {
        if (capacityMap == null) initFluidParams();
        if (maxDrain == 0) return null;

        Fluid resourceFluid = null;
        int resourceFluidId = -1;

        FluidStack ret = null;

        List<Integer> emptySlots = new ArrayList<Integer>();
        List<Integer> capsuleSlots = new ArrayList<Integer>();
        int amount = 0;
        boolean resourceHasTag = false;
        boolean slotsIsNull = (slots == null);
        int iMax = slotsIsNull ? inventory.length : slots.length;
        ItemStack itemstack;
        int slot = -1;
        Item item;

        for (int j = 0; j < iMax; j++) {
            if (slotsIsNull) {
                slot = j;
                itemstack = inventory[j];
            } else {
                slot = slots[j];
                itemstack = inventory[slot];
            }
            if (itemstack == null) emptySlots.add(slot);
            else {
                item = itemstack.getItem();
                if (item instanceof ItemCapsule) {
                    if (ret == null) {
                        resourceFluidId = itemstack.getItemDamage();
                        resourceFluid = UtilFluid.getFluid(resourceFluidId);
                        if (resourceFluid != null) {
                            ret = new FluidStack(resourceFluid, maxDrain);
                            resourceHasTag = itemstack.hasTagCompound();
                            if (resourceHasTag) ret.tag = itemstack.getTagCompound();
                        } else {
                            resourceFluidId = -1;
                        }
                    }

                    if (ret != null && resourceFluidId == itemstack.getItemDamage()
                            && ((!resourceHasTag && !itemstack.hasTagCompound())
                            || (resourceHasTag && ret.tag.equals(itemstack.getTagCompound())))) {
                        capsuleSlots.add(slot);
                        amount += itemstack.stackSize * capacityMap.get(item);
                    }
                }
            }
        }

        if (ret == null) return null;

        int oldAmount = amount;
        ret.amount = Math.min(maxDrain, oldAmount);
        amount -= ret.amount;

        int[] stackSizes = new int[sortedCapsules.length];
        int stackNum = 0;

        for (int i = 0; i < sortedCapsules.length; i++) {
            stackSizes[i] = amount / sortedCapacities[i];
            int stackN = (stackSizes[i] + sortedStackLimits[i] - 1) / sortedStackLimits[i];
            if (stackN + stackNum > capsuleSlots.size() + emptySlots.size()) return null;

            amount -= stackSizes[i] * sortedCapacities[i];
            stackNum += (stackSizes[i] + sortedStackLimits[i] - 1) / sortedStackLimits[i];
        }

        if (!doDrain) return ret;

        int slotsSize = capsuleSlots.size(), emptySlotsSize = emptySlots.size();
        int slotMax = slotsSize + emptySlotsSize, k = 0;
        int m = 0;

        for (int sloti = 0; sloti < slotMax; sloti++) {
            while (m < stackSizes.length && stackSizes[m] == 0) m++;

            if (sloti < slotsSize) k = capsuleSlots.get(sloti);
            else k = emptySlots.get(sloti - slotsSize);

            if (m >= stackSizes.length) inventory[k] = null;
            else if (stackSizes[m] > sortedStackLimits[m]) {
                inventory[k] = new ItemStack(sortedCapsules[m], sortedStackLimits[m], resourceFluidId);
                if (resourceHasTag) inventory[k].setTagCompound(ret.tag);
                stackSizes[m] = stackSizes[m] - sortedStackLimits[m];
            } else {
                inventory[k] = new ItemStack(sortedCapsules[m], stackSizes[m], resourceFluidId);
                if (resourceHasTag) inventory[k].setTagCompound(ret.tag);
                m++;
            }
        }

        return ret;
    }

    public static boolean canFillFluid(ItemStack[] inventory, int[] slots, Fluid fluid) {
        if (capacityMap == null) initFluidParams();
        if (fluid == null) return false;
        int amount = 0;
        int slotNum = 0;
        int emptySlotNum = 0;
        int fluidId = UtilFluid.getFluidID(fluid);

        boolean slotsIsNull = (slots == null);
        int iMax = slotsIsNull ? inventory.length : slots.length;
        ItemStack itemstack;
        int slot = -1;
        Item item;
        for (int i = 0; i < iMax; i++) {
            if (slotsIsNull) {
                slot = i;
                itemstack = inventory[i];
            } else {
                slot = slots[i];
                itemstack = inventory[slot];
            }

            if (itemstack == null) emptySlotNum++;
            else {
                item = itemstack.getItem();
                if (item instanceof ItemCapsule && itemstack.getItemDamage() == fluidId) {
                    amount += itemstack.stackSize * capacityMap.get(item);
                    slotNum++;
                }
            }
        }

        return (amount <= (slotNum + emptySlotNum) * sortedCapacities[0] * sortedStackLimits[0]);
    }

    public static boolean canDrainFluid(ItemStack[] inventory, int[] slots, Fluid fluid) {
        if (capacityMap == null) initFluidParams();
        if (fluid == null) return false;

        int fluidId = UtilFluid.getFluidID(fluid);
        boolean slotsIsNull = (slots == null);
        int iMax = slotsIsNull ? inventory.length : slots.length;
        ItemStack itemstack = null;
        int slot = -1;

        for (int i = 0; i < iMax; i++) {
            if (slotsIsNull) {
                slot = i;
                itemstack = inventory[i];
            } else {
                slot = slots[i];
                itemstack = inventory[slot];
            }

            if (itemstack != null && itemstack.getItem() instanceof ItemCapsule && itemstack.getItemDamage() == fluidId) {
                return true;
            }
        }

        return false;
    }

    public static FluidTankInfo[] getTankInfo(ItemStack[] inventory, int[] slots) {
        if (capacityMap == null) {
            initFluidParams();
        }

        Map<CapsuleKey, Integer> amounts = new HashMap<CapsuleKey, Integer>();
        Map<CapsuleKey, Integer> slotNums = new HashMap<CapsuleKey, Integer>();
        int emptySlotNum = 0;
        boolean slotsIsNull = (slots == null);
        int iMax = slotsIsNull ? inventory.length : slots.length;
        ItemStack itemstack = null;
        int slot = -1;
        Item item = null;

        for (int j = 0; j < iMax; j++) {
            if (slotsIsNull) {
                slot = j;
                itemstack = inventory[j];
            } else {
                slot = slots[j];
                itemstack = inventory[slot];
            }
            if (itemstack == null) emptySlotNum++;
            else {
                item = itemstack.getItem();
                if (item instanceof ItemCapsule) {
                    CapsuleKey key = new CapsuleKey(itemstack);
                    if (UtilFluid.getFluid(key.damage) != null) {
                        int amount = 0, slotNum = 0;
                        if (amounts.containsKey(key)) {
                            amount = amounts.get(key);
                            slotNum = slotNums.get(key);
                        }

                        amount += itemstack.stackSize * capacityMap.get(item);
                        amounts.put(key, amount);
                        slotNum++;
                        slotNums.put(key, slotNum);
                    }
                }
            }
        }

        FluidTankInfo[] ret = null;
        if (amounts.size() == 0) {
            ret = new FluidTankInfo[1];
            ret[0] = new FluidTankInfo(null, emptySlotNum * sortedCapacities[0] * sortedStackLimits[0]);
            return ret;
        }

        ret = new FluidTankInfo[amounts.size()];
        int i = 0;

        for (Map.Entry<CapsuleKey, Integer> entry : amounts.entrySet()) {
            CapsuleKey key = entry.getKey();
            int amount = entry.getValue();
            FluidStack fluid = new FluidStack(UtilFluid.getFluid(key.damage), amount);
            if (key.tag != null) fluid.tag = key.tag;
            ret[i] = new FluidTankInfo(fluid, (slotNums.get(key) + emptySlotNum) * sortedCapacities[0] * sortedStackLimits[0]);
            i++;
        }

        return ret;
    }
}
