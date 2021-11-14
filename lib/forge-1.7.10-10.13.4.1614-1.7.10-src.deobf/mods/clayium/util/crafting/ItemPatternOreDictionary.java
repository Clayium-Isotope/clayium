package mods.clayium.util.crafting;

import java.util.List;

import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemPatternOreDictionary
        implements IItemPattern {
    private String oreDictionary;
    private int id;
    private int stackSize;

    public ItemPatternOreDictionary(String odName, int stackSize) {
        this.oreDictionary = odName;
        this.id = OreDictionary.getOreID(odName);
        this.stackSize = stackSize;
    }

    public ItemPatternOreDictionary(int odId, int stackSize) {
        this.id = odId;
        this.stackSize = stackSize;
    }


    public String toString() {
        String ret = (this.oreDictionary == null) ? "null" : this.oreDictionary;
        return "ItemPatternItemStack<" + ret + ":" + this.stackSize + ">";
    }

    public boolean match(ItemStack itemstack, boolean checkStackSize) {
        return (itemstack != null && (!checkStackSize || itemstack.stackSize >= this.stackSize) && UtilItemStack.hasOreName(itemstack, this.id));
    }


    public boolean hasIntersection(IItemPattern pattern, boolean checkStackSize) {
        if (pattern == null)
            return false;
        for (ItemStack item : toItemStacks()) {
            if (pattern.match(item, checkStackSize))
                return true;
        }
        return false;
    }

    public String getPatternString() {
        if (this.oreDictionary == null) {
            this.oreDictionary = OreDictionary.getOreName(this.id);
        }
        return this.oreDictionary;
    }

    public int getStackSize(ItemStack itemstack) {
        return this.stackSize;
    }

    public ItemStack[] toItemStacks() {
        ItemStack[] stacks = (ItemStack[]) OreDictionary.getOres(Integer.valueOf(this.id)).toArray((Object[]) new ItemStack[0]);
        ItemStack[] stacks1 = new ItemStack[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            stacks1[i] = stacks[i].copy();
            (stacks1[i]).stackSize = this.stackSize;
        }
        return stacks1;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.id;
        result = 31 * result + this.stackSize;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemPatternOreDictionary other = (ItemPatternOreDictionary) obj;
        if (this.id != other.id)
            return false;
        if (this.stackSize != other.stackSize)
            return false;
        return true;
    }


    public ItemStack isSimple() {
        return null;
    }


    public boolean isAvailable() {
        List<ItemStack> list = OreDictionary.getOres(Integer.valueOf(this.id));
        return (list != null && list.size() != 0);
    }
}


