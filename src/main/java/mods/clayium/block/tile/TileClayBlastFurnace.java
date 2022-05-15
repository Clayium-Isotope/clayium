package mods.clayium.block.tile;

import java.util.ArrayList;

import mods.clayium.block.CBlocks;
import mods.clayium.util.crafting.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class TileClayBlastFurnace
        extends TileMultiblockMachines {
    public int recipeTier = 0;


    private Recipes recipe;


    public boolean isConstructed() {
        int sum = 0;
        int num = 0;
        boolean flag = true;
        for (int yy = 0; yy < 2; yy++) {
            for (int xx = -1; xx < 2; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    if (xx != 0 || yy != 0 || zz != 0) {
                        num++;
                        int atier = getMachineTier(xx, yy, zz);
                        if (atier <= 4)
                            flag = false;
                        sum = (int) (sum + Math.pow(2.0D, (16 - atier)));
                        TileEntity te = getTileEntity(xx, yy, zz);
                        if (te != null && (te instanceof TileClayContainerInterface || te instanceof TileRedstoneInterface)) {

                            int[] coord = getRelativeCoord(0, 0, 0);
                            int[] coord1 = getRelativeCoord(xx, yy, zz);
                            ((ISynchronizedInterface) te).setCoreBlockCoord(coord[0] - coord1[0], coord[1] - coord1[1], coord[2] - coord1[2]);
                        }
                    }
                }
            }
        }


        this.recipeTier = Math.max((int) (16.0D - Math.floor(Math.log((sum / num)) / Math.log(2.0D) + 0.5D)), 0);
        return flag;
    }


    public boolean acceptEnergyClay() {
        return true;
    }


    protected void onConstruction() {
        setRenderSyncFlag();
    }


    protected void onDestruction() {
        setRenderSyncFlag();
        this.machineCraftTime = 0L;
        this.containerItemStacks[5] = null;
        this.containerItemStacks[4] = null;
    }

    protected int getMachineTier(int xx, int yy, int zz) {
        Block block = getBlock(xx, yy, zz);
        if (block == CBlocks.blockMachineHull)
            return getBlockMetadata(xx, yy, zz) + 1;
        if (block == CBlocks.blockOverclocker) {
            int[] coord = getRelativeCoord(xx, yy, zz);
            return CBlocks.blockOverclocker.getTier((IBlockAccess) this.worldObj, coord[0], coord[1], coord[2]);
        }
        TileEntity te = getTileEntity(xx, yy, zz);
        if (te != null && te instanceof TileClayContainerInterface)
            return ((TileClayContainerInterface) te).getTier();
        if (te != null && te instanceof TileRedstoneInterface)
            return getBlockTier(xx, yy, zz);
        return -1;
    }


    public void initParamsByTier(int tier) {}


    public int getRecipeTier() {
        return this.recipeTier;
    }


    protected int resultSlotNum = 2;


    public void refreshRecipe() {
        Recipes recipe = Recipes.GetRecipes(this.recipeId);
        if (recipe != null) this.recipe = recipe;

    }

    public void initParams() {
        this.containerItemStacks = new ItemStack[7];
        this.clayEnergySlot = 6;
        this.listSlotsInsert = new ArrayList<int[]>();
        this.listSlotsExtract = new ArrayList<int[]>();
        this.listSlotsInsert.add(new int[] {0});
        this.listSlotsInsert.add(new int[] {1});
        this.listSlotsInsert.add(new int[] {0, 1});
        this.listSlotsInsert.add(new int[] {6});
        this.listSlotsExtract.add(new int[] {2});
        this.listSlotsExtract.add(new int[] {3});
        this.listSlotsExtract.add(new int[] {2, 3});
        this.insertRoutes = new int[] {-1, 2, -1, 3, -1, -1};
        this.maxAutoExtract = new int[] {64, 64, 64, 1};
        this.extractRoutes = new int[] {2, -1, -1, -1, -1, -1};
        this.maxAutoInsert = new int[] {64, 64, 64};
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.slotsDrop = new int[] {0, 1, 2, 3, 6};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    protected boolean canCraft(ItemStack[] materials) {
        int method = 0;
        if (materials == null || this.recipe == null) {
            return false;
        }
        ItemStack[] itemstacks = this.recipe.getResult(materials, method, this.recipeTier);
        if (itemstacks == null) return false;
        for (int i = 0; i < Math.min(this.resultSlotNum, itemstacks.length); i++) {
            if (this.containerItemStacks[i + 2] != null && itemstacks[i] != null) {
                if (!this.containerItemStacks[i + 2].isItemEqual(itemstacks[i])) return false;
                int result = (this.containerItemStacks[i + 2]).stackSize + (itemstacks[i]).stackSize;
                if (result > getInventoryStackLimit() || result > this.containerItemStacks[i + 2].getMaxStackSize())
                    return false;
            }
        }
        return true;
    }

    protected int[] getCraftPermutation(ItemStack[] materials) {
        if (canCraft(materials)) return new int[] {0, 1};
        if (canCraft(new ItemStack[] {materials[1], materials[0]})) return new int[] {1, 0};
        if (canCraft(new ItemStack[] {materials[0]})) return new int[] {0};
        if (canCraft(new ItemStack[] {materials[1]})) return new int[] {1};
        return null;
    }


    public boolean canProceedCraftWhenConstructed() {
        if (this.containerItemStacks[4] != null || this.containerItemStacks[5] != null) {
            ItemStack[] arrayOfItemStack = {this.containerItemStacks[4], this.containerItemStacks[5]};
            if (getCraftPermutation(arrayOfItemStack) != null) {
                return true;
            }
            return false;
        }
        ItemStack[] itemstacks = {this.containerItemStacks[0], this.containerItemStacks[1]};
        if (getCraftPermutation(itemstacks) != null) {
            return true;
        }
        return false;
    }


    public void proceedCraft() {
        int method = 0;


        if (this.containerItemStacks[4] == null && this.containerItemStacks[5] == null) {
            ItemStack[] mats = {this.containerItemStacks[0], this.containerItemStacks[1]};
            int[] perm = getCraftPermutation(mats);
            if (perm == null) {
                throw new RuntimeException("Invalid recipe reference : The permutation variable is null!");
            }
            ItemStack[] itemstacks = new ItemStack[perm.length];
            for (int i = 0; i < perm.length; i++) {
                itemstacks[i] = mats[perm[i]];
            }
            this.machineConsumingEnergy = (long) ((float) this.recipe.getEnergy(itemstacks, method, this.recipeTier) * this.multConsumingEnergy);
        }
        if (consumeClayEnergy(this.machineConsumingEnergy)) {
            if (this.containerItemStacks[4] == null && this.containerItemStacks[5] == null) {
                ItemStack[] mats = {this.containerItemStacks[0], this.containerItemStacks[1]};
                int[] perm = getCraftPermutation(mats);
                if (perm == null) {
                    throw new RuntimeException("Invalid recipe reference : The permutation variable is null!");
                }
                ItemStack[] itemstacks = new ItemStack[perm.length];
                for (int i = 0; i < perm.length; i++) {
                    itemstacks[i] = mats[perm[i]];
                }
                this.machineTimeToCraft = (long) ((float) this.recipe.getTime(itemstacks, method, this.recipeTier) * this.multCraftTime);
                int[] consumedStackSize = this.recipe.getConsumedStackSize(itemstacks, method, this.recipeTier);
                for (int j = 0; j < perm.length; j++) {
                    this.containerItemStacks[j + 4] = this.containerItemStacks[perm[j]].splitStack(consumedStackSize[j]);
                    if ((this.containerItemStacks[perm[j]]).stackSize <= 0) this.containerItemStacks[perm[j]] = null;
                }
            }
            this.machineCraftTime++;
            this.isDoingWork = true;
            if (this.machineCraftTime >= this.machineTimeToCraft) {
                ItemStack[] mats = {this.containerItemStacks[4], this.containerItemStacks[5]};
                int[] perm = getCraftPermutation(mats);
                if (perm == null) {
                    throw new RuntimeException("Invalid recipe reference : The permutation variable is null!");
                }
                ItemStack[] itemstacks = new ItemStack[perm.length];
                for (int i = 0; i < perm.length; i++) {
                    itemstacks[i] = mats[perm[i]];
                }
                ItemStack[] results = this.recipe.getResult(itemstacks, method, this.recipeTier);
                int[] consumedStackSize = this.recipe.getConsumedStackSize(itemstacks, method, this.recipeTier);
                this.machineCraftTime = 0L;
                this.machineConsumingEnergy = 0L;
                int j;
                for (j = 0; j < Math.min(this.resultSlotNum, results.length); j++) {
                    if (this.containerItemStacks[j + 2] == null) {
                        this.containerItemStacks[j + 2] = results[j].copy();
                    } else if (this.containerItemStacks[j + 2].getItem() == results[j].getItem()) {
                        (this.containerItemStacks[j + 2]).stackSize += (results[j]).stackSize;
                    }
                }
                for (j = 0; j < perm.length; j++) {
                    if (((this.containerItemStacks[j + 4]).stackSize -= consumedStackSize[j]) <= 0)
                        this.containerItemStacks[j + 4] = null;

                }
                if (this.externalControlState > 0) {
                    this.externalControlState--;
                    if (this.externalControlState == 0) this.externalControlState = -1;
                }
            }
        }
    }
}
