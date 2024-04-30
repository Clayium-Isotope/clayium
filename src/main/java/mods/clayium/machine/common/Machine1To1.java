package mods.clayium.machine.common;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface Machine1To1 extends IMachine {

    int MATERIAL = 0;
    int PRODUCT = 1;
}
