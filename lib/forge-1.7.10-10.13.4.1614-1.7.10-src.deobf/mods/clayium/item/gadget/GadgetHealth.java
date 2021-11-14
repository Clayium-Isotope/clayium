package mods.clayium.item.gadget;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public class GadgetHealth
        extends GadgetOrdinal {
    private AttributeModifier mod;

    public GadgetHealth() {
        super(new String[] {"Health0", "Health1", "Health2"});
    }


    private static UUID uuid = UUID.fromString("066bfee5-6cd5-4166-93d7-dfcc32c337a8");

    public void update(int itemIndex, Entity entity, boolean isRemote) {
        if (this.mod == null) {
            this.mod = new AttributeModifier(uuid, "GadgetHealth", 20.0D, 0);
        }
        if (itemIndex >= 0) {
            if (entity instanceof EntityLivingBase) {
                HashMultimap hashMultimap = HashMultimap.create();


                hashMultimap.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(uuid, this.mod.getName(), this.mod.getAmount() * (itemIndex + 1) * (itemIndex + 1), this.mod.getOperation()));

                ((EntityLivingBase) entity).getAttributeMap().applyAttributeModifiers((Multimap) hashMultimap);
            }

        } else if (entity instanceof EntityLivingBase) {
            HashMultimap hashMultimap = HashMultimap.create();
            hashMultimap.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), this.mod);
            ((EntityLivingBase) entity).getAttributeMap().removeAttributeModifiers((Multimap) hashMultimap);
        }
    }
}
