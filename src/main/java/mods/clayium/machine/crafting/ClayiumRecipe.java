package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClayiumRecipe extends ArrayList<RecipeElement> {
    public ClayiumRecipe() {
        super();
        init();
    }

    public void init() {}

    public void addRecipe(ItemStack materialIn, ItemStack resultIn, long time) {
        addRecipe(materialIn, 0, 0, resultIn, 0, time);
    }

    public void addRecipe(List<ItemStack> materialIn, List<ItemStack> resultIn, long time) {
        addRecipe(materialIn, 0, 0, resultIn, 0, time);
    }

    public void addRecipe(ItemStack materialIn, ItemStack resultIn, long energy, long time) {
        addRecipe(materialIn, 0, 0, resultIn, energy, time);
    }

    public void addRecipe(ItemStack materialIn, int tier, ItemStack resultIn, long energy, long time) {
        addRecipe(materialIn, 0, tier, resultIn, energy, time);
    }

    public void addRecipe(ItemStack materialIn, int method, int tier, ItemStack resultIn, long energy, long time) {
        add(new RecipeElement(materialIn, method, tier, resultIn, energy, time));
    }

    public void addRecipe(List<ItemStack> materialIn, int method, List<ItemStack> resultIn, long time) {
        addRecipe(materialIn, method, 0, resultIn, 0, time);
    }

    public void addRecipe(List<ItemStack> materialIn, int method, int tier, List<ItemStack> resultIn, long energy, long time) {
        add(new RecipeElement(materialIn, method, tier, resultIn, energy, time));
    }
}
