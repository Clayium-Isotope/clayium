package mods.clayium.machine.crafting;

import mezz.jei.api.recipe.IRecipeWrapper;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.OreDictionaryStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public interface IRecipeElement extends IRecipe, IRecipeWrapper {
    List<ItemStack> getMaterials();
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

        for (ItemStack stack : getMaterials()) {
//            if (canBeCraftedODs(itemstack, stack, false)) {
//                return true;
//            }

            if (canBeCrafted(itemstack, stack, false)) {
                return true;
            }
        }
        return false;
    }

    default boolean match(List<ItemStack> itemStacksIn, int methodIn, int tierIn) {
        if (getMethod() != methodIn || getTier() > tierIn || getMaterials().size() > itemStacksIn.size()) return false;

        for (int i = 0; i < getMaterials().size(); i++)
            if (!inclusion(getMaterials().get(i), itemStacksIn.get(i)))
                return false;

        return true;
    }

    static boolean inclusion(ItemStack self, ItemStack comes) {
        if (self.isEmpty()) return true;
        if (UtilItemStack.areTypeEqual(self, comes) && self.getCount() <= comes.getCount()) return true;
        if (self.getHasSubtypes() && comes.getHasSubtypes()) return self.isItemEqual(comes);
        return self.getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }

    default int[] getStackSizes(ItemStack ...items) {
        int[] sizes = new int[items.length];
        for (int i = 0; i < items.length && i < getMaterials().size(); i++) {
            sizes[i] = getStackSize(getMaterials().get(i), items[i]);
        }
        return sizes;
    }

    static int getStackSize(Object recipe, ItemStack item) {
        if (recipe instanceof IItemPattern) {
            if (item == null) {
                List<ItemStack> items = ((IItemPattern) recipe).toItemStacks();
                if (items != null && items.size() >= 1)
                    item = items.get(0);
            }
            return ((IItemPattern) recipe).getStackSize(item);
        }
        return getStackSize(recipe);
    }

    static int getStackSize(Object item) {
        if (item instanceof ItemStack) return ((ItemStack) item).getCount();
//        if (item instanceof OreDictionaryStack) return ((OreDictionaryStack) item).stackSize;
        if (item instanceof String) return 1;

        return 0;
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
