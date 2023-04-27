package mods.clayium.machine.Interface;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ISynchronizedInterface {
    void setCoreBlock(@Nullable IInterfaceCaptive tile);

    IInterfaceCaptive getCore();

    World getWorld();
    BlockPos getPos();

    boolean isSynced();

    boolean isSyncEnabled();
    boolean markEnableSync();

    int getHullTier();
}
