package mods.clayium.util.crafting;

import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.crafting.IRecipeElement;
import mods.clayium.machine.crafting.RecipeList;
import mods.clayium.util.TierPrefix;
import net.minecraft.nbt.NBTTagCompound;

public abstract class KitchenWithRecipe<RecipeType extends IRecipeElement> extends Kitchen {

    protected final RecipeList<RecipeType> recipes;
    protected final RecipeType flat;
    protected final TierPrefix machineTier;

    protected RecipeType currentRecipe;

    public KitchenWithRecipe(RecipeList<RecipeType> recipes, TierPrefix tier, RecipeType flat, IClayEnergyConsumer ce) {
        super(ce);

        this.machineTier = tier;
        this.recipes = recipes;

        this.flat = flat;
        this.currentRecipe = flat;
    }

    // ===== Public Methods =====

    public final boolean isDoingWork() {
        return !this.currentRecipe.isFlat();
    }

    // ===== Internal Methods =====

    /**
     * @return レシピが設定されたときのみ true
     */
    @Override
    protected boolean setNewRecipe() {
        this.craftTime = 0;
        this.currentRecipe = this.getRecipe();
        if (this.currentRecipe.isFlat()) return false;

        this.debtEnergy = this.currentRecipe.getEnergy();
        this.timeToCraft = this.currentRecipe.getTime();
        if (this.canCraft() && this.canProceedCraft()) {
            // ClayiumCore.logger.info("[KitchenWithRecipe] Set New Recipe: true");
            this.consumeMaterial();
            return true;
        }

        this.currentRecipe = this.flat;
        this.timeToCraft = 0L;
        this.debtEnergy = 0L;
        return false;
    }

    @Override
    protected final void postWork() {
        ClayiumCore.logger.info("[KitchenWithRecipe] Post Work");

        this.produceResult();

        this.craftTime = 0L;
        this.debtEnergy = 0L;
        this.timeToCraft = 0L;
        this.currentRecipe = this.flat;
    }

    abstract protected RecipeType getRecipe();

    /**
     * レシピ実行が確定してから、アイテム消費などをおこなう
     */
    protected void consumeMaterial() {}

    /**
     * レシピパラメーターのリセット前に、アイテム生産などをおこなう
     */
    protected void produceResult() {}

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        super.deserializeNBT(compound);

        this.currentRecipe = this.recipes.getRecipe(compound.getInteger("RecipeHash"));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = super.serializeNBT();

        compound.setInteger("RecipeHash", this.currentRecipe.hashCode());

        return compound;
    }
}
