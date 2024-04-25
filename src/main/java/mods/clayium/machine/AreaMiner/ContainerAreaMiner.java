package mods.clayium.machine.AreaMiner;

import mods.clayium.gui.*;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.util.TierPrefix;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerAreaMiner extends ContainerTemp {
    protected final int inventoryX, inventoryY;
    
    public ContainerAreaMiner(InventoryPlayer player, TileEntityAreaMiner tile) {
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
        TileEntityAreaMiner tile = (TileEntityAreaMiner)this.tileEntity;
        int j;
        int i;
        if (tile.replaceMode.get()) {
            for(j = 0; j < this.inventoryY; ++j) {
                for(i = 0; i < this.inventoryX; ++i) {
                    this.addMachineSlotToContainer(new SlotWithTexture(tile, i + j * this.inventoryX, i * 18 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, j * 18 + 18));
                }
            }

            for(j = 0; j < this.inventoryY; ++j) {
                for(i = 0; i < this.inventoryX; ++i) {
                    this.addMachineSlotToContainer(new SlotWithTexture(tile, i + j * this.inventoryX + this.inventoryY * this.inventoryX, i * 18 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, (j + this.inventoryY) * 18 + 18 + 2));
                }
            }

            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaMiner.filterHarvestSlot, this.inventoryX * 18 + 2 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaMiner.filterFortuneSlot, this.inventoryX * 18 + 2 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 36, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaMiner.filterSilktouchSlot, this.inventoryX * 18 + 2 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 54, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
        } else if (TierPrefix.ultimate.equals(tile.getHullTier())) {
            for(j = 0; j < this.inventoryY; ++j) {
                for(i = 0; i < this.inventoryX; ++i) {
                    this.addMachineSlotToContainer(new SlotWithTexture(tile, i + j * this.inventoryX, i * 18 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, j * 18 + 18));
                }
            }

            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaMiner.filterHarvestSlot, this.inventoryX * 18 + 4 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaMiner.filterFortuneSlot, this.inventoryX * 18 + 4 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 36, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaMiner.filterSilktouchSlot, this.inventoryX * 18 + 4 + (this.machineGuiSizeX - 32 - 18 * this.inventoryX) / 2, 54, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
        } else {
            for(j = 0; j < this.inventoryY; ++j) {
                for(i = 0; i < this.inventoryX; ++i) {
                    this.addMachineSlotToContainer(new SlotWithTexture(tile, i + j * this.inventoryX, i * 18 + (this.machineGuiSizeX - 18 * this.inventoryX) / 2, j * 18 + 18));
                }
            }

            this.addMachineSlotToContainer(new SlotMemory(tile, TileEntityAreaMiner.filterHarvestSlot, this.inventoryX * 18 + 4 + (this.machineGuiSizeX - 18 * this.inventoryX) / 2, 18, RectangleTexture.SmallSlotFilterTexture, true));
            this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
        }

    }

    protected void initParameters(InventoryPlayer player) {
        TileEntityAreaMiner tile = (TileEntityAreaMiner)this.tileEntity;
        if (tile.getHullTier() == TierPrefix.antimatter) {
            this.machineGuiSizeY = this.inventoryY * 18 * 2 + 2 + 18 + 18;
        } else {
            this.machineGuiSizeY = this.inventoryY * 18 + 18 + 18;
        }

        super.initParameters(player);
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return ((TileEntityAreaMiner)this.tileEntity).replaceMode.get();
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int slot) {
        return IClayEnergyConsumer.isItemValidForSlot((IClayEnergyConsumer) this.tileEntity, slot, itemstack1)
            || (
                ((TileEntityAreaMiner)this.tileEntity).replaceMode.get()
                && this.mergeItemStack(itemstack1, this.inventoryX * this.inventoryY, this.inventoryX * this.inventoryY * 2, false)
            );
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return this.transferStackToPlayerInventory(itemstack1, true);
    }
}
