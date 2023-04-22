package mods.clayium.machine.MultiblockMachine;

import com.google.common.collect.ImmutableMap;
import mods.clayium.machine.ClayContainer.BlockStateClaySidedContainer;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;

import java.util.Arrays;
import java.util.List;

public class BlockStateMultiblockMachine extends BlockStateClaySidedContainer {
    public static final PropertyBool IS_CONSTRUCTED = PropertyBool.create("is_constructed");

    public static List<IProperty<?>> getPropertyList() {
        return Arrays.asList(
                FACING, IS_PIPE, IS_CONSTRUCTED,
                ARM_UP, ARM_DOWN, ARM_NORTH, ARM_SOUTH, ARM_WEST, ARM_EAST
        );
    }

    protected BlockStateMultiblockMachine(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
    }

    public static boolean isConstructed(TileEntityMultiblockMachine machine) {
        return machine.getWorld().getBlockState(machine.getPos()).getValue(IS_CONSTRUCTED);
    }

    public static void setConstructed(TileEntityMultiblockMachine machine, boolean b) {
        machine.getWorld().setBlockState(machine.getPos(),
                machine.getWorld().getBlockState(machine.getPos()).withProperty(IS_CONSTRUCTED, b), 3);
    }
}
