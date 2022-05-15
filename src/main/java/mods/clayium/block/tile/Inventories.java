package mods.clayium.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class Inventories
        implements IInventory, IGeneralInterface {
    public IInventory[] inventories;

    public Inventories(IInventory[] inventories) {
        this.inventories = inventories;
    }


    public int getSizeInventory() {
        int size = 0;
        for (IInventory inventory : this.inventories)
            size += inventory.getSizeInventory();
        return size;
    }

    public int invPos(int id, int pos) {
        if (id < 0 || id >= this.inventories.length || pos < 0) return -1;
        int size = 0;
        int i = 0;
        for (; i < id; i++)
            size += this.inventories[i].getSizeInventory();
        if (pos >= this.inventories[i].getSizeInventory())
            return -1;
        return size + pos;
    }

    public int[] invIdPos(int pos) {
        int size = 0;
        for (int i = 0; i < this.inventories.length; i++) {
            if (size + this.inventories[i].getSizeInventory() > pos) {
                return new int[] {i, pos - size};
            }
            size += this.inventories[i].getSizeInventory();
        }
        return null;
    }

    public IInventory get(int id) {
        return this.inventories[id];
    }

    public int size() {
        return this.inventories.length;
    }

    public ItemStack getStackInSlot(int pos) {
        int[] idPos = invIdPos(pos);
        return (idPos == null) ? null : this.inventories[idPos[0]].getStackInSlot(idPos[1]);
    }

    public ItemStack decrStackSize(int pos, int stackSize) {
        int[] idPos = invIdPos(pos);
        return (idPos == null) ? null : this.inventories[idPos[0]].decrStackSize(idPos[1], stackSize);
    }

    public ItemStack getStackInSlotOnClosing(int pos) {
        int[] idPos = invIdPos(pos);
        return (idPos == null) ? null : this.inventories[idPos[0]].getStackInSlotOnClosing(idPos[1]);
    }

    public void setInventorySlotContents(int pos, ItemStack p_70299_2_) {
        int[] idPos = invIdPos(pos);
        if (idPos != null) this.inventories[idPos[0]].setInventorySlotContents(idPos[1], p_70299_2_);
    }

    public String getInventoryName() {
        return null;
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void markDirty() {
        for (IInventory inventory : this.inventories)
            inventory.markDirty();
    }

    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    public void openInventory() {
        for (IInventory inventory : this.inventories)
            inventory.openInventory();
    }

    public void closeInventory() {
        for (IInventory inventory : this.inventories)
            inventory.closeInventory();
    }

    public boolean isItemValidForSlot(int pos, ItemStack p_94041_2_) {
        int[] idPos = invIdPos(pos);
        return (idPos == null) ? false : this.inventories[idPos[0]].isItemValidForSlot(idPos[1], p_94041_2_);
    }


    public void markForStrongUpdate() {
        for (IInventory inventory : this.inventories) {
            if (inventory instanceof IGeneralInterface)
                ((IGeneralInterface) inventory).markForStrongUpdate();
        }
    }

    public void markForWeakUpdate() {
        for (IInventory inventory : this.inventories) {
            if (inventory instanceof IGeneralInterface)
                ((IGeneralInterface) inventory).markForWeakUpdate();
        }
    }

    public void setSyncFlag() {
        for (IInventory inventory : this.inventories) {
            if (inventory instanceof IGeneralInterface)
                ((IGeneralInterface) inventory).setSyncFlag();
        }
    }

    public void setInstantSyncFlag() {
        for (IInventory inventory : this.inventories) {
            if (inventory instanceof IGeneralInterface)
                ((IGeneralInterface) inventory).setInstantSyncFlag();
        }
    }

    public void setRenderSyncFlag() {
        for (IInventory inventory : this.inventories) {
            if (inventory instanceof IGeneralInterface)
                ((IGeneralInterface) inventory).setRenderSyncFlag();
        }
    }

    public static int idOffset = 8;


    public void pushButton(EntityPlayer player, int action) {
        int inventoryId = getInventoryId(action);
        action = getInnerActionId(action);
        if (inventoryId >= 0 && inventoryId < this.inventories.length && this.inventories[inventoryId] instanceof IGeneralInterface)
            ((IGeneralInterface) this.inventories[inventoryId]).pushButton(player, action);
    }

    public void pushButton(EntityPlayer player, int action, int inventoryId) {
        pushButton(player, getGlobalActionId(action, inventoryId));
    }

    public static int getGlobalActionId(int action, int inventoryId) {
        return action + inventoryId * idOffset;
    }

    public static int getInnerActionId(int action) {
        return action % idOffset;
    }

    public static int getInventoryId(int action) {
        return action / idOffset;
    }
}
