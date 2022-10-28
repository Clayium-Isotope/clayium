package mods.clayium.machine.ClayEnergyLaser.laser;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IClayLaserManager {
    ClayLaser getClayLaser();

    EnumFacing getDirection();

    int getLaserLength();

    BlockPos getTargetCoord();

    boolean hasTarget();

    boolean isIrradiating();
}
