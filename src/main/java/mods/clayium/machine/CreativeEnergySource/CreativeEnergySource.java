package mods.clayium.machine.CreativeEnergySource;

import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class CreativeEnergySource extends ClayDirectionalNoRecipeMachine {
    public CreativeEnergySource() {
        super(TileEntityCreativeEnergySource.class, EnumMachineKind.creativeCESource, TierPrefix.OPA);
    }
}
