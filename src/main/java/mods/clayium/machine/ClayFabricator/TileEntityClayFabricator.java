package mods.clayium.machine.ClayFabricator;

import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.SolarClayFabricator.TileEntitySolarClayFabricator;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTransfer;
import mods.clayium.util.exception.IllegalTierException;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TileEntityClayFabricator extends TileEntitySolarClayFabricator {
    public float exponentOfNumber;

    public void initParams() {
        super.initParams();
        this.containerItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);

        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.exponentOfNumber = 0.8F;
    }

    public void initParamsByTier(TierPrefix tier) {
        this.tier = tier;
        this.setDefaultTransportation(tier);

        this.initCraftTime = 0.01F;

        float craftTimeDivisor;

        switch (tier) {
            case clayium:
                this.acceptableTier = TierPrefix.pureAntimatter;
                this.baseCraftTime = 5.0F;
                this.exponentOfNumber = 0.85F;
                craftTimeDivisor = 4.5E7F;
                break;
            case ultimate:
                this.acceptableTier = TierPrefix.OPA;
                this.baseCraftTime = 2.0F;
                this.exponentOfNumber = 0.3F;
                craftTimeDivisor = 1.0E9F;
                break;
            case OPA:
                this.acceptableTier = TierPrefix.OPA;
                this.baseCraftTime = 1.3F;
                this.exponentOfNumber = 0.06F;
                craftTimeDivisor = 1.0E12F;
                break;
            default:
                throw new IllegalTierException();
        }

        this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier.meta()) * 64.0D / (Math.pow(this.baseCraftTime, this.acceptableTier.meta()) * Math.pow(64.0D, this.exponentOfNumber)) / (double)(ClayiumConfiguration.multiplyProgressionRate(craftTimeDivisor) / 20.0F));
    }

    @Override
    public boolean canCraft(ItemStack material) {
        if (material.isEmpty()) return false;

        return isTierValid(IClayEnergy.getTier(material)) && UtilTransfer.canProduceItemStack(material, this.getContainerItemStacks(), 1, this.getInventoryStackLimit()) > 0;
    }

    public boolean canProceedCraft() {
        return true;

//        if (this.getStackInSlot(2).isEmpty())
//            return this.canCraft(this.getStackInSlot(0));
//
//        return this.canCraft(this.getStackInSlot(2));
    }

    public void proceedCraft() {
        ++this.craftTime;
        this.containEnergy().set((long) (Math.pow(10.0D, IClayEnergy.getTier(this.getStackInSlot(2)).meta()) * this.getStackInSlot(2).getCount() * this.craftTime / (double)this.timeToCraft));
        if (this.craftTime < this.timeToCraft) {
            return;
        }

        UtilTransfer.produceItemStack(this.getStackInSlot(2).copy(), this.getContainerItemStacks(), 1, this.getInventoryStackLimit());

        this.containEnergy().clear();
        this.craftTime = 0L;
//            if (this.externalControlState > 0) {
//                --this.externalControlState;
//                if (this.externalControlState == 0) {
//                    this.externalControlState = -1;
//                }
//            }

    }

    @Override
    public boolean setNewRecipe() {
        if (!this.canCraft(this.getStackInSlot(0)))
            return false;

        this.craftTime = 1;
        this.timeToCraft = (long)(Math.pow(this.baseCraftTime, IClayEnergy.getTier(this.getStackInSlot(0)).meta()) * Math.pow(this.getStackInSlot(0).getCount(), this.exponentOfNumber) * (double)this.multCraftTime);
        this.setInventorySlotContents(2, this.getStackInSlot(0).copy());

        return true;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return slot == 0 && isTierValid(IClayEnergy.getTier(itemstack));
    }
}
