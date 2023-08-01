package mods.clayium.machine.AutoClayCondenser;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.RecipeProvider;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTransfer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TileEntityAutoClayCondenser extends TileEntityClayiumMachine {
    /*package-private*/ static final int SAMPLE_SLOT = 20;
    private int mergeFrom = -1;
    private boolean isStable = false;

    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(21, ItemStack.EMPTY);
        this.setImportRoutes(-1, 0, -1, -1, -1, -1);
        this.setExportRoutes(0, -1, -1, -1, -1, -1);
        this.listSlotsImport.add(IntStream.range(0, 15).toArray());
        this.listSlotsExport.add(IntStream.range(0, 20).toArray());
        this.slotsDrop = IntStream.range(0, 20).toArray();
        this.autoInsert = true;
        this.autoExtract = true;

        this.debtEnergy = 0;
    }

    @Override
    public int getEnergySlot() {
        return -1;
    }

    @Override
    public boolean isDoingWork() {
        return this.craftTime > 0;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        super.initParamsByTier(tier);
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
    }

    public void sortInventory() {
        List<Integer> num = this.getQuantityOfClay();

        ItemStack sample = this.getStackInSlot(SAMPLE_SLOT).copy();
        this.clear();
        this.setInventorySlotContents(SAMPLE_SLOT, sample.copy());

        int maxSize = Math.min(64, this.getInventoryStackLimit());
        int size;

        for (int i = 0, j = 0; j < num.size() && i < 20; j++) {
            size = num.get(j);
            if (size == 0) continue;

            while (size > maxSize && i < 20) {
                size -= maxSize;
                this.setInventorySlotContents(i++, IClayEnergy.getCompressedClay(j, maxSize));
            }

            this.setInventorySlotContents(i++, IClayEnergy.getCompressedClay(j, size));
        }

//        this.setSyncFlag();
    }

    public List<Integer> getQuantityOfClay() {
        // [0, 0, ..., 0] cuz primitive init
        // + 1 for minecraft:clay
        int[] arr = new int[ClayiumBlocks.compressedClay.size() + 1];

        for (ItemStack stack : this.getContainerItemStacks()) {
            if (IClayEnergy.getTier(stack) == -1) continue;

            arr[IClayEnergy.getTier(stack)] += stack.getCount();
        }

        return Arrays.stream(arr).boxed().collect(Collectors.toList());
    }

    protected boolean canCraft(int tier) {
        return UtilTransfer.canProduceItemStack(IClayEnergy.getCompressedClay(tier), this.containerItemStacks, 0, 20, this.getInventoryStackLimit()) >= 1;
    }

    @Override
    public boolean canCraft(ItemStack material) {
        if (material.getItem() == Item.getItemFromBlock(Blocks.CLAY))
            return true;

        if (material.getItem() instanceof IClayEnergy)
            return this.canCraft(IClayEnergy.getTier(material) + 1);

        return false;
    }

    @Override
    public void update() {
        if (!this.world.isRemote) return;

        this.doTransfer();
        if (this.isStable) return;

        RecipeProvider.update(this);
        this.sortInventory();

        long energy = 0;
        for (ItemStack stack : this.getContainerItemStacks()) {
            if (stack.getItem() instanceof IClayEnergy) {
                energy += ((IClayEnergy) stack.getItem()).getClayEnergy() * stack.getCount();
            }
        }
        this.setContainEnergy(energy);
    }

    @Override
    public boolean canProceedCraft() {
        return true;
        /*this.externalControlState >= 0 &&*/
//        if (!this.isDoingWork()) {
//            return this.getConsumedClay() != -1;
//        }
//        return this.canCraft(this.mergeFrom + 1);
    }

    public int getConsumedClay() {
        List<Integer> num = this.getQuantityOfClay();
        int max = ClayiumBlocks.compressedClay.size();

        if (!this.getStackInSlot(SAMPLE_SLOT).isEmpty() && IClayEnergy.getTier(this.getStackInSlot(SAMPLE_SLOT)) != -1) {
            max = IClayEnergy.getTier(this.getStackInSlot(SAMPLE_SLOT));
        }

        for (int j = max - 1; j >= 0; --j) {
            if (num.get(j) >= 9 && this.canCraft(j + 1)) {
                return j;
            }
        }

        this.isStable = true;
        return -1;
    }

    @Override
    public void proceedCraft() {
        ++this.craftTime;
        if (this.craftTime < this.timeToCraft) {
            return;
        }

        UtilTransfer.produceItemStack(IClayEnergy.getCompressedClay(this.mergeFrom + 1), this.containerItemStacks, 0, 20, this.getInventoryStackLimit());
        this.craftTime = 0L;
        this.mergeFrom = -1;
//                if (this.externalControlState > 0) {
//                    --this.externalControlState;
//                    if (this.externalControlState == 0) {
//                        this.externalControlState = -1;
//                    }
//                }

    }

    @Override
    public boolean setNewRecipe() {
        this.timeToCraft = (long)(1.0F * this.multCraftTime);
        this.mergeFrom = this.getConsumedClay();
        if (this.mergeFrom == -1) return false;

        UtilTransfer.consumeItemStack(IClayEnergy.getCompressedClay(this.mergeFrom, 9), this.containerItemStacks, 0, 20);
        return true;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side) {
        return super.canExtractItem(slot, itemstack, side) && IClayEnergy.getTier(itemstack) >= IClayEnergy.getTier(this.getStackInSlot(SAMPLE_SLOT));
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return IClayEnergy.getTier(itemstack) != -1;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.isStable = false;
    }
}
