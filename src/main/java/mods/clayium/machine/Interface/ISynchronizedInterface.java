package mods.clayium.machine.Interface;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mods.clayium.util.TierPrefix;
import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.TileEntity)
public interface ISynchronizedInterface {

    void setCoreBlock(@Nullable IInterfaceCaptive tile);

    IInterfaceCaptive getCore();

    World getWorld();

    BlockPos getPos();

    boolean isSynced();

    boolean isSyncEnabled();

    boolean markEnableSync();

    TierPrefix getHullTier();
}
