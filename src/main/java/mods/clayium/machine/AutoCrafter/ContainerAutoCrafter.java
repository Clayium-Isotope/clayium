package mods.clayium.machine.AutoCrafter;

import mods.clayium.gui.*;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerAutoCrafter extends ContainerTemp {
    public ContainerAutoCrafter(InventoryPlayer player, TileEntityAutoCrafter tile) {
        super(player, tile);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        int j;
        int i;
        for(j = 0; j < 3; ++j) {
            for(i = 0; i < 3; ++i) {
                SlotWithTexture slot = new SlotWithTexture(this.tileEntity, i + j * 3, i * 18 + (this.machineGuiSizeX - 54) / 2 + 1, j * 18 + 18);
                slot.setRestricted();
                this.addMachineSlotToContainer(slot);
            }
        }

        for(j = 0; j < 3; ++j) {
            for(i = 0; i < 3; ++i) {
                this.addMachineSlotToContainer(new SlotMemory(this.tileEntity, i + j * 3 + 15, i * 18 + 5, j * 18 + 18, RectangleTexture.SmallSlotFilterTexture, true));
            }
        }

        for(j = 0; j < 3; ++j) {
            for(i = 0; i < 2; ++i) {
                this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, i + j * 2 + 9, i * 18 + (this.machineGuiSizeX - 36) - 5, j * 18 + 18));
            }
        }

        if (IClayEnergyConsumer.hasClayEnergy(this.tileEntity)) {
            this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, this.machineGuiSizeY));
        }
    }

    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = 84;
        super.initParameters(player);
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        TileEntityAutoCrafter tile = (TileEntityAutoCrafter) this.tileEntity;
        boolean res = false;

        for(int i = 0; i < 9; ++i) {
            res |= tile.isItemValidForSlot(i, itemstack1);
        }

        return res;
    }

    @Override
    public boolean transferStackToMachineInventory(ItemStack itemstack, int index) {
        TileEntityAutoCrafter tile = (TileEntityAutoCrafter) this.tileEntity;
        boolean res = false;

        for(int i = 0; i < 9; ++i) {
            if (tile.isItemValidForSlot(i, itemstack)) {
                res |= this.mergeItemStack(itemstack, i, i + 1, false);
            }
        }

        return res;
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return this.transferStackToPlayerInventory(itemstack1, true);
    }
}
