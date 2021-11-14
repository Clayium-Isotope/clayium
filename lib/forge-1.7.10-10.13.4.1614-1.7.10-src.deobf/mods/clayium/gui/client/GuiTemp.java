package mods.clayium.gui.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.network.GuiTextFieldPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiTemp extends GuiContainer {
    protected static ResourceLocation PlayerInventoryTexture = new ResourceLocation("clayium", "textures/gui/playerinventory.png");
    protected static ResourceLocation DefaultBackTexture = new ResourceLocation("clayium", "textures/gui/back.png");
    protected static ResourceLocation ButtonTexture = new ResourceLocation("clayium", "textures/gui/button.png");
    private boolean initTextureSize = false;
    private int tlw;
    private int tlh;
    private int trw;
    private int trh;
    private int blw;
    protected static ResourceLocation DefaultTextureBack = new ResourceLocation("clayium", "textures/gui/gui_back.png");
    private int blh;
    private int brw;
    private int brh;
    private int th;
    private int bh;
    private int lw;
    private int rw;
    protected static ResourceLocation DefaultTextureTop = new ResourceLocation("clayium", "textures/gui/gui_t.png");
    protected static ResourceLocation DefaultTextureBottom = new ResourceLocation("clayium", "textures/gui/gui_b.png");
    protected static ResourceLocation DefaultTextureLeft = new ResourceLocation("clayium", "textures/gui/gui_l.png");
    protected static ResourceLocation DefaultTextureRight = new ResourceLocation("clayium", "textures/gui/gui_r.png");
    protected static ResourceLocation DefaultTextureTopLeft = new ResourceLocation("clayium", "textures/gui/gui_tl.png");
    protected static ResourceLocation DefaultTextureTopRight = new ResourceLocation("clayium", "textures/gui/gui_tr.png");
    protected static ResourceLocation DefaultTextureBottomLeft = new ResourceLocation("clayium", "textures/gui/gui_bl.png");
    protected static ResourceLocation DefaultTextureBottomRight = new ResourceLocation("clayium", "textures/gui/gui_br.png");
    protected static ResourceLocation DefaultTexturePlayer = new ResourceLocation("clayium", "textures/gui/gui_playerinventory.png");

    public ResourceLocation backTexture = null;
    public ResourceLocation overlayTexture = null;


    protected String guiTitle = "";
    protected List<GuiTextField> textFields = new ArrayList<GuiTextField>();
    protected boolean keyEvents = false;

    public GuiTemp(ContainerTemp container) {
        super(container);
        ContainerTemp containerTemp = (ContainerTemp) this.inventorySlots;
        this.xSize = containerTemp.machineGuiSizeX;
        this.ySize = containerTemp.machineGuiSizeY + 94;
        setGuiTitle(container.getInventoryName());
    }

    public void setGuiTitle(String guiTitle) {
        this.guiTitle = guiTitle;
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        if (drawInventoryName())
            this.fontRendererObj.drawString(this.guiTitle, 6, 6, 4210752);
        if (drawPlayerInventoryName())
            this.fontRendererObj.drawString(I18n.format("container.inventory"), container.playerSlotOffsetX + 8, container.playerSlotOffsetY, 4210752);
    }

    private int getTextureWidth() {
        return GL11.glGetTexLevelParameteri(3553, 0, 4096);
    }

    private int getTextureHeight() {
        return GL11.glGetTexLevelParameteri(3553, 0, 4097);
    }

    public void drawTexture(int p_146110_0_, int p_146110_1_, int p_146110_4_, int p_146110_5_) {
        func_146110_a(p_146110_0_, p_146110_1_, 0.0F, 0.0F, p_146110_4_, p_146110_5_, getTextureWidth(), getTextureHeight());
    }

    public boolean drawInventoryName() {
        return ((ContainerTemp) this.inventorySlots).drawInventoryName();
    }

    public boolean drawPlayerInventoryName() {
        return ((ContainerTemp) this.inventorySlots).drawInventoryName();
    }


    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        ContainerTemp container = (ContainerTemp) this.inventorySlots;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int offsetX = (this.width - this.xSize) / 2;
        int offsetY = (this.height - this.ySize) / 2;

        if (this.backTexture == null) {
            if (!this.initTextureSize) {
                this.mc.getTextureManager().bindTexture(DefaultTextureTopLeft);
                this.tlw = getTextureWidth();
                this.tlh = getTextureHeight();
                this.mc.getTextureManager().bindTexture(DefaultTextureTopRight);
                this.trw = getTextureWidth();
                this.trh = getTextureHeight();
                this.mc.getTextureManager().bindTexture(DefaultTextureBottomLeft);
                this.blw = getTextureWidth();
                this.blh = getTextureHeight();
                this.mc.getTextureManager().bindTexture(DefaultTextureBottomRight);
                this.brw = getTextureWidth();
                this.brh = getTextureHeight();

                this.mc.getTextureManager().bindTexture(DefaultTextureTop);
                this.th = getTextureHeight();
                this.mc.getTextureManager().bindTexture(DefaultTextureBottom);
                this.bh = getTextureHeight();

                this.mc.getTextureManager().bindTexture(DefaultTextureLeft);
                this.lw = getTextureWidth();
                this.mc.getTextureManager().bindTexture(DefaultTextureRight);
                this.rw = getTextureWidth();

                this.initTextureSize = true;
            }

            this.mc.getTextureManager().bindTexture(DefaultTextureBack);
            drawTexture(offsetX + this.lw, offsetY + this.th, this.xSize - this.lw - this.rw, this.ySize - this.th - this.bh);

            this.mc.getTextureManager().bindTexture(DefaultTextureTop);
            drawTexture(offsetX + this.tlw, offsetY, this.xSize - this.tlw - this.trw, this.th);
            this.mc.getTextureManager().bindTexture(DefaultTextureBottom);
            drawTexture(offsetX + this.blw, offsetY + this.ySize - this.bh, this.xSize - this.blw - this.brw, this.bh);

            this.mc.getTextureManager().bindTexture(DefaultTextureLeft);
            drawTexture(offsetX, offsetY + this.tlh, this.lw, this.ySize - this.tlh - this.blh);
            this.mc.getTextureManager().bindTexture(DefaultTextureRight);
            drawTexture(offsetX + this.xSize - this.rw, offsetY + this.trh, this.rw, this.ySize - this.trh - this.brh);

            this.mc.getTextureManager().bindTexture(DefaultTextureTopLeft);
            drawTexture(offsetX, offsetY, this.tlw, this.tlh);
            this.mc.getTextureManager().bindTexture(DefaultTextureTopRight);
            drawTexture(offsetX + this.xSize - this.trw, offsetY, this.trw, this.trh);
            this.mc.getTextureManager().bindTexture(DefaultTextureBottomLeft);
            drawTexture(offsetX, offsetY + this.ySize - this.blh, this.blw, this.blh);
            this.mc.getTextureManager().bindTexture(DefaultTextureBottomRight);
            drawTexture(offsetX + this.xSize - this.brw, offsetY + this.ySize - this.brh, this.brw, this.brh);

            this.mc.getTextureManager().bindTexture(DefaultTexturePlayer);
            drawTexture(offsetX + container.playerSlotOffsetX, offsetY + container.playerSlotOffsetY, 176, 94);
        } else {
            this.mc.getTextureManager().bindTexture((this.backTexture == null) ? DefaultBackTexture : this.backTexture);
            drawTexturedModalRect(offsetX, offsetY, 0, 0, container.machineGuiSizeX, container.machineGuiSizeY);
            this.mc.getTextureManager().bindTexture(PlayerInventoryTexture);
            drawTexturedModalRect(offsetX + container.playerSlotOffsetX, offsetY + container.playerSlotOffsetY, 0, 0, 176, 94);
        }

        for (int i = 0; i < container.machineInventorySlots.size(); i++) {
            Slot slot = container.machineInventorySlots.get(i);
            if (slot instanceof SlotWithTexture) {
                ((SlotWithTexture) slot).draw(this, offsetX, offsetY);
            } else {
                ResourceLocation SlotTexture = new ResourceLocation("clayium", "textures/gui/slot.png");
                int slotSizeX = 18, slotSizeY = 18;
                int slotTextureX = 0, slotTextureY = 0;
                this.mc.getTextureManager().bindTexture(SlotTexture);
                drawTexturedModalRect(offsetX + slot.xDisplayPosition - (slotSizeX - 16) / 2, offsetY + slot.yDisplayPosition - (slotSizeY - 16) / 2, slotTextureX, slotTextureY, slotSizeX, slotSizeY);
            }
        }

        if (this.overlayTexture != null) {
            this.mc.getTextureManager().bindTexture(this.overlayTexture);
            drawTexturedModalRect(offsetX, offsetY, 0, 0, container.machineGuiSizeX, container.machineGuiSizeY);
        }
    }


    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        GL11.glDisable(2896);
        GL11.glDisable(3042);
        for (GuiTextField textField : this.textFields) {
            textField.drawTextBox();
        }
    }

    public void initGui() {
        super.initGui();
        addButton();
        addTextField();
        if (this.textFields != null && this.textFields.size() != 0) {
            this.keyEvents = true;
            Keyboard.enableRepeatEvents(true);
        }
    }

    public void addButton() {}

    public void addTextField() {}

    protected void keyTyped(char c, int key, int id) {}

    public void addTextFieldToList(GuiTextField textField) {
        String s = ((ContainerTemp) this.inventorySlots).getTextFieldString(this.mc.thePlayer, this.textFields.size());
        textField.setText((s == null) ? "" : s);
        this.textFields.add(textField);
    }


    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.keyEvents == true) Keyboard.enableRepeatEvents(false);

    }

    protected void keyTyped(char c, int key) {
        boolean flag = true;
        for (int i = 0; i < this.textFields.size(); i++) {
            GuiTextField textField = this.textFields.get(i);
            if (textField.textboxKeyTyped(c, key)) {

                ClayiumCore.packetDispatcher.sendToServer(new GuiTextFieldPacket(textField.getText(), i));
                keyTyped(c, key, i);
                flag = false;
            }
        }
        if (flag) {
            super.keyTyped(c, key);
        }
    }


    public void setTextFieldString(String string, int id, boolean sendToContainer) {
        if (id < 0 || id >= this.textFields.size())
            return;
        GuiTextField textField = this.textFields.get(id);
        textField.setText(string);
        if (sendToContainer) {
            ContainerTemp container = (ContainerTemp) this.inventorySlots;
            container.setTextFieldString((Minecraft.getMinecraft()).thePlayer, string, id);
        }
    }


    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        for (GuiTextField textField : this.textFields) {
            textField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        }
    }


    public boolean doesGuiPauseGame() {
        return false;
    }


    protected void actionPerformed(GuiButton guibutton) {
        this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, guibutton.id);
        ((ContainerTemp) this.inventorySlots).pushClientButton((Minecraft.getMinecraft()).thePlayer, guibutton.id);
    }
}
