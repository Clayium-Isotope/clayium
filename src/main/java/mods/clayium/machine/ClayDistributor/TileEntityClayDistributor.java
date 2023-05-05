package mods.clayium.machine.ClayDistributor;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.util.EnumSide;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TileEntityClayDistributor extends TileEntityClayContainer {
    public int inventoryX = 0;
    public int inventoryY = 0;
    public int invSectorX = 0;
    public int invSectorY = 0;
    public int lastReachedSide = 0;
    public int sectorPutInto = 0;
    public int sectorPopFrom = 0;
    public boolean autoInsertDelayFlag = false;

    public TileEntityClayDistributor() {}

    public void initParams() {
        super.initParams();
        this.setImportRoutes(-1, -1, -1, 0, -1, -1);
        this.setExportRoutes(0, 0, 0, -1, 0, 0);
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = NonNullList.withSize(48, ItemStack.EMPTY);
    }

    public void initParamsByTier(int tier) {
        this.tier = tier;

        switch (tier) {
            case 7:
                this.inventoryX = this.inventoryY = 2;
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
                this.invSectorX = this.invSectorY = 2;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                break;
            case 8:
                this.inventoryX = this.inventoryY = 2;
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 128;
                this.invSectorX = (this.invSectorY = 2) + 1;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                break;
            case 9:
                this.inventoryX = this.inventoryY = 2;
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 512;
                this.invSectorX = (this.invSectorY = 3) + 1;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                break;
            default:
                this.inventoryX = this.inventoryY = 1;
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
                this.invSectorX = this.invSectorY = 1;
                this.autoExtractInterval = this.autoInsertInterval = 8;
        }

        int slotNum = this.inventoryX * this.inventoryY * this.invSectorX * this.invSectorY;
        int[] slots = new int[slotNum];
        int[] slots2 = new int[slotNum];

        for(int i = 0; i < slots.length; ++i) {
            slots[i] = i;
            slots2[i] = slots.length - i - 1;
        }

        this.listSlotsImport.add(slots);
        this.listSlotsExport.add(slots2);
        this.slotsDrop = slots;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        for(int i = 0; i < this.getListSlotsImport().size(); ++i) {
            for (int j : this.getListSlotsImport().get(i)) {
                if (j == slot) return true;
            }
        }

        return false;
    }

    @Override
    public void update() {
        if (this.world.isRemote) {
            super.update();
            return;
        }

//        if (this.sectorPopFrom >= this.invSectorX * this.invSectorY) {
//            this.sectorPopFrom = 0;
//        }
//
//        if (this.sectorPutInto >= this.invSectorX * this.invSectorY) {
//            this.sectorPutInto = 0;
//        }

        boolean flag;
        boolean syncFlag = false;
        if (this.sectorPopFrom != this.sectorPutInto || !this.autoInsertDelayFlag) {
            flag = !this.isSectorEmpty(this.sectorPopFrom);

            if (flag && ++this.sectorPopFrom >= this.invSectorX * this.invSectorY) {
                this.sectorPopFrom = 0;
                this.autoInsertDelayFlag = true;
            }

            if (flag) {
                syncFlag = true;
            }

            this.getListSlotsImport().clear();
            this.getListSlotsImport().add(getSlots(this.sectorPopFrom));

            this.getListSlotsExport().clear();
            this.getListSlotsExport().add(getSlots(this.sectorPutInto));
        }

        if (this.sectorPopFrom != this.sectorPutInto || this.autoInsertDelayFlag) {
            flag = this.isSectorEmpty(this.sectorPutInto);

            if (flag && ++this.sectorPutInto >= this.invSectorX * this.invSectorY) {
                this.sectorPutInto = 0;
                this.autoInsertDelayFlag = false;
            }

            if (flag) {
                syncFlag = true;
            }

            this.getListSlotsExport().clear();
            this.getListSlotsExport().add(getSlots(this.sectorPutInto));
        }

        if (this.sectorPopFrom == this.sectorPutInto && this.autoInsertDelayFlag) {
            this.getListSlotsImport().clear();
            this.getListSlotsImport().add(new int[0]);
        }

        if (syncFlag) {
//            this.setSyncFlag();
        }

        super.update();
    }

    public boolean isCrowded() {
        return this.autoInsertDelayFlag && this.sectorPopFrom == this.sectorPutInto;
    }

    @Override
    protected void doAutoTakeOut() {
//        this.setSyncFlag();

        int max = this.maxAutoInsertDefault;

        // D U F B L R,D U F B L R
        // skip=4 |  limit=6  |
        List<EnumSide> sides = Stream.concat(Stream.of(EnumSide.values()), Stream.of(EnumSide.values()))  // Copy twice
                .limit(this.lastReachedSide + EnumSide.VALUES.length).skip(this.lastReachedSide)  // Slice.  on py: arr[startSide : startSide+6]
                .filter(side -> {  // Filtering
                    int route = this.getExportRoute(side);
                    return route >= 0 && route < this.getListSlotsExport().size();
                })
                .collect(Collectors.toList());

        for (EnumSide side : sides) {
            if (UtilTransfer.insert(this, this.getListSlotsExport().get(this.getExportRoute(side)), UtilDirection.getSideOfDirection(this.getFront(), side), 1) == 0) {
                if ((--max) <= 0) {
                    this.lastReachedSide = side.ordinal();
                    return;
                }
            }
        }
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);

        this.lastReachedSide = tagCompound.getShort("StartSide");
        this.sectorPutInto = tagCompound.getShort("AutoInsertColony");
        this.sectorPopFrom = tagCompound.getShort("AutoExtractColony");
        this.autoInsertDelayFlag = tagCompound.getBoolean("AutoInsertDelayFlag");
        this.inventoryX = tagCompound.getShort("InventoryX");
        this.inventoryY = tagCompound.getShort("InventoryY");
        this.invSectorX = tagCompound.getShort("InventoryColonyX");
        this.invSectorY = tagCompound.getShort("InventoryColonyY");
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);

        tagCompound.setShort("StartSide", (short)this.lastReachedSide);
        tagCompound.setShort("AutoInsertColony", (short)this.sectorPutInto);
        tagCompound.setShort("AutoExtractColony", (short)this.sectorPopFrom);
        tagCompound.setBoolean("AutoInsertDelayFlag", this.autoInsertDelayFlag);
        tagCompound.setShort("InventoryX", (short)this.inventoryX);
        tagCompound.setShort("InventoryY", (short)this.inventoryY);
        tagCompound.setShort("InventoryColonyX", (short)this.invSectorX);
        tagCompound.setShort("InventoryColonyY", (short)this.invSectorY);

        return tagCompound;
    }

    private boolean isSectorEmpty(int sector) {
        int o = this.inventoryX * this.inventoryY * sector;

        for (int i = o; i < o + this.inventoryX * this.inventoryY; ++i) {
            if (!this.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private int[] getSlots(int sector) {
        int o = this.inventoryX * this.inventoryY * sector;

        int[] slots = new int[this.inventoryX * this.inventoryY];

        for (int i = 0; i < this.inventoryX * this.inventoryY; ++i) {
            slots[i] = i + o;
        }

        return slots;
    }

    @Override
    public void registerIOIcons() {
        this.registerInsertIcons("import");
        this.registerExtractIcons("export");
    }
}
