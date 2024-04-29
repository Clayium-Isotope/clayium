package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import mods.clayium.util.TierPrefix;

public class FilterBlacklist extends FilterWhitelist {

    public FilterBlacklist() {
        super("filter_blacklist", TierPrefix.advanced);
    }

    @Override
    public boolean test(NBTTagCompound filterTag, ItemStack input) {
        return !super.test(filterTag, input);
    }
}
