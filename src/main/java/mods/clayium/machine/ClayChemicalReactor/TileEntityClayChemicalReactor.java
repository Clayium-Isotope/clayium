package mods.clayium.machine.ClayChemicalReactor;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.ClayiumRecipeProvider;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class TileEntityClayChemicalReactor extends TileEntityClayiumMachine {
    enum ChemicalReactorSlots {
        MATERIAL_1,
        MATERIAL_2,
        PRODUCT_1,
        PRODUCT_2,
        ENERGY
    }

    protected int resultSlotNum = 2;

    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(ChemicalReactorSlots.values().length, ItemStack.EMPTY);

        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, ENERGY_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
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

    @Override
    public boolean canCraft(List<ItemStack> materials) {
        if (materials.stream().anyMatch(ItemStack::isEmpty))
            return false;

        return this.canCraft(this.getRecipe(materials));
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStacks(recipe.getResults(), this.getContainerItemStacks(),
                ChemicalReactorSlots.PRODUCT_1.ordinal(), ChemicalReactorSlots.PRODUCT_1.ordinal() + this.resultSlotNum, this.getInventoryStackLimit());
    }

    @Override
    public boolean canProceedCraft() {
        return IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy, false);

//        return IRecipeProvider.getCraftPermutation(this, this.getStackInSlot(ChemicalReactorSlots.MATERIAL_1), this.getStackInSlot(ChemicalReactorSlots.MATERIAL_2)) != null;
    }

    @Override
    public void proceedCraft() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) return;

        ++this.craftTime;
        if (this.craftTime < this.timeToCraft) return;

        UtilTransfer.produceItemStacks(UtilTransfer.getHardCopy(this.doingRecipe.getResults()), this.getContainerItemStacks(),
                ChemicalReactorSlots.PRODUCT_1.ordinal(), ChemicalReactorSlots.PRODUCT_1.ordinal() + this.resultSlotNum,
                this.getInventoryStackLimit());

        this.craftTime = 0L;
        this.debtEnergy = 0L;
        this.timeToCraft = 0L;
        this.doingRecipe = this.getFlat();
    }

    @Override
    public boolean setNewRecipe() {
        this.doingRecipe = ClayiumRecipeProvider.getCraftPermRecipe(this, this.getStackInSlot(ChemicalReactorSlots.MATERIAL_1), this.getStackInSlot(ChemicalReactorSlots.MATERIAL_2));
        if (this.doingRecipe.isFlat()) return false;

        this.debtEnergy = this.doingRecipe.getEnergy();
        this.timeToCraft = this.doingRecipe.getTime();

        UtilTransfer.consumeByIngredient(this.doingRecipe.getIngredients(), this.getContainerItemStacks(), ChemicalReactorSlots.MATERIAL_1.ordinal(), ChemicalReactorSlots.MATERIAL_2.ordinal() + 1);

        return true;
    }

    @Override
    public int getEnergySlot() {
        return ChemicalReactorSlots.ENERGY.ordinal();
    }
}
