package mods.clayium.gui;

import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.TileEntityGeneric;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerNothing extends ContainerTemp {

    public ContainerNothing(InventoryPlayer player, TileEntityGeneric tile) {
        this(player, tile, 32);
    }

    public ContainerNothing(InventoryPlayer player, TileEntityGeneric tile, int machineGuiSizeY) {
        super(player, tile);

        this.machineGuiSizeY = machineGuiSizeY;
        postConstruct();
    }

    @Override
    protected boolean earlierConstruct() {
        return false;
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        if (IClayEnergyConsumer.hasClayEnergy(this.tileEntity))
            addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return false;
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return false;
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return false;
    }
}
