package mods.clayium.machine.WaterWheel;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.TileEntityGeneric;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTier;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityWaterWheel extends TileEntityGeneric {

    public int progress = 0;
    public int progressEfficiency = 1000;
    protected static int progressMax = 20000;
    private TierPrefix tier;

    public void initParams() {
        this.slotsDrop = new int[] { 0 };
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        this.tier = tier;
        this.progressEfficiency = (int) ((double) this.progressEfficiency *
                Math.pow(Math.max(this.tier.meta(), 1.0D), 3.0D));
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);
        this.progress = tagCompound.getInteger("Progress");
        this.progressEfficiency = tagCompound.getInteger("ProgressEfficiency");
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);
        tagCompound.setInteger("Progress", this.progress);
        tagCompound.setInteger("ProgressEfficiency", this.progressEfficiency);
        return tagCompound;
    }

    public void update() {
        if (this.world.isRemote || TileEntityGeneric.random.nextInt(40) >= this.countSurroundingWater()) {
            return;
        }

        this.progress = (int) ((double) this.progress +
                (double) this.progressEfficiency * Math.pow(Math.max(this.tier.meta(), 1.0D), 3.0D));
        if (this.progress < progressMax) {
            return;
        }

        this.progress -= progressMax;
        this.progressEfficiency -= TileEntityGeneric.random.nextInt(5) == 0 ? 1 : 0;
        this.emitEnergy();
    }

    public int getProgressIcon() {
        return this.progress * 10 / progressMax / 2 == 0 ? 0 : 1;
    }

    public double getProgress() {
        return (double) this.progress / (double) progressMax;
    }

    public void emitEnergy() {
        for (EnumFacing direction : EnumFacing.VALUES) {
            TileEntity te = this.world.getTileEntity(this.pos.offset(direction));
            if (te instanceof TileEntityClayiumMachine &&
                    UtilTier.acceptWaterWheel(((TileEntityClayiumMachine) te).getHullTier()) &&
                    !((TileEntityClayiumMachine) te).containEnergy().hasEnough(5.0D * Math.pow(Math.max(this.tier.meta(), 1.0D), 8.0D))) {
                ((TileEntityClayiumMachine) te).containEnergy()
                        .add((long) Math.pow(Math.max(this.tier.meta(), 1.0D), 8.0D));
                te.markDirty();
            }
        }
    }

    public int countSurroundingWater() {
        int count = 0;

        for (BlockPos pos : BlockPos.getAllInBox(-1, -1, -1, 1, 1, 1)) {
            IBlockState state = this.getWorld().getBlockState(this.pos.add(pos));

            // TODO 拡張性
            if (state.getMaterial() == Material.WATER && state.getBlock() instanceof BlockDynamicLiquid) {
                count++;
            }
        }

        return count;
    }
}
