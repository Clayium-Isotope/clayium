package mods.clayium.machine.common;

import mods.clayium.util.UsedFor;
import net.minecraft.inventory.IInventory;

@UsedFor(UsedFor.Type.TileEntity)
public interface INormalInventory extends IInventory {

    int getInventoryX();

    int getInventoryY();

    int getInventoryP();

    int getInventoryStart();
}
