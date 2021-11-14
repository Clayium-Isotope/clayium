package mods.clayium.block.tile;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;

public class TileClayFabricator
        extends TileSolarClayFabricator {
    public float exponentOfNumber;

    public void initParams() {
        super.initParams();
        this.exponentOfNumber = 0.8F;
        this.insertRoutes = new int[] {-1, 0, -1, -1, -1, -1};
        this.extractRoutes = new int[] {0, -1, -1, -1, -1, -1};
        this.slotsDrop = new int[] {0, 1};
    }


    public void initParamsByTier(int tier) {
        this.initCraftTime = 0.01F;
        if (tier >= 8) {
            this.acceptableTier = 11;
            this.baseCraftTime = 5.0F;
            this.exponentOfNumber = 0.85F;
            this
                    .initCraftTime = (float) (Math.pow(10.0D, this.acceptableTier) * 64.0D / Math.pow(this.baseCraftTime, this.acceptableTier) * Math.pow(64.0D, this.exponentOfNumber) / (ClayiumCore.multiplyProgressionRateF(4.5E7F) / 20.0F));
        }
        if (tier >= 9) {
            this.acceptableTier = 13;
            this.baseCraftTime = 2.0F;
            this.exponentOfNumber = 0.3F;
            this
                    .initCraftTime = (float) (Math.pow(10.0D, this.acceptableTier) * 64.0D / Math.pow(this.baseCraftTime, this.acceptableTier) * Math.pow(64.0D, this.exponentOfNumber) / (ClayiumCore.multiplyProgressionRateF(1.0E9F) / 20.0F));
        }
        if (tier >= 13) {
            this.acceptableTier = 13;
            this.baseCraftTime = 1.3F;
            this.exponentOfNumber = 0.06F;
            this
                    .initCraftTime = (float) (Math.pow(10.0D, this.acceptableTier) * 64.0D / Math.pow(this.baseCraftTime, this.acceptableTier) * Math.pow(64.0D, this.exponentOfNumber) / (ClayiumCore.multiplyProgressionRateF(1.0E12F) / 20.0F));
        }
    }


    protected boolean canCraft(int tier, int size) {
        return (tier >= 0 && tier <= this.acceptableTier) ? ((UtilTransfer.canProduceItemStack(getCompressedClay(tier, size), this.containerItemStacks, 1, 2, getInventoryStackLimit()) >= 1)) : false;
    }


    public boolean canProceedCraft() {
        return (this.containerItemStacks[2] == null) ? ((this.containerItemStacks[0] == null) ? false : canCraft(getTierOfCompressedClay(this.containerItemStacks[0], false), (this.containerItemStacks[0]).stackSize)) :
                canCraft(getTierOfCompressedClay(this.containerItemStacks[2], false), (this.containerItemStacks[2]).stackSize);
    }


    public void proceedCraft() {
        if (this.containerItemStacks[2] == null) {
            this
                    .machineTimeToCraft = (long) (Math.pow(this.baseCraftTime, getTierOfCompressedClay(this.containerItemStacks[0], false)) * Math.pow((this.containerItemStacks[0]).stackSize, this.exponentOfNumber) * this.multCraftTime);
            this.containerItemStacks[2] = this.containerItemStacks[0].copy();
        }
        this.machineCraftTime++;
        this.isDoingWork = true;
        this.clayEnergy = (long) (Math.pow(10.0D, getTierOfCompressedClay(this.containerItemStacks[2], false)) * (this.containerItemStacks[2]).stackSize * this.machineCraftTime / this.machineTimeToCraft);
        if (this.machineCraftTime >= this.machineTimeToCraft) {
            this.clayEnergy = 0L;
            ItemStack result = getCompressedClay(getTierOfCompressedClay(this.containerItemStacks[2], false));
            result.stackSize = (this.containerItemStacks[2]).stackSize;
            this.containerItemStacks[2] = null;
            UtilTransfer.produceItemStack(result, this.containerItemStacks, 1, 2, getInventoryStackLimit());
            this.machineCraftTime = 0L;

            if (this.externalControlState > 0) {
                this.externalControlState--;
                if (this.externalControlState == 0) this.externalControlState = -1;

            }
        }
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return (slot == 0) ? ((getTierOfCompressedClay(itemstack, false) >= 0 && getTierOfCompressedClay(itemstack, false) <= this.acceptableTier)) : true;
    }
}
