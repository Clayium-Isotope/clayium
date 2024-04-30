package mods.clayium.item.gadget;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.TierPrefix;

@Mod.EventBusSubscriber(modid = ClayiumCore.ModId)
public class GadgetRepeatedlyAttack extends GadgetTemp {

    // TODO: capabilities will be better way
    private static final Map<Entity, Boolean> appliers = new HashMap<>();

    public GadgetRepeatedlyAttack() {
        super("gadget_repeatedly_attack", 0, TierPrefix.antimatter);
    }

    @Override
    public String getGroupName() {
        return "GadgetRepeatedlyAttack";
    }

    @Override
    public void onApply(Entity entity, ItemStack gadget, boolean isRemote) {
        appliers.put(entity, true);
    }

    @Override
    public void onTick(Entity entity, ItemStack gadget, boolean isRemote) {}

    @Override
    public void onReform(Entity entity, ItemStack before, ItemStack after, boolean isRemote) {}

    @Override
    public void onRemove(Entity entity, ItemStack gadget, boolean isRemote) {
        appliers.put(entity, false);
    }

    @SubscribeEvent
    public static void onAttacked(LivingAttackEvent event) {
        if (event.getSource().getImmediateSource() != null &&
                appliers.get(event.getSource().getImmediateSource()) != null &&
                appliers.get(event.getSource().getImmediateSource()) && event.getEntityLiving() != null) {
            event.getEntityLiving().hurtResistantTime = 0;
        }
    }
}
