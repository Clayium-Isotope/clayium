package mods.clayium.util;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;


public enum UtilDirection {
    DOWN(0, -1, 0),


    UP(0, 1, 0),


    NORTH(0, 0, -1),


    SOUTH(0, 0, 1),


    WEST(-1, 0, 0),


    EAST(1, 0, 0),


    DOWNSIDE(0, -1, 0, 1),
    UPSIDE(0, 1, 0, 1),
    FRONTSIDE(0, 0, -1, 1),
    BACKSIDE(0, 0, 1, 1),
    LEFTSIDE(-1, 0, 0, 1),
    RIGHTSIDE(1, 0, 0, 1),
    UNKNOWN(0, 0, 0);

    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;

    static {
        VALID_DIRECTIONS = new UtilDirection[] {DOWN, UP, NORTH, SOUTH, WEST, EAST, DOWNSIDE, UPSIDE, FRONTSIDE, BACKSIDE, LEFTSIDE, RIGHTSIDE};

        OPPOSITES = new int[] {1, 0, 3, 2, 5, 4};

        LEFTSIDES = new int[] {4, 4, 4, 5, 3, 2};
        RIGHTSIDES = new int[] {5, 5, 5, 4, 2, 3};
        UPSIDES = new int[] {2, 3, 1, 1, 1, 1};
        DOWNSIDES = new int[] {3, 2, 0, 0, 0, 0};

        ROTATION_MATRIX = new int[][] {{0, 1, 4, 5, 3, 2, 6}, {0, 1, 5, 4, 2, 3, 6}, {5, 4, 2, 3, 0, 1, 6}, {4, 5, 2, 3, 1, 0, 6}, {2, 3, 1, 0, 4, 5, 6}, {3, 2, 0, 1, 4, 5, 6}, {0, 1, 2, 3, 4, 5, 6}};
    }

    public final int flag;
    public final int sideFlag;
    public static final UtilDirection[] VALID_DIRECTIONS;
    public static final int[] OPPOSITES;
    public static final int[] LEFTSIDES;
    public static final int[] RIGHTSIDES;
    public static final int[] UPSIDES;
    public static final int[] DOWNSIDES;
    public static final int[][] ROTATION_MATRIX;
    private static volatile int[][] side2DirectionMap;
    private static volatile int[][] direction2SideMap;

    UtilDirection(int x, int y, int z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.flag = 1 << ordinal();
        this.sideFlag = 0;
    }

    UtilDirection(int x, int y, int z, int s) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.flag = 1 << ordinal();
        this.sideFlag = s;
    }


    public static UtilDirection getOrientation(int id) {
        if (id >= 0 && id < VALID_DIRECTIONS.length) {
            return VALID_DIRECTIONS[id];
        }
        return UNKNOWN;
    }


    public static UtilDirection getSide(int id) {
        if (id >= 0 && id < 6) {
            return VALID_DIRECTIONS[id + 6];
        }
        if (id >= 6 && id < 12) {
            return VALID_DIRECTIONS[id];
        }
        return UNKNOWN;
    }


    public boolean isDirection() {
        return (this != UNKNOWN && this.sideFlag == 0);
    }


    public boolean isSide() {
        return (this.sideFlag == 1);
    }


    public UtilDirection getOpposite() {
        return isSide() ? getSide(OPPOSITES[ordinal() - 6]) : getOrientation(OPPOSITES[ordinal()]);
    }


    public UtilDirection getRotation(UtilDirection axis) {
        if ((axis.isDirection() && isSide()) || (isDirection() && axis.isSide()) || axis == UNKNOWN || this == UNKNOWN) {
            return UNKNOWN;
        }
        return isSide() ? getSide(ROTATION_MATRIX[axis.ordinal() - 6][ordinal() - 6]) : getOrientation(ROTATION_MATRIX[axis.ordinal()][ordinal()]);
    }


    public UtilDirection getSideOfDirection(UtilDirection side) {
        if (side.isDirection()) {
            if (this == side)
                return FRONTSIDE;
            if (getOrientation(DOWNSIDES[ordinal()]) == side)
                return DOWNSIDE;
            if (getOrientation(UPSIDES[ordinal()]) == side)
                return UPSIDE;
            if (getOrientation(LEFTSIDES[ordinal()]) == side)
                return LEFTSIDE;
            if (getOrientation(RIGHTSIDES[ordinal()]) == side)
                return RIGHTSIDE;
            if (getOrientation(OPPOSITES[ordinal()]) == side) {
                return BACKSIDE;
            }
        }
        int i = isDirection() ? ordinal() : (ordinal() - 6);
        switch (side) {
            case DOWNSIDE:
                return isDirection() ? getOrientation(DOWNSIDES[i]) : getSide(DOWNSIDES[i]);
            case UPSIDE:
                return isDirection() ? getOrientation(UPSIDES[i]) : getSide(UPSIDES[i]);
            case FRONTSIDE:
                return isDirection() ? getOrientation(i) : getSide(i);
            case BACKSIDE:
                return isDirection() ? getOrientation(OPPOSITES[i]) : getSide(OPPOSITES[i]);
            case LEFTSIDE:
                return isDirection() ? getOrientation(LEFTSIDES[i]) : getSide(LEFTSIDES[i]);
            case RIGHTSIDE:
                return isDirection() ? getOrientation(RIGHTSIDES[i]) : getSide(RIGHTSIDES[i]);
        }
        return UNKNOWN;
    }


    public UtilDirection getSide(UtilDirection side) {
        return getSideOfDirection(side);
    }


    public static synchronized int side2Direction(int frontDirection, int side) {
        if (side2DirectionMap == null) {
            side2DirectionMap = new int[12][12];
            for (int i = 0; i < side2DirectionMap.length; i++) {
                for (int j = 0; j < (side2DirectionMap[i]).length; j++) {
                    side2DirectionMap[i][j] = -1;
                }
            }
        }
        if (frontDirection >= 12 || side >= 12 || frontDirection < 0 || side < 0)
            return getOrientation(frontDirection).getSideOfDirection(getSide(side)).ordinal();
        if (side2DirectionMap[frontDirection][side] == -1) {
            side2DirectionMap[frontDirection][side] = getOrientation(frontDirection).getSideOfDirection(getSide(side)).ordinal();
        }
        return side2DirectionMap[frontDirection][side];
    }


    public static synchronized int direction2Side(int frontDirection, int theDirection) {
        if (direction2SideMap == null) {
            direction2SideMap = new int[12][12];
            for (int i = 0; i < direction2SideMap.length; i++) {
                for (int j = 0; j < (direction2SideMap[i]).length; j++) {
                    direction2SideMap[i][j] = -1;
                }
            }
        }
        if (frontDirection >= 12 || theDirection >= 12 || frontDirection < 0 || theDirection < 0)
            return getOrientation(frontDirection).getSideOfDirection(getOrientation(theDirection)).ordinal();
        if (direction2SideMap[frontDirection][theDirection] == -1) {
            direction2SideMap[frontDirection][theDirection] = getOrientation(frontDirection).getSideOfDirection(getOrientation(theDirection)).ordinal();
        }
        return direction2SideMap[frontDirection][theDirection];
    }

    public ForgeDirection toForgeDirection() {
        return ForgeDirection.getOrientation(ordinal());
    }

    public Vec3 toVec3() {
        return Vec3.createVectorHelper(this.offsetX, this.offsetY, this.offsetZ);
    }

    public Block getBlock(IBlockAccess world, int x, int y, int z) {
        return world.getBlock(x + this.offsetX, y + this.offsetY, z + this.offsetZ);
    }

    public static Block getBlock(IBlockAccess world, int x, int y, int z, UtilDirection direction) {
        return direction.getBlock(world, x, y, z);
    }

    public static Block getBlock(IBlockAccess world, int x, int y, int z, ForgeDirection direction) {
        return world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
    }

    public int getBlockMetadata(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x + this.offsetX, y + this.offsetY, z + this.offsetZ);
    }

    public static int getBlockMetadata(IBlockAccess world, int x, int y, int z, UtilDirection direction) {
        return direction.getBlockMetadata(world, x, y, z);
    }

    public static int getBlockMetadata(IBlockAccess world, int x, int y, int z, ForgeDirection direction) {
        return world.getBlockMetadata(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
    }

    public TileEntity getTileEntity(IBlockAccess world, int x, int y, int z) {
        return UtilBuilder.safeGetTileEntity(world, x + this.offsetX, y + this.offsetY, z + this.offsetZ);
    }

    public static TileEntity getTileEntity(IBlockAccess world, int x, int y, int z, UtilDirection direction) {
        return direction.getTileEntity(world, x, y, z);
    }

    public static TileEntity getTileEntity(IBlockAccess world, int x, int y, int z, ForgeDirection direction) {
        return UtilBuilder.safeGetTileEntity(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
    }
}


