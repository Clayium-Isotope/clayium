package mods.clayium.machine.ClayBuffer;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.INormalInventory;
import mods.clayium.util.UtilTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.stream.IntStream;

public class TileEntityClayBuffer extends TileEntityClayContainer implements INormalInventory {
    protected int inventoryX;
    protected int inventoryY;
    protected boolean isBuffer = true;

    // Alternative for getAccessibleSlotsFromSide(int side)
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return isBuffer ? IntStream.range(0, this.inventoryX * this.inventoryY).toArray() : super.getSlotsForFace(side);
    }

    @Override
    public int getInventoryX() {
        return this.inventoryX;
    }

    @Override
    public int getInventoryY() {
        return this.inventoryY;
    }

    @Override
    public int getInventoryP() {
        return 1;
    }

    @Override
    public int getInventoryStart() {
        return 0;
    }

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(54, ItemStack.EMPTY);

        this.setImportRoutes(-1, -1, -1, 0, -1, -1);
    }

    @Override
    public void initParamsByTier(int tier) {
        if (this.tier == tier) return;
        this.tier = tier;

        switch (this.tier) {
            case 4:
                this.inventoryX = 1 + (this.inventoryY = 1);
                break;
            case 5:
                this.inventoryX = 1 + (this.inventoryY = 2);
                break;
            case 6:
                this.inventoryX = 1 + (this.inventoryY = 3);
                break;
            case 7:
                this.inventoryX = 1 + (this.inventoryY = 4);
                break;
            case 8:
                this.inventoryX = 5 + (this.inventoryY = 4);
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                this.inventoryX = 3 + (this.inventoryY = 6);
                break;
            default:
                this.inventoryX = this.inventoryY = 1;
                break;
        }

        // TODO refactor below
        int machineInvSize = this.inventoryX * this.inventoryY;
        int[] inserts = new int[machineInvSize];
        int[] extracts = new int[machineInvSize];

        for (int i = 0; i < machineInvSize; i++) {
            inserts[i] = i;
            extracts[i] = machineInvSize - 1 - i;
        }

        this.listSlotsImport.add(inserts);
        this.listSlotsExport.add(extracts);
        this.slotsDrop = inserts;

        this.setDefaultTransportation(this.tier);
    }

    @Override
    protected void setDefaultTransportation(int tier) {
        UtilTier.BufferTransport config = UtilTier.BufferTransport.getByTier(tier);
        if (config != null) {
            this.autoInsertInterval = config.autoInsertInterval;
            this.autoExtractInterval = config.autoExtractInterval;
            this.maxAutoInsertDefault = config.maxAutoInsertDefault;
            this.maxAutoExtractDefault = config.maxAutoExtractDefault;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isBuffer ? !checkBlocked(itemStackIn, direction) : super.canInsertItem(index, itemStackIn, direction);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isBuffer ? !checkBlocked(itemStackIn, direction) : super.canExtractItem(index, itemStackIn, direction);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIOIcons() {
        this.registerInsertIcons("import");
        this.registerExtractIcons("export");
    }
}
