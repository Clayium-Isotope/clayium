package mods.clayium.machine.ClayReactor;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.MachineHull;
import mods.clayium.block.Overclocker;
import mods.clayium.block.common.ITieredBlock;
import mods.clayium.machine.ClayEnergyLaser.laser.ClayLaser;
import mods.clayium.machine.ClayEnergyLaser.laser.IClayLaserMachine;
import mods.clayium.machine.Interface.ClayInterface.TileEntityClayInterface;
import mods.clayium.machine.Interface.ClayLaserInterface.TileEntityClayLaserInterface;
import mods.clayium.machine.Interface.ISynchronizedInterface;
import mods.clayium.machine.Interface.RedstoneInterface.TileEntityRedstoneInterface;
import mods.clayium.machine.MultiblockMachine.BlockStateMultiblockMachine;
import mods.clayium.machine.MultiblockMachine.TileEntityMultiblockMachine;
import mods.clayium.machine.common.ClayiumRecipeProvider;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.SyncManager;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class TileEntityClayReactor extends TileEntityMultiblockMachine implements IClayLaserMachine {
    public TierPrefix recipeTier = TierPrefix.none;
    public ClayLaser irradiatedLaser = null;
    protected int resultSlotNum = 2;

    public TileEntityClayReactor() {}

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);
        this.listSlotsImport.add(new int[]{0});
        this.listSlotsImport.add(new int[]{1});
        this.listSlotsImport.add(new int[]{0, 1});
        this.listSlotsExport.add(new int[]{2});
        this.listSlotsExport.add(new int[]{3});
        this.listSlotsExport.add(new int[]{2, 3});
        this.setImportRoutes(-1, 2, -1, -2, -1, -1);
        this.maxAutoExtract = new int[]{64, 64, 64, 1};
        this.setExportRoutes(2, -1, -1, -1, -1, -1);
        this.maxAutoInsert = new int[]{64, 64, 64};
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.slotsDrop = new int[]{0, 1, 2, 3, this.getEnergySlot()};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    public boolean isConstructed() {
        double sum = 0;
        boolean flag = true;

        for (BlockPos relative : BlockPos.getAllInBox(-1, -1, 0, 1, 1, 2)) {
            if (relative.equals(BlockPos.ORIGIN)) continue;

            int atier = this.getMachineTier(relative);
            if (atier <= 6) {
                flag = false;
            }

            if (!relative.equals(new Vec3i(0, 1, 1)) && this.getTileEntity(relative) instanceof TileEntityClayLaserInterface) {
                flag = false;
            }

            sum += Math.pow(2.0, 16 - atier);
        }

        this.recipeTier = TierPrefix.get(Math.max((int) (16.0 - Math.floor(Math.log(sum / 26) / Math.log(2.0) + 0.5)), 0));
        return flag;
    }

    @Override
    protected void onConstruction() {
//        this.setRenderSyncFlag();
        BlockStateMultiblockMachine.setConstructed(this, true);

        // sync the interface around the blast furnace.
        for (BlockPos relative : BlockPos.getAllInBox(-1, -1, 0, 1, 1, 2)) {
            if (relative.equals(BlockPos.ORIGIN)) continue;

            TileEntity te = this.getTileEntity(relative);
            if (te instanceof TileEntityClayInterface || te instanceof TileEntityRedstoneInterface) {
                SyncManager.immediateSync(this, (ISynchronizedInterface) te);
            }

            if (te instanceof TileEntityClayLaserInterface && relative.equals(new Vec3i(0, 1, 1))) {
                SyncManager.immediateSync(this, (ISynchronizedInterface) te);
            }
        }

        this.markDirty();
    }

    @Override
    public boolean acceptClayEnergy() {
        return true;
    }

    protected int getMachineTier(BlockPos pos) {
        Block block = this.getBlock(pos);
        if (block instanceof MachineHull) {
            return ((ITieredBlock) block).getTier(this.world, this.getRelativeCoord(pos)).meta();
        }

        if (block instanceof Overclocker) {
            return ClayiumBlocks.overclocker.getTier(block).meta();
        }

        TileEntity te = this.getTileEntity(pos);
        if (te instanceof ISynchronizedInterface) {
            return ((ISynchronizedInterface) te).getHullTier().meta();
        }

        return -1;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
    }

    @Override
    public TierPrefix getRecipeTier() {
        return this.recipeTier;
    }

    @Override
    public TierPrefix getHullTier() {
        return TierPrefix.claySteel;
    }

    @Override
    public int getEnergySlot() {
        return 4;
    }

    @Override
    public boolean isDoingWork() {
        return BlockStateMultiblockMachine.isConstructed(this) && !this.doingRecipe.isFlat();
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStacks(recipe.getResults(), this.getContainerItemStacks(), 2, 4, this.getInventoryStackLimit());
    }

    @Override
    public boolean canProceedCraftWhenConstructed() {
        return IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy, false) && this.irradiatedLaser != null;
    }

    public void proceedCraft() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy) || this.irradiatedLaser == null) return;

        this.craftTime += (long) this.irradiatedLaser.getEnergy();
        this.irradiatedLaser = null;

        if (this.craftTime < this.timeToCraft) return;

        UtilTransfer.produceItemStacks(UtilTransfer.getHardCopy(this.doingRecipe.getResults()), this.getContainerItemStacks(), 2, 2 + resultSlotNum, this.getInventoryStackLimit());
        this.craftTime = 0;
        this.timeToCraft = 0;
        this.debtEnergy = 0;
        this.doingRecipe = RecipeElement.flat();
    }

    @Override
    public boolean setNewRecipe() {
        if (!BlockStateMultiblockMachine.isConstructed(this)) return false;

        this.doingRecipe = ClayiumRecipeProvider.getCraftPermRecipe(this, this.getStackInSlot(0), this.getStackInSlot(1));
        if (this.doingRecipe.isFlat()) return false;

        this.debtEnergy = (long) (this.doingRecipe.getEnergy() * this.multConsumingEnergy);
        if (!this.canProceedCraft()) {
            this.timeToCraft = 0L;
            this.debtEnergy = 0L;
            this.doingRecipe = this.getFlat();
            return false;
        }

        this.timeToCraft = (long) (this.doingRecipe.getTime() * this.multCraftTime);

        UtilTransfer.consumeByIngredient(this.doingRecipe.getIngredients(), this.getContainerItemStacks(), 0, 2);

        return true;
    }

    @Override
    public boolean irradiateClayLaser(ClayLaser laser, EnumFacing facing) {
        if (this.world.isRemote) return false;

        this.irradiatedLaser = laser;
//        this.setSyncFlag();
        return true;
    }
}