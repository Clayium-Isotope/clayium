package mods.clayium.item;

import net.minecraft.item.ItemStack;

public class ItemCraftingTools extends ItemTiered {
    private ItemStack brokenItem;

    public ItemCraftingTools(ItemStack itemstack) {
        this.brokenItem = itemstack.copy();

        setMaxStackSize(1);
        setNoRepair();
    }

    public ItemStack getContainerItem(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= getMaxDamage()) {
            return this.brokenItem.copy();
        }
        return new ItemStack(this, 1, itemstack.getItemDamage() + 1);
    }

    public boolean hasContainerItem(ItemStack itemstack) {
        return (getContainerItem(itemstack) != null);
    }


    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= getMaxDamage()) {
            return true;
        }
        return false;
    }
}
