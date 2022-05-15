package mods.clayium.block.tile;

import net.minecraft.item.ItemStack;

public class TileCACondenser extends TileCAMachines {
    public void proceedCraft() {
        if (this.containerItemStacks[2] == null) {
            this.machineConsumingEnergy = (long) ((float) this.recipe.getEnergy(this.containerItemStacks[0], this.baseTier) * this.multConsumingEnergy);
        }
        if (consumeClayEnergy(this.machineConsumingEnergy)) {
            if (this.containerItemStacks[2] == null) {
                this.machineTimeToCraft = (long) ((float) this.recipe.getTime(this.containerItemStacks[0], this.baseTier) * this.multCraftTime);
                this.containerItemStacks[2] = this.containerItemStacks[0].splitStack(this.recipe.getConsumedStackSize(this.containerItemStacks[0], this.baseTier));
                if ((this.containerItemStacks[0]).stackSize <= 0) this.containerItemStacks[0] = null;
            }
            this.machineCraftTime++;
            this.isDoingWork = true;
            if (this.machineCraftTime >= this.machineTimeToCraft) {
                ItemStack itemstack = this.recipe.getResult(this.containerItemStacks[2], this.baseTier).copy();
                itemstack.stackSize = (int) (itemstack.stackSize * (Math.log(getResonance()) + 1.0D));
                this.machineCraftTime = 0L;
                this.machineConsumingEnergy = 0L;
                if (this.containerItemStacks[1] == null) {
                    this.containerItemStacks[1] = itemstack.copy();
                } else if (this.containerItemStacks[1].getItem() == itemstack.getItem()) {
                    (this.containerItemStacks[1]).stackSize += itemstack.stackSize;
                }
                if ((this.containerItemStacks[1]).stackSize >= this.containerItemStacks[1].getMaxStackSize()) {
                    (this.containerItemStacks[1]).stackSize = this.containerItemStacks[1].getMaxStackSize();
                }
                if (((this.containerItemStacks[2]).stackSize -= this.recipe.getConsumedStackSize(this.containerItemStacks[2], this.baseTier)) <= 0)
                    this.containerItemStacks[2] = null;

                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;
                }
            }
        }
    }
}
