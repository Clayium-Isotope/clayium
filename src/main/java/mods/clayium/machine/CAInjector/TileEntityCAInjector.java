package mods.clayium.machine.CAInjector;

import mods.clayium.machine.CAMachine.TileEntityCAMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.ClayiumRecipeProvider;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine2To1;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.IllegalTierException;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class TileEntityCAInjector extends TileEntityCAMachine implements Machine2To1 {
    protected double caFactorExponent = 1.0;

    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
        this.listSlotsImport.clear();
        this.listSlotsExport.clear();
        this.listSlotsImport.add(new int[]{0});
        this.listSlotsImport.add(new int[]{1});
        this.listSlotsImport.add(new int[]{0, 1});
        this.listSlotsExport.add(new int[]{2});
        this.setImportRoutes(NONE_ROUTE, 2, NONE_ROUTE, ENERGY_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.maxAutoExtract = new int[]{-1, -1, -1, 1};
        this.setExportRoutes(0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.maxAutoInsert = new int[]{-1};
        this.slotsDrop = new int[]{0, 1, 2, this.getEnergySlot()};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    @Override
    public int getEnergySlot() {
        return 3;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        super.initParamsByTier(tier);
        switch (tier) {
            case ultimate:
                this.caFactorExponent = 0.2;
                break;
            case antimatter:
                this.caFactorExponent = 0.9;
                break;
            case pureAntimatter:
                this.caFactorExponent = 3.0;
            default:
                throw new IllegalTierException();
        }
    }

    @Nonnull
    @Override
    public ClayiumRecipe getRecipeCard() {
        return ClayiumRecipes.CAInjector;
    }

    @Nonnull
    @Override
    public RecipeElement getFlat() {
        return RecipeElement.flat();
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStack(recipe.getResults().get(0), this.getContainerItemStacks(), 2, this.getInventoryStackLimit()) > 0;
    }

    public double getCraftTimeMultiplier() {
        return Math.pow(this.resonanceHandler.getResonance(), -this.caFactorExponent);
    }

    @Override
    public boolean isDoingWork() {
        return !this.doingRecipe.isFlat();
    }

    @Override
    public boolean canProceedCraft() {
        return IClayEnergyConsumer.compensateClayEnergy(this, this.doingRecipe.getEnergy(), false);
    }

    @Override
    public void proceedCraft() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) {
            return;
        }

        ++this.craftTime;
        if (this.craftTime < this.timeToCraft) {
            return;
        }

        ItemStack result = this.doingRecipe.getResults().get(0);
        UtilTransfer.produceItemStack(result, this.getContainerItemStacks(), 2, this.getInventoryStackLimit());

        this.timeToCraft = 0;
        this.craftTime = 0;
        this.debtEnergy = 0;
        this.doingRecipe = this.getFlat();

//        if (this.externalControlState > 0) {
//            --this.externalControlState;
//            if (this.externalControlState == 0) {
//                this.externalControlState = -1;
//            }
//        }

    }

    @Override
    public boolean setNewRecipe() {
        this.doingRecipe = ClayiumRecipeProvider.getCraftPermRecipe(this, this.getStackInSlot(0), this.getStackInSlot(1));
//        ClayiumCore.logger.info("[CA Injector] Set recipe to [" + this.getStackInSlot(0) + ", " + this.getStackInSlot(1) + "] -> " + this.doingRecipe.getResults());
        if (this.doingRecipe.isFlat()) return false;

        this.debtEnergy = (long) (this.doingRecipe.getEnergy() * this.multConsumingEnergy);
        if (!this.canProceedCraft()) {
            this.debtEnergy = 0;
            this.doingRecipe = this.getFlat();
            this.timeToCraft = 0;
            this.craftTime = 0;
            return false;
        }

        UtilTransfer.consumeByIngredient(this.doingRecipe.getIngredients(), this.getContainerItemStacks(), 0, 2);
        this.timeToCraft = (long) (this.doingRecipe.getTime() * this.getCraftTimeMultiplier() * (double) this.multCraftTime);
        this.craftTime = 1;
        return true;
    }

    @Override
    public EnumMachineKind getKind() {
        return EnumMachineKind.CAInjector;
    }
}
