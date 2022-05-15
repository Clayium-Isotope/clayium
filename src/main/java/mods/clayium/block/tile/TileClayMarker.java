package mods.clayium.block.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileClayMarker
        extends TileGeneric implements IAxisAlignedBBProvider, IAxisAlignedBBContainer {
    protected AxisAlignedBB aabb;
    public int state = 0;


    public static int maxRange = 64;
    protected int xx = 0;
    protected int yy = 0;
    protected int zz = 0;

    public void activate() {
        if (!this.worldObj.isRemote) {
            setInstantSyncFlag();

            int i = 1;
            this.xx = this.yy = this.zz = 0;
            for (i = 1; i < maxRange; i++) {
                if (isMarker(i, 0, 0)) {
                    this.xx = i;
                    break;
                }
                if (isMarker(-i, 0, 0)) {
                    this.xx = -i;
                    break;
                }

            }
            for (i = 1; i < maxRange; i++) {
                if (isMarker(0, i, 0)) {
                    this.yy = i;
                    break;
                }
                if (isMarker(0, -i, 0)) {
                    this.yy = -i;
                    break;
                }

            }
            for (i = 1; i < maxRange; i++) {
                if (isMarker(0, 0, i)) {
                    this.zz = i;
                    break;
                }
                if (isMarker(0, 0, -i)) {
                    this.zz = -i;
                    break;
                }

            }
            if (this.xx != 0 || this.yy != 0 || this.zz != 0 || this.state == 1) {
                this.aabb = AxisAlignedBB.getBoundingBox(Math.min(this.xCoord, this.xCoord + this.xx), Math.min(this.yCoord, this.yCoord + this.yy), Math.min(this.zCoord, this.zCoord + this.zz), (
                        Math.max(this.xCoord, this.xCoord + this.xx) + 1), (Math.max(this.yCoord, this.yCoord + this.yy) + 1), (Math.max(this.zCoord, this.zCoord + this.zz) + 1));
                this.state = (this.state <= 1) ? 3 : ((++this.state >= 5) ? 2 : this.state);
            } else {
                this.aabb = null;
                this.state = (this.state == 0) ? 1 : 0;
            }
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.state = tagCompound.getByte("State");
        readAxisAlignedBBFromNBT(tagCompound, this);
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setByte("State", (byte) this.state);
        writeAxisAlignedBBToNBT(tagCompound, this);
    }

    protected boolean isMarker(int xx, int yy, int zz) {
        return this.worldObj.getBlock(this.xCoord + xx, this.yCoord + yy, this.zCoord + zz) instanceof mods.clayium.block.ClayMarker;
    }


    public AxisAlignedBB getAxisAlignedBB() {
        return this.aabb;
    }


    public void setAxisAlignedBB(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }


    public boolean hasAxisAlignedBB() {
        return (this.aabb != null && this.state >= 2);
    }


    public int getBoxAppearance() {
        return (this.state < 2) ? 0 : (this.state - 2);
    }


    public void setAxisAlignedBBToMachine() {
        markDirty();
        breakMarker(this.xx, 0, 0);
        breakMarker(0, this.yy, 0);
        breakMarker(0, 0, this.zz);
        breakMarker(0, 0, 0);
    }

    protected void breakMarker(int xx, int yy, int zz) {
        Block block = this.worldObj.getBlock(this.xCoord + xx, this.yCoord + yy, this.zCoord + zz);
        if (block instanceof mods.clayium.block.ClayMarker) {
            UtilBuilder.dropItems(this.worldObj, this.xCoord, this.yCoord, this.zCoord,
                    UtilBuilder.harvestBlock(this.worldObj, this.xCoord + xx, this.yCoord + yy, this.zCoord + zz, false, false, 0));
        }
    }


    public boolean shouldRenderInPass(int pass) {
        return (pass == 1);
    }


    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return Double.POSITIVE_INFINITY;
    }


    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }


    public static void writeAxisAlignedBBToNBT(NBTTagCompound tagCompound, IAxisAlignedBBContainer tile) {
        if (tile.hasAxisAlignedBB()) {
            tagCompound.setBoolean("hasAABB", true);
            tagCompound.setDouble("AABBMinX", (tile.getAxisAlignedBB()).minX);
            tagCompound.setDouble("AABBMinY", (tile.getAxisAlignedBB()).minY);
            tagCompound.setDouble("AABBMinZ", (tile.getAxisAlignedBB()).minZ);
            tagCompound.setDouble("AABBMaxX", (tile.getAxisAlignedBB()).maxX);
            tagCompound.setDouble("AABBMaxY", (tile.getAxisAlignedBB()).maxY);
            tagCompound.setDouble("AABBMaxZ", (tile.getAxisAlignedBB()).maxZ);
        } else {
            tagCompound.setBoolean("hasAABB", false);
        }
    }

    public static void readAxisAlignedBBFromNBT(NBTTagCompound tagCompound, IAxisAlignedBBContainer tile) {
        boolean hasAABB = tagCompound.getBoolean("hasAABB");
        if (hasAABB) {
            double minX = tagCompound.getDouble("AABBMinX");
            double minY = tagCompound.getDouble("AABBMinY");
            double minZ = tagCompound.getDouble("AABBMinZ");
            double maxX = tagCompound.getDouble("AABBMaxX");
            double maxY = tagCompound.getDouble("AABBMaxY");
            double maxZ = tagCompound.getDouble("AABBMaxZ");

            if (!tile.hasAxisAlignedBB() ||
                    (tile.getAxisAlignedBB()).minX != minX || (tile.getAxisAlignedBB()).minY != minY || (tile.getAxisAlignedBB()).minZ != minZ ||
                    (tile.getAxisAlignedBB()).maxX != maxX || (tile.getAxisAlignedBB()).maxY != maxY || (tile.getAxisAlignedBB()).maxZ != maxZ)
                tile.setAxisAlignedBB(AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ));
        }
    }
}
