package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClayiumRecipe extends HashMap<Integer, RecipeElement> {
    private static final Map<String, ClayiumRecipe> id2Recipe = new HashMap<>();

    public ClayiumRecipe(String recipeId) {
        super();
        id2Recipe.put(recipeId, this);
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
        addRecipe(materialIn, -1, 0, resultIn, energy, time);
    }

    public void addRecipe(ItemStack materialIn, int tier, ItemStack resultIn, long energy, long time) {
        addRecipe(materialIn, -1, tier, resultIn, energy, time);
    }

    public void addRecipe(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        addRecipe(materialIn, -1, tier, resultIn, energy, time);
    }

    public void addRecipe(ItemStack materialIn, int method, ItemStack resultIn, long time) {
        addRecipe(materialIn, method, 0, resultIn, 1L, time);
    }

    public void addRecipe(List<ItemStack> materialIn, int method, List<ItemStack> resultIn, long time) {
        addRecipe(materialIn, method, 0, resultIn, 1L, time);
    }

    public void addRecipe(ItemStack materialIn, int method, int tier, ItemStack resultIn, long energy, long time) {
        RecipeElement element = new RecipeElement(materialIn, method, tier, resultIn, energy, time);
        this.put(element.hashCode(), element);
    }

    public void addRecipe(List<ItemStack> materialIn, int method, int tier, List<ItemStack> resultIn, long energy, long time) {
        RecipeElement element = new RecipeElement(materialIn, method, tier, resultIn, energy, time);
        this.put(element.hashCode(), element);
    }

    public RecipeElement getRecipe(List<ItemStack> materials, int method, int tier) {
        for (RecipeElement element : this.values()) {
            if (element.getCondition().match(materials, method, tier)) {
                return element;
            }
        }

        return RecipeElement.FLAT;
    }

    public RecipeElement getRecipe(ItemStack material, int tier) {
        for (RecipeElement element : this.values()) {
            if (element.getCondition().isCraftable(material, tier)) {
                return element;
            }
        }

        return RecipeElement.FLAT;
    }
}
