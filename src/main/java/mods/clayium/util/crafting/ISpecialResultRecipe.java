package mods.clayium.util.crafting;

import net.minecraft.item.ItemStack;

public interface ISpecialResultRecipe {
    RecipeMap[] getUsageRecipeMap(ItemStack paramItemStack);

    RecipeMap[] getCraftingRecipeMap(ItemStack paramItemStack);
}


