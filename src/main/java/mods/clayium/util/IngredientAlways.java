package mods.clayium.util;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

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
