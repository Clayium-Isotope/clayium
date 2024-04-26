package mods.clayium.machine.AutoTrader;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerAutoTrader extends ContainerTemp {
    public ContainerAutoTrader(InventoryPlayer player, TileEntityAutoTrader tile) {
        super(player, tile);
    }

    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY += 16;
        super.initParameters(player);
    }

    public TileEntityAutoTrader getTileEntity() {
        return (TileEntityAutoTrader) this.tileEntity;
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, 0, 36, 53, RectangleTexture.SmallSlotImport1Texture));
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, 1, 62, 53, RectangleTexture.SmallSlotImport2Texture));
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, 2, 120, 53, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }
        });
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return this.mergeItemStack(itemstack1, 0, 2, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return this.transferStackToPlayerInventory(itemstack1, true);
    }
}
