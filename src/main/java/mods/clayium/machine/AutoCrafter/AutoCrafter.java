package mods.clayium.machine.AutoCrafter;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

@HasOriginalState
public class AutoCrafter extends ClayHorizontalNoRecipeMachine {

    public AutoCrafter(TierPrefix tier) {
        super(TileEntityAutoCrafter.class, EnumMachineKind.autoCrafter, GuiHandler.GuiIdAutoCrafter, tier);
    }
}
