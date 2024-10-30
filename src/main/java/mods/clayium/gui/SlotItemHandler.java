package mods.clayium.gui;

import mods.clayium.machine.common.FlexibleStackLimit;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Sorry for NullPointerException when you access {@link net.minecraft.inventory.Slot#inventory}
 */
public class SlotItemHandler extends SlotWithTexture {

    protected final IItemHandler handler;

    public SlotItemHandler(TileEntity inventory, int indexIn, int xPos, int yPos, ITexture texture) {
        super((IInventory) inventory, indexIn, xPos, yPos, texture);

        if (!inventory.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            throw new IllegalArgumentException(
                    "[SlotItemHandler] argument must have CapabilityItemHandler.ITEM_HANDLER_CAPABILITY for null facing");
        }

        this.handler = inventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    @Override
    public ItemStack getStack() {
        return this.handler.getStackInSlot(this.getSlotIndex());
    }

    @Override
    public void putStack(ItemStack stack) {
        this.handler.insertItem(this.getSlotIndex(), stack, false);
        this.onSlotChanged();
    }

    @Override
    public void onSlotChanged() {}

    @Override
    public int getSlotStackLimit() {
        return this.handler.getSlotLimit(this.getSlotIndex());
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        if (this.inventory instanceof FlexibleStackLimit)
            return ((FlexibleStackLimit) this.inventory).getInventoryStackLimit(this.getSlotIndex(), stack);
        return this.handler.getSlotLimit(this.getSlotIndex());
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return this.handler.extractItem(this.getSlotIndex(), amount, false);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return this.handler.isItemValid(this.getSlotIndex(), itemstack);
    }
}
