package mods.clayium.item.gadget;

import mods.clayium.gui.ContainerInItemStack;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGadgetHolder extends ContainerInItemStack {

    public ContainerGadgetHolder(EntityPlayer player) {
        super(player, 5, 2, stack -> stack.getItem() instanceof GadgetHolder);
    }

    @Override
    protected Slot specialMachineSlot(IInventory inventoryIn, int indexIn, int xPos, int yPos) {
        return new SlotWithTexture(inventoryIn, indexIn, xPos, yPos) {

            @Override
            public boolean isItemValid(ItemStack stack) {
                return IGadget.isGadget(stack);
            }
        };
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        GadgetHolder.loadGadgets(this.impl.getContains(), playerIn, playerIn.world);
        this.impl.closeInventory(playerIn);
    }
}
