package mods.clayium.machine.ClayMarker;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

public interface AABBHolder {
    AxisAlignedBB NULL_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    PropertyEnum<Appearance> APPEARANCE = PropertyEnum.create("appearance", Appearance.class);

    AxisAlignedBB getAxisAlignedBB();

    void setAxisAlignedBB(AxisAlignedBB aabb);

    boolean hasAxisAlignedBB();

    Appearance getBoxAppearance();

    enum Appearance implements IStringSerializable {
        NoRender,
        Grid,
        _2,
        Box,
        _4;

        public static int compare(Appearance a, Appearance b) {
            return a.compareTo(b);
        }

        public static Appearance back2(Appearance self) {
            return switch (self) {
                case NoRender, Grid, _2 -> NoRender;
                case Box -> Grid;
                case _4 -> _2;
            };
        }

        public static Appearance increment(Appearance self, Appearance overflow) {
            return switch (self) {
                case NoRender -> Grid;
                case Grid -> _2;
                case _2 -> Box;
                case Box -> _4;
                case _4 -> overflow;
            };
        }

        public int toMeta() {
            return this.ordinal();
        }

        public static Appearance fromMeta(int meta) {
            return switch (meta) {
                case 0 -> NoRender;
                case 1 -> Grid;
                case 2 -> _2;
                case 3 -> Box;
                case 4 -> _4;
                default -> throw new EnumConstantNotPresentException(Appearance.class, "[default]");
            };
        }

        public static Appearance max(Appearance a, Appearance b) {
            return a.compareTo(b) >= 0 ? a : b;
        }

        @Override
        public String getName() {
            return Integer.toString(this.toMeta());
        }
    }
}
