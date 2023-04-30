package mods.clayium.block;

import mods.clayium.util.UsedFor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@UsedFor(UsedFor.Type.Block)
public interface ICAResonator {
    double getResonance(IBlockAccess world, BlockPos pos);
}
