package mods.clayium.machine.WaterWheel;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.util.UtilTier;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

import java.util.Random;

public class TileEntityWaterWheel extends TileEntityClayiumMachine {
    public int progress = 0;
    public int progressEfficiency = 1000;
    protected static int progressMax = 20000;
    private static Random random = new Random();

    public void initParams() {
        super.initParams();
        this.progressEfficiency = (int)((double)this.progressEfficiency * Math.pow(Math.max(this.tier, 1.0D), 3.0D));
        this.containerItemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
        this.listSlotsImport.add(new int[]{0});
        this.listSlotsExport.add(new int[]{0});
        this.slotsDrop = new int[]{0};
        this.autoInsert = false;
        this.autoExtract = false;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.progress = tagCompound.getInteger("Progress");
        this.progressEfficiency = tagCompound.getInteger("ProgressEfficiency");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("Progress", this.progress);
        tagCompound.setInteger("ProgressEfficiency", this.progressEfficiency);
        return tagCompound;
    }

    public void updateEntity() {
        super.updateEntity();
        if (!this.world.isRemote && random.nextInt(40) < this.countSurroundingWater()) {
            this.progress = (int)((double)this.progress + (double)this.progressEfficiency * Math.pow(Math.max(this.tier, 1.0D), 3.0D));
            if (this.progress >= progressMax) {
                this.progress -= progressMax;
                this.progressEfficiency -= random.nextInt(5) == 0 ? 1 : 0;
                this.emitEnergy();
            }
        }
    }

    public int getProgressIcon() {
        return this.progress * 10 / progressMax / 2 == 0 ? 0 : 1;
    }

    public double getProgress() {
        return (double)this.progress / (double)progressMax;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    public void emitEnergy() {
        EnumFacing[] var1 = EnumFacing.VALUES;

        for (EnumFacing direction : var1) {
            TileEntity te = this.world.getTileEntity(this.pos.offset(direction));
            if (te != null && te instanceof TileEntityClayiumMachine
                    && UtilTier.acceptWaterWheel(((TileEntityClayiumMachine) te).getTier())
                    && (double) ((TileEntityClayiumMachine) te).containEnergy < 5.0D * Math.pow(Math.max(this.tier, 1.0D), 8.0D)) {
                ((TileEntityClayiumMachine) te).containEnergy += (long) Math.pow(Math.max(this.tier, 1.0D), 8.0D);
            }
        }
    }

    public int countSurroundingWater() {
        int count = 0;

        for(int x = -1; x <= 1; ++x) {
            for(int y = -1; y <= 1; ++y) {
                for(int z = -1; z <= 1; ++z) {
                    IBlockState block = this.world.getBlockState(this.pos.add(x, y, z));
                    // TODO 拡張性
                    if (block.getMaterial() == Material.WATER && block.getBlock() instanceof BlockDynamicLiquid) {
                        count++;
                    }
                }
            }
        }

        return count;
    }
}
