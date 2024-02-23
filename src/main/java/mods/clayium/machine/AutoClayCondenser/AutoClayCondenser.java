package mods.clayium.machine.AutoClayCondenser;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class AutoClayCondenser extends ClayiumMachine {
    public AutoClayCondenser(TierPrefix tier) {
        super(EnumMachineKind.autoClayCondenser, tier == TierPrefix.advanced ? "mk1" : tier == TierPrefix.claySteel ? "mk2" : "", tier, TileEntityAutoClayCondenser::new, GuiHandler.GuiIdAutoClayCondenser);
    }
}
