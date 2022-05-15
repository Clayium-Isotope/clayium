package mods.clayium.gui.container;

import mods.clayium.block.tile.TileAutoTrader;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotResultWithTexture;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutoTrader extends ContainerTemp {
    public ContainerAutoTrader(InventoryPlayer player, TileAutoTrader tile, Block block) {
        super(player, tile, block);
    }


    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY += 16;
        super.initParameters(player);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        addMachineSlotToContainer(new SlotWithTexture(this.tile, 0, 36, 53, RectangleTexture.SmallSlotImport1Texture));
        addMachineSlotToContainer(new SlotWithTexture(this.tile, 1, 62, 53, RectangleTexture.SmallSlotImport2Texture));
        addMachineSlotToContainer(new SlotResultWithTexture(this.tile, 2, 120, 53, RectangleTexture.LargeSlotTexture));
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return mergeItemStack(itemstack1, 0, 2, false);
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }
}
