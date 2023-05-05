package mods.clayium.machine.AutoCrafter;

import mods.clayium.client.render.HasOriginalState;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;

@HasOriginalState
public class AutoCrafter extends ClayHorizontalNoRecipeMachine {
    public AutoCrafter(int tier) {
        super(TileEntityAutoCrafter.class, EnumMachineKind.autoCrafter, GuiHandler.GuiIdAutoCrafter, tier);
    }
}
