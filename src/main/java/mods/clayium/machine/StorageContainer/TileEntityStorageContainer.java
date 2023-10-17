package mods.clayium.machine.StorageContainer;

import mods.clayium.block.tile.FlexibleStackLimit;
import mods.clayium.client.render.CustomHull;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.List;

@CustomHull(CustomHull.AZ91D)
public class TileEntityStorageContainer extends TileEntityClayContainer implements FlexibleStackLimit {
    protected StorageContainerSize maxStorageSize = StorageContainerSize.NORMAL;
    protected int currentStackSize = 0;

    @Override
    public void initParams() {
        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.maxStorageSize = StorageContainerSize.NORMAL;
        this.listSlotsImport.clear();
        this.listSlotsExport.clear();

        this.listSlotsImport.add(new int[] { 0 });
        this.listSlotsExport.add(new int[] { 0 });
        this.slotsDrop = new int[0];
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0 && isValidItemstack(stack);
    }

    public boolean isValidItemstack(final ItemStack stack) {
        return this.isEmpty() || UtilItemStack.areItemDamageTagEqual(this.getPrototype(), stack);
    }

    public boolean putItemStack(int index, ItemStack stack) {
        if (!this.isItemValidForSlot(index, stack)) return false;

        if (this.isEmpty()) {
            ItemStack raw = stack.copy();
            raw.setCount(1);
            this.setInventorySlotContents(0, stack.copy());
            this.currentStackSize = 0;
        }

        int diff = Math.min(this.getInventoryStackLimit() - this.getCurrentStackSize(), stack.getCount());
        this.currentStackSize += diff;
        stack.shrink(diff);

        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return this.maxStorageSize.maxSize;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index != 0 || this.isEmpty()) return ItemStack.EMPTY;

        final ItemStack stack = this.getPrototype().copy();
        stack.setCount(Math.min(this.getCurrentStackSize(), stack.getMaxStackSize()));
        return stack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index != 0 || this.isEmpty()) return ItemStack.EMPTY;

        final ItemStack stack = this.getPrototype().copy();
        final int newCount = Math.min(this.getCurrentStackSize(), count);
        this.currentStackSize -= newCount;
        stack.setCount(newCount);
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index != 0) return ItemStack.EMPTY;

        final ItemStack stack = this.getPrototype().copy();
        stack.setCount(Math.min(this.getCurrentStackSize(), stack.getMaxStackSize()));
        this.currentStackSize = 0;
        this.setInventorySlotContents(0, ItemStack.EMPTY);
        return stack;
    }

    public int getCurrentStackSize() {
        if (this.isEmpty()) return 0;
        return this.currentStackSize;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: return this.maxStorageSize.ordinal();
            case 1: return this.getCurrentStackSize();
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: this.maxStorageSize = StorageContainerSize.getByID(value);
            case 1: this.currentStackSize = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound compound) {
        super.writeMoreToNBT(compound);

        compound.setInteger("MaxStorageSizeMeta", this.maxStorageSize.ordinal());
        compound.setInteger("ItemStackSize", this.getCurrentStackSize());

        return compound;
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound compound) {
        super.readMoreFromNBT(compound);

        this.maxStorageSize = StorageContainerSize.getByID(compound.getInteger("MaxStorageSizeMeta"));
        this.currentStackSize = compound.getInteger("ItemStackSize");
    }

    @Override
    public boolean isEmpty() {
        return this.getPrototype().isEmpty();
    }

    public ItemStack getPrototype() {
        return this.getContainerItemStacks().get(0);
    }

    @Override
    public int getInventoryStackLimit(int slot, ItemStack stack) {
        if (this.isItemValidForSlot(slot, stack)) return this.maxStorageSize.maxSize;
        return 0;
    }

    @Override
    public boolean hasSpecialDrops() {
        return true;
    }

    @Override
    public void addSpecialDrops(List<ItemStack> drops) {
        drops.add(new ItemStack(Item.getItemFromBlock(ClayiumMachines.storageContainer), 1, 0, this.writeToNBT(new NBTTagCompound())));
    }
}
