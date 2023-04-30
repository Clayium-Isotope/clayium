package mods.clayium.machine.SolarClayFabricator;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;

@HasOriginalState
public class SolarClayFabricator extends ClayHorizontalNoRecipeMachine {
    public SolarClayFabricator(int tier) {
        super(TileEntitySolarClayFabricator.class, EnumMachineKind.solarClayFabricator,
                tier == 5 ? "mk1" : tier == 6 ? "mk2" : tier == 7 ? "mk3" : "",
                GuiHandler.GuiIdClayMachines, tier);
    }
}
