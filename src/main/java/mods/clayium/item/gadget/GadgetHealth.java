package mods.clayium.item.gadget;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class GadgetHealth extends GadgetTemp {
    private final int line;
    private static final UUID uuid = UUID.fromString("066bfee5-6cd5-4166-93d7-dfcc32c337a8");

    public GadgetHealth(int meta, int tier, int line) {
        super("gadget_health_" + meta, meta, tier);
        this.line = line;
    }

    @Override
    public String getGroupName() {
        return "GadgetHealth";
    }

    @Override
    public void onApply(Entity entity, ItemStack gadget, boolean isRemote) {
        if (entity instanceof EntityLivingBase) {
            IAttributeInstance iattributeinstance = ((EntityLivingBase) entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            if (iattributeinstance != null) {
                iattributeinstance.removeModifier(uuid); // lazy code
                iattributeinstance.applyModifier(new AttributeModifier(uuid, "GadgetHealth", 20.0d * this.line, 0));
            }
        }
    }

    @Override
    public void onTick(Entity entity, ItemStack gadget, boolean isRemote) {}

    @Override
    public void onReform(Entity entity, ItemStack before, ItemStack after, boolean isRemote) {
        this.onApply(entity, after, isRemote);
    }

    @Override
    public void onRemove(Entity entity, ItemStack gadget, boolean isRemote) {
        if (entity instanceof EntityLivingBase) {
            IAttributeInstance iattributeinstance = ((EntityLivingBase) entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            if (iattributeinstance != null)
                iattributeinstance.removeModifier(uuid);
        }
    }
}
