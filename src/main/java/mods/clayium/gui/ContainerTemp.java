package mods.clayium.gui;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IHasButton;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public abstract class ContainerTemp extends Container {
    protected InventoryPlayer player;
    protected TileEntityClayContainer tileEntity;
    public ArrayList<Slot> machineInventorySlots = new ArrayList<>();
    protected int playerSlotIndex;
    public int machineGuiSizeX = 176;
    public int machineGuiSizeY = 72;

    public int playerSlotOffsetX;
    public int playerSlotOffsetY;

    public ContainerTemp(InventoryPlayer player, TileEntityClayContainer tileEntity) {
        this.player = player;
        this.tileEntity = tileEntity;

        if (this.tileEntity != null)
            this.tileEntity.openInventory(this.player.player);
        postConstruct();
    }

    protected void postConstruct() {
        initParameters(this.player);
        setMachineInventorySlots(this.player);
        addMachineInventorySlots(this.player);
        setupPlayerSlots(this.player);
    }

    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeX = Math.max(this.machineGuiSizeX, 176);
        this.playerSlotOffsetX = (this.machineGuiSizeX - 176) / 2;
        this.playerSlotOffsetY = this.machineGuiSizeY;
    }

    public int addMachineSlotToContainer(Slot slot) {
        this.machineInventorySlots.add(slot);
        return this.machineInventorySlots.size() - 1;
    }

    public void addMachineInventorySlots(InventoryPlayer player) {
        for (Slot machineInventorySlot : this.machineInventorySlots) {
            addSlotToContainer(machineInventorySlot);
        }
        this.playerSlotIndex = this.machineInventorySlots.size();
    }


    public void setupPlayerSlots(InventoryPlayer player) {
        setupPlayerSlots(player, this.playerSlotOffsetY);
    }

    protected void setupPlayerSlots(InventoryPlayer player, int customYOffset) {
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, 9 + x + y * 9, this.playerSlotOffsetX + 8 + x * 18, customYOffset + 12 + y * 18));
            }
        }

        for(int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, this.playerSlotOffsetX + 8 + x * 18, customYOffset + 70));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        if (this.tileEntity == null) return true;
        return this.tileEntity.isUsableByPlayer(player);
    }

    public String getInventoryName() {
        return this.tileEntity != null ? this.tileEntity.getName() : "";
    }

    public String getTextFieldString(EntityPlayer player, int id) {
        return null;
    }

    public void setTextFieldString(EntityPlayer player, String string, int id) {}

    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
        Slot slot = this.inventorySlots.get(par2);

        if (slot == null || !slot.getHasStack())
            return ItemStack.EMPTY;

        ItemStack itemstack1 = slot.getStack();
        ItemStack itemstack = itemstack1.copy();

        if (par2 < this.playerSlotIndex) {
            if (!transferStackFromMachineInventory(itemstack1, par2))
                return ItemStack.EMPTY;

            slot.onSlotChange(itemstack1, itemstack);
        } else if (!transferStackFromPlayerInventory(itemstack1, par2)) {
            return ItemStack.EMPTY;
        }

        if (itemstack1.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }

        if (itemstack1.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, itemstack1);
        return itemstack;
    }

    public boolean transferStackToPlayerInventory(ItemStack itemstack1, boolean flag) {
        return mergeItemStack(itemstack1, this.playerSlotIndex, this.playerSlotIndex + 36, flag);
    }

    public boolean transferStackFromPlayerInventory(ItemStack itemstack1, int index) {
        if (canTransferToMachineInventory(itemstack1))
            return transferStackToMachineInventory(itemstack1);

        if (index >= this.playerSlotIndex && index < this.playerSlotIndex + 27)
            return mergeItemStack(itemstack1, this.playerSlotIndex + 27, this.playerSlotIndex + 36, false);

        if (index >= this.playerSlotIndex + 27 && index < this.playerSlotIndex + 36)
            return mergeItemStack(itemstack1, this.playerSlotIndex, this.playerSlotIndex + 27, false);
        return true;
    }

    protected int canMergeItemStack(ItemStack itemStackIn, int startIndex, int endIndex, boolean reverseDirection) {
        int stackSize = itemStackIn.getCount();
        int ptr = !reverseDirection ? startIndex : endIndex - 1;

        Slot slot;
        ItemStack stack;
        if (itemStackIn.isStackable()) {
            while (stackSize > 0 && (!reverseDirection && ptr < endIndex || reverseDirection && ptr >= startIndex)) {
                slot = inventorySlots.get(ptr);
                stack = slot.getStack();

                if (!stack.isEmpty() && stack.isItemEqual(itemStackIn)
//                        && (!itemStackIn.getHasSubtypes() || itemStackIn.getMetadata() == stack.getMetadata())
//                        && ItemStack.areItemStackTagsEqual(itemStackIn, stack)
                ) {
                    int cnt = stack.getCount() + stackSize;
                    if (cnt <= itemStackIn.getMaxStackSize()) {
                        return 0;
                    }

                    if (stack.getCount() < itemStackIn.getMaxStackSize()) {
                        stackSize -= itemStackIn.getMaxStackSize() - stack.getCount();
                    }
                }

                ptr += !reverseDirection ? 1 : -1;
            }
        }

        if (stackSize > 0) {
            ptr = !reverseDirection ? startIndex : endIndex - 1;

            while (!reverseDirection && ptr < endIndex || ptr >= startIndex) {
                slot = inventorySlots.get(ptr);
                stack = slot.getStack();
                if (stack.isEmpty()) {
                    return 0;
                }

                ptr += !reverseDirection ? 1 : -1;
            }
        }

        return stackSize;
    }

    public boolean enchantItem(EntityPlayer player, int action) {
        if (this.tileEntity instanceof IHasButton) {
            ((IHasButton) this.tileEntity).pushButton(player, action);
        }
        return true;
    }

    public void detectAndSendChanges() {
        for (int i = 0; i < this.inventorySlots.size(); i++) {
            ItemStack itemstack = this.inventorySlots.get(i).getStack();
            ItemStack itemstack1 = this.inventoryItemStacks.get(i);

            if (!UtilItemStack.areStackEqual(itemstack1, itemstack)) {
                itemstack1 = itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);

                for (net.minecraft.inventory.IContainerListener listener : this.listeners) {
                    listener.sendSlotContents(this, i, itemstack1);
                }
            }
        }
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (this.tileEntity != null)
            this.tileEntity.closeInventory(playerIn);
    }

    public boolean drawInventoryName() {
        return true;
    }

    public boolean drawPlayerInventoryName() {
        return true;
    }

    public abstract void setMachineInventorySlots(InventoryPlayer player);

    public abstract boolean canTransferToMachineInventory(ItemStack itemStack);

    public abstract boolean transferStackToMachineInventory(ItemStack itemStack);

    public abstract boolean transferStackFromMachineInventory(ItemStack itemStack, int index);
}
