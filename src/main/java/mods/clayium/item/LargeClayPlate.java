package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class LargeClayPlate extends Item {
    public LargeClayPlate() {
        setUnlocalizedName("large_clay_plate");
        setRegistryName(ClayiumCore.ModId, "large_clay_plate");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
