package mods.clayium.item.filter;

import mods.clayium.util.TierPrefix;
import net.minecraft.item.ItemStack;

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
