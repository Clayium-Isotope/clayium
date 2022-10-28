package mods.clayium.machine.ClayEnergyLaser;

import mods.clayium.machine.ClayContainer.BlockStateClayDirectionalContainer;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaser;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaserManager;
import mods.clayium.machine.ClayEnergyLaser.laser.IClayLaserManager;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityClayEnergyLaser extends TileEntityClayContainer implements IClayEnergyConsumer, IClayLaserManager {
    protected long containEnergy;
    protected int machineConsumingEnergy;
    protected ClayLaser machineClayLaser;
    protected ClayLaserManager manager;
    protected boolean powered;
    public static final int consumingEnergyBlue = 40000;
    public static final int consumingEnergyGreen = 400000;
    public static final int consumingEnergyRed = 4000000;
    public static final int consumingEnergyWhite = 40000000;

    public TileEntityClayEnergyLaser() {
        super();
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean powered) {
//        this.setInstantSyncFlag();
        this.powered = powered;
    }

    public void initParams() {
        super.initParams();
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
        this.autoExtractInterval = this.autoInsertInterval = 8;

        this.containerItemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
        this.slotsDrop = new int[]{ this.getEnergySlot() };
        this.autoInsert = false;
        this.autoExtract = true;

        this.manager = new ClayLaserManager();
    }

    public void setManager(World world, BlockPos pos, EnumFacing direction) {
//        if (this.manager == null) {
//            this.manager = new ClayLaserManager(world, pos, direction);
//        } else {
            this.manager.reset(world, pos, direction);
//        }

//        this.setSyncFlag();
    }

    public void initParamsByTier(int tier) {
        this.tier = tier;

        switch(tier) {
            case 7:
                this.machineConsumingEnergy = consumingEnergyBlue;
                this.machineClayLaser = new ClayLaser(0, 1, 0, 0);
                break;
            case 8:
                this.machineConsumingEnergy = consumingEnergyGreen;
                this.machineClayLaser = new ClayLaser(0, 0, 1, 0);
                break;
            case 9:
                this.machineConsumingEnergy = consumingEnergyRed;
                this.machineClayLaser = new ClayLaser(0, 0, 0, 1);
                break;
            case 10:
                this.machineConsumingEnergy = consumingEnergyWhite;
                this.machineClayLaser = new ClayLaser(0, 3, 3, 3);
        }

        this.manager.clayLaser = this.machineClayLaser;
    }

    public void update() {
        super.update();
        if (this.manager != null) {
            this.manager.set(this.world, this.pos, this.world.getBlockState(this.pos).getValue(BlockStateClayDirectionalContainer.FACING));
            this.manager.update(this.isPowered() && this.compensateClayEnergy(this.machineConsumingEnergy));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.manager.readFromNBT(tagCompound.getCompoundTag("ClayEnergyManager"));
        this.setPowered(tagCompound.getBoolean("Powered"));
        this.containEnergy = tagCompound.getLong("ContainEnergy");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagCompound manager = new NBTTagCompound();
        this.manager.writeToNBT(manager);
        tagCompound.setTag("ClayEnergyManager", manager);
        tagCompound.setBoolean("Powered", this.isPowered());
        tagCompound.setLong("ContainEnergy", this.containEnergy);
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

    @SideOnly(Side.CLIENT) // from TileEntityBeacon
    public double getMaxRenderDistanceSquared() {
        return 65536.0d;
    }

    @SideOnly(Side.CLIENT) // from TileEntityBeacon
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    public long getContainEnergy() {
        return containEnergy;
    }

    @Override
    public void setContainEnergy(long energy) {
        this.containEnergy = energy;
    }

    @Override
    public int getEnergySlot() {
        return 0;
    }

    @Override
    public int getEnergyStorageSize() {
        return this.clayEnergyStorageSize;
    }

    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        return EnumMachineKind.clayEnergyLaser.getFaceResource();
    }
}
