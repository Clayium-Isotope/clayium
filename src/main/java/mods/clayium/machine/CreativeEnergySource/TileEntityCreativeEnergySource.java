package mods.clayium.machine.CreativeEnergySource;

import mods.clayium.client.render.CustomHull;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.ClayBuffer.TileEntityClayBuffer;
import mods.clayium.util.TierPrefix;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

@CustomHull("blocks/machine/creativeenergy")
public class TileEntityCreativeEnergySource extends TileEntityClayBuffer {
    public static final ItemStack oec = IClayEnergy.getCompressedClay(13, 64);

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(1, getEnergeticClay());

        this.inventoryX = this.inventoryY = 1;
        this.listSlotsExport.add(new int[] { 0 });
        this.slotsDrop = new int[0];
        this.setExportRoutes(-1, -1, -1, 0, -1, -1);
    }

    public void initParamsByTier(TierPrefix tier) {
        this.setDefaultTransportation(tier);
    }

    public static ItemStack getEnergeticClay() {
        return oec;
    }

    public void update() {
        super.update();
        this.setInventorySlotContents(0, getEnergeticClay().copy());
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return itemstack.getItem() instanceof IClayEnergy;
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

    @Override
    public void registerIOIcons() {
        this.registerExtractIcons("export");
    }
}
