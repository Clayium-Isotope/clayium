package mods.clayium.util;

import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.OreDictionaryStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.stream.Collectors;

public class UtilItemStack {
    public static boolean areItemDamageEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return ItemStack.areItemsEqual(itemstack1, itemstack2) && areDamageEqual(itemstack1, itemstack2);
    }

    public static boolean areItemDamageEqualOrDamageable(ItemStack itemstack1, ItemStack itemstack2) {
        return ItemStack.areItemsEqual(itemstack1, itemstack2) && (areDamageEqual(itemstack1, itemstack2) || itemstack1.getItem().isDamageable());
    }

    public static boolean areTypeEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return areItemDamageEqual(itemstack1, itemstack2) && ItemStack.areItemStackTagsEqual(itemstack1, itemstack2);
    }

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

    public static boolean haveSameOD(ItemStack itemstack1, ItemStack itemstack2) {
        for (int id : OreDictionary.getOreIDs(itemstack1))
            for (int id1 : OreDictionary.getOreIDs(itemstack2))
                if (id1 == id)
                    return true;

        return false;
    }

    public static boolean hasOreName(ItemStack itemstack, String orename) {
        for (int i : OreDictionary.getOreIDs(itemstack))
            if (OreDictionary.getOreName(i).equals(orename))
                return true;

        return false;
    }

    public static boolean hasOreName(ItemStack itemstack, int oreid) {
        for (int i : OreDictionary.getOreIDs(itemstack))
            if (i == oreid)
                return true;

        return false;
    }

    public static String[] getOreNames(ItemStack itemstack) {
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
        if (item == ItemStack.EMPTY) return 0;

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

    public static List<ItemStack> getItemsFromTag(String name, ItemStack item) {
        return item != null && item.hasTagCompound() ? getItemsFromTag(name, item.getTagCompound()) : new ArrayList<>();
    }

    public static List<ItemStack> getItemsFromTag(String name, NBTTagCompound tag) {
        return tag != null ? tagList2ItemList(tag.getTagList(name, 10)) : new ArrayList<>();
    }

    public static void setItemsToTag(String name, List<ItemStack> items, NBTTagCompound tag) {
        if (tag == null) tag = new NBTTagCompound();
        tag.setTag(name, items2TagList(items));
    }

    public static List<ItemStack> tagList2ItemList(NBTTagList tagList) {
        List<ItemStack> res = new ArrayList<>();
        if (tagList == null) return res;

        for(int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
            short byte0 = tagCompound1.getShort("Slot");

            while(res.size() <= byte0) {
                res.add(ItemStack.EMPTY);
            }

            res.set(byte0, new ItemStack(tagCompound1));
        }

        return res;
    }

    public static ItemStack[] tagList2Items(NBTTagList tagList) {
        return tagList2ItemList(tagList).toArray(new ItemStack[0]);
    }

    public static void tagList2Items(NBTTagList tagList, List<ItemStack> itemstacks) {
        if (tagList == null || itemstacks == null) return;

        for(int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
            short byte0 = tagCompound1.getShort("Slot");
            if (byte0 >= 0 && byte0 < itemstacks.size())
                itemstacks.set(byte0, new ItemStack(tagCompound1));
        }
    }

    public static NBTTagList items2TagList(List<ItemStack> items) {
        NBTTagList tagList = new NBTTagList();
        if (items == null) return tagList;

        for(int i = 0; i < items.size(); ++i) {
            if (items.get(i) != null) {
                NBTTagCompound tagCompound1 = new NBTTagCompound();
                tagCompound1.setShort("Slot", (short) i);
                items.get(i).writeToNBT(tagCompound1);
                tagList.appendTag(tagCompound1);
            }
        }

        return tagList;
    }
}
