package mods.clayium.machine.WaterWheel;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayDirectionalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;

public class WaterWheel extends ClayDirectionalNoRecipeMachine {
    public WaterWheel(int tier) {
        super(TileEntityWaterWheel.class, EnumMachineKind.waterWheel, GuiHandler.GuiIdClayWaterWheel, tier);
    }
}
