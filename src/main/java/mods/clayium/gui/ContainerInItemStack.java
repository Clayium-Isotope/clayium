package mods.clayium.gui;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.InventoryInItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public abstract class ContainerInItemStack extends ContainerTemp {
    protected final int inventoryX;
    protected final int inventoryY;
    protected final int theStackPos;
    protected final InventoryInItemStack impl;
    protected final boolean onHotbar;

    public ContainerInItemStack(EntityPlayer player, int inventoryX, int inventoryY, Predicate<ItemStack> predicate) {
        super(player.inventory, null);

        this.inventoryX = inventoryX;
        this.inventoryY = inventoryY;
        this.machineGuiSizeY = this.inventoryY * 18 + 18;

        ItemStack activeStack = this.player.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        int index;
        if (!activeStack.isEmpty() && predicate.test(activeStack)) {
            index = this.player.currentItem;

            this.onHotbar = true;
        } else {
            activeStack = this.player.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
            if (!activeStack.isEmpty() && predicate.test(activeStack)) {
                index = this.player.getSizeInventory() - this.player.offHandInventory.size();
            } else {
                activeStack = ItemStack.EMPTY;
                index = -1;
            }

            this.onHotbar = false;
        }

        if (activeStack.isEmpty()) ClayiumCore.logger.warn("unknown item");
        this.impl = new InventoryInItemStack(activeStack, this.inventoryX * this.inventoryY);
        this.theStackPos = index;

        postConstruct();
    }

    @Override
    protected boolean earlierConstruct() {
        return false;
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        for(int j = 0; j < this.inventoryY; ++j) {
            for(int i = 0; i < this.inventoryX; ++i) {
                this.addMachineSlotToContainer(this.specialMachineSlot(this.impl, i + j * this.inventoryX, i * 18 + (this.machineGuiSizeX - 18 * this.inventoryX) / 2, j * 18 + 18));
            }
        }
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
            if (this.onHotbar && this.theStackPos == i) {
                this.addSlotToContainer(new Slot(player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70) {
                    public boolean canTakeStack(EntityPlayer p_82869_1_) {
                        return false;
                    }
                });
            } else {
                this.addSlotToContainer(new Slot(player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70));
            }
        }

        if (!this.onHotbar && this.theStackPos == -1) {
            this.addSlotToContainer(new Slot(player, this.theStackPos, 0, 0) {
                @Override
                public boolean isEnabled() {
                    return false;
                }
            });
        }
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return this.mergeItemStack(itemstack1, 0, this.playerSlotIndex, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return this.transferStackToPlayerInventory(itemstack1, true);
    }

    @Override
    public String getInventoryName() {
        return this.impl.getName();
    }

    protected abstract Slot specialMachineSlot(IInventory inventoryIn, int indexIn, int xPos, int yPos);
}

