package mods.clayium.gui.container;

import mods.clayium.block.tile.TileAutoCrafter;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import mods.clayium.gui.SlotResultWithTexture;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutoCrafter extends ContainerTemp {
    public ContainerAutoCrafter(InventoryPlayer player, TileAutoCrafter tile, Block block) {
        super(player, (IInventory) tile, block, new Object[0]);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        TileAutoCrafter tile = (TileAutoCrafter) this.tile;
        int j;
        for (j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                SlotWithTexture slot = new SlotWithTexture((IInventory) tile, i + j * 3, i * 18 + (this.machineGuiSizeX - 54) / 2 + 1, j * 18 + 18);

                slot.setRestricted();
                addMachineSlotToContainer((Slot) slot);
            }
        }

        for (j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                addMachineSlotToContainer((Slot) new SlotMemory((IInventory) tile, i + j * 3 + 15, i * 18 + 5, j * 18 + 18, RectangleTexture.SmallSlotFilterTexture));
            }
        }


        for (j = 0; j < 3; j++) {
            for (int i = 0; i < 2; i++) {
                addMachineSlotToContainer((Slot) new SlotResultWithTexture((IInventory) tile, i + j * 2 + 9, i * 18 + this.machineGuiSizeX - 36 - 5, j * 18 + 18));
            }
        }
    }


    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = 84;
        super.initParameters(player);
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        TileAutoCrafter tile = (TileAutoCrafter) this.tile;
        boolean res = false;
        for (int i = 0; i < 9; i++) {
            res |= tile.isItemValidForSlot(i, itemstack1);
        }
        return res;
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        TileAutoCrafter tile = (TileAutoCrafter) this.tile;
        boolean res = false;
        for (int i = 0; i < 9; i++) {
            if (tile.isItemValidForSlot(i, itemstack1))
                res |= mergeItemStack(itemstack1, i, i + 1, false);
        }
        return res;
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }
}
