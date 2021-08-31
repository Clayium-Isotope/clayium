package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class DenseClayStick extends Item {
    public DenseClayStick() {
        setUnlocalizedName("dense_clay_stick");
        setRegistryName(ClayiumCore.ModId, "dense_clay_stick");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
