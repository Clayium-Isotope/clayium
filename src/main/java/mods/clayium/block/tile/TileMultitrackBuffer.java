package mods.clayium.block.tile;

import mods.clayium.item.filter.ItemFilterTemp;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class TileMultitrackBuffer
        extends TileClayContainerTiered implements IMultitrackInventory {
    public int[][] tracks;
    public int[] slot2track;
    public int[] allSlots;
    public static final int filterPos = 54;

    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, -1, -1, 0, -1, -1};
        this.extractRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = new ItemStack[60];
        this.clayEnergySlot = -1;
    }

    public void initParamsByTier(int tier) {
        setDefaultTransportationParamsByTier(tier, ParamMode.BUFFER);
        int trackNum = 6, trackInvSize = 9;
        switch (tier) {
            case 4:
                trackNum = 2;
                trackInvSize = 1;
                break;
            case 5:
                trackNum = 3;
                trackInvSize = 2;
                break;
            case 6:
                trackNum = 4;
                trackInvSize = 4;
                break;
            case 7:
                trackNum = 5;
                trackInvSize = 4;
                break;
            case 8:
                trackNum = 6;
                trackInvSize = 6;
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                trackNum = 6;
                trackInvSize = 9;
                break;
        }

        int slotNum = trackNum * trackInvSize;

        int[] slots = new int[slotNum];
        int[] slots2 = new int[slotNum];
        int i;
        for (i = 0; i < slots.length; i++) {
            slots[i] = i;
            slots2[i] = slots.length - i - 1;
        }
        this.listSlotsInsert.add(slots);
        this.listSlotsExtract.add(slots2);
        this.slotsDrop = slots;
        this.allSlots = slots;

        this.tracks = new int[trackNum][];
        this.slot2track = new int[slotNum];
        for (i = 0; i < this.slot2track.length; i++) {
            this.slot2track[i] = -1;
        }

        for (int t = 0; t < trackNum; t++) {
            slots = new int[trackInvSize];
            slots2 = new int[trackInvSize];
            for (int j = 0; j < slots.length; j++) {
                slots[j] = j + t * trackInvSize;
                slots2[j] = slots.length - j - 1 + t * trackInvSize;
                this.slot2track[j + t * trackInvSize] = t;
            }
            this.listSlotsInsert.add(slots);
            this.listSlotsExtract.add(slots2);

            this.tracks[t] = slots;
        }
    }


    public void openInventory() {}


    public void closeInventory() {}


    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return isItemValidForTrack(getTrack(slot), itemstack);
    }

    public boolean isItemValidForTrack(int track, ItemStack itemstack) {
        if (track < 0)
            return true;
        ItemStack filter = (track + 54 < this.containerItemStacks.length) ? this.containerItemStacks[track + 54] : null;
        if (filter == null)
            return true;
        return ((ItemFilterTemp.isFilter(filter) && ItemFilterTemp.match(filter, itemstack)) ||
                ItemFilterTemp.matchBetweenItemstacks(filter, itemstack, false));
    }

    public int getTrack(int slot) {
        if (this.slot2track == null || slot < 0 || this.slot2track.length <= slot)
            return -1;
        return this.slot2track[slot];
    }


    public int[] getAccessibleSlotsFromSide(int side) {
        return this.allSlots;
    }

    public int[] getAccessibleSlotsFromSide(int side, int track) {
        return (this.tracks != null && track >= 0 && track < this.tracks.length) ? this.tracks[track] : getAccessibleSlotsFromSide(side);
    }

    public boolean canInsertItem(int slot, ItemStack itemstack, int side, int track) {
        return ((track < 0 || getTrack(slot) == track) && canInsertItem(slot, itemstack, side));
    }

    public boolean canExtractItem(int slot, ItemStack itemstack, int side, int track) {
        return ((track < 0 || getTrack(slot) == track) && canExtractItem(slot, itemstack, side));
    }

    public boolean canInsertItemUnsafe(int slot, ItemStack itemstack, int route) {
        if (route == -1)
            return isItemValidForSlot(slot, itemstack);
        return super.canInsertItemUnsafe(slot, itemstack, route);
    }

    public boolean canExtractItemUnsafe(int slot, ItemStack itemstack, int route) {
        if (route == -1)
            return true;
        return super.canInsertItemUnsafe(slot, itemstack, route);
    }

    protected MultitrackSelector selector = new MultitrackSelector();

    public void doAutoExtractFromSide(int side, int front) {
        int route = this.insertRoutes[side];
        for (int k = 0; k < this.tracks.length; k++) {
            if (route == 0 || route - 1 == k) {
                this.selector.track = k;
                UtilTransfer.extract(this, this.tracks[k], UtilDirection.getOrientation(front), UtilDirection.getSide(side), (this.maxAutoExtract != null && route < this.maxAutoExtract.length && this.maxAutoExtract[route] >= 0) ? this.maxAutoExtract[route] : this.maxAutoExtractDefault, new Object[] {this.selector});
            }
        }
    }


    public void doAutoInsertToSide(int side, int front) {
        int route = this.extractRoutes[side];
        for (int k = 0; k < this.tracks.length; k++) {
            if (route == 0 || route - 1 == k) {
                this.selector.track = k;
                UtilTransfer.insert(this, this.tracks[k], UtilDirection.getOrientation(front), UtilDirection.getSide(side), (this.maxAutoInsert != null && route < this.maxAutoInsert.length && this.maxAutoInsert[route] >= 0) ? this.maxAutoInsert[route] : this.maxAutoInsertDefault, new Object[] {this.selector});
            }
        }
    }

    public static class MultitrackSelector extends UtilTransfer.InventorySelector {
        public int track = -1;

        public int[] getSlotToInsertTo(ForgeDirection direction) {
            if (this.selected == null)
                return null;
            if (this.selected instanceof IMultitrackInventory) {
                return ((IMultitrackInventory) this.selected).getAccessibleSlotsFromSide(direction.getOpposite().ordinal(), this.track);
            }
            return super.getSlotToExtractFrom(direction);
        }


        public int[] getSlotToExtractFrom(ForgeDirection direction) {
            if (this.selected == null)
                return null;
            if (this.selected instanceof IMultitrackInventory) {
                return ((IMultitrackInventory) this.selected).getAccessibleSlotsFromSide(direction.getOpposite().ordinal(), this.track);
            }
            return super.getSlotToExtractFrom(direction);
        }
    }
}
