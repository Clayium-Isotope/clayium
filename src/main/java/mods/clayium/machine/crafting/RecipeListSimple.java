package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeListSimple extends RecipeListGeneral {
    public RecipeListSimple(String id) {
        super(id);
    }

    @Override
    public RecipeElement makeRecipe(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        throw new UnsupportedOperationException("Simple Recipe is unary operator of ItemStack. Tried: on " + this.recipeId + ", " + materialIn + ", " + resultIn);
    }
}
