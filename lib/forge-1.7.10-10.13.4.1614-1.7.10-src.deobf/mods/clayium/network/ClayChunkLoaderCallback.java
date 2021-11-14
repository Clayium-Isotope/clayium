package mods.clayium.network;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.IClayChunkLoader;
import mods.clayium.block.tile.TileClayChunkLoader;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilBuilder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;


public class ClayChunkLoaderCallback
        implements ForgeChunkManager.OrderedLoadingCallback {
    public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
        for (ForgeChunkManager.Ticket ticket : tickets) {
            int x = ticket.getModData().getInteger("chunkLoaderX");
            int y = ticket.getModData().getInteger("chunkLoaderY");
            int z = ticket.getModData().getInteger("chunkLoaderZ");
            TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
            if (te instanceof IClayChunkLoader) {
                IClayChunkLoader chunkloader = (IClayChunkLoader) te;
                if (chunkloader.hasTicket())
                    chunkloader.releaseTicket();
                chunkloader.appendTicket(ticket);
                chunkloader.forceChunk();
                if (TileClayChunkLoader.chunkLoaderLog)
                    ClayiumCore.logger.info("Activating the chunkLoader " + x + " " + y + " " + z);
                continue;
            }
            ForgeChunkManager.releaseTicket(ticket);
        }
    }


    public List<ForgeChunkManager.Ticket> ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world, int maxTicketCount) {
        List<ForgeChunkManager.Ticket> res = new ArrayList<ForgeChunkManager.Ticket>();
        for (ForgeChunkManager.Ticket ticket : tickets) {
            int x = ticket.getModData().getInteger("chunkLoaderX");
            int y = ticket.getModData().getInteger("chunkLoaderY");
            int z = ticket.getModData().getInteger("chunkLoaderZ");
            TileEntity te = UtilBuilder.safeGetTileEntity((IBlockAccess) world, x, y, z);
            if (te instanceof IClayChunkLoader) {
                IClayChunkLoader chunkloader = (IClayChunkLoader) te;
                if (!chunkloader.isPassive(world, x, y, z)) {
                    res.add(ticket);
                }
            }
        }
        return res;
    }
}
