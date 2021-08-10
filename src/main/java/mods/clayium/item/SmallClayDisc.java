package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class SmallClayDisc extends Item {
    public SmallClayDisc() {
        setUnlocalizedName("small_clay_disc");
        setRegistryName(ClayiumCore.ModId, "small_clay_disc");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
