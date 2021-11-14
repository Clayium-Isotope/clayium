package mods.clayium.block.tile;

import net.minecraft.world.World;

public interface ISynchronizedInterface {
    boolean setCoreBlockCoord(int paramInt1, int paramInt2, int paramInt3);

    boolean setCoreBlockDimension(int paramInt);

    int getCoreBlockXCoord();

    int getCoreBlockYCoord();

    int getCoreBlockZCoord();

    World getCoreWorld();

    boolean isSynced();

    boolean acceptCoordChanger();
}
