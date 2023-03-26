package mods.clayium.machine.SolarClayFabricator;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.EnumSkyBlock;

public class TileEntitySolarClayFabricator extends TileEntityClayiumMachine {
    public int acceptableTier;
    public float baseCraftTime;

    private int raiseFrom;

    public void initParams() {
        super.initParams();
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = NonNullList.withSize(2, ItemStack.EMPTY);

        this.setImportRoutes(-1, -1, -1, 0, -1, -1);
        this.setExportRoutes(-1, -1, 0, -1, -1, -1);
        this.listSlotsImport.clear();
        this.listSlotsExport.clear();
        this.listSlotsImport.add(new int[]{ 0 });
        this.listSlotsExport.add(new int[]{ 1 });
        this.slotsDrop = new int[]{ 0, 1 };
        this.acceptableTier = 4;
        this.maxAutoExtract = new int[]{64};
        this.maxAutoInsert = new int[]{64};
        this.initCraftTime = 0.3F;
        this.baseCraftTime = 4.0F;

        this.debtEnergy = 0;
    }

    @Override
    public int getEnergySlot() {
        return -1;
    }

    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);

        this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier + 1) * (double)(this.baseCraftTime - 1.0F) / ((double)this.baseCraftTime * (Math.pow(this.baseCraftTime, this.acceptableTier) - 1.0D)) / (double)(ClayiumCore.multiplyProgressionRate(5000.0F) / 20.0F));
        if (tier >= 6) {
            this.acceptableTier = 6;
            this.baseCraftTime = 3.0F;
            this.initCraftTime = 0.01F;
            this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier + 1) * (double)(this.baseCraftTime - 1.0F) / ((double)this.baseCraftTime * (Math.pow(this.baseCraftTime, this.acceptableTier) - 1.0D)) / (double)(ClayiumCore.multiplyProgressionRate(50000.0F) / 20.0F));
        }

        if (tier >= 7) {
            this.acceptableTier = 9;
            this.baseCraftTime = 2.0F;
            this.initCraftTime = 0.07F;
            this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier + 1) * (double)(this.baseCraftTime - 1.0F) / ((double)this.baseCraftTime * (Math.pow(this.baseCraftTime, this.acceptableTier) - 1.0D)) / (double)(ClayiumCore.multiplyProgressionRate(3000000.0F) / 20.0F));
        }

        this.autoExtractInterval = this.autoInsertInterval = 4;
    }

    protected boolean canCraft(int tier) {
        if (tier < 0 || tier > this.acceptableTier) return false;

//        return this.getStackInSlot(MachineSlots.PRODUCT).isEmpty()
//                || (IClayEnergy.getClayEnergy(this.getStackInSlot(MachineSlots.PRODUCT)) == tier + 1
//                    && this.getStackInSlot(MachineSlots.PRODUCT).getCount() < this.getInventoryStackLimit());
        return UtilTransfer.canProduceItemStack(IClayEnergy.getCompressedClay(tier + 1), this.containerItemStacks, 1, 2, this.getInventoryStackLimit()) >= 1;
    }

    @Override
    protected boolean canCraft(ItemStack material) {
        return this.canCraft(getTierOfCompressedClay(material));
    }

    public boolean canProceedCraft() {
        if (this.world.getLightFor(EnumSkyBlock.SKY, this.getPos().up()) < 15 || this.world.getSkylightSubtracted() > 0) return false;

        // PRODUCT スロットが適切な状態でなければ、raiseFrom == -1
        return this.raiseFrom != -1 || this.canCraft(this.getStackInSlot(MachineSlots.MATERIAL));
//        return this.getStackInSlot(WORKING_SLOT).isEmpty() ? this.canCraft(getTierOfCompressedClay(this.containerItemStacks.get(0))) : this.canCraft(getTierOfCompressedClay(this.getStackInSlot(WORKING_SLOT)));
    }

    public void proceedCraft() {
        ++this.craftTime;
        this.containEnergy = (long)(Math.pow(10.0D, this.raiseFrom + 1) * (double)this.craftTime / (double)this.timeToCraft);
        if (this.craftTime < this.timeToCraft) {
            return;
        }

        this.containEnergy = 0L;
        UtilTransfer.produceItemStack(IClayEnergy.getCompressedClay(this.raiseFrom + 1), this.containerItemStacks, 1, 2, this.getInventoryStackLimit());
        this.markDirty();
        this.craftTime = 0L;
        this.isDoingWork = false;
//            if (this.externalControlState > 0) {
//                --this.externalControlState;
//                if (this.externalControlState == 0) {
//                    this.externalControlState = -1;
//                }
//            }
    }

    @Override
    protected boolean setNewRecipe() {
        if (this.world.isRemote) return false;

        if (this.world.getLightFor(EnumSkyBlock.SKY, this.getPos().up()) < 15 || this.world.getSkylightSubtracted() > 0) return false;

        this.raiseFrom = getTierOfCompressedClay(this.getStackInSlot(MachineSlots.MATERIAL));
        if (this.raiseFrom == -1 || !this.canCraft(this.raiseFrom)) return false;

        this.timeToCraft = (long)(Math.pow(this.baseCraftTime, this.raiseFrom) * (double)this.multCraftTime);
        this.getStackInSlot(MachineSlots.MATERIAL).shrink(1);
        this.markDirty();

        return true;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return slot == 0 && getTierOfCompressedClay(itemstack) >= 0 && getTierOfCompressedClay(itemstack) <= this.acceptableTier;
    }

    private static int getTierOfCompressedClay(ItemStack itemstack) {
        int asCC = IClayEnergy.getTier(itemstack);
        if (asCC != -1) return asCC;

        if (UtilItemStack.areItemDamageEqual(new ItemStack(Blocks.SAND), itemstack)) {
            return 2;
        }

        return UtilItemStack.hasOreName(itemstack, ClayiumMaterials.getOreName(ClayiumMaterial.lithium, ClayiumShape.ingot)) ? 8 : -1;
    }
}
