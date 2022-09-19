package mods.clayium.machine.ClayChemicalReactor;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Arrays;
import java.util.List;

public class TileEntityClayChemicalReactor extends TileEntityClayiumMachine {
    enum ChemicalReactorSlots {
        MATERIAL_1,
        MATERIAL_2,
        PRODUCT_1,
        PRODUCT_2,
        REMINDER_1,
        REMINDER_2,
        ENERGY
    }

    protected int resultSlotNum = 2;

    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(ChemicalReactorSlots.values().length, ItemStack.EMPTY);
        this.clayEnergySlot = ChemicalReactorSlots.ENERGY.ordinal();
        this.listSlotsImport.add(new int[]{ ChemicalReactorSlots.MATERIAL_1.ordinal(), ChemicalReactorSlots.MATERIAL_2.ordinal() });
        this.listSlotsImport.add(new int[]{ ChemicalReactorSlots.MATERIAL_1.ordinal() });
        this.listSlotsImport.add(new int[]{ ChemicalReactorSlots.MATERIAL_2.ordinal() });
        this.listSlotsExport.add(new int[]{ ChemicalReactorSlots.PRODUCT_1.ordinal(), ChemicalReactorSlots.PRODUCT_2.ordinal() });
        this.listSlotsExport.add(new int[]{ ChemicalReactorSlots.PRODUCT_1.ordinal() });
        this.listSlotsExport.add(new int[]{ ChemicalReactorSlots.PRODUCT_2.ordinal() });
        this.maxAutoExtract = new int[]{-1, -1, -1, 1};
        this.maxAutoInsert = new int[]{-1, -1, -1};
        this.slotsDrop = new int[]{ ChemicalReactorSlots.MATERIAL_1.ordinal(), ChemicalReactorSlots.MATERIAL_2.ordinal(), ChemicalReactorSlots.PRODUCT_1.ordinal(), ChemicalReactorSlots.PRODUCT_2.ordinal(), ChemicalReactorSlots.ENERGY.ordinal() };
        this.autoInsert = true;
        this.autoExtract = true;
    }

    public ItemStack getStackInSlot(ChemicalReactorSlots slot) {
        return super.getStackInSlot(slot.ordinal());
    }

    protected boolean canCraft(List<ItemStack> materials) {
        if (materials.stream().anyMatch(ItemStack::isEmpty) || this.doingRecipe == RecipeElement.FLAT)
            return false;

        List<ItemStack> itemstacks = this.doingRecipe.getResult().getResults();
        if (itemstacks.stream().anyMatch(ItemStack::isEmpty))
            return false;

        for(int i = 0; i < Math.min(this.resultSlotNum, itemstacks.size()); ++i) {
            if (!this.getStackInSlot(i + 2).isEmpty() && !itemstacks.get(i).isEmpty()) {
                if (!UtilItemStack.areTypeEqual(this.getStackInSlot(i + 2), itemstacks.get(i))) {
                    return false;
                }

                int result = this.getStackInSlot(i + 2).getCount() + itemstacks.get(i).getCount();
                if (result > this.getInventoryStackLimit() || result > this.getStackInSlot(i + 2).getMaxStackSize()) {
                    return false;
                }
            }
        }

        return true;
    }

    protected int[] getCraftPermutation() {
        if (this.canCraft(Arrays.asList(this.getStackInSlot(ChemicalReactorSlots.MATERIAL_1), this.getStackInSlot(ChemicalReactorSlots.MATERIAL_2))))
            return new int[]{ 0, 1 };

        if (this.canCraft(Arrays.asList(this.getStackInSlot(ChemicalReactorSlots.MATERIAL_2), this.getStackInSlot(ChemicalReactorSlots.MATERIAL_1))))
            return new int[]{ 1, 0 };

        if (this.canCraft(Arrays.asList(this.getStackInSlot(ChemicalReactorSlots.MATERIAL_1))))
            return new int[]{ 0 };

        if (this.canCraft(Arrays.asList(this.getStackInSlot(ChemicalReactorSlots.MATERIAL_2))))
            return new int[]{ 1 };

        return null;
    }

    public boolean canProceedCraft() {
        return this.getCraftPermutation() != null;
    }

    public void proceedCraft() {
        ++this.craftTime;
//        this.isDoingWork = true;
        if (this.craftTime < this.timeToCraft) return;

        int[] perm = this.getCraftPermutation();
        if (perm == null) throw new RuntimeException("Invalid recipe reference : The permutation variable is null!");

        ItemStack[] itemstacks = new ItemStack[perm.length];
        for (int i = 0; i < perm.length; i++) {
            itemstacks[i] = this.getStackInSlot(perm[i]);
        }

        List<ItemStack> results = this.doingRecipe.getResult().getResults();
        int[] consumedStackSize = this.doingRecipe.getCondition().getStackSizes(itemstacks);

        int i;
        for(i = 0; i < Math.min(this.resultSlotNum, results.size()); ++i) {
            if (this.getStackInSlot(i + 2).isEmpty()) {
                this.setInventorySlotContents(i + 2, results.get(i).copy());
            } else if (UtilItemStack.areTypeEqual(this.getStackInSlot(i + 2), results.get(i))) {
                this.getStackInSlot(i + 2).grow(results.get(i).getCount());
            }
        }

        for(i = 0; i < perm.length; ++i) {
            this.getStackInSlot(i + 4).shrink(consumedStackSize[i]);
            if (this.getStackInSlot(i + 4).getCount() <= 0) {
                this.setInventorySlotContents(i + 4, ItemStack.EMPTY);
            }
        }
    }

    @Override
    protected boolean setNewRecipe() {
        this.craftTime = 0L;

        this.debtEnergy = 0L;
        this.timeToCraft = 0L;

        int[] perm = getCraftPermutation();
        if (perm == null) return false;

        ItemStack[] mats = new ItemStack[perm.length];
        for (int i = 0; i < perm.length; i++) {
            mats[i] = this.getStackInSlot(perm[i]);
        }

        this.doingRecipe = this.recipeCards.getRecipe(Arrays.asList(mats), this.tier);

        if (this.doingRecipe == RecipeElement.FLAT) return false;

        if (!compensateClayEnergy(this.doingRecipe.getResult().getEnergy())) {
            this.doingRecipe = RecipeElement.FLAT;
            return false;
        }

        this.debtEnergy = this.doingRecipe.getResult().getEnergy();
        this.timeToCraft = this.doingRecipe.getResult().getTime();

        int[] stackSizes = this.doingRecipe.getCondition().getStackSizes(getStackInSlot(perm[0]), getStackInSlot(perm[1]));
        for (int i = 0; i < perm.length; i++) {
            this.getStackInSlot(perm[i]).shrink(stackSizes[i]);
        }

        proceedCraft();

        return true;
    }
}
