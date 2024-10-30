package mods.clayium.machine.CACollector;

import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.CAMachine.TileEntityCAMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.INormalInventory;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.machine.crafting.RecipeListGeneral;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TileEntityCACollector extends TileEntityCAMachine implements INormalInventory {
    public long baseCraftTime = 10000L;

    @Override
    public void initParams() {
        super.initParams();
        this.setImportRoutes(-1, -1, -1, -1, -1, -1);
        this.setExportRoutes(-1, -1, 0, -1, -1, -1);
        this.autoInsert = true;
        this.autoExtract = true;
        this.containerItemStacks = NonNullList.withSize(36, ItemStack.EMPTY);
        this.listSlotsImport.clear();
        this.maxAutoInsertDefault = this.maxAutoExtractDefault = 64;
        this.autoExtractInterval = this.autoInsertInterval = 1;
        this.timeToCraft = this.baseCraftTime;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        // int inventoryX = 3, inventoryY = 3, slotNum = 3 * 3;
        this.listSlotsExport.clear();
        this.listSlotsExport.add(IntStream.range(0, 9).map(i -> 9 - 1 - i).toArray());
        this.slotsDrop = IntStream.range(0, 9).toArray();
    }

    @Override
    public boolean canProceedCraft() {
        return true;
    }

    @Override
    public void proceedCraft() {
        this.timeToCraft = (long)((float)this.baseCraftTime * this.multCraftTime);
        this.craftTime = (long)((double)this.craftTime + ClayiumConfiguration.multiplyProgressionRate(this.resonanceHandler.getResonance() - 1.0));
        this.craftTime = Math.min(this.craftTime, this.timeToCraft * 9 * (long)this.getInventoryStackLimit());

        int n;
        for (boolean res = true; this.craftTime >= this.timeToCraft && res; res = UtilTransfer.produceItemStack(ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, n), this.containerItemStacks, 0, 9, this.getInventoryStackLimit()).isEmpty()) {
            n = Math.min((int)(this.craftTime / this.timeToCraft), 64);
            this.craftTime -= (long)n * this.timeToCraft;
        }

//        if (this.externalControlState > 0) {
//            --this.externalControlState;
//            if (this.externalControlState == 0) {
//                this.externalControlState = -1;
//            }
//        }
        this.markDirty();
    }

    @Override
    public boolean setNewRecipe() {
        return false;
    }

    public int getInventoryX() {
        return 3;
    }

    public int getInventoryY() {
        return 3;
    }

    public int getInventoryStart() {
        return 0;
    }

    public int getInventoryP() {
        return 1;
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
        return true;
    }

    @Nonnull
    @Override
    public RecipeListGeneral getRecipeList() {
        return ClayiumRecipes.EMPTY;
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        return true;
    }

    @Nullable
    @Override
    public ResourceLocation getFaceResource() {
        return EnumMachineKind.CACollector.getFaceResource();
    }

    @Override
    public TierPrefix getHullTier() {
        return TierPrefix.antimatter;
    }

    @Override
    public void registerIOIcons() {
        this.registerInsertIcons("import");
        this.registerExtractIcons("export");
    }
}
