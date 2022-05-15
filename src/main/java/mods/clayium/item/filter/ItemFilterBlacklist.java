package mods.clayium.item.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemFilterBlacklist
        extends ItemFilterWhitelist {
    public boolean filterMatch(NBTTagCompound filterTag, ItemStack itemstack) {
        return !super.filterMatch(filterTag, itemstack);
    }

    public boolean filterMatch(NBTTagCompound filterTag, World world, int x, int y, int z) {
        return !super.filterMatch(filterTag, world, x, y, z);
    }
}
