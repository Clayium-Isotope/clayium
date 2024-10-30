package mods.clayium.machine.AreaMiner;

import mods.clayium.gui.*;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerAreaMiner extends ContainerTemp {
    protected final int inventoryX, inventoryY;
    
    public ContainerAreaMiner(InventoryPlayer player, TileEntityAreaWorker tile) {
        super(player, tile);
        this.inventoryX = tile.inventoryX.get();
        this.inventoryY = tile.inventoryY.get();
        postConstruct();
    }

    @Override
    protected boolean earlierConstruct() {
        return false;
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        TileEntityAreaWorker tile = (TileEntityAreaWorker)this.tileEntity;
        int j;
        int i;
        this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
        if (tile.isAreaReplacer()) {
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaWorker.filterHarvestSlot, this.inventoryX * 18 + 2 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaWorker.filterFortuneSlot, this.inventoryX * 18 + 2 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 36, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaWorker.filterSilktouchSlot, this.inventoryX * 18 + 2 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 54, RectangleTexture.SmallSlotFilterTexture, true));

            for(j = 0; j < this.inventoryY; ++j) {
                for(i = 0; i < this.inventoryX; ++i) {
                    this.addMachineSlotToContainer(new SlotWithTexture(tile, 4 + i + j * this.inventoryX, i * 18 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, j * 18 + 18));
                }
            }

            for(j = 0; j < this.inventoryY; ++j) {
                for(i = 0; i < this.inventoryX; ++i) {
                    this.addMachineSlotToContainer(new SlotWithTexture(tile, 4 + i + j * this.inventoryX + this.inventoryY * this.inventoryX, i * 18 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, (j + this.inventoryY) * 18 + 18 + 2));
                }
            }
        } else if (tile.hasSomeFilterSlot()) {
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaWorker.filterHarvestSlot, this.inventoryX * 18 + 4 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaWorker.filterFortuneSlot, this.inventoryX * 18 + 4 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 36, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaWorker.filterSilktouchSlot, this.inventoryX * 18 + 4 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 54, RectangleTexture.SmallSlotFilterTexture, true));

            for(j = 0; j < this.inventoryY; ++j) {
                for(i = 0; i < this.inventoryX; ++i) {
                    this.addMachineSlotToContainer(new SlotWithTexture(tile, 4 + i + j * this.inventoryX, i * 18 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, j * 18 + 18));
                }
            }
        } else {
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaWorker.filterHarvestSlot, this.inventoryX * 18 + 4 + (this.machineGuiSizeX - 18 * this.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture, true));

            for(j = 0; j < this.inventoryY; ++j) {
                for(i = 0; i < this.inventoryX; ++i) {
                    this.addMachineSlotToContainer(new SlotWithTexture(tile, 4 + i + j * this.inventoryX, i * 18 + (this.machineGuiSizeX - 18 * this.inventoryX) / 2, j * 18 + 18));
                }
            }
        }
    }

    protected void initParameters(InventoryPlayer player) {
        TileEntityAreaWorker tile = (TileEntityAreaWorker)this.tileEntity;
        if (tile.isAreaReplacer()) {
            this.machineGuiSizeY = this.inventoryY * 18 * 2 + 2 + 18 + 18;
        } else {
            this.machineGuiSizeY = this.inventoryY * 18 + 18 + 18;
        }

        super.initParameters(player);
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int slot) {
        return IClayEnergyConsumer.isItemValidForSlot((IClayEnergyConsumer) this.tileEntity, slot, itemstack1)
            || (
                ((TileEntityAreaWorker)this.tileEntity).isAreaReplacer()
                && this.mergeItemStack(itemstack1, this.inventoryX * this.inventoryY, this.inventoryX * this.inventoryY * 2, false)
            );
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return this.transferStackToPlayerInventory(itemstack1, true);
    }
}
