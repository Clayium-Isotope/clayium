package mods.clayium.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * compound key is hard-corded. we can change whenever you want.
 */
public class InventoryInItemStack extends NBTInventoryWrapper {
    protected final ItemStack itemstack;

    public InventoryInItemStack(ItemStack itemstack, int sizeInventory) {
        super(getSufficientTagCompound(itemstack), !itemstack.isEmpty() ? itemstack.getDisplayName() : "", sizeInventory);
        this.itemstack = itemstack;
    }

    protected static NBTTagCompound getSufficientTagCompound(ItemStack from) {
        NBTTagCompound res;

        if (from.hasTagCompound()) {
            res = from.getTagCompound();
        } else {
            res = new NBTTagCompound();
        }

        if (!res.hasKey("Items")) {
            res.setTag("Items", new NBTTagList());
        }

        return res;
    }

    public void markDirty() {
        super.markDirty();
        if (this.itemstack.getTagCompound() == null) this.itemstack.setTagCompound(this.reference);
        else this.itemstack.getTagCompound().setTag("Items", this.reference.getTagList("Items", 10));
    }
}
