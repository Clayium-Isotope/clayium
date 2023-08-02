package mods.clayium.machine.AutoCrafter;

import mods.clayium.gui.ContainerDummy;
import mods.clayium.item.filter.IFilter;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.RecipeProvider;
import mods.clayium.util.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Predicate;

/**
 * <br>0 - 8: Depot: stacks will be consumed
 * <br>9 - 14: Output: produced item is here
 * <br>15 - 23: Pattern: ghost items are recipe reference
 * <br>24: Energy
 */
public class TileEntityAutoCrafter extends TileEntityClayContainer implements IClayEnergyConsumer, RecipeProvider {
    public int craftTime;
    public int timeToCraft = 20;
    public long debtEnergy = 10L;
    public float multCraftTime = 1.0F;
    public float multConsumingEnergy = 1.0F;
    public int numAutomation = 1;

    /**
     * craftMatrix couldn't be an array of {@link java.util.function.Predicate}{@code <}{@link net.minecraft.item.ItemStack}{@code >};
     * <br>using {@link mods.clayium.item.filter.IFilter#getFilterPredicate(ItemStack) }
     * <br>
     * <br> Can't compare between {@code Predicate<ItemStack>} and Recipes;
     * <br> Because of fixed processes:
     * <br>{@link net.minecraft.item.crafting.Ingredient#apply(ItemStack)}
     * <br>--- on {@link net.minecraft.item.crafting.ShapedRecipes#matches(InventoryCrafting, World)}
     * <br>--- on {@link CraftingManager#findMatchingRecipe(InventoryCrafting, World)}
     * <br>--- on {@link net.minecraft.inventory.Container#slotChangedCraftingGrid(World, EntityPlayer, InventoryCrafting, InventoryCraftResult)}
     */
    private final InventoryCrafting craftMatrix = new InventoryCrafting(new ContainerDummy(), 3, 3);

    private final ContainClayEnergy containEnergy = new ContainClayEnergy();
    private int energyStorageSize = 1;
    private final List<Predicate<ItemStack>> patternCache = NonNullList.withSize(9, ItemStack::isEmpty);
    private IRecipe doingRecipe;
    private ItemStack doingRecipeResult;
    private TierPrefix tier;

    public TileEntityAutoCrafter() {}

    public void initParams() {
        super.initParams();
        this.containerItemStacks = NonNullList.withSize(25, ItemStack.EMPTY);
        this.listSlotsImport.add(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
        this.listSlotsExport.add(new int[]{9, 10, 11, 12, 13, 14});
        this.setImportRoutes(-1, 0, -1, -2, -1, -1);
        this.maxAutoExtract = new int[]{-1, 1};
        this.setExportRoutes(0, -1, -1, -1, -1, -1);
        this.maxAutoInsert = new int[]{-1};
        this.slotsDrop = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, this.getEnergySlot()};
        this.autoInsert = true;
        this.autoExtract = true;
    }

    @Override
    public int getEnergySlot() {
        return 24;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        this.tier = tier;
        switch (tier) {
            case advanced:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 4;
                this.autoExtractInterval = this.autoInsertInterval = 4;
                this.multConsumingEnergy = 0.0F;
                break;
            case precision:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 16;
                this.autoExtractInterval = this.autoInsertInterval = 2;
                this.multCraftTime = 0.05F;
                break;
            case claySteel:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.multCraftTime = 0.05F;
                this.multConsumingEnergy = 4.0F;
                this.numAutomation = 16;
                break;
            case clayium:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 16;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.multCraftTime = 0.05F;
                this.multConsumingEnergy = 16.0F;
                this.numAutomation = 64;
                break;
            case ultimate:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 576;
                this.autoExtractInterval = this.autoInsertInterval = 1;
                this.multCraftTime = 0.05F;
                this.multConsumingEnergy = 64.0F;
                this.numAutomation = 256;
                break;
            default:
                this.maxAutoInsertDefault = this.maxAutoExtractDefault = 1;
                this.autoExtractInterval = this.autoInsertInterval = 8;
        }

        if (TierPrefix.comparator.compare(tier, TierPrefix.advanced) <= 0 && this.getImportRoute(EnumSide.BACK) == -2) {
            this.setImportRoute(EnumSide.BACK, -1);
        }
    }

    @Override
    public ContainClayEnergy containEnergy() {
        return containEnergy;
    }

    @Override
    public int getClayEnergyStorageSize() {
        return this.energyStorageSize;
    }

    @Override
    public void setClayEnergyStorageSize(int size) {
        this.energyStorageSize = size;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        if (slot >= 0 && slot < 9) { // Into Depot Slots
            return this.patternCache.get(slot).test(itemstack);
        }

        if (slot >= 15 && slot < 24) { // Into Pattern Slots
            return true;
        }

        return super.isItemValidForSlot(slot, itemstack);
    }

    @Override
    public boolean acceptClayEnergy() {
        return (float)this.debtEnergy * this.multConsumingEnergy > 0.0F;
    }

    @Override
    public void update() {
        if (this.world.isRemote) {
            super.update();
            return;
        }

        this.spreadMaterials();
        for (int i = 0; i < this.numAutomation; ++i) {
            RecipeProvider.update(this);

            if (!this.isDoingWork()) {
                super.update();
                this.spreadMaterials();
            }
        }
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);
        this.craftTime = tagCompound.getInteger("CraftTime");
        this.timeToCraft = tagCompound.getInteger("TimeToCraft");
        this.debtEnergy = tagCompound.getLong("ConsumingEnergy");
        this.multCraftTime = tagCompound.getFloat("CraftTimeMultiplier");
        this.multConsumingEnergy = tagCompound.getFloat("ConsumingEnergyMultiplier");
        this.numAutomation = tagCompound.getInteger("NumAutomation");
        this.initParamsByTier(TierPrefix.get(tagCompound.getInteger("Tier")));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);
        tagCompound.setInteger("CraftTime", this.craftTime);
        tagCompound.setInteger("TimeToCraft", this.timeToCraft);
        tagCompound.setLong("ConsumingEnergy", this.debtEnergy);
        tagCompound.setFloat("CraftTimeMultiplier", this.multCraftTime);
        tagCompound.setFloat("ConsumingEnergyMultiplier", this.multConsumingEnergy);
        tagCompound.setInteger("NumAutomation", this.numAutomation);
        tagCompound.setInteger("Tier", this.tier.meta());

        return tagCompound;
    }

    @Override
    public void markDirty() {
        for (int i = 0; i < 9; i++) {
            this.patternCache.set(i, IFilter.getFilterPredicate(this.getStackInSlot(15 + i)));
        }

        super.markDirty();
    }

    @Override
    public boolean isDoingWork() {
        return this.doingRecipe != null && this.craftTime > 0;
    }

    @Override
    public boolean canProceedCraft() {
        if (!this.acceptClayEnergy()) return true;

        return IClayEnergyConsumer.compensateClayEnergy(this, debtEnergy, false);
    }

    @Override
    public void proceedCraft() {
        if (this.acceptClayEnergy() && IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) return;

        this.craftTime++;
        if (this.craftTime < this.timeToCraft) return;

        UtilTransfer.produceItemStack(this.doingRecipeResult, this.getContainerItemStacks(),
                9, 15, this.getInventoryStackLimit());

        this.craftTime = 0;
        this.doingRecipe = null;
    }

    @Override
    public boolean setNewRecipe() {
        if (this.getWorld().isRemote) {
            return false;
        }

        boolean successCopy = true;
        this.craftMatrix.clear();

        for (int i = 0; i < 9; i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (!this.isItemValidForSlot(i, stack)) {
                successCopy = false;
            }

            this.craftMatrix.setInventorySlotContents(i, stack);
        }

        if (!successCopy) return false;

        IRecipe irecipe = CraftingManager.findMatchingRecipe(this.craftMatrix, world);

        if (irecipe == null || !(irecipe.isDynamic() || !world.getGameRules().getBoolean("doLimitedCrafting"))) {
            return false;
        }

        this.checkRemaining();

        this.doingRecipe = irecipe;
        this.doingRecipeResult = irecipe.getCraftingResult(this.craftMatrix);
        this.craftTime = 1;
        return true;
    }

    @Override
    public TierPrefix getHullTier() {
        return this.tier;
    }

    /**
     * Copied from {@link net.minecraft.inventory.SlotCrafting#onTake(EntityPlayer, ItemStack)}
     */
    public void checkRemaining() {
        NonNullList<ItemStack> nonnulllist = CraftingManager.getRemainingItems(this.craftMatrix, this.world);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack remaining = nonnulllist.get(i);

            this.craftMatrix.decrStackSize(i, 1);

            if (remaining.isEmpty()) continue;

            remaining = UtilTransfer.produceItemStack(remaining, UtilCollect.sliceInventory(this, 0, 9), i, this.getInventoryStackLimit());
            if (remaining.isEmpty()) continue;

            this.getWorld().spawnEntity(new EntityItem(this.world, this.pos.getX(), this.pos.getY() + 0.5f, this.pos.getZ(), remaining));
        }
    }

    private void spreadMaterials() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (stack.isEmpty() || stack.getCount() <= 1) continue;

            for (int j = 0; j < 9 && stack.getCount() > 1; ++j) {
                if (!this.getStackInSlot(j).isEmpty() || !this.isItemValidForSlot(j, stack))
                    continue;

                if (j != i) this.setInventorySlotContents(j, stack.splitStack(1));
            }

            this.setInventorySlotContents(i, stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIOIcons() {
        this.registerInsertIcons("import");
        this.registerExtractIcons("export");
    }
}
