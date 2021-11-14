package mods.clayium.plugin.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface INEIRecipeEntry {
    boolean matchForCraftingRecipe(ItemStack paramItemStack);

    boolean matchForUsageRecipe(ItemStack paramItemStack);

    void drawExtras();

    TemplateRecipeHandler.CachedRecipe asCachedRecipe();

    List<PositionedStack> getIngredientsToSort();

    TemplateRecipeHandler getHandler();

    void setHandler(TemplateRecipeHandler paramTemplateRecipeHandler);
}
