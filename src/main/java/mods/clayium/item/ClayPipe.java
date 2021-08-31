package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayPipe extends Item {
    public ClayPipe() {
        setUnlocalizedName("clay_pipe");
        setRegistryName(ClayiumCore.ModId, "clay_pipe");
        setCreativeTab(ClayiumCore.tabClayium);
    }
}
