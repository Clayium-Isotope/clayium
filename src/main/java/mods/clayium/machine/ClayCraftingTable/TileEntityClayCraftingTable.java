package mods.clayium.machine.ClayCraftingTable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.util.TierPrefix;

public class TileEntityClayCraftingTable extends TileEntityClayContainer {

    public TileEntityClayCraftingTable() {
        this.containerItemStacks = NonNullList.withSize(10, ItemStack.EMPTY);
        initParamsByTier(TierPrefix.none);
    }
}
