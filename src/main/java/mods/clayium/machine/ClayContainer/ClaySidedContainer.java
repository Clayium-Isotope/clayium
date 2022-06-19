package mods.clayium.machine.ClayContainer;

//import cofh.redstoneflux.api.IEnergyConnection;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public abstract class ClaySidedContainer extends ClayContainer {
    public ClaySidedContainer(Material material, Class<? extends TileEntity> teClass, String modelPath, int guiId) {
        super(material, teClass, modelPath, guiId, -1);
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

        world.setBlockState(pos, world.getBlockState(pos).withProperty(ClaySidedContainerState.FACING, candidacy));

        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(ClaySidedContainerState.FACING, placer.getHorizontalFacing().getOpposite()), 2);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(ClaySidedContainerState.FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ClaySidedContainerStateContainer(this);
    }



    // RAS syndrome
    private static class ClaySidedContainerStateContainer extends BlockStateContainer {
        public ClaySidedContainerStateContainer(ClayContainer blockIn) {
            super(blockIn, ClaySidedContainerState.getPropertyList().toArray(new IProperty[0]));
        }

        @Override
        protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, @Nullable ImmutableMap<net.minecraftforge.common.property.IUnlistedProperty<?>, java.util.Optional<?>> unlistedProperties) {
            return new ClaySidedContainerState(block, properties);
        }
    }

    public static class ClaySidedContainerState extends ClayContainerState {
        public static final PropertyDirection FACING = BlockHorizontal.FACING;

        protected ClaySidedContainerState(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
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
