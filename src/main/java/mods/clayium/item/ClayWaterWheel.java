package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayWaterWheel extends Item {
    public ClayWaterWheel() {
        setUnlocalizedName("clay_water_wheel");
        setRegistryName(ClayiumCore.ModId, "clay_water_wheel");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
