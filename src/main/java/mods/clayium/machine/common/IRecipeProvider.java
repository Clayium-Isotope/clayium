package mods.clayium.machine.common;

import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.IRecipeElement;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IRecipeProvider<T extends IRecipeElement> {
    int getTier();

    boolean isDoingWork();
    void setDoingWork(boolean isDoingWork);

    ClayiumRecipe getRecipeCard();
    T getFlat();
    default T getRecipe(ItemStack stack) {
        return this.getRecipeCard().getRecipe(e -> e.isCraftable(stack, this.getTier()), this.getFlat());
    }
    default T getRecipe(List<ItemStack> stacks) {
        return this.getRecipeCard().getRecipe(e -> e.match(stacks, -1, this.getTier()), this.getFlat());
    }
    default T getRecipe(int hash) {
        return this.getRecipeCard().getRecipe(hash, this.getFlat());
    }

    default boolean canCraft(ItemStack stack) {
        if (stack.isEmpty()) return false;

        return this.canCraft(this.getRecipe(stack));
    }
    default boolean canCraft(List<ItemStack> stacks) {
        if (stacks.isEmpty()) return false;

        return this.canCraft(this.getRecipe(stacks));
    }
    boolean canCraft(T recipe);

    @Nullable
    static <T extends IRecipeElement> int[] getCraftPermutation(IRecipeProvider<T> provider, ItemStack mat1, ItemStack mat2) {
        if (provider.canCraft(Arrays.asList(mat1, mat2)))
            return new int[]{ 0, 1 };

        if (provider.canCraft(Arrays.asList(mat2, mat1)))
            return new int[]{ 1, 0 };

        if (provider.canCraft(Collections.singletonList(mat1)))
            return new int[]{ 0 };

        if (provider.canCraft(Collections.singletonList(mat2)))
            return new int[]{ 1 };

        return null;
    }

    static <T extends IRecipeElement> List<ItemStack> getCraftPermStacks(IRecipeProvider<T> provider, ItemStack mat1, ItemStack mat2) {
        List<ItemStack> materials;

        materials = Arrays.asList(mat1, mat2);
        if (provider.canCraft(materials))
            return materials;

        materials = Arrays.asList(mat2, mat1);
        if (provider.canCraft(materials))
            return materials;

        if (provider.canCraft(Collections.singletonList(mat1)))
            return Collections.singletonList(mat1);

        if (provider.canCraft(Collections.singletonList(mat2)))
            return Collections.singletonList(mat2);

        return Collections.emptyList();
    }

    static <T extends IRecipeElement> void update(IRecipeProvider<T> provider) {
        if (provider.isDoingWork()) {
            provider.proceedCraft();
        } else {
            provider.setDoingWork(provider.setNewRecipe());
        }

        // probably edited its inventory
        if (provider instanceof IInventory) {
            ((IInventory) provider).markDirty();
        }
    }

    boolean canProceedCraft();

    /**
     * <ul>
     *     <li>粘土エネルギー消費
     *          <pre>{@code
     *          if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy) {
     *              return;
     *          }
     *          }</pre>
     *     </li>
     *     <li>作業進行
     *          <pre>{@code
     *          ++this.craftTime;
     *          if (this.craftTime < this.timeToCraft) {
     *              return;
     *          }
     *          }</pre>
     *     </li>
     *     <li>レシピ終了時の処理
     *          <pre>{@code
     *          this.setDoingWork(false);
     *          }</pre>
     *     </li>
     * </ul>
     */
    void proceedCraft();

    /**
     * @return レシピが設定されたならtrue、そうでなければfalse
     */
    boolean setNewRecipe();
}
