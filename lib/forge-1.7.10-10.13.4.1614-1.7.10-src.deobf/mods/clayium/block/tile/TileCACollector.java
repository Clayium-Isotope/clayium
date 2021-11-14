package mods.clayium.block.tile;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilTransfer;

public class TileCACollector
        extends TileCAMachines implements INormalInventory {
    public int inventoryX = 0;
    public int inventoryY = 0;
    public long baseCraftTime = 10000L;

    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        this.extractRoutes = new int[] {-1, -1, 0, -1, -1, -1};
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = new net.minecraft.item.ItemStack[36];
        this.listSlotsInsert.clear();
        this.clayEnergySlot = -1;
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.recipeId = "";

        this.machineTimeToCraft = this.baseCraftTime;
    }


    public void initParamsByTier(int tier) {
        switch (tier) { }
        this.inventoryX = this.inventoryY = 3;

        int slotNum = this.inventoryX * this.inventoryY;

        int[] slots = new int[slotNum];
        int[] slots2 = new int[slotNum];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
            slots2[i] = slots.length - i - 1;
        }
        this.listSlotsExtract.clear();
        this.listSlotsExtract.add(slots2);
        this.slotsDrop = slots;
    }


    public boolean canProceedCraft() {
        return true;
    }


    public void proceedCraft() {
        this.machineTimeToCraft = (long) ((float) this.baseCraftTime * this.multCraftTime);
        this.machineCraftTime = (long) (this.machineCraftTime + ClayiumCore.multiplyProgressionRateD(getResonance() - 1.0D));
        this.isDoingWork = true;
        int slotNum = this.inventoryX * this.inventoryY;
        this.machineCraftTime = Math.min(this.machineCraftTime, this.machineTimeToCraft * slotNum * getInventoryStackLimit());
        boolean res = true;
        while (this.machineCraftTime >= this.machineTimeToCraft && res) {
            int n = Math.min((int) (this.machineCraftTime / this.machineTimeToCraft), 64);
            this.machineCraftTime -= n * this.machineTimeToCraft;

            res = (UtilTransfer.produceItemStack(CMaterials.get(CMaterials.ANTIMATTER, CMaterials.GEM, n), this.containerItemStacks, 0, slotNum, getInventoryStackLimit()) == null);
        }
        if (this.externalControlState > 0) {
            this.externalControlState--;
            if (this.externalControlState == 0) this.externalControlState = -1;

        }
    }

    public int getInventoryX() {
        return this.inventoryX;
    }


    public int getInventoryY() {
        return this.inventoryY;
    }


    public int getInventoryStart() {
        return 0;
    }


    public int getInventoryP() {
        return 1;
    }
}
