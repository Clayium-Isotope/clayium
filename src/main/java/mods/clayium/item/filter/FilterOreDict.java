package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilItemStack;

public class FilterOreDict extends FilterString {

    public FilterOreDict() {
        super("filter_od", TierPrefix.precision);
    }

    @Override
    protected boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty()) return false;

        for (String orename : UtilItemStack.getOreNames(itemstack)) {
            if (checkMatch(filterString, orename)) return true;
        }

        return false;
    }
}
