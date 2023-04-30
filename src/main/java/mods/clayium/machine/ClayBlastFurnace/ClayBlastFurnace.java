package mods.clayium.machine.ClayBlastFurnace;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.MultiblockMachine.MultiblockMachine;

@HasOriginalState
public class ClayBlastFurnace extends MultiblockMachine {
    public ClayBlastFurnace() {
        super(EnumMachineKind.blastFurnace, "", 6, TileEntityClayBlastFurnace.class, GuiHandler.GuiIdClayBlastFurnace);
    }
}
