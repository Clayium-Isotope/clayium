package mods.clayium.block.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.laser.ClayLaser;
import mods.clayium.block.laser.ClayLaserManager;
import mods.clayium.block.laser.IClayLaserManager;
import mods.clayium.util.UtilDirection;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileClayEnergyLaser extends TileClayContainerTiered implements IClayLaserManager {
    protected int machineConsumingEnergy;
    protected ClayLaser machineClayLaser;
    protected ClayLaserManager manager;
    @SideOnly(Side.CLIENT)
    private AxisAlignedBB boundingBox;
    protected boolean powered;

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean powered) {
        setInstantSyncFlag();
        this.powered = powered;
    }

    public static int consumingEnergyBlue = 40000;
    public static int consumingEnergyGreen = 400000;
    public static int consumingEnergyRed = 4000000;
    public static int consumingEnergyWhite = 40000000;

    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, -1, -1, 0, -1, -1};
        this.extractRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.autoInsert = false;
        this.autoExtract = true;
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
        this.autoExtractInterval = this.autoInsertInterval = 8;
        this.containerItemStacks = new net.minecraft.item.ItemStack[1];
        this.listSlotsInsert.add(new int[] {0});
        this.slotsDrop = new int[] {0};
        this.clayEnergySlot = 0;
        this.manager = new ClayLaserManager();
    }


    public void setManager(World world, int x, int y, int z, UtilDirection direction) {
        if (this.manager == null) {
            this.manager = new ClayLaserManager(world, x, y, z, direction);
        } else {

            this.manager.reset(world, x, y, z, direction);
        }
        this.manager.clayLaser = this.machineClayLaser;
        setSyncFlag();
    }

    public void initParamsByTier(int tier) {
        switch (tier) {
            case 7:
                this.machineConsumingEnergy = consumingEnergyBlue;
                this.machineClayLaser = new ClayLaser(0, new int[] {1, 0, 0});
                break;
            case 8:
                this.machineConsumingEnergy = consumingEnergyGreen;
                this.machineClayLaser = new ClayLaser(0, new int[] {0, 1, 0});
                break;
            case 9:
                this.machineConsumingEnergy = consumingEnergyRed;
                this.machineClayLaser = new ClayLaser(0, new int[] {0, 0, 1});
                break;
            default:
                this.machineConsumingEnergy = consumingEnergyWhite;
                this.machineClayLaser = new ClayLaser(0, new int[] {3, 3, 3});
                break;
        }
        this.manager.clayLaser = this.machineClayLaser;
    }

    public void updateEntity() {
        super.updateEntity();
        if (this.manager != null) {
            this.manager.set(getWorldObj(), this.xCoord, this.yCoord, this.zCoord,
                    UtilDirection.getOrientation(getFrontDirection()));
            this.manager.update((

                    !isPowered() &&
                            consumeClayEnergy(this.machineConsumingEnergy)));
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.manager.readFromNBT(tagCompound.getCompoundTag("ClayEnergyManager"));
        setPowered(tagCompound.getBoolean("Powered"));
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagCompound tagCompound1 = new NBTTagCompound();
        this.manager.writeToNBT(tagCompound1);
        tagCompound.setTag("ClayEnergyManager", (NBTBase) tagCompound1);
        tagCompound.setBoolean("Powered", isPowered());
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
