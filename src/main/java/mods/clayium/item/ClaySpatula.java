package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import mods.clayium.creativetab.TabClayium;
import net.minecraft.item.Item;

public class ClaySpatula extends Item {
    public ClaySpatula() {
        setMaxDamage(36);
        setMaxStackSize(1);
        setUnlocalizedName("clay_spatula");
        setRegistryName(ClayiumCore.ModId, "clay_spatula");
        setCreativeTab(TabClayium.tab);
        setContainerItem(this);
    }
}
