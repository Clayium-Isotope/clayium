package mods.clayium.gui.container;

import invtweaks.api.container.ChestContainer;
import mods.clayium.block.tile.INormalInventory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;

@ChestContainer(showButtons = false)
public class ContainerCACollector
        extends ContainerNormalInventory {
    public ContainerCACollector(InventoryPlayer player, INormalInventory tile, Block block) {
        super(player, tile, block);
    }


    protected void initParameters(InventoryPlayer player) {
        super.initParameters(player);
        this.machineGuiSizeY += 14;
        initParametersDefault(player);
    }
}
