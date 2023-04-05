package mods.clayium.machine.ClayFabricator;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.SolarClayFabricator.TileEntitySolarClayFabricator;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TileEntityClayFabricator extends TileEntitySolarClayFabricator {
    public float exponentOfNumber;

    public void initParams() {
        super.initParams();
        this.containerItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);

        this.setImportRoutes(-1, 0, -1, -1, -1, -1);
        this.setExportRoutes(0, -1, -1, -1, -1, -1);
        this.exponentOfNumber = 0.8F;
    }

    public void initParamsByTier(int tier) {
        this.tier = tier;
        this.setDefaultTransportation(tier);

        this.initCraftTime = 0.01F;

        switch (tier) {
            case 8:
                this.acceptableTier = 11;
                this.baseCraftTime = 5.0F;
                this.exponentOfNumber = 0.85F;
                this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier) * 64.0D / (Math.pow(this.baseCraftTime, this.acceptableTier) * Math.pow(64.0D, this.exponentOfNumber)) / (double)(ClayiumCore.multiplyProgressionRate(4.5E7F) / 20.0F));
                break;
            case 9:
                this.acceptableTier = 13;
                this.baseCraftTime = 2.0F;
                this.exponentOfNumber = 0.3F;
                this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier) * 64.0D / (Math.pow(this.baseCraftTime, this.acceptableTier) * Math.pow(64.0D, this.exponentOfNumber)) / (double)(ClayiumCore.multiplyProgressionRate(1.0E9F) / 20.0F));
                break;
            case 13:
                this.acceptableTier = 13;
                this.baseCraftTime = 1.3F;
                this.exponentOfNumber = 0.06F;
                this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier) * 64.0D / (Math.pow(this.baseCraftTime, this.acceptableTier) * Math.pow(64.0D, this.exponentOfNumber)) / (double)(ClayiumCore.multiplyProgressionRate(1.0E12F) / 20.0F));
                break;
        }
    }

    @Override
    public boolean canCraft(ItemStack material) {
        if (material.isEmpty()) return false;

        int tier = IClayEnergy.getTier(material);
        if (tier < 0 || tier > this.acceptableTier) return false;

        return UtilTransfer.canProduceItemStack(material, this.getContainerItemStacks(), 1, this.getInventoryStackLimit()) > 0;
    }

    public boolean canProceedCraft() {
        if (this.getStackInSlot(2).isEmpty())
            return this.canCraft(this.getStackInSlot(0));

        return this.canCraft(this.getStackInSlot(2));
    }

    public void proceedCraft() {
        ++this.craftTime;
        this.setContainEnergy((long)(Math.pow(10.0D, IClayEnergy.getTier(this.getStackInSlot(2))) * this.getStackInSlot(2).getCount() * this.craftTime / (double)this.timeToCraft));
        if (this.craftTime < this.timeToCraft) {
            return;
        }

        UtilTransfer.produceItemStack(this.getStackInSlot(2), this.getContainerItemStacks(), 1, this.getInventoryStackLimit());
        this.setInventorySlotContents(2, ItemStack.EMPTY);

        this.setDoingWork(false);
        this.setContainEnergy(0L);
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
        if (this.getStackInSlot(0).isEmpty() || !this.canCraft(this.getStackInSlot(0)))
            return false;

        this.timeToCraft = (long)(Math.pow(this.baseCraftTime, IClayEnergy.getTier(this.getStackInSlot(0))) * Math.pow(this.getStackInSlot(0).getCount(), this.exponentOfNumber) * (double)this.multCraftTime);
        this.setInventorySlotContents(2, this.getStackInSlot(0).copy());

        return true;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if (slot != 0) return false;

        return IClayEnergy.getTier(itemstack) >= 0 && IClayEnergy.getTier(itemstack) <= this.acceptableTier;
    }
}
