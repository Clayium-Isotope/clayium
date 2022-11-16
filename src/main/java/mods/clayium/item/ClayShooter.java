package mods.clayium.item;

import mods.clayium.entity.EntityClayBall;
import mods.clayium.item.common.ItemTiered;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ClayShooter extends ItemTiered {
    protected final boolean infinity;
    protected final int maxAmmo;
    protected final int bulletDamage;
    protected final int cooldownTime;
    protected final int chargeTime;
    protected final float initialVelocity;
    protected final int lifespan;
    protected final float diffusion;

    protected float cooldown = 0;

    public ClayShooter(int maxDamage, String modelPath, int tier, int bAliveTime, float bInitialVelocity, float bDiffusion, int bDamage, int bShootFrame, int chargeTime) {
        this(maxDamage, modelPath, tier, bAliveTime, bInitialVelocity, bDiffusion, bDamage, 3.0f / bShootFrame, bShootFrame / 3, chargeTime);
    }

    public ClayShooter(int maxDamage, String modelPath, int tier, int bAliveTime, float bInitialVelocity, float bDiffusion, int bDamage, float bShootRate, int bCooldownTime, int chargeTime) {
        super(modelPath, tier);

        setMaxStackSize(1);
        if (maxDamage >= 0) {
            this.setMaxDamage(maxDamage);
            this.infinity = false;
        } else {
            this.infinity = true;
        }

        this.maxAmmo = maxDamage;
        this.bulletDamage = bDamage;
        this.cooldownTime = bCooldownTime;
        this.chargeTime = chargeTime;
        this.initialVelocity = bInitialVelocity;
        this.lifespan = bAliveTime;
        this.diffusion = bDiffusion;

        setFull3D();
    }

    public static void shoot(World worldIn, EntityPlayer playerIn, ItemStack itemstack, float per, boolean critical) {
        if (itemstack.getItem() instanceof ClayShooter) {
            ClayShooter shooter = (ClayShooter) itemstack.getItem();

            playerIn.playSound(SoundEvents.ENTITY_GENERIC_SMALL_FALL, 0.6F, 5.0F / (itemRand.nextFloat() * 0.7F + 1.0F));
            float v = shooter.getInitialVelocity() * per;
            if (v >= 6.0F) {
                playerIn.playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, 0.01F * (v - 6.0F), 1.0F);
                playerIn.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.01F * (v - 6.0F), 6.0F / (v + 2.0F) + 0.2F);
            }

            if (!worldIn.isRemote) {
                EntityClayBall entityclayball = new EntityClayBall(worldIn, playerIn, shooter.lifespan, shooter.initialVelocity, shooter.diffusion, shooter.bulletDamage, 1, critical);
                entityclayball.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, shooter.initialVelocity, shooter.diffusion);
                worldIn.spawnEntity(entityclayball);
            }

            if (!shooter.infinity && !playerIn.isCreative()) {
                itemstack.damageItem((int) (per * (float) shooter.getChargeTime()) + 1, playerIn);
            }
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    public int getChargeTime() {
        return this.chargeTime;
    }

    public boolean isCharger() {
        return this.chargeTime > 0;
    }

    public int getCooldownTime() {
        return this.cooldownTime;
    }

    public float getShootingRate() {
        return 3.0f / 6;
    }

    public float getInitialVelocity() {
        return this.initialVelocity;
    }

    public int getLifespan() {
        return this.lifespan;
    }

    // ===== Shooter Part =====
    // Based on ItemSnowBall class

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase playerIn, int count) {
        if (this.isCharger()) {
            int c = this.getMaxItemUseDuration(stack) - count;
            if (c == 0) {
                playerIn.playSound(SoundEvents.UI_BUTTON_CLICK, 0.5F, 1.3F);
            }

            if (c == this.getChargeTime()) {
                playerIn.playSound(SoundEvents.BLOCK_NOTE_HAT, 0.5F, 0.6F);
                playerIn.playSound(SoundEvents.ENTITY_ENDERDRAGON_HURT, 0.2F, 2.5F);
            }
        } else {
            EntityPlayer player = (EntityPlayer) playerIn;

            float rate = this.getShootingRate();

            this.cooldown += rate;
            while (this.cooldown >= 1.0F) {
                this.cooldown--;
                shoot(player.world, player, stack, 1.0f, false);
            }
        }
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return !this.isCharger() || super.canContinueUsing(oldStack, newStack);
    }

    // ===== Charger Part =====
    // Based on ItemBow class

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        if (this.isCharger()) {
            ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, true);
            if (ret != null) return ret;
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (this.isCharger()) {
            if (!(entityLiving instanceof EntityPlayer)) return;
            EntityPlayer player = (EntityPlayer) entityLiving;

            int charge = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, player, this.getMaxItemUseDuration(stack) - timeLeft, this.getDurabilityForDisplay(stack) < 1.0d);
            if (charge < 0) {
                return;
            }

            float velocity = (float) charge / (float) this.chargeTime;
            velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
            if (velocity > 1.0F) {
                velocity = 1.0F;
            }

            ClayShooter.shoot(worldIn, player, stack, velocity, velocity == 1.0f);
        }
    }
}
