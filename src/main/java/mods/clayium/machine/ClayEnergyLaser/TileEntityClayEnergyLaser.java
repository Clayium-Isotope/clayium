package mods.clayium.machine.ClayEnergyLaser;

import mods.clayium.component.value.ContainClayEnergy;
import mods.clayium.machine.ClayContainer.BlockStateClayContainer;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaser;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaserManager;
import mods.clayium.machine.ClayEnergyLaser.laser.IClayLaserManager;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.util.TierPrefix;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
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

    protected final ContainClayEnergy containEnergy = new ContainClayEnergy();
    protected int machineConsumingEnergy;
    protected ClayLaser machineClayLaser;
    protected ClayLaserManager manager;
    protected boolean powered;
    public static final int consumingEnergyBlue = 40000;
    public static final int consumingEnergyGreen = 400000;
    public static final int consumingEnergyRed = 4000000;
    public static final int consumingEnergyWhite = 40000000;

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean powered) {
        // this.setInstantSyncFlag();
        this.powered = powered;
    }

    public void initParams() {
        super.initParams();
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
        this.autoExtractInterval = this.autoInsertInterval = 8;

        this.containerItemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
        this.setImportRoutes(NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, ENERGY_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.slotsDrop = new int[] { this.getEnergySlot() };
        this.autoInsert = false;
        this.autoExtract = true;

        this.manager = new ClayLaserManager();
    }

    public void setManager(World world, BlockPos pos, EnumFacing direction) {
        // if (this.manager == null) {
        // this.manager = new ClayLaserManager(world, pos, direction);
        // } else {
        this.manager.reset(world, pos, direction);
        // }

        // this.setSyncFlag();
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        this.tier = tier;

        switch (tier) {
            case claySteel:
                this.machineConsumingEnergy = consumingEnergyBlue;
                this.machineClayLaser = new ClayLaser(0, 1, 0, 0);
                break;
            case clayium:
                this.machineConsumingEnergy = consumingEnergyGreen;
                this.machineClayLaser = new ClayLaser(0, 0, 1, 0);
                break;
            case ultimate:
                this.machineConsumingEnergy = consumingEnergyRed;
                this.machineClayLaser = new ClayLaser(0, 0, 0, 1);
                break;
            case antimatter:
                this.machineConsumingEnergy = consumingEnergyWhite;
                this.machineClayLaser = new ClayLaser(0, 3, 3, 3);
        }

        this.manager.clayLaser = this.machineClayLaser;
    }

    public void update() {
        super.update();
        if (this.manager != null && this.world.getBlockState(this.pos) instanceof BlockStateClayContainer) {
            this.manager.set(this.world, this.pos,
                    this.world.getBlockState(this.pos).getValue(BlockStateClayContainer.FACING));
            this.manager.update(
                    this.isPowered() && IClayEnergyConsumer.checkAndConsumeClayEnergy(this, this.machineConsumingEnergy));
        }
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);

        this.manager.readFromNBT(tagCompound.getCompoundTag("ClayLaserManager"));
        this.setPowered(tagCompound.getBoolean("Powered"));
        this.containEnergy().deserializeNBT((NBTTagIntArray) tagCompound.getTag("ContainEnergy"));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);

        NBTTagCompound manager = new NBTTagCompound();
        this.manager.writeToNBT(manager);
        tagCompound.setTag("ClayLaserManager", manager);
        tagCompound.setBoolean("Powered", this.isPowered());
        tagCompound.setTag("ContainEnergy", this.containEnergy().serializeNBT());

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
    public ContainClayEnergy containEnergy() {
        return containEnergy;
    }

    @Override
    public int getClayEnergyStorageSize() {
        return 1;
    }

    @Override
    public void setClayEnergyStorageSize(int size) {}

    @Override
    public int getEnergySlot() {
        return 0;
    }

    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        return EnumMachineKind.clayEnergyLaser.getFaceResource();
    }

    @Override
    public TierPrefix getHullTier() {
        return this.tier;
    }
}
