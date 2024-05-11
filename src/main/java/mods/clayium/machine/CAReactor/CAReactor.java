package mods.clayium.machine.CAReactor;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.MultiblockMachine.MultiblockMachine;
import mods.clayium.util.TierPrefix;

public class CAReactor extends MultiblockMachine {
    public CAReactor(TierPrefix tier) {
        super(EnumMachineKind.CAReactorCore, "", tier, TileEntityCAReactor::new, GuiHandler.GuiIdCAReactor);
    }
}
