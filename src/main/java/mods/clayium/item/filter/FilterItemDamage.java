package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

import mods.clayium.util.TierPrefix;

public class FilterItemDamage extends FilterString {

    public FilterItemDamage() {
        super("filter_metadata", TierPrefix.precision);
    }

    @Override
    public boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty()) return false;

        return checkMatch("^" + filterString + "$", String.valueOf(itemstack.getItemDamage()));
    }
}
