package mods.clayium.machine.ClayReactor;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.MultiblockMachine.MultiblockMachine;

@HasOriginalState
public class ClayReactor extends MultiblockMachine {
    public ClayReactor() {
        super(EnumMachineKind.clayReactor, "", 7, TileEntityClayReactor.class, GuiHandler.GuiIdClayReactor);
    }
}
