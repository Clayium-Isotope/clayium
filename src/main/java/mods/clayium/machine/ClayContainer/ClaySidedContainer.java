package mods.clayium.machine.ClayContainer;

//import cofh.redstoneflux.api.IEnergyConnection;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

public abstract class ClaySidedContainer extends ClayContainer {
    public ClaySidedContainer(Material material, Class<? extends TileEntityClayContainer> teClass, String modelPath, int guiId, int tier) {
        super(material, teClass, modelPath, guiId, tier);

        setDefaultState(this.getDefaultState()
                .withProperty(BlockStateClaySidedContainer.FACING, EnumFacing.NORTH)
        );
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            IBlockState north = worldIn.getBlockState(pos.north());
            IBlockState east = worldIn.getBlockState(pos.east());
            IBlockState south = worldIn.getBlockState(pos.south());
            IBlockState west = worldIn.getBlockState(pos.west());
            EnumFacing face = state.getValue(BlockStateClaySidedContainer.FACING);

            if (face == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) face = EnumFacing.SOUTH;
            else if (face == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) face = EnumFacing.NORTH;
            else if (face == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) face = EnumFacing.WEST;
            else if (face == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) face = EnumFacing.EAST;

            worldIn.setBlockState(pos, state.withProperty(BlockStateClaySidedContainer.FACING, face), 2);
        }
    }

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return EnumFacing.HORIZONTALS;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (world.isRemote) return false;

        EnumFacing[] axes = getValidRotations(world, pos);
        if (axes == null || axes.length == 0) return false;
        EnumFacing candidacy = Arrays.stream(axes).anyMatch(_axis -> axis == _axis) ? axis : axes[0];

        world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockStateClaySidedContainer.FACING, candidacy));

        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(BlockStateClaySidedContainer.FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing face = EnumFacing.getFront(meta);
        if (face.getAxis() == EnumFacing.Axis.Y) face = EnumFacing.NORTH;
        return this.getDefaultState().withProperty(BlockStateClaySidedContainer.FACING, face);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateClaySidedContainer.FACING).getIndex();
    }
}
