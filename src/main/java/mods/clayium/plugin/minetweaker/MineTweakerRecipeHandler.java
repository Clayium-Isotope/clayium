package mods.clayium.plugin.minetweaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.Recipes;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenClass("mods.clayium.ClayMachines")
public class MineTweakerRecipeHandler {
    public static void load() {
        MineTweakerAPI.registerClass(MineTweakerRecipeHandler.class);
    }

    @ZenMethod
    public static void addRecipe(String recipeId, IIngredient[] input, int tier, IItemStack[] output, long energy, long time) {
        addRecipe(recipeId, input, 0, tier, output, energy, time);
    }

    @ZenMethod
    public static void addRecipe(String recipeId, IIngredient[] input, int method, int tier, IItemStack[] output, long energy, long time) {
        if (Recipes.GetRecipes(recipeId) != null)
            MineTweakerAPI.apply(new AddRecipeAction(recipeId, input, method, tier, output, energy, time));
    }

    @ZenMethod
    public static void remove(String recipeId, IIngredient output) {
        removeRecipe(recipeId, null, output);
    }

    @ZenMethod
    public static void removeRecipe(String recipeId, @Optional IIngredient[] input, IIngredient output) {
        if (Recipes.GetRecipes(recipeId) == null) {
            return;
        }
        List<Recipes.RecipeCondition> toRemove = new ArrayList<Recipes.RecipeCondition>();

label37:
        for (Map.Entry<Recipes.RecipeCondition, Recipes.RecipeResult> entry : (Iterable<Map.Entry<Recipes.RecipeCondition, Recipes.RecipeResult>>) (Recipes.GetRecipes(recipeId)).recipeResultMap.entrySet()) {
            for (ItemStack component : ((Recipes.RecipeResult) entry.getValue()).getResults()) {
                if (!output.matches(MineTweakerMC.getIItemStack(component))) {
                    break;
                }
                if (input != null) {
                    for (int i = 0; i < input.length && i < (((Recipes.RecipeCondition) entry.getKey()).getMaterials()).length; i++) {

                        IIngredient ingred = input[i];
                        Object recipe = ((Recipes.RecipeCondition) entry.getKey()).getMaterials()[i];
                        for (ItemStack item : UtilItemStack.object2ItemStacks(recipe)) {
                            if (!ingred.matches(MineTweakerMC.getIItemStack(item))) {
                                continue label37;
                            }
                        }
                    }
                }
                toRemove.add(entry.getKey());
            }
        }

        for (Recipes.RecipeCondition recipe : toRemove)
            MineTweakerAPI.apply(new RemoveRecipeAction(recipeId, recipe));
    }

    public static class RecipePattern
            implements IItemPattern {
        private IIngredient ingred;
        private ItemStack[] ingredExamples;

        public RecipePattern(IIngredient ingred) {
            this.ingred = ingred;
        }


        public String toString() {
            return "RecipePattern<" + ((this.ingred == null) ? "null" : this.ingred.toString()) + ">";
        }


        public boolean match(ItemStack itemstack, boolean checkStackSize) {
            if (this.ingred == null || itemstack == null)
                return false;
            int amount = this.ingred.getAmount();
            if (checkStackSize && itemstack.stackSize < amount)
                return false;
            int o = itemstack.stackSize;
            itemstack.stackSize = amount;
            IItemStack i = MineTweakerMC.getIItemStack(itemstack);
            itemstack.stackSize = o;
            return this.ingred.matches(i);
        }


        public boolean hasIntersection(IItemPattern pattern, boolean checkStackSize) {
            if (pattern == null)
                return false;
            for (ItemStack item : toItemStacks()) {
                if (pattern.match(item, checkStackSize))
                    return true;
            }
            return false;
        }


        public int getStackSize(ItemStack itemstack) {
            return (this.ingred != null) ? this.ingred.getAmount() : 1;
        }


        public ItemStack[] toItemStacks() {
            if (this.ingred == null)
                return null;
            if (this.ingredExamples == null) {
                this.ingredExamples = MineTweakerMC.getExamples(this.ingred);
                for (ItemStack item : this.ingredExamples) {
                    if (item != null)
                        item.stackSize = this.ingred.getAmount();
                }
            }
            return this.ingredExamples;
        }


        public ItemStack isSimple() {
            return null;
        }


        public boolean isAvailable() {
            return true;
        }
    }


    private static class AddRecipeAction
            implements IUndoableAction {
        private String recipeId;

        private Recipes.RecipeCondition condition;
        private Recipes.RecipeResult oldResult;
        private Recipes.RecipeResult newResult;

        public AddRecipeAction(String recipeId, IIngredient[] input, int method, int tier, IItemStack[] output, long energy, long time) {
            this.recipeId = recipeId;

            Object[] materials = new Object[input.length];
            for (int i = 0; i < input.length; i++) {
                materials[i] = new RecipePattern(input[i]);
            }
            this.condition = new Recipes.RecipeCondition(materials, method, tier);
            this.newResult = new Recipes.RecipeResult(MineTweakerMC.getItemStacks(output), energy, time);
        }


        public void apply() {
            this.oldResult = Recipes.GetRecipes(this.recipeId).addRecipe(this.condition, this.newResult);
        }


        public boolean canUndo() {
            return true;
        }


        public void undo() {
            Recipes.GetRecipes(this.recipeId).removeRecipe(this.condition);
            if (this.oldResult != null) {
                Recipes.GetRecipes(this.recipeId).addRecipe(this.condition, this.oldResult);
            }
        }


        public String describe() {
            return "Adding " + this.recipeId + " recipe for " + Arrays.toString((Object[]) MineTweakerMC.getIItemStacks(this.newResult.getResults()));
        }


        public String describeUndo() {
            return "Removing " + this.recipeId + " recipe for " + Arrays.toString((Object[]) MineTweakerMC.getIItemStacks(this.newResult.getResults()));
        }


        public Object getOverrideKey() {
            return null;
        }
    }

    private static class RemoveRecipeAction implements IUndoableAction {
        private String recipeId;
        private Recipes.RecipeCondition condition;
        private Recipes.RecipeResult oldResult;
        private IItemStack[] output;

        public RemoveRecipeAction(String recipeId, Recipes.RecipeCondition condition) {
            this.condition = condition;
            this.recipeId = recipeId;
            this.oldResult = Recipes.GetRecipes(recipeId).getResult(condition);
        }


        public void apply() {
            this.oldResult = Recipes.GetRecipes(this.recipeId).removeRecipe(this.condition);
        }


        public boolean canUndo() {
            return true;
        }


        public void undo() {
            Recipes.GetRecipes(this.recipeId).addRecipe(this.condition, this.oldResult);
        }


        public String describe() {
            return "Removing " + this.recipeId + " recipe for " + Arrays.toString((Object[]) MineTweakerMC.getIItemStacks(this.oldResult.getResults()));
        }


        public String describeUndo() {
            return "Restoring " + this.recipeId + " recipe for " + Arrays.toString((Object[]) MineTweakerMC.getIItemStacks(this.oldResult.getResults()));
        }


        public Object getOverrideKey() {
            return null;
        }
    }
}
