package mods.clayium.item.filter;

import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;

public class FilterOreDict extends FilterString {
    public FilterOreDict() {
        super("filter_od", 6);
    }

    @Override
    protected boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty()) return false;

        for(String orename : UtilItemStack.getOreNames(itemstack)) {
            if (checkMatch(filterString, orename)) return true;
        }

        return false;
    }
}
