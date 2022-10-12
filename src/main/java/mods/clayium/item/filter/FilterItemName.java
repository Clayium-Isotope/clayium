package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

public class FilterItemName extends FilterString {
    public FilterItemName() {
        super("filter_name", 6);
    }

    @Override
    protected boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty()) return false;

        return checkMatch(filterString, itemstack.getDisplayName());
    }
}
