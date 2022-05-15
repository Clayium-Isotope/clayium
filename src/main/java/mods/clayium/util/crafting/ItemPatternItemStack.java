package mods.clayium.util.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;

public class ItemPatternItemStack
        implements IItemPattern {
    private List<ItemStack> items;

    public ItemPatternItemStack(List<ItemStack> itemstacks) {
        this.items = itemstacks;
    }

    public ItemPatternItemStack(ItemStack itemstack) {
        if (itemstack != null) {
            this.items = new ArrayList<ItemStack>(Arrays.asList(new ItemStack[] {itemstack.copy()}));
        } else {
            this.items = new ArrayList<ItemStack>(Arrays.asList(new ItemStack[] {null}));
        }
    }


    public String toString() {
        String ret = "";
        if (this.items == null) {
            ret = "null";
        } else {
            for (int i = 0; i < this.items.size(); i++) {
                ret = ret + ((this.items.get(i) == null) ? null : ((ItemStack) this.items.get(i)).toString());
                if (i < this.items.size() - 1) {
                    ret = ret + ",";
                }
            }
        }
        return "ItemPatternItemStack<" + ret + ">";
    }


    public boolean match(ItemStack itemstack, boolean checkStackSize) {
        for (ItemStack item : this.items) {


            if (canBeCrafted(itemstack, item, checkStackSize))
                return true;
        }
        return false;
    }

    public boolean canBeCrafted(ItemStack ingred, ItemStack recipe, boolean checkStackSize) {
        return Recipes.canBeCrafted(ingred, recipe, checkStackSize);
    }


    public boolean hasIntersection(IItemPattern pattern, boolean checkStackSize) {
        if (pattern == null)
            return false;
        for (ItemStack item : this.items) {
            if (pattern.match(item, checkStackSize))
                return true;
            if (item != null && item.getItemDamage() == 32767) {
                ItemStack[] patternitems = pattern.toItemStacks();
                for (ItemStack patternitem : patternitems) {
                    if (UtilItemStack.areItemEqual(patternitem, item))
                        return true;
                }
            }
        }
        return false;
    }

    public List<ItemStack> getPatternItems() {
        return this.items;
    }

    public int getStackSize(ItemStack itemstack) {
        for (ItemStack item : this.items) {
            if (item == null && itemstack == null)
                return 1;
            if (item != null && item.getItemDamage() == 32767) {
                if (UtilItemStack.areItemEqual(itemstack, item))
                    return Math.max(item.stackSize, 1);
                continue;
            }
            if (UtilItemStack.areItemDamageEqual(itemstack, item)) {
                return Math.max(item.stackSize, 1);
            }
        }

        return 1;
    }

    public ItemStack[] toItemStacks() {
        return this.items.<ItemStack>toArray(new ItemStack[0]);
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + UtilItemStack.getItemStackHashCode(this.items);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemPatternItemStack other = (ItemPatternItemStack) obj;
        if (this.items == null) {
            if (other.items != null)
                return false;
        } else if (!UtilItemStack.areStacksEqual(this.items, other.items)) {
            return false;
        }
        return true;
    }

    public ItemStack isSimple() {
        if (this.items.size() != 1)
            return null;
        ItemStack ret = this.items.get(0);
        return (ret != null && ret.getItemDamage() == 32767) ? null : ret;
    }

    public boolean isAvailable() {
        return (this.items != null && this.items.size() >= 1);
    }
}


