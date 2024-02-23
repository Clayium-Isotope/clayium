package mods.clayium.machine.SaltExtractor;

import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class SaltExtractor extends ClayDirectionalNoRecipeMachine {
    public SaltExtractor(TierPrefix tier) {
        super(TileEntitySaltExtractor::new, EnumMachineKind.saltExtractor, tier);
    }
}
