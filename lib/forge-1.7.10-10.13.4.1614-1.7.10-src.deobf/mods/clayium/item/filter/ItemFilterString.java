package mods.clayium.item.filter;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.core.ClayiumCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class ItemFilterString
        extends ItemFilterTemp {
    public boolean filterMatch(NBTTagCompound filterTag, ItemStack itemstack) {
        return (filterTag == null) ? false : filterStringMatch(filterTag.getString("FilterString"), itemstack);
    }


    public boolean filterMatch(NBTTagCompound filterTag, World world, int x, int y, int z) {
        if (filterTag == null) return false;
        String filterString = filterTag.getString("FilterString");
        return shouldApplySpecialPatternForBlock(filterString, world, x, y, z) ? filterStringMatch(filterString, world, x, y, z) : super.filterMatch(filterTag, world, x, z, z);
    }

    public abstract boolean filterStringMatch(String paramString, ItemStack paramItemStack);

    public boolean shouldApplySpecialPatternForBlock(String filterString, World world, int x, int y, int z) { return false; }

    public boolean filterStringMatch(String filterString, World world, int x, int y, int z) {
        return false;
    }


    public void openGui(ItemStack itemstack, World world, EntityPlayer player) {
        player.openGui(ClayiumCore.INSTANCE, 21, world, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    /*    */
    @SideOnly(Side.CLIENT)
    public void addTooltip(NBTTagCompound filterTag, List list, int indent) {
        if (list.size() >= 100)
            return;
        if (filterTag != null) {
            String in = "";
            for (int i = 0; i < indent; i++)
                in = in + " ";
            String filter = filterTag.getString("FilterString");
            if (filter != null && !filter.equals("")) {
                list.add(in + filter);
            }
        }
    }


    public int getFilterSize(NBTTagCompound filterTag) {
        return 1;
    }
}
