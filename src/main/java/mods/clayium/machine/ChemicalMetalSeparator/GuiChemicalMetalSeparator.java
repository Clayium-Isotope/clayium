package mods.clayium.machine.ChemicalMetalSeparator;

import mods.clayium.machine.ClayiumMachine.GuiClayiumMachine;

public class GuiChemicalMetalSeparator extends GuiClayiumMachine {
    public GuiChemicalMetalSeparator(ContainerChemicalMetalSeparator container) {
        super(container);
    }

    protected void calculateProgressBarOffsets() {
        this.progressBarSizeX = 24;
        this.progressBarSizeY = 17;
        this.progressBarPosX = 55;
        this.progressBarPosY = 44;
    }
}
