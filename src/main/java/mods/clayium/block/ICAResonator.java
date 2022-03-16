package mods.clayium.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ICAResonator {
    double getResonance(IBlockAccess world, BlockPos pos);
}
