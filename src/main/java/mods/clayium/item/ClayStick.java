package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayStick extends Item {
    public ClayStick() {
        setMaxStackSize(64);
        setUnlocalizedName("clay_stick");
        setRegistryName(ClayiumCore.ModId, "clay_stick");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
