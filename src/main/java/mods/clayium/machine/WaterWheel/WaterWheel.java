package mods.clayium.machine.WaterWheel;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;

public class WaterWheel extends ClayDirectionalNoRecipeMachine {
    public WaterWheel(TierPrefix tier) {
        super(TileEntityWaterWheel::new, EnumMachineKind.waterWheel, GuiHandler.GuiIdClayWaterWheel, tier);
    }
}
