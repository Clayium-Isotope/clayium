package mods.clayium.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class FilterBlockHarvestable
        implements ISpecialFilter {
    public boolean filterMatch(NBTTagCompound filterTag, ItemStack itemstack, boolean lastResult) {
        return false;
    }


    public boolean filterMatch(NBTTagCompound filterTag, World world, int x, int y, int z, boolean lastResult) {
        if (lastResult) return true;
        if (world == null)
            return false;
        Block block = world.getBlock(x, y, z);
        if (block instanceof IGrowable) {
            return !((IGrowable) block).func_149851_a(world, x, y, z, world.isRemote);
        }
        return false;
    }

    public void addTooltip(NBTTagCompound filterTag, List list, int indent) {}

    public void openGui(ItemStack itemstack, World world, EntityPlayer player) {}
}
