package mods.clayium.block.itemblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IOverridableBlock {

    void overrideTo(ItemStack stackIn, EntityPlayer playerIn, World worldIn, BlockPos posIn, EnumFacing side,
                    float hitX, float hitY, float hitZ, IBlockState overriddenState, Class overriddenTEClass,
                    NBTTagCompound overriddenTETag);

    boolean canOverride(ItemStack stackIn, EntityPlayer playerIn, World worldIn, BlockPos posIn, EnumFacing side,
                        float hitX, float hitY, float hitZ);

    void onOverridden(ItemStack stackIn, EntityPlayer playerIn, World worldIn, BlockPos posIn, EnumFacing side,
                      float hitX, float hitY, float hitZ);
}
