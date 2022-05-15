package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

public class ItemFilterDuplicator extends ItemFilterTemp {
    public boolean isCopy(ItemStack filter) {
        return true;
    }

    public ItemStack clearCopyFlag(ItemStack filter) {
        return filter;
    }

    public ItemStack setCopyFlag(ItemStack filter) {
        return filter;
    }
}
