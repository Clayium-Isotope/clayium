package mods.clayium.block.tile;

import java.util.List;

import mods.clayium.item.filter.ItemFilterTemp;
import mods.clayium.util.UtilTransfer;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class TileAreaCollector
        extends TileAreaMiner {
    public void initParams() {
        super.initParams();
        this.energyPerTick = 100L;
        this.progressPerTick = 100;
        this.progressPerJob = 10;
    }

    public void initState() {
        setState(3);
    }

    public void doWork() {
        long c;
        AxisAlignedBB aabb = getAxisAlignedBB();
        switch (getState()) {
            case 0:
            case 3:
                setState(getState() + 1);
                break;
            case 1:
            case 4:
                c = (long) (this.energyPerTick * (1.0D + 4.0D * Math.log10((this.laserEnergy / 1000L + 1L))));
                if (consumeClayEnergy(c)) {
                    setSyncFlag();
                    this.progress += (long) (this.progressPerTick * (1.0D + 4.0D * Math.log10((this.laserEnergy / 1000L + 1L))));
                    this.laserEnergy = 0L;
                    for (int count = 0; count < this.maxJobCount; count++) {
                        List list = this.worldObj.selectEntitiesWithinAABB(EntityItem.class, this.aabb, IEntitySelector.selectAnything);
                        long i = (long) (this.progressPerJob * 1.0D);
                        ItemStack filter = this.containerItemStacks[this.filterHarvestSlot];
                        for (Object a : list) {
                            if (a instanceof EntityItem) {
                                EntityItem eitem = (EntityItem) a;
                                ItemStack item = eitem.getEntityItem().copy();
                                ItemStack item1 = item.copy();
                                item1.stackSize = 1;
                                boolean flag = true;
                                if (ItemFilterTemp.isFilter(filter)) {
                                    flag = ItemFilterTemp.match(filter, item);
                                }
                                if (flag) {
                                    int slotNum = this.inventoryX * this.inventoryY;
                                    boolean flag1 = true;
                                    while (flag1) {
                                        if (UtilTransfer.canProduceItemStack(item1, this.containerItemStacks, 0, slotNum, getInventoryStackLimit()) >= 1 && i <= this.progress) {
                                            this.progress -= i;
                                            UtilTransfer.produceItemStack(item1, this.containerItemStacks, 0, slotNum, getInventoryStackLimit());
                                            if (--item.stackSize <= 0) {
                                                eitem.setDead();
                                                flag1 = false;
                                            } else {
                                                eitem.setEntityItemStack(item);
                                            }
                                            if (getState() == 1) {
                                                flag1 = false;
                                                this.progress = 0L;
                                                setState(2);
                                            }
                                            continue;
                                        }
                                        flag1 = false;
                                    }
                                }

                                if (i > this.progress)
                                    break;
                            }
                        }
                    }
                }
                break;
        }

    }
}
