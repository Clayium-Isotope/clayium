package mods.clayium.machine.StorageContainer;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotStorageContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerStorageContainer extends ContainerTemp {

    protected int currentStackSize;

    public ContainerStorageContainer(InventoryPlayer player, TileEntityStorageContainer tile) {
        super(player, tile);
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        TileEntityStorageContainer tile = (TileEntityStorageContainer) this.tileEntity;
        this.addMachineSlotToContainer(new SlotStorageContainer(tile, 0, (this.machineGuiSizeX - 20) / 2, 18,
                RectangleTexture.LargeSlotTexture));
    }

    @Override
    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return this.tileEntity.isItemValidForSlot(0, itemstack1);
    }

    @Override
    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return ((TileEntityStorageContainer) this.tileEntity).putItemStack(0, itemstack1);
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return this.transferStackToPlayerInventory(itemstack1, true);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (this.currentStackSize != this.tileEntity.getField(0)) {
                listener.sendWindowProperty(this, 0, this.tileEntity.getField(1));
            }
        }

        this.currentStackSize = this.tileEntity.getField(1);
    }

    @Override
    protected int getUpperLimit(Slot slot, ItemStack holdStack) {
        if (slot instanceof SlotStorageContainer)
            return this.tileEntity.getInventoryStackLimit();
        return super.getUpperLimit(slot, holdStack);
    }
}
