package mods.clayium.machine.common;

import mods.clayium.gui.ContainerNormalInventory;
import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.HashMap;
import java.util.Map;

public class InventoryMultiPage implements INormalInventory, IButtonProvider {

    public final IInventory inventory;
    protected int offset = 0;
    protected int offsetMin = Integer.MIN_VALUE;
    protected int offsetMax = Integer.MAX_VALUE;
    protected final int inventorySize;
    public final int pageNum;
    public final int sizePerPage;

    protected Map<Integer, Integer> buttonMap = new HashMap<>();

    public InventoryMultiPage(IInventory inventory, int initialOffset, int sizePerPage, int pageNum) {
        this.pageNum = pageNum;
        this.sizePerPage = sizePerPage;
        this.inventory = inventory;
        setOffset(initialOffset);
        this.inventorySize = sizePerPage;

        addButton(ContainerNormalInventory.buttonIdPrevious, -this.sizePerPage);
        addButton(ContainerNormalInventory.buttonIdNext, this.sizePerPage);
        setOffsetBound(0, (this.pageNum - 1) * this.sizePerPage);
    }

    public InventoryMultiPage(TileEntityClayBuffer normalInventory) {
        this(normalInventory, normalInventory.getInventoryStart(),
                normalInventory.getInventoryX() * normalInventory.getInventoryY(), normalInventory.getInventoryP());
    }

    public InventoryMultiPage(IInventory inventory) {
        this(inventory, 0, inventory.getSizeInventory(), 1);
    }

    public InventoryMultiPage(IInventory inventory, int initialOffset) {
        this(inventory, initialOffset, inventory.getSizeInventory(), 1);
    }

    public InventoryMultiPage(IInventory inventory, int initialOffset, int inventorySize) {
        this(inventory, initialOffset, inventorySize, 1);
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setOffsetBound(int min, int max) {
        this.offsetMin = min;
        this.offsetMax = max;
    }

    public void addButton(int buttonid, int offset) {
        this.buttonMap.put(buttonid, offset);
    }

    @Override
    public ButtonProperty canPushButton(int button) {
        return ButtonProperty.PERMIT;
    }

    @Override
    public boolean isButtonEnable(int button) {
        return true;
    }

    public void pushButton(EntityPlayer player, int action) {
        if (this.buttonMap.containsKey(action)) {
            this.offset += this.buttonMap.get(action);
            this.offset = Math.max(this.offsetMin, Math.min(this.offset, this.offsetMax));
        }
    }

    public int getSizeInventory() {
        return this.inventorySize;
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.isEmpty();
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

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        if (this.offset + slot >= 0 && this.offset + slot < this.inventory.getSizeInventory()) {
            return this.inventory.removeStackFromSlot(this.offset + slot);
        }
        return null;
    }

    public void setInventorySlotContents(int slot, ItemStack p_70299_2_) {
        if (this.offset + slot >= 0 && this.offset + slot < this.inventory.getSizeInventory()) {
            this.inventory.setInventorySlotContents(this.offset + slot, p_70299_2_);
        }
    }

    public String getName() {
        return this.inventory.getName();
    }

    public boolean hasCustomName() {
        return this.inventory.hasCustomName();
    }

    public ITextComponent getDisplayName() {
        return this.inventory.getDisplayName();
    }

    public int getInventoryStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }

    public void markDirty() {
        this.inventory.markDirty();
    }

    public boolean isUsableByPlayer(EntityPlayer p_70300_1_) {
        return this.inventory.isUsableByPlayer(p_70300_1_);
    }

    public void openInventory(EntityPlayer player) {
        this.inventory.openInventory(player);
    }

    public void closeInventory(EntityPlayer player) {
        this.inventory.closeInventory(player);
    }

    public boolean isItemValidForSlot(int slot, ItemStack p_94041_2_) {
        if (this.offset + slot >= 0 && this.offset + slot < this.inventory.getSizeInventory()) {
            return this.inventory.isItemValidForSlot(this.offset + slot, p_94041_2_);
        }
        return false;
    }

    @Override
    public int getField(int id) {
        return this.inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        this.inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return this.inventory.getFieldCount();
    }

    @Override
    public void clear() {
        this.inventory.clear();
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

    public int getPresentPage() {
        return getOffset() / this.sizePerPage;
    }

    public boolean isMultiPage() {
        return this.pageNum > 1;
    }
}
