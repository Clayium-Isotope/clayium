package mods.clayium.item;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.Item)
public interface IAdvancedTool {

    IHarvestCoord getHarvestCoord();
}
