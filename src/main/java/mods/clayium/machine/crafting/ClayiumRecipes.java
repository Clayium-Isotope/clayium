package mods.clayium.machine.crafting;

import mods.clayium.item.ClayiumItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClayiumRecipes {
    public static List<RecipeElement> clayWorkTableRecipe = new ArrayList<>(L(
            new RecipeElement(L(I(Items.CLAY_BALL), ItemStack.EMPTY), L(I(ClayiumItems.clayStick), ItemStack.EMPTY), 0, 4),
            new RecipeElement(L(I(ClayiumItems.largeClayBall), ItemStack.EMPTY), L(I(ClayiumItems.clayDisc), ItemStack.EMPTY), 1, 30),
            new RecipeElement(L(I(ClayiumItems.largeClayBall), I(ClayiumItems.clayRollingPin)), L(I(ClayiumItems.clayDisc), I(Items.CLAY_BALL, 2)), 2, 4),
            new RecipeElement(L(I(ClayiumItems.largeClayBall), ItemStack.EMPTY), L(I(ClayiumItems.clayCylinder), ItemStack.EMPTY), 0, 4),
            new RecipeElement(L(I(ClayiumItems.clayPlate), ItemStack.EMPTY), L(I(ClayiumItems.clayBlade), ItemStack.EMPTY), 1, 10),
            new RecipeElement(L(I(ClayiumItems.clayPlate), I(ClayiumItems.clayRollingPin)), L(I(ClayiumItems.clayBlade), I(Items.CLAY_BALL, 2)), 2, 1),
            new RecipeElement(L(I(ClayiumItems.clayPlate), I(ClayiumItems.claySlicer)), L(I(ClayiumItems.clayStick, 4), ItemStack.EMPTY), 5, 3),
            new RecipeElement(L(I(ClayiumItems.clayPlate), I(ClayiumItems.claySpatula)), L(I(ClayiumItems.clayStick, 4), ItemStack.EMPTY), 5, 3),
            new RecipeElement(L(I(ClayiumItems.clayPlate, 6), I(ClayiumItems.clayRollingPin)), L(I(ClayiumItems.largeClayPlate), ItemStack.EMPTY), 3, 10),
            new RecipeElement(L(I(ClayiumItems.clayPlate, 3), ItemStack.EMPTY), L(I(ClayiumItems.largeClayBall), ItemStack.EMPTY), 0, 40),
            new RecipeElement(L(I(ClayiumItems.clayDisc), I(ClayiumItems.claySlicer)), L(I(ClayiumItems.clayPlate), I(Items.CLAY_BALL, 2)), 3, 4),
            new RecipeElement(L(I(ClayiumItems.clayDisc), I(ClayiumItems.claySpatula)), L(I(ClayiumItems.clayPlate), I(Items.CLAY_BALL, 2)), 3, 4),
            new RecipeElement(L(I(ClayiumItems.clayDisc), I(ClayiumItems.claySpatula)), L(I(ClayiumItems.clayRing), I(ClayiumItems.smallClayDisc)), 4, 2),
            new RecipeElement(L(I(ClayiumItems.clayDisc), ItemStack.EMPTY), L(I(ClayiumItems.rawClaySlicer), ItemStack.EMPTY), 2, 15),
            new RecipeElement(L(I(ClayiumItems.clayDisc), I(ClayiumItems.clayRollingPin)), L(I(ClayiumItems.rawClaySlicer), ItemStack.EMPTY), 3, 2),
            new RecipeElement(L(I(ClayiumItems.smallClayDisc), I(ClayiumItems.claySpatula)), L(I(ClayiumItems.smallClayRing), I(ClayiumItems.shortClayStick)), 4, 1),
            new RecipeElement(L(I(ClayiumItems.clayCylinder), ItemStack.EMPTY), L(I(ClayiumItems.clayNeedle), ItemStack.EMPTY), 0, 3),
            new RecipeElement(L(I(ClayiumItems.clayCylinder), I(ClayiumItems.claySlicer)), L(I(ClayiumItems.smallClayDisc, 8), ItemStack.EMPTY), 0, 7),
            new RecipeElement(L(I(ClayiumItems.clayCylinder), I(ClayiumItems.claySpatula)), L(I(ClayiumItems.smallClayDisc, 8), ItemStack.EMPTY), 0, 7)
            ));

    private static <T> List<T> L(T... elements) {
        return Arrays.asList(elements);
    }
    private static ItemStack I(Item item) {
        return new ItemStack(item);
    }
    private static ItemStack I(Item itemIn, int amount) {
        return new ItemStack(itemIn, amount);
    }

    public static boolean hasResult(List<RecipeElement> recipes, List<ItemStack> stack) {
        for (RecipeElement recipe : recipes)
            if (recipe.getCondition().match(stack)) return true;

        return false;
    }

    public static boolean hasResult(List<RecipeElement> recipes, ItemStack... stacks) {
        return hasResult(recipes, L(stacks));
    }

    public static RecipeElement getRecipeElement(List<RecipeElement> recipes, NonNullList<ItemStack> referStacks, int method, int tier) {
        for (RecipeElement recipe : recipes) {
            if (recipe.getCondition().match(referStacks, method, tier)) {
                return recipe;
            }
        }

        return RecipeElement.FLAT;
    }
}
