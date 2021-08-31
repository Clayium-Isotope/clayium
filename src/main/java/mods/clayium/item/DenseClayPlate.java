package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class DenseClayPlate extends Item {
    public DenseClayPlate() {
        setUnlocalizedName("dense_clay_plate");
        setRegistryName(ClayiumCore.ModId, "dense_clay_plate");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
