package mods.clayium.gui.container;

import mods.clayium.block.tile.TileAreaMiner;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAreaMiner extends ContainerTemp {
    public ContainerAreaMiner(InventoryPlayer player, TileAreaMiner tile, Block block) {
        super(player, tile, block);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        TileAreaMiner tile = (TileAreaMiner) this.tile;
        switch (tile.getTier()) {
            case 9:
                for (int j = 0; j < tile.inventoryY; j++) {
                    for (int i = 0; i < tile.inventoryX; i++) {
                        addMachineSlotToContainer(new Slot(tile, i + j * tile.inventoryX, i * 18 + (this.machineGuiSizeX - 32 - 18 * tile.inventoryX) / 2, j * 18 + 18));
                    }
                }

                addMachineSlotToContainer(new SlotMemory(tile, tile.filterHarvestSlot, tile.inventoryX * 18 + 4 + (this.machineGuiSizeX - 32 - 18 * tile.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture));

                addMachineSlotToContainer(new SlotMemory(tile, tile.filterFortuneSlot, tile.inventoryX * 18 + 4 + (this.machineGuiSizeX - 32 - 18 * tile.inventoryX) / 2, 36, RectangleTexture.SmallSlotFilterTexture));

                addMachineSlotToContainer(new SlotMemory(tile, tile.filterSilktouchSlot, tile.inventoryX * 18 + 4 + (this.machineGuiSizeX - 32 - 18 * tile.inventoryX) / 2, 54, RectangleTexture.SmallSlotFilterTexture));
                return;

            case 10:
                for (int j = 0; j < tile.inventoryY; j++) {
                    for (int i = 0; i < tile.inventoryX; i++) {
                        addMachineSlotToContainer(new Slot(tile, i + j * tile.inventoryX, i * 18 + (this.machineGuiSizeX - 32 - 18 * tile.inventoryX) / 2, j * 18 + 18));
                    }
                }

                for (int j = 0; j < tile.inventoryY; j++) {
                    for (int i = 0; i < tile.inventoryX; i++) {
                        addMachineSlotToContainer(new Slot(tile, i + j * tile.inventoryX + tile.inventoryY * tile.inventoryX, i * 18 + (this.machineGuiSizeX - 32 - 18 * tile.inventoryX) / 2, (j + tile.inventoryY) * 18 + 18 + 2));
                    }
                }

                addMachineSlotToContainer(new SlotMemory(tile, tile.filterHarvestSlot, tile.inventoryX * 18 + 2 + (this.machineGuiSizeX - 32 - 18 * tile.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture));

                addMachineSlotToContainer(new SlotMemory(tile, tile.filterFortuneSlot, tile.inventoryX * 18 + 2 + (this.machineGuiSizeX - 32 - 18 * tile.inventoryX) / 2, 36, RectangleTexture.SmallSlotFilterTexture));

                addMachineSlotToContainer(new SlotMemory(tile, tile.filterSilktouchSlot, tile.inventoryX * 18 + 2 + (this.machineGuiSizeX - 32 - 18 * tile.inventoryX) / 2, 54, RectangleTexture.SmallSlotFilterTexture));
                return;
        }

        for (int j = 0; j < tile.inventoryY; j++) {
            for (int i = 0; i < tile.inventoryX; i++) {
                addMachineSlotToContainer(new Slot(tile, i + j * tile.inventoryX, i * 18 + (this.machineGuiSizeX - 18 * tile.inventoryX) / 2, j * 18 + 18));
            }
        }

        addMachineSlotToContainer(new SlotMemory(tile, tile.filterHarvestSlot, tile.inventoryX * 18 + 4 + (this.machineGuiSizeX - 18 * tile.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture));
    }


    protected void initParameters(InventoryPlayer player) {
        TileAreaMiner tile = (TileAreaMiner) this.tile;
        switch (tile.getTier()) {
            case 10:
                this.machineGuiSizeY = tile.inventoryY * 18 * 2 + 2 + 18 + 18;
                break;
            default:
                this.machineGuiSizeY = tile.inventoryY * 18 + 18 + 18;
                break;
        }

        super.initParameters(player);
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return !(((TileAreaMiner) this.tile).getTier() != 10);
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        TileAreaMiner tile = (TileAreaMiner) this.tile;
        return (tile.getTier() != 10) ? false : mergeItemStack(itemstack1, tile.inventoryY * tile.inventoryX, tile.inventoryY * tile.inventoryX * 2, false);
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }
}
