package mods.clayium.machine.ClayAssembler;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.TileEntityGeneric;
import mods.clayium.util.TierPrefix;

import java.util.function.Supplier;

public class ClayAssembler extends ClayiumMachine {

    public ClayAssembler(EnumMachineKind kind, TierPrefix tier) {
        super(kind, "", tier, TileEntityClayAssembler::new, GuiHandler.GuiIdClayAssembler);
    }

    public ClayAssembler(EnumMachineKind kind, TierPrefix tier, Supplier<? extends TileEntityGeneric> teSupplier) {
        super(kind, "", tier, teSupplier, GuiHandler.GuiIdClayAssembler);
    }
}
