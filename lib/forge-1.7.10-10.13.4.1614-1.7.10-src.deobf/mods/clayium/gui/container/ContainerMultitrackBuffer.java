package mods.clayium.gui.container;

import mods.clayium.block.tile.TileMultitrackBuffer;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMultitrackBuffer extends ContainerTemp {
    public ContainerMultitrackBuffer(InventoryPlayer player, TileMultitrackBuffer tile, Block block) {
        super(player, (IInventory) tile, block, new Object[0]);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        TileMultitrackBuffer tile = (TileMultitrackBuffer) this.tile;
        int offsetY = 18;
        int x = 0;
        for (int[] track : tile.tracks) {
            x = Math.max(x, track.length);
        }
        int xx = x * 18 + 4 + 18;
        int offsetX = (this.machineGuiSizeX - xx) / 2 + 1;
        int[] trackl = new int[tile.tracks.length];
        for (int j = 0; j < tile.slot2track.length; j++) {
            int i = tile.slot2track[j];
            if (i >= 0 && i < trackl.length) {
                SlotWithTexture slot = new SlotWithTexture((IInventory) tile, j, trackl[i] * 18 + offsetX, i * 18 + offsetY, RectangleTexture.SmallSlotMultitrackTextures[i]);
                slot.setRestricted();
                addMachineSlotToContainer((Slot) slot);
                trackl[i] = trackl[i] + 1;
            }
        }

        for (int t = 0; t < tile.tracks.length; t++) {
            if (t + 54 < tile.getSizeInventory()) {
                addMachineSlotToContainer((Slot) new SlotMemory((IInventory) tile, t + 54, x * 18 + 4 + offsetX, t * 18 + offsetY, RectangleTexture.SmallSlotMultitrackFilterTextures[t]));
            }
        }
    }


    protected void initParameters(InventoryPlayer player) {
        TileMultitrackBuffer tile = (TileMultitrackBuffer) this.tile;
        int y = tile.tracks.length;
        this.machineGuiSizeY = y * 18 + 18;
        if (y > 6)
            this.machineGuiSizeY -= 20;
        int x = 0;
        for (int[] track : tile.tracks) {
            x = Math.max(x, track.length);
        }
        this.machineGuiSizeX = Math.max(this.machineGuiSizeX, x * 18 + 4 + 18 + 8);
        super.initParameters(player);
    }

    public boolean drawInventoryName() {
        return (((TileMultitrackBuffer) this.tile).tracks.length <= 6);
    }

    public boolean drawPlayerInventoryName() {
        return (((TileMultitrackBuffer) this.tile).tracks.length <= 6);
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        int num = 0;
        TileMultitrackBuffer tile = (TileMultitrackBuffer) this.tile;
        for (int j = 0; j < tile.slot2track.length; j++) {
            int t = tile.slot2track[j];
            if (t >= 0 && t < tile.tracks.length) {
                num++;
            }
        }
        return mergeItemStack(itemstack1, 0, num, false);
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return transferStackToPlayerInventory(itemstack1, true);
    }
}
