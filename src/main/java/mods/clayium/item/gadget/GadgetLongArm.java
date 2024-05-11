package mods.clayium.item.gadget;

import mods.clayium.util.TierPrefix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

public class GadgetLongArm extends GadgetTemp {

    private final float length;
    private static final UUID uuid = UUID.fromString("8d995722-38da-4f79-80cf-d45f86c93a3e"); // it means nothing

    public GadgetLongArm(int meta, TierPrefix tier, float length) {
        super("gadget_long_arm_" + meta, meta, tier);
        this.length = length;
    }

    @Override
    public String getGroupName() {
        return "GadgetLongArm";
    }

    @Override
    public void onApply(Entity entity, ItemStack gadget, boolean isRemote) {
        if (entity instanceof EntityLivingBase) {
            IAttributeInstance iattributeinstance = ((EntityLivingBase) entity)
                    .getEntityAttribute(EntityPlayer.REACH_DISTANCE);
            if (iattributeinstance != null) {
                iattributeinstance.removeModifier(uuid); // lazy code
                iattributeinstance.applyModifier(new AttributeModifier(uuid, "GadgetLongArm", this.length,
                        Constants.AttributeModifierOperation.ADD));
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
            IAttributeInstance iattributeinstance = ((EntityLivingBase) entity)
                    .getEntityAttribute(EntityPlayer.REACH_DISTANCE);
            if (iattributeinstance != null)
                iattributeinstance.removeModifier(uuid);
        }
    }
}
