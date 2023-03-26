package mods.clayium.machine.AutoClayCondenser;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;

public class AutoClayCondenser extends ClayiumMachine {
    public AutoClayCondenser(int tier) {
        super(EnumMachineKind.autoClayCondenser, tier == 5 ? "mk1" : tier == 7 ? "mk2" : "", tier, TileEntityAutoClayCondenser.class, GuiHandler.GuiIdAutoClayCondenser);
    }
}
