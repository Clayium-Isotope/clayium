package mods.clayium.machine.MultitrackBuffer;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class MultitrackBuffer extends ClayiumMachine {
    public MultitrackBuffer(TierPrefix tier) {
        super(EnumMachineKind.multitrackBuffer, "", tier, TileEntityMultitrackBuffer::new, GuiHandler.GuiIdMultitrackBuffer);
    }
}
