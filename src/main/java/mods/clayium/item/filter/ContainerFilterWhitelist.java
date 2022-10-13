package mods.clayium.item.filter;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.SlotMemory;
import mods.clayium.item.InventoryInItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFilterWhitelist extends ContainerTemp {
    protected static final int inventoryX = 5;
    protected static final int inventoryY = 2;
    protected final int filterSlotIndex;
    protected final boolean onHotbar;
    protected final InventoryInItemStack impl;

    public ContainerFilterWhitelist(EntityPlayer player) {
        super(player.inventory, null);

        this.machineGuiSizeY = inventoryY * 18 + 18;

        ItemStack filter = this.player.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        int index;
        if (!filter.isEmpty() && IFilter.isFilter(filter)) {
            index = this.player.currentItem;

            this.onHotbar = true;
        } else {
            filter = this.player.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
            if (!filter.isEmpty() && IFilter.isFilter(filter)) {
                index = this.player.getSizeInventory() - this.player.offHandInventory.size();
            } else {
                filter = ItemStack.EMPTY;
                index = -1;
            }

            this.onHotbar = false;
        }

        if (filter.isEmpty()) ClayiumCore.logger.warn("unknown item");
        this.impl = new InventoryInItemStack(filter, inventoryX * inventoryY);
        this.filterSlotIndex = index;

        postConstruct();
    }

    @Override
    protected void earlyConstruct() {}

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        for(int j = 0; j < inventoryY; ++j) {
            for(int i = 0; i < inventoryX; ++i) {
                this.addMachineSlotToContainer(new SlotMemory(this.impl, i + j * inventoryX, i * 18 + (this.machineGuiSizeX - 18 * inventoryX) / 2, j * 18 + 18));
            }
        }
    }

    @Override
    public boolean canTransferToMachineInventory(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean transferStackToMachineInventory(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemStack, int index) {
        return true;
    }

    @Override
    public void setupPlayerSlots(InventoryPlayer player) {
        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player, j + i * 9 + 9, this.playerSlotOffsetX + 8 + j * 18, this.playerSlotOffsetY + 12 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            if (this.onHotbar && this.filterSlotIndex == i) {
                this.addSlotToContainer(new Slot(player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70) {
                    @Override
                    public boolean canTakeStack(EntityPlayer player) {
                        return false;
                    }
                });
            } else {
                this.addSlotToContainer(new Slot(player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70));
            }
        }

        if (!this.onHotbar && this.filterSlotIndex != -1) {
            this.addSlotToContainer(new Slot(player, this.filterSlotIndex, 0, 0) {
                @Override
                public boolean isEnabled() {
                    return false;
                }
            });
        }
    }

    @Override
    public String getInventoryName() {
        return this.impl.getName();
    }
}
