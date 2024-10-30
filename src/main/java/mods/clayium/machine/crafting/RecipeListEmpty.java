package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeListEmpty extends RecipeListGeneral {
    public RecipeListEmpty() {
        super("EMPTY");
    }

    @Override
    public RecipeElement makeRecipe(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        throw new UnsupportedOperationException("EMPTY recipe list cannot make new recipe.");
    }

    @Override
    public RecipeElement makeRecipe(ItemStack materialIn, int tier, ItemStack resultIn, long energy, long time) {
        throw new UnsupportedOperationException("EMPTY recipe list cannot make new recipe.");
    }
}
