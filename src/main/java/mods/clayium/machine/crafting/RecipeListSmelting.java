package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.Map;
import java.util.function.Predicate;

public class RecipeListSmelting extends RecipeListGeneral {

    protected static final int tierSmelting = 0;
    protected static final int energySmelting = 4;
    protected static final int timeSmelting = 200;

    protected boolean beforeLoadFurnaceRecipe = true;

    public RecipeListSmelting(String id) {
        super(id);
    }

    @Override
    public RecipeElement getRecipe(Predicate<RecipeElement> matcher) {
        if (this.beforeLoadFurnaceRecipe) registerFurnaceRecipe();

        return super.getRecipe(matcher);
    }

    private void registerFurnaceRecipe() {
        this.beforeLoadFurnaceRecipe = false;

        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            this.addRecipe(entry.getKey(), tierSmelting, entry.getValue(), energySmelting, timeSmelting);
        }
    }
}
