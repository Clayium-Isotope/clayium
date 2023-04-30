package mods.clayium.machine.CreativeEnergySource;

import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;

public class CreativeEnergySource extends ClayDirectionalNoRecipeMachine {
    public CreativeEnergySource() {
        super(TileEntityCreativeEnergySource.class, EnumMachineKind.creativeCESource, 13);
    }
}
