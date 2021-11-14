package mods.clayium.gui.client;

import mods.clayium.block.tile.TileAreaMiner;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.gui.GuiPictureButton;
import mods.clayium.gui.container.ContainerAreaMiner;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;

public class GuiAreaMiner
        extends GuiClayEnergyTemp {
    public GuiAreaMiner(ContainerAreaMiner container, TileAreaMiner tile, Block block) {
        super((ContainerTemp) container, (TileClayContainer) tile, block);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        long energy = ((TileAreaMiner) this.tile).laserEnergy;
        this.fontRendererObj.drawString(UtilLocale.laserGui(energy), 64, container.machineGuiSizeY - 12, 4210752);

        if (((TileAreaMiner) this.tile).getTier() >= 9) {

            this.fontRendererObj.drawString(I18n.format("gui.AreaMiner.harvest", new Object[0]), container.machineGuiSizeX - 48, 21, 4210752);

            this.fontRendererObj.drawString(I18n.format("gui.AreaMiner.fortune", new Object[0]), container.machineGuiSizeX - 48, 39, 4210752);

            this.fontRendererObj.drawString(I18n.format("gui.AreaMiner.silktouch", new Object[0]), container.machineGuiSizeX - 48, 57, 4210752);
        }
    }


    public void addButton() {
        int offsetX = (this.width - this.xSize) / 2;
        int offsetY = (this.height - this.ySize) / 2;
        if (((TileAreaMiner) this.tile).getTier() >= 9) {
            offsetX = (this.width - this.xSize) / 2 - 10;
        }
        if (((TileAreaMiner) this.tile).getTier() <= 9) {
            this.buttonList.add(new GuiPictureButton(0, offsetX + 16, offsetY + 16, 16, 16, GuiTemp.ButtonTexture, 16, 0));
            this.buttonList.add(new GuiPictureButton(1, offsetX + 16 + 18, offsetY + 16, 16, 16, GuiTemp.ButtonTexture, 32, 0));
            this.buttonList.add(new GuiPictureButton(2, offsetX + 16, offsetY + 16 + 18, 16, 16, GuiTemp.ButtonTexture, 64, 0));
            this.buttonList.add(new GuiPictureButton(3, offsetX + 16 + 18, offsetY + 16 + 18, 16, 16, GuiTemp.ButtonTexture, 48, 0));
        } else {
            this.buttonList.add(new GuiPictureButton(0, offsetX + 16, offsetY + 16, 16, 16, GuiTemp.ButtonTexture, 16, 0));
            this.buttonList.add(new GuiPictureButton(1, offsetX + 16, offsetY + 16 + 18, 16, 16, GuiTemp.ButtonTexture, 32, 0));
            this.buttonList.add(new GuiPictureButton(2, offsetX + 16, offsetY + 16 + 36, 16, 16, GuiTemp.ButtonTexture, 64, 0));
            this.buttonList.add(new GuiPictureButton(3, offsetX + 16, offsetY + 16 + 54, 16, 16, GuiTemp.ButtonTexture, 48, 0));
        }
    }
}
