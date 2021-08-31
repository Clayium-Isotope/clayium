package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayGear extends Item {
    public ClayGear() {
        setUnlocalizedName("clay_gear");
        setRegistryName(ClayiumCore.ModId, "clay_gear");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
