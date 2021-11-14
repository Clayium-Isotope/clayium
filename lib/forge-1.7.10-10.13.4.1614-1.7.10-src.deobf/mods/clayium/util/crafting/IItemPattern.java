package mods.clayium.util.crafting;

import net.minecraft.item.ItemStack;

public interface IItemPattern {
    boolean match(ItemStack paramItemStack, boolean paramBoolean);

    boolean hasIntersection(IItemPattern paramIItemPattern, boolean paramBoolean);

    int getStackSize(ItemStack paramItemStack);

    ItemStack[] toItemStacks();

    ItemStack isSimple();

    boolean isAvailable();
}


