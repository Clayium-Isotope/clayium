package mods.clayium.component.bot;

import mods.clayium.component.GeneralBot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 狭域に対して作用する bot
 */
public interface LocalBot extends GeneralBot<BlockPos> {
    long progressPerJob = 400;
    int referSlotHarvest = 0;
    int referSlotFortune = 1;
    int referSlotSilktouch = 2;

    void setWorld(World world);
    boolean hasWorld();

    @Override
    default boolean isReady() {
        return this.hasWorld();
    }

    @Override
    default long progressPerJob() {
        return progressPerJob;
    }

    void setProgressAccess(GeneralBot<?> other);
}
