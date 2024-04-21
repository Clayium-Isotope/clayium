package mods.clayium.machine.ClayMarker;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockStateClayMarker extends BlockStateContainer.StateImplementation {
    protected static final float radius = 0.1875F;
    protected static final AxisAlignedBB markerBB = new AxisAlignedBB(0.5F - radius, 0.5F - radius, 0.5F - radius, 0.5F + radius, 0.5F + radius, 0.5F + radius);

    protected BlockStateClayMarker(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockAccess source, BlockPos pos) {
        return markerBB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockAccess worldIn, BlockPos pos) {
        return markerBB;
    }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType() {
        return EnumBlockRenderType.MODEL;
    }
}
