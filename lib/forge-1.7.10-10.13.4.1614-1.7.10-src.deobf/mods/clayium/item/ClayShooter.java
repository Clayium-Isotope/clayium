package mods.clayium.item;

import cpw.mods.fml.common.eventhandler.Event;
import mods.clayium.core.ClayiumCore;
import mods.clayium.entity.EntityClayBall;
import mods.clayium.util.UtilPlayer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;

public class ClayShooter
        extends ItemTiered {
    protected int bulletLifespan;
    protected float bulletInitialVelocity;
    protected float bulletDiffusion;
    protected int chargeTime = 0;


    protected int bulletDamage;

    protected float bulletShootingRate;

    protected int bulletCooldownTime;

    protected boolean infinity = false;


    public ClayShooter(int maxDamage, String unlocalizedName, String textureName, int bulletLifespan, float bulletInitialVelocity, float bulletDiffusion, int bulletDamage, float bulletShootingRate, int bulletCooldownTime, int chargeTime) {
        this.maxStackSize = 1;

        setCreativeTab(ClayiumCore.creativeTabClayium);

        if (maxDamage >= 0) {
            setMaxDamage(maxDamage);
        } else {
            this.infinity = true;
        }
        setUnlocalizedName(unlocalizedName);
        setTextureName("clayium:" + textureName);

        this.bulletLifespan = bulletLifespan;
        this.bulletInitialVelocity = bulletInitialVelocity;
        this.bulletDiffusion = bulletDiffusion;
        this.bulletDamage = bulletDamage;
        this.bulletShootingRate = bulletShootingRate;
        this.bulletCooldownTime = bulletCooldownTime;

        this.chargeTime = chargeTime;

        setFull3D();
    }


    public ClayShooter(int maxDamage, String unlocalizedName, String textureName, int bulletLifespan, float bulletInitialVelocity, float bulletDiffusion, int bulletDamage, int bulletShootingFrame, int chargeTime) {
        this(maxDamage, unlocalizedName, textureName, bulletLifespan, bulletInitialVelocity, bulletDiffusion, bulletDamage, 3.0F / bulletShootingFrame, bulletShootingFrame / 3, chargeTime);
    }


    public void shootBullet(ItemStack stack, EntityPlayer player) {
        shootBullet(stack, player, 1.0F);
    }

    public void shootBullet(ItemStack stack, EntityPlayer player, float per) {
        shootBullet(stack, player, per, false);
    }


    public void shootBullet(ItemStack stack, EntityPlayer player, float per, boolean critical) {
        player.worldObj.playSoundAtEntity((Entity) player, "game.neutral.hurt.fall.small", 0.6F, 5.0F / (itemRand.nextFloat() * 0.7F + getDamage(stack, player) * per + 1.0F));
        float v = getInitialVelocity(stack, player) * per;
        if (v >= 6.0F) {
            player.worldObj.playSoundAtEntity((Entity) player, "fireworks.launch", 0.01F * (v - 6.0F), 1.0F);
            player.worldObj.playSoundAtEntity((Entity) player, "random.explode", 0.01F * (v - 6.0F), 6.0F / (v + 2.0F) + 0.2F);
        }
        spawnEntity(stack, player, per, critical);
        if (!this.infinity)
            stack.damageItem((int) (per * getChargeTime(stack, player)) + 1, (EntityLivingBase) player);
    }

    public void spawnEntity(ItemStack stack, EntityPlayer player, float per, boolean critical) {
        if (!player.worldObj.isRemote) {
            player.worldObj.spawnEntityInWorld((Entity) new EntityClayBall(player.worldObj, (EntityLivingBase) player,
                    getLifespan(stack, player), getInitialVelocity(stack, player) * per, getDiffusion(stack, player), (int) (getDamage(stack, player) * per), 1, critical));
        }
    }


    public int getLifespan(ItemStack stack, EntityPlayer player) {
        return this.bulletLifespan;
    }

    public float getInitialVelocity(ItemStack stack, EntityPlayer player) {
        return this.bulletInitialVelocity;
    }

    public float getDiffusion(ItemStack stack, EntityPlayer player) {
        return this.bulletDiffusion;
    }

    public int getDamage(ItemStack stack, EntityPlayer player) {
        return this.bulletDamage;
    }

    public float getShootingRate(ItemStack stack, EntityPlayer player) {
        return this.bulletShootingRate;
    }

    public int getCooldownTime(ItemStack stack, EntityPlayer player) {
        return this.bulletCooldownTime;
    }


    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.bow;
    }

    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        if (isCharger(p_77659_1_, p_77659_3_)) {
            float cooldown = ((Float) UtilPlayer.getPlayerInstantDataWithSafety(p_77659_3_, "ClayShooterCoolDown", new Float(0.0F))).floatValue();
            if (cooldown <= -(getCooldownTime(p_77659_1_, p_77659_3_)))
                p_77659_3_.setItemInUse(p_77659_1_, getMaxItemUseDuration(p_77659_1_));
        }
        return p_77659_1_;
    }

    public int getChargeTime(ItemStack stack, EntityPlayer player) {
        return this.chargeTime;
    }

    public boolean isCharger(ItemStack stack, EntityPlayer player) {
        return (this.chargeTime > 0);
    }


    public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_) {
        if (isCharger(p_77615_1_, p_77615_3_)) {
            int j = getMaxItemUseDuration(p_77615_1_) - p_77615_4_;

            ArrowLooseEvent event = new ArrowLooseEvent(p_77615_3_, p_77615_1_, j);
            MinecraftForge.EVENT_BUS.post((Event) event);
            if (event.isCanceled()) {
                return;
            }

            j = event.charge;

            boolean flag = (p_77615_3_.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, p_77615_1_) > 0);
            flag = true;
            if (flag) {

                float f = j / this.chargeTime;
                f = (f * f + f * 2.0F) / 3.0F;

                if (f > 1.0F) {
                    f = 1.0F;
                }
                shootBullet(p_77615_1_, p_77615_3_, f, (f == 1.0F));
            }
        }
    }


    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (isCharger(stack, player)) {
            int c = getMaxItemUseDuration(stack) - count;
            if (c == 0) {
                player.worldObj.playSoundAtEntity((Entity) player, "random.click", 0.5F, 1.3F);
            }
            if (c == getChargeTime(stack, player)) {
                player.worldObj.playSoundAtEntity((Entity) player, "note.hat", 0.5F, 0.6F);
                player.worldObj.playSoundAtEntity((Entity) player, "mob.enderdragon.hit", 0.2F, 2.5F);
            }
        }
    }
}
