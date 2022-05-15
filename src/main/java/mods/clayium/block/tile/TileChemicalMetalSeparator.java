package mods.clayium.block.tile;

import java.util.List;
import java.util.Random;

import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilTransfer;
import mods.clayium.util.crafting.SimpleMachinesRecipes;
import mods.clayium.util.crafting.WeightedList;
import net.minecraft.item.ItemStack;

public class TileChemicalMetalSeparator
        extends TileClayMachines {
    public static int baseConsumingEnergy = 5000;
    public static int baseCraftTime = 40;
    public static WeightedList<ItemStack> results = new WeightedList();
    private Random random = new Random();


    public void initParams() {
        this.containerItemStacks = new ItemStack[19];
        this.clayEnergySlot = 18;
        this.listSlotsInsert.add(new int[] {0});
        this.listSlotsInsert.add(new int[] {18});
        this.listSlotsExtract.add(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});

        this.insertRoutes = new int[] {-1, 0, -1, 1, -1, -1};
        this.maxAutoExtract = new int[] {-1, 1};
        this.extractRoutes = new int[] {0, -1, -1, -1, -1, -1};
        this.maxAutoInsert = new int[] {-1};
        this.slotsDrop = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18};
        this.autoInsert = true;
        this.autoExtract = true;
        this.recipeId = "";
    }


    public void setRecipe(String recipeId_) {}

    public void refreshRecipe() {}

    public SimpleMachinesRecipes getRecipe() {
        return null;
    }

    public String getNEIOutputId() {
        return "ChemicalMetalSeparator";
    }


    protected boolean canCraft(ItemStack material) {
        if (material == null || !material.isItemEqual(CMaterials.get(CMaterials.IND_CLAY, CMaterials.DUST)))
            return false;
        List<ItemStack> resultlist = results.getResultList();

        for (int i = 0; i < resultlist.size(); i++) {
            if (UtilTransfer.canProduceItemStack((ItemStack) resultlist.get(i), this.containerItemStacks, 1, 17, getInventoryStackLimit()) < ((ItemStack) resultlist
                    .get(i)).stackSize)
                return false;
        }
        return true;
    }


    public boolean canProceedCraft() {
        if (this.containerItemStacks[17] != null) {
            ItemStack itemStack = this.containerItemStacks[17];
            if (canCraft(itemStack)) {
                return true;
            }
            return false;
        }
        ItemStack itemstack = this.containerItemStacks[0];
        if (canCraft(itemstack)) {
            return true;
        }
        return false;
    }


    public void proceedCraft() {
        if (this.containerItemStacks[17] == null) {
            this.machineConsumingEnergy = (long) (baseConsumingEnergy * this.multConsumingEnergy);
        }
        if (consumeClayEnergy(this.machineConsumingEnergy)) {
            if (this.containerItemStacks[17] == null) {
                this.machineTimeToCraft = (long) (baseCraftTime * this.multCraftTime);
                this.containerItemStacks[17] = this.containerItemStacks[0].splitStack(1);
                if ((this.containerItemStacks[0]).stackSize <= 0) this.containerItemStacks[0] = null;
            }
            this.machineCraftTime++;
            this.isDoingWork = true;
            if (this.machineCraftTime >= this.machineTimeToCraft) {
                ItemStack itemstack = (ItemStack) results.put(this.random);
                this.machineCraftTime = 0L;
                this.machineConsumingEnergy = 0L;
                this.containerItemStacks[17] = null;
                if (!this.worldObj.isRemote)
                    UtilTransfer.produceItemStack(itemstack, this.containerItemStacks, 1, 17, getInventoryStackLimit());
                this.syncFlag = true;

                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;
                }
            }
        }
    }
}
