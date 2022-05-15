package mods.clayium.gui.container;

import mods.clayium.block.tile.TileAutoClayCondenser;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutoClayCondenser extends ContainerTemp {
    static int inventoryX = 5;
    static int inventoryY = 4;

    public ContainerAutoClayCondenser(InventoryPlayer player, TileAutoClayCondenser tile, Block block) {
        super(player, (IInventory) tile, block, new Object[0]);
    }


    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = inventoryY * 18 + 32;
        super.initParameters(player);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        TileAutoClayCondenser tile = (TileAutoClayCondenser) this.tile;
        for (int j = 0; j < inventoryY; j++) {
            for (int i = 0; i < inventoryX; i++) {
                addMachineSlotToContainer(new Slot((IInventory) tile, i + j * inventoryX, i * 18 + (this.machineGuiSizeX - 18 * inventoryX) / 2, j * 18 + 18) {

                    public boolean isItemValid(ItemStack itemstack) {
                        return (TileAutoClayCondenser.getTierOfCompressedClay(itemstack) >= 0);
                    }
                });
            }
        }
        addMachineSlotToContainer((Slot) new SlotMemory((IInventory) tile, 21, 108 + (this.machineGuiSizeX - 18 * inventoryX) / 2, 18, RectangleTexture.SmallSlotEClayTexture));
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return (TileAutoClayCondenser.getTierOfCompressedClay(itemstack1) != -1);
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return mergeItemStack(itemstack1, 0, inventoryX * inventoryY, false);
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return (slot < inventoryX * inventoryY && transferStackToPlayerInventory(itemstack1, true));
    }
}
