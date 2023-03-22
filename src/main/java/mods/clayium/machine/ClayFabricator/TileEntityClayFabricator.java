package mods.clayium.machine.ClayFabricator;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.SolarClayFabricator.TileEntitySolarClayFabricator;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayFabricator extends TileEntitySolarClayFabricator {
    public float exponentOfNumber;

    public TileEntityClayFabricator() {
    }

    public void initParams() {
        super.initParams();
        this.exponentOfNumber = 0.8F;
        this.slotsDrop = new int[]{0, 1};
    }

    public void initParamsByTier(int tier) {
        this.initCraftTime = 0.01F;
        if (tier >= 8) {
            this.acceptableTier = 11;
            this.baseCraftTime = 5.0F;
            this.exponentOfNumber = 0.85F;
            this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier) * 64.0D / (Math.pow((double)this.baseCraftTime, this.acceptableTier) * Math.pow(64.0D, this.exponentOfNumber)) / (double)(ClayiumCore.multiplyProgressionRate(4.5E7F) / 20.0F));
        }

        if (tier >= 9) {
            this.acceptableTier = 13;
            this.baseCraftTime = 2.0F;
            this.exponentOfNumber = 0.3F;
            this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier) * 64.0D / (Math.pow((double)this.baseCraftTime, this.acceptableTier) * Math.pow(64.0D, this.exponentOfNumber)) / (double)(ClayiumCore.multiplyProgressionRate(1.0E9F) / 20.0F));
        }

        if (tier >= 13) {
            this.acceptableTier = 13;
            this.baseCraftTime = 1.3F;
            this.exponentOfNumber = 0.06F;
            this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier) * 64.0D / (Math.pow((double)this.baseCraftTime, this.acceptableTier) * Math.pow(64.0D, this.exponentOfNumber)) / (double)(ClayiumCore.multiplyProgressionRate(1.0E12F) / 20.0F));
        }

    }

    protected boolean canCraft(int tier, int size) {
        if (tier < 0 || tier > this.acceptableTier) return false;

        return UtilTransfer.canProduceItemStack(IClayEnergy.getCompressedClay(tier, size), this.containerItemStacks, 1, 2, this.getInventoryStackLimit()) >= 1;
    }

    public boolean canProceedCraft() {
        if (this.getStackInSlot(2).isEmpty())
            return !this.getStackInSlot(0).isEmpty() && this.canCraft(IClayEnergy.getTier(this.getStackInSlot(0)), this.getStackInSlot(0).getCount());

        return this.canCraft(IClayEnergy.getTier(this.getStackInSlot(2)), this.getStackInSlot(2).getCount());
    }

    public void proceedCraft() {
        if (this.getStackInSlot(2).isEmpty()) {
            this.timeToCraft = (long)(Math.pow(this.baseCraftTime, IClayEnergy.getTier(this.getStackInSlot(0))) * Math.pow(this.getStackInSlot(0).getCount(), this.exponentOfNumber) * (double)this.multCraftTime);
            this.setInventorySlotContents(2, this.getStackInSlot(0).copy());
        }

        ++this.craftTime;
        this.isDoingWork = true;
        this.containEnergy = (long)(Math.pow(10.0D, IClayEnergy.getTier(this.getStackInSlot(2))) * this.getStackInSlot(2).getCount() * this.craftTime / (double)this.timeToCraft);
        if (this.craftTime >= this.timeToCraft) {
            this.containEnergy = 0L;
            ItemStack result = IClayEnergy.getCompressedClay(IClayEnergy.getTier(this.getStackInSlot(2)));
            result.setCount(this.getStackInSlot(2).getCount());
            this.setInventorySlotContents(2, ItemStack.EMPTY);
            UtilTransfer.produceItemStack(result, this.containerItemStacks, 1, 2, this.getInventoryStackLimit());
            this.craftTime = 0L;
//            if (this.externalControlState > 0) {
//                --this.externalControlState;
//                if (this.externalControlState == 0) {
//                    this.externalControlState = -1;
//                }
//            }
        }

    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if (slot != 0) return true;

        return IClayEnergy.getTier(itemstack) >= 0 && IClayEnergy.getTier(itemstack) <= this.acceptableTier;
    }

    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        this.registerInsertIcons("import");
        this.registerExtractIcons("export");
    }
}
