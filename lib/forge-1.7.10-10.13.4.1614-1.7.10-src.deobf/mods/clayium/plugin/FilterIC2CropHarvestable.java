package mods.clayium.plugin;

import ic2.api.crops.ICropTile;

import java.util.List;

import mods.clayium.item.ISpecialFilter;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class FilterIC2CropHarvestable
        implements ISpecialFilter {
    public boolean filterMatch(NBTTagCompound filterTag, ItemStack itemstack, boolean lastResult) {
        return false;
    }


    public boolean filterMatch(NBTTagCompound filterTag, World world, int x, int y, int z, boolean lastResult) {
        if (lastResult) return true;
        if (world == null)
            return false;
        Block block = world.getBlock(x, y, z);
        if (block instanceof ic2.core.crop.BlockCrop) {
            TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
            if (te instanceof ICropTile && ((ICropTile) te).getCrop() != null) {
                return !((ICropTile) te).getCrop().canGrow((ICropTile) te);
            }
        }
        return false;
    }

    public void addTooltip(NBTTagCompound filterTag, List list, int indent) {}

    public void openGui(ItemStack itemstack, World world, EntityPlayer player) {}
}
