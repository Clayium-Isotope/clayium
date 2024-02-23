package mods.clayium.machine.SolarClayFabricator;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

@HasOriginalState
public class SolarClayFabricator extends ClayHorizontalNoRecipeMachine {
    public SolarClayFabricator(TierPrefix tier) {
        super(TileEntitySolarClayFabricator::new, EnumMachineKind.solarClayFabricator,
                tier == TierPrefix.advanced ? "mk1" : tier == TierPrefix.precision ? "mk2" : tier == TierPrefix.claySteel? "mk3" : "",
                GuiHandler.GuiIdClayMachines, tier);
    }
}
