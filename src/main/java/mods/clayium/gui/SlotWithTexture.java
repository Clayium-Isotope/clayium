package mods.clayium.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWithTexture extends Slot {
    private ITexture texture;
    protected Container listener = null;
    private boolean restricted = false;

    public SlotWithTexture(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        this(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_, RectangleTexture.SmallSlotTexture);
    }

    public SlotWithTexture(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ITexture texture) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        this.texture = texture;
    }

    public SlotWithTexture(Container listener, IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        this(listener, p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_, RectangleTexture.SmallSlotTexture);
    }


    public SlotWithTexture(Container listener, IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ITexture texture) {
        this(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_, texture);
        this.listener = listener;
    }

    @SideOnly(Side.CLIENT)
    public void draw(GuiScreen gui, int offsetX, int offsetY) {
        this.texture.draw((Gui) gui, offsetX + this.xDisplayPosition - (this.texture.getSizeX() - 16) / 2, offsetY + this.yDisplayPosition - (this.texture.getSizeY() - 16) / 2);
    }


    public boolean isItemValid(ItemStack itemstack) {
        return this.restricted ? this.inventory.isItemValidForSlot(getSlotIndex(), itemstack) : true;
    }

    public void setRestricted() {
        this.restricted = true;
    }


    public void onSlotChanged() {
        if (this.listener != null)
            this.listener.onCraftMatrixChanged(this.inventory);
        super.onSlotChanged();
    }


    public ItemStack decrStackSize(int p_75209_1_) {
        if (this.listener != null)
            this.listener.onCraftMatrixChanged(this.inventory);
        return super.decrStackSize(p_75209_1_);
    }
}
