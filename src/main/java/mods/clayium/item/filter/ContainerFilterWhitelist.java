package mods.clayium.item.filter;

import mods.clayium.gui.ContainerInItemStack;
import mods.clayium.gui.SlotMemory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerFilterWhitelist extends ContainerInItemStack {

    public ContainerFilterWhitelist(EntityPlayer player) {
        super(player, 5, 2, IFilter::isFilter);
    }

    @Override
    protected Slot specialMachineSlot(IInventory inventoryIn, int indexIn, int xPos, int yPos) {
        return new SlotMemory(inventoryIn, indexIn, xPos, yPos, false);
    }
}
