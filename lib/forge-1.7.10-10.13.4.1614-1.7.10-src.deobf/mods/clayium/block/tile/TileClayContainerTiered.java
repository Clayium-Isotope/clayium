package mods.clayium.block.tile;

import mods.clayium.block.ClayContainerTiered;
import mods.clayium.util.UtilTier;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;

public abstract class TileClayContainerTiered extends TileClayContainer {
    protected int baseTier = -1;


    private boolean initialized = false;


    public void setBaseTier(int tier) {
        this.baseTier = tier;


        if (!this.initialized && this.baseTier != -1) {
            initByTier(this.baseTier);
        }
    }


    public int getTier() {
        return this.baseTier;
    }

    public int getRecipeTier() {
        return getTier();
    }


    public void updateEntity() {
        super.updateEntity();


        if (this.baseTier == -1 && this.worldObj != null) {
            Block block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
            if (block != null && block instanceof ClayContainerTiered) {
                setBaseTier(((ClayContainerTiered) block).getTier((IBlockAccess) this.worldObj, this.xCoord, this.yCoord, this.zCoord));
            }
        }
        if (!this.initialized && this.baseTier != -1) {
            initByTier(this.baseTier);
            setSyncFlag();
        }
    }


    public boolean acceptEnergyClay() {
        return UtilTier.acceptEnergyClay(this.baseTier);
    }


    public final void initByTier(int tier) {
        this.initialized = true;
        initParamsByTier(tier);
    }


    public void initParamsByTier(int tier) {}


    enum ParamMode {
        MACHINE, BUFFER;
    }

    protected void setDefaultTransportationParamsByTier(int tier, ParamMode mode) {
        switch (mode) {


            case MACHINE:
                UtilTier.TierManager.applyTransportTierManager(this, tier, UtilTier.tierMachineTransport);
                if (!UtilTier.canAutoTransfer(this.baseTier)) {
                    this.autoExtract = this.autoInsert = false;
                }
                break;


            case BUFFER:
                UtilTier.TierManager.applyTransportTierManager(this, tier, UtilTier.tierBufferTransport);
                break;
        }
    }

    public void readCoordFromNBT(NBTTagCompound tagCompound) {
        super.readCoordFromNBT(tagCompound);
        if (tagCompound.hasKey("BaseTier") &&
                this.baseTier == -1) {
            setBaseTier(tagCompound.getShort("BaseTier"));
        }
    }


    public void writeCoordToNBT(NBTTagCompound tagCompound) {
        super.writeCoordToNBT(tagCompound);
        tagCompound.setShort("BaseTier", (short) this.baseTier);
    }
}
