package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;

public class ContainerClayCraftingTable extends ContainerTemp {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public InventoryCraftResult craftResult = new InventoryCraftResult();
    private final AccessibleTile<TileEntityClayCraftingTable> tileTable;
    private AccessibleTile<TileEntityChest> tileChest = null;
    private final int resultSlot;
    private final int machineSlot;

    public ContainerClayCraftingTable(InventoryPlayer playerIn, TileEntityClayCraftingTable tile) {
        super(playerIn, tile);

        this.tileTable = new AccessibleTile<>((TileEntityClayCraftingTable) this.tileEntity, 0, 3, 3, 30, 17);

        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity rawTile = this.tileTable.getInventory().getWorld().getTileEntity(this.tileTable.getInventory().getPos().offset(facing));
            if (rawTile instanceof TileEntityChest) {
                this.tileChest = new AccessibleTile<>((TileEntityChest) rawTile, 0, 9, 3, 8, 75);
                break;
            }
        }

        int guiY = 19 + this.tileTable.getHeight() * this.tileTable.getY();

        if (this.tileChest != null) {
            guiY += 5 + this.tileTable.getHeight() * this.tileTable.getY();
        }

        guiY += 5;

        this.machineGuiSizeY = Math.max(guiY, 72);

        addMachineSlotToContainer(new SlotCrafting(player.player, this.craftMatrix, this.craftResult, 0, 124, 35));

        this.resultSlot = this.inventorySlots.size(); // as of now

        for(int y = 0; y < this.tileTable.getHeight(); ++y) {
            for(int x = 0; x < this.tileTable.getWidth(); ++x) {
                addSlotToContainer(new SlotWithTexture(this.craftMatrix, this.tileTable.getStart() + x + y * this.tileTable.getWidth(), this.tileTable.getX() + x * 18, this.tileTable.getY() + y * 18, this));
            }
        }

        this.machineSlot = this.inventorySlots.size(); // as of now

        if (this.tileChest != null) {
            for(int y = 0; y < this.tileChest.getHeight(); ++y) {
                for(int x = 0; x < this.tileChest.getWidth(); ++x) {
                    addSlotToContainer(new SlotWithTexture(this.tileChest.getInventory(), this.tileChest.getStart() + x + y * this.tileChest.getWidth(), this.tileChest.getX() + x * 18, this.tileChest.getY() + y * 18, this));
                }
            }
        }

        this.playerSlotIndex = this.inventorySlots.size(); // as of now

        setupPlayerSlots(player, this.machineGuiSizeY);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        slotChangedCraftingGrid(this.tileTable.getInventory().getWorld(), this.player.player, this.craftMatrix, this.craftResult);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!this.tileTable.getInventory().getWorld().isRemote) {
            clearContainer(playerIn, this.tileTable.getInventory().getWorld(), this.craftMatrix);
            this.tileChest = null;
        }
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {

    }

    @Override
    public boolean canTransferToMachineInventory(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean transferStackToMachineInventory(ItemStack itemStack) {
        return mergeItemStack(itemStack, this.machineSlot, this.playerSlotIndex, false)
                || mergeItemStack(itemStack, this.resultSlot, this.machineSlot, false);
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemStack, int index) {
        if (index == this.resultSlot) {
            int stackSize = canMergeItemStack(itemStack, this.machineSlot, this.playerSlotIndex, true);

            if (stackSize == 0) {
                return mergeItemStack(itemStack, this.machineSlot, this.playerSlotIndex, true);
            } else {
                ItemStack _itemStack = itemStack.copy();
                _itemStack.setCount(stackSize);

                if (canMergeItemStack(_itemStack, this.playerSlotIndex, this.playerSlotIndex + 36, true) == 0) {
                    mergeItemStack(itemStack, this.machineSlot, this.playerSlotIndex, true);
                    mergeItemStack(itemStack, this.playerSlotIndex, this.playerSlotIndex + 36, true);
                    return true;
                }
            }
            return false;
        } else {
            if (index >= this.resultSlot && index < this.machineSlot) {
                if (mergeItemStack(itemStack, 0, this.machineSlot, false)) {
                    return true;
                }
            } else if (index < this.machineSlot
                    && mergeItemStack(itemStack, this.machineSlot, this.resultSlot, false)) {
                return true;
            }

            return transferStackToPlayerInventory(itemStack, false);
        }
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}
