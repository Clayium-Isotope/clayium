package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

import mods.clayium.util.TierPrefix;

public class FilterItemName extends FilterString {

    public FilterItemName() {
        super("filter_name", TierPrefix.precision);
    }

    @Override
    protected boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty()) return false;

        return checkMatch(filterString, itemstack.getDisplayName());
    }
}
