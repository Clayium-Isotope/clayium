package mods.clayium.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IOverclocker {
    double getOverclockFactor(IBlockAccess world, BlockPos pos);
}
