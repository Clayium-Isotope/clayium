package mods.clayium.pan;

import java.util.List;

import mods.clayium.block.tile.InventoryCraftingInTile;
import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.ItemPatternItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class PANACFactoryCraftingTable
        implements IPANAdapterConversionFactory {
    public static long energy = 10L;


    public boolean match(World world, int x, int y, int z) {
        return (world.getBlock(x, y, z) == Blocks.crafting_table);
    }


    public IPANConversion getConversion(IPANAdapter adapter) {
        boolean flag = true;
        for (ItemStack item : adapter.getPatternItems()) {
            if (item != null) {
                flag = false;
                break;
            }
        }
        if (flag)
            return null;
        List recipes = CraftingManager.getInstance().getRecipeList();
        for (Object recipe : recipes) {
            if (recipe instanceof IRecipe && ((IRecipe) recipe).matches(InventoryCraftingInTile.getNormalCraftingGrid(adapter.getPatternItems()), adapter.getConnectedWorld())) {
                if (recipe instanceof ShapedRecipes) {
                    ShapedRecipes recipe1 = (ShapedRecipes) recipe;
                    IItemPattern[] patterns = new IItemPattern[recipe1.recipeItems.length];
                    for (int i = 0; i < recipe1.recipeItems.length; i++) {
                        patterns[i] = new ItemPatternItemStack(recipe1.recipeItems[i]);
                    }
                    return new PANConversion(patterns, new ItemStack[] {recipe1.getRecipeOutput().copy()}, energy);
                }
                if (recipe instanceof ShapelessRecipes) {
                    ShapelessRecipes recipe1 = (ShapelessRecipes) recipe;
                    IItemPattern[] patterns = new IItemPattern[recipe1.recipeItems.size()];
                    for (int i = 0; i < recipe1.recipeItems.size(); i++) {
                        patterns[i] = new ItemPatternItemStack((List<ItemStack>) recipe1.recipeItems.get(i));
                    }
                    return new PANConversion(patterns, new ItemStack[] {recipe1.getRecipeOutput().copy()}, energy);
                }
                if (recipe instanceof ShapedOreRecipe) {
                    ShapedOreRecipe recipe1 = (ShapedOreRecipe) recipe;
                    IItemPattern[] patterns = new IItemPattern[(recipe1.getInput()).length];
                    for (int i = 0; i < (recipe1.getInput()).length; i++) {
                        if (recipe1.getInput()[i] instanceof ItemStack)
                            patterns[i] = new ItemPatternItemStack((ItemStack) recipe1.getInput()[i]);
                        if (recipe1.getInput()[i] instanceof List)
                            patterns[i] = new ItemPatternItemStack((List) recipe1.getInput()[i]);
                    }
                    return new PANConversion(patterns, new ItemStack[] {recipe1.getRecipeOutput().copy()}, energy);
                }
                if (recipe instanceof ShapelessOreRecipe) {
                    ShapelessOreRecipe recipe1 = (ShapelessOreRecipe) recipe;
                    IItemPattern[] patterns = new IItemPattern[recipe1.getInput().size()];
                    for (int i = 0; i < recipe1.getInput().size(); i++) {
                        if (recipe1.getInput().get(i) instanceof ItemStack)
                            patterns[i] = new ItemPatternItemStack((List<ItemStack>) recipe1.getInput().get(i));
                        if (recipe1.getInput().get(i) instanceof List)
                            patterns[i] = new ItemPatternItemStack((List<ItemStack>) recipe1.getInput().get(i));
                    }
                    return new PANConversion(patterns, new ItemStack[] {recipe1.getRecipeOutput().copy()}, energy);
                }
            }
        }
        return null;
    }
}
