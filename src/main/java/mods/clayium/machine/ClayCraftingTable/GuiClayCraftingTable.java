package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiClayCraftingTable extends GuiContainer {
    private final TileEntityClayCraftingTable tile;
    private final Block block;
    private final ContainerClayCraftingTable container;
    private String guiTitle = "";

    public GuiClayCraftingTable(ContainerClayCraftingTable container, TileEntityClayCraftingTable tile, Block block) {
        super(container);
        this.tile = tile;
        this.block = block;
        this.container = container;
        xSize = 176;
        ySize = container.machineGuiHeight + 94;
        setGuiTitle(container.getInventoryName());
    }

    public void setGuiTitle(String guiTitle) {
        this.guiTitle = guiTitle;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRenderer.drawString(guiTitle, 6, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, container.machineGuiHeight, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int offsetX = (width - xSize) / 2;
        int offsetY = (height - ySize) / 2;

        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/back.png"));
        drawTexturedModalRect(offsetX, offsetY, 0, 0, 176, container.machineGuiHeight);
        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/playerinventory.png"));
        drawTexturedModalRect(offsetX, offsetY + container.machineGuiHeight, 0, 0, 176, 94);

        for(int i = 1; i < 10; ++i) {
            ((SlotWithTexture) container.inventorySlots.get(i)).draw(this, offsetX, offsetY);
        }

        if (container.tileChest != null) {
            for (int y = 0; y < container.tileChest.getHeight(); y++) {
                for (int x = 0; x < container.tileChest.getWidth(); x++) {
                    ((SlotWithTexture) container.inventorySlots.get(10 + y * container.tileChest.getWidth() + x)).draw(this, offsetX, offsetY);
                }
            }
        }

        mc.getTextureManager().bindTexture(new ResourceLocation(ClayiumCore.ModId, "textures/gui/claycraftingtable.png"));
        drawTexturedModalRect(offsetX, offsetY, 0, 0, 176, container.machineGuiHeight);
    }
}
