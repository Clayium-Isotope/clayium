package mods.clayium.block.tile;

import mods.clayium.block.CBlocks;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import mods.clayium.util.crafting.SimpleMachinesRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class TileAutoClayCondenser
        extends TileClayMachines {
    public void initParams() {
        this.containerItemStacks = new ItemStack[22];
        this.clayEnergySlot = -1;
        this.listSlotsInsert.add(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14});
        this.listSlotsExtract.add(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});

        this.insertRoutes = new int[] {-1, 0, -1, -1, -1, -1};
        this.extractRoutes = new int[] {0, -1, -1, -1, -1, -1};
        this.slotsDrop = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        this.autoInsert = true;
        this.autoExtract = true;
        this.recipeId = "";
    }

    public void initParamsByTier(int tier) {
        setDefaultTransportationParamsByTier(tier, ParamMode.MACHINE);
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
    }

    public void setRecipe(String recipeId_) {}

    public void refreshRecipe() {}

    public SimpleMachinesRecipes getRecipe() {
        return null;
    }

    public void sortInventory() {
        int[] num = getQuantityOfClay();
        int i;
        for (i = 0; i < 20; i++) {
            this.containerItemStacks[i] = null;
        }
        i = 0;
        int j = 0;
        while (j < 17 && i < 20) {
            if (num[j] == 0) {
                j++;
                continue;
            }
            int maxSize = Math.min(64, getInventoryStackLimit());
            int size = Math.min(maxSize, num[j]);
            num[j] = num[j] - size;
            this.containerItemStacks[i] = getCompressedClay(j, size);
            i++;
        }
        setSyncFlag();
    }

    public int[] getQuantityOfClay() {
        int[] num = new int[17];
        for (int i = 0; i < 17; i++) {
            num[i] = UtilTransfer.countItemStack(getCompressedClay(i), this.containerItemStacks, 0, 20);
        }
        return num;
    }

    protected boolean canCraft(int tier) {
        return (UtilTransfer.canProduceItemStack(getCompressedClay(tier), this.containerItemStacks, 0, 20, getInventoryStackLimit()) >= 1);
    }


    public void updateEntity() {
        super.updateEntity();
        if (this.baseTier >= 7) {
            while (this.externalControlState >= 0 && canProceedCraft()) {
                proceedCraft();
            }
        }
        this.clayEnergy = 0L;
        int[] num = getQuantityOfClay();
        for (int i = 0; i < num.length; i++) {
            this.clayEnergy += (long) Math.pow(10.0D, i) * num[i];
        }
    }


    public boolean canProceedCraft() {
        if (this.containerItemStacks[20] != null) {
            if (canCraft(getTierOfCompressedClay(this.containerItemStacks[20]))) {
                return true;
            }
            return false;
        }
        return (getConsumedClay() != -1);
    }

    public int getConsumedClay() {
        int[] num = getQuantityOfClay();
        int max = (this.containerItemStacks[21] == null || getTierOfCompressedClay(this.containerItemStacks[21]) == -1) ? 13 : getTierOfCompressedClay(this.containerItemStacks[21]);
        for (int j = max - 1; j >= 0; j--) {
            if (num[j] >= 9 && canCraft(j)) {
                return j;
            }
        }
        return -1;
    }


    public void proceedCraft() {
        if (this.containerItemStacks[20] == null) {
            this.machineConsumingEnergy = (long) (0.0F * this.multConsumingEnergy);
        }
        if (consumeClayEnergy(this.machineConsumingEnergy)) {
            if (this.containerItemStacks[20] == null) {
                this.machineTimeToCraft = (long) (1.0F * this.multCraftTime);
                int tier = getConsumedClay();
                UtilTransfer.consumeItemStack(getCompressedClay(tier, 9), this.containerItemStacks, 0, 20);
                this.containerItemStacks[20] = getCompressedClay(tier, 9);
            }
            this.machineCraftTime++;
            this.isDoingWork = true;
            if (this.machineCraftTime >= this.machineTimeToCraft) {
                ItemStack result = getCompressedClay(getTierOfCompressedClay(this.containerItemStacks[20]) + 1);
                this.containerItemStacks[20] = null;
                UtilTransfer.produceItemStack(result, this.containerItemStacks, 0, 20, getInventoryStackLimit());
                sortInventory();
                this.machineCraftTime = 0L;

                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;
                }
            }
        }
    }

    public static ItemStack getCompressedClay(int tier, int size) {
        return (tier == 0) ? new ItemStack(Blocks.clay, size) : new ItemStack(CBlocks.blockCompressedClay, size, tier - 1);
    }

    public static ItemStack getCompressedClay(int tier) {
        return getCompressedClay(tier, 1);
    }

    public static int getTierOfCompressedClay(ItemStack itemstack) {
        if (itemstack == null) return -1;
        for (int i = 0; i < 17; i++) {
            if (UtilItemStack.areItemDamageEqual(getCompressedClay(i), itemstack)) {
                return i;
            }
        }
        return -1;
    }


    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        return (super.canExtractItem(slot, itemstack, side) &&
                getTierOfCompressedClay(itemstack) >= getTierOfCompressedClay(this.containerItemStacks[21]));
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return (i == 20) ? false : ((getTierOfCompressedClay(itemstack) != -1));
    }
}
