package mods.clayium.util;

import mods.clayium.machine.ClayContainer.BlockStateClaySidedContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UtilDirection {

    private static final int[] OPPOSITES = new int[] { 1, 0, 3, 2, 5, 4 };
    private static final int[] LEFTSIDES = new int[] { 4, 4, 4, 5, 3, 2 };
    private static final int[] RIGHTSIDES = new int[] { 5, 5, 5, 4, 2, 3 };
    private static final int[] UPSIDES = new int[] { 2, 3, 1, 1, 1, 1 };
    private static final int[] DOWNSIDES = new int[] { 3, 2, 0, 0, 0, 0 };

    private static final int[][] ROTATION_MATRIX = new int[][] {
            { 0, 1, 4, 5, 3, 2, 6 },
            { 0, 1, 5, 4, 2, 3, 6 },
            { 5, 4, 2, 3, 0, 1, 6 },
            { 4, 5, 2, 3, 1, 0, 6 },
            { 2, 3, 1, 0, 4, 5, 6 },
            { 3, 2, 0, 1, 4, 5, 6 },
            { 0, 1, 2, 3, 4, 5, 6 }
    };

    public static EnumSide getSide(EnumFacing facing) {
        return EnumSide.getFront(facing.getIndex());
    }

    public static EnumFacing getOpposite(EnumFacing facing) {
        return facing.getOpposite();
    }

    public static EnumSide getOpposite(EnumSide side) {
        return EnumSide.getFront(OPPOSITES[side.getIndex()]);
    }

    public static EnumSide getRotation(EnumFacing asFront, EnumFacing angular) {
        return EnumSide.getFront(ROTATION_MATRIX[angular.getIndex()][asFront.getIndex()]);
    }

    public static EnumSide getRotation(EnumSide asFront, EnumSide angular) {
        if (asFront == EnumSide.UNKNOWN || angular == EnumSide.UNKNOWN) return EnumSide.UNKNOWN;
        return EnumSide.getFront(ROTATION_MATRIX[angular.getIndex()][asFront.getIndex()]);
    }

    public static EnumSide getSideOfDirection(EnumFacing asFront, EnumFacing side) {
        if (side == asFront) return EnumSide.FRONT;
        if (side == EnumFacing.byIndex(DOWNSIDES[asFront.getIndex()])) return EnumSide.DOWN;
        if (side == EnumFacing.byIndex(UPSIDES[asFront.getIndex()])) return EnumSide.UP;
        if (side == EnumFacing.byIndex(LEFTSIDES[asFront.getIndex()])) return EnumSide.LEFT;
        if (side == EnumFacing.byIndex(RIGHTSIDES[asFront.getIndex()])) return EnumSide.RIGHT;
        if (side == EnumFacing.byIndex(OPPOSITES[asFront.getIndex()])) return EnumSide.BACK;

        return EnumSide.UNKNOWN;
    }

    @Nullable
    public static EnumFacing getSideOfDirection(EnumFacing asFront, EnumSide side) {
        switch (side) {
            case DOWN:
                return EnumFacing.byIndex(DOWNSIDES[asFront.getIndex()]);
            case UP:
                return EnumFacing.byIndex(UPSIDES[asFront.getIndex()]);
            case FRONT:
                return EnumFacing.byIndex(asFront.getIndex());
            case BACK:
                return EnumFacing.byIndex(OPPOSITES[asFront.getIndex()]);
            case LEFT:
                return EnumFacing.byIndex(LEFTSIDES[asFront.getIndex()]);
            case RIGHT:
                return EnumFacing.byIndex(RIGHTSIDES[asFront.getIndex()]);
            default:
                return null;
        }
    }

    public static EnumSide getSideOfDirection(EnumSide asFront, EnumSide side) {
        switch (side) {
            case DOWN:
                return EnumSide.getFront(DOWNSIDES[asFront.getIndex()]);
            case UP:
                return EnumSide.getFront(UPSIDES[asFront.getIndex()]);
            case FRONT:
                return EnumSide.getFront(asFront.getIndex());
            case BACK:
                return EnumSide.getFront(OPPOSITES[asFront.getIndex()]);
            case LEFT:
                return EnumSide.getFront(LEFTSIDES[asFront.getIndex()]);
            case RIGHT:
                return EnumSide.getFront(RIGHTSIDES[asFront.getIndex()]);
            default:
                return EnumSide.UNKNOWN;
        }
    }

    /**
     * @return 上下東西南北のうち、entity が pos を見る向こう側の facing
     */
    public static EnumFacing getDirectionFromEntity(BlockPos pos, EntityLivingBase entity) {
        return EnumFacing.getDirectionFromEntityLiving(pos, entity).getOpposite();
    }

    /**
     * @return 東西南北のうち、entity が向いている facing
     */
    public static EnumFacing getHorizontalFromEntity(EntityLivingBase entity) {
        return entity.getHorizontalFacing();
    }

    @Nonnull
    public static EnumFacing getBetterFront(IBlockState state, BlockPos pos, EntityLivingBase placer) {
        EnumFacing front;
        if (state instanceof BlockStateClaySidedContainer) {
            front = UtilDirection.getHorizontalFromEntity(placer);
        } else {
            front = UtilDirection.getDirectionFromEntity(pos, placer);
        }
        return front.getOpposite();
    }

    public static List<EnumFacing> getFrontedSides(EnumFacing front) {
        return Arrays.stream(EnumSide.VALUES)
                .map(side -> UtilDirection.getSideOfDirection(front, side))
                .collect(Collectors.toList());
    }
}
