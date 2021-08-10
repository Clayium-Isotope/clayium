package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayPlate extends Item {
    public ClayPlate() {
        setUnlocalizedName("clay_plate");
        setRegistryName(ClayiumCore.ModId, "clay_plate");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
