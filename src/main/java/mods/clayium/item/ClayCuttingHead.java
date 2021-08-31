package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayCuttingHead extends Item {
    public ClayCuttingHead() {
        setUnlocalizedName("clay_cutting_head");
        setRegistryName(ClayiumCore.ModId, "clay_cutting_head");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
