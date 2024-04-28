package mods.clayium.machine.CACollector;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class CACollector extends ClayHorizontalNoRecipeMachine {
    public CACollector() {
        super(TileEntityCACollector::new, EnumMachineKind.CACollector, TierPrefix.antimatter, "ca_collector", GuiHandler.GuiIdCACollector);
    }
}
