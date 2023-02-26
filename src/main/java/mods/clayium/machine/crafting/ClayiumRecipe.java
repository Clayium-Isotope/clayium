package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ClayiumRecipe extends ArrayList<IRecipeElement> {
    private static final Map<String, ClayiumRecipe> id2Recipe = new HashMap<>();

    public ClayiumRecipe(String recipeId) {
        super();
        id2Recipe.put(recipeId, this);
    }

    public void addRecipe(ItemStack materialIn, ItemStack resultIn, long time) {
        addRecipe(materialIn, 0, resultIn, 1L, time);
    }

    public void addRecipe(List<ItemStack> materialIn, List<ItemStack> resultIn, long time) {
        addRecipe(materialIn, 0, resultIn, 1L, time);
    }

    public void addRecipe(ItemStack materialIn, ItemStack resultIn, long energy, long time) {
        addRecipe(materialIn, 0, resultIn, energy, time);
    }

    public void addRecipe(List<ItemStack> materialIn, List<ItemStack> resultIn, long energy, long time) {
        addRecipe(materialIn, 0, resultIn, energy, time);
    }

    public void addRecipe(ItemStack materialIn, int tier, ItemStack resultIn, long energy, long time) {
        this.add(new RecipeElement(materialIn, tier, resultIn, energy, time));
    }

    public void addRecipe(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        this.add(new RecipeElement(materialIn, tier, resultIn, energy, time));
    }

    @SuppressWarnings("unchecked")
    public <T extends IRecipeElement> T getRecipe(Predicate<T> matcher, T flat) {
        for (IRecipeElement element : this) {
            if (matcher.test((T) element)) return (T) element;
        }

        return flat;
    }

    public <T extends IRecipeElement> T getRecipe(int hash, T flat) {
        return this.getRecipe(e -> e.hashCode() == hash, flat);
    }
}
