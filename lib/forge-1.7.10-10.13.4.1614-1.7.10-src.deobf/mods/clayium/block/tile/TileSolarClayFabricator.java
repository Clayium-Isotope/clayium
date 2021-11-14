package mods.clayium.block.tile;

import mods.clayium.block.CBlocks;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import mods.clayium.util.crafting.SimpleMachinesRecipes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class TileSolarClayFabricator extends TileClayMachines {
    public int acceptableTier;

    public void initParams() {
        super.initParams();
        this.insertRoutes = new int[] {-1, -1, -1, 0, -1, -1};
        this.extractRoutes = new int[] {-1, -1, 0, -1, -1, -1};
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = new ItemStack[3];
        this.listSlotsInsert.clear();
        this.listSlotsExtract.clear();
        this.listSlotsInsert.add(new int[] {0});
        this.listSlotsExtract.add(new int[] {1});
        this.slotsDrop = new int[] {0, 1, 2};
        this.acceptableTier = 4;
        this.clayEnergySlot = -1;
        this.maxAutoExtract = new int[] {64};
        this.maxAutoInsert = new int[] {64};
        this.autoExtractInterval = this.autoInsertInterval = 4;
        this.recipeId = "";
        this.initCraftTime = 0.3F;
        this.baseCraftTime = 4.0F;
    }


    public float baseCraftTime;


    public void initParamsByTier(int tier) {
        this
                .initCraftTime = (float) (Math.pow(10.0D, (this.acceptableTier + 1)) * (this.baseCraftTime - 1.0F) / this.baseCraftTime * (Math.pow(this.baseCraftTime, this.acceptableTier) - 1.0D) / (ClayiumCore.multiplyProgressionRateF(5000.0F) / 20.0F));
        if (tier >= 6) {
            this.acceptableTier = 6;
            this.baseCraftTime = 3.0F;
            this.initCraftTime = 0.01F;
            this
                    .initCraftTime = (float) (Math.pow(10.0D, (this.acceptableTier + 1)) * (this.baseCraftTime - 1.0F) / this.baseCraftTime * (Math.pow(this.baseCraftTime, this.acceptableTier) - 1.0D) / (ClayiumCore.multiplyProgressionRateF(50000.0F) / 20.0F));
        }
        if (tier >= 7) {
            this.acceptableTier = 9;
            this.baseCraftTime = 2.0F;
            this.initCraftTime = 0.07F;
            this
                    .initCraftTime = (float) (Math.pow(10.0D, (this.acceptableTier + 1)) * (this.baseCraftTime - 1.0F) / this.baseCraftTime * (Math.pow(this.baseCraftTime, this.acceptableTier) - 1.0D) / (ClayiumCore.multiplyProgressionRateF(3000000.0F) / 20.0F));
        }
    }


    public void setRecipe(String recipeId_) {}

    public void refreshRecipe() {}

    public SimpleMachinesRecipes getRecipe() {
        return null;
    }


    public void openInventory() {}


    public void closeInventory() {}


    protected boolean canCraft(int tier) {
        return (tier >= 0 && tier <= this.acceptableTier) ? ((UtilTransfer.canProduceItemStack(getCompressedClay(tier + 1), this.containerItemStacks, 1, 2, getInventoryStackLimit()) >= 1)) : false;
    }


    public boolean canProceedCraft() {
        for (int y = 255; y > this.yCoord; y--) {
            if (getWorldObj().getBlockLightOpacity(this.xCoord, y, this.zCoord) > 0) {
                return false;
            }
        }
        return (this.containerItemStacks[2] == null) ? canCraft(getTierOfCompressedClay(this.containerItemStacks[0])) :
                canCraft(getTierOfCompressedClay(this.containerItemStacks[2]));
    }


    public void proceedCraft() {
        if (this.containerItemStacks[2] == null) {
            this.machineTimeToCraft = (long) (Math.pow(this.baseCraftTime, getTierOfCompressedClay(this.containerItemStacks[0])) * this.multCraftTime);
            this.containerItemStacks[2] = this.containerItemStacks[0].splitStack(1);
            if ((this.containerItemStacks[0]).stackSize <= 0) this.containerItemStacks[0] = null;
        }
        this.machineCraftTime++;
        this.isDoingWork = true;
        this.clayEnergy = (long) (Math.pow(10.0D, (getTierOfCompressedClay(this.containerItemStacks[2]) + 1)) * this.machineCraftTime / this.machineTimeToCraft);
        if (this.machineCraftTime >= this.machineTimeToCraft) {
            this.clayEnergy = 0L;
            ItemStack result = getCompressedClay(getTierOfCompressedClay(this.containerItemStacks[2]) + 1);
            this.containerItemStacks[2] = null;
            UtilTransfer.produceItemStack(result, this.containerItemStacks, 1, 2, getInventoryStackLimit());
            this.machineCraftTime = 0L;

            if (this.externalControlState > 0) {
                this.externalControlState--;
                if (this.externalControlState == 0) this.externalControlState = -1;

            }
        }
    }


    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return (slot == 0) ? ((getTierOfCompressedClay(itemstack) >= 0 && getTierOfCompressedClay(itemstack) <= this.acceptableTier)) : true;
    }

    public static ItemStack getCompressedClay(int tier, int size) {
        return (tier == 0) ? new ItemStack(Blocks.clay, size) : new ItemStack(CBlocks.blockCompressedClay, size, tier - 1);
    }

    public static ItemStack getCompressedClay(int tier) {
        return getCompressedClay(tier, 1);
    }

    public static int getTierOfCompressedClay(ItemStack itemstack) {
        return getTierOfCompressedClay(itemstack, true);
    }

    public static int getTierOfCompressedClay(ItemStack itemstack, boolean acceptOthers) {
        if (itemstack == null) return -1;
        for (int i = 0; i < 17; i++) {
            if (UtilItemStack.areItemDamageEqual(getCompressedClay(i), itemstack)) {
                return i;
            }
        }
        if (!acceptOthers) return -1;
        ItemStack sand = new ItemStack((Block) Blocks.sand);
        if (UtilItemStack.areItemDamageEqual(sand, itemstack)) return 2;
        return UtilItemStack.hasOreName(itemstack, CMaterials.getOD(CMaterials.LITHIUM, CMaterials.INGOT).getOreName()) ? 8 : -1;
    }
}
