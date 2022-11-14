package mods.clayium.item.gadget;

import mods.clayium.gui.ContainerInItemStack;
import mods.clayium.gui.SlotMemory;
import mods.clayium.item.filter.IFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ContainerGadgetAutoEat extends ContainerInItemStack {
    public ContainerGadgetAutoEat(EntityPlayer player) {
        super(player, 5, 2, stack -> stack.getItem() instanceof GadgetAutoEat);
    }

    @Override
    protected Slot specialMachineSlot(IInventory inventoryIn, int indexIn, int xPos, int yPos) {
        return new SlotMemory(inventoryIn, indexIn, xPos, yPos) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return itemstack.getItem() instanceof ItemFood || IFilter.isFilter(itemstack);
            }
        };
    }
}
