package mods.clayium.util.crafting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CItems;
import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class ClayWorkTableRecipes {
    private static final ClayWorkTableRecipes SMELTING_BASE = new ClayWorkTableRecipes();

    private Map smeltingList = new HashMap<Object, Object>();
    private Map experienceList = new HashMap<Object, Object>();
    public Map kneadingList = new HashMap<Object, Object>();

    public static ClayWorkTableRecipes smelting() {
        return SMELTING_BASE;
    }


    private ClayWorkTableRecipes() {
        addRecipe(new ItemStack(Items.clay_ball), 1, CMaterials.itemClayParts.getItemStack("Stick"), (ItemStack) null, 4);
        addRecipe(CMaterials.itemClayParts.getItemStack("LargeBall"), 2, CMaterials.itemClayParts.getItemStack("Disc"), (ItemStack) null, 30);
        addRecipe(CMaterials.itemClayParts.getItemStack("LargeBall"), 3, CMaterials.itemClayParts.getItemStack("Disc"), new ItemStack(Items.clay_ball, 2), 4);
        addRecipe(CMaterials.itemClayParts.getItemStack("LargeBall"), 1, CMaterials.itemClayParts.getItemStack("Cylinder"), (ItemStack) null, 4);
        addRecipe(CMaterials.itemClayParts.getItemStack("Plate"), 2, CMaterials.itemClayParts.getItemStack("Blade"), (ItemStack) null, 10);
        addRecipe(CMaterials.itemClayParts.getItemStack("Plate"), 3, CMaterials.itemClayParts.getItemStack("Blade"), new ItemStack(Items.clay_ball, 2), 1);
        addRecipe(CMaterials.itemClayParts.getItemStack("Plate"), 6, CMaterials.itemClayParts.getItemStack("Stick", 4), (ItemStack) null, 3);
        addRecipe(CMaterials.itemClayParts.getItemStack("Disc"), 4, CMaterials.itemClayParts.getItemStack("Plate"), new ItemStack(Items.clay_ball, 2), 4);
        addRecipe(CMaterials.itemClayParts.getItemStack("Disc"), 5, CMaterials.itemClayParts.getItemStack("Ring"), CMaterials.itemClayParts.getItemStack("SmallDisc"), 2);
        addRecipe(CMaterials.itemClayParts.getItemStack("SmallDisc"), 5, CMaterials.itemClayParts.getItemStack("SmallRing"), CMaterials.itemClayParts.getItemStack("ShortStick"), 1);
        addRecipe(CMaterials.itemClayParts.getItemStack("Cylinder"), 1, CMaterials.itemClayParts.getItemStack("Needle"), (ItemStack) null, 3);
        addRecipe(CMaterials.itemClayParts.getItemStack("Cylinder"), 6, CMaterials.itemClayParts.getItemStack("SmallDisc", 8), (ItemStack) null, 7);

        addRecipe(CMaterials.itemClayParts.getItemStack("Disc"), 2, CItems.itemRawClayCraftingTools.getItemStack("Slicer"), (ItemStack) null, 15);
        addRecipe(CMaterials.itemClayParts.getItemStack("Disc"), 3, CItems.itemRawClayCraftingTools.getItemStack("Slicer"), (ItemStack) null, 2);

        addRecipe(CMaterials.itemClayParts.getItemStack("Plate", 6), 3, CMaterials.itemClayParts.getItemStack("LargePlate"), (ItemStack) null, 10);
        addRecipe(CMaterials.itemClayParts.getItemStack("Plate", 3), 1, CMaterials.itemClayParts.getItemStack("LargeBall"), (ItemStack) null, 40);
    }

    public void addRecipe(Item item, ItemStack itemstack, float experience) {
        addLists(item, itemstack, experience);
    }

    public void addLists(Item item, ItemStack itemstack, float experience) {
        putLists(new ItemStack(item), itemstack, experience);
    }

    public void putLists(ItemStack itemstack, ItemStack itemstack2, float experience) {
        this.smeltingList.put(itemstack, itemstack2);
        this.experienceList.put(itemstack2, Float.valueOf(experience));
    }

    public ItemStack getSmeltingResult(ItemStack itemstack) {
        Iterator<Map.Entry> iterator = this.smeltingList.entrySet().iterator();


        while (true) {
            if (!iterator.hasNext()) {
                return null;
            }
            Map.Entry entry = iterator.next();

            if (canBeSmelted(itemstack, (ItemStack) entry.getKey()))
                return (ItemStack) entry.getValue();
        }
    }

    private boolean canBeSmelted(ItemStack itemstack, ItemStack itemstack2) {
        return (UtilItemStack.areItemEqual(itemstack2, itemstack) && (itemstack2
                .getItemDamage() == 32767 || UtilItemStack.areDamageEqual(itemstack2, itemstack)) && itemstack2.stackSize <= itemstack.stackSize);
    }


    public float giveExperience(ItemStack itemstack) {
        Map.Entry entry;
        Iterator<Map.Entry> iterator = this.experienceList.entrySet().iterator();


        do {
            if (!iterator.hasNext()) {
                return 0.0F;
            }
            entry = iterator.next();
        }
        while (!canBeSmelted(itemstack, (ItemStack) entry.getKey()));

        if (itemstack.getItem().getSmeltingExperience(itemstack) != -1.0F) {
            return itemstack.getItem().getSmeltingExperience(itemstack);
        }

        return ((Float) entry.getValue()).floatValue();
    }

    public void addRecipe(Item item, int buttonId, ItemStack itemstack, ItemStack itemstack2, int cookTime) {
        addRecipe(new ItemStack(item), buttonId, itemstack, itemstack2, cookTime);
    }

    public void addRecipe(ItemStack itemstack, int buttonId, ItemStack itemstack2, ItemStack itemstack3, int cookTime) {
        Map<Object, Object> keyMap = new HashMap<Object, Object>();
        Map<Object, Object> valueMap = new HashMap<Object, Object>();
        keyMap.put("Material", itemstack);
        keyMap.put("ButtonId", Integer.valueOf(buttonId));
        valueMap.put("Result", itemstack2);
        valueMap.put("Result2", itemstack3);
        valueMap.put("CookTime", Integer.valueOf(ClayiumCore.divideByProgressionRateI(cookTime)));
        this.kneadingList.put(keyMap, valueMap);
    }


    public Map getKneadingResultMap(ItemStack itemstack, int buttonId) {
        Map.Entry entry_ = null;
        int maxStackSize = 0;
        for (Iterator<Map.Entry> iterator = this.kneadingList.entrySet().iterator(); iterator.hasNext(); ) {

            Map.Entry entry = iterator.next();

            if (canBeSmelted(itemstack, (ItemStack) ((Map) entry.getKey()).get("Material")) && ((Integer) ((Map) entry
                    .getKey()).get("ButtonId")).intValue() == buttonId && ((ItemStack) ((Map) entry
                    .getKey()).get("Material")).stackSize > maxStackSize) {
                entry_ = entry;
                maxStackSize = ((ItemStack) ((Map) entry.getKey()).get("Material")).stackSize;
            }
        }
        if (entry_ == null) return null;
        return (Map) entry_.getValue();
    }

    public int getConsumedStackSize(ItemStack itemstack, int buttonId) {
        Map.Entry entry_ = null;
        int maxStackSize = 0;
        for (Iterator<Map.Entry> iterator = this.kneadingList.entrySet().iterator(); iterator.hasNext(); ) {

            Map.Entry entry = iterator.next();

            if (canBeSmelted(itemstack, (ItemStack) ((Map) entry.getKey()).get("Material")) && ((Integer) ((Map) entry
                    .getKey()).get("ButtonId")).intValue() == buttonId && ((ItemStack) ((Map) entry
                    .getKey()).get("Material")).stackSize > maxStackSize) {
                entry_ = entry;
                maxStackSize = ((ItemStack) ((Map) entry.getKey()).get("Material")).stackSize;
            }
        }
        if (entry_ == null) return 0;
        return maxStackSize;
    }

    public ItemStack getKneadingResult(ItemStack itemstack, int buttonId) {
        if (getKneadingResultMap(itemstack, buttonId) == null)
            return null;
        return (ItemStack) getKneadingResultMap(itemstack, buttonId).get("Result");
    }

    public ItemStack getKneadingResult2(ItemStack itemstack, int buttonId) {
        if (getKneadingResultMap(itemstack, buttonId) == null)
            return null;
        return (ItemStack) getKneadingResultMap(itemstack, buttonId).get("Result2");
    }

    public int getKneadingTime(ItemStack itemstack, int buttonId) {
        if (getKneadingResultMap(itemstack, buttonId) == null)
            return 0;
        return ((Integer) getKneadingResultMap(itemstack, buttonId).get("CookTime")).intValue();
    }

    public boolean hasKneadingResult(ItemStack itemstack) {
        boolean flag = false;
        for (Iterator<Map.Entry> iterator = this.kneadingList.entrySet().iterator(); iterator.hasNext(); ) {

            Map.Entry entry = iterator.next();

            if (canBeSmelted(itemstack, (ItemStack) ((Map) entry.getKey()).get("Material")))
                flag = true;
        }
        return flag;
    }
}


