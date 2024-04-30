package mods.clayium.machine.crafting;

import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

class SmeltingRecipe extends ClayiumRecipes.SimpleMachineRecipe {

    private static final int tierSmelting = 0;
    private static final int energySmelting = 4;
    private static final int timeSmelting = 200;

    private boolean containRegisteredRecipes = false;

    SmeltingRecipe(String id) {
        super(id);
    }

    // TODO もっとスマートにしたい
    @Override
    public <T extends IRecipeElement> T getRecipe(Predicate<T> matcher, T flat) {
        if (!containRegisteredRecipes) registerFurnaceRecipe();

        return super.getRecipe(matcher, flat);
    }

    private void registerFurnaceRecipe() {
        containRegisteredRecipes = true;

        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            this.addRecipe(entry.getKey(), tierSmelting, entry.getValue(), energySmelting, timeSmelting);
        }
    }
}
