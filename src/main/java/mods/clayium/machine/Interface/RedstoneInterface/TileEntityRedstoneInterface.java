package mods.clayium.machine.Interface.RedstoneInterface;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.machine.Interface.IExternalControl;
import mods.clayium.machine.Interface.IInterfaceCaptive;
import mods.clayium.machine.Interface.ISynchronizedInterface;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class TileEntityRedstoneInterface extends TileEntityGeneric implements ISynchronizedInterface, ITickable {
    protected boolean synced = false;
    protected IInterfaceCaptive core = IInterfaceCaptive.NONE;
    protected int[] syncSource = null;
    protected int lastSignal = 0;
    protected int lastPower = 0;
    private int tier;

    public TileEntityRedstoneInterface() {}

    @Override
    public void initParamsByTier(int tier) {
        this.tier = tier;
    }

    @Override
    public void setCoreBlock(@Nullable IInterfaceCaptive core) {
        if (IInterfaceCaptive.isSyncable(core)) {
            this.core = core;
            this.synced = true;
        } else {
            this.core = IInterfaceCaptive.NONE;
            this.synced = false;
        }
    }

    public boolean isSynced() {
        return this.synced;
    }

    @Override
    public boolean isSyncEnabled() {
        return true;
    }

    @Override
    public boolean markEnableSync() {
        return false;
    }

    @Override
    public int getHullTier() {
        return this.tier;
    }

    @Override
    public void update() {
        int signal = this.getSignal();
        int power = this.getProvidingWeakPower();

        if (this.syncSource != null) {
            if (UtilBuilder.getTileFromIntArray(this.syncSource) instanceof IInterfaceCaptive)
                UtilBuilder.synchronize((IInterfaceCaptive) UtilBuilder.getTileFromIntArray(this.syncSource), this);
            this.syncSource = null;
        }

        if (!this.isSynced()) {
            return;
        }

        IExternalControl te = IExternalControl.cast(this.core);
        switch (this.getState()) {
            case DoWork:
                if (this.lastSignal <= 0 && signal > 0) {
                    te.startWork();
                }

                if (this.lastSignal != 0 && signal == 0) {
                    te.stopWork();
                }
                break;

            case DoNotWork:
                if (this.lastSignal <= 0 && signal > 0) {
                    te.stopWork();
                }

                if (this.lastSignal != 0 && signal == 0) {
                    te.startWork();
                }
                break;

            case StartWork:
                if (this.lastSignal <= 0 && signal > 0) {
                    te.startWork();
                }
                break;

            case StopWork:
                if (this.lastSignal <= 0 && signal > 0) {
                    te.stopWork();
                }
                break;

            case DoWorkOnce:
                if (this.lastSignal <= 0 && signal > 0) {
                    te.doWorkOnce();
                }
                break;
        }

        if (this.lastPower != power || this.lastSignal != signal) {
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
        }

        this.lastSignal = signal;
        this.lastPower = power;
    }

    public int getProvidingWeakPower() {
        if (!this.isSynced()) {
            return 0;
        }

        IExternalControl te = IExternalControl.cast(this.core);
        switch (this.getState()) {
            case EmitIfWorkScheduled:
                return te.isScheduled() ? 15 : 0;

            case EmitIfDoingWork:
                return te.isDoingWork() ? 15 : 0;

            case EmitIfIdle:
                return te.isDoingWork() ? 0 : 15;

            default:
                return 0;
        }
    }

    public IInterfaceCaptive getCore() {
        return this.core;
    }

    public int getSignal() {
        return this.world.isBlockIndirectlyGettingPowered(this.pos);
    }

    public EnumControlState changeState() {
        IBlockState newState = this.world.getBlockState(this.pos).cycleProperty(BlockStateRedstoneInterface.CONTROL_STATE);

        this.world.setBlockState(this.pos, newState, 3);

        return newState.getValue(BlockStateRedstoneInterface.CONTROL_STATE);
    }

    public void changeState(EnumControlState state) {
        this.lastSignal = -1;

        this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withProperty(BlockStateRedstoneInterface.CONTROL_STATE, state), 3);

//        this.setInstantSyncFlag();
    }

    public EnumControlState getState() {
        return this.world.getBlockState(this.pos).getValue(BlockStateRedstoneInterface.CONTROL_STATE);
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("CustomName", Constants.NBT.TAG_STRING)) {
            this.setCustomName(compound.getString("CustomName"));
        }

        if (compound.hasKey("SyncSource", Constants.NBT.TAG_INT_ARRAY)) {
            this.syncSource = compound.getIntArray("SyncSource");
        }

        this.lastSignal = compound.getInteger("LastSignal");
        this.lastPower = compound.getInteger("LastPower");

        initParamsByTier(compound.getInteger("Tier"));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound compound) {
        if (hasCustomName()) {
            compound.setString("CustomName", this.getName());
        }

        compound.setInteger("Tier", this.tier);

        if (this.isSynced()) {
            compound.setIntArray("SyncSource", UtilBuilder.getIntArrayFromTile((TileEntity) this.core));
        }

        compound.setInteger("LastSignal", this.lastSignal);
        compound.setInteger("LastPower", this.lastPower);

        return compound;
    }
}
