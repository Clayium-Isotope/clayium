package mods.clayium.machine.CobblestoneGenerator;

import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class TileEntityCobblestoneGenerator extends TileEntityClayBuffer {
    public int progress = 0;
    public int progressEfficiency = 5;
    public static int progressMax = 100;
    public int externalControlState = 0;
    public boolean isDoingWork = false;
    protected boolean isCobblestoneGenerator = true;

    @Override
    public void initParams() {
        super.initParams();
        this.setExportRoutes(-1, -1, 0, -1, -1, -1);
        this.autoInsert = true;
        this.autoExtract = false;
        this.isBuffer = false;
    }

    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);
        this.listSlotsImport.clear();
        switch(tier) {
            case 1:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRate(2);
                break;
            case 2:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRate(5);
                break;
            case 3:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRate(15);
                break;
            case 4:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRate(50);
                break;
            case 5:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRate(200);
                break;
            case 6:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRate(1000);
                break;
            case 7:
                this.progressEfficiency = ClayiumCore.multiplyProgressionRate(8000);
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return isCobblestoneGenerator ? false : super.isItemValidForSlot(slot, itemstack);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isCobblestoneGenerator ? false : super.canInsertItem(index, itemStackIn, direction);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.progress = tagCompound.getInteger("Progress");
        this.progressEfficiency = tagCompound.getInteger("ProgressEfficiency");
        this.externalControlState = tagCompound.getInteger("ExternalControlState");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("Progress", this.progress);
        tagCompound.setInteger("ProgressEfficiency", this.progressEfficiency);
        tagCompound.setInteger("ExternalControlState", this.externalControlState);
        return tagCompound;
    }

    public void update() {
        super.update();
        if (!this.world.isRemote) {
            this.produce();
        }
    }

    public void produce() {
        this.isDoingWork = false;
        if (this.canProduce()) {
            if (this.externalControlState >= 0) {
                this.progress += this.progressEfficiency;
            }

            for(; this.progress >= progressMax; this.isDoingWork = true) {
                this.progress -= progressMax;
                UtilTransfer.produceItemStack(new ItemStack(Blocks.COBBLESTONE), this.containerItemStacks, 0, this.inventoryX * this.inventoryY, this.getInventoryStackLimit());
                if (this.externalControlState > 0) {
                    --this.externalControlState;
                    if (this.externalControlState == 0) {
                        this.externalControlState = -1;
                    }
                }
            }
        }
    }

    public boolean canProduce() {
        boolean water = false;
        boolean lava = false;

        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            IBlockState block = this.world.getBlockState(this.pos.offset(facing));
            if (block.getMaterial() == Material.WATER) {
                water = true;
            }

            if (block.getMaterial() == Material.LAVA) {
                lava = true;
            }
        }

        return water && lava;
    }

    public void doWorkOnce() {
        if (this.externalControlState > 0) {
            ++this.externalControlState;
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
        return this.canProduce();
    }

    public boolean isDoingWork() {
        return this.isDoingWork;
    }

    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        return EnumMachineKind.cobblestoneGenerator.getFaceResource();
    }
}
