package mods.clayium.core;

import mods.clayium.item.ClaySteelPickaxe;
import mods.clayium.util.UtilAdvancedTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class ClayiumCommonProxy {
    public void registerTileEntity() {}

    public void registerRenderer() {}

    public int getRenderID() {
        return -1;
    }


    public World getClientWorld() {
        return null;
    }


    public void LoadNEI() {}

    public ClaySteelPickaxe newClaySteelPickaxe() {
        return new ClaySteelPickaxe();
    }

    public int getHittingSide(EntityPlayer player) {
        Integer s = (Integer) UtilAdvancedTools.sideList.get(player);
        return (s == null || s.intValue() < 0 || s.intValue() >= 6) ? -1 : s.intValue();
    }

    public void updateHittingSide(EntityPlayer player) {}

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public boolean renderAsPipingMode() {
        return false;
    }


    public void clientPlayerTick(EntityPlayer player) {}


    public void overclockPlayer(int delay) {}


    public void updateFlightStatus(int mode) {}

    public float hookBlockReachDistance(float distance) {
        return distance;
    }

    public boolean isCreative(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            return ((EntityPlayerMP) player).theItemInWorldManager.isCreative();
        }
        return false;
    }
}
