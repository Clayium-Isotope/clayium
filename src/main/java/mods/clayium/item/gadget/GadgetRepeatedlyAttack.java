package mods.clayium.item.gadget;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.clayium.util.UtilPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class GadgetRepeatedlyAttack
        extends GadgetOrdinal {
    public GadgetRepeatedlyAttack() {
        super(new String[] {"RepeatedlyAttack"});
    }

    public void update(int itemIndex, Entity entity, boolean isRemote) {
        if (entity instanceof EntityPlayer) {
            UtilPlayer.setPlayerInstantData((EntityPlayer) entity, "GadgetRepeatedlyAttack", Boolean.valueOf((itemIndex == 0)));
        }
    }

    @SubscribeEvent
    public void onAttacked(LivingAttackEvent event) {
        if (event.source != null && event.source.getSourceOfDamage() instanceof EntityPlayer && (
                (Boolean) UtilPlayer.getPlayerInstantDataWithSafety((EntityPlayer) event.source.getSourceOfDamage(), "GadgetRepeatedlyAttack", Boolean.valueOf(false))).booleanValue() &&
                event.entityLiving != null)
            event.entityLiving.hurtResistantTime = 0;
    }
}
