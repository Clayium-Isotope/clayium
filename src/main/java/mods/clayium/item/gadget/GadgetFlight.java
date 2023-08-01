package mods.clayium.item.gadget;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.TierPrefix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GadgetFlight extends GadgetTemp {
    private final int mode;

    public GadgetFlight(int meta, TierPrefix tier, int mode) {
        super("gadget_flight_" + meta, meta, tier);
        this.mode = mode;
    }

    @Override
    public String getGroupName() {
        return "GadgetFlight";
    }

    @Override
    public void onApply(Entity entity, ItemStack gadget, boolean isRemote) {}

    @Override
    public void onTick(Entity entity, ItemStack gadget, boolean isRemote) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;

            player.capabilities.allowFlying = true;
            if (entity.motionY >= 0.0D) {
                entity.fallDistance = 0.0F;
            } else {
                int t = (int)(-entity.motionY / 0.05D);
                entity.fallDistance = Math.min(entity.fallDistance, (float)(t * (t - 1)) * 0.025F);
            }

            if (isRemote && ClayiumCore.proxy.getClientPlayer() == entity) {
                ClayiumCore.proxy.updateFlightStatus(this.mode);
            }
        }
    }

    @Override
    public void onReform(Entity entity, ItemStack before, ItemStack after, boolean isRemote) {}

    @Override
    public void onRemove(Entity entity, ItemStack gadget, boolean isRemote) {
        if (entity instanceof EntityPlayer) {
            if (!((EntityPlayer) entity).isCreative()) {
                ((EntityPlayer) entity).capabilities.allowFlying = false;
                ((EntityPlayer) entity).capabilities.isFlying = false;
            }
        }
    }
}
