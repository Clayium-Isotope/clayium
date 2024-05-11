package mods.clayium.util.crafting;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IItemPattern {

    boolean match(ItemStack var1, boolean var2);

    boolean hasIntersection(IItemPattern var1, boolean var2);

    int getStackSize(ItemStack var1);

    List<ItemStack> toItemStacks();

    ItemStack isSimple();

    boolean isAvailable();
}
