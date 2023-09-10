package mods.clayium.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.OreDictionaryStack;
import mods.clayium.util.crafting.SpeciesIngredientFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UtilItemStack {
    /**
     * Returns true if the same item, damage btw the ones.
     * In other words, compare them without NBT and size
     *
     * If one/both of args be EMPTY, returns false owing to ItemStack.areItemsEqual
     */
    public static boolean areItemDamageEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return ItemStack.areItemsEqual(itemstack1, itemstack2) && areDamageEqual(itemstack1, itemstack2);
    }

    public static boolean areItemDamageEqualOrDamageable(ItemStack itemstack1, ItemStack itemstack2) {
        return ItemStack.areItemsEqual(itemstack1, itemstack2) && (areDamageEqual(itemstack1, itemstack2) || itemstack1.getItem().isDamageable());
    }

    /**
     * Returns true if the same item, damage and NBT btw the ones.
     * In other words, compare them without size
     *
     * TODO: rename to areItemDamageTagEqual
     */
    public static boolean areTypeEqual(@Nullable ItemStack itemstack1, @Nullable ItemStack itemstack2) {
        if (itemstack1 == null || itemstack2 == null) return false;
        return areItemDamageEqual(itemstack1, itemstack2) && ItemStack.areItemStackTagsEqual(itemstack1, itemstack2);
    }

    /**
     * Returns true if the same item, damage, NBT and size btw the ones.
     */
    public static boolean areStackEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return areTypeEqual(itemstack1, itemstack2) && areSizeEqual(itemstack1, itemstack2);
    }

    @Deprecated // Use ItemStack.areItemsEqual
    public static boolean areItemEqual(ItemStack itemstack1, ItemStack itemstack2) {
        if (itemstack1 != ItemStack.EMPTY && itemstack2 != ItemStack.EMPTY) {
            return itemstack1.isItemEqual(itemstack2);
        }

        return itemstack1 == ItemStack.EMPTY && itemstack2 == ItemStack.EMPTY;
    }

    public static boolean areDamageEqual(ItemStack itemstack1, ItemStack itemstack2) {
        if (itemstack1 != ItemStack.EMPTY && itemstack2 != ItemStack.EMPTY) {
            return itemstack1.getMetadata() == itemstack2.getMetadata();
        } else {
            return itemstack1 == ItemStack.EMPTY && itemstack2 == ItemStack.EMPTY;
        }
    }

    public static boolean areSizeEqual(ItemStack itemstack1, ItemStack itemstack2) {
        if (itemstack1 != ItemStack.EMPTY && itemstack2 != ItemStack.EMPTY) {
            return itemstack1.getCount() == itemstack2.getCount();
        } else {
            return itemstack1 == ItemStack.EMPTY && itemstack2 == ItemStack.EMPTY;
        }
    }

    @Deprecated // Use ItemStack.areItemStackTagsEqual
    public static boolean areTagEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return ItemStack.areItemStackTagsEqual(itemstack1, itemstack2);
    }

    /**
     * @return true when stacks' contain item and tag are equal.
     * Several params are ignored: damage and size.
     *
     * TODO: rename to areItemTagEqual
     */
    public static boolean areKindEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return ItemStack.areItemsEqual(itemstack1, itemstack2) && ItemStack.areItemStackTagsEqual(itemstack1, itemstack2);
    }

    public static boolean haveSameOD(ItemStack itemstack1, ItemStack itemstack2) {
        if (itemstack1.isEmpty() || itemstack2.isEmpty()) return false;

        for (int id : OreDictionary.getOreIDs(itemstack1))
            for (int id1 : OreDictionary.getOreIDs(itemstack2))
                if (id1 == id)
                    return true;

        return false;
    }

    public static boolean hasOreName(ItemStack itemstack, String orename) {
        if (itemstack.isEmpty()) return false;

        for (int i : OreDictionary.getOreIDs(itemstack))
            if (OreDictionary.getOreName(i).equals(orename))
                return true;

        return false;
    }

    public static boolean hasOreName(ItemStack itemstack, int oreid) {
        if (itemstack.isEmpty()) return false;

        for (int i : OreDictionary.getOreIDs(itemstack))
            if (i == oreid)
                return true;

        return false;
    }

    public static String[] getOreNames(ItemStack itemstack) {
        if (itemstack.isEmpty()) return new String[0];

        int[] ids = OreDictionary.getOreIDs(itemstack);
        String[] res = new String[ids.length];

        for(int i = 0; i < ids.length; ++i) {
            res[i] = OreDictionary.getOreName(ids[i]);
        }

        return res;
    }

    /*
     * public static GameRegistry.UniqueIdentifier findUniqueIdentifierFor(Item item) {
     *     if (item == null) return null;
     *     if (item instanceof ItemBlock)
     *         return GameRegistry.findUniqueIdentifierFor(((ItemBlock) item).field_150939_a);
     *     return GameRegistry.findUniqueIdentifierFor(item);
     * }
     */

    public static int getItemStackHashCode(ItemStack item) {
        if (item.isEmpty()) return 0;

        int result = 1;
        result = 31 * result + item.getItem().getRegistryName().hashCode();
        result = 31 * result + item.getMetadata();
        result = 31 * result + item.getCount();
        result = 31 * result + (item.getTagCompound() == null ? 0 : item.getTagCompound().hashCode());
        return result;
    }

    public static int getItemStackHashCode(Iterable<ItemStack> items) {
        if (items == null) return 0;

        int result = 1;
        for (ItemStack item : items) {
            result = 31 * result + (!item.isEmpty() ? getItemStackHashCode(item) : item.hashCode());
        }

        return result;
    }

    public static boolean areStacksEqual(List<ItemStack> items1, List<ItemStack> items2) {
        if (items1 == items2) return true;
        if (items1.isEmpty() || items2.isEmpty()) return false;
        if (items1.size() != items2.size()) return false;

        for (int i = 0; i < items1.size(); i++) {
            if (!ItemStack.areItemStacksEqual(items1.get(i), items2.get(i)))
                return false;
        }

        return true;
    }

    public static List<ItemStack> object2ItemStacks(Object object) {
        if (object instanceof ItemStack) {
            return Arrays.asList((ItemStack) object);
        }
        if (object instanceof List) {
            return ((List<?>) object).stream()
                    .map(ItemStack.class::cast)
                    .collect(Collectors.toList());
        }
        if (object instanceof OreDictionaryStack) {
            List<ItemStack> stacks = OreDictionary.getOres(((OreDictionaryStack) object).getOreName());
            List<ItemStack> stacks1 = new ArrayList<>(stacks.size());

            for (int i = 0; i < stacks.size(); ++i) {
                stacks1.set(i, stacks.get(i).copy());
                stacks1.get(i).setCount(((OreDictionaryStack) object).getStackSize());
            }

            return stacks1;
        }
        if (object instanceof IItemPattern) {
            return ((IItemPattern) object).toItemStacks();
        }
        if (object instanceof String) {
            return object2ItemStacks(new OreDictionaryStack((String) object, 1));
        }
        return new ArrayList<>();
    }

    public static List<ItemStack> getItemsFromTag(ItemStack item) {
        return item != null && item.hasTagCompound() ? getItemsFromTag(item.getTagCompound()) : new ArrayList<>();
    }

    public static List<ItemStack> getItemsFromTag(NBTTagCompound tag) {
        return tag != null ? UtilCollect.tagList2ItemList(tag.getTagList("Items", Constants.NBT.TAG_COMPOUND)) : new ArrayList<>();
    }

    public static void setItemsToTag(NBTTagCompound tag, List<ItemStack> items) {
        if (tag == null) tag = new NBTTagCompound();
        tag.setTag("Items", UtilCollect.items2TagList(items));
    }

    public static ItemStack getItemStack(final JsonObject json, final JsonContext context)
    {
        final String type = context.appendModId(json.get("type").getAsString());
        if ("minecraft:item".equals(type)) return CraftingHelper.getItemStack(json, context);
        if ("clayium:species".equals(type)) return SpeciesIngredientFactory.getStack(json);
        throw new JsonSyntaxException("[UtilItemStack] Couldn't interpret the output type: " + type);
    }
}
