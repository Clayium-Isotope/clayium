package mods.clayium.machine.StorageContainer;

import mods.clayium.client.render.CustomHull;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.common.FlexibleStackLimit;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@CustomHull(CustomHull.AZ91D)
public class TileEntityStorageContainer extends TileEntityClayContainer
                                        implements FlexibleStackLimit, ICapabilityProvider {

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
        // TODO getValueはO(1)としても、getBlockStateのコストがわからないので、キャッシュしたよさげ？
        return this.getWorld().getBlockState(this.getPos()).getValue(StorageContainerSize.STORAGE_SIZE).maxSize;
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
        if (id == 0) {
            return this.getCurrentStackSize();
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        if (id == 0) {
            this.currentStackSize = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound compound) {
        super.writeMoreToNBT(compound);

        compound.setInteger("ItemStackSize", this.getCurrentStackSize());

        return compound;
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound compound) {
        super.readMoreFromNBT(compound);

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
        if (this.isItemValidForSlot(slot, stack)) return this.getInventoryStackLimit();
        return 0;
    }

    @Override
    public boolean hasSpecialDrops() {
        return true;
    }

    @Override
    public void addSpecialDrops(List<ItemStack> drops) {
        if (this.isEmpty()) return;

        NBTTagCompound stackTag = new NBTTagCompound();
        NBTTagCompound tileTag = this.writeToNBT(new NBTTagCompound());
        stackTag.setTag("TileEntityNBTTag", tileTag);

        drops.add(new ItemStack(Item.getItemFromBlock(ClayiumMachines.storageContainer), 1, 1, stackTag));
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    protected final IItemHandler handler = new StorageContainerItemHandler(this);

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.handler;
        }
        return super.getCapability(capability, facing);
    }

    protected static class StorageContainerItemHandler implements IItemHandler {

        private final TileEntityStorageContainer inv;

        StorageContainerItemHandler(TileEntityStorageContainer tesc) {
            this.inv = tesc;
        }

        @Override
        public int getSlots() {
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return this.inv.getStackInSlot(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            this.inv.putItemStack(slot, stack);
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return this.inv.decrStackSize(slot, amount);
        }

        @Override
        public int getSlotLimit(int slot) {
            return this.inv.getInventoryStackLimit();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return this.inv.isItemValidForSlot(slot, stack);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        this.registerInsertIcons("import");
        this.registerExtractIcons("export");
    }
}
