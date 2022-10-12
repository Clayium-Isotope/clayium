package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

public class FilterUnlocalizedName extends FilterString {
    public FilterUnlocalizedName() {
        super("filter_unl10n", 6);
    }

    @Override
    protected boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty()) return false;

        return checkMatch(filterString, itemstack.getUnlocalizedName());
    }
}
