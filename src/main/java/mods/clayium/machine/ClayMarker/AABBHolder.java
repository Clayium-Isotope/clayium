package mods.clayium.machine.ClayMarker;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface AABBHolder {
    AxisAlignedBB NULL_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    PropertyEnum<Appearance> APPEARANCE = PropertyEnum.create("appearance", Appearance.class);

    AxisAlignedBB getAxisAlignedBB();

    void setAxisAlignedBB(AxisAlignedBB aabb);

    boolean hasAxisAlignedBB();

    Appearance getBoxAppearance();

    default void applyAppearance(World world, BlockPos pos, IBlockState oldState, Appearance app) {
        world.setBlockState(pos, oldState.withProperty(APPEARANCE, app));
    }

    default void applyAppearance(World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(APPEARANCE, this.getBoxAppearance()));
    }

    enum Appearance implements IStringSerializable {
        NoRender,
        Grid,
        Box_Worker,
        Box_Marker,
        Box_Unused;

        public static int compare(Appearance a, Appearance b) {
            return a.compareTo(b);
        }

        public static Appearance back2(Appearance self) {
            return switch (self) {
                case NoRender, Grid, Box_Worker -> NoRender;
                case Box_Marker -> Grid;
                case Box_Unused -> Box_Worker;
            };
        }

        public static Appearance increment(Appearance self, Appearance overflow) {
            return switch (self) {
                case NoRender -> Grid;
                case Grid -> Box_Worker;
                case Box_Worker -> Box_Marker;
                case Box_Marker -> Box_Unused;
                case Box_Unused -> overflow;
            };
        }

        public int toMeta() {
            return this.ordinal();
        }

        public static Appearance fromMeta(int meta) {
            return switch (meta) {
                case 0 -> NoRender;
                case 1 -> Grid;
                case 2 -> Box_Worker;
                case 3 -> Box_Marker;
                case 4 -> Box_Unused;
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
