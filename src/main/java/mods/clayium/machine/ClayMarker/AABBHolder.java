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
        _0,
        _1,
        _2,
        _3,
        _4;

        public static int compare(Appearance a, Appearance b) {
            return a.compareTo(b);
        }

        public static Appearance back2(Appearance self) {
            switch (self) {
                case _0:
                case _1:
                case _2: return _0;
                case _3: return _1;
                case _4: return _2;
                default: throw new EnumConstantNotPresentException(Appearance.class, "[default]");
            }
        }

        public static Appearance increment(Appearance self, Appearance overflow) {
            switch (self) {
                case _0: return _1;
                case _1: return _2;
                case _2: return _3;
                case _3: return _4;
                case _4: return overflow;
                default: throw new EnumConstantNotPresentException(Appearance.class, "[default]");
            }
        }

        public int toMeta() {
            return this.ordinal();
        }

        public static Appearance fromMeta(int meta) {
            switch (meta) {
                case 0: return _0;
                case 1: return _1;
                case 2: return _2;
                case 3: return _3;
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
