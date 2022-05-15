package mods.clayium.util.crafting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SmeltingRecipe
        extends SimpleMachinesRecipes {
    protected static int methodSmelting = 0;
    protected static int tierSmelting = 0;
    public static int energySmelting = 4;
    public static int timeSmelting = 200;

    public SmeltingRecipe(String recipeid) {
        super(recipeid);
    }

    @Deprecated
    protected Map getMaterialMap(List<ItemStack> materials, int method, int tier) {
        if (method == methodSmelting && tier >= tierSmelting && materials != null && materials
                .size() == 1 &&
                FurnaceRecipes.smelting().getSmeltingResult(materials.get(0)) != null) {
            Map<Object, Object> keyMap = new HashMap<Object, Object>();
            keyMap.put("Material", materials);
            keyMap.put("Method", Integer.valueOf(methodSmelting));
            keyMap.put("Tier", Integer.valueOf(tierSmelting));
            return keyMap;
        }

        Map.Entry entry = getResultEntry(materials, method, tier);
        if (entry == null) return null;
        return (Map) entry.getKey();
    }

    @Deprecated
    protected Map getResultMap(List<ItemStack> materials, int method, int tier) {
        if (method == methodSmelting && tier >= tierSmelting && materials != null && materials
                .size() == 1 &&
                FurnaceRecipes.smelting().getSmeltingResult(materials.get(0)) != null) {
            ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(materials.get(0));
            Map<Object, Object> valueMap = new HashMap<Object, Object>();
            valueMap.put("Result", Array2ArrayList(new ItemStack[] {result}));
            valueMap.put("Energy", Integer.valueOf(energySmelting));
            valueMap.put("Time", Integer.valueOf(timeSmelting));
            return valueMap;
        }
        Map.Entry entry = getResultEntry(materials, method, tier);
        if (entry == null) return null;
        return (Map) entry.getValue();
    }

    @Deprecated
    public boolean hasResult(List<ItemStack> materials, int tier) {
        if (tier >= tierSmelting && materials != null && materials
                .size() == 1 &&
                FurnaceRecipes.smelting().getSmeltingResult(materials.get(0)) != null) {
            return true;
        }
        return super.hasResult(materials, tier);
    }


    public boolean isCraftable(ItemStack material, int tier) {
        if (tier >= tierSmelting &&
                FurnaceRecipes.smelting().getSmeltingResult(material) != null) {
            return true;
        }
        return super.isCraftable(material, tier);
    }


    public RecipeCondition getRecipeConditionFromRecipe(Object[] materials, int method, int tier, boolean sizeCheck) {
        if (materials == null)
            return null;
        if (method == methodSmelting && tier >= tierSmelting && materials.length == 1 && materials[0] instanceof ItemStack) {
            if (FurnaceRecipes.smelting().getSmeltingResult((ItemStack) materials[0]) != null) {
                ItemStack ingred = ((ItemStack) materials[0]).copy();
                ingred.stackSize = 1;
                return new RecipeCondition((Object[]) new ItemStack[] {ingred}, methodSmelting, tierSmelting);
            }
        }
        return super.getRecipeConditionFromRecipe(materials, method, tier, sizeCheck);
    }


    public RecipeCondition getRecipeConditionFromInventory(ItemStack[] materials, int method, int tier, boolean sizeCheck) {
        if (materials == null)
            return null;
        if (method == methodSmelting && tier >= tierSmelting && materials.length == 1) {
            if (FurnaceRecipes.smelting().getSmeltingResult(materials[0]) != null) {
                ItemStack ingred = materials[0].copy();
                ingred.stackSize = 1;
                return new RecipeCondition((Object[]) new ItemStack[] {ingred}, methodSmelting, tierSmelting);
            }
        }
        return super.getRecipeConditionFromInventory(materials, method, tier, sizeCheck);
    }


    public RecipeResult getRecipeResult(RecipeCondition condition) {
        if (condition == null)
            return null;
        ItemStack result;
        if (condition.getMethod() == methodSmelting && condition.getTier() >= tierSmelting && condition
                .getMaterials() != null && (condition.getMaterials()).length == 1 && condition.getMaterials()[0] instanceof ItemStack && (
                result = FurnaceRecipes.smelting().getSmeltingResult((ItemStack) condition.getMaterials()[0])) != null) {
            return new RecipeResult(new ItemStack[] {result}, energySmelting, timeSmelting);
        }
        return super.getRecipeResult(condition);
    }


    public int[] getConsumedStackSize(ItemStack[] materials, int method, int tier) {
        if (method == methodSmelting && tier >= tierSmelting && materials != null && materials.length == 1) {
            if (FurnaceRecipes.smelting().getSmeltingResult(materials[0]) != null)
                return new int[] {1};
        }
        return super.getConsumedStackSize(materials, method, tier);
    }
}


