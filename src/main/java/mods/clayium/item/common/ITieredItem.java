package mods.clayium.item.common;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.Item)
public interface ITieredItem {
    int getTier();
}
