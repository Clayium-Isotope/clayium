package mods.clayium.machine.VacuumContainer;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

@HasOriginalState
public class VacuumContainer extends ClayHorizontalNoRecipeMachine {
    public VacuumContainer() {
        super(TileEntityVacuumContainer.class, EnumMachineKind.vacuumContainer, GuiHandler.GuiIdVacuumContainer, TierPrefix.precision);
    }
}
