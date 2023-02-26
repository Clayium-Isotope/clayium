package mods.clayium.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class IngredientAlways extends Ingredient {
    @Override
    public ItemStack[] getMatchingStacks() {
        return new ItemStack[] { ItemStack.EMPTY };
    }

    @Override
    public boolean apply(@Nullable ItemStack p_apply_1_) {
        return true;
    }
}
