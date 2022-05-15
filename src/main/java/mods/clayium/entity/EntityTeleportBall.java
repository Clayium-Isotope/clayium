package mods.clayium.entity;

import cpw.mods.fml.common.eventhandler.Event;
import mods.clayium.util.UtilNetwork;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntityTeleportBall extends EntityClayBall {
    public EntityTeleportBall(World world) {
        super(world);
    }

    public EntityTeleportBall(World world, double xCoord, double yCoord, double zCoord, int lifespan) {
        super(world, xCoord, yCoord, zCoord, lifespan);
    }

    public EntityTeleportBall(World world, EntityLivingBase thrower, int lifespan, float initialVelocity, float diffusion, int damage, int numberOfTick, boolean critical) {
        super(world, thrower, lifespan, initialVelocity, diffusion, damage, numberOfTick, critical);
    }


    protected void onDeath() {
        if (!this.worldObj.isRemote) {
            if (getThrower() != null && getThrower() instanceof EntityPlayerMP) {

                EntityPlayerMP entityplayermp = (EntityPlayerMP) getThrower();

                if (entityplayermp.playerNetServerHandler.func_147362_b().isChannelOpen() && entityplayermp.worldObj == this.worldObj) {

                    EnderTeleportEvent event = new EnderTeleportEvent((EntityLivingBase) entityplayermp, this.posX, this.posY, this.posZ, 0.0F);
                    if (!MinecraftForge.EVENT_BUS.post((Event) event)) {
                        double x, y, z;
                        this.worldObj.playSoundAtEntity((Entity) getThrower(), "mob.endermen.portal", 1.0F, 1.0F);

                        double oldX = (getThrower()).posX;
                        double oldY = (getThrower()).posY;
                        double oldZ = (getThrower()).posZ;
                        WorldServer server = (WorldServer) this.worldObj;

                        if (getThrower().isRiding()) {
                            getThrower().mountEntity((Entity) null);
                        }
                        double dx = 0.0D, dy = 0.0D, dz = 0.0D;
                        int c = 0;


                        do {
                            x = Math.floor(event.targetX + dx);
                            y = Math.floor(event.targetY + dy);
                            z = Math.floor(event.targetZ + dz);
                            dx = this.rand.nextGaussian() * c / 10.0D;
                            dy = this.rand.nextGaussian() * c / 10.0D;
                            dz = this.rand.nextGaussian() * c / 10.0D;
                        }
                        while ((isCollidable(this.worldObj, x, y, z) || isCollidable(this.worldObj, x, y + 1.0D, z)) && c++ < 100);

                        getThrower().setPositionAndUpdate(x + 0.5D, y, z + 0.5D);
                        (getThrower()).fallDistance = 0.0F;
                        getThrower().attackEntityFrom(DamageSource.fall, event.attackDamage);

                        for (int i = 0; i < 30; i++) {
                            UtilNetwork.sendParticlePacketFromServer(server, "portal", event.targetX, event.targetY + this.rand.nextDouble() * 2.0D, event.targetZ, 0, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), 1.0D, 20.0D);
                        }

                        int length = (int) Math.sqrt((event.targetX - (getThrower()).posX) * (event.targetX - (getThrower()).posX) + (event.targetY -
                                (getThrower()).posY) * (event.targetY - (getThrower()).posY) + (event.targetZ -
                                (getThrower()).posZ) * (event.targetZ - (getThrower()).posZ));

                        for (int j = 0; j < length; j++) {

                            double r = j / length;
                            UtilNetwork.sendParticlePacketFromServer(server, "portal", (event.targetX - oldX) * r + oldX, (event.targetY - oldY) * r + oldY + 1.0D, (event.targetZ - oldZ) * r + oldZ, 0, (oldX - this.posX) / length, (oldY - this.posY) / length, (oldZ - this.posZ) / length, 1.0D, 200.0D);
                        }
                        this.worldObj.playSoundAtEntity(this, "mob.endermen.portal", 1.0F, 1.0F);
                    }
                }
            }

            setDead();
        }
    }

    protected boolean isCollidable(World world, double x, double y, double z) {
        return (world.getBlock((int) x, (int) y, (int) z).getCollisionBoundingBoxFromPool(world, (int) x, (int) y, (int) z) != null);
    }


    protected boolean checkDeath(MovingObjectPosition moposition) {
        return true;
    }


    protected void spawnFlyingDustPartcle() {
        this.worldObj.spawnParticle("blockdust_" + Block.getIdFromBlock(Blocks.wool) + "_13", this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ, this.motionX / 2.0D, this.motionY / 2.0D, this.motionZ / 2.0D);
        this.worldObj.spawnParticle("blockdust_" + Block.getIdFromBlock(Blocks.wool) + "_13", (this.posX + this.lastTickPosX) / 2.0D, (this.posY + this.lastTickPosY) / 2.0D, (this.posZ + this.lastTickPosZ) / 2.0D, this.motionX / 2.0D, this.motionY / 2.0D, this.motionZ / 2.0D);
    }


    protected void spawnImpactDustParticle() {
        for (int i = 0; i < 8; i++) {
            this.worldObj.spawnParticle("blockdust_" + Block.getIdFromBlock(Blocks.wool) + "_13", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }


    protected void playImpactSound() {
        this.worldObj.playSoundAtEntity(this, "step.stone", 0.06F * this.damage, 1.4F / (this.rand.nextFloat() * 0.4F + 0.6F));
    }
}
