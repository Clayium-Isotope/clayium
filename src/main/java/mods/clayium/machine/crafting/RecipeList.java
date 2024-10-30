package mods.clayium.machine.crafting;

import mods.clayium.util.TierPrefix;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class RecipeList<RecipeType extends IRecipeElement> extends ArrayList<RecipeType> {
    protected final String recipeId;
    protected final RecipeType flat;

    public RecipeList(String recipeId, RecipeType flat) {
        this.recipeId = recipeId;
        this.flat = flat;
    }

    public RecipeType getFlat() {
        return this.flat;
    }

    public abstract RecipeType makeRecipe(ItemStack materialIn, int tier, ItemStack resultIn, long energy, long time);
    public abstract RecipeType makeRecipe(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time);

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
        this.add(this.makeRecipe(materialIn, tier, resultIn, energy, time));
    }

    public void addRecipe(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        this.add(this.makeRecipe(materialIn, tier, resultIn, energy, time));
    }

    public RecipeType getRecipe(Predicate<RecipeType> matcher) {
        for (RecipeType element : this) {
            if (matcher.test(element)) return element;
        }

        return this.flat;
    }

    public RecipeType getRecipe(int hash) {
        return this.getRecipe(e -> e.hashCode() == hash);
    }

    public RecipeType getRecipe(ItemStack stack, TierPrefix tier) {
        if (stack.isEmpty()) return this.getFlat();

        return this.getRecipe(e -> e.isCraftable(stack, tier));
    }

    public RecipeType getRecipe(List<ItemStack> stacks, TierPrefix tier) {
        if (stacks.isEmpty() || stacks.stream().allMatch(ItemStack::isEmpty)) return this.getFlat();

        return this.getRecipe(e -> e.match(stacks, -1, tier));
    }
}
