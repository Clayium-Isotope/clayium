package mods.clayium.machine.ClayEnergyLaser.laser;

import net.minecraft.util.EnumFacing;

import mods.clayium.util.UsedFor;

/**
 * Works similar to {@link IClayLaserVictim}
 */
@UsedFor(UsedFor.Type.TileEntity)
public interface IClayLaserMachine {

    boolean irradiateClayLaser(ClayLaser laser, EnumFacing facing);
}
