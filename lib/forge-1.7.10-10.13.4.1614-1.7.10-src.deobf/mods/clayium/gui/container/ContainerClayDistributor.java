package mods.clayium.gui.container;

import mods.clayium.block.tile.TileClayDistributor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerClayDistributor
        extends ContainerTemp {
    public ContainerClayDistributor(InventoryPlayer player, TileClayDistributor tile, Block block) {
        super(player, (IInventory) tile, block, new Object[0]);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        TileClayDistributor tile = (TileClayDistributor) this.tile;
        for (int jj = 0; jj < tile.inventoryColonyY; jj++) {
            for (int ii = 0; ii < tile.inventoryColonyX; ii++) {
                for (int j = 0; j < tile.inventoryY; j++) {
                    for (int i = 0; i < tile.inventoryX; i++) {
                        addMachineSlotToContainer(new Slot((IInventory) tile, i + tile.inventoryX * (j + tile.inventoryY * (ii + tile.inventoryColonyX * jj)), i * 18 + ii * (tile.inventoryX * 18 + 2) + (this.machineGuiSizeX - tile.inventoryX * tile.inventoryColonyX * 18 + (tile.inventoryColonyX - 1) * 2) / 2 + 1, j * 18 + jj * (tile.inventoryY * 18 + 2) + 18));
                    }
                }
            }
        }
    }


    protected void initParameters(InventoryPlayer player) {
        TileClayDistributor tile = (TileClayDistributor) this.tile;
        this.machineGuiSizeY = tile.inventoryY * tile.inventoryColonyY * 18 + (tile.inventoryColonyY - 1) * 2 + 18;
        super.initParameters(player);
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        TileClayDistributor tile = (TileClayDistributor) this.tile;
        return !tile.isCrowded();
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        TileClayDistributor tile = (TileClayDistributor) this.tile;
        return mergeItemStack(itemstack1, tile.inventoryColonyX * tile.inventoryColonyY * tile.autoExtractColony, tile.inventoryColonyX * tile.inventoryColonyY * (tile.autoExtractColony + 1), false);
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }
}
