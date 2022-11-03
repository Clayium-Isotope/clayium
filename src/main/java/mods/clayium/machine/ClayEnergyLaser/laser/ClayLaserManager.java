package mods.clayium.machine.ClayEnergyLaser.laser;

import mods.clayium.block.ClayiumBlocks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayDeque;

public class ClayLaserManager {
    public ClayLaser clayLaser;
    public static final int historyLength = 10;
    public static final int laserLengthMax = 32;
    public long totalIrradiatedEnergy = 0L;
    /**
     * history of laser on latest {@link ClayLaserManager#historyLength} tick
     */
    protected final ArrayDeque<Long> laserEnergyHistory = new ArrayDeque<>(historyLength);
    /**
     * current laser length
     */
    protected int laserLength;
    protected World world;
    protected BlockPos posNow;
    protected BlockPos targetPos;
    protected EnumFacing direction;
    protected boolean hasTarget;
    protected boolean isIrradiating;
    protected boolean initialized;

    public ClayLaserManager() {
        this.initialized = false;
    }

    public void reset(World world, BlockPos pos, EnumFacing direction) {
        this.laserEnergyHistory.clear();
        this.totalIrradiatedEnergy = 0L;
        this.laserLength = 0;
        this.hasTarget = false;
        this.isIrradiating = false;
        this.set(world, pos, direction);
    }

    public void set(World world, BlockPos pos, EnumFacing direction) {
        this.world = world;
        this.posNow = pos;
        this.direction = direction;
        this.initialized = true;
    }

    public void update(boolean irradiation) {
        if (!this.initialized) return;
        if (!irradiation || this.clayLaser == null) {
            this.isIrradiating = false;
            return;
        }

        if (this.clayLaser.numbers.x > 0 || this.clayLaser.numbers.y > 0 || this.clayLaser.numbers.z > 0) {
            this.irradiateLaser();
            this.isIrradiating = true;
        } else {
            this.isIrradiating = false;
        }
    }

    public void irradiateLaser() {
        if (this.world == null) return;

        int newLaserLength = laserLengthMax;
        this.hasTarget = false;

        IBlockState state;
        for(int i = 1; i < laserLengthMax; ++i) {
            BlockPos front = this.posNow.offset(this.direction, i);
            if (this.canGoThrough(front)) continue;

            newLaserLength = i;
            this.hasTarget = true;
            this.targetPos = front;
            break;
        }

        if (!this.hasTarget || newLaserLength != this.laserLength) {
            this.laserEnergyHistory.clear();
            this.totalIrradiatedEnergy = 0L;
        }

        this.laserLength = newLaserLength;
        if (!this.hasTarget) return;

        long n = (long) this.clayLaser.getEnergy();
        this.totalIrradiatedEnergy += n;

        long m = n;
        for (long h : this.laserEnergyHistory) {
            m = Math.min(m, h);
        }

        this.totalIrradiatedEnergy = m * this.totalIrradiatedEnergy / (n == 0 ? 1 : n) ;

        if (this.laserEnergyHistory.size() >= historyLength)
            this.laserEnergyHistory.removeFirst();
        this.laserEnergyHistory.addLast(n);

        state = this.world.getBlockState(this.targetPos);
        TileEntity tile = this.world.getTileEntity(this.targetPos);

        if (state.getBlock() instanceof IClayLaserVictim) {
            ((IClayLaserVictim) state.getBlock()).onLaserComes(this.world, this.targetPos, this.direction.getOpposite(), this.totalIrradiatedEnergy);
            return;
        }

        if (state.getMaterial() == Material.ROCK && state.getBlock() != Blocks.BEDROCK && this.totalIrradiatedEnergy >= (long)(state.getBlockHardness(this.world, this.targetPos) + 1.0F) * 10L) {
            if (state.getBlock() == Blocks.COAL_ORE) {
                this.world.setBlockState(this.targetPos, Blocks.COAL_BLOCK.getDefaultState(), 3);
            } else if (state.getBlock() == Blocks.IRON_ORE) {
                this.world.setBlockState(this.targetPos, Blocks.IRON_BLOCK.getDefaultState(), 3);
            } else if (state.getBlock() == Blocks.GOLD_ORE) {
                this.world.setBlockState(this.targetPos, Blocks.GOLD_BLOCK.getDefaultState(), 3);
            } else if (state.getBlock() == Blocks.DIAMOND_ORE) {
                this.world.setBlockState(this.targetPos, Blocks.DIAMOND_BLOCK.getDefaultState(), 3);
            } else if (state.getBlock() == Blocks.REDSTONE_ORE) {
                this.world.setBlockState(this.targetPos, Blocks.REDSTONE_BLOCK.getDefaultState(), 3);
            } else if (state.getBlock() == Blocks.LAPIS_ORE) {
                this.world.setBlockState(this.targetPos, Blocks.LAPIS_BLOCK.getDefaultState(), 3);
            } else if (state.getBlock() == Blocks.EMERALD_ORE) {
                this.world.setBlockState(this.targetPos, Blocks.EMERALD_BLOCK.getDefaultState(), 3);
            } else {
                this.world.destroyBlock(this.targetPos, true);
            }
            return;
        }

        if (state.getBlock() == Blocks.SAPLING && this.totalIrradiatedEnergy >= 300L && this.totalIrradiatedEnergy < 1000L) {
            this.world.setBlockState(this.targetPos, state.cycleProperty(BlockSapling.TYPE), 3);
            return;
        }

        if (state.getBlock() == Blocks.SAPLING && this.totalIrradiatedEnergy >= 300000L) {
            this.world.setBlockState(this.targetPos, ClayiumBlocks.clayTreeSapling.getDefaultState(), 3);
        }

        if (tile instanceof IClayLaserMachine) {
            ((IClayLaserMachine) tile).irradiateClayLaser(this.clayLaser, this.direction.getOpposite());
        }
    }

    // TODO where's better way?
    protected boolean canGoThrough(BlockPos pos) {
        if (this.world == null) {
            return false;
        }

        IBlockState state = this.world.getBlockState(pos);
        return state.getMaterial() == Material.AIR || state.getMaterial() == Material.GLASS;
    }

    public EnumFacing getDirection() {
        return this.direction;
    }

    public int getLaserLength() {
        return this.laserLength;
    }

    public BlockPos getTargetCoord() {
        return this.hasTarget ? this.posNow.offset(this.direction) : null;
    }

    public boolean hasTarget() {
        return this.hasTarget;
    }

    public boolean isIrradiating() {
        return this.isIrradiating;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        this.totalIrradiatedEnergy = tagCompound.getLong("TotalIrradiatedEnergy");
        this.laserEnergyHistory.clear();

        /* NBTTagLongArray is uncompleted
        NBTBase raw = tagCompound.getTag("LaserEnergyHistory");
        if (raw.getId() == 12) {
            assert raw instanceof NBTTagLongArray;
            // ...
        }
        */

        NBTTagList tagList = tagCompound.getTagList("LaserEnergyHistory", 10);
        for (NBTBase nbt : tagList) {
            if (((NBTTagCompound) nbt).hasKey("Energy", 4))
                this.laserEnergyHistory.addLast(((NBTTagCompound) nbt).getLong("Energy"));
        }

        this.laserLength = tagCompound.getInteger("LaserLength");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setLong("TotalIrradiatedEnergy", this.totalIrradiatedEnergy);

        /* NBTTagLongArray is uncompleted
        NBTTagLongArray tagList = new NBTTagLongArray(Arrays.stream(this.laserEnergyHistory.toArray(new Long[0])).mapToLong(e -> e).toArray());
         */

        NBTTagList tagList = new NBTTagList();
        for (long h : this.laserEnergyHistory) {
            NBTTagCompound elm = new NBTTagCompound();
            elm.setLong("Energy", h);
            tagList.appendTag(elm);
        }

        tagCompound.setTag("LaserEnergyHistory", tagList);
        tagCompound.setInteger("LaserLength", this.laserLength);
    }
}
