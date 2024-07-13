package mods.clayium.machine.ClayMarker;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

public interface AABBHolder {
    AxisAlignedBB NULL_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    PropertyEnum<Appearance> APPEARANCE = PropertyEnum.create("appearance", Appearance.class);

    AxisAlignedBB getAxisAlignedBB();

    void setAxisAlignedBB(AxisAlignedBB var1);

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
            switch (self) {
                case NoRender:
                case Grid:
                case _2: return NoRender;
                case Box: return Grid;
                case _4: return _2;
                default: throw new EnumConstantNotPresentException(Appearance.class, "[default]");
            }
        }

        public static Appearance increment(Appearance self, Appearance overflow) {
            switch (self) {
                case NoRender: return Grid;
                case Grid: return _2;
                case _2: return Box;
                case Box: return _4;
                case _4: return overflow;
                default: throw new EnumConstantNotPresentException(Appearance.class, "[default]");
            }
        }

        public int toMeta() {
            return this.ordinal();
        }

        public static Appearance fromMeta(int meta) {
            switch (meta) {
                case 0: return NoRender;
                case 1: return Grid;
                case 2: return _2;
                case 3: return Box;
                case 4: return _4;
                default: throw new EnumConstantNotPresentException(Appearance.class, "[default]");
            }
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
