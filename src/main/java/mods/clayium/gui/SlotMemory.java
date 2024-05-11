package mods.clayium.gui;

import mods.clayium.item.filter.IFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class SlotMemory extends SlotWithTexture {

    protected final boolean makeMock;

    public SlotMemory(IInventory inventoryIn, int indexIn, int xPos, int yPos, boolean makeMock) {
        super(inventoryIn, indexIn, xPos, yPos);
        this.makeMock = makeMock;
    }

    public SlotMemory(IInventory inventoryIn, int indexIn, int xPos, int yPos, ITexture texture, boolean makeMock) {
        super(inventoryIn, indexIn, xPos, yPos, texture);
        this.makeMock = makeMock;
    }

    @Override
    public void putStack(ItemStack stack) {
        if (stack.isEmpty()) {
            super.putStack(ItemStack.EMPTY);
            return;
        }

        if (this.makeMock && !IFilter.isFilter(stack)) {
            super.putStack(IFilter.getMockWhitelist(stack));
            return;
        }

        super.putStack(stack);
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        this.onSlotChanged();
        ItemStack playerStack = player.inventory.getItemStack();
        ItemStack slotStack = this.getStack();

        if (IFilter.isFilter(playerStack) && ((IFilter) playerStack.getItem()).isCopy(playerStack)) {
            if (!player.world.isRemote) {
                player.sendMessage(new TextComponentString("Copied " + slotStack.getDisplayName()));
                List<String> list = new ArrayList<>();
                ((IFilter) slotStack.getItem()).addFilterInformation(slotStack, player, list, true);

                for (String s : list) {
                    player.sendMessage(new TextComponentString("| " + s));
                }
            }

            return ((IFilter) slotStack.getItem()).setCopyFlag(slotStack.copy());
        }

        this.putStack(ItemStack.EMPTY);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getStack() {
        if (this.inventory == null) {
            // ClayiumCore.logger.warn("inv is null");
            return ItemStack.EMPTY;
        }
        ItemStack stack = this.inventory.getStackInSlot(this.getSlotIndex());
        return stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return ItemStack.EMPTY;
    }
}
