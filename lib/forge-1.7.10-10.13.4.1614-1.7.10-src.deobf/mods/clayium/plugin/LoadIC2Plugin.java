package mods.clayium.plugin;

import cpw.mods.fml.common.Optional.Method;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import ic2.core.Ic2Items;

import java.util.HashMap;
import java.util.Map;

import mods.clayium.item.CItems;
import mods.clayium.item.CMaterials;
import mods.clayium.util.crafting.CRecipes;
import mods.clayium.util.crafting.OreDictionaryStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
/*    */


public class LoadIC2Plugin {
    @Method(modid = "IC2")
    public static void loadRecipes() {
        CRecipes.register1to1Recipe(CRecipes.recipeCondenser, CMaterials.getOD(CMaterials.IRIDIUM, CMaterials.INGOT), 5, Ic2Items.iridiumOre, 60L);
        CRecipes.register1to1Recipe(CRecipes.recipeCondenser, CMaterials.getOD(CMaterials.URANIUM, CMaterials.INGOT), 5, Ic2Items.Uran238, 60L);
        CRecipes.register1to1Recipe(CRecipes.recipeCondenser, CMaterials.getOD(CMaterials.PLUTONIUM, CMaterials.INGOT), 5, Ic2Items.Plutonium, 60L);
        CRecipes.register1to1Recipe(CRecipes.recipeSmelter, Ic2Items.iridiumOre, 5, CMaterials.getODExist(CMaterials.IRIDIUM, CMaterials.INGOT), 60L);
        CRecipes.register1to1Recipe(CRecipes.recipeSmelter, Ic2Items.Uran238, 5, CMaterials.getODExist(CMaterials.URANIUM, CMaterials.INGOT), 60L);
        CRecipes.register1to1Recipe(CRecipes.recipeSmelter, Ic2Items.Plutonium, 5, CMaterials.getODExist(CMaterials.PLUTONIUM, CMaterials.INGOT), 60L);

        if (!CMaterials.existOD("itemRawRubber")) {
            CRecipes.recipeCAInjector.addRecipe((Object[]) CRecipes.ii(new ItemStack[] {CRecipes.i(Blocks.log), CMaterials.get(CMaterials.ANTIMATTER, CMaterials.GEM)}), 0, 10,
                    CRecipes.ii(new ItemStack[] {Ic2Items.resin}), CRecipes.e(2.0D, 10), 60L);
        }


        Map<IMachineRecipeManager, CRecipes.RecipeConditions> recipeMap = new HashMap<IMachineRecipeManager, CRecipes.RecipeConditions>();
        recipeMap.put(Recipes.metalformerExtruding, new CRecipes.RecipeConditions(CRecipes.recipeWireDrawingMachine, 4, 20L));
        recipeMap.put(Recipes.metalformerRolling, new CRecipes.RecipeConditions(CRecipes.recipePipeDrawingMachine, 4, 20L));
        recipeMap.put(Recipes.metalformerCutting, new CRecipes.RecipeConditions(CRecipes.recipeCuttingMachine, 4, 20L));
        recipeMap.put(Recipes.compressor, new CRecipes.RecipeConditions(CRecipes.recipeCondenser, 5, 20L));
        recipeMap.put(Recipes.macerator, new CRecipes.RecipeConditions(CRecipes.recipeGrinder, 6, 20L));
        recipeMap.put(Recipes.extractor, new CRecipes.RecipeConditions(CRecipes.recipeTransformer, 7, 20L));

        for (IMachineRecipeManager ic2recipes : recipeMap.keySet()) {
            mods.clayium.util.crafting.Recipes crecipes = recipeMap.get(ic2recipes).recipes;
            int tier = recipeMap.get(ic2recipes).tier;
            long time = recipeMap.get(ic2recipes).time;
            for (Map.Entry<IRecipeInput, RecipeOutput> entry : ic2recipes.getRecipes().entrySet()) {
                /* 61 */
                IRecipeInput input = entry.getKey();
                RecipeOutput output = entry.getValue();
                if (input instanceof RecipeInputItemStack) {
                    CRecipes.register1to1Recipe(crecipes, CRecipes.s(((RecipeInputItemStack) input).input.copy(), ((RecipeInputItemStack) input).amount), tier, output.items
                            .get(0), time);
                    continue;
                }
                if (input instanceof RecipeInputOreDict) {
                    CRecipes.register1to1Recipe(crecipes, new OreDictionaryStack(((RecipeInputOreDict) input).input, ((RecipeInputOreDict) input).amount), tier, output.items
                            .get(0), time);
                }
            }
        }

        CItems.itemFilterBlockHarvestable.addSpecialFilter(new FilterIC2CropHarvestable());
    }
}
