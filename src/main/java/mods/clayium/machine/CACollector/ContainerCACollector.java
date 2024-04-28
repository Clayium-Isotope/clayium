package mods.clayium.machine.CACollector;

import mods.clayium.gui.ContainerNormalInventory;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerCACollector extends ContainerNormalInventory {
    public ContainerCACollector(InventoryPlayer player, TileEntityCACollector tile) {
        super(player, tile);
    }

    @Override
    protected void doSomethingJustBeforeConstruct() {
        this.machineGuiSizeY += 14;
    }
}