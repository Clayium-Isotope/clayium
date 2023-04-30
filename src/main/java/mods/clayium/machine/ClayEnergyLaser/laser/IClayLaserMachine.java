package mods.clayium.machine.ClayEnergyLaser.laser;

import mods.clayium.util.UsedFor;
import net.minecraft.util.EnumFacing;

/**
 * Works similar to {@link IClayLaserVictim}
 */
@UsedFor(UsedFor.Type.TileEntity)
public interface IClayLaserMachine {
    boolean irradiateClayLaser(ClayLaser laser, EnumFacing facing);
}
