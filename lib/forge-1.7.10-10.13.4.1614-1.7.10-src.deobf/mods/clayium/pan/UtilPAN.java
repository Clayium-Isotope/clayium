package mods.clayium.pan;

import mods.clayium.core.ClayiumCore;
import mods.clayium.plugin.multipart.UtilMultipart;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class UtilPAN {
    public static boolean isPANConductor(IBlockAccess world, int x, int y, int z) {
        if (world == null)
            return false;
        if (y < 0 || y >= 255)
            return false;
        Block b = world.getBlock(x, y, z);
        if (isPANConductor(b, x, y, z))
            return true;
        if (ClayiumCore.IntegrationID.MULTI_PART.loaded() && UtilMultipart.containsPANConductor(world, x, y, z))
            return true;
        return false;
    }

    public static boolean isPANConductor(Block block, int x, int y, int z) {
        return block instanceof mods.clayium.block.IPANConductor;
    }
}
