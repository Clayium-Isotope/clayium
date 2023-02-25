package mods.clayium.machine.ClayWorkTable;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IHasButton;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.KneadingRecipeElement;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayWorkTable extends TileEntityClayContainer implements IHasButton {
    private static final int[] slotsTop = new int[] { ClayWorkTableSlots.TOOL.ordinal() };
    private static final int[] slotsSide = new int[] { ClayWorkTableSlots.MATERIAL.ordinal() };
    private static final int[] slotsBottom = new int[] { ClayWorkTableSlots.PRODUCT.ordinal(), ClayWorkTableSlots.CHANGE.ordinal() };
    public KneadingRecipeElement craftingRecipe = KneadingRecipeElement.FLAT;
    private long craftTime;
    private long timeToCraft;

    public TileEntityClayWorkTable() {
        this.containerItemStacks = NonNullList.withSize(ClayWorkTableSlots.values().length, ItemStack.EMPTY);
        initParamsByTier(0);
    }

    public ItemStack getStackInSlot(ClayWorkTableSlots slot) {
        return this.getStackInSlot(slot.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        this.craftTime = tagCompound.getLong("KneadProgress");
        this.timeToCraft = tagCompound.getLong("TimeToKnead");
        RecipeElement temp = ClayiumRecipes.getRecipeElement(ClayiumRecipes.clayWorkTable, tagCompound.getInteger("RecipeHash"));
        if (temp == RecipeElement.FLAT) temp = KneadingRecipeElement.FLAT;
        this.craftingRecipe = (KneadingRecipeElement) temp;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setLong("KneadProgress", this.craftTime);
        tagCompound.setLong("TimeToKnead", this.timeToCraft);
        tagCompound.setInteger("RecipeHash", this.craftingRecipe.hashCode());

        return tagCompound;
    }

    private void sendUpdate() {
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(pos, tileentity);
        }

        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int pixels) {
        return this.timeToCraft != 0 && this.craftingRecipe != RecipeElement.FLAT ? (int) (this.craftTime * pixels / this.timeToCraft) : 0;
    }

    private boolean canKnead(int method) {
        // assert 作業中でない

        // 素材スロットが空なら不可
        if (this.getStackInSlot(ClayWorkTableSlots.MATERIAL).isEmpty()) {
            return false;
        }

        RecipeElement recipe = ClayiumRecipes.getRecipeElement(ClayiumRecipes.clayWorkTable, this.containerItemStacks, method, 0);
        // 該当レシピ無し
        if (recipe == RecipeElement.FLAT) {
            return false;
        }
        if (recipe == KneadingRecipeElement.FLAT) {
            return false;
        }

        // assert 割り当て可能なレシピが存在している

        // 主産物しかないレシピ
        if (recipe.getResult().getResults().size() == 1) {
            if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isEmpty()) return true;

            if (!UtilItemStack.areTypeEqual(this.getStackInSlot(ClayWorkTableSlots.PRODUCT), recipe.getResult().getResults().get(0))) return false;

            return this.getStackInSlot(ClayWorkTableSlots.PRODUCT).getCount() + recipe.getResult().getResults().get(0).getCount()
                    <= Math.min(this.getInventoryStackLimit(), this.getStackInSlot(ClayWorkTableSlots.PRODUCT).getMaxStackSize());
        }

        // 副産物もあるレシピ

        int count;
        if (!recipe.getResult().getResults().get(1).isEmpty() && !this.getStackInSlot(ClayWorkTableSlots.CHANGE).isEmpty()) {
            if (!UtilItemStack.areTypeEqual(this.getStackInSlot(ClayWorkTableSlots.CHANGE), recipe.getResult().getResults().get(1))) {
                return false;
            }

            count = this.getStackInSlot(ClayWorkTableSlots.CHANGE).getCount() + recipe.getResult().getResults().get(1).getCount();
            if (count > Math.min(this.getInventoryStackLimit(), this.getStackInSlot(ClayWorkTableSlots.CHANGE).getMaxStackSize())) {
                return false;
            }
        }

        count = this.getStackInSlot(ClayWorkTableSlots.PRODUCT).getCount() + recipe.getResult().getResults().get(0).getCount();
        return count <= Math.min(this.getInventoryStackLimit(), this.getStackInSlot(ClayWorkTableSlots.PRODUCT).getMaxStackSize());
    }

    public ButtonProperty canPushButton(int button) {
        // 作業実行中は確認不要
        if (this.craftingRecipe != KneadingRecipeElement.FLAT && this.craftingRecipe.method == button) {
            return ButtonProperty.PERMIT;
        }

        // assert 作業中でない

        // 道具とメソッドが整合しないなら、失敗判定
        if (!this.getStackInSlot(ClayWorkTableSlots.TOOL).isEmpty()) {
            switch (button) {
                case 2:  // 圧延は麺棒でのみおこなう
                    if (this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() != ClayiumItems.rollingPin)
                        return ButtonProperty.FAILURE;
                    break;
                case 3:
                case 5:  // 矩形に打抜くのはスライサーでもへらでも出来る
                    if (this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() != ClayiumItems.slicer
                            && this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() != ClayiumItems.spatula)
                        return ButtonProperty.FAILURE;
                    break;
                case 4:  // 円形に切り出すのはへらでのみおこなう
                    if (this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() != ClayiumItems.spatula)
                        return ButtonProperty.FAILURE;
                    break;
            }
        }

        // 素材の判定は別メソッドで
        return this.canKnead(button) ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
    }

    public boolean isButtonEnable(int button) {
        return this.canPushButton(button) == ButtonProperty.PERMIT;
    }

    public void pushButton(EntityPlayer player, int button) {
        if (this.craftingRecipe == KneadingRecipeElement.FLAT) {
            if (this.canPushButton(button) == ButtonProperty.PERMIT) {
                this.craftingRecipe = ClayiumRecipes.getClayWorkTableRecipeElement(this.getStackInSlot(ClayWorkTableSlots.MATERIAL), this.getStackInSlot(ClayWorkTableSlots.TOOL), button);
                if (this.craftingRecipe == KneadingRecipeElement.FLAT) return;

                ClayiumCore.logger.info("Put recipe into " + this.craftingRecipe.hashCode());
                this.timeToCraft = this.craftingRecipe.time;
                this.craftTime = 0;
                this.getStackInSlot(ClayWorkTableSlots.MATERIAL).shrink(this.craftingRecipe.material.getCount());

                if (this.craftingRecipe.tool.apply(this.getStackInSlot(ClayWorkTableSlots.TOOL))) {
                    this.containerItemStacks.set(ClayWorkTableSlots.TOOL.ordinal(), ForgeHooks.getContainerItem(this.getStackInSlot(ClayWorkTableSlots.TOOL)));
                }
            } else {
                return;
            }
        }

        if (++this.craftTime >= this.timeToCraft) {
            ItemStack product = this.craftingRecipe.result;
            ItemStack change = this.craftingRecipe.change;
            this.craftTime = 0;
            this.craftingRecipe = KneadingRecipeElement.FLAT;

            if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isEmpty()) {
                this.containerItemStacks.set(ClayWorkTableSlots.PRODUCT.ordinal(), product.copy());
            } else if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isItemEqual(product)) {
                this.getStackInSlot(ClayWorkTableSlots.PRODUCT).grow(product.getCount());
            }

            if (!change.isEmpty()) {
                if (this.getStackInSlot(ClayWorkTableSlots.CHANGE).isEmpty()) {
                    this.containerItemStacks.set(ClayWorkTableSlots.CHANGE.ordinal(), change.copy());
                } else if (this.getStackInSlot(ClayWorkTableSlots.CHANGE).isItemEqual(change)) {
                    this.getStackInSlot(ClayWorkTableSlots.CHANGE).grow(change.getCount());
                }
            }
        }

//        sendUpdate();
        markDirty();
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        sendUpdate();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemstack) {
        if (index == 0) return true;
        if (index == 1) return ClayiumItems.isWorkTableTool(itemstack);
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.UP) return slotsTop;
        if (side == EnumFacing.DOWN) return slotsBottom;
        return slotsSide;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (checkBlocked(itemStackIn, direction)) return false;

        return isItemValidForSlot(index, itemStackIn);
    }

    public enum ClayWorkTableSlots {
        MATERIAL,
        TOOL,
        PRODUCT,
        CHANGE
    }

    public enum KneadingMethod {
        Roll,       // 転がす
        Press,      // つぶす
        Bend,       // 引き延ばす
        CutRect,    // 矩形に切る
        CutCircle,  // 円形に切る
        Slice,      // 断面を切る
        UNKNOWN;    // 未知用

        public static KneadingMethod fromId(int id) {
            switch (id) {
                case 0:
                    return Roll;
                case 1:
                    return Press;
                case 2:
                    return Bend;
                case 3:
                    return CutRect;
                case 4:
                    return CutCircle;
                case 5:
                    return Slice;
                default:
                    return UNKNOWN;
            }
        }
    }
}
