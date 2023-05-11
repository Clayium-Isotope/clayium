package mods.clayium.machine.ClayCentrifuge;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

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

        this.setImportRoutes(-1, 0, -1, -2, -1, -1);
        this.setExportRoutes(0, -1, -1, -1, -1, -1);
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

        switch (tier) {
            case 3: this.resultSlotNum = 1; break;
            case 4: this.resultSlotNum = 2; break;
            case 5: this.resultSlotNum = 3; break;
            case 6: this.resultSlotNum = 4; break;
            default: this.resultSlotNum = 0;
        }
    }

    public ItemStack getStackInSlot(CentrifugeSlots slot) {
        return super.getStackInSlot(slot.ordinal());
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStacks(recipe.getResults(), this.getContainerItemStacks(),
                CentrifugeSlots.PRODUCT_1.ordinal(), CentrifugeSlots.PRODUCT_1.ordinal() + this.resultSlotNum,
                this.getInventoryStackLimit());
    }

    public boolean canProceedCraft() {
        return IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy, false);

//        return this.canCraft(this.getStackInSlot(CentrifugeSlots.MATERIAL));
    }

    public void proceedCraft() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) return;

        ++this.craftTime;
        if (this.craftTime < this.timeToCraft) return;

        UtilTransfer.produceItemStacks(UtilTransfer.getHardCopy(this.doingRecipe.getResults()), this.getContainerItemStacks(),
                CentrifugeSlots.PRODUCT_1.ordinal(), CentrifugeSlots.PRODUCT_1.ordinal() + this.resultSlotNum,
                this.getInventoryStackLimit());

        this.craftTime = 0L;
        this.debtEnergy = 0L;
        this.timeToCraft = 0L;
        this.doingRecipe = this.getFlat();
    }

    @Override
    public boolean setNewRecipe() {
        this.doingRecipe = this.getRecipe(this.getStackInSlot(CentrifugeSlots.MATERIAL));

        if (!canCraft(this.doingRecipe)) {
            return false;
        }

        this.debtEnergy = this.doingRecipe.getEnergy();
        this.timeToCraft = this.doingRecipe.getTime();
        UtilTransfer.consumeByIngredient(this.doingRecipe.getIngredients().get(0), this.getContainerItemStacks(), CentrifugeSlots.MATERIAL.ordinal());

        return true;
    }

    @Override
    public int getEnergySlot() {
        return CentrifugeSlots.ENERGY.ordinal();
    }
}
