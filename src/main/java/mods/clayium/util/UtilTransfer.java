package mods.clayium.util;

import mods.clayium.machine.common.IClayInventory;
import mods.clayium.util.crafting.AmountedIngredient;
import mods.clayium.util.exception.IncompleteItemHandlingException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UtilTransfer {
    private static final IInventorySelector defaultSelector = new InventorySelector();

    /**
     * Transfer item(s) from a tile to another tile.
     *
     * @param from        wants to carry out
     * @param to          wants to carry in
     * @param maxTransfer the operation stops when transferred-items' count became greater than this.
     */
    public static int transfer(IItemHandler from, IItemHandler to, int maxTransfer) {
        int transferred = maxTransfer;

        int fromSize = from.getSlots();
        for (int outIndex = 0; outIndex < fromSize && transferred > 0; outIndex++) {
            ItemStack luggage = from.extractItem(outIndex, transferred, false);
            ItemStack rest = ItemHandlerHelper.insertItemStacked(to, luggage, false);
            from.insertItem(outIndex, rest, false);
            transferred = transferred - luggage.getCount() + rest.getCount();
        }

        return transferred;
    }

    public static int insert(TileEntity from, int[] fromSlots, EnumFacing direction, int maxTransfer, IInventorySelector selector) {
        TileEntity to = from.getWorld().getTileEntity(from.getPos().offset(direction));
        if (!(from instanceof IInventory) || !(to instanceof IInventory)) return maxTransfer;

        return transfer(UtilTransfer.getItemHandler(from, direction, fromSlots), UtilTransfer.getItemHandler(to, direction.getOpposite(), null), maxTransfer);
    }

    public static int insert(TileEntity from, int[] fromSlots, EnumFacing direction, int maxTransfer) {
        return insert(from, fromSlots, direction, maxTransfer, defaultSelector);
    }

    public static int extract(TileEntity to, int[] toSlots, EnumFacing direction, int maxTransfer, IInventorySelector selector) {
        TileEntity from = to.getWorld().getTileEntity(to.getPos().offset(direction));
        if (!(to instanceof IInventory) || !(from instanceof IInventory)) return maxTransfer;

        return transfer(UtilTransfer.getItemHandler(from, direction, null), UtilTransfer.getItemHandler(to, direction.getOpposite(), toSlots), maxTransfer);
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

    /**
     * @param itemstack won't be changed.
     * @return the rest of itemstack
     */
    public static ItemStack produceItemStack(ItemStack itemstack, List<ItemStack> inventory, int index, int inventoryStackLimit) {
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack res = itemstack.copy();
        if (inventory.get(index).isEmpty()) {
            inventory.set(index, res.splitStack(Math.min(res.getCount(), inventoryStackLimit)));
        } else if (UtilItemStack.areItemDamageTagEqual(inventory.get(index), res)) {
            int a = Math.min(res.getCount(), inventory.get(index).getMaxStackSize() - inventory.get(index).getCount());
            inventory.get(index).grow(a);
            res.shrink(a);
        }

        if (res.getCount() <= 0) {
            res = ItemStack.EMPTY;
        }

        return res;
    }

    /**
     * @param itemstack won't be changed.
     */
    public static ItemStack produceItemStack(ItemStack itemstack, List<ItemStack> inventory, int i, int j, int inventoryStackLimit) {
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack res = itemstack.copy();

        for (int k = i; k < j; ++k) {
            res = produceItemStack(res, inventory, k, inventoryStackLimit);
        }

        return res;
    }

    /**
     * <B>inventory</B> の {@code [startIncl, endExcl)} の範囲に、できるだけ <B>itemstacks</B> を詰める。<br>
     * <B>itemstacks</B> は内部でコピーがおこなわれて変更されない。変更後の値は、返り値を参照のこと
     */
    public static List<ItemStack> produceItemStacks(List<ItemStack> itemstacks, List<ItemStack> inventory, int startIncl, int endExcl, int inventoryStackLimit) {
        if (itemstacks.isEmpty()) {
            return Collections.emptyList();
        }

        List<ItemStack> copiedItemstacks = getHardCopy(itemstacks);
        copiedItemstacks.replaceAll(itemstack -> produceItemStack(itemstack, inventory, startIncl, endExcl, inventoryStackLimit));

        return copiedItemstacks;
    }

    public static List<ItemStack> produceItemStacks(List<ItemStack> itemstacks, IItemHandler inventory) {
        if (itemstacks.isEmpty()) {
            return Collections.emptyList();
        }

        List<ItemStack> copiedItemstacks = getHardCopy(itemstacks);
        copiedItemstacks.replaceAll(itemstack -> ItemHandlerHelper.insertItemStacked(inventory, itemstack, false));
        return copiedItemstacks;
    }

    public static List<ItemStack> tryProduceItemStacks(IItemHandler inventory, List<ItemStack> stacks) {
        return stacks.stream()
                .map(stack -> ItemHandlerHelper.insertItemStacked(inventory, stack, false))
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());
    }

    public static UnaryOperator<ItemStack> tryInsertItemStack(IItemHandler inventory) {
        return stack -> ItemHandlerHelper.insertItemStacked(inventory, stack, false);
    }

    /**
     * 変更点: <B>itemstack</B> が {@link ItemStack#EMPTY} のとき、返り値を 0 でなく <B>inventoryStackLimit</B> に。
     *
     * @return <B>inventory</B> が <B>index</B> 番目に <B>itemstack</B> のうち、いくつを挿入できるか
     */
    public static int canProduceItemStack(ItemStack itemstack, List<ItemStack> inventory, int index, int inventoryStackLimit) {
        if (itemstack.isEmpty() || inventory.get(index).isEmpty()) {
            return inventoryStackLimit;
        }

        return UtilItemStack.areItemDamageTagEqual(inventory.get(index), itemstack) ? inventory.get(index).getMaxStackSize() - inventory.get(index).getCount() : 0;
    }

    /**
     * @return <B>inventory</B> が {@code [startIncl, endExcl)} の範囲に <B>itemstack</B> のうち、いくつを挿入できるか
     */
    public static int canProduceItemStack(ItemStack itemstack, List<ItemStack> inventory, int startIncl, int endExcl, int inventoryStackLimit) {
        int rest = 0;

        for (int k = startIncl; k < endExcl; ++k) {
            rest += canProduceItemStack(itemstack, inventory, k, inventoryStackLimit);
        }

        return rest;
    }

    /**
     * <B>inventory</B> の {@code [startIncl, endExcl)} の範囲に <B>itemstacks</B> をすべて入れることができるか調べる。<br>
     * 実際に詰めてみる実装なので、遅い可能性あり
     */
    public static boolean canProduceItemStacks(List<ItemStack> itemstacks, List<ItemStack> inventory, int startIncl, int endExcl, int inventoryStackLimit) {
        return produceItemStacks(itemstacks, getHardCopy(inventory), startIncl, endExcl, inventoryStackLimit).stream().allMatch(ItemStack::isEmpty);
    }

    public static boolean canProduceItemStacks(List<ItemStack> itemstacks, IItemHandler inventory) {
        IItemHandler invCopy = getHardCopy(inventory);

        return itemstacks.stream()
                .map(ItemStack::copy)
                .map(stack -> ItemHandlerHelper.insertItemStacked(invCopy, stack, false))
                .anyMatch(stack -> !stack.isEmpty());
    }

    /**
     * Do deep copy
     * @see <a href="https://stackoverflow.com/a/33507565">Deep Copy Method at Stack Overflow</a>
     */
    public static List<ItemStack> getHardCopy(List<ItemStack> itemstacks) {
        if (itemstacks == null || itemstacks.isEmpty()) {
            return Collections.emptyList();
        }

        return itemstacks.stream().map(ItemStack::copy).collect(Collectors.toList());
    }

    /**
     *  {@link IItemHandler#getStackInSlot} と {@link IItemHandler#extractItem} で制限の掛け方が違う場合などでは、
     *  うまく動作しないバグに遭遇する可能性がある。
     *  複数の {@link ItemStack} をinsert/extractする完全なシミュレーションはできなさそう。
     *  simulate=false で実際にやってみて初めて、本当に成功するか失敗するかが変わるかもしれない。
     */
    public static NonNullList<ItemStack> getStacks(IItemHandler handler) {
        return IntStream.range(0, handler.getSlots())
                .mapToObj(handler::getStackInSlot)
                .map(ItemStack::copy)
                .collect(UtilCollect.toNonNullList());
    }

    public static IItemHandler getHardCopy(IItemHandler handler) {
        return new ItemStackHandler(getStacks(handler));
    }

    /**
     * Sum each stack size of inv [start, end)
     */
    public static int countItemStack(ItemStack itemstack, List<ItemStack> inventory, int start, int end) {
        if (start < 0 || end <= start || end >= inventory.size()) return 0;

        int res = 0;

        for (int k = start; k < end; ++k) {
            if (!inventory.get(k).isEmpty() && UtilItemStack.areItemDamageTagEqual(inventory.get(k), itemstack)) {
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

        if (!inventory.get(index).isEmpty() && UtilItemStack.areItemDamageTagEqual(inventory.get(index), res)) {
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

        for (int k = i; k < j; ++k) {
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

    public static int consumeByIngredient(Ingredient ingredient, int amount, List<ItemStack> inventory, int index) {
        if (ingredient.apply(inventory.get(index))) {
            int n = Math.min(amount, inventory.get(index).getCount());
            amount -= n;
            inventory.get(index).shrink(n);
            if (inventory.get(index).getCount() <= 0) {
                inventory.set(index, ItemStack.EMPTY);
            }
        }

        return amount;
    }

    public static int consumeByIngredient(Ingredient ingredient, List<ItemStack> inventory, int index) {
        return consumeByIngredient(ingredient, (ingredient instanceof AmountedIngredient) ? ((AmountedIngredient) ingredient).getAmount() : 1, inventory, index);
    }

    /**
     * @return 0 or remaining amount
     */
    public static int consumeByIngredient(Ingredient ingredient, List<ItemStack> inventory, int startIncl, int endExcl) {
        int amount = (ingredient instanceof AmountedIngredient) ? ((AmountedIngredient) ingredient).getAmount() : 1;

        for (int i = startIncl; i < endExcl && amount > 0; ++i) {
            amount = consumeByIngredient(ingredient, amount, inventory, i);
        }

        return amount;
    }

    public static List<Integer> consumeByIngredient(List<Ingredient> ingredients, List<ItemStack> inventory, int startIncl, int endExcl) {
        List<Integer> res = new ArrayList<>(ingredients.size());

        for (Ingredient ingredient : ingredients) {
            res.add(consumeByIngredient(ingredient, inventory, startIncl, endExcl));
        }

        return res;
    }

    @Nonnull
    public static int[] findGreedyMatch(boolean[][] applyMatrix, int[] requires, int[] stocks, boolean individually) {
        int invSize = stocks.length;
        int[] quota = new int[invSize];

        for (int j = 0; j < requires.length; j++) {
            int amount = requires[j];

            if (!individually) {
                for (int i = 0; i < invSize && amount > 0; i++) {
                    if (!applyMatrix[j][i] || stocks[i] < amount) continue;

                    int min = Math.min(stocks[i], amount);
                    stocks[i] -= min;
                    amount -= min;
                    quota[i] += min;
                }
            }

            for (int i = 0; i < invSize && amount > 0; i++) {
                if (!applyMatrix[j][i]) continue;

                int min = Math.min(stocks[i], amount);
                stocks[i] -= min;
                amount -= min;
                quota[i] += min;
            }

            if (amount > 0) {
                return new int[0];
            }
        }
        return quota;
    }

    /**
     * @return
     *      基本的な返り値としてのint型配列は、インデックス番目のインベントリを減らす個数を表す。
     *      空なら、失敗したということ。
     */
    @Nonnull
    public static int[] findBestMatch(boolean[][] applyMatrix, int[] requires, int[] stocks, boolean individually) {
        int ingSize = requires.length;
        int invSize = stocks.length;
        if (ingSize == 0) {
            int[] ret = new int[invSize];
            Arrays.fill(ret, 0);
            return ret;
        }
        if (invSize == 0) return new int[0];

        // Arrays.copyOf で配列の末尾を切りながらコピーができるので、末尾から処理する。

        int lastIngIndex = requires.length - 1;
        boolean[] applyCol = applyMatrix[lastIngIndex];
        int requirement = requires[lastIngIndex];

        if (!individually) {
            for (int i = 0; i < invSize; i++) {
                if (!applyCol[i] || stocks[i] < requirement) continue;

                stocks[i] -= requirement;

                int[] subMat = findBestMatch(applyMatrix, Arrays.copyOf(requires, lastIngIndex), stocks, false);
                if (subMat.length == 0) {
                    stocks[i] += requirement;
                    continue;
                }

                subMat[i] += requirement;
                return subMat;
            }
        }

        int[] validStocks = new int[invSize];
        for (int i = 0; i < invSize; i++) {
            validStocks[i] = applyCol[i] ? stocks[i] : 0;
        }

        List<int[]> combinations = new ArrayList<>(invSize * requirement);
        findCombinationsHelper(validStocks, requirement, 0, new int[invSize], combinations);

        for (int[] combination : combinations) {
            assert combination.length == invSize;
            for (int i = 0; i < invSize; i++) {
                stocks[i] -= combination[i];
            }

            int[] subMat = findBestMatch(applyMatrix, Arrays.copyOf(requires, lastIngIndex), stocks, individually);
            if (subMat.length == 0) {
                for (int i = 0; i < invSize; i++) {
                    stocks[i] += combination[i];
                }
                continue;
            }

            for (int i = 0; i < invSize; i++) {
                subMat[i] += combination[i];
            }
            return subMat;
        }

        return new int[0];
    }

    /**
     * Generated by ChatGPT-4o.
     * prompt: int[]型引数 maxs および、int型引数 sum をとり、0<=x_i<=maxs[i] かつ x_0 + x_1 + ... + x_i + ... + x_{maxs.length - 1} = sum となる組を求めるjavaのプログラムを書けますか？
     */
    public static void findCombinationsHelper(int[] maxes, int remainingSum, int index, int[] currentCombination, List<int[]> combinations) {
        if (index == maxes.length) {
            if (remainingSum == 0) {
                combinations.add(currentCombination.clone());
            }
            return;
        }

        int min = Math.min(maxes[index], remainingSum);
        for (int i = 0; i <= min; i++) {
            currentCombination[index] = i;
            findCombinationsHelper(maxes, remainingSum - i, index + 1, currentCombination, combinations);
        }
    }

    /**
     * @param smartly
     *      {@code false} のときは、greedyに消費する。
     *      インベントリの走査後に消費が終わっていなければ {@link IncompleteItemHandlingException} を投げる。
     *      {@code true} のときは、...
     * @param individually
     *      {@code false} のときは、なるべく1つの {@link ItemStack} だけで消費しようとする。
     */
    public static void consumeByIngredient(List<Ingredient> ingredients, IItemHandler inv, boolean smartly, boolean individually) throws IncompleteItemHandlingException {
        // getStackInSlot が重たい可能性があるので、コピー。
        NonNullList<ItemStack> stacks = getStacks(inv);
        int invSize = stacks.size();
        int ingSize = ingredients.size();
        boolean[][] applyMatrix = new boolean[ingSize][invSize];
        int[] requires = new int[ingSize];

        for (int i = 0; i < ingSize; i++) {
            Ingredient ing = ingredients.get(i);
            if (ing instanceof AmountedIngredient aing) {
                requires[i] = aing.getAmount();
                for (int j = 0; j < invSize; j++) {
                    applyMatrix[i][j] = aing.getIngredient().apply(stacks.get(j));
                }
            } else {
                requires[i] = 1;
                for (int j = 0; j < invSize; j++) {
                    applyMatrix[i][j] = ing.apply(stacks.get(j));
                }
            }
        }
        int[] stocks = stacks.stream().mapToInt(ItemStack::getCount).toArray();

        int[] quota = smartly ? findBestMatch(applyMatrix, requires, stocks, individually) : findGreedyMatch(applyMatrix, requires, stocks, individually);
        if (quota.length == 0) {
            throw new IncompleteItemHandlingException();
        }

        for (int i = 0; i < inv.getSlots(); i++) {
            inv.extractItem(i, quota[i], false);
        }
    }

    // TODO will substitute ItemStackHandler for this
    public static class InventorySelector implements UtilTransfer.IInventorySelector {
        protected IInventory selected = null;

        public InventorySelector() {}

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
                IInventory to = (IInventory) te;
                Block block = world.getBlockState(pos.offset(direction)).getBlock();
                if (block instanceof BlockChest) {
                    IInventory chest = ((BlockChest) block).getContainer(world, pos.offset(direction), true);
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

                    for (int i = 0; i < this.selected.getSizeInventory(); toSlots[i] = i++);
                } else {
                    toSlots = ((ISidedInventory) this.selected).getSlotsForFace(toSide);
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
                IInventory from = (IInventory) te;
                Block block = world.getBlockState(pos.offset(direction)).getBlock();
                if (block instanceof BlockChest) {
                    IInventory chest = ((BlockChest) block).getContainer(world, pos.offset(direction), true);
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

                    for (int i = 0; i < this.selected.getSizeInventory(); fromSlots[i] = i++);
                } else {
                    fromSlots = ((ISidedInventory) this.selected).getSlotsForFace(fromSide);
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

    /**
     * @param ioSlots when null, filled by using {@link UtilTransfer#getSlots}
     */
    public static IItemHandler getItemHandler(TileEntity te, @Nullable EnumFacing facing, @Nullable int[] ioSlots) throws IllegalArgumentException {
        if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
            return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
        }

        if (te instanceof IClayInventory) {
            if (ioSlots == null && facing == null) {
                throw new IllegalArgumentException("[Bug] \"Facing\" is needed when get ItemHandler with \"ioSlots\" null");
            }
            return new OffsettedHandler(new InvWrapper((IInventory) te), Objects.nonNull(ioSlots) ? ioSlots : UtilTransfer.getSlots(te, facing));
        } else if (te instanceof ISidedInventory) {
            if (facing == null) {
                throw new IllegalArgumentException("[Bug] \"Facing\" is needed when get ItemHandler from ISidedInventory");
            }
            return new SidedInvWrapper((ISidedInventory) te, facing);
        } else if (te instanceof IInventory) {
            return new InvWrapper((IInventory) te);
        }

        throw new IllegalArgumentException("The tile entity doesn't implement IInventory");
    }

    public static class OffsettedHandler implements IItemHandler {
        protected final IItemHandler handler;
        protected final int[] ioSlots;

        OffsettedHandler(InvWrapper handler) {
            this(handler, IntStream.range(0, handler.getSlots()).toArray());
        }
        OffsettedHandler(InvWrapper handler, int startIncl, int endExcl) {
            this(handler, IntStream.range(startIncl, endExcl).toArray());
        }

        OffsettedHandler(IItemHandler handler, int[] ioSlots) {
            this.handler = handler;
            this.ioSlots = ioSlots;
        }

        @Override
        public int getSlots() {
            return this.ioSlots.length;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return this.handler.getStackInSlot(this.toAbsoluteSlot(slot));
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return this.handler.insertItem(this.toAbsoluteSlot(slot), stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return this.handler.extractItem(this.toAbsoluteSlot(slot), amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return this.handler.getSlotLimit(this.toAbsoluteSlot(slot));
        }

        protected int toAbsoluteSlot(int slot) {
            return this.ioSlots[slot];
        }
    }
}
