package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;

public class ClaySpatula extends ClayiumItem {
    public ClaySpatula() {
        super("clay_spatula");
        setMaxDamage(36);
        setMaxStackSize(1);
        setContainerItem(this);
    }
}
