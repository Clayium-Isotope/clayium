package mods.clayium.machine.MultitrackBuffer;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;

public class MultitrackBuffer extends ClayiumMachine {
    public MultitrackBuffer(int tier) {
        super(EnumMachineKind.multitrackBuffer, "", tier, TileEntityMultitrackBuffer.class, GuiHandler.GuiIdMultitrackBuffer);
    }
}
