package mods.clayium.item;

import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;


public class NBTInventoryWrapper
        implements IInventory {
    protected NBTTagList tagList;
    protected ItemStack[] containerItemStacks;
    protected String inventoryName;

    NBTInventoryWrapper(NBTTagList tagList, int sizeInventory) {
        this.containerItemStacks = new ItemStack[sizeInventory];
        setTagList(tagList);
    }

    public void setTagList(NBTTagList tagList) {
        this.tagList = tagList;
        this.containerItemStacks = new ItemStack[this.containerItemStacks.length];
        refreshContainer();
    }


    public void refreshContainer() {
        ItemStack[] res = UtilItemStack.tagList2Items(this.tagList);
        for (int i = 0; i < res.length && i < this.containerItemStacks.length; i++)
            this.containerItemStacks[i] = res[i];
    }

    public void updateList() {
        this.tagList = UtilItemStack.items2TagList(this.containerItemStacks);
    }


    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }


    public int getSizeInventory() {
        return this.containerItemStacks.length;
    }


    public ItemStack getStackInSlot(int slot) {
        refreshContainer();
        return (this.containerItemStacks != null && this.containerItemStacks.length > slot && slot >= 0) ? this.containerItemStacks[slot] : null;
    }


    public ItemStack decrStackSize(int par1, int par2) {
        if (this.containerItemStacks[par1] != null) {

            if ((this.containerItemStacks[par1]).stackSize <= par2) {
                ItemStack itemStack = this.containerItemStacks[par1];
                this.containerItemStacks[par1] = null;
                updateList();
                return itemStack;
            }

            ItemStack itemstack = this.containerItemStacks[par1].splitStack(par2);
            if ((this.containerItemStacks[par1]).stackSize == 0) {
                this.containerItemStacks[par1] = null;
            }
            updateList();
            return itemstack;
        }

        return null;
    }


    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }


    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        this.containerItemStacks[slot] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
        updateList();
    }


    public String getInventoryName() {
        return this.inventoryName;
    }


    public boolean hasCustomInventoryName() {
        return true;
    }


    public int getInventoryStackLimit() {
        return 64;
    }


    public void markDirty() {
        updateList();
    }


    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }


    public void openInventory() {}


    public void closeInventory() {}


    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }
}
