package mods.clayium.machine.AreaMiner;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class AreaMiner extends ClayHorizontalNoRecipeMachine {
    public AreaMiner(TierPrefix tier) {
        super(null, EnumMachineKind.areaMiner, GuiHandler.GuiIdAreaMiner, tier);
    }
}
