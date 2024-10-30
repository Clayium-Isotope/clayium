package mods.clayium.machine.CAMachine;

import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.TileEntityGeneric;
import mods.clayium.util.TierPrefix;

import java.util.function.Supplier;

public class CAMachine extends ClayiumMachine {
    public CAMachine(EnumMachineKind kind, String suffix, TierPrefix tier, Supplier<? extends TileEntityGeneric> teSupplier, int guiID) {
        super(kind, suffix, tier, teSupplier, guiID);
    }
}
