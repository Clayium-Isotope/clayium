package mods.clayium.machine.ClayAssembler;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.IRecipeProvider;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilTransfer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityClayAssembler extends TileEntityClayiumMachine {
    enum AssemblerSlots {
        MATERIAL_1,
        MATERIAL_2,
        PRODUCT,
        ENERGY
    }

    @Override
    public void initParams() {
        this.containerItemStacks = NonNullList.withSize(AssemblerSlots.values().length, ItemStack.EMPTY);

        this.setImportRoutes(-1, 0, -1, -2, -1, -1);
        this.setExportRoutes(0, -1, -1, -1, -1, -1);
        this.listSlotsImport.add(new int[]{ AssemblerSlots.MATERIAL_1.ordinal(), AssemblerSlots.MATERIAL_2.ordinal() });
        this.listSlotsImport.add(new int[]{ AssemblerSlots.MATERIAL_1.ordinal() });
        this.listSlotsImport.add(new int[]{ AssemblerSlots.MATERIAL_2.ordinal() });
        this.listSlotsExport.add(new int[]{ AssemblerSlots.PRODUCT.ordinal() });
        this.maxAutoExtract = new int[] {-1, -1, -1, 1};
        this.maxAutoInsert = new int[] {-1};

        this.slotsDrop = new int[]{ AssemblerSlots.MATERIAL_1.ordinal(), AssemblerSlots.MATERIAL_2.ordinal(), AssemblerSlots.PRODUCT.ordinal(), AssemblerSlots.ENERGY.ordinal() };
        this.autoInsert = true;
        this.autoExtract = true;
    }

    public ItemStack getStackInSlot(AssemblerSlots index) {
        return this.containerItemStacks.get(index.ordinal());
    }

    @Override
    public boolean canCraft(RecipeElement recipe) {
        if (recipe.isFlat()) return false;

        return UtilTransfer.canProduceItemStack(recipe.getResults().get(0), this.getContainerItemStacks(), AssemblerSlots.PRODUCT.ordinal(), this.getInventoryStackLimit()) > 0;
    }

    @Override
    public boolean canProceedCraft() {
        return IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy, false);

//        return IRecipeProvider.getCraftPermutation(this, this.getStackInSlot(AssemblerSlots.MATERIAL_1), this.getStackInSlot(AssemblerSlots.MATERIAL_2)) != null;
    }

    @Override
    public void proceedCraft() {
        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) return;

        ++this.craftTime;
        if (this.craftTime < this.timeToCraft) return;

        UtilTransfer.produceItemStack(this.doingRecipe.getResults().get(0),
                this.getContainerItemStacks(), AssemblerSlots.PRODUCT.ordinal(), this.getInventoryStackLimit());

        this.craftTime = 0L;
        this.debtEnergy = 0L;
        this.timeToCraft = 0L;
        this.doingRecipe = this.getFlat();
    }

    @Override
    public boolean setNewRecipe() {
        List<ItemStack> permStack = IRecipeProvider.getCraftPermStacks(this, this.getStackInSlot(AssemblerSlots.MATERIAL_1), this.getStackInSlot(AssemblerSlots.MATERIAL_2));
        if (permStack.isEmpty()) return false;

        this.doingRecipe = this.getRecipe(permStack);

        if (this.doingRecipe.isFlat()) return false;

        this.debtEnergy = this.doingRecipe.getEnergy();
        if (!this.canCraft(this.doingRecipe) || !this.canProceedCraft()) return false;

        this.timeToCraft = this.doingRecipe.getTime();

        UtilTransfer.consumeItemStack(this.doingRecipe.getMaterials(), this.getContainerItemStacks(), AssemblerSlots.MATERIAL_1.ordinal(), AssemblerSlots.MATERIAL_2.ordinal() + 1);

        return true;
    }

    @Override
    public int getEnergySlot() {
        return AssemblerSlots.ENERGY.ordinal();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIOIcons() {
        this.registerInsertIcons("import", "import_1", "import_2");
        this.registerExtractIcons("export");
    }
}
