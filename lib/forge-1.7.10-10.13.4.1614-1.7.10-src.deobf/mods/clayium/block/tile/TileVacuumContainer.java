package mods.clayium.block.tile;

import mods.clayium.item.filter.ItemFilterTemp;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class TileVacuumContainer
        extends TileStorageContainer {
    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        int m = super.getItemUseMode(itemStack, player);
        return (m == 99) ? -1 : m;
    }


    public ItemStack getStackInSlot(int slot) {
        if (slot >= this.extractSlotNum + this.insertSlotNum + 1) {
            return super.getStackInSlot(slot);
        }
        return null;
    }


    public ItemStack decrStackSize(int slot, int size) {
        if (slot >= this.extractSlotNum + this.insertSlotNum + 1) {
            return super.decrStackSize(slot, size);
        }
        return null;
    }


    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        if (slot >= this.extractSlotNum + this.insertSlotNum + 1) {
            super.setInventorySlotContents(slot, itemstack);
            return;
        }
    }


    public boolean checkFilterSlot(ItemStack itemstack, ItemStack filter) {
        return (UtilItemStack.areTypeEqual(itemstack, filter) || ItemFilterTemp.match(filter, itemstack));
    }
}
