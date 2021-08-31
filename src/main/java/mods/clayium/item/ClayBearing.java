package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayBearing extends Item {
    public ClayBearing() {
        setUnlocalizedName("clay_bearing");
        setRegistryName(ClayiumCore.ModId, "clay_bearing");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
