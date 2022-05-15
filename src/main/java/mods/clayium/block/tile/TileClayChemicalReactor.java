package mods.clayium.block.tile;

import mods.clayium.util.crafting.Recipes;
import net.minecraft.item.ItemStack;

public class TileClayChemicalReactor
        extends TileClayMachines {
    private Recipes recipe;
    protected int resultSlotNum = 2;


    public void refreshRecipe() {
        Recipes recipe = Recipes.GetRecipes(this.recipeId);
        if (recipe != null) this.recipe = recipe;

    }

    public void initParams() {
        this.containerItemStacks = new ItemStack[7];
        this.clayEnergySlot = 6;
        this.listSlotsInsert.add(new int[] {0});
        this.listSlotsInsert.add(new int[] {1});
        this.listSlotsInsert.add(new int[] {0, 1});
        this.listSlotsInsert.add(new int[] {6});
        this.listSlotsExtract.add(new int[] {2});
        this.listSlotsExtract.add(new int[] {3});
        this.listSlotsExtract.add(new int[] {2, 3});
        this.insertRoutes = new int[] {-1, 2, -1, 3, -1, -1};
        this.maxAutoExtract = new int[] {-1, -1, -1, 1};
        this.extractRoutes = new int[] {2, -1, -1, -1, -1, -1};
        this.maxAutoInsert = new int[] {-1, -1, -1};
        this.slotsDrop = new int[] {0, 1, 2, 3, 6};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    protected boolean canCraft(ItemStack[] materials) {
        int method = 0;
        if (materials == null || this.recipe == null) {
            return false;
        }
        ItemStack[] itemstacks = this.recipe.getResult(materials, method, this.baseTier);
        if (itemstacks == null) return false;
        for (int i = 0; i < Math.min(this.resultSlotNum, itemstacks.length); i++) {
            if (this.containerItemStacks[i + 2] != null && itemstacks[i] != null) {
                if (!this.containerItemStacks[i + 2].isItemEqual(itemstacks[i])) return false;
                int result = (this.containerItemStacks[i + 2]).stackSize + (itemstacks[i]).stackSize;
                if (result > getInventoryStackLimit() || result > this.containerItemStacks[i + 2].getMaxStackSize())
                    return false;
            }
        }
        return true;
    }

    protected int[] getCraftPermutation(ItemStack[] materials) {
        if (canCraft(materials)) return new int[] {0, 1};
        if (canCraft(new ItemStack[] {materials[1], materials[0]})) return new int[] {1, 0};
        if (canCraft(new ItemStack[] {materials[0]})) return new int[] {0};
        if (canCraft(new ItemStack[] {materials[1]})) return new int[] {1};
        return null;
    }


    public boolean canProceedCraft() {
        if (this.containerItemStacks[4] != null || this.containerItemStacks[5] != null) {
            ItemStack[] arrayOfItemStack = {this.containerItemStacks[4], this.containerItemStacks[5]};
            if (getCraftPermutation(arrayOfItemStack) != null) {
                return true;
            }
            return false;
        }
        ItemStack[] itemstacks = {this.containerItemStacks[0], this.containerItemStacks[1]};
        if (getCraftPermutation(itemstacks) != null) {
            return true;
        }
        return false;
    }


    public void proceedCraft() {
        int method = 0;


        if (this.containerItemStacks[4] == null && this.containerItemStacks[5] == null) {
            ItemStack[] mats = {this.containerItemStacks[0], this.containerItemStacks[1]};
            int[] perm = getCraftPermutation(mats);
            if (perm == null) {
                throw new RuntimeException("Invalid recipe reference : The permutation variable is null!");
            }
            ItemStack[] itemstacks = new ItemStack[perm.length];
            for (int i = 0; i < perm.length; i++) {
                itemstacks[i] = mats[perm[i]];
            }
            this.machineConsumingEnergy = (long) ((float) this.recipe.getEnergy(itemstacks, method, this.baseTier) * this.multConsumingEnergy);
        }
        if (consumeClayEnergy(this.machineConsumingEnergy)) {
            if (this.containerItemStacks[4] == null && this.containerItemStacks[5] == null) {
                ItemStack[] mats = {this.containerItemStacks[0], this.containerItemStacks[1]};
                int[] perm = getCraftPermutation(mats);
                if (perm == null) {
                    throw new RuntimeException("Invalid recipe reference : The permutation variable is null!");
                }
                ItemStack[] itemstacks = new ItemStack[perm.length];
                for (int i = 0; i < perm.length; i++) {
                    itemstacks[i] = mats[perm[i]];
                }
                this.machineTimeToCraft = (long) ((float) this.recipe.getTime(itemstacks, method, this.baseTier) * this.multCraftTime);
                int[] consumedStackSize = this.recipe.getConsumedStackSize(itemstacks, method, this.baseTier);
                for (int j = 0; j < perm.length; j++) {
                    this.containerItemStacks[j + 4] = this.containerItemStacks[perm[j]].splitStack(consumedStackSize[j]);
                    if ((this.containerItemStacks[perm[j]]).stackSize <= 0) this.containerItemStacks[perm[j]] = null;
                }
            }
            this.machineCraftTime++;
            this.isDoingWork = true;
            if (this.machineCraftTime >= this.machineTimeToCraft) {
                ItemStack[] mats = {this.containerItemStacks[4], this.containerItemStacks[5]};
                int[] perm = getCraftPermutation(mats);
                if (perm == null) {
                    throw new RuntimeException("Invalid recipe reference : The permutation variable is null!");
                }
                ItemStack[] itemstacks = new ItemStack[perm.length];
                for (int i = 0; i < perm.length; i++) {
                    itemstacks[i] = mats[perm[i]];
                }
                ItemStack[] results = this.recipe.getResult(itemstacks, method, this.baseTier);
                int[] consumedStackSize = this.recipe.getConsumedStackSize(itemstacks, method, this.baseTier);
                this.machineCraftTime = 0L;
                this.machineConsumingEnergy = 0L;
                int j;
                for (j = 0; j < Math.min(this.resultSlotNum, results.length); j++) {
                    if (this.containerItemStacks[j + 2] == null) {
                        this.containerItemStacks[j + 2] = results[j].copy();
                    } else if (this.containerItemStacks[j + 2].getItem() == results[j].getItem()) {
                        (this.containerItemStacks[j + 2]).stackSize += (results[j]).stackSize;
                    }
                }
                for (j = 0; j < perm.length; j++) {
                    if (((this.containerItemStacks[j + 4]).stackSize -= consumedStackSize[j]) <= 0)
                        this.containerItemStacks[j + 4] = null;

                }
                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;
                }
            }
        }
    }
}