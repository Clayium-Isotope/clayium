package mods.clayium.gui.client;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cpw.mods.fml.client.config.GuiButtonExt;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TilePANCore;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPANCore extends GuiClayEnergyTemp {
    private List<TilePANCore.ItemStackWithEnergy> conversionList;
    private Set<TilePANCore.ItemStackWithEnergy> prohibitedList;
    private Comparator<TilePANCore.ItemStackWithEnergy> comp;
    private ReversibleComparator<TilePANCore.ItemStackWithEnergy>[] comparators;
    protected int slidery;
    protected int sliderymax;
    protected GuiSlider slider;
    protected int offsetX;
    protected int offsetY;
    protected int tableX;
    protected int tableY;
    protected int sliderWidth;
    protected int marginR;
    protected int marginB;
    protected int tableWidth;
    protected int tableHeight;
    protected boolean detail;
    private TilePANCore.ItemStackWithEnergy tooltipStack;

    static class ReversibleComparator<T> implements Comparator<T> {
        private Comparator<T>[] comps;
        private boolean reversed;

        public boolean isReversed() {
            return this.reversed;
        }

        public void setReversed(boolean reversed) {
            this.reversed = reversed;
        }

        ReversibleComparator(Comparator<T>[] comps) {
            this.comps = comps;
        }

        ReversibleComparator(Comparator<T> comp) {
            this.comps = (Comparator<T>[]) new Comparator[] {comp};
        }

        public int compare(T o1, T o2) {
            if (this.comps != null)
                for (Comparator<T> comp : this.comps) {
                    int c = comp.compare(o1, o2);
                    if (c != 0)
                        return this.reversed ? -c : c;
                }
            return 0;
        }
    }

    static class ConsumptionComparator implements Comparator<TilePANCore.ItemStackWithEnergy> {
        public int compare(TilePANCore.ItemStackWithEnergy o1, TilePANCore.ItemStackWithEnergy o2) {
            double d1 = (o1 == null) ? 0.0D : o1.consumption;
            double d2 = (o2 == null) ? 0.0D : o2.consumption;
            if (d1 > d2)
                return 1;
            if (d1 < d2)
                return -1;
            return 0;
        }
    }

    static class CostComparator implements Comparator<TilePANCore.ItemStackWithEnergy> {
        public int compare(TilePANCore.ItemStackWithEnergy o1, TilePANCore.ItemStackWithEnergy o2) {
            double d1 = (o1 == null) ? 0.0D : o1.cost;
            double d2 = (o2 == null) ? 0.0D : o2.cost;
            if (d1 > d2)
                return 1;
            if (d1 < d2)
                return -1;
            return 0;
        }
    }

    public GuiPANCore(ContainerTemp container, TilePANCore tile, Block block) {
        super(container, (TileClayContainer) tile, block);


        this.comp = (Comparator<TilePANCore.ItemStackWithEnergy>) new TilePANCore.ItemStackComparator();
        this.comparators = (ReversibleComparator<TilePANCore.ItemStackWithEnergy>[]) new ReversibleComparator[3];

        this.comparators[0] = new ReversibleComparator<TilePANCore.ItemStackWithEnergy>(this.comp);
        this.comparators[1] = new ReversibleComparator<TilePANCore.ItemStackWithEnergy>(new CostComparator());
        this.comparators[2] = new ReversibleComparator<TilePANCore.ItemStackWithEnergy>(new ConsumptionComparator());


        this.slidery = 0;
        this.sliderymax = 0;


        this.tableX = 11;
        this.tableY = 18;
        this.sliderWidth = 10;
        this.marginR = 11;
        this.marginB = 14;


        this.detail = false;


        this.tooltipStack = null;
    }

    public void addButton() {
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        this.offsetX = (this.width - this.xSize) / 2;
        this.offsetY = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButtonExt(0, this.offsetX + container.machineGuiSizeX - 80 - 6, 5, 12, 12, "I"));
        this.buttonList.add(new GuiButtonExt(1, this.offsetX + container.machineGuiSizeX - 64 - 6, 5, 12, 12, "M"));
        this.buttonList.add(new GuiButtonExt(2, this.offsetX + container.machineGuiSizeX - 48 - 6, 5, 12, 12, "C"));
        this.buttonList.add(new GuiButtonExt(3, this.offsetX + container.machineGuiSizeX - 32 - 4, 5, 30, 12, "Dump"));
        this.tableWidth = container.machineGuiSizeX - this.tableX - this.sliderWidth - this.marginR;
        this.tableHeight = container.machineGuiSizeY - this.tableY - this.marginB;
        this.slider = new GuiSlider(4, this.offsetX + this.tableX + this.tableWidth, this.offsetY + this.tableY, this.sliderWidth, this.tableHeight, false);
        this.buttonList.add(this.slider);
        this.buttonList.add(new GuiButtonExt(5, this.offsetX + this.xSize / 2 - 16, this.offsetY + this.tableY + this.tableHeight + 3, 12, 12, "D"));
    }

    public void addTextField() {
        GuiTextField textField = new GuiTextField(this.fontRendererObj, this.offsetX + this.xSize / 2, this.offsetY + this.tableY + this.tableHeight + 3, this.xSize / 2 - this.marginR, 12);
        textField.setTextColor(-1);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(true);
        textField.setMaxStringLength(128);
        addTextFieldToList(textField);
    }

    public void drawScreen(int mouseX, int mouseZ, float p_73863_3_) {
        super.drawScreen(mouseX, mouseZ, p_73863_3_);
        if (this.tooltipStack != null && this.tooltipStack.itemstack != null) {
            ItemStack stack = this.tooltipStack.itemstack;


            renderToolTipWithAdditionalText(stack, mouseX, mouseZ, " [" + UtilLocale.ClayEnergyNumeral(this.tooltipStack.cost) + "CE : " + UtilLocale.ClayEnergyNumeral(this.tooltipStack.consumption) + "CE ]");
            GL11.glDisable(2896);
            GL11.glEnable(32826);
            GL11.glEnable(2903);
        }
    }

    protected void actionPerformed(GuiButton guibutton) {
        super.actionPerformed(guibutton);
        int id = guibutton.id;
        if (id <= 2 && this.conversionList != null) {
            this.comparators[id].setReversed(!this.comparators[id].isReversed());
            Collections.sort(this.conversionList, this.comparators[id]);
        }
        if (id == 3 && this.conversionList != null) {
            Pattern pattern = null;
            try {
                pattern = Pattern.compile(((GuiTextField) this.textFields.get(0)).getText(), 2);
            } catch (PatternSyntaxException e) { ClayiumCore.logger.error("Illegal Pattern! \n" + e.getMessage()); }
            for (TilePANCore.ItemStackWithEnergy item : this.conversionList) {
                boolean flag = false;
                if (pattern != null && item.itemstack != null) {
                    Matcher matcher = pattern.matcher(item.itemstack.getDisplayName());
                    flag = matcher.find();
                }
                if (item.itemstack != null && flag)
                    this.mc.thePlayer.addChatMessage((IChatComponent) new ChatComponentText(item.toString()));
            }
        }
        if (id == 5)
            this.detail = !this.detail;
    }

    public void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseZ);
        drawRect(this.offsetX + this.tableX - 1, this.offsetY + this.tableY - 1, this.offsetX + this.tableX + this.tableWidth + this.sliderWidth + 1, this.offsetY + this.tableY + this.tableHeight + 1, -16769536);
    }

    protected void drawItemStack(TilePANCore.ItemStackWithEnergy itemstack, int x, int y, int mousex, int mousey) {
        if (itemstack == null || itemstack.itemstack == null)
            return;
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(2896);

        itemRender.zLevel = -10.0F;
        GL11.glEnable(32826);
        GL11.glEnable(2929);

        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack.itemstack, x, y);
        itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack.itemstack, x, y);

        if (func_146978_c(x + 1, y + 1, 14, 14, mousex, mousey)) {
            this.tooltipStack = itemstack;
        }
    }

    public void handleMouseInput() {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i > 0) { this.slider.sliderNormalizedPos -= 1.0F / (this.sliderymax + 1); } else if (i < 0) {
            this.slider.sliderNormalizedPos += 1.0F / (this.sliderymax + 1);
        }
        this.slider.sliderNormalizedPos = Math.min(1.0F, Math.max(this.slider.sliderNormalizedPos, 0.0F));
    }

    public void setItemList(Set<TilePANCore.ItemStackWithEnergy> ingreds, Set<TilePANCore.ItemStackWithEnergy> prohibiteds) {
        if (ingreds != null) {
            this.conversionList = new ArrayList<TilePANCore.ItemStackWithEnergy>();
            this.conversionList.addAll(ingreds);
            Collections.sort(this.conversionList, this.comp);
            this.prohibitedList = prohibiteds;
        }
    }

    public void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        if (this.conversionList != null) {
            this.slidery = Math.min((int) ((this.sliderymax + 1) * this.slider.sliderNormalizedPos), this.sliderymax);
            this.tooltipStack = null;
            int ix = Math.max(this.tableWidth / 16, 1), iy = Math.max(this.tableHeight / 16, 1);
            if (this.detail)
                ix = 1;
            int count = 0;
            Pattern pattern = null;
            try {
                pattern = Pattern.compile(((GuiTextField) this.textFields.get(0)).getText(), 2);
            } catch (PatternSyntaxException e) { ClayiumCore.logger.error("Illegal Pattern! \n" + e.getMessage()); }
            for (TilePANCore.ItemStackWithEnergy item : this.conversionList) {
                boolean flag = false;
                if (pattern != null && item.itemstack != null) {
                    Matcher matcher = pattern.matcher(item.itemstack.getDisplayName());
                    flag = matcher.find();
                }
                if (item.itemstack != null && flag) {
                    int px = count % ix;
                    int py = count / ix;
                    if (py >= this.slidery && py < this.slidery + iy) {
                        int cx = this.tableX + px * 16;
                        int cy = this.tableY + (py - this.slidery) * 16;
                        if (this.prohibitedList.contains(item)) {
                            GL11.glDisable(2896);
                            GL11.glDisable(2929);
                            RenderHelper.disableStandardItemLighting();
                            drawRect(cx, cy, cx + 16, cy + 16, 2026380830);
                            RenderHelper.enableGUIStandardItemLighting();
                            GL11.glEnable(2896);
                            GL11.glEnable(2929);
                        }
                        drawItemStack(item, cx, cy, mouseX, mouseZ);
                        if (this.detail) {
                            this.fontRendererObj.drawString(UtilLocale.ClayEnergyNumeral(item.cost) + "CE", cx + 24, cy + 4, -2302756);
                            this.fontRendererObj.drawString(UtilLocale.ClayEnergyNumeral(item.consumption) + "CE", cx + 24 + (this.tableWidth - cx - 24) / 2 + 6, cy + 4, -986896);
                        }
                    }
                    count++;
                }
            }
            this.sliderymax = Math.max((count - 1) / ix - iy + 1, 0);
        }
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        RenderHelper.enableGUIStandardItemLighting();
    }

    protected void renderToolTipWithAdditionalText(ItemStack p_146285_1_, int p_146285_2_, int p_146285_3_, String string) {
        List<String> list = p_146285_1_.getTooltip((EntityPlayer) this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
        list.add(1, EnumChatFormatting.WHITE + string);

        for (int k = 0; k < list.size(); k++) {

            if (k == 0) {

                list.set(k, (p_146285_1_.getRarity()).rarityColor + (String) list.get(k) + " " + Item.getIdFromItem(p_146285_1_.getItem()) + ((p_146285_1_.getItemDamage() != 0) ? (":" + p_146285_1_.getItemDamage()) : ""));
            } else {

                list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
            }
        }

        FontRenderer font = p_146285_1_.getItem().getFontRenderer(p_146285_1_);
        drawHoveringText(list, p_146285_2_, p_146285_3_, (font == null) ? this.fontRendererObj : font);
    }


    public class GuiSlider
            extends GuiButton {
        public float sliderNormalizedPos = 0.0F;
        protected boolean isDragged = false;
        protected int knobLength = 10;

        protected boolean horizontal = false;

        public GuiSlider(int buttonId, int x, int y, int sizeX, int sizeY, boolean horizontal) {
            super(buttonId, x, y, sizeX, sizeY, "");
            this.horizontal = horizontal;
        }


        public int getHoverState(boolean p_146114_1_) {
            return 0;
        }


        protected void mouseDragged(Minecraft minecraft, int mouseX, int mouseY) {
            if (this.visible) {

                if (this.isDragged) {

                    if (this.horizontal) {
                        this.sliderNormalizedPos = (mouseX - this.xPosition + this.knobLength / 2) / (this.width - this.knobLength);
                    } else {
                        this.sliderNormalizedPos = (mouseY - this.yPosition + this.knobLength / 2) / (this.height - this.knobLength);
                    }

                    if (this.sliderNormalizedPos < 0.0F) {
                        this.sliderNormalizedPos = 0.0F;
                    }

                    if (this.sliderNormalizedPos > 1.0F) {
                        this.sliderNormalizedPos = 1.0F;
                    }
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if (!this.horizontal) {


                    drawRect(this.xPosition + 1, this.yPosition + (int) (this.sliderNormalizedPos * (this.height - this.knobLength)) + 1, this.xPosition + this.width - 1, this.yPosition + (int) (this.sliderNormalizedPos * (this.height - this.knobLength)) + this.knobLength / 2 + this.knobLength / 2 - 1, -14795746);
                }
            }
        }


        public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
            if (this.visible) {

                drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -6232416);
                mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
            }
        }


        public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
            if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_)) {

                this.sliderNormalizedPos = (p_146116_2_ - this.xPosition + 4) / (this.width - 8);

                if (this.sliderNormalizedPos < 0.0F) {
                    this.sliderNormalizedPos = 0.0F;
                }

                if (this.sliderNormalizedPos > 1.0F) {
                    this.sliderNormalizedPos = 1.0F;
                }


                this.isDragged = true;
                return true;
            }


            return false;
        }


        public void mouseReleased(int p_146118_1_, int p_146118_2_) {
            this.isDragged = false;
        }
    }
}
