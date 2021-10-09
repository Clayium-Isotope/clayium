package mods.clayium.machine.common;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiClayMachineTemp extends GuiContainer {
    protected final TileEntity tile;
    protected final Block block;
    protected final ContainerClayMachineTemp container;
    private final int machineHeight;
    protected ResourceLocation coverTexture;

    public GuiClayMachineTemp(ContainerClayMachineTemp container, TileEntity tile, Block block, int height) {
        super(container);
        this.tile = tile;
        this.block = block;
        this.container = container;
        xSize = 176;
        ySize = height + 94;
        machineHeight = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRenderer.drawString(tile.getDisplayName().getUnformattedText(), 6, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, machineHeight, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

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
