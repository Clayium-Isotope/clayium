package mods.clayium.util.crafting;

import net.minecraft.item.ItemStack;

public interface IItemPattern {
    boolean match(ItemStack var1, boolean var2);

    boolean hasIntersection(IItemPattern var1, boolean var2);

    int getStackSize(ItemStack var1);

    ItemStack[] toItemStacks();

    ItemStack isSimple();

    boolean isAvailable();
}
