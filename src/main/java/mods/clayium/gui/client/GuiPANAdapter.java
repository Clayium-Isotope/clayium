package mods.clayium.gui.client;

import cpw.mods.fml.client.config.GuiButtonExt;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.gui.container.ContainerNormalInventory;
import mods.clayium.gui.container.ContainerPANAdapter;
import mods.clayium.gui.container.ContainerTemp;
import net.minecraft.block.Block;

public class GuiPANAdapter extends GuiClayContainerTemp {
    public GuiPANAdapter(ContainerPANAdapter container, TileClayContainer tile, Block block) {
        super((ContainerTemp) container, tile, block);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);

        if (this.inventorySlots instanceof ContainerPANAdapter) {
            ContainerPANAdapter container = (ContainerPANAdapter) this.inventorySlots;
            if (container.isMultipage())
                this.fontRendererObj.drawString(container.getPresentPageNum() + "/" + container.getMaxPageNum(), container.machineGuiSizeX - 20, container.playerSlotOffsetY - 32, 4210752);
        }
    }

    public void addButton() {
        if (this.inventorySlots instanceof ContainerPANAdapter) {
            ContainerPANAdapter container = (ContainerPANAdapter) this.inventorySlots;
            if (container.isMultipage()) {
                int offsetX = (this.width - this.xSize) / 2;
                int offsetY = (this.height - this.ySize) / 2;
                this.buttonList.add(new GuiButtonExt(ContainerNormalInventory.buttonIdPrevious, offsetX + 4, offsetY + container.playerSlotOffsetY - 60, 16, 16, "<"));
                this.buttonList.add(new GuiButtonExt(ContainerNormalInventory.buttonIdNext, offsetX + container.machineGuiSizeX - 20, offsetY + container.playerSlotOffsetY - 60, 16, 16, ">"));
            }
        }
    }
}
