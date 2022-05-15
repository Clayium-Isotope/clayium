package mods.clayium.block.tile;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryOffsetted
        implements INormalInventory, IGeneralInterface {
    private IInventory inventory;
    protected int offset = 0;
    protected int offsetmin = Integer.MIN_VALUE;
    protected int offsetmax = Integer.MAX_VALUE;
    protected int inventorySize = 0;

    protected Map<Integer, Integer> buttonMap = new HashMap<Integer, Integer>();

    public InventoryOffsetted(IInventory inventory) {
        this(inventory, 0);
    }

    public InventoryOffsetted(IInventory inventory, int initialOffset) {
        this(inventory, initialOffset, inventory.getSizeInventory());
    }


    public InventoryOffsetted(IInventory inventory, int initialOffset, int inventorySize) {
        this.inventory = inventory;
        setOffset(initialOffset);
        this.inventorySize = inventorySize;
    }


    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setOffsetBound(int min, int max) {
        this.offsetmin = min;
        this.offsetmax = max;
    }

    public void addButton(int buttonid, int offset) {
        this.buttonMap.put(Integer.valueOf(buttonid), Integer.valueOf(offset));
    }


    public void markForStrongUpdate() {
        if (this.inventory instanceof IGeneralInterface) {
            ((IGeneralInterface) this.inventory).markForStrongUpdate();
        }
    }


    public void markForWeakUpdate() {
        if (this.inventory instanceof IGeneralInterface) {
            ((IGeneralInterface) this.inventory).markForWeakUpdate();
        }
    }


    public void setSyncFlag() {
        if (this.inventory instanceof IGeneralInterface) {
            ((IGeneralInterface) this.inventory).setSyncFlag();
        }
    }


    public void setInstantSyncFlag() {
        if (this.inventory instanceof IGeneralInterface) {
            ((IGeneralInterface) this.inventory).setInstantSyncFlag();
        }
    }


    public void setRenderSyncFlag() {
        if (this.inventory instanceof IGeneralInterface) {
            ((IGeneralInterface) this.inventory).setRenderSyncFlag();
        }
    }


    public void pushButton(EntityPlayer player, int action) {
        if (this.buttonMap.containsKey(Integer.valueOf(action))) {
            this.offset += ((Integer) this.buttonMap.get(Integer.valueOf(action))).intValue();
            this.offset = Math.max(this.offsetmin, Math.min(this.offset, this.offsetmax));
            return;
        }
        if (this.inventory instanceof IGeneralInterface) {
            ((IGeneralInterface) this.inventory).setRenderSyncFlag();
        }
    }


    public int getSizeInventory() {
        return this.inventorySize;
    }


    public ItemStack getStackInSlot(int slot) {
        if (this.offset + slot >= 0 && this.offset + slot < this.inventory.getSizeInventory()) {
            return this.inventory.getStackInSlot(this.offset + slot);
        }
        return null;
    }


    public ItemStack decrStackSize(int slot, int p_70298_2_) {
        if (this.offset + slot >= 0 && this.offset + slot < this.inventory.getSizeInventory()) {
            return this.inventory.decrStackSize(this.offset + slot, p_70298_2_);
        }
        return null;
    }


    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.offset + slot >= 0 && this.offset + slot < this.inventory.getSizeInventory()) {
            return this.inventory.getStackInSlotOnClosing(this.offset + slot);
        }
        return null;
    }


    public void setInventorySlotContents(int slot, ItemStack p_70299_2_) {
        if (this.offset + slot >= 0 && this.offset + slot < this.inventory.getSizeInventory()) {
            this.inventory.setInventorySlotContents(this.offset + slot, p_70299_2_);
        }
    }


    public String getInventoryName() {
        return this.inventory.getInventoryName();
    }


    public boolean hasCustomInventoryName() {
        return this.inventory.hasCustomInventoryName();
    }


    public int getInventoryStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }


    public void markDirty() {
        this.inventory.markDirty();
    }


    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return this.inventory.isUseableByPlayer(p_70300_1_);
    }


    public void openInventory() {
        this.inventory.openInventory();
    }


    public void closeInventory() {
        this.inventory.closeInventory();
    }


    public boolean isItemValidForSlot(int slot, ItemStack p_94041_2_) {
        if (this.offset + slot >= 0 && this.offset + slot < this.inventory.getSizeInventory()) {
            return this.inventory.isItemValidForSlot(this.offset + slot, p_94041_2_);
        }
        return false;
    }


    public int getInventoryX() {
        if (this.inventory instanceof INormalInventory) {
            return ((INormalInventory) this.inventory).getInventoryX();
        }
        return 1;
    }


    public int getInventoryY() {
        if (this.inventory instanceof INormalInventory) {
            return ((INormalInventory) this.inventory).getInventoryY();
        }
        return 1;
    }


    public int getInventoryP() {
        if (this.inventory instanceof INormalInventory) {
            return ((INormalInventory) this.inventory).getInventoryP();
        }
        return 1;
    }


    public int getInventoryStart() {
        if (this.inventory instanceof INormalInventory) {
            return ((INormalInventory) this.inventory).getInventoryStart();
        }
        return 0;
    }
}
