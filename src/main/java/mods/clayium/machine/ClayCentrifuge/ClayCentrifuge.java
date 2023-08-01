package mods.clayium.machine.ClayCentrifuge;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class ClayCentrifuge extends ClayiumMachine {
    public ClayCentrifuge(TierPrefix tier) {
        super(EnumMachineKind.centrifuge, "", tier, TileEntityClayCentrifuge.class, GuiHandler.GuiIdClayCentrifuge);
    }
}
