package mods.clayium.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface ICAMachine {

    int RESONATE_RANGE = 2;

    /**
     * 副作用がない({@link CAResonator#getResonance}には依存)のでpublic
     */
    static double getResonance(IBlockAccess world, BlockPos caMachine) {
        double totalResonance = 1.0;

        for (BlockPos pos : BlockPos.getAllInBox(-RESONATE_RANGE, -RESONATE_RANGE, -RESONATE_RANGE, RESONATE_RANGE,
                RESONATE_RANGE, RESONATE_RANGE)) {
            Block block = world.getBlockState(caMachine.add(pos)).getBlock();
            if (block instanceof CAResonator) {
                totalResonance *= ((CAResonator) block).getResonance(world, caMachine.add(pos));
            }
        }

        return totalResonance;
    }
}
