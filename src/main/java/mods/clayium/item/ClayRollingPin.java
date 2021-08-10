package mods.clayium.item;

import mods.clayium.core.ClayiumCore;
import net.minecraft.item.Item;

public class ClayRollingPin extends Item {
    public ClayRollingPin() {
        setMaxDamage(60);
        setMaxStackSize(1);
        setUnlocalizedName("clay_rolling_pin");
        setRegistryName(ClayiumCore.ModId, "clay_rolling_pin");
        setCreativeTab(ClayiumCore.tabClayium);
        setContainerItem(this);
    }
}
