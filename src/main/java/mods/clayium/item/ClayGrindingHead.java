package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayGrindingHead extends Item {
    public ClayGrindingHead() {
        setUnlocalizedName("clay_grinding_head");
        setRegistryName(ClayiumCore.ModId, "clay_grinding_head");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
