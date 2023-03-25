package mods.clayium.util;

import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class UtilDirection {
    private static final int[] OPPOSITES    = new int[]{1, 0, 3, 2, 5, 4};
    private static final int[] LEFTSIDES    = new int[]{4, 4, 4, 5, 3, 2};
    private static final int[] RIGHTSIDES   = new int[]{5, 5, 5, 4, 2, 3};
    private static final int[] UPSIDES      = new int[]{2, 3, 1, 1, 1, 1};
    private static final int[] DOWNSIDES    = new int[]{3, 2, 0, 0, 0, 0};

    private static final int[][] ROTATION_MATRIX = new int[][]{
            {0, 1, 4, 5, 3, 2, 6},
            {0, 1, 5, 4, 2, 3, 6},
            {5, 4, 2, 3, 0, 1, 6},
            {4, 5, 2, 3, 1, 0, 6},
            {2, 3, 1, 0, 4, 5, 6},
            {3, 2, 0, 1, 4, 5, 6},
            {0, 1, 2, 3, 4, 5, 6}
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
        if (side == asFront)                                                return EnumSide.FRONT;
        if (side == EnumFacing.getFront(DOWNSIDES[asFront.getIndex()]))     return EnumSide.DOWN;
        if (side == EnumFacing.getFront(UPSIDES[asFront.getIndex()]))       return EnumSide.UP;
        if (side == EnumFacing.getFront(LEFTSIDES[asFront.getIndex()]))     return EnumSide.LEFT;
        if (side == EnumFacing.getFront(RIGHTSIDES[asFront.getIndex()]))    return EnumSide.RIGHT;
        if (side == EnumFacing.getFront(OPPOSITES[asFront.getIndex()]))     return EnumSide.BACK;

        return EnumSide.UNKNOWN;
    }

    @Nullable
    public static EnumFacing getSideOfDirection(EnumFacing asFront, EnumSide side) {
        switch (side) {
            case DOWN:  return EnumFacing.getFront(DOWNSIDES[asFront.getIndex()]);
            case UP:    return EnumFacing.getFront(UPSIDES[asFront.getIndex()]);
            case FRONT: return EnumFacing.getFront(asFront.getIndex());
            case BACK:  return EnumFacing.getFront(OPPOSITES[asFront.getIndex()]);
            case LEFT:  return EnumFacing.getFront(LEFTSIDES[asFront.getIndex()]);
            case RIGHT: return EnumFacing.getFront(RIGHTSIDES[asFront.getIndex()]);
            default:    return null;
        }
    }

    public static EnumSide getSideOfDirection(EnumSide asFront, EnumSide side) {
        switch (side) {
            case DOWN:  return EnumSide.getFront(DOWNSIDES[asFront.getIndex()]);
            case UP:    return EnumSide.getFront(UPSIDES[asFront.getIndex()]);
            case FRONT: return EnumSide.getFront(asFront.getIndex());
            case BACK:  return EnumSide.getFront(OPPOSITES[asFront.getIndex()]);
            case LEFT:  return EnumSide.getFront(LEFTSIDES[asFront.getIndex()]);
            case RIGHT: return EnumSide.getFront(RIGHTSIDES[asFront.getIndex()]);
            default:    return EnumSide.UNKNOWN;
        }
    }
}
