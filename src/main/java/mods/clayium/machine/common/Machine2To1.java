package mods.clayium.machine.common;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface Machine2To1 extends IMachine {
    int MATERIAL_1 = 0;
    int MATERIAL_2 = 1;
    int PRODUCT = 2;
}
