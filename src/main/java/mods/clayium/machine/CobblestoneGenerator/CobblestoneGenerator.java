package mods.clayium.machine.CobblestoneGenerator;

import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class CobblestoneGenerator extends ClayDirectionalNoRecipeMachine {

    public CobblestoneGenerator(TierPrefix tier) {
        super(TileEntityCobblestoneGenerator::new, EnumMachineKind.cobblestoneGenerator, tier);
    }
}
