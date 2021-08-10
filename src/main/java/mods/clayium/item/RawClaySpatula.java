package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class RawClaySpatula extends Item {
    public RawClaySpatula() {
        setUnlocalizedName("raw_clay_spatula");
        setRegistryName(ClayiumCore.ModId, "raw_clay_spatula");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
