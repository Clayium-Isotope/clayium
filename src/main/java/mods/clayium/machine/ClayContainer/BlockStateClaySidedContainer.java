package mods.clayium.machine.ClayContainer;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import java.util.Arrays;
import java.util.List;

public class BlockStateClaySidedContainer extends BlockStateClayContainer {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    protected BlockStateClaySidedContainer(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
    }

    public static List<IProperty<?>> getPropertyList() {
        return Arrays.asList(
                FACING, IS_PIPE,
                ARM_UP, ARM_DOWN, ARM_NORTH, ARM_SOUTH, ARM_WEST, ARM_EAST
        );
    }

    @Override
    public IBlockState withRotation(Rotation rot) {
        return this.withProperty(FACING, rot.rotate(this.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(Mirror mirrorIn) {
        return this.withRotation(mirrorIn.toRotation(this.getValue(FACING)));
    }
}
