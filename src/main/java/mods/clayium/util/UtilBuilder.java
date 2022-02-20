package mods.clayium.util;

import mods.clayium.block.ClayContainer;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilBuilder {
    public static boolean rotateBlockByWrench(World world, BlockPos pos, EnumFacing side) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof ClayContainer) return false;

        EnumFacing direction = EnumFacing.getFront(side.ordinal());
        EnumFacing[] axes = block.getValidRotations(world, pos);
        if (axes == null || axes.length == 0) return false;

        EnumFacing axis = axes[0];
        for (EnumFacing axis1 : axes) {
            if (axis1 == direction)
                axis = axis1;
        }
        return block.rotateBlock(world, pos, axis);
    }
}
