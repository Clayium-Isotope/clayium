package mods.clayium.gui;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.item.filter.IItemFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class SlotMemory
        extends SlotWithTexture {
    public SlotMemory(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        setRestricted();
    }

    public SlotMemory(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ITexture texture) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_, texture);
        setRestricted();
    }

    public SlotMemory(Container listener, IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(listener, p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        setRestricted();
    }


    public SlotMemory(Container listener, IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ITexture texture) {
        super(listener, p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_, texture);
        setRestricted();
    }


    public boolean canTakeStack(EntityPlayer player) {
        boolean flag = false;
        ItemStack playerstack = player.inventory.getItemStack();
        ItemStack slotstack = getStack();
        if (playerstack != null && playerstack.getItem() instanceof IItemFilter && slotstack != null && slotstack
                .getItem() instanceof IItemFilter) {
            IItemFilter playerfilter = (IItemFilter) playerstack.getItem();
            if (playerfilter.isCopy(playerstack)) {
                playerstack = slotstack.copy();
                playerfilter = (IItemFilter) playerstack.getItem();
                playerstack = playerfilter.setCopyFlag(playerstack);
                if (!player.worldObj.isRemote) {
                    player.addChatMessage((IChatComponent) new ChatComponentText("Copied " + slotstack.getDisplayName()));


                    ItemStack appliedFilter = slotstack;
                    List<String> list = new ArrayList<String>();
                    ((IItemFilter) appliedFilter.getItem()).addFilterInformation(appliedFilter, player, list, true);
                    for (String s : list) {
                        player.addChatMessage((IChatComponent) new ChatComponentText(" " + s));
                    }
                }
                player.inventory.setItemStack(playerstack);
                flag = true;
            }
        }
        if (!flag)
            putStack(null);
        return false;
    }


    public boolean isItemValid(ItemStack itemstack) {
        return super.isItemValid(itemstack);
    }


    public ItemStack getStack() {
        return (super.getStack() == null) ? null : super.getStack().copy();
    }
}
