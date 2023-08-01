package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.util.TierPrefix;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TileEntityClayCraftingTable extends TileEntityClayContainer {
    public TileEntityClayCraftingTable() {
        this.containerItemStacks = NonNullList.withSize(10, ItemStack.EMPTY);
        initParamsByTier(TierPrefix.none);
    }
}
