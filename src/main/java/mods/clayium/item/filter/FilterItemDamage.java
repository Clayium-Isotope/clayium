package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

public class FilterItemDamage extends FilterString {
    public FilterItemDamage() {
        super("filter_metadata", 6);
    }

    @Override
    public boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty()) return false;

        return checkMatch("^" + filterString + "$", String.valueOf(itemstack.getItemDamage()));
    }
}
