package mods.clayium.plugin.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mods.clayium.machine.ClayWorkTable.ContainerClayWorkTable;
import mods.clayium.machine.ClayWorkTable.GuiClayWorkTable;
import mods.clayium.machine.crafting.ClayiumRecipes;

@JEIPlugin
public class JEICompatibility implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helper = registry.getJeiHelpers();
        final IGuiHelper gui = helper.getGuiHelper();

        registry.addRecipeCategories(new ClayWorkTableCategory(gui));
        IModPlugin.super.registerCategories(registry);
    }

    @Override
    public void register(IModRegistry registry) {
        final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

        registry.addRecipes(ClayiumRecipes.clayWorkTable, ClayWorkTableCategory.categoryID);
        registry.addRecipeClickArea(GuiClayWorkTable.class, 48, 33, 80, 12, ClayWorkTableCategory.categoryID);
        recipeTransfer.addRecipeTransferHandler(ContainerClayWorkTable.class, ClayWorkTableCategory.categoryID, 0, 2, 4, 36);
    }
}
