package mods.clayium.machine.ClayAssembler;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityClayAssembler extends TileEntityClayiumMachine {
    enum AssemblerSlots {
        MATERIAL_1,
        MATERIAL_2,
        PRODUCT,
        ENERGY
    }

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(AssemblerSlots.values().length, ItemStack.EMPTY);
        this.clayEnergySlot = AssemblerSlots.ENERGY.ordinal();

        this.listSlotsImport.add(new int[]{ AssemblerSlots.MATERIAL_1.ordinal(), AssemblerSlots.MATERIAL_2.ordinal() });
        this.listSlotsImport.add(new int[]{ AssemblerSlots.MATERIAL_1.ordinal() });
        this.listSlotsImport.add(new int[]{ AssemblerSlots.MATERIAL_2.ordinal() });
        this.listSlotsImport.add(new int[]{ AssemblerSlots.ENERGY.ordinal() });
        this.listSlotsExport.add(new int[]{ AssemblerSlots.PRODUCT.ordinal() });
        this.maxAutoExtract = new int[] {-1, -1, -1, 1};
        this.maxAutoInsert = new int[] {-1};

        this.slotsDrop = new int[]{ AssemblerSlots.MATERIAL_1.ordinal(), AssemblerSlots.MATERIAL_2.ordinal(), AssemblerSlots.PRODUCT.ordinal(), AssemblerSlots.ENERGY.ordinal() };
        this.autoInsert = true;
        this.autoExtract = true;
    }

    @Override
    public void initByTier(int tier) {
        this.tier = tier;
    }

    public ItemStack getStackInSlot(AssemblerSlots index) {
        return this.containerItemStacks.get(index.ordinal());
    }

    protected boolean canCraft(List<ItemStack> materials) {
        if (this.doingRecipe != RecipeElement.FLAT) return true;

        if (materials.isEmpty()) return false;

        RecipeElement recipe = this.recipeCards.getRecipe(materials, this.tier);
        if (recipe == RecipeElement.FLAT) {
            return false;
        }

        if (this.getStackInSlot(AssemblerSlots.PRODUCT).isEmpty()) {
            return true;
        } else if (UtilItemStack.areTypeEqual(this.getStackInSlot(AssemblerSlots.PRODUCT), recipe.getResult().getResults().get(0))){
            return true;
        } else {
            int result = this.getStackInSlot(AssemblerSlots.PRODUCT).getCount() + recipe.getResult().getResults().get(0).getCount();
            return result <= this.getInventoryStackLimit() && result <= this.getStackInSlot(AssemblerSlots.PRODUCT).getMaxStackSize();
        }
    }

    protected int[] getCraftPermutation() {
        if (this.canCraft(Arrays.asList(this.getStackInSlot(AssemblerSlots.MATERIAL_1), this.getStackInSlot(AssemblerSlots.MATERIAL_2))))
            return new int[] { 0, 1 };

        if (this.canCraft(Arrays.asList(this.getStackInSlot(AssemblerSlots.MATERIAL_2), this.getStackInSlot(AssemblerSlots.MATERIAL_1))))
            return new int[] { 1, 0 };

        if (this.canCraft(Arrays.asList(this.getStackInSlot(AssemblerSlots.MATERIAL_1))))
            return new int[] { 0 };

        if (this.canCraft(Arrays.asList(this.getStackInSlot(AssemblerSlots.MATERIAL_2))))
            return new int[] { 1 };

        return null;
    }

    public boolean canProceedCraft() {
        return this.getCraftPermutation() != null;
    }

    public void proceedCraft() {
        ++this.craftTime;
//            this.isDoingWork = true;
        if (this.craftTime < this.timeToCraft) return;

        ItemStack result = this.doingRecipe.getResult().getResults().get(0);
        this.craftTime = 0L;
        if (this.getStackInSlot(AssemblerSlots.PRODUCT).isEmpty()) {
            this.setInventorySlotContents(AssemblerSlots.PRODUCT.ordinal(), result.copy());
        } else if (UtilItemStack.areTypeEqual(this.getStackInSlot(AssemblerSlots.PRODUCT), result)) {
            this.getStackInSlot(AssemblerSlots.PRODUCT).grow(result.getCount());
        }

        setNewRecipe();
    }

    @Override
    protected boolean setNewRecipe() {
        this.craftTime = 0L;

        this.debtEnergy = 0L;
        this.timeToCraft = 0L;

        int[] perm = getCraftPermutation();
        if (perm == null) return false;

        List<ItemStack> mats = new ArrayList<>(2);
        for (int i : perm) {
            mats.add(this.getStackInSlot(i));
        }

        this.doingRecipe = this.recipeCards.getRecipe(mats, this.tier);

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
