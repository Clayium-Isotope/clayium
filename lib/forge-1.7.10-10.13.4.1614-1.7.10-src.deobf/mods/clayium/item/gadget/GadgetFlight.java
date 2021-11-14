package mods.clayium.item.gadget;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class GadgetFlight
        extends GadgetOrdinal {
    public GadgetFlight() {
        super(new String[] {"Flight0", "Flight1", "Flight2"});
    }


    public void update(int itemIndex, Entity entity, boolean isRemote) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            int oldMode = -1;
            if (UtilPlayer.getPlayerInstantData(player, "GadgetFlight") != null)
                oldMode = ((Integer) UtilPlayer.getPlayerInstantData(player, "GadgetFlight")).intValue();
            if (itemIndex == -1) {
                if (oldMode != itemIndex &&
                        !ClayiumCore.proxy.isCreative(player)) {
                    player.capabilities.allowFlying = false;
                    player.capabilities.isFlying = false;
                }
            } else {

                player.capabilities.allowFlying = true;
                if (entity.motionY >= 0.0D) {
                    entity.fallDistance = 0.0F;
                } else {
                    int t = (int) (-entity.motionY / 0.05D);
                    entity.fallDistance = Math.min(entity.fallDistance, (t * (t - 1)) * 0.025F);
                }
            }
            if (isRemote && ClayiumCore.proxy.getClientPlayer() == entity) {
                ClayiumCore.proxy.updateFlightStatus(itemIndex);
            }

            UtilPlayer.setPlayerInstantData(player, "GadgetFlight", Integer.valueOf(itemIndex));
        }
    }
}
