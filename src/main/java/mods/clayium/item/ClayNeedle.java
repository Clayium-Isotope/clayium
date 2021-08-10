package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayNeedle extends Item {
    public ClayNeedle() {
        setUnlocalizedName("clay_needle");
        setRegistryName(ClayiumCore.ModId, "clay_needle");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
