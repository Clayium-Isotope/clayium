package mods.clayium.machine.QuartzCrucible;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockStateQuartzCrucible extends BlockStateContainer.StateImplementation {

    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 9);
    public static final AxisAlignedBB CRUCIBLE_AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 0.75d, 1.0d);
    protected AxisAlignedBB currentAABB = CRUCIBLE_AABB;

    public BlockStateQuartzCrucible(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
    }

    /**
     * Reproduction of protected {@link Block#addCollisionBoxToList(BlockPos, AxisAlignedBB, List, AxisAlignedBB)}
     * behavior as done in {@link BlockStairs}.
     */
    @Override
    public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB entityBox,
                                      List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn,
                                      boolean isActualState) {
        collidingBoxes.addAll(getAABBList().stream().map(e -> e.offset(pos)).filter(entityBox::intersects)
                .collect(Collectors.toList()));
    }

    private static List<AxisAlignedBB> getAABBList() {
        List<AxisAlignedBB> collidingBoxes = new ArrayList<>(6);

        collidingBoxes.add(new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 0.0675d, 1.0d));
        double f = 1d / 16;
        collidingBoxes.add(new AxisAlignedBB(0.0d, 0.0d, 0.0d, f, 0.75d, 1.0d));
        collidingBoxes.add(new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 0.75d, f));
        collidingBoxes.add(new AxisAlignedBB(1.0d - f, 0.0d, 0.0d, 1.0d, 0.75d, 1.0d));
        collidingBoxes.add(new AxisAlignedBB(0.0d, 0.0d, 1.0d - f, 1.0d, 0.75d, 1.0d));

        return collidingBoxes;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos) {
        return currentAABB;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        if (facing == EnumFacing.UP)
            return BlockFaceShape.BOWL;
        if (facing == EnumFacing.DOWN)
            return BlockFaceShape.SOLID;
        return BlockFaceShape.UNDEFINED;
    }
}
