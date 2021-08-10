package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class SmallClayRing extends Item {
    public SmallClayRing() {
        setUnlocalizedName("small_clay_ring");
        setRegistryName(ClayiumCore.ModId, "small_clay_ring");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
