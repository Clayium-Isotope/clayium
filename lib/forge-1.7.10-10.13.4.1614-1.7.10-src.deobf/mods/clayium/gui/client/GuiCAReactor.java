package mods.clayium.gui.client;

import cpw.mods.fml.client.config.GuiButtonExt;
import mods.clayium.block.tile.TileCAReactor;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.gui.container.ContainerTemp;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;

public class GuiCAReactor extends GuiClayMachines {
    public GuiCAReactor(ContainerTemp container, TileCAReactor tile, Block block) {
        super(container, (TileClayContainer) tile, block);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        TileCAReactor tile = (TileCAReactor) this.tile;
        if (!tile.errorMessage.equals("")) {

            ((GuiButtonExt) this.buttonList.get(0)).displayString = I18n.format("gui.CAReactor.invalid", new Object[0]);
            ((GuiButtonExt) this.buttonList.get(0)).enabled = true;
        } else {

            this.fontRendererObj.drawString(I18n.format("gui.CAReactor.rankSize", new Object[] {Double.valueOf(tile.reactorRank + 1.0D), Integer.valueOf(tile.reactorHullNum)}), 6, 16, 4210752);


            this.fontRendererObj.drawString(I18n.format("gui.CAReactor.efficiency", new Object[] {Double.valueOf(tile.getEfficiency())}), 64, container.machineGuiSizeY - 12, 4210752);


            ((GuiButtonExt) this.buttonList.get(0)).displayString = I18n.format("gui.CAReactor.constructed", new Object[0]);
            ((GuiButtonExt) this.buttonList.get(0)).enabled = false;
        }
    }


    public void addButton() {
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        int offsetX = (this.width - this.xSize) / 2;
        int offsetY = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButtonExt(1, offsetX + container.machineGuiSizeX - 84, offsetY + container.machineGuiSizeY - 1, 80, 10, ""));
        ((GuiButtonExt) this.buttonList.get(0)).enabled = false;
    }
}
