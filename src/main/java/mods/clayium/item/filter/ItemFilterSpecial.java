package mods.clayium.item.filter;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.item.ISpecialFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemFilterSpecial
        extends ItemFilterTemp {
    public List<ISpecialFilter> filters = new ArrayList<ISpecialFilter>();

    public ItemFilterSpecial addSpecialFilter(ISpecialFilter specialFilter) {
        this.filters.add(specialFilter);
        return this;
    }

    public boolean filterMatch(NBTTagCompound filterTag, ItemStack itemstack) {
        boolean ret = false;
        for (ISpecialFilter filter : this.filters) {
            ret = filter.filterMatch(filterTag, itemstack, ret);
        }
        return ret;
    }

    public boolean filterMatch(NBTTagCompound filterTag, World world, int x, int y, int z) {
        boolean ret = false;
        for (ISpecialFilter filter : this.filters) {
            ret = filter.filterMatch(filterTag, world, x, y, z, ret);
        }
        return ret;
    }

    public void addTooltip(NBTTagCompound filterTag, List list, int indent) {
        for (ISpecialFilter filter : this.filters) {
            filter.addTooltip(filterTag, list, indent);
        }
    }

    public void openGui(ItemStack itemstack, World world, EntityPlayer player) {
        for (ISpecialFilter filter : this.filters)
            filter.openGui(itemstack, world, player);
    }
}
