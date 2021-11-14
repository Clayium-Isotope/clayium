package mods.clayium.block.laser;

import mods.clayium.util.UtilDirection;

public interface IClayLaserManager {
    ClayLaser getClayLaser();

    UtilDirection getDirection();

    int getLaserLength();

    int[] getTargetCoord();

    boolean hasTarget();

    boolean isIrradiating();
}
