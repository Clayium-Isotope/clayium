package mods.clayium.machine.ClayAssembler;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class ClayAssembler extends ClayiumMachine {
    public ClayAssembler(EnumMachineKind kind, TierPrefix tier) {
        super(kind, "", tier, TileEntityClayAssembler.class, GuiHandler.GuiIdClayAssembler);
    }

    public ClayAssembler(EnumMachineKind kind, TierPrefix tier, Class<? extends TileEntityGeneric> teClass) {
        super(kind, "", tier, teClass, GuiHandler.GuiIdClayAssembler);
    }
}
