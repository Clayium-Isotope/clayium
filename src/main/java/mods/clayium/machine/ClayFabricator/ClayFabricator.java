package mods.clayium.machine.ClayFabricator;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;

public class ClayFabricator extends ClayHorizontalNoRecipeMachine {
    public ClayFabricator(int tier) {
        super(TileEntityClayFabricator.class, EnumMachineKind.clayFabricator,
                tier == 8 ? "mk1" : tier == 9 ? "mk2" : tier == 13 ? "mk3" : "",
                GuiHandler.GuiIdClayMachines, tier);
    }
}
