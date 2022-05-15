package mods.clayium.gui.container;

import mods.clayium.gui.SlotMemory;
import mods.clayium.item.InventoryInItemStack;
import mods.clayium.item.filter.IFilterSizeChecker;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemFilterWhiteList extends ContainerTemp {
    static int inventoryX = 5;
    static int inventoryY = 2;
    protected EntityPlayer player;
    protected int filterPos;

    public ContainerItemFilterWhiteList(EntityPlayer player) {
        super(player.inventory, (IInventory) null, (Block) null, new Object[0]);
        this.player = player;
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        for (int j = 0; j < inventoryY; j++) {
            for (int i = 0; i < inventoryX; i++) {
                addMachineSlotToContainer((Slot) new SlotMemory(this.tile, i + j * inventoryX, i * 18 + (this.machineGuiSizeX - 18 * inventoryX) / 2, j * 18 + 18));
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
                this.filterPos = this.inventorySlots.size() - 1;
            } else {

                addSlotToContainer(new Slot((IInventory) player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70));
            }
        }
    }


    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = inventoryY * 18 + 18;
        this.tile = (IInventory) new InventoryInItemStack(player.getCurrentItem(), "Items", inventoryX * inventoryY);
        super.initParameters(player);
    }


    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        ((InventoryInItemStack) this.tile).setItemStack(getSlot(this.filterPos).getStack());
        ItemStack item = getSlot(this.filterPos).getStack();
        if (item != null && item.getItem() instanceof IFilterSizeChecker) {
            ((IFilterSizeChecker) item.getItem()).checkFilterSize(item, this.player, this.player.getEntityWorld());
        }
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return false;
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return false;
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return false;
    }
}
