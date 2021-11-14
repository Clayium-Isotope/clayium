package mods.clayium.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerNothing extends ContainerTemp {
    public ContainerNothing(InventoryPlayer player, IInventory tile) {
        this(player, tile, 32);
    }

    public ContainerNothing(InventoryPlayer player, IInventory tile, int machineGuiSizeY) {
        super(player, tile, null, new Object[] {Integer.valueOf(machineGuiSizeY)});
    }


    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = ((Integer) this.additionalParams[0]).intValue();
        super.initParameters(player);
    }


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
}
