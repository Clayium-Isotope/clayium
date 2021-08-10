package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayDisc extends Item {
    public ClayDisc() {
        setUnlocalizedName("clay_disc");
        setRegistryName(ClayiumCore.ModId, "clay_disc");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
