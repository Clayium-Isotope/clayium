package mods.clayium.item.crafting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.*;
import mods.clayium.util.UtilItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ClayWorkTableRecipes {
    private static final ClayWorkTableRecipes SMELTING_BASE = new ClayWorkTableRecipes();
    private Map smeltingList = new HashMap();
    private Map experienceList = new HashMap();
    public Map kneadingList = new HashMap();

    public static mods.clayium.util.crafting.ClayWorkTableRecipes smelting() {
        return SMELTING_BASE;
    }

    private ClayWorkTableRecipes() {
        this.addRecipe(new ItemStack(Items.CLAY_BALL),               1, new ItemStack(ItemClayStick.block), ItemStack.EMPTY, 4);
        this.addRecipe(new ItemStack(ItemLargeClayBall.block),       2, new ItemStack(ItemClayDisc.block), ItemStack.EMPTY, 30);
        this.addRecipe(new ItemStack(ItemLargeClayBall.block),       3, new ItemStack(ItemClayDisc.block), new ItemStack(Items.CLAY_BALL, 2), 4);
        this.addRecipe(new ItemStack(ItemLargeClayBall.block),       1, new ItemStack(ItemClayCylinder.block), (ItemStack)null, 4);
        this.addRecipe(new ItemStack(ItemClayPlate.block),           2, new ItemStack(ItemClayBlade.block), (ItemStack)null, 10);
        this.addRecipe(new ItemStack(ItemClayPlate.block),           3, new ItemStack(ItemClayBlade.block), new ItemStack(Items.CLAY_BALL, 2), 1);
        this.addRecipe(new ItemStack(ItemClayPlate.block),           6, new ItemStack(ItemClayStick.block, 4), (ItemStack)null, 3);
        this.addRecipe(new ItemStack(ItemClayDisc.block),            4, new ItemStack(ItemClayPlate.block), new ItemStack(Items.CLAY_BALL, 2), 4);
        this.addRecipe(new ItemStack(ItemClayDisc.block),            5, new ItemStack(ItemClayRing.block), CMaterials.itemClayParts.getItemStack("SmallDisc"), 2);
        this.addRecipe((ItemStack)CMaterials.itemClayParts.getItemStack("SmallDisc"), 5, CMaterials.itemClayParts.getItemStack("SmallRing"), CMaterials.itemClayParts.getItemStack("ShortStick"), 1);
        this.addRecipe(new ItemStack(ItemClayCylinder.block),        1, new ItemStack(ItemClayNeedle.block), (ItemStack)null, 3);
        this.addRecipe(new ItemStack(ItemClayCylinder.block),        6, CMaterials.itemClayParts.getItemStack("SmallDisc", 8), (ItemStack)null, 7);
        this.addRecipe(new ItemStack(ItemClayDisc.block),            2, new ItemStack(ItemRawClaySlicer.block), (ItemStack)null, 15);
        this.addRecipe(new ItemStack(ItemClayDisc.block),            3, new ItemStack(ItemRawClaySlicer.block), (ItemStack)null, 2);
        this.addRecipe(new ItemStack(ItemClayPlate.block, 6), 3, new ItemStack(ItemLargeClayPlate.block), (ItemStack)null, 10);
        this.addRecipe(new ItemStack(ItemClayPlate.block, 3), 1, new ItemStack(ItemLargeClayBall.block), (ItemStack)null, 40);
    }

    public void addRecipe(Item item, ItemStack itemstack, float experience) {
        this.addLists(item, itemstack, experience);
    }

    public void addLists(Item item, ItemStack itemstack, float experience) {
        this.putLists(new ItemStack(item), itemstack, experience);
    }

    public void putLists(ItemStack itemstack, ItemStack itemstack2, float experience) {
        this.smeltingList.put(itemstack, itemstack2);
        this.experienceList.put(itemstack2, experience);
    }

    public ItemStack getSmeltingResult(ItemStack itemstack) {
        Iterator iterator = this.smeltingList.entrySet().iterator();

        while(iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            if (this.canBeSmelted(itemstack, (ItemStack)entry.getKey())) {
                return (ItemStack)entry.getValue();
            }
        }

        return null;
    }

    private boolean canBeSmelted(ItemStack itemstack, ItemStack itemstack2) {
        return UtilItemStack.areItemEqual(itemstack2, itemstack) && (itemstack2.func_77960_j() == 32767 || UtilItemStack.areDamageEqual(itemstack2, itemstack)) && itemstack2.field_77994_a <= itemstack.field_77994_a;
    }

    public float giveExperience(ItemStack itemstack) {
        Iterator iterator = this.experienceList.entrySet().iterator();

        while(iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            if (this.canBeSmelted(itemstack, (ItemStack)entry.getKey())) {
                if (itemstack.func_77973_b().getSmeltingExperience(itemstack) != -1.0F) {
                    return itemstack.func_77973_b().getSmeltingExperience(itemstack);
                }

                return (Float)entry.getValue();
            }
        }

        return 0.0F;
    }

    public void addRecipe(Item item, int buttonId, ItemStack itemstack, ItemStack itemstack2, int cookTime) {
        this.addRecipe(new ItemStack(item), buttonId, itemstack, itemstack2, cookTime);
    }

    public void addRecipe(ItemStack itemstack, int buttonId, ItemStack itemstack2, ItemStack itemstack3, int cookTime) {
        Map keyMap = new HashMap();
        Map valueMap = new HashMap();
        keyMap.put("Material", itemstack);
        keyMap.put("ButtonId", buttonId);
        valueMap.put("Result", itemstack2);
        valueMap.put("Result2", itemstack3);
        valueMap.put("CookTime", ClayiumCore.divideByProgressionRateI(cookTime));
        this.kneadingList.put(keyMap, valueMap);
    }

    public Map getKneadingResultMap(ItemStack itemstack, int buttonId) {
        Entry entry_ = null;
        int maxStackSize = 0;
        Iterator iterator = this.kneadingList.entrySet().iterator();

        while(iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            if (this.canBeSmelted(itemstack, (ItemStack)((Map)entry.getKey()).get("Material")) && (Integer)((Map)entry.getKey()).get("ButtonId") == buttonId && ((ItemStack)((Map)entry.getKey()).get("Material")).field_77994_a > maxStackSize) {
                entry_ = entry;
                maxStackSize = ((ItemStack)((Map)entry.getKey()).get("Material")).field_77994_a;
            }
        }

        if (entry_ == null) {
            return null;
        } else {
            return (Map)entry_.getValue();
        }
    }

    public int getConsumedStackSize(ItemStack itemstack, int buttonId) {
        Entry entry_ = null;
        int maxStackSize = 0;
        Iterator iterator = this.kneadingList.entrySet().iterator();

        while(iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            if (this.canBeSmelted(itemstack, (ItemStack)((Map)entry.getKey()).get("Material")) && (Integer)((Map)entry.getKey()).get("ButtonId") == buttonId && ((ItemStack)((Map)entry.getKey()).get("Material")).field_77994_a > maxStackSize) {
                entry_ = entry;
                maxStackSize = ((ItemStack)((Map)entry.getKey()).get("Material")).field_77994_a;
            }
        }

        if (entry_ == null) {
            return 0;
        } else {
            return maxStackSize;
        }
    }

    public ItemStack getKneadingResult(ItemStack itemstack, int buttonId) {
        return this.getKneadingResultMap(itemstack, buttonId) == null ? null : (ItemStack)this.getKneadingResultMap(itemstack, buttonId).get("Result");
    }

    public ItemStack getKneadingResult2(ItemStack itemstack, int buttonId) {
        return this.getKneadingResultMap(itemstack, buttonId) == null ? null : (ItemStack)this.getKneadingResultMap(itemstack, buttonId).get("Result2");
    }

    public int getKneadingTime(ItemStack itemstack, int buttonId) {
        return this.getKneadingResultMap(itemstack, buttonId) == null ? 0 : (Integer)this.getKneadingResultMap(itemstack, buttonId).get("CookTime");
    }

    public boolean hasKneadingResult(ItemStack itemstack) {
        boolean flag = false;
        Iterator iterator = this.kneadingList.entrySet().iterator();

        while(iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            if (this.canBeSmelted(itemstack, (ItemStack)((Map)entry.getKey()).get("Material"))) {
                flag = true;
            }
        }

        return flag;
    }

}
