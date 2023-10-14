package mods.clayium.machine.StorageContainer;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.GuiTemp;

public class GuiStorageContainer extends GuiTemp {
    public GuiStorageContainer(ContainerTemp container) {
        super(container);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);

        int size = ((TileEntityStorageContainer) this.tile).getCurrentStackSize();
        this.fontRenderer.drawString(size + " / " + this.tile.getInventoryStackLimit(),
                this.container.machineGuiSizeX / 2 - 9 - Math.max(0, (int) Math.log10(size)), // sizX / 2 - 8 - (log10(size) + 1)
                this.container.machineGuiSizeY - 12, 4210752);
    }
}
