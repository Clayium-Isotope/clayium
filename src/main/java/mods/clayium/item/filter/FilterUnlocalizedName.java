package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;

import mods.clayium.util.TierPrefix;

public class FilterUnlocalizedName extends FilterString {

    public FilterUnlocalizedName() {
        super("filter_unl10n", TierPrefix.precision);
    }

    @Override
    protected boolean filterStringMatch(String filterString, ItemStack itemstack) {
        if (itemstack.isEmpty()) return false;

        return checkMatch(filterString, itemstack.getTranslationKey());
    }
}
