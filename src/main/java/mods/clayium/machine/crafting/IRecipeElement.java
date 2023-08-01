package mods.clayium.machine.crafting;

import mezz.jei.api.recipe.IRecipeWrapper;
import mods.clayium.util.TierPrefix;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public interface IRecipeElement extends IRecipe, IRecipeWrapper {
    int getTier();
    default int getMethod() { return -1; }
    List<ItemStack> getResults();
    long getEnergy();
    long getTime();

    boolean isFlat();

    default boolean isCraftable(ItemStack itemstack, TierPrefix tier) {
        if (this.getTier() > tier.meta()) {
            return false;
        }

        for (Ingredient ingredient : this.getIngredients()) {
            if (ingredient.apply(itemstack)) {
                return true;
            }
        }
        return false;
    }

    default boolean match(List<ItemStack> itemStacksIn, int methodIn, TierPrefix tierIn) {
        if (this.getMethod() != methodIn || this.getTier() > tierIn.meta() || this.getIngredients().size() > itemStacksIn.size()) return false;

        for (int i = 0; i < this.getIngredients().size(); i++)
            if (!this.getIngredients().get(i).apply(itemStacksIn.get(i)))
                return false;

        return true;
    }
}
