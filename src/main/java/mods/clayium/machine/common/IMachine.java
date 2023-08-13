package mods.clayium.machine.common;

import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface IMachine {
    EnumMachineKind getKind();

    /**
     * by IInventory
     */
    int getField(int id);
}
