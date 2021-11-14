package mods.clayium.block.tile;

import mods.clayium.block.IClayChunkLoader;
import mods.clayium.core.ClayiumCore;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

public class TileClayChunkLoader
        extends TileGeneric implements IClayChunkLoader {
    public static boolean chunkLoaderLog = false;
    private ForgeChunkManager.Ticket ticket;

    public boolean isPassive(World world, int x, int y, int z) {
        return false;
    }


    public void updateEntity() {
        if (!hasTicket()) {
            appendTicket(requestTicket());
            forceChunk();
        }


        super.updateEntity();
    }


    public ForgeChunkManager.Ticket requestTicket() {
        ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestTicket(ClayiumCore.INSTANCE, this.worldObj, ForgeChunkManager.Type.NORMAL);
        if (ticket != null) {
            ticket.getModData().setInteger("chunkLoaderX", this.xCoord);
            ticket.getModData().setInteger("chunkLoaderY", this.yCoord);
            ticket.getModData().setInteger("chunkLoaderZ", this.zCoord);
        } else {
            ClayiumCore.logger.warn("ChunkLoader " + this.xCoord + " " + this.yCoord + " " + this.zCoord + " failed to request a new ticket. " + "There are too many Clayium's Chunk Loaders in the world.");
        }

        return ticket;
    }


    public boolean hasTicket() {
        if (this.ticket == null && chunkLoaderLog)
            ClayiumCore.logger.info("ChunkLoader " + this.xCoord + " " + this.yCoord + " " + this.zCoord + " has no ticket.");
        return (this.ticket != null);
    }


    public void releaseTicket() {
        if (chunkLoaderLog)
            ClayiumCore.logger.info("ChunkLoader " + this.xCoord + " " + this.yCoord + " " + this.zCoord + " released its ticket.");
        if (hasTicket()) {
            ForgeChunkManager.releaseTicket(this.ticket);
        }
    }


    public void appendTicket(ForgeChunkManager.Ticket ticket) {
        if (chunkLoaderLog)
            ClayiumCore.logger.info("ChunkLoader " + this.xCoord + " " + this.yCoord + " " + this.zCoord + " was appended a new ticket.");
        if (ticket != null) {
            if (this.ticket == null) {
                this.ticket = ticket;
            } else if (this.ticket != ticket) {
                if (chunkLoaderLog)
                    ClayiumCore.logger.info("ChunkLoader " + this.xCoord + " " + this.yCoord + " " + this.zCoord + " released the old ticket.");
                ForgeChunkManager.releaseTicket(this.ticket);
                this.ticket = ticket;
            }
        }
    }


    public void forceChunk() {
        if (chunkLoaderLog)
            ClayiumCore.logger.info("ChunkLoader " + this.xCoord + " " + this.yCoord + " " + this.zCoord + " tried to force the chunk.");
        if (hasTicket()) {
            int size = 1;
            for (int chunkX = (this.xCoord >> 4) - size; chunkX <= (this.xCoord >> 4) + size; chunkX++) {
                for (int chunkZ = (this.zCoord >> 4) - size; chunkZ <= (this.zCoord >> 4) + size; chunkZ++) {
                    if (chunkLoaderLog)
                        ClayiumCore.logger.info("ChunkLoader " + this.xCoord + " " + this.yCoord + " " + this.zCoord + " forced the chunk [" + chunkX + "] [" + chunkZ + "].");
                    ForgeChunkManager.forceChunk(this.ticket, new ChunkCoordIntPair(chunkX, chunkZ));
                }
            }
        }
    }
}
