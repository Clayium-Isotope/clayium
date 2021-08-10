package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class RawClayRollingPin extends Item {
    public RawClayRollingPin() {
        setUnlocalizedName("raw_clay_rolling_pin");
        setRegistryName(ClayiumCore.ModId, "raw_clay_rolling_pin");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
