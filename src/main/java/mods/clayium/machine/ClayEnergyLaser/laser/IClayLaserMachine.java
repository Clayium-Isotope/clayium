package mods.clayium.machine.ClayEnergyLaser.laser;

import mods.clayium.util.UsedFor;
import net.minecraft.util.EnumFacing;

/**
 * Works similar to {@link IClayLaserVictim}
 * But for TileEntity.
 * [TODO] Make this into a Capability
 */
@UsedFor(UsedFor.Type.TileEntity)
public interface IClayLaserMachine {
    boolean irradiateClayLaser(ClayLaser laser, EnumFacing facing);
}
