package mods.clayium.machine.ChemicalMetalSeparator;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;

public class ChemicalMetalSeparator extends ClayiumMachine {
    public ChemicalMetalSeparator() {
        super(EnumMachineKind.chemicalMetalSeparator, "", 6, TileEntityChemicalMetalSeparator.class, GuiHandler.GuiIdChemicalMetalSeparator);
    }
}
