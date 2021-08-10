package mods.clayium.item.crafting;

import mods.clayium.item.ClayiumItems;
import mods.clayium.util.ProgressRatedVariable;
import mods.clayium.util.UtilItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClayWorkTableRecipes {
    public static class RecipeElement {
        public RecipeElement(ItemStack material, int buttonID, ItemStack result, ItemStack result1, int cookTime, ItemStack requireTool) {
            this.material = material;
            this.buttonID = buttonID;
            this.result = result;
            this.result1 = result1;
            this.cookTime = ProgressRatedVariable.divideByProgressionRateI(cookTime);
            this.requireTool = requireTool;
        }

        public RecipeElement(ItemStack material, int buttonID, ItemStack result, ItemStack result1, int cookTime) {
            this(material, buttonID, result, result1, cookTime, ItemStack.EMPTY);
        }

        public boolean canSmelting(ItemStack itemstack) {
            return this.material.isItemEqual(itemstack)
//                    && (this.material.getMetadata() == 32767 || UtilItemStack.areDamageEqual(this.material, itemstack))
                    && this.material.getCount() <= itemstack.getCount();
        }

        public ItemStack getMaterial() {
            return this.material;
        }

        public int getButtonID() {
            return this.buttonID;
        }

        public ItemStack getResult() {
            return this.result;
        }

        public ItemStack getResult1() {
            return this.result1;
        }

        public int getCookTime() {
            return this.cookTime;
        }

        public boolean isSuitableTool(ItemStack tool) {
            return this.requireTool == ItemStack.EMPTY || this.requireTool == tool;
        }

        private final ItemStack material;
        private final int buttonID;
        private final ItemStack result;
        private final ItemStack result1;
        private final int cookTime;
        private final ItemStack requireTool;

        public static final RecipeElement FLAT = new RecipeElement(ItemStack.EMPTY, -1, ItemStack.EMPTY, ItemStack.EMPTY, 0);
    }

    private static final ClayWorkTableRecipes SMELTING_BASE = new ClayWorkTableRecipes();
    private final List<RecipeElement> recipes = new ArrayList<>();

    public static ClayWorkTableRecipes smelting() {
        return SMELTING_BASE;
    }

    private ClayWorkTableRecipes() {
        this.addRecipe(new ItemStack(Items.CLAY_BALL), 1, new ItemStack(ClayiumItems.clayStick), ItemStack.EMPTY, 4);
        this.addRecipe(new ItemStack(ClayiumItems.largeClayBall), 2, new ItemStack(ClayiumItems.clayDisc), ItemStack.EMPTY, 30);
        this.addRecipe(new ItemStack(ClayiumItems.largeClayBall), 3, new ItemStack(ClayiumItems.clayDisc), new ItemStack(Items.CLAY_BALL, 2), 4, new ItemStack(ClayiumItems.clayRollingPin));
        this.addRecipe(new ItemStack(ClayiumItems.largeClayBall), 1, new ItemStack(ClayiumItems.clayCylinder), ItemStack.EMPTY, 4);
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate), 2, new ItemStack(ClayiumItems.clayBlade), ItemStack.EMPTY, 10);
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate), 3, new ItemStack(ClayiumItems.clayBlade), new ItemStack(Items.CLAY_BALL, 2), 1, new ItemStack(ClayiumItems.clayRollingPin));
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate), 6, new ItemStack(ClayiumItems.clayStick, 4), ItemStack.EMPTY, 3, new ItemStack(ClayiumItems.claySlicer));
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate), 6, new ItemStack(ClayiumItems.clayStick, 4), ItemStack.EMPTY, 3, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 4, new ItemStack(ClayiumItems.clayPlate), new ItemStack(Items.CLAY_BALL, 2), 4, new ItemStack(ClayiumItems.claySlicer));
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 4, new ItemStack(ClayiumItems.clayPlate), new ItemStack(Items.CLAY_BALL, 2), 4, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 5, new ItemStack(ClayiumItems.clayRing), new ItemStack(ClayiumItems.smallClayDisc), 2, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.smallClayDisc), 5, new ItemStack(ClayiumItems.smallClayRing), new ItemStack(ClayiumItems.shortClayStick), 1, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.clayCylinder), 1, new ItemStack(ClayiumItems.clayNeedle), ItemStack.EMPTY, 3);
        this.addRecipe(new ItemStack(ClayiumItems.clayCylinder), 6, new ItemStack(ClayiumItems.smallClayDisc, 8), ItemStack.EMPTY, 7, new ItemStack(ClayiumItems.claySlicer));
        this.addRecipe(new ItemStack(ClayiumItems.clayCylinder), 6, new ItemStack(ClayiumItems.smallClayDisc, 8), ItemStack.EMPTY, 7, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 2, new ItemStack(ClayiumItems.rawClaySlicer), ItemStack.EMPTY, 15);
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 3, new ItemStack(ClayiumItems.rawClaySlicer), ItemStack.EMPTY, 2, new ItemStack(ClayiumItems.clayRollingPin));
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate, 6), 3, new ItemStack(ClayiumItems.largeClayPlate), ItemStack.EMPTY, 10, new ItemStack(ClayiumItems.clayRollingPin));
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate, 3), 1, new ItemStack(ClayiumItems.largeClayBall), ItemStack.EMPTY, 40);
    }

    public ItemStack getSmeltingResult(ItemStack itemstack) {
        for (RecipeElement recipe : this.recipes) {
            if (recipe.canSmelting(itemstack)) {
                return recipe.getResult();
            }
        }

        return ItemStack.EMPTY;
    }

    @Deprecated /* RecipeElement#canSmelting */
    private boolean canBeSmelted(ItemStack itemstack, ItemStack itemstack2) {
        return UtilItemStack.areItemEqual(itemstack2, itemstack) && (itemstack2.getMetadata() == 32767 || UtilItemStack.areDamageEqual(itemstack2, itemstack)) && itemstack2.getCount() <= itemstack.getCount();
    }

    public void addRecipe(ItemStack itemstack, int buttonId, ItemStack itemstack2, ItemStack itemstack3, int cookTime) {
        addRecipe(itemstack, buttonId, itemstack2, itemstack3, cookTime, ItemStack.EMPTY);
    }

    public void addRecipe(ItemStack itemstack, int buttonId, ItemStack itemstack2, ItemStack itemstack3, int cookTime, ItemStack requireTool) {
        this.recipes.add(new RecipeElement(itemstack, buttonId, itemstack2, itemstack3, cookTime, requireTool));
    }

    public RecipeElement getKneadingResultMap(ItemStack itemstack, int buttonId, ItemStack tool) {
        RecipeElement entry_ = RecipeElement.FLAT;
        int maxStackSize = 0;

        for (RecipeElement recipe : this.recipes) {
            if (recipe.canSmelting(itemstack)
                    && recipe.getButtonID() == buttonId
                    && recipe.getMaterial().getCount() > maxStackSize
                    && maxStackSize <= itemstack.getCount()
                    && recipe.isSuitableTool(tool)) {
                entry_ = recipe;
                maxStackSize = recipe.getMaterial().getCount();
            }
        }

        return entry_;
    }

    public RecipeElement getKneadingResultMap(ItemStack itemstack, int buttonId) {
        return getKneadingResultMap(itemstack, buttonId, ItemStack.EMPTY);
    }

    public int getConsumedStackSize(ItemStack itemstack, int buttonId) {
        return getConsumedStackSize(itemstack, buttonId, ItemStack.EMPTY);
    }

    public int getConsumedStackSize(ItemStack itemstack, int buttonId, ItemStack tool) {
        return this.getConsumedStackSize(this.getKneadingResultMap(itemstack, buttonId, tool));
    }

    public int getConsumedStackSize(RecipeElement recipe) {
        return recipe.getMaterial().getCount();
    }

    public ItemStack getKneadingResult(ItemStack material, int buttonId) {
        return this.getKneadingResult(material, buttonId, ItemStack.EMPTY);
    }

    public ItemStack getKneadingResult(ItemStack material, int buttonId, ItemStack tool) {
        return this.getKneadingResult(this.getKneadingResultMap(material, buttonId, tool));
    }

    public ItemStack getKneadingResult(RecipeElement recipe) {
        return recipe.getResult();
    }

    public ItemStack getKneadingResult1(ItemStack material, int buttonId) {
        return this.getKneadingResult1(material, buttonId, ItemStack.EMPTY);
    }

    public ItemStack getKneadingResult1(ItemStack material, int buttonId, ItemStack tool) {
        return this.getKneadingResult1(this.getKneadingResultMap(material, buttonId, tool));
    }

    public ItemStack getKneadingResult1(RecipeElement recipe) {
        return recipe.getResult();
    }

    public int getKneadingTime(ItemStack material, int buttonId) {
        return this.getKneadingTime(material, buttonId, ItemStack.EMPTY);
    }

    public int getKneadingTime(ItemStack material, int buttonId, ItemStack tool) {
        return this.getKneadingTime(this.getKneadingResultMap(material, buttonId, tool));
    }

    public int getKneadingTime(RecipeElement recipe) {
        return recipe.getCookTime();
    }

    public boolean hasKneadingResult(ItemStack material) {
        boolean flag = false;

        for (RecipeElement recipe : this.recipes) {
            if (recipe.canSmelting(material)) {
                flag = true;
            }
        }

        return flag;
    }
}
