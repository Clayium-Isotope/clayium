package mods.clayium.gui.client;

import mods.clayium.block.tile.TileClayBlastFurnace;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.gui.container.ContainerTemp;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;


public class GuiClayBlastFurnace
        extends GuiClayMachines {
    public GuiClayBlastFurnace(ContainerTemp container, TileClayBlastFurnace tile, Block block) {
        super(container, (TileClayContainer) tile, block);
    }


    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        ContainerTemp container = (ContainerTemp) this.inventorySlots;
        this.fontRendererObj.drawString(UtilLocale.tierGui(((TileClayBlastFurnace) this.tile).recipeTier), 64, container.machineGuiSizeY - 12, 4210752);
    }

    public void addButton() {}

    public void drawButton() {}
}
