package mods.clayium.gui.client;

import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileClayReactor;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;


public class GuiClayReactor
        extends GuiClayMachines {
    public GuiClayReactor(ContainerTemp container, TileClayReactor tile, Block block) {
        super(container, (TileClayContainer) tile, block);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        long energy = (((TileClayReactor) this.tile).irradiatedLaser == null) ? 0L : (long) ((TileClayReactor) this.tile).irradiatedLaser.getEnergy();

        this.fontRendererObj.drawString(UtilLocale.tierGui(((TileClayReactor) this.tile).recipeTier) + "  " + UtilLocale.laserGui(energy), 64, container.machineGuiSizeY - 12, 4210752);
    }

    public void addButton() {}

    public void drawButton() {}
}
