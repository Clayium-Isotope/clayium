package mods.clayium.util;

import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.OreDictionaryStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class UtilItemStack {
    public UtilItemStack() {
    }

    public static boolean areItemDamageEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return areItemEqual(itemstack1, itemstack2) && areDamageEqual(itemstack1, itemstack2);
    }

    public static boolean areItemDamageEqualOrDamageable(ItemStack itemstack1, ItemStack itemstack2) {
        return areItemEqual(itemstack1, itemstack2) && (areDamageEqual(itemstack1, itemstack2) || itemstack1.getItem().isDamageable());
    }

    public static boolean areTypeEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return areItemDamageEqual(itemstack1, itemstack2) && areTagEqual(itemstack1, itemstack2);
    }

    public static boolean areStackEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return areTypeEqual(itemstack1, itemstack2) && areSizeEqual(itemstack1, itemstack2);
    }

    public static boolean areItemEqual(ItemStack itemstack1, ItemStack itemstack2) {
        if (itemstack1 != null && itemstack2 != null) {
            return itemstack1.getItem() == itemstack2.getItem();
        } else {
            return itemstack1 == null && itemstack2 == null;
        }
    }

    public static boolean areDamageEqual(ItemStack itemstack1, ItemStack itemstack2) {
        if (itemstack1 != null && itemstack2 != null) {
            return itemstack1.getMetadata() == itemstack2.getMetadata();
        } else {
            return itemstack1 == null && itemstack2 == null;
        }
    }

    public static boolean areSizeEqual(ItemStack itemstack1, ItemStack itemstack2) {
        if (itemstack1 != null && itemstack2 != null) {
            return itemstack1.getCount() == itemstack2.getCount();
        } else {
            return itemstack1 == null && itemstack2 == null;
        }
    }

    public static boolean areTagEqual(ItemStack itemstack1, ItemStack itemstack2) {
        return ItemStack.areItemStackTagsEqual(itemstack1, itemstack2);
    }

    public static boolean haveSameOD(ItemStack itemstack1, ItemStack itemstack2) {
        int[] ids = OreDictionary.getOreIDs(itemstack1);
        int[] ids1 = OreDictionary.getOreIDs(itemstack2);

        for (int id : ids) {
            for (int id1 : ids1) {
                if (id1 == id) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasOreName(ItemStack itemstack, String orename) {
        int[] var2 = OreDictionary.getOreIDs(itemstack);

        for (int i : var2) {
            if (OreDictionary.getOreName(i).equals(orename)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasOreName(ItemStack itemstack, int oreid) {
        int[] var2 = OreDictionary.getOreIDs(itemstack);

        for (int i : var2) {
            if (i == oreid) {
                return true;
            }
        }

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

    public static int getItemStackHashCode(ItemStack item) {
        if (item == null) {
            return 0;
        }

        int result = 1;
        result = 31 * result + item.getItem().getRegistryName().hashCode();
        result = 31 * result + item.getMetadata();
        result = 31 * result + item.getCount();
        result = 31 * result + (item.getTagCompound() == null ? 0 : item.getTagCompound().hashCode());
        return result;
    }

    public static int getItemStackHashCode(Iterable<ItemStack> items) {
        if (items == null) {
            return 0;
        }

        int result = 1;

        ItemStack item;
        for(Iterator<ItemStack> var3 = items.iterator(); var3.hasNext(); result = 31 * result + (item != null ? getItemStackHashCode(item) : 0)) {
            item = var3.next();
        }

        return result;
    }

    public static int getItemStackHashCode(ItemStack[] items) {
        return getItemStackHashCode(Arrays.asList(items));
    }

    public static boolean areStacksEqual(Object[] items1, Object[] items2) {
        if (items1 == null) {
            return items2 == null;
        }
        if (items2 == null) {
            return false;
        }
        if (items1.length != items2.length) {
            return false;
        }

        for(int i = 0; i < items1.length; ++i) {
            if (items1[i] == null && items2[i] != null || items1[i] instanceof ItemStack && items2[i] instanceof ItemStack && !areStackEqual((ItemStack)items1[i], (ItemStack)items2[i]) || items1[i] != items2[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean areStacksEqual(Collection<ItemStack> items1, Collection<ItemStack> items2) {
        return items1 == null ? items2 == null : (items2 != null && areStacksEqual(items1.toArray(new ItemStack[0]), items2.toArray(new ItemStack[0])));
    }

    public static ItemStack[] object2ItemStacks(Object object) {
        if (object instanceof ItemStack) {
            return new ItemStack[]{(ItemStack)object};
        }
        if (object instanceof ItemStack[]) {
            return (ItemStack[])object;
        }
        if (!(object instanceof OreDictionaryStack)) {
            if (object instanceof String) {
                return object2ItemStacks(new OreDictionaryStack((String) object, 1));
            }

            return object instanceof IItemPattern ? ((IItemPattern) object).toItemStacks() : null;
        }

        OreDictionaryStack odstack = (OreDictionaryStack)object;
        ItemStack[] stacks = OreDictionary.getOres(odstack.getOreName()).toArray(new ItemStack[0]);
        ItemStack[] stacks1 = new ItemStack[stacks.length];

        for(int i = 0; i < stacks.length; ++i) {
            stacks1[i] = stacks[i].copy();
            stacks1[i].setCount(odstack.stackSize);
        }

        return stacks1;
    }

    public static ItemStack[] getItemsFromTag(String name, ItemStack item) {
        return item != null && item.hasTagCompound() ? getItemsFromTag(name, item.getTagCompound()) : null;
    }

    public static ItemStack[] getItemsFromTag(String name, NBTTagCompound tag) {
        return tag == null ? null : tagList2Items(tag.getTagList(name, 10));
    }

    public static void setItemsToTag(String name, ItemStack[] items, NBTTagCompound tag) {
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        tag.setTag(name, items2TagList(items));
    }

    public static List<ItemStack> tagList2ItemList(NBTTagList tagList) {
        if (tagList == null) {
            return null;
        }

        List<ItemStack> res = new ArrayList<>();

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
        List<ItemStack> list = tagList2ItemList(tagList);
        return list == null ? null : list.toArray(new ItemStack[0]);
    }

    public static void tagList2Items(NBTTagList tagList, ItemStack[] itemstacks) {
        if (tagList != null && itemstacks != null) {
            for(int i = 0; i < tagList.tagCount(); ++i) {
                NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
                short byte0 = tagCompound1.getShort("Slot");
                if (byte0 >= 0 && byte0 < itemstacks.length) {
                    itemstacks[byte0] = new ItemStack(tagCompound1);
                }
            }

        }
    }

    public static NBTTagList items2TagList(ItemStack[] items) {
        NBTTagList tagList = new NBTTagList();
        if (items != null) {
            for(int i = 0; i < items.length; ++i) {
                if (items[i] != null) {
                    NBTTagCompound tagCompound1 = new NBTTagCompound();
                    tagCompound1.setShort("Slot", (short)i);
                    items[i].writeToNBT(tagCompound1);
                    tagList.appendTag(tagCompound1);
                }
            }
        }

        return tagList;
    }
}
