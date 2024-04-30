package mods.clayium.machine.ClayEnergyLaser.laser;

import net.minecraft.util.EnumFacing;

import mods.clayium.util.UsedFor;

/**
 * Works similar to {@link IClayLaserVictim}
 * But for TileEntity.
 * [TODO] Make this into a Capability
 */
@UsedFor(UsedFor.Type.TileEntity)
public interface IClayLaserMachine {

    boolean irradiateClayLaser(ClayLaser laser, EnumFacing facing);
}
