package mods.clayium.item.crafting;

import mods.clayium.machine.ClayWorkTable.TileEntityClayWorkTable.ClayWorkTableSlots;
import mods.clayium.item.ClayiumItems;
import mods.clayium.util.ProgressRatedVariable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class ClayWorkTableRecipes {
    public static class RecipeElement {
        public RecipeElement(ItemStack material, int button, ItemStack product, ItemStack change, int kneadTime, ItemStack requireTool) {
            this.material = material;
            this.button = button;
            this.product = product;
            this.change = change;
            this.kneadTime = ProgressRatedVariable.divideByProgressionRateI(kneadTime);
            this.requireTool = requireTool;
        }

        public RecipeElement(ItemStack material, int button, ItemStack product, ItemStack change, int kneadTime) {
            this(material, button, product, change, kneadTime, ItemStack.EMPTY);
        }

        public boolean canKneading(ItemStack itemstack) {
            return this.material.isItemEqual(itemstack)
                    //                    && (this.material.getMetadata() == 32767 || UtilItemStack.areDamageEqual(this.material, itemstack))
                    && this.material.getCount() <= itemstack.getCount();
        }

        public ItemStack getMaterial() {
            return this.material;
        }

        public int getButton() {
            return this.button;
        }

        public ItemStack getProduct() {
            return this.product;
        }

        public ItemStack getChange() {
            return this.change;
        }

        public int getKneadTime() {
            return this.kneadTime;
        }

        public ItemStack getRequireTool() {
            return this.requireTool;
        }

        public boolean isSuitableTool(ItemStack tool) {
            return this.requireTool.isEmpty() || this.requireTool == tool;
        }

        private final ItemStack material;
        private final int button;
        private final ItemStack product;
        private final ItemStack change;
        private final int kneadTime;
        private final ItemStack requireTool;

        public static final RecipeElement FLAT = new RecipeElement(ItemStack.EMPTY, -1, ItemStack.EMPTY, ItemStack.EMPTY, 0);
    }

    private static final ClayWorkTableRecipes KNEADING_BASE = new ClayWorkTableRecipes();
    private final List<RecipeElement> recipes = new ArrayList<>();

    public static ClayWorkTableRecipes instance() {
        return KNEADING_BASE;
    }

    private ClayWorkTableRecipes() {
        this.addRecipe(new ItemStack(Items.CLAY_BALL), 0, new ItemStack(ClayiumItems.clayStick), ItemStack.EMPTY, 4);
        this.addRecipe(new ItemStack(ClayiumItems.largeClayBall), 1, new ItemStack(ClayiumItems.clayDisc), ItemStack.EMPTY, 30);
        this.addRecipe(new ItemStack(ClayiumItems.largeClayBall), 2, new ItemStack(ClayiumItems.clayDisc), new ItemStack(Items.CLAY_BALL, 2), 4, new ItemStack(ClayiumItems.clayRollingPin));
        this.addRecipe(new ItemStack(ClayiumItems.largeClayBall), 0, new ItemStack(ClayiumItems.clayCylinder), ItemStack.EMPTY, 4);
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate), 1, new ItemStack(ClayiumItems.clayBlade), ItemStack.EMPTY, 10);
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate), 2, new ItemStack(ClayiumItems.clayBlade), new ItemStack(Items.CLAY_BALL, 2), 1, new ItemStack(ClayiumItems.clayRollingPin));
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate), 5, new ItemStack(ClayiumItems.clayStick, 4), ItemStack.EMPTY, 3, new ItemStack(ClayiumItems.claySlicer));
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate), 5, new ItemStack(ClayiumItems.clayStick, 4), ItemStack.EMPTY, 3, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 3, new ItemStack(ClayiumItems.clayPlate), new ItemStack(Items.CLAY_BALL, 2), 4, new ItemStack(ClayiumItems.claySlicer));
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 3, new ItemStack(ClayiumItems.clayPlate), new ItemStack(Items.CLAY_BALL, 2), 4, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 4, new ItemStack(ClayiumItems.clayRing), new ItemStack(ClayiumItems.smallClayDisc), 2, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.smallClayDisc), 4, new ItemStack(ClayiumItems.smallClayRing), new ItemStack(ClayiumItems.shortClayStick), 1, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.clayCylinder), 0, new ItemStack(ClayiumItems.clayNeedle), ItemStack.EMPTY, 3);
        this.addRecipe(new ItemStack(ClayiumItems.clayCylinder), 5, new ItemStack(ClayiumItems.smallClayDisc, 8), ItemStack.EMPTY, 7, new ItemStack(ClayiumItems.claySlicer));
        this.addRecipe(new ItemStack(ClayiumItems.clayCylinder), 5, new ItemStack(ClayiumItems.smallClayDisc, 8), ItemStack.EMPTY, 7, new ItemStack(ClayiumItems.claySpatula));
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 2, new ItemStack(ClayiumItems.rawClaySlicer), ItemStack.EMPTY, 15);
        this.addRecipe(new ItemStack(ClayiumItems.clayDisc), 3, new ItemStack(ClayiumItems.rawClaySlicer), ItemStack.EMPTY, 2, new ItemStack(ClayiumItems.clayRollingPin));
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate, 6), 3, new ItemStack(ClayiumItems.largeClayPlate), ItemStack.EMPTY, 10, new ItemStack(ClayiumItems.clayRollingPin));
        this.addRecipe(new ItemStack(ClayiumItems.clayPlate, 3), 0, new ItemStack(ClayiumItems.largeClayBall), ItemStack.EMPTY, 40);
    }

    private void addRecipe(ItemStack material, int button, ItemStack product, ItemStack change, int cookTime) {
        addRecipe(material, button, product, change, cookTime, ItemStack.EMPTY);
    }

    private void addRecipe(ItemStack material, int button, ItemStack product, ItemStack change, int cookTime, ItemStack requireTool) {
        recipes.add(new RecipeElement(material, button, product, change, cookTime, requireTool));
    }

    public RecipeElement getKneadingResultMap(int button, NonNullList<ItemStack> referItemStacks) {
        RecipeElement entry_ = RecipeElement.FLAT;
        int maxStackSize = 0;

        for (RecipeElement recipe : recipes) {
            if (recipe.canKneading(referItemStacks.get(ClayWorkTableSlots.MATERIAL.ordinal()))
                    && recipe.getButton() == button
                    && recipe.getMaterial().getCount() > maxStackSize
                    && recipe.getMaterial().getCount() <= referItemStacks.get(ClayWorkTableSlots.MATERIAL.ordinal()).getCount()
                    && recipe.isSuitableTool(referItemStacks.get(ClayWorkTableSlots.TOOL.ordinal()))) {
                entry_ = recipe;
                maxStackSize = recipe.getMaterial().getCount();
            }
        }

        return entry_;
    }

    public boolean hasKneadingResult(ItemStack stack) {
        for (RecipeElement recipe : recipes) {
            if (recipe.material.getItem() == stack.getItem()) {
                return true;
            }
        }

        return false;
    }
}
