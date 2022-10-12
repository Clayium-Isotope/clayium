package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

public class FilterRegistryName extends FilterString {
    public FilterRegistryName() {
        super("filter_id", 6);
    }

    @Override
    protected boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty() || itemstack.getItem().getRegistryName() == null) return false;

        return checkMatch(filterString, itemstack.getItem().getRegistryName().toString());
    }
}
