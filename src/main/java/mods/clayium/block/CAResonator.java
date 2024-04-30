package mods.clayium.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.Block)
public interface CAResonator {

    double getResonance(IBlockAccess world, BlockPos pos);
}
