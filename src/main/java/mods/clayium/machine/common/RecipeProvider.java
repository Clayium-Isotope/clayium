package mods.clayium.machine.common;

import net.minecraft.inventory.IInventory;

public interface RecipeProvider {
    int getRecipeTier();

    boolean isDoingWork();

    static void update(RecipeProvider provider) {
        if (provider.isDoingWork()) {
            if (!provider.canProceedCraft()) return;

            provider.proceedCraft();
        } else {
            provider.setNewRecipe();
        }

        // probably edited its inventory
        if (provider instanceof IInventory) {
            ((IInventory) provider).markDirty();
        }
    }

    /**
     * 内部を変更しないこと。
     * <pre>{@code
     * return IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy, false);
     * }</pre>
     */
    boolean canProceedCraft();

    /**
     * <ul>
     *     <li>粘土エネルギー消費
     *          <pre>{@code
     *          if (!IClayEnergyConsumer.compensateClayEnergy(this, this.debtEnergy)) {
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
     * <ul>
     *     <li>レシピ検索
     *          <pre>{@code
     *          this.doingRecipe = getRecipe(getStackInSlot(MachineSlots.MATERIAL));
     *          if (this.doingRecipe.isFlat()) return false;
     *          }</pre>
     *     </li>
     *     <li>実行できるか検証
     *         <pre>{@code
     *         if (!this.canCraft(this.doingRecipe) || !this.canProceedCraft()) {
     *             this.timeToCraft = 0L;
     *             this.debtEnergy = 0L;
     *             this.doingRecipe = this.getFlat();
     *             return false;
     *         }
     *         }</pre>
     *     </li>
     * </ul>
     * @return レシピが設定されたならtrue、そうでなければfalse
     */
    boolean setNewRecipe();
}
