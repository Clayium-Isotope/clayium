package mods.clayium.plugin.nei;

import codechicken.nei.NEIClientConfig;
import codechicken.nei.recipe.ShapelessRecipeHandler;

import java.util.List;

import mods.clayium.util.crafting.ISpecialResultRecipe;
import mods.clayium.util.crafting.RecipeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class NEIShapelessSpecialResultRecipe extends ShapelessRecipeHandler {
    public void loadCraftingRecipes(ItemStack result) {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            if (irecipe instanceof ISpecialResultRecipe) {
                RecipeMap[] maps = ((ISpecialResultRecipe) irecipe).getCraftingRecipeMap(result);
                if (maps != null) {
                    for (RecipeMap map : maps) {
                        ShapelessRecipeHandler.CachedShapelessRecipe recipe = toShapelessRecipe(map);
                        if (recipe != null) {
                            this.arecipes.add(recipe);
                        }
                    }
                }
            }
        }
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            if (irecipe instanceof ISpecialResultRecipe) {
                RecipeMap[] maps = ((ISpecialResultRecipe) irecipe).getUsageRecipeMap(ingredient);
                if (maps != null) {
                    for (RecipeMap map : maps) {
                        ShapelessRecipeHandler.CachedShapelessRecipe recipe = toShapelessRecipe(map);
                        if (recipe != null) {
                            recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                            this.arecipes.add(recipe);
                        }
                    }
                }
            }
        }
    }

    public ShapelessRecipeHandler.CachedShapelessRecipe toShapelessRecipe(RecipeMap map) {
        if (map == null || !"shapeless".equals(map.getRecipeType()) || map.getResults() == null || (map.getResults()).length == 0)
            return null;
        try {
            return new ShapelessRecipeHandler.CachedShapelessRecipe(/* this,*/ map.getIngredients(), map.getResults()[0]);
        } catch (Exception e) {
            NEIClientConfig.logger.error("Error loading recipe: ", e);
            return null;
        }
    }
}
