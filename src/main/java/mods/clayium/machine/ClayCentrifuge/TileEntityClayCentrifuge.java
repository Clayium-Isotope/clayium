package mods.clayium.machine.ClayCentrifuge;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.crafting.IRecipeElement;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class TileEntityClayCentrifuge extends TileEntityClayiumMachine {
    enum CentrifugeSlots {
        MATERIAL,
        PRODUCT_1,
        PRODUCT_2,
        PRODUCT_3,
        PRODUCT_4,
        ENERGY,
    }

    public int resultSlotNum = 4;

    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(CentrifugeSlots.values().length, ItemStack.EMPTY);
        this.listSlotsImport.add(new int[]{ CentrifugeSlots.MATERIAL.ordinal() });
        this.listSlotsExport.add(new int[]{ CentrifugeSlots.PRODUCT_1.ordinal(), CentrifugeSlots.PRODUCT_2.ordinal(), CentrifugeSlots.PRODUCT_3.ordinal(), CentrifugeSlots.PRODUCT_4.ordinal() });
        this.maxAutoExtract = new int[]{-1, 1};
        this.maxAutoInsert = new int[]{-1};
        this.slotsDrop = new int[]{ CentrifugeSlots.MATERIAL.ordinal(), CentrifugeSlots.PRODUCT_1.ordinal(), CentrifugeSlots.PRODUCT_2.ordinal(), CentrifugeSlots.PRODUCT_3.ordinal(), CentrifugeSlots.PRODUCT_4.ordinal(), CentrifugeSlots.ENERGY.ordinal() };
        this.autoInsert = true;
        this.autoExtract = true;
    }

    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);
        if (tier <= 3) {
            this.resultSlotNum = 1;
        } else if (tier <= 4) {
            this.resultSlotNum = 2;
        } else if (tier <= 5) {
            this.resultSlotNum = 3;
        } else {
            this.resultSlotNum = 4;
        }
    }

    public ItemStack getStackInSlot(CentrifugeSlots slot) {
        return super.getStackInSlot(slot.ordinal());
    }

    protected boolean canCraft(ItemStack material) {
        if (material.isEmpty()) {
            return false;
        }

        List<ItemStack> itemstacks = this.doingRecipe.getResults();
        if (this.doingRecipe.getResults().isEmpty()) {
            return false;
        }

        for(int i = 0; i < Math.min(this.resultSlotNum, itemstacks.size()); ++i) {
            if (this.getStackInSlot(i + 1).isEmpty() || itemstacks.get(i).isEmpty()) {
                continue;
            }

            if (!this.getStackInSlot(i + 1).isItemEqual(itemstacks.get(i))) {
                return false;
            }

            int result = this.getStackInSlot(i + 1).getCount() + itemstacks.get(i).getCount();
            if (result > this.getInventoryStackLimit() || result > this.getStackInSlot(i + 1).getMaxStackSize()) {
                return false;
            }
        }

        return true;
    }

    public boolean canProceedCraft() {
        return this.canCraft(this.getStackInSlot(CentrifugeSlots.MATERIAL));
    }

    public void proceedCraft() {
        ++this.craftTime;
//            this.isDoingWork = true;
        if (this.craftTime < this.timeToCraft) return;

        List<ItemStack> itemstacks = this.doingRecipe.getResults();

        for(int i = 0; i < Math.min(this.resultSlotNum, itemstacks.size()); ++i) {
            if (this.getStackInSlot(i + 1).isEmpty()) {
                this.setInventorySlotContents(i + 1, itemstacks.get(i).copy());
            } else if (UtilItemStack.areTypeEqual(this.getStackInSlot(i + 1), itemstacks.get(i))) {
                this.getStackInSlot(i + 1).grow(itemstacks.get(i).getCount());
            }
        }

        this.isDoingWork = setNewRecipe();
    }

    @Override
    protected boolean setNewRecipe() {
        IRecipeElement _recipe = getRecipe(getStackInSlot(CentrifugeSlots.MATERIAL));

        this.craftTime = 0L;

        if (canCraft(_recipe) && IClayEnergyConsumer.compensateClayEnergy(this, _recipe.getEnergy())) {
            this.doingRecipe = _recipe;

            this.debtEnergy = this.doingRecipe.getEnergy();
            this.timeToCraft = this.doingRecipe.getTime();
            getStackInSlot(CentrifugeSlots.MATERIAL).shrink(this.doingRecipe.getStackSizes(getStackInSlot(CentrifugeSlots.MATERIAL))[0]);

            proceedCraft();
            return true;
        }

        this.timeToCraft = 0L;
        this.debtEnergy = 0L;
        this.doingRecipe = RecipeElement.flat();
        return false;
    }

    @Override
    public int getEnergySlot() {
        return CentrifugeSlots.ENERGY.ordinal();
    }
}
