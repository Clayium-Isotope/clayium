package mods.clayium.block.tile;

import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;

public class TileCreativeEnergySource extends TileClayBuffer {
    public static ItemStack oec;

    public void initParamsByTier(int tier) {
        setDefaultTransportationParamsByTier(tier, TileClayContainerTiered.ParamMode.BUFFER);
        switch (tier) { }
        this.inventoryX = this.inventoryY = 1;

        int slotNum = this.inventoryX * this.inventoryY;

        int[] slots = new int[slotNum];
        int[] slots2 = new int[slotNum];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
            slots2[i] = slots.length - i - 1;
        }

        this.listSlotsExtract.add(slots2);

        this.slotsDrop = new int[0];
    }


    public static ItemStack getEnergeticClay() {
        if (oec == null)
            setEnergeticClay(TileAutoClayCondenser.getCompressedClay(13, 64));
        return oec;
    }

    public static void setEnergeticClay(ItemStack item) {
        oec = item;
    }


    public void updateEntity() {
        super.updateEntity();
        setInventorySlotContents(0, getEnergeticClay().copy());
    }


    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return UtilItemStack.areStackEqual(getEnergeticClay(), itemstack);
    }

    public boolean canExtractItemUnsafe(int slot, ItemStack itemstack, int route) {
        setInventorySlotContents(0, getEnergeticClay().copy());
        return true;
    }

    public boolean canInsertItemUnsafe(int slot, ItemStack itemstack, int route) {
        return (itemstack == null);
    }


    public ItemStack getStackInSlot(int slot) {
        return getEnergeticClay().copy();
    }
}
