package mods.clayium.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class TileGeneric extends TileEntity {
    protected static Random random = new Random();

    public boolean isUsable(ItemStack itemStack, EntityPlayer player, EnumFacing direction, float hitX, float hitY, float hitZ) {
//        return getItemUseMode(itemStack, player) != -1;
        return true;
    }

    public void useItem(ItemStack stackIn, EntityPlayer playerIn, EnumFacing direction, float hitX, float hitY, float hitZ) {
        if (!this.world.isRemote) {
            useItemFromSide(stackIn, playerIn, direction, getItemUseMode(stackIn, playerIn));
        }
    }

    public boolean hasSpecialDrops() {
        return false;
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ItemStack stack = getNormalDrop(world.getBlockState(pos).getBlock(), fortune);
        drops.add(stack);
    }

    public static ItemStack getNormalDrop(Block blockIn, int fortune) {
        return new ItemStack(blockIn.getItemDropped(blockIn.getDefaultState(), random, fortune));
    }

    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        return -1;
    }

    public void useItemFromSide(ItemStack itemStack, EntityPlayer player, EnumFacing side, int mode) {}

    public void pushButton(EntityPlayer playerIn, int button) {}

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        updateEntity();
    }

    public void updateEntity() {
        if (!this.world.isRemote) {
//            world.markBlockRangeForRenderUpdate(pos, pos);
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }
}
