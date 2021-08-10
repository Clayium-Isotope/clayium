package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import mods.clayium.creativetab.TabClayium;
import net.minecraft.item.Item;

public class ClaySlicer extends Item {
    public ClaySlicer() {
        setMaxDamage(60);
        setMaxStackSize(1);
        setUnlocalizedName("clay_slicer");
        setRegistryName(ClayiumCore.ModId, "clay_slicer");
        setCreativeTab(TabClayium.tab);
        setContainerItem(this);
    }
}
