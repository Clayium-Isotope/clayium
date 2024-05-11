package mods.clayium.machine.ClayDistributor;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerClayDistributor extends ContainerTemp {

    public ContainerClayDistributor(InventoryPlayer player, TileEntityClayDistributor tile) {
        super(player, tile);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        TileEntityClayDistributor tile = (TileEntityClayDistributor) this.tileEntity;

        for (int jj = 0; jj < tile.invSectorY; ++jj) {
            for (int ii = 0; ii < tile.invSectorX; ++ii) {
                for (int j = 0; j < tile.inventoryY; ++j) {
                    for (int i = 0; i < tile.inventoryX; ++i) {
                        this.addMachineSlotToContainer(new SlotWithTexture(tile,
                                i + tile.inventoryX * (j + tile.inventoryY * (ii + tile.invSectorX * jj)),
                                i * 18 + ii * (tile.inventoryX * 18 + 2) +
                                        (this.machineGuiSizeX -
                                                (tile.inventoryX * tile.invSectorX * 18 + (tile.invSectorX - 1) * 2)) /
                                                2 +
                                        1,
                                j * 18 + jj * (tile.inventoryY * 18 + 2) + 18));
                    }
                }
            }
        }
    }

    protected void initParameters(InventoryPlayer player) {
        TileEntityClayDistributor tile = (TileEntityClayDistributor) this.tileEntity;
        this.machineGuiSizeY = tile.inventoryY * tile.invSectorY * 18 + (tile.invSectorY - 1) * 2 + 18;
        super.initParameters(player);
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return !((TileEntityClayDistributor) this.tileEntity).isCrowded();
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        TileEntityClayDistributor tile = (TileEntityClayDistributor) this.tileEntity;
        return this.mergeItemStack(itemstack1, tile.invSectorX * tile.invSectorY * tile.sectorPopFrom,
                tile.invSectorX * tile.invSectorY * (tile.sectorPopFrom + 1), false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return this.transferStackToPlayerInventory(itemstack1, true);
    }
}
