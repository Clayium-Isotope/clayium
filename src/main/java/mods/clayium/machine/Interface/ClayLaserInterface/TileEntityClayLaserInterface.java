package mods.clayium.machine.Interface.ClayLaserInterface;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaser;
import mods.clayium.machine.ClayEnergyLaser.laser.IClayLaserMachine;
import mods.clayium.machine.Interface.IInterfaceCaptive;
import mods.clayium.machine.Interface.ISynchronizedInterface;
import mods.clayium.util.SyncManager;
import mods.clayium.util.TierPrefix;

public class TileEntityClayLaserInterface extends TileEntityGeneric
                                          implements ISynchronizedInterface, IClayLaserMachine {

    protected boolean enableSync = false;
    protected boolean synced = false;
    protected IInterfaceCaptive core = IInterfaceCaptive.NONE;
    protected int[] syncSource = null;
    private TierPrefix tier;

    @Override
    public void initParamsByTier(TierPrefix tier) {
        this.tier = tier;
    }

    @Override
    public boolean isSynced() {
        return this.synced && this.core != IInterfaceCaptive.NONE;
    }

    /**
     * assert
     * 
     * <pre>
     * {@code this.enableSync == true}
     * </pre>
     */
    public void setCoreBlock(@Nullable IInterfaceCaptive tile) {
        if (tile == null) tile = IInterfaceCaptive.NONE;

        this.core = tile;
        this.synced = IInterfaceCaptive.isSyncable(tile);
        this.initParamsByTier(this.tier);
    }

    public IInterfaceCaptive getCore() {
        return this.core;
    }

    public boolean markEnableSync() {
        if (this.isSyncEnabled()) return false;

        this.enableSync = true;
        return true;
    }

    @Override
    public TierPrefix getHullTier() {
        return this.tier;
    }

    public boolean isSyncEnabled() {
        return this.enableSync;
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("CustomName", Constants.NBT.TAG_STRING)) {
            this.setCustomName(compound.getString("CustomName"));
        }

        if (compound.hasKey("SyncSource", Constants.NBT.TAG_INT_ARRAY)) {
            this.syncSource = compound.getIntArray("SyncSource");
        }

        this.enableSync = compound.getBoolean("SyncEnabled");

        initParamsByTier(TierPrefix.get(compound.getInteger("Tier")));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound compound) {
        if (hasCustomName()) {
            compound.setString("CustomName", this.getName());
        }

        compound.setInteger("Tier", this.tier.meta());

        if (this.isSynced()) {
            compound.setIntArray("SyncSource", SyncManager.getIntArrayFromTile((TileEntity) this.core));
        }

        compound.setBoolean("SyncEnabled", this.enableSync);

        return compound;
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (this.isSynced()) {
            this.core.markDirty();

            assert this.core instanceof TileEntity;
            TileEntity te = SyncManager.getTileFromIntArray(SyncManager.getIntArrayFromTile((TileEntity) this.core));
            if (IInterfaceCaptive.isSyncable(te)) {
                SyncManager.immediateSync((IInterfaceCaptive) te, this);
            } else {
                SyncManager.immediateSync(null, this);
            }
        }
    }

    @Override
    public boolean irradiateClayLaser(ClayLaser laser, EnumFacing facing) {
        if (!this.isSynced() || !(this.core instanceof IClayLaserMachine)) return false;

        return ((IClayLaserMachine) this.core).irradiateClayLaser(laser, facing);
    }
}
