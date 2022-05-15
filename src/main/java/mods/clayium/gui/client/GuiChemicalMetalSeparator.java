package mods.clayium.gui.client;

import mods.clayium.block.tile.TileChemicalMetalSeparator;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.gui.container.ContainerChemicalMetalSeparator;
import mods.clayium.gui.container.ContainerTemp;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiChemicalMetalSeparator extends GuiClayMachines {
    public GuiChemicalMetalSeparator(InventoryPlayer invPlayer, TileChemicalMetalSeparator tile, Block block) {
        super((ContainerTemp) new ContainerChemicalMetalSeparator(invPlayer, tile, block), (TileClayContainer) tile, block);
    }

    public GuiChemicalMetalSeparator(ContainerTemp container, TileChemicalMetalSeparator tile, Block block) {
        super(container, (TileClayContainer) tile, block);
    }


    protected void calculateProgressBarOffsets() {
        this.progressBarSizeX = 24;
        this.progressBarSizeY = 17;
        this.progressBarPosX = 55;
        this.progressBarPosY = 44;
    }
}
