package mods.clayium.util.crafting;

import net.minecraft.item.ItemStack;


public class SimpleMachinesRecipes
        extends Recipes {
    public SimpleMachinesRecipes(String recipeid) {
        super(recipeid);
    }

    public void addRecipe(Object materials, int tier, ItemStack results, long energy, long time) {
        addRecipe(new Object[] {materials}, 0, tier, new ItemStack[] {results}, energy, time);
    }

    public int getConsumedStackSize(ItemStack materials, int tier) {
        int[] res = getConsumedStackSize(new ItemStack[] {materials}, 0, tier);
        if (res == null) return 0;
        return res[0];
    }

    public ItemStack getResult(ItemStack materials, int tier) {
        ItemStack[] res = getResult(new ItemStack[] {materials}, 0, tier);
        if (res == null) return null;
        return res[0];
    }

    public long getEnergy(ItemStack materials, int tier) {
        return getEnergy(new ItemStack[] {materials}, 0, tier);
    }

    public long getTime(ItemStack materials, int tier) {
        return getTime(new ItemStack[] {materials}, 0, tier);
    }

    public boolean hasResult(ItemStack materials, int tier) {
        return hasResult(new ItemStack[] {materials}, tier);
    }

    public void addRecipe(Object materials, ItemStack results) {
        addRecipe(materials, results, 1L);
    }

    public void addRecipe(Object materials, ItemStack results, long time) {
        addRecipe(materials, results, 1L, time);
    }

    public void addRecipe(Object materials, ItemStack results, long energy, long time) {
        addRecipe(materials, 0, results, energy, time);
    }

    public int getConsumedStackSize(ItemStack materials) {
        return getConsumedStackSize(materials, 0);
    }

    public ItemStack getResult(ItemStack materials) {
        return getResult(materials, 0);
    }

    public long getEnergy(ItemStack materials) {
        return getEnergy(materials, 0);
    }

    public long getTime(ItemStack materials) {
        return getTime(materials, 0);
    }

    public boolean hasResult(ItemStack materials) {
        return hasResult(materials, 0);
    }
}


