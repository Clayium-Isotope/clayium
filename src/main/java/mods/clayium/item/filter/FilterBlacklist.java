package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FilterBlacklist extends FilterWhitelist {
    public FilterBlacklist() {
        super("filter_blacklist", 5);
    }

    @Override
    public boolean test(NBTTagCompound filterTag, ItemStack input) {
        return !super.test(filterTag, input);
    }
}
