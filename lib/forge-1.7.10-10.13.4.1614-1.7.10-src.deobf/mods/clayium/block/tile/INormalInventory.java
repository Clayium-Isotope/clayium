package mods.clayium.block.tile;

import net.minecraft.inventory.IInventory;

public interface INormalInventory extends IInventory {
    int getInventoryX();

    int getInventoryY();

    int getInventoryP();

    int getInventoryStart();
}
