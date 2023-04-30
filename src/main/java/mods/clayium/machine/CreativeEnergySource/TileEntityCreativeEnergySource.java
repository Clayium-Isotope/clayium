package mods.clayium.machine.CreativeEnergySource;

import mods.clayium.client.render.CustomHull;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

@CustomHull("blocks/machine/creativeenergy")
public class TileEntityCreativeEnergySource extends TileEntityClayBuffer {
    public static final ItemStack oec = IClayEnergy.getCompressedClay(13, 64);

    public TileEntityCreativeEnergySource() {
    }

    public void initParamsByTier(int tier) {
        this.setDefaultTransportation(tier);

        this.inventoryX = this.inventoryY = 1;
        this.listSlotsExport.add(new int[] { 0 });
        this.slotsDrop = new int[0];
    }

    public static ItemStack getEnergeticClay() {
        return oec;
    }

    public void update() {
        super.update();
        this.setInventorySlotContents(0, getEnergeticClay().copy());
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return UtilItemStack.areStackEqual(getEnergeticClay(), itemstack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        this.setInventorySlotContents(0, getEnergeticClay().copy());
        return true;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    public ItemStack getStackInSlot(int slot) {
        return getEnergeticClay().copy();
    }
}
