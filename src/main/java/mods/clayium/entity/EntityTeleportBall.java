package mods.clayium.entity;

import mods.clayium.util.UtilNetwork;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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
        if (!this.world.isRemote) {
            if (this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP) {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this.getThrower();
                if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.world == this.world) {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, this.posX, this.posY, this.posZ, 0.0F);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        this.getThrower().playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
                        double oldX = this.getThrower().posX;
                        double oldY = this.getThrower().posY;
                        double oldZ = this.getThrower().posZ;
                        WorldServer server = (WorldServer)this.world;
                        if (this.getThrower().isRiding()) {
                            this.getThrower().dismountRidingEntity();
                        }

                        double dx = 0.0D;
                        double dy = 0.0D;
                        double dz = 0.0D;
                        int c = 0;

                        double x;
                        double y;
                        double z;
                        do {
                            x = Math.floor(event.getTargetX() + dx);
                            y = Math.floor(event.getTargetY() + dy);
                            z = Math.floor(event.getTargetZ() + dz);
                            dx = this.rand.nextGaussian() * (double)c / 10.0D;
                            dy = this.rand.nextGaussian() * (double)c / 10.0D;
                            dz = this.rand.nextGaussian() * (double)c / 10.0D;
                        } while((this.isCollidable(this.world, x, y, z) || this.isCollidable(this.world, x, y + 1.0D, z)) && c++ < 100);

                        this.getThrower().setPositionAndUpdate(x + 0.5D, y, z + 0.5D);
                        this.getThrower().fallDistance = 0.0F;
                        this.getThrower().attackEntityFrom(DamageSource.FALL, event.getAttackDamage());

                        int length;
                        for(length = 0; length < 30; ++length) {
                            UtilNetwork.sendParticlePacketFromServer(server, EnumParticleTypes.PORTAL, event.getTargetX(), event.getTargetY() + this.rand.nextDouble() * 2.0D, event.getTargetZ(), 0, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), 1.0D, 20.0D);
                        }

                        length = (int)Math.sqrt((event.getTargetX() - this.getThrower().posX) * (event.getTargetX() - this.getThrower().posX) + (event.getTargetY() - this.getThrower().posY) * (event.getTargetY() - this.getThrower().posY) + (event.getTargetZ() - this.getThrower().posZ) * (event.getTargetZ() - this.getThrower().posZ));

                        for(int i = 0; i < length; ++i) {
                            double r = (double)i / (double)length;
                            UtilNetwork.sendParticlePacketFromServer(server, EnumParticleTypes.PORTAL, (event.getTargetX() - oldX) * r + oldX, (event.getTargetY() - oldY) * r + oldY + 1.0D, (event.getTargetZ() - oldZ) * r + oldZ, 0, (oldX - this.posX) / (double)length, (oldY - this.posY) / (double)length, (oldZ - this.posZ) / (double)length, 1.0D, 200.0D);
                        }

                        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
                    }
                }
            }

            this.setDead();
        }

    }

    protected boolean isCollidable(World world, double x, double y, double z) {
        return world.getBlockState(new BlockPos(x, y, z)).getCollisionBoundingBox(world, new BlockPos(x, y, z)) != null;
    }

    @Override
    protected boolean checkDeath(RayTraceResult moposition) {
        return true;
    }

    @Override
    protected void spawnFlyingDustPartcle() {
        this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ, this.motionX / 2.0D, this.motionY / 2.0D, this.motionZ / 2.0D, Block.getStateId(Blocks.WOOL.getDefaultState()) + 13);
        this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, (this.posX + this.lastTickPosX) / 2.0D, (this.posY + this.lastTickPosY) / 2.0D, (this.posZ + this.lastTickPosZ) / 2.0D, this.motionX / 2.0D, this.motionY / 2.0D, this.motionZ / 2.0D, Block.getStateId(Blocks.WOOL.getDefaultState()) + 13);
    }

    protected void spawnImpactDustParticle() {
        for(int i = 0; i < 8; ++i) {
            this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, Block.getStateId(Blocks.WOOL.getDefaultState()) + 13);
        }

    }

    protected void playImpactSound() {
        this.playSound(SoundEvents.BLOCK_STONE_STEP, 0.06F * (float)this.damage, 1.4F / (this.rand.nextFloat() * 0.4F + 0.6F));
    }
}
