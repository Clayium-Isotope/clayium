package mods.clayium.machine.ClayCentrifuge;

import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.common.ClayiumRecipeProvider;
import mods.clayium.machine.common.Machine1ToSome;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTransfer;
import mods.clayium.util.crafting.KitchenWithRecipe;

/* package-private */ final class KitchenCentrifuge extends KitchenWithRecipe<RecipeElement> {

    private final TileEntityClayCentrifuge centrifuge;

    /* package-private */ KitchenCentrifuge(TileEntityClayCentrifuge centrifuge, TierPrefix tier) {
        super(ClayiumRecipes.centrifuge, tier, RecipeElement.flat(), centrifuge);

        this.centrifuge = centrifuge;
    }

    @Override
    protected boolean canCraft() {
        if (this.currentRecipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStacks(this.currentRecipe.getResults(),
                this.centrifuge.getContainerItemStacks(),
                Machine1ToSome.PRODUCT_1, Machine1ToSome.PRODUCT_1 + this.centrifuge.getResultSlotCount(),
                this.centrifuge.getInventoryStackLimit());
    }

    @Override
    protected RecipeElement getRecipe() {
        return ClayiumRecipeProvider.getRecipe(this.recipes,
                e -> e.isCraftable(this.centrifuge.getStackInSlot(Machine1ToSome.MATERIAL), this.machineTier));
    }

    @Override
    protected void consumeMaterial() {
        UtilTransfer.consumeByIngredient(this.currentRecipe.getIngredients().get(0),
                this.centrifuge.getContainerItemStacks(), Machine1ToSome.MATERIAL);
    }

    @Override
    protected void produceResult() {
        ClayiumCore.logger.info("[KitchenCentrifuge] Produce Result");
        UtilTransfer.produceItemStacks(this.currentRecipe.getResults(), this.centrifuge.getContainerItemStacks(),
                Machine1ToSome.PRODUCT_1, Machine1ToSome.PRODUCT_1 + this.centrifuge.getResultSlotCount(),
                this.centrifuge.getInventoryStackLimit());
    }
}
