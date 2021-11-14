package mods.clayium.block.tile;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

import java.util.Map;

import mods.clayium.block.ClayRFGenerator;
import mods.clayium.block.IClayContainerModifier;
import mods.clayium.block.IOverclocker;
import mods.clayium.util.UtilDirection;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class TileClayRFGenerator
        extends TileClayContainerTiered
        implements IEnergyProvider {
    protected String blockName;
    public int rfStored = 0;
    public int maxRfStored = 10000;

    public long ceConsumptionPerTick = 100L;
    public int rfProductionPerTick = 10;
    public int rfOutputPerTick = 10;

    public long ceConsumptionPerTickBase = 100L;
    public int rfProductionPerTickBase = 10;
    public int rfOutputPerTickBase = 10;

    public double overclockExponent = 1.0D;

    public double overclockTotalFactor = 1.0D;

    protected boolean powered;


    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, -1, -1, 0, -1, -1};
        this.extractRoutes = new int[] {-1, -1, 0, -1, -1, -1};
        this.autoInsert = false;
        this.autoExtract = true;
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
        this.autoExtractInterval = this.autoInsertInterval = 8;
        this.containerItemStacks = new net.minecraft.item.ItemStack[1];
        this.listSlotsInsert.add(new int[] {0});
        this.listSlotsExtract.add(new int[0]);
        this.slotsDrop = new int[] {0};
        this.clayEnergySlot = 0;
    }


    public void openInventory() {}


    public void closeInventory() {}


    public boolean canConnectEnergy(ForgeDirection paramForgeDirection) {
        if (!canGetFrontDirection())
            return false;
        int front = getFrontDirection();
        int d = UtilDirection.direction2Side(front, paramForgeDirection.ordinal()) - 6;
        return (this.extractRoutes[d] == 0);
    }


    public int extractEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean) {
        if (!canConnectEnergy(paramForgeDirection) || isPowered())
            return 0;
        int i = Math.min(this.rfStored, Math.min(this.rfOutputPerTick, paramInt));
        if (!paramBoolean) {
            this.rfStored -= i;
        }
        return i;
    }


    public int getEnergyStored(ForgeDirection paramForgeDirection) {
        return this.rfStored;
    }


    public int getMaxEnergyStored(ForgeDirection paramForgeDirection) {
        return this.maxRfStored;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean powered) {
        setInstantSyncFlag();
        this.powered = powered;
    }


    public void readCoordFromNBT(NBTTagCompound tagCompound) {
        super.readCoordFromNBT(tagCompound);
        if (tagCompound.hasKey("BlockName") &&
                this.blockName == null) {
            setBlockName(tagCompound.getString("BlockName"));
        }
    }


    public void writeCoordToNBT(NBTTagCompound tagCompound) {
        super.writeCoordToNBT(tagCompound);
        tagCompound.setString("BlockName", this.blockName);
    }


    public void setBlockName(String blockName) {
        this.blockName = blockName;
        if (!this.initializedBlockName && this.blockName != null) {
            initByBlockName(this.blockName);
            this.initializedBlockName = true;
        }
    }

    protected boolean initializedBlockName = false;

    public void initByBlockName(String blockName) {
        this.blockName = blockName;
        if (!this.initializedBlockName && this.blockName != null) {
            Map<String, Object> config = ClayRFGenerator.getConfig(this.blockName);
            if (config != null) {
                Object obj = config.get("CEConsumptionPerTick");
                if (obj instanceof Number) {
                    this.ceConsumptionPerTickBase = ((Number) obj).longValue();
                }
                obj = config.get("RFProductionPerTick");
                if (obj instanceof Number) {
                    this.rfProductionPerTickBase = ((Number) obj).intValue();
                }
                obj = config.get("RFOutputPerTick");
                if (obj instanceof Number) {
                    this.rfOutputPerTickBase = ((Number) obj).intValue();
                }
                obj = config.get("RFStorageSize");
                if (obj instanceof Number) {
                    this.maxRfStored = ((Number) obj).intValue();
                }
                obj = config.get("OverclockExponent");
                if (obj instanceof Number) {
                    this.overclockExponent = ((Number) obj).doubleValue();
                }
            }
        }
    }


    public void updateEntity() {
        super.updateEntity();

        UtilDirection[] directions = {UtilDirection.UP, UtilDirection.DOWN, UtilDirection.NORTH, UtilDirection.SOUTH, UtilDirection.WEST, UtilDirection.EAST};


        this.clayEnergyStorageSize = 1;

        this.overclockTotalFactor = 1.0D;
        for (UtilDirection direction : directions) {
            Block block = this.worldObj.getBlock(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
            if (block instanceof IOverclocker) {
                this.overclockTotalFactor *= ((IOverclocker) block).getOverclockFactor((IBlockAccess) this.worldObj, this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
            }
            if (block instanceof IClayContainerModifier) {
                ((IClayContainerModifier) block).modifyClayContainer((IBlockAccess) this.worldObj, this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ, this);
            }
        }

        double overclockExponentFactor = Math.pow(this.overclockTotalFactor, this.overclockExponent);
        double d = this.ceConsumptionPerTickBase * overclockExponentFactor;
        this.ceConsumptionPerTick = (d >= 9.223372036854776E18D) ? Long.MAX_VALUE : (long) d;
        d = this.rfProductionPerTickBase * overclockExponentFactor;
        this.rfProductionPerTick = (d >= 2.147483647E9D) ? Integer.MAX_VALUE : (int) d;
        d = this.rfOutputPerTickBase * overclockExponentFactor;
        this.rfOutputPerTick = (d >= 2.147483647E9D) ? Integer.MAX_VALUE : (int) d;
        if (this.rfStored < this.maxRfStored && consumeClayEnergy(this.ceConsumptionPerTick)) {
            this.rfStored += this.rfProductionPerTick;
        }

        if (!isPowered()) {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                if (canConnectEnergy(direction)) {
                    TileEntity tile = UtilDirection.getTileEntity((IBlockAccess) this.worldObj, this.xCoord, this.yCoord, this.zCoord, direction);
                    if (tile instanceof IEnergyReceiver) {
                        ForgeDirection opposite = direction.getOpposite();
                        if (((IEnergyReceiver) tile).canConnectEnergy(opposite)) {
                            extractEnergy(direction, ((IEnergyReceiver) tile).receiveEnergy(opposite, extractEnergy(direction, this.rfOutputPerTick, true), false), false);
                        }
                    }
                }
            }
        }
    }


    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        setPowered(tagCompound.getBoolean("Powered"));
        this.rfStored = tagCompound.getInteger("RFStored");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("Powered", isPowered());
        tagCompound.setInteger("RFStored", this.rfStored);
    }
}
