package mods.clayium.machine.ChemicalMetalSeparator;

import mods.clayium.gui.GuiIMachine;

public class GuiChemicalMetalSeparator extends GuiIMachine {

    public GuiChemicalMetalSeparator(ContainerChemicalMetalSeparator container) {
        super(container);
        this.progressBarSizeX = 24;
        this.progressBarSizeY = 17;
        this.progressBarPosX = 55;
        this.progressBarPosY = 44;
    }
}
