package mods.clayium.block;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

public interface IClayChunkLoader {
    boolean isPassive(World paramWorld, int paramInt1, int paramInt2, int paramInt3);

    boolean hasTicket();

    ForgeChunkManager.Ticket requestTicket();

    void releaseTicket();

    void appendTicket(ForgeChunkManager.Ticket paramTicket);

    void forceChunk();
}
