package mods.clayium.machine.ClayAssembler;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;

public class ClayAssembler extends ClayiumMachine {
    public ClayAssembler(EnumMachineKind kind, int tier) {
        super(kind, "", tier, TileEntityClayAssembler.class, GuiHandler.GuiIdClayAssembler);
    }

    public ClayAssembler(EnumMachineKind kind, int tier, Class<? extends TileEntityClayAssembler> teClass) {
        super(kind, "", tier, teClass, GuiHandler.GuiIdClayAssembler);
    }
}
