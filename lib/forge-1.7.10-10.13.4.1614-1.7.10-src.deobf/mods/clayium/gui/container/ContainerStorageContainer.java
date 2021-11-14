package mods.clayium.gui.container;

import mods.clayium.block.tile.TileStorageContainer;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import mods.clayium.gui.SlotResultWithTexture;
import mods.clayium.gui.SlotStorageContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerStorageContainer extends ContainerTemp {
    public ContainerStorageContainer(InventoryPlayer player, TileStorageContainer tile, Block block) {
        super(player, (IInventory) tile, block, new Object[0]);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        TileStorageContainer tile = (TileStorageContainer) this.tile;
        addMachineSlotToContainer((Slot) new SlotStorageContainer(tile, tile.extractSlotNum + tile.insertSlotNum - 1, 44, 35, RectangleTexture.LargeSlotTexture));
        addMachineSlotToContainer((Slot) new SlotResultWithTexture((IInventory) tile, tile.extractSlotNum + tile.insertSlotNum, 116, 35, RectangleTexture.LargeSlotTexture));
        addMachineSlotToContainer((Slot) new SlotMemory((IInventory) tile, tile.extractSlotNum + tile.insertSlotNum + 1, 108 + (this.machineGuiSizeX - 108) / 2, 18));
    }


    protected void initParameters(InventoryPlayer player) {
        super.initParameters(player);
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return this.tile.isItemValidForSlot(0, itemstack1);
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return mergeItemStack(itemstack1, 0, 2, false);
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }
}
