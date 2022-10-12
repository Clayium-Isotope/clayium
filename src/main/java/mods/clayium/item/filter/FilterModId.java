package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

public class FilterModId extends FilterString {
    public FilterModId() {
        super("filter_mod", 6);
    }

    @Override
    protected boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty() || itemstack.getItem().getRegistryName() == null) return false;

        return checkMatch(filterString, itemstack.getItem().getRegistryName().getResourceDomain());
    }
}
