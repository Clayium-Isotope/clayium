package mods.clayium.machine.WaterWheel;

import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;

public class WaterWheel extends ClayiumMachine {
    public WaterWheel(int tier) {
        super(EnumMachineKind.waterWheel, "",  tier, TileEntityWaterWheel.class, GuiHandler.GuiIdClayWaterWheel);
    }
}
