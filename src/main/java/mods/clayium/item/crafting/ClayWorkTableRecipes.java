package mods.clayium.item.crafting;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.*;
import mods.clayium.util.UtilItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ClayWorkTableRecipes {
    private static final ClayWorkTableRecipes SMELTING_BASE = new ClayWorkTableRecipes();
    private Map<ItemStack, ItemStack> smeltingList = new HashMap<>();
    private Map<ItemStack, Float> experienceList = new HashMap<>();
    public Map<Map<String, Object>, Map<String, Object>> kneadingList = new HashMap<>();

    public static ClayWorkTableRecipes smelting() {
        return SMELTING_BASE;
    }

    private ClayWorkTableRecipes() {
        this.addRecipe(new ItemStack(Items.CLAY_BALL), 1, new ItemStack(ItemClayStick.block), ItemStack.EMPTY, 4);
        this.addRecipe(new ItemStack(ItemLargeClayBall.block), 2, new ItemStack(ItemClayDisc.block), ItemStack.EMPTY, 30);
        this.addRecipe(new ItemStack(ItemLargeClayBall.block), 3, new ItemStack(ItemClayDisc.block), new ItemStack(Items.CLAY_BALL, 2), 4);
        this.addRecipe(new ItemStack(ItemLargeClayBall.block), 1, new ItemStack(ItemClayCylinder.block), ItemStack.EMPTY, 4);
        this.addRecipe(new ItemStack(ItemClayPlate.block), 2, new ItemStack(ItemClayBlade.block), ItemStack.EMPTY, 10);
        this.addRecipe(new ItemStack(ItemClayPlate.block), 3, new ItemStack(ItemClayBlade.block), new ItemStack(Items.CLAY_BALL, 2), 1);
        this.addRecipe(new ItemStack(ItemClayPlate.block), 6, new ItemStack(ItemClayStick.block, 4), ItemStack.EMPTY, 3);
        this.addRecipe(new ItemStack(ItemClayDisc.block), 4, new ItemStack(ItemClayPlate.block), new ItemStack(Items.CLAY_BALL, 2), 4);
        this.addRecipe(new ItemStack(ItemClayDisc.block), 5, new ItemStack(ItemClayRing.block), new ItemStack(ItemSmallClayDisc.block), 2);
        this.addRecipe(new ItemStack(ItemSmallClayDisc.block), 5, new ItemStack(ItemSmallClayRing.block), new ItemStack(ItemShortClayStick.block), 1);
        this.addRecipe(new ItemStack(ItemClayCylinder.block), 1, new ItemStack(ItemClayNeedle.block), ItemStack.EMPTY, 3);
        this.addRecipe(new ItemStack(ItemClayCylinder.block), 6, new ItemStack(ItemSmallClayDisc.block, 8), ItemStack.EMPTY, 7);
        this.addRecipe(new ItemStack(ItemClayDisc.block), 2, new ItemStack(ItemRawClaySlicer.block), ItemStack.EMPTY, 15);
        this.addRecipe(new ItemStack(ItemClayDisc.block), 3, new ItemStack(ItemRawClaySlicer.block), ItemStack.EMPTY, 2);
        this.addRecipe(new ItemStack(ItemClayPlate.block, 6), 3, new ItemStack(ItemLargeClayPlate.block), ItemStack.EMPTY, 10);
        this.addRecipe(new ItemStack(ItemClayPlate.block, 3), 1, new ItemStack(ItemLargeClayBall.block), ItemStack.EMPTY, 40);
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
        for (Entry<ItemStack, ItemStack> itemStackItemStackEntry : this.smeltingList.entrySet()) {
            if (this.canBeSmelted(itemstack, itemStackItemStackEntry.getKey())) {
                return itemStackItemStackEntry.getValue();
            }
        }

        return null;
    }

    private boolean canBeSmelted(ItemStack itemstack, ItemStack itemstack2) {
        return UtilItemStack.areItemEqual(itemstack2, itemstack) && (itemstack2.getMetadata() == 32767 || UtilItemStack.areDamageEqual(itemstack2, itemstack)) && itemstack2.getCount() <= itemstack.getCount();
    }

    public float giveExperience(ItemStack itemstack) {
        for (Entry<ItemStack, Float> itemStackFloatEntry : this.experienceList.entrySet()) {
            if (this.canBeSmelted(itemstack, itemStackFloatEntry.getKey())) {
                if (itemstack.getItem().getSmeltingExperience(itemstack) != -1.0F) {
                    return itemstack.getItem().getSmeltingExperience(itemstack);
                }

                return itemStackFloatEntry.getValue();
            }
        }

        return 0.0F;
    }

    public void addRecipe(Item item, int buttonId, ItemStack itemstack, ItemStack itemstack2, int cookTime) {
        this.addRecipe(new ItemStack(item), buttonId, itemstack, itemstack2, cookTime);
    }

    public void addRecipe(ItemStack itemstack, int buttonId, ItemStack itemstack2, ItemStack itemstack3, int cookTime) {
        Map<String, Object> keyMap = new HashMap<>();
        Map<String, Object> valueMap = new HashMap<>();
        keyMap.put("Material", itemstack);
        keyMap.put("ButtonId", buttonId);
        valueMap.put("Result", itemstack2);
        valueMap.put("Result2", itemstack3);
        valueMap.put("CookTime", ClayiumCore.divideByProgressionRateI(cookTime));
        this.kneadingList.put(keyMap, valueMap);
    }

    public Map<String, Object> getKneadingResultMap(ItemStack itemstack, int buttonId) {
        Entry<Map<String, Object>, Map<String, Object>> entry_ = null;
        int maxStackSize = 0;

        for (Entry<Map<String, Object>, Map<String, Object>> entry : this.kneadingList.entrySet()) {
            if (this.canBeSmelted(itemstack, (ItemStack) entry.getKey().get("Material"))
                    && (Integer) entry.getKey().get("ButtonId") == buttonId
                    && ((ItemStack) entry.getKey().get("Material")).getCount() > maxStackSize) {
                entry_ = entry;
                maxStackSize = ((ItemStack) entry.getKey().get("Material")).getCount();
            }
        }

        if (entry_ == null) {
            return null;
        }
        return entry_.getValue();
    }

    public int getConsumedStackSize(ItemStack itemstack, int buttonId) {
        Entry<Map<String, Object>, Map<String, Object>> entry_ = null;
        int maxStackSize = 0;

        for (Entry<Map<String, Object>, Map<String, Object>> mapMapEntry : this.kneadingList.entrySet()) {
            if (this.canBeSmelted(itemstack, (ItemStack) mapMapEntry.getKey().get("Material"))
                    && (Integer) mapMapEntry.getKey().get("ButtonId") == buttonId
                    && ((ItemStack) mapMapEntry.getKey().get("Material")).getCount() > maxStackSize) {
                entry_ = mapMapEntry;
                maxStackSize = ((ItemStack) mapMapEntry.getKey().get("Material")).getCount();
            }
        }

        if (entry_ == null) {
            return 0;
        }
        return maxStackSize;
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

        for (Entry<Map<String, Object>, Map<String, Object>> mapMapEntry : this.kneadingList.entrySet()) {
            if (this.canBeSmelted(itemstack, (ItemStack) mapMapEntry.getKey().get("Material"))) {
                flag = true;
            }
        }

        return flag;
    }
}
