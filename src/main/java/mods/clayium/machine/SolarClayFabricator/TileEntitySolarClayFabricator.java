package mods.clayium.machine.SolarClayFabricator;

import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.item.common.IClayEnergy;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.util.IllegalTierException;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;

import javax.annotation.Nullable;

public class TileEntitySolarClayFabricator extends TileEntityClayiumMachine {
    public TierPrefix acceptableTier;
    public float baseCraftTime;

    private TierPrefix raiseFrom = TierPrefix.unknown;

    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(2, ItemStack.EMPTY);

        this.setImportRoutes(NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, 0, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(NONE_ROUTE, NONE_ROUTE, 0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.listSlotsImport.add(new int[] { 0 });
        this.listSlotsExport.add(new int[] { 1 });
        this.slotsDrop = new int[] { 0, 1 };

        this.maxAutoExtract = new int[] {64};
        this.maxAutoInsert = new int[] {64};

        this.debtEnergy = 0;
    }

    @Override
    public int getEnergySlot() {
        return -1;
    }

    @Override
    public boolean acceptClayEnergy() {
        return false;
    }

    @Override
    public boolean isDoingWork() {
        return this.craftTime > 0;
    }

    public void initParamsByTier(TierPrefix tier) {
        this.tier = tier;
        this.setDefaultTransportation(tier);
        float craftTimeDivisor;

        switch (tier) {
            case advanced:
                this.acceptableTier = TierPrefix.basic;
                this.baseCraftTime = 4.0F;
                craftTimeDivisor = 5000.0f;
                break;
            case precision:
                this.acceptableTier = TierPrefix.precision;
                this.baseCraftTime = 3.0F;
                craftTimeDivisor = 50000.0f;
                break;
            case claySteel:
                this.acceptableTier = TierPrefix.ultimate;
                this.baseCraftTime = 2.0F;
                craftTimeDivisor = 3000000.0F;
                break;
            default:
                throw new IllegalTierException();
        }

        this.initCraftTime = (float)(Math.pow(10.0D, this.acceptableTier.meta() + 1) * (double)(this.baseCraftTime - 1.0F) / ((double)this.baseCraftTime * (Math.pow(this.baseCraftTime, this.acceptableTier.meta()) - 1.0D)) / (double)(ClayiumConfiguration.multiplyProgressionRate(craftTimeDivisor) / 20.0F));
        this.autoExtractInterval = this.autoInsertInterval = 4;
    }

    protected boolean canCraft(TierPrefix tier) {
        return isTierValid(tier) && UtilTransfer.canProduceItemStack(IClayEnergy.getCompressedClay(tier.offset(1)), this.containerItemStacks, 1, 2, this.getInventoryStackLimit()) >= 1;
    }

    @Override
    public boolean canCraft(ItemStack material) {
        return this.canCraft(getTierOfCompressedClay(material));
    }

    public boolean canProceedCraft() {
        if (this.world.getLightFor(EnumSkyBlock.SKY, this.getPos().up()) < 15 || this.world.getSkylightSubtracted() > 0) return false;

        return this.canCraft(this.getStackInSlot(0));
    }

    public void proceedCraft() {
        ++this.craftTime;
        this.containEnergy().set((long)(Math.pow(10.0D, this.raiseFrom.meta() + 1) * (double)this.craftTime / (double)this.timeToCraft));
        if (this.craftTime < this.timeToCraft) {
            return;
        }

        this.containEnergy().clear();
        UtilTransfer.produceItemStack(IClayEnergy.getCompressedClay(this.raiseFrom.offset(1)), this.containerItemStacks, 1, this.getInventoryStackLimit());
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
//        if (this.world.isRemote) return false;

        this.raiseFrom = getTierOfCompressedClay(this.getStackInSlot(0));
        if (!this.canCraft(this.raiseFrom)) return false;

        this.craftTime = 1;
        this.timeToCraft = (long) (Math.pow(this.baseCraftTime, this.raiseFrom.meta()) * (double) this.multCraftTime);
        this.getStackInSlot(0).shrink(1);

        return true;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return slot == 0 && isTierValid(getTierOfCompressedClay(itemstack));
    }

    private static TierPrefix getTierOfCompressedClay(ItemStack itemstack) {
        TierPrefix asCC = IClayEnergy.getTier(itemstack);
        if (asCC.isValid()) return asCC;

        if (UtilItemStack.areItemDamageEqual(new ItemStack(Blocks.SAND), itemstack)) {
            return TierPrefix.denseClay;
        }

        return UtilItemStack.hasOreName(itemstack, ClayiumMaterials.getOreName(ClayiumMaterial.lithium, ClayiumShape.ingot)) ? TierPrefix.clayium : TierPrefix.unknown;
    }

    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        return null;
    }

    protected boolean isTierValid(TierPrefix tier) {
        return tier.isValid() && TierPrefix.comparator.compare(tier, this.acceptableTier) <= 0;
    }
}
