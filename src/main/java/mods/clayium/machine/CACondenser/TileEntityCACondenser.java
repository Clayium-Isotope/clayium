package mods.clayium.machine.CACondenser;

import mods.clayium.block.ICAMachine;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.CAMachine.TileEntityCAMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine1To1;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import static mods.clayium.machine.crafting.ClayiumRecipes.e;

public class TileEntityCACondenser extends TileEntityCAMachine implements Machine1To1 {
    protected static final RecipeElement RECIPE = new RecipeElement(new ItemStack(ClayiumItems.antimatterSeed), 9,
            ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), e(2.5D, 9), ClayiumCore.divideByProgressionRate(2000L));

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);

        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, ENERGY_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.listSlotsImport.add(new int[] { Machine1To1.MATERIAL });
        this.listSlotsExport.add(new int[] { Machine1To1.PRODUCT });

        this.maxAutoExtract = new int[] { -1, 1 };
        this.maxAutoInsert = new int[] { -1 };

        this.autoInsert = true;
        this.autoExtract = true;

        this.slotsDrop = new int[] { Machine1To1.MATERIAL, Machine1To1.PRODUCT, this.getEnergySlot() };
    }

    @Override
    public int getEnergySlot() {
        return 2;
    }

    @Override
    public boolean isDoingWork() {
        return this.craftTime > 0;
    }

    @Override
    public RecipeElement getFlat() {
        return RecipeElement.flat();
    }

    @Override
    public void proceedCraft() {
        ++this.craftTime;
        if (this.craftTime < this.timeToCraft) {
            return;
        }

        ItemStack itemstack = RECIPE.getResults().get(0).copy();
        itemstack.setCount((int) (itemstack.getCount() * Math.log(ICAMachine.getResonance(this.getWorld(), this.getPos())) + 1.0d));
        this.craftTime = 0L;
        this.timeToCraft = 0L;
        this.debtEnergy = 0L;

        UtilTransfer.produceItemStack(itemstack, this.containerItemStacks, 1, this.getInventoryStackLimit());

//        if (this.externalControlState > 0) {
//            --this.externalControlState;
//            if (this.externalControlState == 0) {
//                this.externalControlState = -1;
//            }
//        }
    }

    @Override
    public boolean setNewRecipe() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, RECIPE.getEnergy(), false)
                || UtilTransfer.consumeByIngredient(RECIPE.getIngredients().get(0), this.containerItemStacks, 0) != 0) {
            this.debtEnergy = 0;
            this.timeToCraft = 0L;
            this.craftTime = 0;

            return false;
        }

        this.debtEnergy = (long) (RECIPE.getEnergy() * this.multConsumingEnergy);
        this.timeToCraft = (long)((float) RECIPE.getTime() * this.multCraftTime);
        this.craftTime = 1;
        UtilTransfer.consumeByIngredient(RECIPE.getIngredients().get(0), this.containerItemStacks, 0);
        return true;
    }

    @Override
    public ClayiumRecipe getRecipeCard() {
        return ClayiumRecipes.EMPTY;
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        return RECIPE.equals(recipe);
    }

    @Override
    public boolean canProceedCraft() {
        return IClayEnergyConsumer.compensateClayEnergy(this, RECIPE.getEnergy(), false);
    }

    @Override
    public EnumMachineKind getKind() {
        return EnumMachineKind.CACondenser;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) return true;
        if (index == 1) return false;
        return super.isItemValidForSlot(index, stack);
    }
}
