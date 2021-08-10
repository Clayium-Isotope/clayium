package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayCylinder extends Item {
    public ClayCylinder() {
        setUnlocalizedName("clay_cylinder");
        setRegistryName(ClayiumCore.ModId, "clay_cylinder");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
