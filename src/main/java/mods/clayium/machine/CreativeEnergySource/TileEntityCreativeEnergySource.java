package mods.clayium.machine.CreativeEnergySource;

import mods.clayium.client.render.CustomHull;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IClayInventory;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * [TODO] [GUI]専用のGUI, 流量調節機能, [Container]エネルギー粘土を無に帰す
 */
@CustomHull("blocks/machine/creativeenergy")
public class TileEntityCreativeEnergySource extends TileEntityClayContainer implements ICapabilityProvider {
    protected static final ItemStack oec = IClayEnergy.getCompressedClay(13, 64);

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(1, getEnergeticClay());

        this.listSlotsExport.add(new int[] { 0 });
        this.slotsDrop = new int[0];
        this.setExportRoutes(-1, -1, -1, 0, -1, -1);
    }

    protected void setDefaultTransportation(TierPrefix tier) {
        UtilTier.BufferTransport config = UtilTier.BufferTransport.getByTier(tier);
        if (config != null) {
            this.autoInsertInterval = config.autoInsertInterval;
            this.autoExtractInterval = config.autoExtractInterval;
            this.maxAutoInsertDefault = config.maxAutoInsertDefault;
            this.maxAutoExtractDefault = config.maxAutoExtractDefault;
        }
    }

    public static ItemStack getEnergeticClay() {
        return oec;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return IClayEnergy.hasClayEnergy(itemstack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0 && IClayEnergy.hasClayEnergy(itemStackIn);
    }

    public ItemStack getStackInSlot(int slot) {
        return getEnergeticClay().copy();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                && this.getExportRoute(UtilDirection.getSideOfDirection(this.getFront(), facing)) != IClayInventory.NONE_ROUTE)
                || super.hasCapability(capability, facing);
    }

    protected final IItemHandler handler = new CESourceHandler();

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                && this.getExportRoute(UtilDirection.getSideOfDirection(this.getFront(), facing)) != IClayInventory.NONE_ROUTE) {
            return (T) this.handler;
        }
        return super.getCapability(capability, facing);
    }

    protected static class CESourceHandler implements IItemHandler {
        @Override
        public int getSlots() {
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return oec.copy();
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return this.isItemValid(slot, stack) ? ItemStack.EMPTY : stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != 0) return ItemStack.EMPTY;

            ItemStack copied = oec.copy();
            copied.setCount(amount);
            return copied;
        }

        @Override
        public int getSlotLimit(int slot) {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return slot == 0 && IClayEnergy.hasClayEnergy(stack);
        }
    }

    @Override
    public void registerIOIcons() {
        this.registerExtractIcons("export");
    }
}
