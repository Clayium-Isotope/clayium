package mods.clayium.machine.ClayAssembler;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.ClayiumRecipeProvider;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine2To1;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayAssembler extends TileEntityClayiumMachine implements Machine2To1 {

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);

        this.setImportRoutes(NONE_ROUTE, 0, NONE_ROUTE, ENERGY_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.setExportRoutes(0, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE, NONE_ROUTE);
        this.listSlotsImport.add(new int[] { Machine2To1.MATERIAL_1, Machine2To1.MATERIAL_2 });
        this.listSlotsImport.add(new int[] { Machine2To1.MATERIAL_1 });
        this.listSlotsImport.add(new int[] { Machine2To1.MATERIAL_2 });
        this.listSlotsExport.add(new int[] { Machine2To1.PRODUCT });
        this.maxAutoExtract = new int[] { -1, -1, -1, 1 };
        this.maxAutoInsert = new int[] { -1 };

        this.slotsDrop = new int[] { Machine2To1.MATERIAL_1, Machine2To1.MATERIAL_2, Machine2To1.PRODUCT,
                this.getEnergySlot() };
        this.autoInsert = true;
        this.autoExtract = true;
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStack(recipe.getResults().get(0), this.getContainerItemStacks(),
                Machine2To1.PRODUCT, this.getInventoryStackLimit()) > 0;
    }

    @Override
    public boolean canProceedCraft() {
        return IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy);

        // return IRecipeProvider.getCraftPermutation(this, this.getStackInSlot(AssemblerSlots.MATERIAL_1),
        // this.getStackInSlot(AssemblerSlots.MATERIAL_2)) != null;
    }

    @Override
    public void proceedCraft() {
        if (!IClayEnergyConsumer.consumeClayEnergy(this, this.debtEnergy)) return;

        this.craftTime.add(1);
        if (this.craftTime.get() < this.timeToCraft.get()) return;

        UtilTransfer.produceItemStack(this.doingRecipe.getResults().get(0),
                this.getContainerItemStacks(), Machine2To1.PRODUCT, this.getInventoryStackLimit());

        this.craftTime.set(0);
        this.debtEnergy = 0L;
        this.timeToCraft.set(0);
        this.doingRecipe = this.getRecipeList().getFlat();
    }

    @Override
    public boolean setNewRecipe() {
        this.doingRecipe = ClayiumRecipeProvider.getCraftPermRecipe(this, this.getStackInSlot(Machine2To1.MATERIAL_1),
                this.getStackInSlot(Machine2To1.MATERIAL_2));

        if (this.doingRecipe.isFlat()) return false;

        this.debtEnergy = this.doingRecipe.getEnergy();
        if (!this.canCraft(this.doingRecipe) || !this.canProceedCraft()) return false;

        this.timeToCraft.set(this.doingRecipe.getTime());

        UtilTransfer.consumeByIngredient(this.doingRecipe.getIngredients(), this.getContainerItemStacks(),
                Machine2To1.MATERIAL_1, Machine2To1.MATERIAL_2 + 1);

        return true;
    }

    @Override
    public int getEnergySlot() {
        return 3;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIOIcons() {
        this.registerInsertIcons("import", "import_1", "import_2");
        this.registerExtractIcons("export");
    }
}
