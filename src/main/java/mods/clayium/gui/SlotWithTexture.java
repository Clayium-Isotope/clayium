package mods.clayium.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotWithTexture extends Slot {
    private final ITexture texture;
    private boolean restricted = false;
    protected Container listener = null;

    public SlotWithTexture(IInventory inventoryIn, int indexIn, int xPos, int yPos) {
        this(inventoryIn, indexIn, xPos, yPos, RectangleTexture.SmallSlotTexture);
    }

    public SlotWithTexture(IInventory inventoryIn, int indexIn, int xPos, int yPos, ITexture texture) {
        super(inventoryIn, indexIn, xPos, yPos);
        this.texture = texture;
    }

    public SlotWithTexture(IInventory inventoryIn, int indexIn, int xPos, int yPos, Container listener) {
        this(inventoryIn, indexIn, xPos, yPos, listener, RectangleTexture.SmallSlotTexture);
    }

    public SlotWithTexture(IInventory inventoryIn, int indexIn, int xPos, int yPos, Container listener, ITexture texture) {
        this(inventoryIn, indexIn, xPos, yPos, texture);
        this.listener = listener;
    }

    @SideOnly(Side.CLIENT)
    public void draw(GuiScreen gui, int offsetX, int offsetY) {
        this.texture.draw(gui, offsetX + this.xPos - (this.texture.getSizeX() - 16) / 2, offsetY + this.yPos - (this.texture.getSizeY() - 16) / 2);
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return !this.restricted || this.inventory.isItemValidForSlot(this.getSlotIndex(), itemstack);
    }

    public void setRestricted() {
        this.restricted = true;
    }

    @Override
    public void onSlotChanged() {
        if (this.listener != null) {
            this.listener.onCraftMatrixChanged(this.inventory);
        }

        super.onSlotChanged();
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.listener != null) {
            this.listener.onCraftMatrixChanged(this.inventory);
        }

        return super.decrStackSize(amount);
    }
}
