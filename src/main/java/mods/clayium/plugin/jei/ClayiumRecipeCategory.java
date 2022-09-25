package mods.clayium.plugin.jei;

import mezz.jei.api.recipe.IRecipeCategory;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.crafting.RecipeElement;

public abstract class ClayiumRecipeCategory implements IRecipeCategory<RecipeElement> {
    @Override
    public String getModName() {
        return ClayiumCore.ModName;
    }
}
