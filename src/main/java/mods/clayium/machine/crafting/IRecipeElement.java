package mods.clayium.machine.crafting;

import mezz.jei.api.recipe.IRecipeWrapper;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.OreDictionaryStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public interface IRecipeElement extends IRecipe, IRecipeWrapper {
    int getTier();
    default int getMethod() { return -1; }
    List<ItemStack> getResults();
    long getEnergy();
    long getTime();

    boolean isFlat();

    default boolean isCraftable(ItemStack itemstack, int tier) {
        if (getTier() > tier) {
            return false;
        }

        for (Ingredient ingredient : this.getIngredients()) {
            if (ingredient.apply(itemstack)) {
                return true;
            }
        }
        return false;
    }

    default boolean match(List<ItemStack> itemStacksIn, int methodIn, int tierIn) {
        if (this.getMethod() != methodIn || this.getTier() > tierIn || this.getIngredients().size() > itemStacksIn.size()) return false;

        for (int i = 0; i < this.getIngredients().size(); i++)
            if (!this.getIngredients().get(i).apply(itemStacksIn.get(i)))
                return false;

        return true;
    }

    static boolean inclusion(ItemStack self, ItemStack comes) {
        if (self.isEmpty()) return true;
        if (UtilItemStack.areTypeEqual(self, comes) && self.getCount() <= comes.getCount()) return true;
        if (self.getHasSubtypes() && comes.getHasSubtypes()) return self.isItemEqual(comes);
        return self.getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }

    static boolean canBeCrafted(ItemStack itemstack, ItemStack itemstack2, boolean sizeCheck) {
        if (itemstack2 == ItemStack.EMPTY) return true;
        if (itemstack == ItemStack.EMPTY) return false;

        return ItemStack.areItemsEqual(itemstack2, itemstack)
                && (itemstack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemstack.getItemDamage() == OreDictionary.WILDCARD_VALUE
                || UtilItemStack.areDamageEqual(itemstack2, itemstack))
                && (!sizeCheck || itemstack2.getCount() <= itemstack.getCount());
    }

    static boolean canBeCrafted(ItemStack itemstack, ItemStack itemstack2) {
        return canBeCrafted(itemstack, itemstack2, true);
    }

    static boolean canBeCraftedOD(ItemStack itemstack, Object object, boolean sizeCheck) {
        if (object == null) return true;
        if (itemstack == ItemStack.EMPTY) return false;
        if (object instanceof String) {
            return UtilItemStack.hasOreName(itemstack, (String) object);
        }
        if (object instanceof OreDictionaryStack) {
            if (sizeCheck && ((OreDictionaryStack) object).getStackSize() > itemstack.getCount())
                return false;
            return UtilItemStack.hasOreName(itemstack, ((OreDictionaryStack) object).getId());
        }
        if (object instanceof ItemStack)
            return canBeCrafted(itemstack, (ItemStack) object, sizeCheck);
        if (object instanceof IItemPattern)
            return ((IItemPattern) object).match(itemstack, sizeCheck);

        return false;
    }

    static boolean canBeCraftedOD(ItemStack itemstack, Object object) {
        return canBeCraftedOD(itemstack, object, true);
    }

    static boolean canBeCraftedODs(Object stackingred, Object recipeingred, boolean sizeCheck) {
        if (recipeingred == null) return true;
        if (stackingred == null) return false;
        if (stackingred instanceof ItemStack) {
            return canBeCraftedOD((ItemStack) stackingred, recipeingred, sizeCheck);
        }
        if (stackingred instanceof String || stackingred instanceof OreDictionaryStack) {
            int oreid = (stackingred instanceof OreDictionaryStack) ? ((OreDictionaryStack) stackingred).getId() : OreDictionary.getOreID((String) stackingred);
            int stackSize = (stackingred instanceof OreDictionaryStack) ? ((OreDictionaryStack) stackingred).getStackSize() : 1;
            for (ItemStack item : OreDictionary.getOres(String.valueOf(oreid))) {
                ItemStack item0 = item.copy();
                item0.setCount(stackSize);
                if (canBeCraftedOD(item0, recipeingred, sizeCheck)) {
                    return true;
                }
            }
        }
        if (stackingred instanceof IItemPattern) {
            return ((IItemPattern) stackingred).hasIntersection(convert(recipeingred), sizeCheck);
        }
        return false;
    }

    static IItemPattern convert(Object ingred) {
    /* TODO still added ItemPattern
        if (ingred instanceof ItemStack) {
            return new ItemPatternItemStack((ItemStack) ingred);
        }
        if (ingred instanceof OreDictionaryStack) {
            return new ItemPatternOreDictionary(((OreDictionaryStack) ingred).id, ((OreDictionaryStack) ingred).stackSize);
        }
        if (ingred instanceof String) {
            return new ItemPatternOreDictionary((String) ingred, 1);
        }
    */
        if (ingred instanceof IItemPattern) {
            return (IItemPattern) ingred;
        }
        return null;
    }
}
