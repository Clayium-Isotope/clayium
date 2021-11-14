package mods.clayium.gui.client;

import mods.clayium.block.tile.TileAreaActivator;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.container.ContainerAreaActivator;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;

public class GuiAreaActivator extends GuiClayEnergyTemp {
    public GuiAreaActivator(ContainerAreaActivator container, TileAreaActivator tile, Block block) {
        super((ContainerTemp) container, (TileClayContainer) tile, block);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        long energy = ((TileAreaActivator) this.tile).laserEnergy;
        this.fontRendererObj.drawString(UtilLocale.laserGui(energy), 64, container.machineGuiSizeY - 12, 4210752);

        TileAreaActivator tile = (TileAreaActivator) this.tile;
        ((GuiPictureButton) this.buttonList.get(4)).setTexture(GuiTemp.ButtonTexture, 16 * (5 + tile.target), 0);
        ((GuiPictureButton) this.buttonList.get(5)).setTexture(GuiTemp.ButtonTexture, 16 * (8 + (tile.enableRayTrace ? 1 : 0)), 0);
        ((GuiPictureButton) this.buttonList.get(6)).setTexture(GuiTemp.ButtonTexture, 16 * (10 + (tile.sneak ? 1 : 0)), 0);
    }


    public void addButton() {
        int offsetX = (this.width - this.xSize) / 2;
        int offsetY = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiPictureButton(0, offsetX + 16, offsetY + 16, 16, 16, GuiTemp.ButtonTexture, 16, 0));
        this.buttonList.add(new GuiPictureButton(1, offsetX + 16 + 18, offsetY + 16, 16, 16, GuiTemp.ButtonTexture, 32, 0));
        this.buttonList.add(new GuiPictureButton(2, offsetX + 16, offsetY + 16 + 18, 16, 16, GuiTemp.ButtonTexture, 64, 0));
        this.buttonList.add(new GuiPictureButton(3, offsetX + 16 + 18, offsetY + 16 + 18, 16, 16, GuiTemp.ButtonTexture, 48, 0));

        this.buttonList.add(new GuiPictureButton(4, offsetX + this.xSize - 16 - 16, offsetY + 16, 16, 16, GuiTemp.ButtonTexture, 16, 0));
        this.buttonList.add(new GuiPictureButton(5, offsetX + this.xSize - 16 - 16, offsetY + 16 + 18, 16, 16, GuiTemp.ButtonTexture, 16, 0));
        this.buttonList.add(new GuiPictureButton(6, offsetX + this.xSize - 16 - 16, offsetY + 16 + 36, 16, 16, GuiTemp.ButtonTexture, 16, 0));
    }
}
