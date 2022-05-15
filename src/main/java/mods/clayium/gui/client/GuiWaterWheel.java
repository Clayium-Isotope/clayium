package mods.clayium.gui.client;

import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileWaterWheel;
import mods.clayium.gui.container.ContainerTemp;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;


public class GuiWaterWheel
        extends GuiClayContainerTemp {
    public GuiWaterWheel(ContainerTemp container, TileClayContainer tile, Block block) {
        super(container, tile, block);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);


        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        TileWaterWheel tile = (TileWaterWheel) this.tile;

        this.fontRendererObj.drawString(I18n.format("gui.WaterWheel.progress", new Object[] {Integer.valueOf(tile.progress)}), 6, container.machineGuiSizeY - 12, 4210752);


        this.fontRendererObj.drawString(I18n.format("gui.WaterWheel.durability", new Object[] {Integer.valueOf(tile.progressEfficiency)}), 90, container.machineGuiSizeY - 12, 4210752);


        this.fontRendererObj.drawString(I18n.format("gui.WaterWheel.surroundingWater", new Object[] {Integer.valueOf(tile.countSurroundingWater())}), 90, container.machineGuiSizeY - 0, 4210752);
    }
}
