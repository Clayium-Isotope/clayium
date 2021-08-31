package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayDust extends Item {
    public ClayDust() {
        setUnlocalizedName("clay_dust");
        setRegistryName(ClayiumCore.ModId, "clay_dust");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
