package mods.clayium.item;

import mods.clayium.entity.EntityTeleportBall;
import mods.clayium.util.TierPrefix;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class InstantTeleporter extends ClayShooter {

    public InstantTeleporter(int maxDamage, String unlocalizedName, TierPrefix tier, int bulletLifespan,
                             float bulletInitialVelocity, float bulletDiffusion, int bulletDamage,
                             float bulletShootingRate, int bulletCooldownTime, int chargeTime) {
        super(maxDamage, unlocalizedName, tier, bulletLifespan, bulletInitialVelocity, bulletDiffusion, bulletDamage,
                bulletShootingRate, bulletCooldownTime, chargeTime);
    }

    public InstantTeleporter(int maxDamage, String unlocalizedName, TierPrefix tier, int bulletLifespan,
                             float bulletInitialVelocity, float bulletDiffusion, int bulletDamage,
                             int bulletShootingFrame, int chargeTime) {
        super(maxDamage, unlocalizedName, tier, bulletLifespan, bulletInitialVelocity, bulletDiffusion, bulletDamage,
                bulletShootingFrame, chargeTime);
    }

    @Override
    public void spawnBullet(World worldIn, EntityPlayer playerIn, ItemStack stack, ClayShooter shooter, float per,
                            boolean critical) {
        if (!playerIn.world.isRemote) {
            EntityTeleportBall teleportBall = new EntityTeleportBall(worldIn, playerIn, shooter.lifespan,
                    shooter.initialVelocity, shooter.diffusion, shooter.bulletDamage, 1, critical);
            teleportBall.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, shooter.initialVelocity,
                    shooter.diffusion);
            worldIn.spawnEntity(teleportBall);
        }
    }
}
