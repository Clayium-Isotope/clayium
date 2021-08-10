package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayRing extends Item {
    public ClayRing() {
        setUnlocalizedName("clay_ring");
        setRegistryName(ClayiumCore.ModId, "clay_ring");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
