package mods.clayium.item.filter;

import mods.clayium.util.TierPrefix;
import net.minecraft.item.ItemStack;

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
