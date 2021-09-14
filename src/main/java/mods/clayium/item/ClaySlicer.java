package mods.clayium.item;

import mods.clayium.item.common.ClayiumItem;

public class ClaySlicer extends ClayiumItem {
    public ClaySlicer() {
        super("clay_slicer");
        setMaxDamage(60);
        setMaxStackSize(1);
        setContainerItem(this);
    }
}
