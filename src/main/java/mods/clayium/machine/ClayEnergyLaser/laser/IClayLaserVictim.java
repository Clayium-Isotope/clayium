package mods.clayium.machine.ClayEnergyLaser.laser;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * For Block
 */
public interface IClayLaserVictim {

    void onLaserComes(World world, BlockPos pos, EnumFacing aspect, long irradiatedEnergy);
}
