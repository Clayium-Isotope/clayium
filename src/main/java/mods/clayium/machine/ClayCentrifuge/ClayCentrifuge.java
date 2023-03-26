package mods.clayium.machine.ClayCentrifuge;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;

public class ClayCentrifuge extends ClayiumMachine {
    public ClayCentrifuge(int tier) {
        super(EnumMachineKind.centrifuge, "", tier, TileEntityClayCentrifuge.class, GuiHandler.GuiIdClayCentrifuge);
    }
}
