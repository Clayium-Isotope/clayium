package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClaySpindle extends Item {
    public ClaySpindle() {
        setUnlocalizedName("clay_spindle");
        setRegistryName(ClayiumCore.ModId, "clay_spindle");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
