package mods.clayium.machine.crafting;

import mods.clayium.machine.ClayWorkTable.KneadingMethod;
import mods.clayium.machine.ClayWorkTable.KneadingRecipe;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeListKneading extends RecipeList<KneadingRecipe> {

    public RecipeListKneading() {
        super("ClayWorkTable", KneadingRecipe.flat());
    }

    public void addRecipe(ItemStack materialIn, int method, ItemStack resultIn, ItemStack changeIn, long time) {
        add(new KneadingRecipe(materialIn, KneadingMethod.fromId(method), (int) time, resultIn, changeIn));
    }

    @Override
    public KneadingRecipe makeRecipe(ItemStack materialIn, int tier, ItemStack resultIn, long energy, long time) {
        throw new UnsupportedOperationException("Use the other method to add kneading recipe.");
    }

    @Override
    public KneadingRecipe makeRecipe(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        throw new UnsupportedOperationException("Use the other method to add kneading recipe.");
    }
}
