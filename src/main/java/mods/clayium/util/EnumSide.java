package mods.clayium.util;

import net.minecraft.util.EnumFacing;

public enum EnumSide {
    DOWN(0, 0, -1, 0),
    UP(1, 0, 1, 0),
    FRONT(2, 0, 0, -1),
    BACK(3, 0, 0, 1),
    LEFT(4, -1, 0, 0),
    RIGHT(5, 1, 0, 0),
    UNKNOWN(6, 0, 0, 0);

    EnumSide(int index, int x, int y, int z) {
        this.index = index;
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
//            this.offset = new Vec3i(x, y, z);
    }

    final int index;
    final int offsetX, offsetY, offsetZ;
//        final Vec3i offset;
    public static final EnumSide[] VALUES = new EnumSide[] {DOWN, UP, FRONT, BACK, LEFT, RIGHT};

    public static EnumSide getFront(int id) {
        return (id >= 0 && id < values().length) ? values()[id] : UNKNOWN;
    }

    int getIndex() {
        return this.index;
    }

    public EnumFacing toEnumFacing() {
        return EnumFacing.byIndex(this.index);
    }

    public EnumSide getOpposite() {
        return UtilDirection.getOpposite(this);
    }
}
