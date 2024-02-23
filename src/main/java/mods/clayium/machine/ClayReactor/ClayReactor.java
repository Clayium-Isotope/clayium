package mods.clayium.machine.ClayReactor;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.MultiblockMachine.MultiblockMachine;
import mods.clayium.util.TierPrefix;

@HasOriginalState
public class ClayReactor extends MultiblockMachine {
    public ClayReactor() {
        super(EnumMachineKind.clayReactor, "", TierPrefix.claySteel, TileEntityClayReactor::new, GuiHandler.GuiIdClayReactor);
    }
}
