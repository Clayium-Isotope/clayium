package mods.clayium.machine.ClayEnergyLaser.laser;

import net.minecraft.util.EnumFacing;

/**
 * Works similar to {@link IClayLaserVictim}
 */
public interface IClayLaserMachine {
    boolean irradiateClayLaser(ClayLaser laser, EnumFacing facing);
}
