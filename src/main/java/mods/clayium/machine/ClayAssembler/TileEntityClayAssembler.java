package mods.clayium.machine.ClayAssembler;

import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public void initParamsByTier(int tier) {
        this.tier = tier;
    }

    public ItemStack getStackInSlot(AssemblerSlots index) {
        return this.containerItemStacks.get(index.ordinal());
    }

    protected boolean canCraft(List<ItemStack> materials) {
        if (this.isDoingWork) return true;

        if (materials.isEmpty()) return false;

        RecipeElement recipe = (RecipeElement) this.recipeCards.getRecipe(e -> e.match(materials, -1, this.tier), RecipeElement.flat());
        if (recipe == RecipeElement.flat()) {
            return false;
        }

        if (this.getStackInSlot(AssemblerSlots.PRODUCT).isEmpty()) {
            return true;
        } else if (UtilItemStack.areTypeEqual(this.getStackInSlot(AssemblerSlots.PRODUCT), recipe.getResults().get(0))){
            return true;
        } else {
            int result = this.getStackInSlot(AssemblerSlots.PRODUCT).getCount() + recipe.getResults().get(0).getCount();
            return result <= this.getInventoryStackLimit() && result <= this.getStackInSlot(AssemblerSlots.PRODUCT).getMaxStackSize();
        }
    }

    protected int[] getCraftPermutation() {
        if (this.canCraft(Arrays.asList(this.getStackInSlot(AssemblerSlots.MATERIAL_1), this.getStackInSlot(AssemblerSlots.MATERIAL_2))))
            return new int[] { 0, 1 };

        if (this.canCraft(Arrays.asList(this.getStackInSlot(AssemblerSlots.MATERIAL_2), this.getStackInSlot(AssemblerSlots.MATERIAL_1))))
            return new int[] { 1, 0 };

        if (this.canCraft(Arrays.asList(this.getStackInSlot(AssemblerSlots.MATERIAL_1))))
            return new int[] { 0 };

        if (this.canCraft(Arrays.asList(this.getStackInSlot(AssemblerSlots.MATERIAL_2))))
            return new int[] { 1 };

        return null;
    }

    public boolean canProceedCraft() {
        return this.getCraftPermutation() != null;
    }

    public void proceedCraft() {
        ++this.craftTime;
//            this.isDoingWork = true;
        if (this.craftTime < this.timeToCraft) return;

        ItemStack result = this.doingRecipe.getResults().get(0);
        this.craftTime = 0L;
        if (this.getStackInSlot(AssemblerSlots.PRODUCT).isEmpty()) {
            this.setInventorySlotContents(AssemblerSlots.PRODUCT.ordinal(), result.copy());
        } else if (UtilItemStack.areTypeEqual(this.getStackInSlot(AssemblerSlots.PRODUCT), result)) {
            this.getStackInSlot(AssemblerSlots.PRODUCT).grow(result.getCount());
        }

        this.isDoingWork = setNewRecipe();
    }

    @Override
    protected boolean setNewRecipe() {
        this.craftTime = 0L;

        this.debtEnergy = 0L;
        this.timeToCraft = 0L;

        int[] perm = getCraftPermutation();
        if (perm == null) return false;

        List<ItemStack> mats = new ArrayList<>(2);
        for (int i : perm) {
            mats.add(this.getStackInSlot(i));
        }

        this.doingRecipe = this.recipeCards.getRecipe(e -> e.match(mats, -1, tier), RecipeElement.flat());

        if (this.doingRecipe == RecipeElement.flat()) return false;

        if (!IClayEnergyConsumer.compensateClayEnergy(this, this.doingRecipe.getEnergy())) {
            this.doingRecipe = RecipeElement.flat();
            return false;
        }

        this.debtEnergy = this.doingRecipe.getEnergy();
        this.timeToCraft = this.doingRecipe.getTime();

        int[] stackSizes = this.doingRecipe.getStackSizes(getStackInSlot(perm[0]), getStackInSlot(perm[1]));
        for (int i = 0; i < perm.length; i++) {
            this.getStackInSlot(perm[i]).shrink(stackSizes[i]);
        }

        proceedCraft();

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
