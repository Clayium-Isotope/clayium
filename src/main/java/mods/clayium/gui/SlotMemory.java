package mods.clayium.gui;

import mods.clayium.item.filter.IFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class SlotMemory extends SlotWithTexture {
    public SlotMemory(IInventory inventoryIn, int indexIn, int xPos, int yPos) {
        super(inventoryIn, indexIn, xPos, yPos);
    }

    public SlotMemory(IInventory inventoryIn, int indexIn, int xPos, int yPos, ITexture texture) {
        super(inventoryIn, indexIn, xPos, yPos, texture);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        ItemStack playerStack = player.inventory.getItemStack();
        ItemStack slotStack = this.getStack();

        if (!IFilter.isFilter(playerStack) || !IFilter.isFilter(slotStack) || !((IFilter) playerStack.getItem()).isCopy(playerStack)) {
            this.putStack(ItemStack.EMPTY);
            return false;
        }

        if (!player.world.isRemote) {
            player.sendMessage(new TextComponentString("Copied " + slotStack.getDisplayName()));
            List<String> list = new ArrayList<>();
            ((IFilter) slotStack.getItem()).addFilterInformation(slotStack, player, list, true);

            for (String s : list) {
                player.sendMessage(new TextComponentString(" " + s));
            }
        }

        player.inventory.setItemStack(((IFilter) slotStack.getItem()).setCopyFlag(slotStack.copy()));

        return false;
    }

    @Override
    public ItemStack getStack() {
        if (this.inventory == null) {
//            ClayiumCore.logger.warn("inv is null");
            return ItemStack.EMPTY;
        }
        ItemStack stack = this.inventory.getStackInSlot(this.getSlotIndex());
        return stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
    }
}
