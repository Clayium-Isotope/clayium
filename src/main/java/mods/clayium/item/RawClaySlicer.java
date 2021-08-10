package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class RawClaySlicer extends Item {
    public RawClaySlicer() {
        setUnlocalizedName("raw_clay_slicer");
        setRegistryName(ClayiumCore.ModId, "raw_clay_slicer");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
