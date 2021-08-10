package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayBlade extends Item {
    public ClayBlade() {
        setUnlocalizedName("clay_blade");
        setRegistryName(ClayiumCore.ModId, "clay_blade");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
