package mods.clayium.machine.ClayFabricator;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class ClayFabricator extends ClayHorizontalNoRecipeMachine {

    public ClayFabricator(TierPrefix tier) {
        super(TileEntityClayFabricator.class, EnumMachineKind.clayFabricator,
                tier == TierPrefix.clayium ? "mk1" :
                        tier == TierPrefix.ultimate ? "mk2" : tier == TierPrefix.OPA ? "mk3" : "",
                GuiHandler.GuiIdClayMachines, tier);
    }
}
