package mods.clayium.machine.ClayEnergyLaser.laser;

import mods.clayium.util.UsedFor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * For Block
 */
@UsedFor(UsedFor.Type.Block)
public interface IClayLaserVictim {
    /**
     * You can act when laser comes.
     * Faster than {@link IClayLaserMachine}
     */
    void onLaserComes(World world, BlockPos pos, EnumFacing aspect, long irradiatedEnergy);
}
