package mods.clayium.machine.ClayContainer;

import mods.clayium.gui.ContainerNormalInventory;
import mods.clayium.gui.GuiTemp;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiMultiPageContainer extends GuiTemp {
    public GuiMultiPageContainer(ContainerNormalInventory container) {
        super(container);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);

        if (this.container instanceof ContainerNormalInventory) {
            ContainerNormalInventory container = (ContainerNormalInventory) this.container;
            if (container.isMultiPage())
                this.fontRenderer.drawString(container.getPresentPageNum() + "/" + container.getMaxPageNum(), container.playerSlotOffsetX + 8 + 162, container.playerSlotOffsetY + 32, 4210752);
        }
    }

    public void addButtons() {
        if (this.container instanceof ContainerNormalInventory) {
            ContainerNormalInventory container = (ContainerNormalInventory) this.container;
            if (container.isMultiPage()) {
                int offsetX = (this.width - this.xSize) / 2;
                int offsetY = (this.height - this.ySize) / 2;
                addButton(new GuiButtonExt(ContainerNormalInventory.buttonIdPrevious, offsetX + container.playerSlotOffsetX + 8 + 162, offsetY + container.playerSlotOffsetY + 12, 16, 16, "<"));
                addButton(new GuiButtonExt(ContainerNormalInventory.buttonIdNext, offsetX + container.playerSlotOffsetX + 8 + 180, offsetY + container.playerSlotOffsetY + 12, 16, 16, ">"));
            }
        }
    }
}
