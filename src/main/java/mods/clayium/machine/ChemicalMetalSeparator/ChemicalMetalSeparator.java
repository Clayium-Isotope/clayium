package mods.clayium.machine.ChemicalMetalSeparator;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class ChemicalMetalSeparator extends ClayiumMachine {

    public ChemicalMetalSeparator() {
        super(EnumMachineKind.chemicalMetalSeparator, "", TierPrefix.precision, TileEntityChemicalMetalSeparator.class,
                GuiHandler.GuiIdChemicalMetalSeparator);
    }
}
