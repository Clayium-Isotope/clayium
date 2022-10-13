package mods.clayium.machine.ClayContainer;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

public abstract class ClayDirectionalContainer extends ClayContainer {
    public ClayDirectionalContainer(Material material, Class<? extends TileEntityClayContainer> teClass, String modelPath, int guiId, int tier) {
        super(material, teClass, modelPath, guiId, tier);

        setDefaultState(this.getDefaultState()
                .withProperty(BlockStateClayDirectionalContainer.FACING, EnumFacing.NORTH)
        );
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state.withProperty(BlockStateClayDirectionalContainer.FACING, state.getValue(BlockStateClayDirectionalContainer.FACING)), 2);
        }
    }

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return EnumFacing.VALUES;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (world.isRemote) return false;

        EnumFacing[] axes = getValidRotations(world, pos);
        if (axes == null || axes.length == 0) return false;
        EnumFacing candidacy = Arrays.stream(axes).anyMatch(_axis -> axis == _axis) ? axis : axes[0];

        world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockStateClayDirectionalContainer.FACING, candidacy));

        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(BlockStateClayDirectionalContainer.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockStateClayDirectionalContainer.FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateClayDirectionalContainer.FACING).getIndex();
    }
}
