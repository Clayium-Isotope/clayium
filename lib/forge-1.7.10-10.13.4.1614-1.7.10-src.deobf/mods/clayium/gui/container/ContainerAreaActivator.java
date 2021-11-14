package mods.clayium.gui.container;

import mods.clayium.block.tile.TileAreaActivator;
import mods.clayium.block.tile.TileAreaMiner;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAreaActivator extends ContainerTemp {
    public ContainerAreaActivator(InventoryPlayer player, TileAreaActivator tile, Block block) {
        super(player, (IInventory) tile, block, new Object[0]);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        TileAreaActivator tile = (TileAreaActivator) this.tile;
        for (int j = 0; j < tile.inventoryY; j++) {
            for (int i = 0; i < tile.inventoryX; i++) {
                addMachineSlotToContainer(new Slot((IInventory) tile, i + j * tile.inventoryX, i * 18 + (this.machineGuiSizeX - 18 * tile.inventoryX) / 2, j * 18 + 18));
            }
        }

        addMachineSlotToContainer((Slot) new SlotMemory((IInventory) tile, tile.filterHarvestSlot, tile.inventoryX * 18 + 4 + (this.machineGuiSizeX - 18 * tile.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture));
    }


    protected void initParameters(InventoryPlayer player) {
        TileAreaActivator tile = (TileAreaActivator) this.tile;
        this.machineGuiSizeY = tile.inventoryY * 18 + 18 + 18;
        super.initParameters(player);
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        TileAreaMiner tile = (TileAreaMiner) this.tile;
        return mergeItemStack(itemstack1, 0, tile.inventoryY * tile.inventoryX, false);
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }
}
