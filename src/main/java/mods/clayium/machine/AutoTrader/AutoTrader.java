package mods.clayium.machine.AutoTrader;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

@HasOriginalState
public class AutoTrader extends ClayHorizontalNoRecipeMachine {
    public AutoTrader() {
        super(TileEntityAutoTrader::new, EnumMachineKind.autoTrader, TierPrefix.clayium, "auto_trader", GuiHandler.GuiIdAutoTrader);
    }
}
