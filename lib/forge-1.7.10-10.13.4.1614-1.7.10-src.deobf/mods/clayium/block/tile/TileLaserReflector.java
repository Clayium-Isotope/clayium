package mods.clayium.block.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.laser.ClayLaser;
import mods.clayium.block.laser.ClayLaserManager;
import mods.clayium.block.laser.IClayLaserMachine;
import mods.clayium.block.laser.IClayLaserManager;
import mods.clayium.util.UtilDirection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

public class TileLaserReflector
        extends TileClayContainer implements IClayLaserMachine, IClayLaserManager {
    protected int machineConsumingEnergy;
    protected ClayLaser machineClayLaser;
    protected List<ClayLaser> listIrradiatedClayLasers = new ArrayList<ClayLaser>();
    protected List<Long> irradiatedTime = new ArrayList<Long>();

    protected ClayLaserManager manager;
    @SideOnly(Side.CLIENT)
    private AxisAlignedBB boundingBox;

    public TileLaserReflector() {
        this.insertRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.extractRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.autoInsert = false;
        this.autoExtract = false;
        this.containerItemStacks = new ItemStack[1];
        this.slotsDrop = new int[0];

        this.manager = new ClayLaserManager();
    }


    public boolean irradiateClayLaser(ClayLaser laser, UtilDirection direction) {
        if (laser == null) return false;
        long time = this.worldObj.getWorldInfo().getWorldTotalTime();
        this.irradiatedTime.add(Long.valueOf(time));
        this.listIrradiatedClayLasers.add(laser);
        return true;
    }

    public void updateEntity() {
        super.updateEntity();

        long time = this.worldObj.getWorldInfo().getWorldTotalTime();
        boolean flag = false;
        int num = 0;
        for (int i = 0; i < this.listIrradiatedClayLasers.size(); i++) {
            if (((Long) this.irradiatedTime.get(i)).longValue() != time)
                num++;
        }
        ClayLaser[] array = new ClayLaser[num];
        List<ClayLaser> newListIrradiatedClayLasers = new ArrayList<ClayLaser>();
        List<Long> newIrradiatedTime = new ArrayList<Long>();
        int j = 0;
        for (int k = 0; k < this.listIrradiatedClayLasers.size(); k++) {
            if (((Long) this.irradiatedTime.get(k)).longValue() != time) {
                array[j] = this.listIrradiatedClayLasers.get(k);
                j++;
                flag = true;
            } else {
                newListIrradiatedClayLasers.add(this.listIrradiatedClayLasers.get(k));
                newIrradiatedTime.add(this.irradiatedTime.get(k));
            }
        }
        if (!flag) {
            this.machineClayLaser = null;
        } else {
            this.machineClayLaser = ClayLaser.mergeClayLasers(array);
            this.machineClayLaser.age++;
            this.listIrradiatedClayLasers = newListIrradiatedClayLasers;
            this.irradiatedTime = newIrradiatedTime;
        }
        this.manager.set(getWorldObj(), this.xCoord, this.yCoord, this.zCoord,
                UtilDirection.getOrientation(getBlockMetadata()));
        this.manager.clayLaser = this.machineClayLaser;
        this.manager.update(flag);
    }

    public boolean isUsable(ItemStack itemStack, EntityPlayer player, int direction, float hitX, float hitY, float hitZ) {
        return (getItemUseMode(itemStack, player) == 20);
    }


    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        int res = super.getItemUseMode(itemStack, player);
        return (res == 2) ? 20 : res;
    }


    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.manager.readFromNBT(tagCompound.getCompoundTag("ClayEnergyManager"));
    }


    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagCompound tagCompound1 = new NBTTagCompound();
        this.manager.writeToNBT(tagCompound1);
        tagCompound.setTag("ClayEnergyManager", (NBTBase) tagCompound1);
    }


    public void openInventory() {}


    public void closeInventory() {}


    public ClayLaser getClayLaser() {
        return (this.manager == null) ? null : this.manager.clayLaser;
    }

    public UtilDirection getDirection() {
        return (this.manager == null) ? null : this.manager.getDirection();
    }

    public int getLaserLength() {
        return (this.manager == null) ? 0 : this.manager.getLaserLength();
    }

    public int[] getTargetCoord() {
        return (this.manager == null) ? null : this.manager.getTargetCoord();
    }

    public boolean hasTarget() {
        return (this.manager == null) ? false : this.manager.hasTarget();
    }

    public boolean isIrradiating() {
        return (this.manager == null) ? false : this.manager.isIrradiating();
    }


    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return Double.POSITIVE_INFINITY;
    }


    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        UtilDirection direction = getDirection();
        if (direction != null) {
            if (this.boundingBox == null) {
                this.boundingBox = super.getRenderBoundingBox().copy();
            }
            int l = getLaserLength();
            this.boundingBox.maxX = Math.max(this.xCoord, this.xCoord + direction.offsetX * l) + 1.0D;
            this.boundingBox.minX = Math.min(this.xCoord, this.xCoord + direction.offsetX * l);
            this.boundingBox.maxY = Math.max(this.yCoord, this.yCoord + direction.offsetY * l) + 1.0D;
            this.boundingBox.minY = Math.min(this.yCoord, this.yCoord + direction.offsetY * l);
            this.boundingBox.maxZ = Math.max(this.zCoord, this.zCoord + direction.offsetZ * l) + 1.0D;
            this.boundingBox.minZ = Math.min(this.zCoord, this.zCoord + direction.offsetZ * l);
            return this.boundingBox;
        }
        return super.getRenderBoundingBox();
    }


    public boolean shouldRenderInPass(int pass) {
        return (pass == 1);
    }
}
