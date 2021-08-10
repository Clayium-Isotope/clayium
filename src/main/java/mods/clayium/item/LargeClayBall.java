package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class LargeClayBall extends Item {
    public LargeClayBall() {
        setUnlocalizedName("large_clay_ball");
        setRegistryName(ClayiumCore.ModId, "large_clay_ball");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
