package mods.clayium.network;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mods.clayium.item.ClayShooter;

// Related to fov, etc...
@Mod.EventBusSubscriber
public class UXEventHandler {

    @SubscribeEvent
    public static void onFOVUpdateEvent(FOVUpdateEvent event) {
        ItemStack stack = event.getEntity().getActiveItemStack();
        if (stack.isEmpty() || !event.getEntity().isHandActive()) return;

        if (stack.getItem() instanceof ClayShooter) {
            ClayShooter shooter = (ClayShooter) stack.getItem();
            if (!shooter.isCharger()) return;

            int time = shooter.getChargeTime();
            int i = shooter.getMaxItemUseDuration(stack) - event.getEntity().getItemInUseCount();

            if (i > time) {
                i = time;
            }

            float f1 = (float) i / (float) time;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 *= f1;
            }

            float t = shooter.getInitialVelocity() * shooter.getLifespan();
            float u = (float) i / (float) time;
            float k = t * u - 10.0F;
            if (k < 0.0F) {
                k = 0.0F;
            }

            k = k + 3600.0F / (k + 60.0F) - 60.0F;
            float m = 2.5F / (k + 2.5F);
            event.setNewfov(event.getNewfov() * m + (1.0F - m) * 0.1F);
        }
    }
}
