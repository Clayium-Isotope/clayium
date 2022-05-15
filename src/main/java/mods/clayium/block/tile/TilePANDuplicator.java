package mods.clayium.block.tile;

import java.util.Set;

import mods.clayium.item.CMaterials;
import mods.clayium.pan.IPANComponent;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;

public class TilePANDuplicator
        extends TileClayMachines
        implements IPANComponent {
    protected TilePANCore core;
    protected int remainingTime = -1;
    protected static ItemStack antimatter;

    public void initParams() {
        this.containerItemStacks = new ItemStack[6];
        this.clayEnergySlot = 5;
        this.listSlotsInsert.add(new int[] {0});
        this.listSlotsInsert.add(new int[] {1});
        this.listSlotsInsert.add(new int[] {0, 1});
        this.listSlotsInsert.add(new int[] {5});
        this.listSlotsExtract.add(new int[] {2});
        this.insertRoutes = new int[] {-1, 2, -1, 3, -1, -1};
        this.maxAutoExtract = new int[] {-1, -1, -1, 1};
        this.extractRoutes = new int[] {0, -1, -1, -1, -1, -1};
        this.maxAutoInsert = new int[] {-1};
        this.slotsDrop = new int[] {0, 1, 2, 5};
        this.autoInsert = true;
        this.autoExtract = true;
    }


    public void updateEntity() {
        super.updateEntity();
        if (this.remainingTime >= 0) {
            this.remainingTime--;
        } else {
            this.core = null;
        }
    }


    public void setPANCore(TilePANCore tile, int time) {
        this.remainingTime = time;
        this.core = tile;
    }


    protected static ItemStack getAntimatter() {
        if (antimatter == null)
            antimatter = CMaterials.get(CMaterials.ANTIMATTER, CMaterials.GEM);
        return antimatter;
    }

    protected TilePANCore.ItemStackWithEnergy getResult(ItemStack[] materials) {
        if (materials == null || this.core == null || materials.length != 2)
            return null;
        if (!UtilItemStack.areTypeEqual(materials[0], getAntimatter()) && !UtilItemStack.areTypeEqual(materials[1], getAntimatter()))
            return null;
        Set<TilePANCore.ItemStackWithEnergy> conv = this.core.getConversionItemSet();
        if (conv == null)
            return null;
        for (TilePANCore.ItemStackWithEnergy item : conv) {
            if (UtilItemStack.areTypeEqual(item.itemstack, materials[0]) || UtilItemStack.areTypeEqual(item.itemstack, materials[1]))
                return item;
        }
        return null;
    }


    public TilePANCore.ItemStackWithEnergy getResult() {
        if (this.containerItemStacks[3] != null || this.containerItemStacks[4] != null) {
            ItemStack[] arrayOfItemStack = {this.containerItemStacks[3], this.containerItemStacks[4]};
            return getResult(arrayOfItemStack);
        }
        ItemStack[] itemstacks = {this.containerItemStacks[0], this.containerItemStacks[1]};
        return getResult(itemstacks);
    }


    public boolean canProceedCraft() {
        TilePANCore.ItemStackWithEnergy result = getResult();
        return (result != null && UtilTransfer.canProduceItemStack(result.itemstack, this.containerItemStacks, 2, getInventoryStackLimit()) >= 1);
    }

    public void proceedCraft() {
        TilePANCore.ItemStackWithEnergy result0 = getResult();

        this.machineConsumingEnergy = (long) (100000.0F * this.multConsumingEnergy);
        double rest = result0.consumption - this.machineCraftTime * this.machineConsumingEnergy;
        this.machineTimeToCraft = (long) (result0.consumption / this.machineConsumingEnergy);
        long consumption = (rest > this.machineConsumingEnergy) ? this.machineConsumingEnergy : (long) rest;
        if (consumeClayEnergy(consumption)) {
            if (this.containerItemStacks[3] == null && this.containerItemStacks[4] == null)
                for (int i = 0; i < 2; i++) {
                    this.containerItemStacks[i + 3] = this.containerItemStacks[i].splitStack(1);
                    if (!UtilItemStack.areTypeEqual(this.containerItemStacks[i], getAntimatter())) {
                        (this.containerItemStacks[i]).stackSize++;
                    }
                    if ((this.containerItemStacks[i]).stackSize <= 0) this.containerItemStacks[i] = null;

                }
            this.machineCraftTime++;
            this.isDoingWork = true;
            if (rest <= this.machineConsumingEnergy) {
                this.containerItemStacks[3] = null;
                this.containerItemStacks[4] = null;
                this.machineCraftTime = 0L;
                this.machineConsumingEnergy = 0L;
                UtilTransfer.produceItemStack(result0.itemstack, this.containerItemStacks, 2, getInventoryStackLimit());

                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;
                }
            }
        }
    }
}
