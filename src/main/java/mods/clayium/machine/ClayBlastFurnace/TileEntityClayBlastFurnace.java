package mods.clayium.machine.ClayBlastFurnace;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.MachineHull;
import mods.clayium.block.Overclocker;
import mods.clayium.block.common.ITieredBlock;
import mods.clayium.machine.Interface.ISynchronizedInterface;
import mods.clayium.machine.MultiblockMachine.BlockStateMultiblockMachine;
import mods.clayium.machine.MultiblockMachine.TileEntityMultiblockMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.IRecipeProvider;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.SyncManager;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityClayBlastFurnace extends TileEntityMultiblockMachine implements IClayEnergyConsumer {
    private static final int resultSlotNum = 2;
    private int recipeTier = -1;

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);

        this.setImportRoutes(-1, 2, -1, -2, -1, -1);
        this.setExportRoutes(2, -1, -1, -1, -1, -1);
        this.listSlotsImport.clear();
        this.listSlotsExport.clear();
        this.listSlotsImport.add(new int[]{0});
        this.listSlotsImport.add(new int[]{1});
        this.listSlotsImport.add(new int[]{0, 1});
        this.listSlotsExport.add(new int[]{2});
        this.listSlotsExport.add(new int[]{3});
        this.listSlotsExport.add(new int[]{2, 3});

        this.maxAutoExtract = new int[]{64, 64, 64, 1};
        this.maxAutoInsert = new int[]{64, 64, 64};
        this.slotsDrop = new int[]{0, 1, 2, 3, this.getEnergySlot()};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    @Override
    public void initParamsByTier(int tier) {
        super.initParamsByTier(tier);

        this.autoExtractInterval = this.autoInsertInterval = 1;
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
    public void onLoad() {
        if (this.isConstructed()) {
            this.onConstruction();
        }
    }

    @Override
    public boolean isConstructed() {
        double sum = 0;
        boolean flag = true;

        for (BlockPos relative : BlockPos.getAllInBox(-1, 0, 0, 1, 1, 2)) {
            if (relative.getX() == 0 && relative.getY() == 0 && relative.getZ() == 0) continue;

            int tier = this.getMachineTier(relative);
            if (tier <= 4) flag = false;

            sum += Math.pow(2, 16 - tier);
        }

        this.recipeTier = Math.max((int)(16.0D - Math.floor(Math.log(sum / 18) / Math.log(2.0D) + 0.5D)), 0);
        return flag;
    }

    @Override
    public boolean acceptClayEnergy() {
        return true;
    }

    @Override
    protected void onConstruction() {
//        this.setRenderSyncFlag();
        BlockStateMultiblockMachine.setConstructed(this, true);

        // sync the interface around the blast furnace.
        for (BlockPos relative : BlockPos.getAllInBox(-1, 0, 0, 1, 1, 2)) {
            if (relative.getX() == 0 && relative.getY() == 0 && relative.getZ() == 0) continue;

            TileEntity te = this.getTileEntity(relative);
            if (te instanceof ISynchronizedInterface) {
                SyncManager.immediateSync(this, (ISynchronizedInterface) te);
            }
        }

        this.markDirty();
    }

    @Override
    protected void onDestruction() {
//        this.setRenderSyncFlag();
        this.craftTime = 0L;
        BlockStateMultiblockMachine.setConstructed(this, false);

        // de-sync the interface around the blast furnace.
        for (BlockPos relative : BlockPos.getAllInBox(-1, 0, 0, 1, 1, 2)) {
            if (relative.getX() == 0 && relative.getY() == 0 && relative.getZ() == 0) continue;

            TileEntity te = this.getTileEntity(relative);
            if (te instanceof ISynchronizedInterface) {
                SyncManager.immediateSync(null, (ISynchronizedInterface) te);
            }
        }

        this.markDirty();
    }

    protected int getMachineTier(BlockPos vector) {
        Block block = this.getBlock(vector);
        if (block == null) return -1;

        if (block instanceof MachineHull) {
//            return this.getBlockMetadata(vector);
            return ((ITieredBlock) block).getTier(this.world, this.getRelativeCoord(vector));
        }

        if (block instanceof Overclocker) {
            return ClayiumBlocks.overclocker.getTier(this.getBlock(vector)).ordinal();
        }

        TileEntity te = this.getTileEntity(vector);
        if (te == null) return -1;

        if (te instanceof ISynchronizedInterface) {
            return ((ISynchronizedInterface) te).getHullTier();
        }

        return -1;
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStacks(recipe.getResults(), this.getContainerItemStacks(), 2, 2 + resultSlotNum, this.getInventoryStackLimit());
    }

    @Override
    public boolean setNewRecipe() {
        if (!BlockStateMultiblockMachine.isConstructed(this)) return false;

        this.doingRecipe = IRecipeProvider.getCraftPermRecipe(this, this.getStackInSlot(0), this.getStackInSlot(1));
        if (this.doingRecipe.isFlat()) return false;

        this.debtEnergy = (long) (this.doingRecipe.getEnergy() * this.multConsumingEnergy);
        if (!this.canProceedCraft()) {
            this.timeToCraft = 0L;
            this.debtEnergy = 0L;
            this.doingRecipe = this.getFlat();
            return false;
        }

        this.timeToCraft = (long) (this.doingRecipe.getTime() * this.multCraftTime);

        UtilTransfer.consumeItemStack(this.doingRecipe.getMaterials(), this.getContainerItemStacks(), 0, 2);

        return true;
    }

    @Override
    public boolean canProceedCraft() {
        return BlockStateMultiblockMachine.isConstructed(this) && IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy, false);
    }

    @Override
    public void proceedCraft() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) return;

        this.craftTime++;
        if (this.craftTime < this.timeToCraft) return;

        UtilTransfer.produceItemStacks(UtilTransfer.getHardCopy(this.doingRecipe.getResults()), this.getContainerItemStacks(), 2, 2 + resultSlotNum, this.getInventoryStackLimit());
        this.craftTime = 0;
        this.timeToCraft = 0;
        this.debtEnergy = 0;
        this.doingRecipe = RecipeElement.flat();

//        if (this.externalControlState > 0) {
//            --this.externalControlState;
//            if (this.externalControlState == 0) {
//                this.externalControlState = -1;
//            }
//        }
    }

    @Override
    public int getRecipeTier() {
        return this.recipeTier;
    }

    @Override
    public int getHullTier() {
        return 6;
    }

    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        this.registerInsertIcons("import_1", "import_2", "import");
        this.registerExtractIcons("export_1", "export_2", "export");
    }
}