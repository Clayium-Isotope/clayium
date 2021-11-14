package mods.clayium.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import mods.clayium.util.UtilNetwork;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityClayBall
        extends Entity
        implements IProjectile {
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;

    private Block inBlock;

    protected boolean inGround;
    public int throwableShake;
    private EntityLivingBase thrower;
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;
    protected int age = 0;
    protected int lifespan = 2;
    protected int damage = 5;
    private int numberOfTick = 1;

    protected boolean critical = false;

    public EntityClayBall(World world) {
        super(world);
        setSize(0.25F, 0.25F);
    }


    protected void entityInit() {}


    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return (distance < d1 * d1);
    }


    public EntityClayBall(World world, EntityLivingBase thrower, int lifespan, float initialVelocity, float diffusion, int damage, int numberOfTick, boolean critical) {
        super(world);

        this.thrower = thrower;
        setSize(0.25F, 0.25F);

        setLocationAndAngles(thrower.posX, thrower.posY + thrower.getEyeHeight(), thrower.posZ, thrower.rotationYaw, thrower.rotationPitch);

        this.posX -= (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        setPosition(this.posX, this.posY, this.posZ);

        double d4 = thrower.posX - (MathHelper.sin(thrower.rotationYaw / 180.0F * 3.1415927F) * initialVelocity) * lifespan - this.posX;
        double d5 = thrower.posZ + (MathHelper.cos(thrower.rotationYaw / 180.0F * 3.1415927F) * initialVelocity) * lifespan - this.posZ;
        this.rotationYaw = (float) (Math.atan2(d5, d4) * 180.0D / Math.PI) - 90.0F;

        this.yOffset = 0.0F;
        float f = 0.4F;
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
        this.motionY = (-MathHelper.sin((this.rotationPitch + getInitialPitch()) / 180.0F * 3.1415927F) * f);

        setThrowableHeading(this.motionX, this.motionY, this.motionZ, initialVelocity, diffusion);

        this.lifespan = lifespan;
        this.damage = damage;
        this.numberOfTick = numberOfTick;
        this.critical = critical;
    }


    public EntityClayBall(World world, double xCoord, double yCoord, double zCoord, int lifespan) {
        super(world);
        this.ticksInGround = 0;
        setSize(0.25F, 0.25F);
        setPosition(xCoord, yCoord, zCoord);
        this.yOffset = 0.0F;

        this.lifespan = lifespan;
    }


    protected float getInitialVeolcity() {
        return 1.5F;
    }


    protected float getInitialPitch() {
        return 0.0F;
    }


    public void setThrowableHeading(double directionX, double directionY, double directionZ, float velocity, float diffusion) {
        float f2 = MathHelper.sqrt_double(directionX * directionX + directionY * directionY + directionZ * directionZ);
        directionX /= f2;
        directionY /= f2;
        directionZ /= f2;
        directionX += this.rand.nextGaussian() * 0.007499999832361937D * diffusion;

        directionZ += this.rand.nextGaussian() * 0.007499999832361937D * diffusion;
        directionX *= velocity / 10.0D;
        directionY *= velocity / 10.0D;
        directionZ *= velocity / 10.0D;
        this.motionX = directionX;
        this.motionY = directionY;
        this.motionZ = directionZ;
        float f3 = MathHelper.sqrt_double(directionX * directionX + directionZ * directionZ);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(directionX, directionZ) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(directionY, f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }


    @SideOnly(Side.CLIENT)
    public void setVelocity(double motionX, double motionY, double motionZ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {

            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(motionY, f) * 180.0D / Math.PI);
        }
    }


    public void onUpdate() {
        if (!this.worldObj.isRemote) {
            for (int i = 0; i < this.numberOfTick; i++) {
                if (!this.isDead) {
                    onUpdatePer();
                }
            }
        } else {
            onUpdatePer();
        }
    }


    public void onUpdatePer() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        if (this.throwableShake > 0) {
            this.throwableShake--;
        }

        if (this.inGround) {

            if (this.worldObj.getBlock(this.xTile, this.yTile, this.zTile) == this.inBlock) {

                this.ticksInGround++;

                if (this.ticksInGround == 1200) {
                    setDead();
                }

                return;
            }

            this.inGround = false;
            this.motionX *= (this.rand.nextFloat() * 0.2F);
            this.motionY *= (this.rand.nextFloat() * 0.2F);
            this.motionZ *= (this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        } else {

            this.ticksInAir++;
        }

        Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX * 10.0D, this.posY + this.motionY * 10.0D, this.posZ + this.motionZ * 10.0D);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec31 = Vec3.createVectorHelper(this.posX + this.motionX * 10.0D, this.posY + this.motionY * 10.0D, this.posZ + this.motionZ * 10.0D);

        if (movingobjectposition != null) {
            vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.isRemote) {

            Entity entity = null;
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX * 10.0D, this.motionY * 10.0D, this.motionZ * 10.0D).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            EntityLivingBase entitylivingbase = getThrower();

            for (int j = 0; j < list.size(); j++) {

                Entity entity1 = list.get(j);

                if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 5)) {

                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null) {

                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {

                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal) {

                setInPortal();
            } else {

                onImpact(movingobjectposition);
            }
        }

        this.posX += this.motionX * 10.0D;
        this.posY += this.motionY * 10.0D;
        this.posZ += this.motionZ * 10.0D;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float) (Math.atan2(this.motionY, f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
            ;


        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }


        float f2 = getFriction();
        float f3 = getGravityVelocity();

        if (isInWater()) {

            spawnInWaterParticle();
            f2 = 0.8F;
        }

        this.motionX *= f2;
        if (this.motionY >= 0.0D) this.motionY *= f2;
        this.motionZ *= f2;
        this.motionY -= f3;
        setPosition(this.posX, this.posY, this.posZ);

        this.age++;

        if (this.worldObj.isRemote) {
            spawnFlyingDustPartcle();
            spawnTwinklingPartcle();
        }
    }


    protected float getFriction() {
        return (this.age < this.lifespan) ? 1.0F : 0.6F;
    }


    protected float getGravityVelocity() {
        return (this.age < this.lifespan) ? 0.0F : 0.45F;
    }


    protected void onImpact(MovingObjectPosition moposition) {
        if (!this.isDead) {

            double xx = this.posX;
            double yy = this.posY;
            double zz = this.posZ;
            if (moposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                this.posX = moposition.hitVec.xCoord;
                this.posY = moposition.hitVec.yCoord;
                this.posZ = moposition.hitVec.zCoord;
            } else if (moposition.entityHit != null) {
                this.posX = moposition.hitVec.xCoord;
                this.posY = moposition.hitVec.yCoord;
                this.posZ = moposition.hitVec.zCoord;
            }
            if (moposition.entityHit != null) {
                onEntityHit(moposition.entityHit);
            }

            spawnImpactDustParticle();
            playImpactSound();
            float s = (float) Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            if (s >= 0.5F && this.age <= this.lifespan) {
                playImpactExplodeSound(s);
                spawnImpactExplodeParticle();
            }
            if (checkDeath(moposition)) {
                onDeath();
            }
            this.posX = xx;
            this.posY = yy;
            this.posZ = zz;
        }
    }

    protected void onEntityHit(Entity entity) {
        if (entity instanceof EntityLivingBase && (
                (EntityLivingBase) entity).getHealth() > 0.0F) {
            if (this.critical) {

                if (!this.worldObj.isRemote) {
                    spawnCriticalParticle();
                }
                playCriticalSound();
            }
            playHitSound();
        }

        if (!this.worldObj.isRemote) {
            causeDamage(entity);
        }
    }

    protected void causeDamage(Entity entity) {
        int b0 = this.damage;
        double motionX = entity.motionX;
        double motionY = entity.motionY;
        double motionZ = entity.motionZ;
        if (entity != this.thrower)
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, (Entity) getThrower()), b0);
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase) entity).hurtResistantTime = 2;
        }
        entity.motionX = motionX;
        entity.motionY = motionY;
        entity.motionZ = motionZ;
    }

    protected void spawnFlyingDustPartcle() {
        this.worldObj.spawnParticle("blockdust_" + Block.getIdFromBlock(Blocks.clay) + "_0", this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ, this.motionX / 2.0D, this.motionY / 2.0D, this.motionZ / 2.0D);
        this.worldObj.spawnParticle("blockdust_" + Block.getIdFromBlock(Blocks.clay) + "_0", (this.posX + this.lastTickPosX) / 2.0D, (this.posY + this.lastTickPosY) / 2.0D, (this.posZ + this.lastTickPosZ) / 2.0D, this.motionX / 2.0D, this.motionY / 2.0D, this.motionZ / 2.0D);
    }

    protected void spawnTwinklingPartcle() {
        int s = (int) Math.floor(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) * 100 / 16;
        if (this.age >= this.lifespan) s = 0;
        s *= this.numberOfTick * this.numberOfTick;

        for (int i = 0; i < s; i++) {
            this.worldObj.spawnParticle("crit", this.posX + this.motionX * 10.0D * this.numberOfTick * (i - s) / s, this.posY + this.motionY * 10.0D * this.numberOfTick * (i - s) / s, this.posZ + this.motionZ * 10.0D * this.numberOfTick * (i - s) / s, this.motionX * 10.0D * this.numberOfTick / s / 2.0D, this.motionY * 10.0D * this.numberOfTick / s / 2.0D, this.motionZ * 10.0D * this.numberOfTick / s / 2.0D);
        }
    }

    protected void spawnInWaterParticle() {
        for (int i = 0; i < 4; i++) {

            float f4 = 0.25F;
            this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ);
        }
    }

    protected void spawnImpactDustParticle() {
        for (int i = 0; i < 8; i++) {
            this.worldObj.spawnParticle("blockdust_" + Block.getIdFromBlock(Blocks.clay) + "_0", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void spawnImpactExplodeParticle() {
        this.worldObj.spawnParticle("largeexplode", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
    }


    protected void spawnCriticalParticle() {
        WorldServer server = (WorldServer) this.worldObj;
        UtilNetwork.sendParticlePacketFromServer(server, "fireworksSpark", this.posX, this.posY, this.posZ, 50, 0.0D, -1.0D, 0.0D, 0.5D, 200.0D);
    }

    protected void playImpactSound() {
        this.worldObj.playSoundAtEntity(this, "mob.slime.big", 0.06F * this.damage, 1.4F / (this.rand.nextFloat() * 0.4F + 0.6F));
    }

    protected void playImpactExplodeSound(float speed) {
        if (speed >= 0.5F && this.age <= this.lifespan)
            this.worldObj.playSoundAtEntity(this, "random.explode", 0.6F * speed, 0.5F / (this.rand.nextFloat() * 0.4F + 0.6F));
    }

    protected void playHitSound() {
        this.worldObj.playSoundAtEntity(this, "game.player.hurt", 0.1F * this.damage, 0.7F);
        this.worldObj.playSoundAtEntity(this, "mob.blaze.hit", 0.05F * this.damage, 0.5F);
        this.worldObj.playSoundAtEntity(this, "random.wood_click", 0.2F * this.damage, 1.2F);
        this.worldObj.playSoundAtEntity(this, "note.hat", 0.3F * this.damage, 1.3F);
        this.worldObj.playSoundAtEntity(this, "note.snare", 0.1F * this.damage, 1.2F);
    }


    protected void playCriticalSound() {
        this.worldObj.playSoundAtEntity((Entity) this.thrower, "fireworks.twinkle", 10.0F, 1.0F);
    }


    protected boolean checkDeath(MovingObjectPosition moposition) {
        return (!(moposition.entityHit instanceof EntityLivingBase) || ((EntityLivingBase) moposition.entityHit)
                .getHealth() > 0.0F);
    }

    protected void onDeath() {
        if (!this.worldObj.isRemote) {
            setDead();
        }
    }


    public void writeEntityToNBT(NBTTagCompound tagcompound) {
        tagcompound.setShort("xTile", (short) this.xTile);
        tagcompound.setShort("yTile", (short) this.yTile);
        tagcompound.setShort("zTile", (short) this.zTile);
        tagcompound.setByte("inTile", (byte) Block.getIdFromBlock(this.inBlock));
        tagcompound.setByte("shake", (byte) this.throwableShake);
        tagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));

        if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof net.minecraft.entity.player.EntityPlayer) {
            this.throwerName = this.thrower.getCommandSenderName();
        }

        tagcompound.setString("ownerName", (this.throwerName == null) ? "" : this.throwerName);

        tagcompound.setShort("age", (short) this.age);
        tagcompound.setShort("lifespan", (short) this.lifespan);
        tagcompound.setShort("clayBallDamage", (short) this.damage);
        tagcompound.setShort("numberOfTick", (short) this.numberOfTick);
        tagcompound.setBoolean("critical", this.critical);
    }


    public void readEntityFromNBT(NBTTagCompound tagcompound) {
        this.xTile = tagcompound.getShort("xTile");
        this.yTile = tagcompound.getShort("yTile");
        this.zTile = tagcompound.getShort("zTile");
        this.inBlock = Block.getBlockById(tagcompound.getByte("inTile") & 0xFF);
        this.throwableShake = tagcompound.getByte("shake") & 0xFF;
        this.inGround = (tagcompound.getByte("inGround") == 1);
        this.throwerName = tagcompound.getString("ownerName");

        if (this.throwerName != null && this.throwerName.length() == 0) {
            this.throwerName = null;
        }

        this.age = tagcompound.getShort("age");
        this.lifespan = tagcompound.getShort("lifespan");
        this.damage = tagcompound.getShort("clayBallDamage");
        this.numberOfTick = tagcompound.getShort("numberOfTick");
        this.critical = tagcompound.getBoolean("critical");
    }


    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }


    public EntityLivingBase getThrower() {
        if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
            this.thrower = (EntityLivingBase) this.worldObj.getPlayerEntityByName(this.throwerName);
        }

        return this.thrower;
    }
}
