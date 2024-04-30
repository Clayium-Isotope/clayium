package mods.clayium.item;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class NBTInventoryWrapper extends InventoryBasic implements INBTSerializable<NBTTagCompound> {

    protected NBTTagCompound reference;

    protected NBTInventoryWrapper(NBTTagCompound tagCompound, String title, int sizeInventory) {
        super(title, !title.isEmpty(), sizeInventory);

        this.reference = tagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.reference = nbt;
        ItemStackHelper.loadAllItems(this.reference, this.getContains());
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return ItemStackHelper.saveAllItems(this.reference, this.getContains());
    }

    public NonNullList<ItemStack> getContains() {
        return ObfuscationReflectionHelper.getPrivateValue(InventoryBasic.class, this, "field_70482_c");
    }

    public ItemStack getStackInSlot(int slot) {
        this.deserializeNBT(this.reference);
        return super.getStackInSlot(slot);
    }

    public void markDirty() {
        this.reference = this.serializeNBT();
    }
}
