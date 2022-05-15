package mods.clayium.item.filter;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemFilterWhitelist
        extends ItemFilterTemp {
    private boolean fuzzy = false;

    public ItemFilterWhitelist() {
        this(false);
    }

    public ItemFilterWhitelist(boolean fuzzy) {
        this.fuzzy = fuzzy;
    }


    public boolean filterMatch(NBTTagCompound filterTag, ItemStack itemstack) {
        if (filterTag == null)
            return false;
        ItemStack[] filters = UtilItemStack.tagList2Items(filterTag.getTagList("Items", 10));
        if (filters == null)
            return false;
        for (ItemStack filter : filters) {
            if ((ItemFilterTemp.isFilter(filter) && ItemFilterTemp.match(filter, itemstack)) ||
                    ItemFilterTemp.matchBetweenItemstacks(filter, itemstack, this.fuzzy)) {
                return true;
            }
        }
        return false;
    }


    public boolean filterMatch(NBTTagCompound filterTag, World world, int x, int y, int z) {
        if (filterTag == null)
            return false;
        ItemStack[] filters = UtilItemStack.tagList2Items(filterTag.getTagList("Items", 10));
        if (filters == null)
            return false;
        for (ItemStack filter : filters) {
            if ((ItemFilterTemp.isFilter(filter) && ItemFilterTemp.match(filter, world, x, y, z)) ||
                    ItemFilterTemp.matchBetweenItemstacks(filter, UtilBuilder.getItemBlock(world, x, y, z), this.fuzzy)) {
                return true;
            }
        }
        return false;
    }


    public void openGui(ItemStack itemstack, World world, EntityPlayer player) {
        player.openGui(ClayiumCore.INSTANCE, 20, world, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @SideOnly(Side.CLIENT)
    public void addTooltip(NBTTagCompound filterTag, List list, int indent) {
        if (list.size() >= 100)
            return;
        if (filterTag != null) {
            String in = "";
            for (int i = 0; i < indent; i++)
                in = in + " ";
            ItemStack[] filters = UtilItemStack.tagList2Items(filterTag.getTagList("Items", 10));
            for (ItemStack filter : filters) {
                if (filter != null) {
                    list.add(in + filter.getDisplayName());
                    if (ItemFilterTemp.isFilter(filter)) {
                        ((IItemFilter) filter.getItem()).addTooltip(filter.stackTagCompound, list, indent + 1);
                    }
                }
            }
        }
    }

    public int getFilterSize(NBTTagCompound filterTag) {
        int res = 0;
        if (filterTag != null) {
            ItemStack[] filters = UtilItemStack.tagList2Items(filterTag.getTagList("Items", 10));
            for (ItemStack filter : filters) {
                if (filter != null) {
                    res++;
                    if (filter.getItem() instanceof IItemWithFilterSize)
                        res += ((IItemWithFilterSize) filter.getItem()).getFilterSize(filter.getTagCompound());
                }
            }
        }
        return res;
    }
}
