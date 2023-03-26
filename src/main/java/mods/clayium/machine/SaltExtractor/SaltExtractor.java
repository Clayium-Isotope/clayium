package mods.clayium.machine.SaltExtractor;

import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;

public class SaltExtractor extends ClayDirectionalNoRecipeMachine {
    public SaltExtractor(int tier) {
        super(TileEntitySaltExtractor.class, EnumMachineKind.saltExtractor, tier);
    }
}
