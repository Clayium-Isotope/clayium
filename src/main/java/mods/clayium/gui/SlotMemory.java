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
        boolean flag = false;
        ItemStack playerstack = player.inventory.getItemStack();
        ItemStack slotstack = this.getStack();

        if (!playerstack.isEmpty() && playerstack.getItem() instanceof IFilter
                && !slotstack.isEmpty() && slotstack.getItem() instanceof IFilter) {
            IFilter playerfilter = (IFilter) playerstack.getItem();

            if (playerfilter.isCopy(playerstack)) {
                playerstack = slotstack.copy();
                playerfilter = (IFilter) playerstack.getItem();
                playerstack = playerfilter.setCopyFlag(playerstack);

                if (!player.world.isRemote) {
                    player.sendMessage(new TextComponentString("Copied " + slotstack.getDisplayName()));
                    List<String> list = new ArrayList<>();
                    ((IFilter) slotstack.getItem()).addFilterInformation(slotstack, player, list, true);

                    for (String s : list) {
                        player.sendMessage(new TextComponentString(" " + s));
                    }
                }

                player.inventory.setItemStack(playerstack);
                flag = true;
            }
        }

        if (!flag) {
            this.putStack(ItemStack.EMPTY);
        }

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
