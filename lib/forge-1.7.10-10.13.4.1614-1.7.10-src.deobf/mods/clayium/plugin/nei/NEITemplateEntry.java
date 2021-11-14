package mods.clayium.plugin.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.clayium.gui.IFunctionalDrawer;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.crafting.Recipes;
import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class NEITemplateEntry extends NEIEntryWithFD implements INEIRecipeEntry {
    protected List<PositionedStack> inputs;

    protected List<PositionedStack> results;

    protected Object[] inputObjects;

    public NEITemplateEntry(Object[] inputs, ItemStack[] results) {
        this(inputs, NEITemp.generateIngredientPositionedStacks(inputs), NEITemp.generateResultPositionedStacks(results));
    }

    public NEITemplateEntry(Object[] inputObjects, List<PositionedStack> inputs, List<PositionedStack> results, boolean drawProgressBar) {
        super(new NEITemp() {
            @Override
            public Comparator<CachedRecipe> getComparator() {
                return null;
            }

            @Override
            public Iterable<INEIRecipeEntry> getMatchedSet() {
                return null;
            }

            @Override
            public String getGuiTexture() {
                return null;
            }
        });
        this.inputObjects = inputObjects;
        this.inputs = inputs;
        this.results = results;

        List<IFunctionalDrawer<Object>> list = getFDList();
        if (drawProgressBar)
            list.add(NEITemp.drawerProgressBar);
        list.add(NEITemp.drawerTier);
        list.add(NEITemp.drawerEnergy);
    }

    public NEITemplateEntry(Object[] inputObjects, List<PositionedStack> inputs, List<PositionedStack> results) {
        this(inputObjects, inputs, results, true);
    }

    public PositionedStack getResult() {
        return null;
    }

    public List<PositionedStack> getIngredients() {
        return getCycledIngredients(neiTemp.cycleticks / 10, this.inputs);
    }

    public List<PositionedStack> getIngredientsToSort() {
        return this.inputs;
    }

    public List<PositionedStack> getOtherStacks() {
        return this.results;
    }

    public void computeVisuals() {
        for (PositionedStack p : this.inputs) {
            NEITemp.generatePermutationsFixed(p);
        }
    }

    public TemplateRecipeHandler.CachedRecipe asCachedRecipe() {
        return this;
    }

    public boolean matchForUsageRecipe(ItemStack ingredient) {
        if (ingredient == null)
            return false;
        ItemStack material2 = ingredient.copy();
        material2.stackSize = material2.getMaxStackSize();
        for (Object material1 : this.inputObjects) {
            if (material1 != null && Recipes.canBeCraftedOD(material2, material1)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchForCraftingRecipe(ItemStack result) {
        for (PositionedStack result0 : getOtherStacks()) {
            for (ItemStack result_ : result0.items) {
                if (result_ != null
                        && (UtilItemStack.areItemDamageEqualOrDamageable(result_, result)
                        || UtilItemStack.haveSameOD(result_, result))) {
                    return true;
                }
            }
        }
        return false;
    }
}
