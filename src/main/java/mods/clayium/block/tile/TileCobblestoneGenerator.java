package mods.clayium.block.tile;

import java.util.ArrayList;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileCobblestoneGenerator
        extends TileClayBuffer
        implements IExternalControl {
    public int progress = 0;
    public int progressEfficiency = 5;
    public static int progressMax = 100;

    public int externalControlState = 0;
    public boolean isDoingWork = false;

    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.extractRoutes = new int[] {-1, -1, 0, -1, -1, -1};
        this.autoInsert = true;
        this.autoExtract = false;
    }

    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);
        this.listSlotsInsert = new ArrayList<int[]>();
        switch (tier) {
            case 1:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRateI(2);
                break;
            case 2:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRateI(5);
                break;
            case 3:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRateI(15);
                break;
            case 4:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRateI(50);
                break;
            case 5:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRateI(200);
                break;
            case 6:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRateI(1000);
                break;
            case 7:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRateI(8000);
                break;
        }

    }


    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return false;
    }

    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return false;
    }


    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);


        this.progress = tagCompound.getInteger("Progress");
        this.progressEfficiency = tagCompound.getInteger("ProgressEfficiency");

        this.externalControlState = tagCompound.getInteger("ExternalControlState");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("Progress", this.progress);
        tagCompound.setInteger("ProgressEfficiency", this.progressEfficiency);

        tagCompound.setInteger("ExternalControlState", this.externalControlState);
    }

    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            produce();
        }
    }

    public void produce() {
        this.isDoingWork = false;
        if (canProduce()) {

            if (this.externalControlState >= 0)
                this.progress += this.progressEfficiency;
            while (this.progress >= progressMax) {
                setSyncFlag();
                ItemStack cobble = new ItemStack(Blocks.cobblestone);
                this.progress -= progressMax;


                UtilTransfer.produceItemStack(cobble, this.containerItemStacks, 0, this.inventoryX * this.inventoryY, getInventoryStackLimit());
                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;
                }
                this.isDoingWork = true;
            }
        }
    }

    public boolean canProduce() {
        ForgeDirection[] sides = {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST};
        boolean flag = false, flag2 = false;
        for (ForgeDirection side : sides) {
            Block block = this.worldObj.getBlock(this.xCoord + side.offsetX, this.yCoord + side.offsetY, this.zCoord + side.offsetZ);
            if (block.getMaterial() == Material.water) {
                flag = true;
            }
            if (block.getMaterial() == Material.lava) {
                flag2 = true;
            }
        }
        return (flag && flag2);
    }


    public void doWorkOnce() {
        if (this.externalControlState > 0) {
            this.externalControlState++;
        } else {
            this.externalControlState = 1;
        }
    }

    public void startWork() {
        this.externalControlState = 0;
    }

    public void stopWork() {
        this.externalControlState = -1;
    }

    public boolean isScheduled() {
        return canProduce();
    }

    public boolean isDoingWork() {
        return this.isDoingWork;
    }
}
