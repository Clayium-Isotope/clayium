package mods.clayium.machine.ClayWorkTable;

import mods.clayium.machine.common.IHasButton;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class InventoryKneading extends InventoryCrafting implements IHasButton {
    private KneadingRecipe currentRecipe;
    private int craftTime;

    public InventoryKneading(Container eventHandlerIn) {
        super(eventHandlerIn, TileEntityClayWorkTable.ClayWorkTableSlots.values().length, 1);
    }

    public boolean onWorking() {
        return this.currentRecipe != KneadingRecipe.FLAT && this.craftTime >= 0;
    }

    public ItemStack getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots index) {
        return this.getStackInSlot(index.ordinal());
    }

    public void growSlotContent(TileEntityClayWorkTable.ClayWorkTableSlots index, ItemStack stack) {
        if (UtilItemStack.areTypeEqual(this.getStackInSlot(index.ordinal()), stack)) {
            this.getStackInSlot(index.ordinal()).grow(stack.getCount());
        }
    }

    public void shrinkSlotContent(TileEntityClayWorkTable.ClayWorkTableSlots index, ItemStack stack) {
        if (UtilItemStack.areTypeEqual(this.getStackInSlot(index.ordinal()), stack)) {
            this.getStackInSlot(index.ordinal()).shrink(stack.getCount());
        }
    }

    public long getCraftTime() {
        return 0;
    }

    @Override
    public ButtonProperty canPushButton(int button) {
        TileEntityClayWorkTable.KneadingMethod method = TileEntityClayWorkTable.KneadingMethod.fromId(button);
        if (method == TileEntityClayWorkTable.KneadingMethod.UNKNOWN) return ButtonProperty.FAILURE;

        if (onWorking()) {
            return this.currentRecipe.method == method ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
        }

        if (!this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT).isEmpty()) return ButtonProperty.FAILURE;

        return KneadingRecipe.find(method, this::match) != KneadingRecipe.FLAT ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
    }

    @Override
    public boolean isButtonEnable(int button) {
        return this.canPushButton(button) == ButtonProperty.PERMIT;
    }

    @Override
    public void pushButton(EntityPlayer playerIn, int id) {
        if (onWorking()) {
            if (++this.craftTime < this.currentRecipe.time) return;

            this.growSlotContent(TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT, this.currentRecipe.product);
            this.growSlotContent(TileEntityClayWorkTable.ClayWorkTableSlots.CHANGE, this.currentRecipe.change);

            this.currentRecipe = KneadingRecipe.FLAT;
            this.craftTime = -1;
        } else {
            TileEntityClayWorkTable.KneadingMethod method = TileEntityClayWorkTable.KneadingMethod.fromId(id);
            KneadingRecipe suggest = KneadingRecipe.find(method, this::match);

            if (suggest == KneadingRecipe.FLAT || !match(suggest)) return;

            this.currentRecipe = suggest;
            this.craftTime = 0;

            this.shrinkSlotContent(TileEntityClayWorkTable.ClayWorkTableSlots.MATERIAL, this.currentRecipe.material);
            this.setInventorySlotContents(TileEntityClayWorkTable.ClayWorkTableSlots.TOOL.ordinal(), ForgeHooks.getContainerItem(this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.TOOL)));
        }
    }

    private boolean match(KneadingRecipe recipe) {
        if (!UtilItemStack.areTypeEqual(recipe.material, this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.MATERIAL))
                || !recipe.tool.apply(this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.TOOL))) {
            return false;
        }

        // 主産物しかないレシピ
        if (recipe.hasChange()) {
            if (this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT).isEmpty()) return true;

            if (!UtilItemStack.areTypeEqual(this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT), recipe.product)) return false;

            return this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT).getCount() + recipe.product.getCount()
                    <= Math.min(this.getInventoryStackLimit(), this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT).getMaxStackSize());
        }

        // 副産物もあるレシピ
        int count;
        if (!this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.CHANGE).isEmpty()) {
            if (!UtilItemStack.areTypeEqual(this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.CHANGE), recipe.change)) {
                return false;
            }

            count = this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.CHANGE).getCount() + recipe.change.getCount();
            if (count > Math.min(this.getInventoryStackLimit(), this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.CHANGE).getMaxStackSize())) {
                return false;
            }
        }

        count = this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT).getCount() + recipe.product.getCount();
        return count <= Math.min(this.getInventoryStackLimit(), this.getStackInSlot(TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT).getMaxStackSize());
    }


}
