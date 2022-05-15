package mods.clayium.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDummy
        extends ContainerTemp {
    public ContainerDummy() {
        super(null, null, null, new Object[0]);
    }


    protected void initParameters(InventoryPlayer player) {}


    public void setTileEntity(IInventory tile) {}


    public void setMachineInventorySlots(InventoryPlayer player) {}

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return false;
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return false;
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return false;
    }

    public int addMachineSlotToContainer(Slot slot) {
        return 0;
    }


    public void addMachineInventorySlots(InventoryPlayer player) {}


    public void addPlayerInventorySlots(InventoryPlayer player) {}

    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public String getInventoryName() {
        return "";
    }


    public String getTextFieldString(EntityPlayer player, int id) {
        return null;
    }


    public void setTextFieldString(EntityPlayer player, String string, int id) {}


    public void sendTextFieldStringToClient(EntityPlayer player, String string, int id) {}


    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
        return null;
    }

    public boolean transferStackToPlayerInventory(ItemStack itemstack1, boolean flag) {
        return false;
    }

    public boolean transferStackFromPlayerInventory(ItemStack itemstack1, int par2) {
        return false;
    }


    public boolean enchantItem(EntityPlayer player, int action) {
        return true;
    }

    public void onCraftMatrixChanged(IInventory p_75130_1_) {}
}
