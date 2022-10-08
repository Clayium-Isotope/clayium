package mods.clayium.gui;

import mods.clayium.core.ClayiumCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiTemp extends GuiContainer {
    protected final IInventory tile;
    protected final ContainerTemp container;
    protected final int machineHeight;
    protected ResourceLocation coverTexture;

    public GuiTemp(ContainerTemp container) {
        super(container);

        this.tile = container.tileEntity;
        this.container = container;
        xSize = 176;
        ySize = this.container.machineGuiSizeY + 94;
        machineHeight = this.container.machineGuiSizeY;
    }

    @Override
    public void initGui() {
        super.initGui();
        addButtons();
    }

    protected void addButtons() {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRenderer.drawString(this.container.tileEntity.getDisplayName().getUnformattedText(), 6, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, machineHeight, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/back.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, machineHeight);
        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/playerinventory.png"));
        drawTexturedModalRect(guiLeft, guiTop + machineHeight, 0, 0, xSize, 94);

        supplyDraw();

        if (coverTexture != null) {
            mc.getTextureManager().bindTexture(coverTexture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, machineHeight);
        }
    }

    protected void supplyDraw() {
        for(int i = 0; i < container.inventorySlots.size(); ++i) {
            if (container.inventorySlots.get(i) instanceof SlotWithTexture)
                ((SlotWithTexture) container.inventorySlots.get(i)).draw(this, guiLeft, guiTop);
        }
    }
}
