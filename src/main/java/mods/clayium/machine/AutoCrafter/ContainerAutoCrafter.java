package mods.clayium.machine.AutoCrafter;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerAutoCrafter extends ContainerTemp {
    public ContainerAutoCrafter(InventoryPlayer player, TileEntityAutoCrafter tile) {
        super(player, tile);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        TileEntityAutoCrafter tile = (TileEntityAutoCrafter) this.tileEntity;

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
                this.addMachineSlotToContainer(new SlotMemory(tile, i + j * 3 + 15, i * 18 + 5, j * 18 + 18, RectangleTexture.SmallSlotFilterTexture));
            }
        }

        for(j = 0; j < 3; ++j) {
            for(i = 0; i < 2; ++i) {
                this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, i + j * 2 + 9, i * 18 + (this.machineGuiSizeX - 36) - 5, j * 18 + 18));
            }
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
