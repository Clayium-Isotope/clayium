package mods.clayium.machine.AutoClayCondenser;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.item.common.IClayEnergy;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerAutoClayCondenser extends ContainerTemp {

    private static final int inventoryX = 5;
    private static final int inventoryY = 4;

    public ContainerAutoClayCondenser(InventoryPlayer player, TileEntityAutoClayCondenser tile) {
        super(player, tile);
    }

    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = inventoryY * 18 + 32;
        super.initParameters(player);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        for (int j = 0; j < inventoryY; ++j) {
            for (int i = 0; i < inventoryX; ++i) {
                this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, i + j * inventoryX,
                        i * 18 + (this.machineGuiSizeX - 18 * inventoryX) / 2, j * 18 + 18) {

                    public boolean isItemValid(ItemStack itemstack) {
                        return IClayEnergy.getTier(itemstack).isValid();
                    }
                });
            }
        }

        this.addMachineSlotToContainer(new SlotMemory(this.tileEntity, TileEntityAutoClayCondenser.SAMPLE_SLOT,
                108 + (this.machineGuiSizeX - 18 * inventoryX) / 2, 18, RectangleTexture.SmallSlotEClayTexture, true));
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return IClayEnergy.getTier(itemstack1).isValid();
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return this.mergeItemStack(itemstack1, 0, inventoryX * inventoryY, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return slot < inventoryX * inventoryY && this.transferStackToPlayerInventory(itemstack1, true);
    }
}
