package mods.clayium.item.common;

import mods.clayium.util.TierPrefix;
import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.Item)
public interface ITieredItem {
    TierPrefix getTier();
}
