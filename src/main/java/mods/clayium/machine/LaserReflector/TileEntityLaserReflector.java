package mods.clayium.machine.LaserReflector;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaser;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaserManager;
import mods.clayium.machine.ClayEnergyLaser.laser.IClayLaserMachine;
import mods.clayium.machine.ClayEnergyLaser.laser.IClayLaserManager;

public class TileEntityLaserReflector extends TileEntityClayContainer implements IClayLaserMachine, IClayLaserManager {

    protected ClayLaser machineClayLaser;
    protected List<ClayLaser> listIrradiatedClayLasers = new ArrayList<>();
    protected List<Long> irradiatedTime = new ArrayList<>();
    protected ClayLaserManager manager;
    @SideOnly(Side.CLIENT)
    private AxisAlignedBB boundingBox;

    @Override
    public void initParams() {
        super.initParams();
        this.autoInsert = false;
        this.autoExtract = false;
        this.containerItemStacks = NonNullList.create();
        this.slotsDrop = new int[0];
        this.manager = new ClayLaserManager();
    }

    public boolean irradiateClayLaser(ClayLaser laser, EnumFacing direction) {
        if (laser == null) {
            return false;
        } else {
            long time = this.world.getWorldInfo().getWorldTotalTime();
            this.irradiatedTime.add(time);
            this.listIrradiatedClayLasers.add(laser);
            return true;
        }
    }

    @Override
    public void update() {
        long time = this.world.getWorldInfo().getWorldTotalTime();
        boolean flag = false;
        int num = 0;

        for (int i = 0; i < this.listIrradiatedClayLasers.size(); ++i) {
            if (this.irradiatedTime.get(i) != time) {
                ++num;
            }
        }

        ClayLaser[] array = new ClayLaser[num];
        List<ClayLaser> newListIrradiatedClayLasers = new ArrayList<>();
        List<Long> newIrradiatedTime = new ArrayList<>();
        int j = 0;

        for (int i = 0; i < this.listIrradiatedClayLasers.size(); ++i) {
            if (this.irradiatedTime.get(i) != time) {
                array[j] = this.listIrradiatedClayLasers.get(i);
                ++j;
                flag = true;
            } else {
                newListIrradiatedClayLasers.add(this.listIrradiatedClayLasers.get(i));
                newIrradiatedTime.add(this.irradiatedTime.get(i));
            }
        }

        if (!flag) {
            this.machineClayLaser = null;
        } else {
            this.machineClayLaser = ClayLaser.mergeClayLasers(array);
            ++this.machineClayLaser.age;
            this.listIrradiatedClayLasers = newListIrradiatedClayLasers;
            this.irradiatedTime = newIrradiatedTime;
        }

        this.manager.set(this.world, this.pos, this.world.getBlockState(this.pos).getValue(LaserReflector.FACING));
        this.manager.clayLaser = this.machineClayLaser;
        this.manager.update(flag);
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        this.manager.readFromNBT(tagCompound.getCompoundTag("ClayEnergyManager"));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        NBTTagCompound tagCompound1 = new NBTTagCompound();
        this.manager.writeToNBT(tagCompound1);
        tagCompound.setTag("ClayEnergyManager", tagCompound1);

        return tagCompound;
    }

    public ClayLaser getClayLaser() {
        return this.manager == null ? null : this.manager.clayLaser;
    }

    public EnumFacing getDirection() {
        return this.manager == null ? null : this.manager.getDirection();
    }

    public int getLaserLength() {
        return this.manager == null ? 0 : this.manager.getLaserLength();
    }

    public BlockPos getTargetCoord() {
        return this.manager == null ? null : this.manager.getTargetCoord();
    }

    public boolean hasTarget() {
        return this.manager != null && this.manager.hasTarget();
    }

    public boolean isIrradiating() {
        return this.manager != null && this.manager.isIrradiating();
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return Double.POSITIVE_INFINITY;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        EnumFacing direction = this.getDirection();
        if (direction == null) {
            return super.getRenderBoundingBox();
        }

        int l = this.getLaserLength();
        this.boundingBox = new AxisAlignedBB(
                Math.min(this.pos.getX(), this.pos.getX() + direction.getXOffset() * l),
                Math.min(this.pos.getY(), this.pos.getY() + direction.getYOffset() * l),
                Math.min(this.pos.getZ(), this.pos.getZ() + direction.getZOffset() * l),
                Math.max(this.pos.getX(), this.pos.getX() + direction.getXOffset() * l) + 1.0D,
                Math.max(this.pos.getY(), this.pos.getY() + direction.getYOffset() * l) + 1.0D,
                Math.max(this.pos.getZ(), this.pos.getZ() + direction.getZOffset() * l) + 1.0D);
        return this.boundingBox;
    }

    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    public boolean acceptInterfaceSync() {
        return false;
    }
}
