package mods.clayium.machine.CACondenser;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.CAMachine.CAMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class CACondenser extends CAMachine {

    public CACondenser(TierPrefix tier) {
        super(EnumMachineKind.CACondenser, "", tier, TileEntityCACondenser.class, GuiHandler.GuiIdClayMachines);
    }
}
