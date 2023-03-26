package mods.clayium.machine.CobblestoneGenerator;

import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;

public class CobblestoneGenerator extends ClayDirectionalNoRecipeMachine {
    public CobblestoneGenerator(int tier) {
        super(TileEntityCobblestoneGenerator.class, EnumMachineKind.cobblestoneGenerator, tier);
    }
}
