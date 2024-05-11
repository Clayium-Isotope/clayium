package mods.clayium.machine.VacuumContainer;

import mods.clayium.block.tile.FlexibleStackLimit;
import mods.clayium.client.render.CustomHull;
import mods.clayium.item.filter.IFilter;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import net.minecraft.item.ItemStack;
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

@CustomHull(CustomHull.AZ91D)
public class TileEntityVacuumContainer extends TileEntityClayContainer
                                       implements FlexibleStackLimit, ICapabilityProvider {

    protected final IItemHandler handler = new VacuumContainerItemHandler();

    @Override
    public void initParams() {
        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = NonNullList.withSize(2, ItemStack.EMPTY);
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.listSlotsImport.clear();
        this.listSlotsExport.clear();

        this.listSlotsImport.add(new int[] { 0 });
        this.slotsDrop = new int[0];
    }

    @Override
    public int getInventoryStackLimit(int slot, ItemStack stack) {
        if (slot == 0) return Integer.MAX_VALUE;
        if (slot == 1 && IFilter.isFilter(stack)) return stack.getMaxStackSize();
        return 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return Integer.MAX_VALUE;
    }

    public boolean canInsert(ItemStack stack) {
        return IFilter.match(this.getStackInSlot(1), stack);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) handler;
        }
        return super.getCapability(capability, facing);
    }

    class VacuumContainerItemHandler implements IItemHandler {

        VacuumContainerItemHandler() {}

        @Override
        public int getSlots() {
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (slot == 0 && TileEntityVacuumContainer.this.canInsert(stack))
                return ItemStack.EMPTY;
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return slot == 0 && TileEntityVacuumContainer.this.canInsert(stack);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        this.registerInsertIcons("import");
    }
}
