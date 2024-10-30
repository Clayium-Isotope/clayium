package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeListGeneral extends RecipeList<RecipeElement> {
    // private static final Map<String, ClayiumRecipe> id2Recipe = new HashMap<>();

    public RecipeListGeneral(String recipeId) {
        super(recipeId, RecipeElement.flat());
        // id2Recipe.put(recipeId, this);
    }

    @Override
    public RecipeElement makeRecipe(ItemStack materialIn, int tier, ItemStack resultIn, long energy, long time) {
        return new RecipeElement(materialIn, tier, resultIn, energy, time);
    }

    @Override
    public RecipeElement makeRecipe(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        return new RecipeElement(materialIn, tier, resultIn, energy, time);
    }
}
