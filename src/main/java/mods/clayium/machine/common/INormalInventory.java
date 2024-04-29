package mods.clayium.machine.common;

import net.minecraft.inventory.IInventory;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface INormalInventory extends IInventory {

    int getInventoryX();

    int getInventoryY();

    int getInventoryP();

    int getInventoryStart();
}
