package mods.clayium.machine.ClayBlastFurnace;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.MachineHull;
import mods.clayium.block.Overclocker;
import mods.clayium.block.common.ITieredBlock;
import mods.clayium.machine.Interface.ClayInterface.TileEntityClayInterface;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayBlastFurnace extends TileEntityMultiblockMachine implements IClayEnergyConsumer {

    private static final int resultSlotNum = 2;
    private TierPrefix recipeTier = TierPrefix.none;

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);

        this.setImportRoutes(NONE_ROUTE, 2, NONE_ROUTE, ENERGY_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(2, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.listSlotsImport.clear();
        this.listSlotsExport.clear();
        this.listSlotsImport.add(new int[] { 0 });
        this.listSlotsImport.add(new int[] { 1 });
        this.listSlotsImport.add(new int[] { 0, 1 });
        this.listSlotsExport.add(new int[] { 2 });
        this.listSlotsExport.add(new int[] { 3 });
        this.listSlotsExport.add(new int[] { 2, 3 });

        this.maxAutoExtract = new int[] { 64, 64, 64, 1 };
        this.maxAutoInsert = new int[] { 64, 64, 64 };
        this.slotsDrop = new int[] { 0, 1, 2, 3, this.getEnergySlot() };
        this.autoInsert = true;
        this.autoExtract = true;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
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

        this.recipeTier = TierPrefix
                .get(Math.max((int) (16.0D - Math.floor(Math.log(sum / 17) / Math.log(2.0D) + 0.5D)), 0));
        return flag;
    }

    @Override
    public boolean acceptClayEnergy() {
        return true;
    }

    @Override
    protected void onConstruction() {
        // this.setRenderSyncFlag();
        BlockStateMultiblockMachine.setConstructed(this, true);

        // sync the interface around the blast furnace.
        for (BlockPos relative : BlockPos.getAllInBox(-1, 0, 0, 1, 1, 2)) {
            if (relative.getX() == 0 && relative.getY() == 0 && relative.getZ() == 0) continue;

            TileEntity te = this.getTileEntity(relative);
            if (te instanceof TileEntityClayInterface || te instanceof TileEntityRedstoneInterface) {
                SyncManager.immediateSync(this, (ISynchronizedInterface) te);
            }
        }

        this.markDirty();
    }

    protected int getMachineTier(BlockPos vector) {
        Block block = this.getBlock(vector);
        if (block == null) return -1;

        if (block instanceof MachineHull) {
            // return this.getBlockMetadata(vector);
            return ((ITieredBlock) block).getTier(this.world, this.getRelativeCoord(vector)).meta();
        }

        if (block instanceof Overclocker) {
            return ClayiumBlocks.overclocker.getTier(block).meta();
        }

        TileEntity te = this.getTileEntity(vector);
        if (te == null) return -1;

        if (te instanceof TileEntityClayInterface || te instanceof TileEntityRedstoneInterface) {
            return ((ISynchronizedInterface) te).getHullTier().meta();
        }

        return -1;
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStacks(recipe.getResults(), this.getContainerItemStacks(), 2,
                2 + resultSlotNum, this.getInventoryStackLimit());
    }

    @Override
    public boolean setNewRecipe() {
        if (!BlockStateMultiblockMachine.isConstructed(this)) return false;

        this.doingRecipe = ClayiumRecipeProvider.getCraftPermRecipe(this, this.getStackInSlot(0), this.getStackInSlot(1));
        if (this.doingRecipe.isFlat()) return false;

        this.debtEnergy = (long) (this.doingRecipe.getEnergy() * this.multConsumingEnergy);
        if (!this.canProceedCraft()) {
            this.timeToCraft.set(0);
            this.craftTime.set(0);
            this.debtEnergy = 0L;
            this.doingRecipe = this.getRecipeList().getFlat();
            return false;
        }

        this.timeToCraft.set((long) (this.doingRecipe.getTime() * this.multCraftTime));

        UtilTransfer.consumeByIngredient(this.doingRecipe.getIngredients(), this.getContainerItemStacks(), 0, 2);

        return true;
    }

    @Override
    public boolean canProceedCraft() {
        return BlockStateMultiblockMachine.isConstructed(this) &&
                IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy);
    }

    @Override
    public void proceedCraft() {
        if (!IClayEnergyConsumer.consumeClayEnergy(this, this.debtEnergy)) return;

        this.craftTime.add(1);
        if (this.craftTime.get() < this.timeToCraft.get()) return;

        UtilTransfer.produceItemStacks(this.doingRecipe.getResults(), this.getContainerItemStacks(), 2,
                2 + resultSlotNum, this.getInventoryStackLimit());
        this.craftTime.set(0);
        this.timeToCraft.set(0);
        this.debtEnergy = 0;
        this.doingRecipe = RecipeElement.flat();

        // if (this.externalControlState > 0) {
        // --this.externalControlState;
        // if (this.externalControlState == 0) {
        // this.externalControlState = -1;
        // }
        // }
    }

    @Override
    public TierPrefix getRecipeTier() {
        return this.recipeTier;
    }

    @Override
    public TierPrefix getHullTier() {
        return TierPrefix.precision;
    }

    @Override
    public int getResultSlotCount() {
        return 2;
    }

    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        this.registerInsertIcons("import_1", "import_2", "import");
        this.registerExtractIcons("export_1", "export_2", "export");
    }
}
