package mods.clayium.machine.AreaCollector;

import mods.clayium.item.filter.IFilter;
import mods.clayium.machine.AreaMiner.EnumAreaMinerState;
import mods.clayium.machine.AreaMiner.TileEntityAreaMiner;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.util.UtilTransfer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityAreaCollector extends TileEntityAreaMiner {
    public static final long energyPerTick = 100L;
    public static final int progressPerTick = 100;
    public static final int progressPerJob = 10;

    public TileEntityAreaCollector() {
    }

    public void initParams() {
        super.initParams();
    }

    public void initState() {
        this.setState(EnumAreaMinerState.LoopMode_Ready);
    }

    public void doWork() {
        AxisAlignedBB aabb = this.getAxisAlignedBB();
        switch (this.getState()) {
            case OnceMode_Ready:
                this.setState(EnumAreaMinerState.OnceMode_Doing);
                break;
            case LoopMode_Ready:
                this.setState(EnumAreaMinerState.LoopMode_Doing);
                break;
            case OnceMode_Doing:
            case LoopMode_Doing:
                long c = (long)((double) this.energyPerTick * (1.0 + 4.0 * Math.log10((double)(this.laserEnergy / 1000L + 1L))));
                if (!IClayEnergyConsumer.compensateClayEnergy(this, c)) {
                    break;
                }

//                this.setSyncFlag();
                this.progress += (long)((double) this.progressPerTick * (1.0 + 4.0 * Math.log10((double)(this.laserEnergy / 1000L + 1L))));
                this.laserEnergy = 0L;

                for (int count = 0; count < maxJobCount; ++count) {
                    List<EntityItem> list = this.world.getEntitiesWithinAABB(EntityItem.class, this.aabb, item -> true);
                    long i = (long) ((double) this.progressPerJob * 1.0);
                    ItemStack filter = this.getStackInSlot(this.filterHarvestSlot);

                    if (list.isEmpty()) {
                        continue;
                    }

                    for (EntityItem eitem : list) {
                        ItemStack item = eitem.getItem().copy();
                        ItemStack item1 = item.copy();
                        item1.setCount(1);

                        if (!IFilter.match(filter, item)) {
                            continue;
                        }

                        int slotNum = this.inventoryX.get() * this.inventoryY.get();
                        boolean flag1 = true;

                        while (flag1 && i <= this.progress) {
                            if (UtilTransfer.canProduceItemStack(item1, this.containerItemStacks, 0, slotNum, this.getInventoryStackLimit()) < 1 || i > this.progress) {
                                break;
                            }

                            this.progress -= i;
                            UtilTransfer.produceItemStack(item1, this.containerItemStacks, 0, slotNum, this.getInventoryStackLimit());
                            item.shrink(1);
                            if (item.getCount() <= 0) {
                                eitem.setDead();
                                flag1 = false;
                            } else {
                                eitem.setItem(item);
                            }

                            if (this.getState() == EnumAreaMinerState.OnceMode_Doing) {
                                this.progress = 0L;
                                this.setState(EnumAreaMinerState.Idle);
                                break;
                            }
                        }
                    }
                }
            case Idle:
        }

    }
}
