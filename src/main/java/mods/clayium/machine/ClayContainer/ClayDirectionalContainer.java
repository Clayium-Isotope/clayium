package mods.clayium.machine.ClayContainer;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public abstract class ClayDirectionalContainer extends ClayContainer {
    public ClayDirectionalContainer(Material material, Class<? extends TileEntityClayContainer> teClass, String modelPath, int guiId, int tier) {
        super(material, teClass, modelPath, guiId, tier);

        setDefaultState(this.getDefaultState()
                .withProperty(ClayDirectionalContainerState.FACING, EnumFacing.NORTH)
        );
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state.withProperty(ClayDirectionalContainerState.FACING, state.getValue(ClayDirectionalContainerState.FACING)), 2);
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

        world.setBlockState(pos, world.getBlockState(pos).withProperty(ClayDirectionalContainerState.FACING, candidacy));

        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(ClayDirectionalContainerState.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ClayDirectionalContainerState.FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ClayDirectionalContainerState.FACING).getIndex();
    }

    public static class ClayDirectionalContainerState extends ClayContainerState {
        public static final PropertyDirection FACING = BlockDirectional.FACING;

        protected ClayDirectionalContainerState(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
            super(blockIn, propertiesIn);
        }

        public static List<IProperty<?>> getPropertyList() {
            return Arrays.asList(
                    FACING, IS_PIPE,
                    ARM_UP, ARM_DOWN, ARM_NORTH, ARM_SOUTH, ARM_WEST, ARM_EAST
            );
        }

        @Override
        public IBlockState withRotation(Rotation rot) {
            return this.withProperty(FACING, rot.rotate(this.getValue(FACING)));
        }

        @Override
        public IBlockState withMirror(Mirror mirrorIn) {
            return this.withRotation(mirrorIn.toRotation(this.getValue(FACING)));
        }
    }
}
