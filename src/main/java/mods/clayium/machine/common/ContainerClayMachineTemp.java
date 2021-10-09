package mods.clayium.machine.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerClayMachineTemp extends Container {
    protected final IInventory tileEntity;
    public int sizeInventory;

    public ContainerClayMachineTemp(IInventory tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.isUsableByPlayer(playerIn);
    }

    public void setupPlayerSlots(IInventory player) {
        setupPlayerSlots(player, 72);
    }

    public void setupPlayerSlots(IInventory player, int machineHeight) {
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, machineHeight + 12 + y * 18));
            }
        }

        for(int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, machineHeight + 70));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, tileEntity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        this.tileEntity.setField(id, value);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack _stack = slot.getStack();
            stack = _stack.copy();

            if (index < sizeInventory) {
                if (!transferStackFromMachineInventory(_stack, index)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(_stack, stack);
            } else if (!transferStackFromPlayerInventory(_stack, index)) {
                return ItemStack.EMPTY;
            }

            if (_stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (_stack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, _stack);
        }

        return stack;
    }

    public boolean transferStackFromMachineInventory(ItemStack itemStackIn, int index) {
        return true;
    }

    public boolean transferStackFromPlayerInventory(ItemStack itemStackIn, int index) {
        if (canTransferToMachineInventory(itemStackIn)) {
            return transferStackToMachineInventory(itemStackIn);
        } else if (index >= sizeInventory && index < sizeInventory + 27) {
            return mergeItemStack(itemStackIn, sizeInventory + 27, sizeInventory + 36, false);
        } else if (index >= sizeInventory + 27 && index < sizeInventory + 36){
            return mergeItemStack(itemStackIn, sizeInventory, sizeInventory + 27, false);
        }

        return true;
    }

    public boolean canTransferToMachineInventory(ItemStack itemStackIn) {
        return true;
    }

    public boolean transferStackToMachineInventory(ItemStack itemStackIn) {
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

    public boolean transferStackToPlayerInventory(ItemStack itemStackIn, boolean reverseDirection) {
        return mergeItemStack(itemStackIn, this.sizeInventory, this.sizeInventory + 36, reverseDirection);
    }
}
