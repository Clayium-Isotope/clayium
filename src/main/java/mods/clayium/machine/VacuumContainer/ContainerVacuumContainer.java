package mods.clayium.machine.VacuumContainer;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotItemHandler;
import mods.clayium.gui.SlotMemory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerVacuumContainer extends ContainerTemp {
    protected final IItemHandler handler;

    public ContainerVacuumContainer(InventoryPlayer player, TileEntityVacuumContainer tileEntity) {
        super(player, tileEntity);

        if (!tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            throw new IllegalArgumentException("Who removed CapabilityItemHandler.ITEM_HANDLER_CAPABILITY from TileEntityVacuumContainer?");
        }

        this.handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        this.addMachineSlotToContainer(new SlotItemHandler((TileEntityVacuumContainer) this.tileEntity, 0, (this.machineGuiSizeX - 16) / 2, 35, RectangleTexture.LargeSlotTexture));
        this.addMachineSlotToContainer(new SlotMemory(this.tileEntity, 1, 108 + (this.machineGuiSizeX - 108) / 2, 18, true));
    }

    @Override
    public boolean canTransferToMachineInventory(ItemStack itemStack) {
        return this.handler.isItemValid(0, itemStack);
    }

    @Override
    public boolean transferStackToMachineInventory(ItemStack itemStack, int index) {
        return false;
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemStack, int index) {
        return false;
    }
}
