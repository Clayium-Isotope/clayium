package mods.clayium.gui.container;

import mods.clayium.block.tile.TileStorageContainer;
import mods.clayium.block.tile.TileVacuumContainer;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import mods.clayium.gui.SlotStorageContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerVacuumContainer extends ContainerTemp {
    public ContainerVacuumContainer(InventoryPlayer player, TileVacuumContainer tile, Block block) {
        super(player, tile, block, new Object[0]);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        TileStorageContainer tile = (TileStorageContainer) this.tile;
        addMachineSlotToContainer(new SlotStorageContainer(tile, tile.extractSlotNum + tile.insertSlotNum - 1, (this.machineGuiSizeX - 16) / 2, 35, RectangleTexture.LargeSlotTexture));
        addMachineSlotToContainer(new SlotMemory(tile, tile.extractSlotNum + tile.insertSlotNum + 1, 108 + (this.machineGuiSizeX - 108) / 2, 18));
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return this.tile.isItemValidForSlot(0, itemstack1);
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return mergeItemStack(itemstack1, 0, 1, false);
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }
}
