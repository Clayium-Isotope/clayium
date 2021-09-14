package mods.clayium.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class TileGeneric extends TileEntity implements IGeneralInterface {
    private boolean removeFlag = false;
    protected static Random random = new Random();

    public boolean isUsable(ItemStack itemStack, EntityPlayer player, EnumFacing direction, float hitX, float hitY, float hitZ) {
        return getItemUseMode(itemStack, player) != -1;
    }

    public void useItem(ItemStack stackIn, EntityPlayer playerIn, EnumFacing direction, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            useItemFromSide(stackIn, playerIn, direction, getItemUseMode(stackIn, playerIn));
        }

        this.setInstantSyncFlag();

        for (EnumFacing dir : EnumFacing.VALUES) {
            TileEntity tile = world.getTileEntity(pos.add(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ()));
            if (tile instanceof IGeneralInterface) {
                ((IGeneralInterface)tile).setInstantSyncFlag();
            }
        }
    }

    public void setRemoveFlag() {
        removeFlag = true;
    }

    public boolean getRemoveFlag() {
        return removeFlag;
    }

    public boolean shouldRefresh() {
        return true;
    }

    public boolean hasSpecialDrops() {
        return false;
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ItemStack stack = getNormalDrop(world.getBlockState(pos).getBlock(), fortune);
        writeTileEntityTagToItemStack(stack, this);
        drops.add(stack);
    }

    public void onBlockPlacedBy(World world, BlockPos pos, EntityLivingBase entity, ItemStack itemstack) {
        readTileEntityTagFromItemStack(itemstack, this, pos);
    }

    public static ItemStack getNormalDrop(Block blockIn, int fortune) {
        Item item = blockIn.getItemDropped(blockIn.getDefaultState(), random, fortune);
        return new ItemStack(item);
    }

    public static void writeTileEntityTagToItemStack(ItemStack item, TileEntity tile) {
        if (item != null && tile != null) {
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound tetag = tile.writeToNBT(new NBTTagCompound());
            tetag.removeTag("x");
            tetag.removeTag("y");
            tetag.removeTag("z");
            tag.setTag("TileEntityNBTTag", tetag);
            item.setTagCompound(tag);
        }
    }

    public static void readTileEntityTagFromItemStack(ItemStack item, TileEntity tile, BlockPos pos) {
        NBTTagCompound tetag = getTileEntityTag(item);
        if (tetag != null) {
            tetag.setInteger("x", pos.getX());
            tetag.setInteger("y", pos.getY());
            tetag.setInteger("z", pos.getZ());
            tile.readFromNBT(tetag);
        }
    }

    public static NBTTagCompound getTileEntityTag(ItemStack item) {
        if (item != null && item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("TileEntityNBTTag")) {
                return (NBTTagCompound)tag.getTag("TileEntityNBTTag");
            }
        }

        return null;
    }

    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        return -1;
    }

    public void useItemFromSide(ItemStack itemStack, EntityPlayer player, EnumFacing side, int mode) {}

    @Override
    public void markForStrongUpdate() {

    }

    @Override
    public void markForWeakUpdate() {

    }

    @Override
    public void setSyncFlag() {

    }

    @Override
    public void setInstantSyncFlag() {

    }

    @Override
    public void setRenderSyncFlag() {

    }

    @Override
    public void pushButton(EntityPlayer playerIn, int button) {

    }
}
