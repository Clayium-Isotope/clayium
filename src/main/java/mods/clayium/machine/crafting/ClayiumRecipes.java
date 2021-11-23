package mods.clayium.machine.crafting;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.ClayiumItems.CMaterial;
import mods.clayium.item.ClayiumItems.CShape;
import mods.clayium.util.ProgressRatedVariable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Arrays;
import java.util.List;

public class ClayiumRecipes {
    public static ClayiumRecipe clayWorkTable = new ClayiumRecipe() {
        public void init() {
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.ball)), ItemStack.EMPTY), 0, L(I(ClayiumItems.get(CMaterial.clay, CShape.stick)), ItemStack.EMPTY), 4);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.largeBall)), ItemStack.EMPTY), 1, L(I(ClayiumItems.get(CMaterial.clay, CShape.disc)), ItemStack.EMPTY), 30);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.largeBall)), I(ClayiumItems.clayRollingPin)), 2, L(I(ClayiumItems.get(CMaterial.clay, CShape.disc)), I(ClayiumItems.get(CMaterial.clay, CShape.ball), 2)), 4);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.largeBall)), ItemStack.EMPTY), 0, L(I(ClayiumItems.get(CMaterial.clay, CShape.cylinder)), ItemStack.EMPTY), 4);

            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.plate)), ItemStack.EMPTY), 1, L(I(ClayiumItems.get(CMaterial.clay, CShape.blade)), ItemStack.EMPTY), 10);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.plate)), I(ClayiumItems.clayRollingPin)), 2, L(I(ClayiumItems.get(CMaterial.clay, CShape.blade)), I(ClayiumItems.get(CMaterial.clay, CShape.ball), 2)), 1);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.plate)), I(ClayiumItems.claySlicer)), 5, L(I(ClayiumItems.get(CMaterial.clay, CShape.stick), 4), ItemStack.EMPTY), 3);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.plate)), I(ClayiumItems.claySpatula)), 5, L(I(ClayiumItems.get(CMaterial.clay, CShape.stick), 4), ItemStack.EMPTY), 3);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.plate), 6), I(ClayiumItems.clayRollingPin)), 3, L(I(ClayiumItems.get(CMaterial.clay, CShape.largePlate)), ItemStack.EMPTY), 10);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.plate), 3), ItemStack.EMPTY), 0, L(I(ClayiumItems.get(CMaterial.clay, CShape.largeBall)), ItemStack.EMPTY), 40);

            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.disc)), I(ClayiumItems.claySlicer)), 3, L(I(ClayiumItems.get(CMaterial.clay, CShape.plate)), I(ClayiumItems.get(CMaterial.clay, CShape.ball), 2)), 4);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.disc)), I(ClayiumItems.claySpatula)), 3, L(I(ClayiumItems.get(CMaterial.clay, CShape.plate)), I(ClayiumItems.get(CMaterial.clay, CShape.ball), 2)), 4);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.disc)), I(ClayiumItems.claySpatula)), 4, L(I(ClayiumItems.get(CMaterial.clay, CShape.ring)), I(ClayiumItems.get(CMaterial.clay, CShape.smallRing))), 2);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.disc)), ItemStack.EMPTY), 2, L(I(ClayiumItems.rawClaySlicer), ItemStack.EMPTY), 15);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.disc)), I(ClayiumItems.clayRollingPin)), 3, L(I(ClayiumItems.rawClaySlicer), ItemStack.EMPTY), 2);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.smallDisc)), I(ClayiumItems.claySpatula)), 4, L(I(ClayiumItems.get(CMaterial.clay, CShape.smallRing)), I(ClayiumItems.get(CMaterial.clay, CShape.shortStick))), 1);

            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.cylinder)), ItemStack.EMPTY), 0, L(I(ClayiumItems.get(CMaterial.clay, CShape.needle)), ItemStack.EMPTY), 3);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.cylinder)), I(ClayiumItems.claySlicer)), 0, L(I(ClayiumItems.get(CMaterial.clay, CShape.smallDisc), 8), ItemStack.EMPTY), 7);
            addRecipe(L(I(ClayiumItems.get(CMaterial.clay, CShape.cylinder)), I(ClayiumItems.claySpatula)), 0, L(I(ClayiumItems.get(CMaterial.clay, CShape.smallDisc), 8), ItemStack.EMPTY), 7);
    }};
    // TODO
    public static ClayiumRecipe bendingMachine = new ClayiumRecipe() {
        public void init() {
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.ball)), I(ClayiumItems.get(CMaterial.clay, CShape.smallDisc)), ProgressRatedVariable.divideByProgressionRateI(3));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.largeBall)), I(ClayiumItems.get(CMaterial.clay, CShape.disc)), ProgressRatedVariable.divideByProgressionRateI(2));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.disc)), I(ClayiumItems.rawClaySlicer), ProgressRatedVariable.divideByProgressionRateI(1));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.block)), I(ClayiumItems.get(CMaterial.clay, CShape.plate)), ProgressRatedVariable.divideByProgressionRateI(1));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.plate), 4), I(ClayiumItems.get(CMaterial.clay, CShape.largePlate)), ProgressRatedVariable.divideByProgressionRateI(4));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.clay, CShape.blade), 2), ProgressRatedVariable.divideByProgressionRateI(4));

            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.block)), I(ClayiumItems.get(CMaterial.denseClay, CShape.plate)), ProgressRatedVariable.divideByProgressionRateI(4));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.plate), 4), I(ClayiumItems.get(CMaterial.denseClay, CShape.largePlate)), ProgressRatedVariable.divideByProgressionRateI(8));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.denseClay, CShape.blade), 2), ProgressRatedVariable.divideByProgressionRateI(8));

            addRecipe(I(ClayiumItems.get(CMaterial.indClay, CShape.block)), 2, I(ClayiumItems.get(CMaterial.indClay, CShape.plate)), 2L, ProgressRatedVariable.divideByProgressionRateI(4));
            addRecipe(I(ClayiumItems.get(CMaterial.indClay, CShape.plate), 4), 2, I(ClayiumItems.get(CMaterial.indClay, CShape.largePlate)), 2L, ProgressRatedVariable.divideByProgressionRateI(8));
            addRecipe(I(ClayiumItems.get(CMaterial.advIndClay, CShape.block)), 2, I(ClayiumItems.get(CMaterial.advIndClay, CShape.plate)), 4L, ProgressRatedVariable.divideByProgressionRateI(4));
            addRecipe(I(ClayiumItems.get(CMaterial.advIndClay, CShape.plate), 4), 2, I(ClayiumItems.get(CMaterial.advIndClay, CShape.largePlate)), 4L, ProgressRatedVariable.divideByProgressionRateI(8));
    }};
    // TODO
    public static ClayiumRecipe wireDrawingMachine = new ClayiumRecipe() {
        public void init() {
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.ball)), I(ClayiumItems.get(CMaterial.clay, CShape.stick)), ProgressRatedVariable.divideByProgressionRateI(1));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.clay, CShape.stick), 8), ProgressRatedVariable.divideByProgressionRateI(3));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.pipe)), I(ClayiumItems.get(CMaterial.clay, CShape.stick), 4), ProgressRatedVariable.divideByProgressionRateI(2));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.smallDisc)), I(ClayiumItems.get(CMaterial.clay, CShape.stick)), ProgressRatedVariable.divideByProgressionRateI(1));

            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.denseClay, CShape.stick), 8), ProgressRatedVariable.divideByProgressionRateI(6));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.pipe)), I(ClayiumItems.get(CMaterial.denseClay, CShape.stick), 4), ProgressRatedVariable.divideByProgressionRateI(4));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.smallDisc)), I(ClayiumItems.get(CMaterial.denseClay, CShape.stick)), ProgressRatedVariable.divideByProgressionRateI(2));
    }};
    // TODO
    public static ClayiumRecipe pipeDrawingMachine = new ClayiumRecipe() {
        public void init() {
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.clay, CShape.pipe), 2), ProgressRatedVariable.divideByProgressionRateI(3));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.denseClay, CShape.pipe), 2), ProgressRatedVariable.divideByProgressionRateI(6));
    }};
    // TODO
    public static ClayiumRecipe cuttingMachine = new ClayiumRecipe() {
        public void init() {
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.largeBall)), I(ClayiumItems.get(CMaterial.clay, CShape.disc)), ProgressRatedVariable.divideByProgressionRateI(2));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.clay, CShape.smallDisc), 8), ProgressRatedVariable.divideByProgressionRateI(2));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.largePlate)), I(ClayiumItems.get(CMaterial.clay, CShape.disc), 2), ProgressRatedVariable.divideByProgressionRateI(3));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.plate)), I(ClayiumItems.get(CMaterial.clay, CShape.smallDisc), 4), ProgressRatedVariable.divideByProgressionRateI(3));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.stick)), I(ClayiumItems.get(CMaterial.clay, CShape.shortStick), 2), ProgressRatedVariable.divideByProgressionRateI(1));

            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.denseClay, CShape.smallDisc), 8), ProgressRatedVariable.divideByProgressionRateI(4));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.largePlate)), I(ClayiumItems.get(CMaterial.denseClay, CShape.disc), 2), ProgressRatedVariable.divideByProgressionRateI(6));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.plate)), I(ClayiumItems.get(CMaterial.denseClay, CShape.smallDisc), 4), ProgressRatedVariable.divideByProgressionRateI(6));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.stick)), I(ClayiumItems.get(CMaterial.denseClay, CShape.shortStick), 2), ProgressRatedVariable.divideByProgressionRateI(2));
    }};
    // TODO
    public static ClayiumRecipe lathe = new ClayiumRecipe() {
        public void init() {
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.ball)), I(ClayiumItems.get(CMaterial.clay, CShape.shortStick)), ProgressRatedVariable.divideByProgressionRateI(1));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.largeBall)), I(ClayiumItems.get(CMaterial.clay, CShape.cylinder), 8), ProgressRatedVariable.divideByProgressionRateI(4));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.clay, CShape.needle), 2), ProgressRatedVariable.divideByProgressionRateI(3));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.needle)), I(ClayiumItems.get(CMaterial.clay, CShape.stick), 6), ProgressRatedVariable.divideByProgressionRateI(3));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.disc)), I(ClayiumItems.get(CMaterial.clay, CShape.ring)), ProgressRatedVariable.divideByProgressionRateI(2));
            addRecipe(I(ClayiumItems.get(CMaterial.clay, CShape.smallDisc)), I(ClayiumItems.get(CMaterial.clay, CShape.smallRing), 2), ProgressRatedVariable.divideByProgressionRateI(1));

            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.block), 2), I(ClayiumItems.get(CMaterial.denseClay, CShape.cylinder)), ProgressRatedVariable.divideByProgressionRateI(4));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.cylinder)), I(ClayiumItems.get(CMaterial.denseClay, CShape.needle)), ProgressRatedVariable.divideByProgressionRateI(6));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.needle)), I(ClayiumItems.get(CMaterial.denseClay, CShape.stick), 6), ProgressRatedVariable.divideByProgressionRateI(6));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.disc)), I(ClayiumItems.get(CMaterial.denseClay, CShape.ring)), ProgressRatedVariable.divideByProgressionRateI(4));
            addRecipe(I(ClayiumItems.get(CMaterial.denseClay, CShape.smallDisc)), I(ClayiumItems.get(CMaterial.denseClay, CShape.smallRing)), ProgressRatedVariable.divideByProgressionRateI(2));
    }};
    // TODO
    public static ClayiumRecipe millingMachine = new ClayiumRecipe() { public void init() {} };
    public static ClayiumRecipe condenser = new ClayiumRecipe() {
        public void init() {
            addRecipe(I(ClayiumBlocks.compressedClays.get(0), 9), I(ClayiumBlocks.compressedClays.get(1)), 1L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(1), 9), I(ClayiumBlocks.compressedClays.get(2)), 1L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(2), 9), I(ClayiumBlocks.compressedClays.get(3)), 10L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(3), 9), I(ClayiumBlocks.compressedClays.get(4)), 100L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(4), 9), 4, I(ClayiumBlocks.compressedClays.get(5)), 100L, 16L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(5), 9), 4, I(ClayiumBlocks.compressedClays.get(6)), 1000L, 16L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(6), 9), 4, I(ClayiumBlocks.compressedClays.get(7)), 10000L, 13L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(7), 9), 5, I(ClayiumBlocks.compressedClays.get(8)), 100000L, 10L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(8), 9), 5, I(ClayiumBlocks.compressedClays.get(9)), 1000000L, 8L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(9), 9), 5, I(ClayiumBlocks.compressedClays.get(10)), 10000000L, 6L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(10), 9), 5, I(ClayiumBlocks.compressedClays.get(11)), 100000000L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(11), 9), 5, I(ClayiumBlocks.compressedClays.get(12)), 1000000000L, 3L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(12), 9), 5, I(ClayiumBlocks.compressedClays.get(13)), 1000000000L, 25L);
        }
    };
    // TODO
    public static ClayiumRecipe grinder = new ClayiumRecipe() { public void init() {} };
    public static ClayiumRecipe decomposer = new ClayiumRecipe() {
        public void init() {
            addRecipe(I(ClayiumBlocks.compressedClays.get(1)), I(ClayiumItems.get(CMaterial.clay, CShape.ball), 4), 1L, 3L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(2)), I(ClayiumBlocks.compressedClays.get(1), 9), 1L, 3L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(3)), I(ClayiumBlocks.compressedClays.get(2), 9), 1L, 3L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(4)), I(ClayiumBlocks.compressedClays.get(3), 9), 1L, 10L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(5)), I(ClayiumBlocks.compressedClays.get(4), 9), 1L, 20L);
        }
    };
    // TODO
    public static ClayiumRecipe assembler = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe inscriber = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe centrifuge = new ClayiumRecipe() { public void init() {} };
    public static ClayiumRecipe energeticClayCondenser = new ClayiumRecipe() {
        public void init() {
            addRecipe(I(ClayiumBlocks.compressedClays.get(5), 9), I(ClayiumBlocks.compressedClays.get(6)), 1L, 16L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(6), 9), I(ClayiumBlocks.compressedClays.get(7)), 10L, 32L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(7), 9), I(ClayiumBlocks.compressedClays.get(8)), 100L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(8), 9), I(ClayiumBlocks.compressedClays.get(9)), 1000L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(9), 9), I(ClayiumBlocks.compressedClays.get(10)), 10000L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(10), 9), I(ClayiumBlocks.compressedClays.get(11)), 100000L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(11), 9), I(ClayiumBlocks.compressedClays.get(12)), 1000000L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(12), 9), I(ClayiumBlocks.compressedClays.get(13)), 10000000L, 64L);
        }
    };
    // TODO
    public static ClayiumRecipe chemicalReactor = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe alloySmelter = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe blastFurnace = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe electrolysisReactor = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe reactor = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe transformer = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe CACondenser = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe CAInjector = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe CAReactor = new ClayiumRecipe() { public void init() {} };
    // TODO
    public static ClayiumRecipe smelter = new ClayiumRecipe() { public void init() {} };
    public static ClayiumRecipe energeticClayDecomposer = new ClayiumRecipe() {
        public void init() {
            addRecipe(I(ClayiumBlocks.compressedClays.get(0)), 13, I(ClayiumItems.get(CMaterial.clay, CShape.ball), 4), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(1)), 13, I(ClayiumBlocks.compressedClays.get(0), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(2)), 13, I(ClayiumBlocks.compressedClays.get(1), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(3)), 13, I(ClayiumBlocks.compressedClays.get(2), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(4)), 13, I(ClayiumBlocks.compressedClays.get(3), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(5)), 13, I(ClayiumBlocks.compressedClays.get(4), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(6)), 13, I(ClayiumBlocks.compressedClays.get(5), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(7)), 13, I(ClayiumBlocks.compressedClays.get(6), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(8)), 13, I(ClayiumBlocks.compressedClays.get(7), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(9)), 13, I(ClayiumBlocks.compressedClays.get(8), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(10)), 13, I(ClayiumBlocks.compressedClays.get(9), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(11)), 13, I(ClayiumBlocks.compressedClays.get(10), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(12)), 13, I(ClayiumBlocks.compressedClays.get(11), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(13)), 13, I(ClayiumBlocks.compressedClays.get(12), 9), 1L, 0L);
        }
    };
    // TODO
    public static ClayiumRecipe fluidTransferMachine = new ClayiumRecipe() { public void init() {} };

    private static <T> List<T> L(T... elements) {
        return Arrays.asList(elements);
    }
    private static ItemStack I(Block block) {
        return I(new ItemBlock(block));
    }
    private static ItemStack I(Block block, int amount) {
        return I(new ItemBlock(block), amount);
    }
    private static ItemStack I(Item item) {
        return new ItemStack(item);
    }
    private static ItemStack I(Item itemIn, int amount) {
        return new ItemStack(itemIn, amount);
    }

    public static boolean hasResult(ClayiumRecipe recipes, List<ItemStack> stack) {
        for (RecipeElement recipe : recipes)
            if (recipe.getCondition().match(stack)) return true;

        return false;
    }

    public static boolean hasResult(ClayiumRecipe recipes, ItemStack... stacks) {
        return hasResult(recipes, L(stacks));
    }

    public static RecipeElement getRecipeElement(ClayiumRecipe recipes, NonNullList<ItemStack> referStacks, int method, int tier) {
        for (RecipeElement recipe : recipes) {
            if (recipe.getCondition().match(referStacks, method, tier)) {
                return recipe;
            }
        }

        return RecipeElement.FLAT;
    }
}
