package mods.clayium.machine.ClayChemicalReactor;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;

public class ClayChemicalReactor extends ClayiumMachine {
    public ClayChemicalReactor(int tier) {
        super(EnumMachineKind.chemicalReactor, "", tier, TileEntityClayChemicalReactor.class, GuiHandler.GuiIdClayChemicalReactor);
    }
}
