package mods.clayium.machine.ClayChemicalReactor;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class ClayChemicalReactor extends ClayiumMachine {
    public ClayChemicalReactor(TierPrefix tier) {
        super(EnumMachineKind.chemicalReactor, "", tier, TileEntityClayChemicalReactor.class, GuiHandler.GuiIdClayChemicalReactor);
    }
}
