package mods.clayium.machine.MultitrackBuffer;

import mods.clayium.item.filter.IFilter;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilTier;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMultitrackBuffer extends TileEntityClayContainer implements IMultitrackInventory {
    public int[] allSlots;
    public int[][] tracks;
    public int[] slot2track;
    protected MultitrackSelector selector = new MultitrackSelector();

    @Override
    public void initParams() {
        this.setImportRoutes(-1, -1, -1, 0, -1, -1);
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = NonNullList.withSize(60, ItemStack.EMPTY);
    }

    public void initParamsByTier(int tier) {
        if (this.tier == tier) return;
        super.initParamsByTier(tier);

        int trackNum;
        int trackSize;
        switch(tier) {
            case 4:
                trackNum = 2;
                trackSize = 1;
                break;
            case 5:
                trackNum = 3;
                trackSize = 2;
                break;
            case 6:
                trackNum = 4;
                trackSize = 4;
                break;
            case 7:
                trackNum = 5;
                trackSize = 4;
                break;
            case 8:
                trackNum = 6;
                trackSize = 6;
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                trackNum = 6;
                trackSize = 9;
                break;
            default:
                trackNum = 0;
                trackSize = 0;
        }

        int slotNum = trackNum * trackSize;

        int[] slots = new int[slotNum];
        int[] slotsInvert = new int[slotNum];

        int i;
        for(i = 0; i < slotNum; i++) {
            slots[i] = i;
            slotsInvert[i] = slotNum - i - 1;
        }

        this.listSlotsImport.add(slots);
        this.listSlotsExport.add(slotsInvert);
        this.slotsDrop = slots;
        this.allSlots = slots;

        this.tracks = new int[trackNum][trackSize];
        this.slot2track = new int[slotNum];

        for(i = 0; i < trackNum; i++) {
            slots = new int[trackSize];
            slotsInvert = new int[trackSize];

            for(int j = 0; j < trackSize; ++j) {
                slots[j] = j + i * trackSize;
                slotsInvert[j] = (trackSize - j - 1) + i * trackSize;
                this.slot2track[j + i * trackSize] = i;
            }

            this.listSlotsImport.add(slots);
            this.listSlotsExport.add(slotsInvert);
            this.tracks[i] = slots;
        }

        UtilTier.BufferTransport config = UtilTier.BufferTransport.getByTier(tier);
        if (config != null) {
            this.autoInsertInterval = config.autoInsertInterval;
            this.autoExtractInterval = config.autoExtractInterval;
            this.maxAutoInsertDefault = config.maxAutoInsertDefault;
            this.maxAutoExtractDefault = config.maxAutoExtractDefault;
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return this.isItemValidForTrack(this.getTrack(index), stack);
    }

    public boolean isItemValidForTrack(int track, ItemStack itemstack) {
        if (track < 0) {
            return true;
        } else {
            ItemStack filter = track + 54 < this.containerItemStacks.size() ? this.containerItemStacks.get(track + 54) : ItemStack.EMPTY;
            if (filter.isEmpty()) {
                return true;
            } else {
                return IFilter.isFilter(filter) && IFilter.match(filter, itemstack)
                        || IFilter.matchBetweenItemstacks(filter, itemstack, false);
            }
        }
    }

    public int getTrack(int slot) {
        return this.slot2track != null && slot >= 0 && this.slot2track.length > slot ? this.slot2track[slot] : -1;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return this.allSlots;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side, int track) {
        return this.tracks != null && track >= 0 && track < this.tracks.length ? this.tracks[track] : this.getSlotsForFace(side);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (checkBlocked(itemStackIn, direction)) return false;

        return this.canInsertItem(index, itemStackIn, direction, this.getTrack(index));
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (checkBlocked(itemStackIn, direction)) return false;

        return this.canExtractItem(index, itemStackIn, direction, this.getTrack(index));
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, EnumFacing side, int track) {
        return (track < 0 || this.getTrack(slot) == track) && super.canInsertItem(slot, itemstack, side);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side, int track) {
        return (track < 0 || this.getTrack(slot) == track) && super.canExtractItem(slot, itemstack, side);
    }

    @Override
    protected void doAutoTakeIn() {
        for (EnumFacing side : EnumFacing.VALUES) {
            int route = this.getImportRoute(UtilDirection.getSideOfDirection(this.getFront(), side));

            for (int k = 0; k < this.tracks.length; ++k) {
                if (route == 0 || route - 1 == k) {
                    this.selector.track = k;
                    UtilTransfer.extract(this, this.tracks[k], side, this.maxAutoExtract != null && route < this.maxAutoExtract.length && this.maxAutoExtract[route] >= 0 ? this.maxAutoExtract[route] : this.maxAutoExtractDefault, this.selector);
                }
            }
        }
    }

    @Override
    protected void doAutoTakeOut() {
        for (EnumFacing side : EnumFacing.VALUES) {
            int route = this.getExportRoute(UtilDirection.getSideOfDirection(this.getFront(), side));

            for (int k = 0; k < this.tracks.length; ++k) {
                if (route == 0 || route - 1 == k) {
                    this.selector.track = k;
                    UtilTransfer.insert(this, this.tracks[k], side, this.maxAutoInsert != null && route < this.maxAutoInsert.length && this.maxAutoInsert[route] >= 0 ? this.maxAutoInsert[route] : this.maxAutoInsertDefault, this.selector);
                }
            }
        }
    }

    public static class MultitrackSelector extends UtilTransfer.InventorySelector {
        public int track = -1;

        public MultitrackSelector() {
        }

        public int[] getSlotToInsertTo(EnumFacing direction) {
            if (this.selected == null) {
                return null;
            } else {
                return this.selected instanceof IMultitrackInventory ? ((IMultitrackInventory)this.selected).getSlotsForFace(direction.getOpposite(), this.track) : super.getSlotToExtractFrom(direction);
            }
        }

        public int[] getSlotToExtractFrom(EnumFacing direction) {
            if (this.selected == null) {
                return null;
            } else {
                return this.selected instanceof IMultitrackInventory ? ((IMultitrackInventory)this.selected).getSlotsForFace(direction.getOpposite(), this.track) : super.getSlotToExtractFrom(direction);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        registerInsertIcons("import_m0", "import_m1", "import_m2", "import_m3", "import_m4", "import_m5", "import_m6");
        registerExtractIcons("export_m0", "export_m1", "export_m2", "export_m3", "export_m4", "export_m5", "export_m6");
    }
}
