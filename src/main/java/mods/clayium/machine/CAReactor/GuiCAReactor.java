package mods.clayium.machine.CAReactor;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.ClayiumMachine.GuiClayiumMachine;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiCAReactor extends GuiClayiumMachine {
    public GuiCAReactor(ContainerClayiumMachine container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp)this.inventorySlots;
        TileEntityCAReactor tile = (TileEntityCAReactor)this.tile;
        if (!tile.errorMessage.equals("")) {
            this.buttonList.get(0).displayString = I18n.format("gui.CAReactor.invalid");
            this.buttonList.get(0).enabled = true;
        } else {
            this.fontRenderer.drawString(I18n.format("gui.CAReactor.rankSize", tile.reactorRank + 1.0, tile.reactorHullNum), 6, 16, 4210752);
            this.fontRenderer.drawString(I18n.format("gui.CAReactor.efficiency", tile.getEfficiency()), 64, container.machineGuiSizeY - 12, 4210752);
            this.buttonList.get(0).displayString = I18n.format("gui.CAReactor.constructed");
            this.buttonList.get(0).enabled = false;
        }
    }

    @Override
    public void addButtons() {
        ContainerTemp container = (ContainerTemp)this.inventorySlots;
        int offsetX = (this.width - this.xSize) / 2;
        int offsetY = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButtonExt(1, offsetX + container.machineGuiSizeX - 84, offsetY + container.machineGuiSizeY - 1, 80, 10, ""));
        this.buttonList.get(0).enabled = false;
    }
}
