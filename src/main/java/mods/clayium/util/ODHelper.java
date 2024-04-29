package mods.clayium.util;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ODHelper {

    public static ItemStack getODStack(String oreName) {
        List<ItemStack> oreList = OreDictionary.getOres(oreName);
        if (oreList.isEmpty()) return ItemStack.EMPTY;

        return oreList.get(0).copy();
    }

    public static ItemStack getODStack(String oreName, int amount) {
        ItemStack res = getODStack(oreName);
        if (res.isEmpty()) return ItemStack.EMPTY;

        res.setCount(amount);
        return res;
    }
}
