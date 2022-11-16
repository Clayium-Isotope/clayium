package mods.clayium.entity;

import mods.clayium.util.UtilNetwork;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public class EntityClayBall extends Entity implements IProjectile {
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
    protected float diffusion;

    public EntityClayBall(World worldIn) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
    }

    public EntityClayBall(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityClayBall(World worldIn, EntityLivingBase throwerIn)
    {
        this(worldIn, throwerIn.posX, throwerIn.posY + (double)throwerIn.getEyeHeight() - 0.10000000149011612D, throwerIn.posZ);
        this.thrower = throwerIn;
    }

    public EntityClayBall(World worldIn, EntityLivingBase thrower, int lifespan, float initialVelocity, float diffusion, int damage, int numberOfTick, boolean critical) {
        super(worldIn);

        this.thrower = thrower;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(thrower.posX, thrower.posY + (double)thrower.getEyeHeight(), thrower.posZ, thrower.rotationYaw, thrower.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
        this.posY -= 0.10000000149011612D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        double d4 = thrower.posX - (double)(MathHelper.sin(thrower.rotationYaw / 180.0F * 3.1415927F) * initialVelocity) * (double)lifespan - this.posX;
        double d5 = thrower.posZ + (double)(MathHelper.cos(thrower.rotationYaw / 180.0F * 3.1415927F) * initialVelocity) * (double)lifespan - this.posZ;
        this.rotationYaw = (float)(Math.atan2(d5, d4) * 180.0D / 3.141592653589793D) - 90.0F;
//        this.yOffset = 0.0F;  use: getYOffset()

        this.lifespan = lifespan;
        this.damage = damage;
        this.numberOfTick = numberOfTick;
        this.critical = critical;
        this.diffusion = diffusion;
    }

    public EntityClayBall(World world, double xCoord, double yCoord, double zCoord, int lifespan) {
        super(world);
        this.ticksInGround = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(xCoord, yCoord, zCoord);
//        this.yOffset = 0.0F;
        this.lifespan = lifespan;
    }

    @Override
    protected void entityInit() {}

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return distance < d1 * d1;
    }

    protected float getInitialVelocity() {
        return 1.5F;
    }

    protected float getInitialPitch() {
        return 0.0F;
    }

    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float diffusion) {
        float f = 0.4F;
        this.motionX = -MathHelper.sin((float) Math.toRadians(this.rotationYaw)) * MathHelper.cos((float) Math.toRadians(this.rotationPitch)) * f;
        this.motionZ = MathHelper.cos((float) Math.toRadians(this.rotationYaw)) * MathHelper.cos((float) Math.toRadians(this.rotationPitch)) * f;
        this.motionY = -MathHelper.sin((float) Math.toRadians(this.rotationPitch + pitchOffset)) * f;
        this.shoot(this.motionX, this.motionY, this.motionZ, velocity, diffusion);
    }

    @Override
    public void shoot(double directionX, double directionY, double directionZ, float velocity, float diffusion) {
        float f2 = MathHelper.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
        directionX /= f2;
        directionY /= f2;
        directionZ /= f2;
        directionX += this.rand.nextGaussian() * 0.007499999832361937D * (double)diffusion;
        directionZ += this.rand.nextGaussian() * 0.007499999832361937D * (double)diffusion;
        directionX *= (double)velocity / 10.0D;
        directionY *= (double)velocity / 10.0D;
        directionZ *= (double)velocity / 10.0D;
        this.motionX = directionX;
        this.motionY = directionY;
        this.motionZ = directionZ;
        float f3 = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.toDegrees(Math.atan2(directionX, directionZ)));
        this.prevRotationPitch = this.rotationPitch = (float)(Math.toDegrees(Math.atan2(directionY, f3)));
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double motionX, double motionY, double motionZ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.toDegrees(Math.atan2(motionX, motionZ)));
            this.prevRotationPitch = this.rotationPitch = (float)(Math.toDegrees(Math.atan2(motionY, f)));
        }
    }

    @Override
    public void onUpdate() {
        if (!this.world.isRemote) {
            for(int i = 0; i < 1; ++i) {
                if (!this.isDead) {
                    this.onUpdatePer();
                }
            }
        } else {
            this.onUpdatePer();
        }
    }

    protected void onUpdatePer() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        if (this.throwableShake > 0) {
            --this.throwableShake;
        }

        if (this.inGround) {
            if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inBlock) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.setDead();
                }

                return;
            }

            this.inGround = false;
            this.motionX *= this.rand.nextFloat() * 0.2F;
            this.motionY *= this.rand.nextFloat() * 0.2F;
            this.motionZ *= this.rand.nextFloat() * 0.2F;
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        } else {
            ++this.ticksInAir;
        }

        Vec3d vec3 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec31 = new Vec3d(this.posX + this.motionX * 10.0D, this.posY + this.motionY * 10.0D, this.posZ + this.motionZ * 10.0D);
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3, vec31);
        vec3 = new Vec3d(this.posX, this.posY, this.posZ);
        vec31 = new Vec3d(this.posX + this.motionX * 10.0D, this.posY + this.motionY * 10.0D, this.posZ + this.motionZ * 10.0D);

        if (movingobjectposition != null) {
            vec31 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);
        }

        if (!this.world.isRemote) {
            Entity entity = null;
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX * 10.0D, this.motionY * 10.0D, this.motionZ * 10.0D).grow(1.0D));
            double d0 = 0.0D;
            EntityLivingBase entitylivingbase = this.getThrower();

            for (Entity entity1 : list) {
                if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksExisted >= 5)) {
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.3F);
                    RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
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
                movingobjectposition = new RayTraceResult(entity);
            }
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.PORTAL) {
                this.setPortal(movingobjectposition.getBlockPos());
            } else {
                this.onImpact(movingobjectposition);
            }
        }

        this.posX += this.motionX * 10.0D;
        this.posY += this.motionY * 10.0D;
        this.posZ += this.motionZ * 10.0D;
        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D);

        for (this.rotationPitch = (float)(Math.toDegrees(Math.atan2(this.motionY, f1))); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        float f2 = this.getFriction();
        float f3 = this.getGravityVelocity();
        if (this.isInWater()) {
            this.spawnInWaterParticle();
            f2 = 0.8F;
        }

        this.motionX *= f2;
        if (this.motionY >= 0.0D) {
            this.motionY *= f2;
        }

        this.motionZ *= f2;
        this.motionY -= f3;
        this.setPosition(this.posX, this.posY, this.posZ);
        ++this.age;
        if (this.world.isRemote) {
            this.spawnFlyingDustPartcle();
            this.spawnTwinklingPartcle();
        }
    }

    protected float getFriction() {
        return this.age < this.lifespan ? 1.0F : 0.6F;
    }

    protected float getGravityVelocity() {
        return this.age < this.lifespan ? 0.0F : 0.45F;
    }

    protected void onImpact(RayTraceResult result) {
        if (this.isDead) return;

        double xx = this.posX;
        double yy = this.posY;
        double zz = this.posZ;
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            this.posX = result.hitVec.x;
            this.posY = result.hitVec.y;
            this.posZ = result.hitVec.z;
        } else if (result.entityHit != null) {
            this.posX = result.hitVec.x;
            this.posY = result.hitVec.y;
            this.posZ = result.hitVec.z;
        }

        if (result.entityHit != null) {
            this.onEntityHit(result.entityHit);
        }

        if (!this.world.isRemote) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.NEUTRAL, 0.06F * (float) this.damage, 1.4F / (this.rand.nextFloat() * 0.4F + 0.6F));

            float s = (float) Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            if (s >= 0.5f && this.age <= this.lifespan) {
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 0.3f * s, 0.5f / (this.rand.nextFloat() * 0.4f + 0.6f));
                ((WorldServer) this.world).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX, this.posY, this.posZ, 1, 0.0f, 0.0f, 0.0f, 3);
            }

            if (this.checkDeath(result)) {
                this.setDead();
            }
        }

        this.spawnImpactDustParticle();
        this.playImpactSound();
        float s = (float)Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        if (s >= 0.5F && this.age <= this.lifespan) {
            this.playImpactExplodeSound(s);
            this.spawnImpactExplodeParticle();
        }

        if (this.checkDeath(result)) {
            this.onDeath();
        }

        this.posX = xx;
        this.posY = yy;
        this.posZ = zz;
    }

    protected void onEntityHit(Entity entity) {
        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() > 0.0F) {
            if (this.critical) {
                if (!this.world.isRemote) {
                    this.spawnCriticalParticle();
                }

                this.playCriticalSound();
            }

            this.playHitSound();
        }

        if (!this.world.isRemote) {
            this.causeDamage(entity);
        }

    }

    protected void causeDamage(Entity entity) {
        int b0 = this.damage;
        double motionX = entity.motionX;
        double motionY = entity.motionY;
        double motionZ = entity.motionZ;
        if (entity != this.thrower) {
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)b0);
        }

        if (entity instanceof EntityLivingBase) {
            entity.hurtResistantTime = 2;
        }

        entity.motionX = motionX;
        entity.motionY = motionY;
        entity.motionZ = motionZ;
    }

    protected void spawnFlyingDustPartcle() {
        this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ, this.motionX / 2.0D, this.motionY / 2.0D, this.motionZ / 2.0D, Block.getStateId(Blocks.CLAY.getDefaultState()));
        this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, (this.posX + this.lastTickPosX) / 2.0D, (this.posY + this.lastTickPosY) / 2.0D, (this.posZ + this.lastTickPosZ) / 2.0D, this.motionX / 2.0D, this.motionY / 2.0D, this.motionZ / 2.0D, Block.getStateId(Blocks.CLAY.getDefaultState()));
    }

    protected void spawnTwinklingPartcle() {
        int s = (int)Math.floor(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) * 100 / 16;
        if (this.age >= this.lifespan) {
            s = 0;
        }

        s *= this.numberOfTick * this.numberOfTick;

        for(int i = 0; i < s; ++i) {
            this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * 10.0D * (double)this.numberOfTick * (double)(i - s) / (double)s, this.posY + this.motionY * 10.0D * (double)this.numberOfTick * (double)(i - s) / (double)s, this.posZ + this.motionZ * 10.0D * (double)this.numberOfTick * (double)(i - s) / (double)s, this.motionX * 10.0D * (double)this.numberOfTick / (double)s / 2.0D, this.motionY * 10.0D * (double)this.numberOfTick / (double)s / 2.0D, this.motionZ * 10.0D * (double)this.numberOfTick / (double)s / 2.0D);
        }

    }

    protected void spawnInWaterParticle() {
        for(int i = 0; i < 4; ++i) {
            float f4 = 0.25F;
            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ);
        }

    }

    protected void spawnImpactDustParticle() {
        for (int i = 0; i < 8; ++i) {
            this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, 0.05d, 0.05d, 0.05d, Block.getStateId(Blocks.CLAY.getDefaultState()));
        }
    }

    protected void spawnImpactExplodeParticle() {
        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
    }

    protected void spawnCriticalParticle() {
        WorldServer server = (WorldServer) this.world;
        UtilNetwork.sendParticlePacketFromServer(server, EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY, this.posZ, 50, 0.0D, -1.0D, 0.0D, 0.5D, 200.0D);
    }

    protected void playImpactSound() {
        this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 0.06F * (float) this.damage, 1.4F / (this.rand.nextFloat() * 0.4F + 0.6F));
    }

    protected void playImpactExplodeSound(float speed) {
        if (speed >= 0.5F && this.age <= this.lifespan) {
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.6F * speed, 0.5F / (this.rand.nextFloat() * 0.4F + 0.6F));
        }
    }

    protected void playHitSound() {
        this.playSound(SoundEvents.ENTITY_PLAYER_HURT, 0.1F * (float)this.damage, 0.7F);
        this.playSound(SoundEvents.ENTITY_BLAZE_HURT, 0.05F * (float)this.damage, 0.5F);
        this.playSound(SoundEvents.BLOCK_WOOD_HIT, 0.2F * (float)this.damage, 1.2F);
        this.playSound(SoundEvents.BLOCK_NOTE_HAT, 0.3F * (float)this.damage, 1.3F);
        this.playSound(SoundEvents.BLOCK_NOTE_SNARE, 0.1F * (float)this.damage, 1.2F);
    }

    protected void playCriticalSound() {
        this.world.playSound(null, this.thrower.posX, this.thrower.posY, this.thrower.posZ, SoundEvents.ENTITY_FIREWORK_TWINKLE, this.getSoundCategory(), 10.0F, 1.0F);
    }

    protected boolean checkDeath(RayTraceResult moposition) {
        return !(moposition.entityHit instanceof EntityLivingBase) || ((EntityLivingBase)moposition.entityHit).getHealth() > 0.0F;
    }

    protected void onDeath() {
        if (!this.world.isRemote) {
            this.setDead();
        }

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagcompound) {
        tagcompound.setShort("xTile", (short)this.xTile);
        tagcompound.setShort("yTile", (short)this.yTile);
        tagcompound.setShort("zTile", (short)this.zTile);
        tagcompound.setByte("inTile", (byte)Block.getIdFromBlock(this.inBlock));
        tagcompound.setByte("shake", (byte)this.throwableShake);
        tagcompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        if ((this.throwerName == null || this.throwerName.isEmpty()) && this.thrower != null && this.thrower instanceof EntityPlayer) {
            this.throwerName = this.thrower.getName();
        }

        tagcompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
        tagcompound.setShort("age", (short)this.age);
        tagcompound.setShort("lifespan", (short)this.lifespan);
        tagcompound.setShort("clayBallDamage", (short)this.damage);
        tagcompound.setShort("numberOfTick", (short)this.numberOfTick);
        tagcompound.setBoolean("critical", this.critical);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagcompound) {
        this.xTile = tagcompound.getShort("xTile");
        this.yTile = tagcompound.getShort("yTile");
        this.zTile = tagcompound.getShort("zTile");
        this.inBlock = Block.getBlockById(tagcompound.getByte("inTile") & 255);
        this.throwableShake = tagcompound.getByte("shake") & 255;
        this.inGround = tagcompound.getByte("inGround") == 1;
        this.throwerName = tagcompound.getString("ownerName");
        if (this.throwerName.isEmpty()) {
            this.throwerName = null;
        }

        this.age = tagcompound.getShort("age");
        this.lifespan = tagcompound.getShort("lifespan");
        this.damage = tagcompound.getShort("clayBallDamage");
        this.numberOfTick = tagcompound.getShort("numberOfTick");
        this.critical = tagcompound.getBoolean("critical");
    }

    public EntityLivingBase getThrower() {
        if (this.thrower == null && this.throwerName != null && !this.throwerName.isEmpty()) {
            this.thrower = this.world.getPlayerEntityByName(this.throwerName);

            // vvv from EntityThrowable class vvv
            if (this.thrower == null && this.world instanceof WorldServer)
            {
                try
                {
                    Entity entity = ((WorldServer)this.world).getEntityFromUuid(UUID.fromString(this.throwerName));

                    if (entity instanceof EntityLivingBase)
                    {
                        this.thrower = (EntityLivingBase)entity;
                    }
                }
                catch (Throwable var2)
                {
                    this.thrower = null;
                }
            }
            // ^^^ from EntityThrowable class ^^^
        }

        return this.thrower;
    }
}
