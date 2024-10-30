package mods.clayium.machine.common;

import mods.clayium.machine.crafting.IRecipeElement;
import mods.clayium.machine.crafting.RecipeList;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UsedFor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@UsedFor(UsedFor.Type.TileEntity)
public interface ClayiumRecipeProvider<T extends IRecipeElement> extends RecipeProvider {
    @Nonnull
    RecipeList<T> getRecipeList();

    static <T extends IRecipeElement> T getRecipe(RecipeList<T> recipeCard, Predicate<T> pred) {
        return recipeCard.getRecipe(pred);
    }

    default T getRecipe(Predicate<T> pred) {
        return this.getRecipeList().getRecipe(pred);
    }
    default T getRecipe(ItemStack stack) {
        if (stack.isEmpty()) return this.getRecipeList().getFlat();

        return this.getRecipe(e -> e.isCraftable(stack, this.getRecipeTier()));
    }
    default T getRecipe(List<ItemStack> stacks) {
        if (stacks.isEmpty()) return this.getRecipeList().getFlat();

        return this.getRecipe(e -> e.match(stacks, -1, this.getRecipeTier()));
    }
    default T getRecipe(int hash) {
        return this.getRecipe(e -> e.hashCode() == hash);
    }

    default boolean canCraft(ItemStack stack) {
        if (stack.isEmpty()) return false;

        return this.canCraft(this.getRecipe(stack));
    }
    default boolean canCraft(List<ItemStack> stacks) {
        if (stacks.isEmpty()) return false;

        return this.canCraft(this.getRecipe(stacks));
    }
    boolean canCraft(T recipe);

    @Nullable
    static <T extends IRecipeElement> int[] getCraftPermutation(ClayiumRecipeProvider<T> provider, ItemStack mat1, ItemStack mat2) {
        if (provider.canCraft(Arrays.asList(mat1, mat2)))
            return new int[] { 0, 1 };

        if (provider.canCraft(Arrays.asList(mat2, mat1)))
            return new int[] { 1, 0 };

        if (provider.canCraft(Collections.singletonList(mat1)))
            return new int[] { 0 };

        if (provider.canCraft(Collections.singletonList(mat2)))
            return new int[] { 1 };

        return null;
    }

    static <T extends IRecipeElement> List<ItemStack> getCraftPermStacks(ClayiumRecipeProvider<T> provider, ItemStack mat1, ItemStack mat2) {
        List<ItemStack> materials;

        materials = Arrays.asList(mat1, mat2);
        if (provider.canCraft(materials))
            return materials;

        materials = Arrays.asList(mat2, mat1);
        if (provider.canCraft(materials))
            return materials;

        if (provider.canCraft(Collections.singletonList(mat1)))
            return Collections.singletonList(mat1);

        if (provider.canCraft(Collections.singletonList(mat2)))
            return Collections.singletonList(mat2);

        return Collections.emptyList();
    }

    static <T extends IRecipeElement> T getCraftPermRecipe(ClayiumRecipeProvider<T> provider, ItemStack mat1, ItemStack mat2) {
        T recipe;

        recipe = provider.getRecipe(Arrays.asList(mat1, mat2));
        if (provider.canCraft(recipe))
            return recipe;

        recipe = provider.getRecipe(Arrays.asList(mat2, mat1));
        if (provider.canCraft(recipe))
            return recipe;

        recipe = provider.getRecipe(Collections.singletonList(mat1));
        if (provider.canCraft(recipe))
            return recipe;

        recipe = provider.getRecipe(Collections.singletonList(mat2));
        if (provider.canCraft(recipe))
            return recipe;

        return provider.getRecipeList().getFlat();
    }

    static <T extends IRecipeElement> T getCraftPermRecipe(RecipeList<T> recipes, TierPrefix tier, Predicate<T> validator, ItemStack mat1, ItemStack mat2) {
        T recipe;

        recipe = recipes.getRecipe(Arrays.asList(mat1, mat2), tier);
        if (validator.test(recipe))
            return recipe;

        recipe = recipes.getRecipe(Arrays.asList(mat2, mat1), tier);
        if (validator.test(recipe))
            return recipe;

        recipe = recipes.getRecipe(Collections.singletonList(mat1), tier);
        if (validator.test(recipe))
            return recipe;

        recipe = recipes.getRecipe(Collections.singletonList(mat2), tier);
        if (validator.test(recipe))
            return recipe;

        return recipes.getFlat();
    }
}
