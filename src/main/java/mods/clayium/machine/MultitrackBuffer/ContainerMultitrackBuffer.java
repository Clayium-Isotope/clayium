package mods.clayium.machine.MultitrackBuffer;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotMemory;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerMultitrackBuffer extends ContainerTemp {
    public ContainerMultitrackBuffer(InventoryPlayer player, TileEntityMultitrackBuffer tile) {
        super(player, tile);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        TileEntityMultitrackBuffer tile = (TileEntityMultitrackBuffer) this.tileEntity;
        int offsetY = 18;
        int x = 0;
        int offsetX;

        for(int[] track : tile.tracks) {
            x = Math.max(x, track.length);
        }

        int xx = x * 18 + 4 + 18;
        offsetX = (this.machineGuiSizeX - xx) / 2 + 1;
        int[] trackl = new int[tile.tracks.length];

        int id;
        for(id = 0; id < tile.slot2track.length; ++id) {
            int track = tile.slot2track[id];
            if (track >= 0 && track < trackl.length) {
                SlotWithTexture slot = new SlotWithTexture(tile, id, trackl[track] * 18 + offsetX, track * 18 + offsetY, RectangleTexture.SmallSlotMultitrackTextures[track]);
                slot.setRestricted();
                this.addMachineSlotToContainer(slot);
                trackl[track]++;
            }
        }

        for(id = 0; id < tile.tracks.length; ++id) {
            if (id + 54 < tile.getSizeInventory()) {
                this.addMachineSlotToContainer(new SlotMemory(tile, id + 54, x * 18 + 4 + offsetX, id * 18 + offsetY, RectangleTexture.SmallSlotMultitrackFilterTextures[id]));
            }
        }
    }

    protected void initParameters(InventoryPlayer player) {
        TileEntityMultitrackBuffer tile = (TileEntityMultitrackBuffer) this.tileEntity;
        int y = tile.tracks.length;
        this.machineGuiSizeY = y * 18 + 18;
        if (y > 6) {
            this.machineGuiSizeY -= 20;
        }

        int x = 0;
        for (int[] track : tile.tracks) {
            x = Math.max(x, track.length);
        }

        this.machineGuiSizeX = Math.max(this.machineGuiSizeX, x * 18 + 4 + 18 + 8);
        super.initParameters(player);
    }

    public boolean drawInventoryName() {
        return ((TileEntityMultitrackBuffer) this.tileEntity).tracks.length <= 6;
    }

    public boolean drawPlayerInventoryName() {
        return ((TileEntityMultitrackBuffer) this.tileEntity).tracks.length <= 6;
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        int num = 0;
        TileEntityMultitrackBuffer tile = (TileEntityMultitrackBuffer) this.tileEntity;

        for(int j = 0; j < tile.slot2track.length; ++j) {
            int t = tile.slot2track[j];
            if (t >= 0 && t < tile.tracks.length) {
                ++num;
            }
        }

        return this.mergeItemStack(itemstack1, 0, num, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return this.transferStackToPlayerInventory(itemstack1, true);
    }
}
