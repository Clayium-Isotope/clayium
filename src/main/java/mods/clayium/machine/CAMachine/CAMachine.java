package mods.clayium.machine.CAMachine;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class CAMachine extends ClayiumMachine {
    public CAMachine(EnumMachineKind kind, String suffix, TierPrefix tier, Class<? extends TileEntityGeneric> teClass, int guiID) {
        super(kind, suffix, tier, teClass, guiID);
    }
}
