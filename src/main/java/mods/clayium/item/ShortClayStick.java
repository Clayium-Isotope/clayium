package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ShortClayStick extends Item {
    public ShortClayStick() {
        setUnlocalizedName("short_clay_stick");
        setRegistryName(ClayiumCore.ModId, "short_clay_stick");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
