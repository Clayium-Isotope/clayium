package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;

public class ClayRollingPin extends ClayiumItem {
    public ClayRollingPin() {
        super("clay_rolling_pin");
        setMaxDamage(60);
        setMaxStackSize(1);
        setContainerItem(this);
    }
}
