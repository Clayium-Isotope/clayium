package mods.clayium.item;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class NBTInventoryWrapper extends InventoryBasic {
    protected NBTTagCompound reference;

    protected NBTInventoryWrapper(NBTTagCompound tagCompound, String title, int sizeInventory) {
        super(title, !title.isEmpty(), sizeInventory);

        this.reference = tagCompound;
    }

    public void loadFromTag() {
        ItemStackHelper.loadAllItems(this.reference, ObfuscationReflectionHelper.getPrivateValue(InventoryBasic.class, this, "field_70482_c"));
    }

    public void writeToTag() {
        ItemStackHelper.saveAllItems(this.reference, ObfuscationReflectionHelper.getPrivateValue(InventoryBasic.class, this, "field_70482_c"));
    }

    public ItemStack getStackInSlot(int slot) {
        this.loadFromTag();
        return super.getStackInSlot(slot);
    }

    public void markDirty() {
        this.writeToTag();
    }
}
