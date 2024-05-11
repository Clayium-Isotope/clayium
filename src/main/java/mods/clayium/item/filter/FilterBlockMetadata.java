package mods.clayium.item.filter;

import mods.clayium.util.TierPrefix;
import net.minecraft.item.ItemStack;

// TODO Checking Block and its Blockstate
@Deprecated // not yet
public class FilterBlockMetadata extends FilterString {

    public FilterBlockMetadata() {
        super("filter_block_metadata", TierPrefix.precision);
    }

    @Override
    protected boolean filterStringMatch(String filterString, ItemStack itemstack) {
        return false;
    }
}
