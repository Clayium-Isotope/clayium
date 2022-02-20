package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClayiumRecipe extends ArrayList<RecipeElement> {
    public ClayiumRecipe() {
        super();
    }

    public void addRecipe(ItemStack materialIn, ItemStack resultIn, long time) {
        addRecipe(materialIn, -1, 0, resultIn, 1L, time);
    }

    public void addRecipe(List<ItemStack> materialIn, List<ItemStack> resultIn, long time) {
        addRecipe(materialIn, -1, 0, resultIn, 1L, time);
    }

    public void addRecipe(ItemStack materialIn, ItemStack resultIn, long energy, long time) {
        addRecipe(materialIn, -1, 0, resultIn, energy, time);
    }

    public void addRecipe(List<ItemStack> materialIn, List<ItemStack> resultIn, long energy, long time) {
        add(new RecipeElement(materialIn, -1, 0, resultIn, energy, time));
    }

    public void addRecipe(ItemStack materialIn, int tier, ItemStack resultIn, long energy, long time) {
        addRecipe(materialIn, -1, tier, resultIn, energy, time);
    }

    public void addRecipe(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        add(new RecipeElement(materialIn, -1, tier, resultIn, energy, time));
    }

    public void addRecipe(ItemStack materialIn, int method, ItemStack resultIn, long time) {
        addRecipe(materialIn, method, 0, resultIn, 1L, time);
    }

    public void addRecipe(List<ItemStack> materialIn, int method, List<ItemStack> resultIn, long time) {
        addRecipe(materialIn, method, 0, resultIn, 1L, time);
    }

    public void addRecipe(ItemStack materialIn, int method, int tier, ItemStack resultIn, long energy, long time) {
        add(new RecipeElement(materialIn, method, tier, resultIn, energy, time));
    }

    public void addRecipe(List<ItemStack> materialIn, int method, int tier, List<ItemStack> resultIn, long energy, long time) {
        add(new RecipeElement(materialIn, method, tier, resultIn, energy, time));
    }

    public RecipeElement getRecipe(List<ItemStack> materials, int method, int tier) {
        for (RecipeElement element : this) {
            if (element.getCondition().match(materials, method, tier)) {
                return element;
            }
        }

        return RecipeElement.FLAT;
    }

    public RecipeElement getRecipe(ItemStack material, int tier) {
        for (RecipeElement element : this) {
            if (element.getCondition().isCraftable(material, tier)) {
                return element;
            }
        }

        return RecipeElement.FLAT;
    }
}
