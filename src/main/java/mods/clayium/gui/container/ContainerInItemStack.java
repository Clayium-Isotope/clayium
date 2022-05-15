package mods.clayium.gui.container;

import mods.clayium.item.InventoryInItemStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerInItemStack extends ContainerTemp {
    protected int inventoryX = 5;
    protected int inventoryY = 2;
    protected EntityPlayer player;
    protected int holderPos;
    protected String tagName;

    public ContainerInItemStack(EntityPlayer player, int inventoryX, int inventoryY, String tagName) {
        super(player.inventory, (IInventory) null, (Block) null, new Object[] {Integer.valueOf(inventoryX), Integer.valueOf(inventoryY), tagName});
        this.player = player;
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        this.inventoryX = ((Integer) this.additionalParams[0]).intValue();
        this.inventoryY = ((Integer) this.additionalParams[1]).intValue();
        for (int j = 0; j < this.inventoryY; j++) {
            for (int i = 0; i < this.inventoryX; i++) {
                addMachineSlotToContainer(new Slot(this.tile, i + j * this.inventoryX, i * 18 + (this.machineGuiSizeX - 18 * this.inventoryX) / 2, j * 18 + 18));
            }
        }
    }


    public void addPlayerInventorySlots(InventoryPlayer player) {
        int i;
        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot((IInventory) player, j + i * 9 + 9, this.playerSlotOffsetX + 8 + j * 18, this.playerSlotOffsetY + 12 + i * 18));
            }
        }
        for (i = 0; i < 9; i++) {
            if (player.currentItem == i) {
                addSlotToContainer(new Slot((IInventory) player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70) {
                    public boolean canTakeStack(EntityPlayer p_82869_1_) {
                        return false;
                    }
                });
                this.holderPos = this.inventorySlots.size() - 1;
            } else {

                addSlotToContainer(new Slot((IInventory) player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70));
            }
        }
    }


    protected void initParameters(InventoryPlayer player) {
        this.inventoryX = ((Integer) this.additionalParams[0]).intValue();
        this.inventoryY = ((Integer) this.additionalParams[1]).intValue();
        this.tagName = (String) this.additionalParams[2];
        this.machineGuiSizeY = this.inventoryY * 18 + 18;
        this.tile = (IInventory) new InventoryInItemStack(player.getCurrentItem(), this.tagName, this.inventoryX * this.inventoryY);
        super.initParameters(player);
    }


    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        ((InventoryInItemStack) this.tile).setItemStack(getSlot(this.holderPos).getStack());
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return mergeItemStack(itemstack1, 0, this.playerSlotIndex, false);
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }
}
