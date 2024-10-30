package mods.clayium.gui;

import mods.clayium.machine.common.IMachine;
import mods.clayium.machine.common.TileEntityGeneric;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public abstract class ContainerIMachine extends ContainerTemp {

    /**
     * The first index of material
     */
    protected final int materialSlotIndex;
    /**
     * The first index of result
     */
    protected final int resultSlotIndex;

    public <TileEntity extends TileEntityGeneric & IMachine> ContainerIMachine(InventoryPlayer player, TileEntity tile, int materialSlotStart, int resultSlotStart) {
        super(player, tile);

        this.materialSlotIndex = materialSlotStart;
        this.resultSlotIndex = resultSlotStart;
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return mergeItemStack(itemstack1, this.materialSlotIndex, this.resultSlotIndex, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        if (slot >= this.resultSlotIndex) {
            return transferStackToPlayerInventory(itemstack1, true);
        }
        return transferStackToPlayerInventory(itemstack1, false);
    }
}
