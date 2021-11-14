package mods.clayium.block.tile;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public interface IMultitrackInventory extends ISidedInventory {
    int[] getAccessibleSlotsFromSide(int paramInt1, int paramInt2);

    boolean canInsertItem(int paramInt1, ItemStack paramItemStack, int paramInt2, int paramInt3);

    boolean canExtractItem(int paramInt1, ItemStack paramItemStack, int paramInt2, int paramInt3);
}
