package mods.clayium.machine.ClayWorkTable;

import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IHasButton;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayWorkTable extends TileEntityClayContainer implements IHasButton {
    private static final int[] slotsTop = new int[] { ClayWorkTableSlots.TOOL.ordinal() };
    private static final int[] slotsSide = new int[] { ClayWorkTableSlots.MATERIAL.ordinal() };
    private static final int[] slotsBottom = new int[] { ClayWorkTableSlots.PRODUCT.ordinal(), ClayWorkTableSlots.CHANGE.ordinal() };
    private KneadingRecipe currentRecipe = KneadingRecipe.flat();
    private long craftTime;
    private long timeToCraft;

    public TileEntityClayWorkTable() {
        this.containerItemStacks = NonNullList.withSize(ClayWorkTableSlots.values().length, ItemStack.EMPTY);
        initParamsByTier(0);
    }

    public boolean onWorking() {
        return this.currentRecipe != KneadingRecipe.flat() && this.craftTime >= 0;
    }

    public ItemStack getStackInSlot(ClayWorkTableSlots index) {
        return this.getStackInSlot(index.ordinal());
    }

    public void growSlotContent(ClayWorkTableSlots index, ItemStack stack) {
        if (this.getStackInSlot(index.ordinal()).isEmpty()) {
            this.setInventorySlotContents(index.ordinal(), stack.copy());
            return;
        }
        if (UtilItemStack.areTypeEqual(this.getStackInSlot(index.ordinal()), stack)) {
            this.getStackInSlot(index.ordinal()).grow(stack.getCount());
        }
    }

    public void shrinkSlotContent(ClayWorkTableSlots index, ItemStack stack) {
        if (UtilItemStack.areTypeEqual(this.getStackInSlot(index.ordinal()), stack)) {
            this.getStackInSlot(index.ordinal()).shrink(stack.getCount());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        this.craftTime = tagCompound.getLong("KneadProgress");
//        this.timeToCraft = tagCompound.getLong("TimeToKnead");
        this.currentRecipe = ClayiumRecipes.clayWorkTable.getRecipe(tagCompound.getInteger("RecipeHash"), KneadingRecipe.flat());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setLong("KneadProgress", this.craftTime);
//        tagCompound.setLong("TimeToKnead", this.timeToCraft);
        tagCompound.setInteger("RecipeHash", this.currentRecipe.hashCode());

        return tagCompound;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int pixels) {
//        return this.timeToCraft != 0 && this.currentRecipe != KneadingRecipe.FLAT ? (int) (this.craftTime * pixels / this.timeToCraft) : 0;
        return this.currentRecipe != KneadingRecipe.flat() ? (int) (this.craftTime * pixels / this.currentRecipe.time) : 0;
    }

    public ButtonProperty canPushButton(int button) {
        KneadingMethod method = KneadingMethod.fromId(button);
        if (method == KneadingMethod.UNKNOWN) return ButtonProperty.FAILURE;

        if (onWorking()) {
            return this.currentRecipe.method == method ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
        }

        return !ClayiumRecipes.clayWorkTable.getRecipe(recipe -> recipe.method == method && this.match(recipe), KneadingRecipe.flat()).equals(KneadingRecipe.flat())
                ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
    }

    public boolean isButtonEnable(int button) {
        return this.canPushButton(button) == ButtonProperty.PERMIT;
    }

    public void pushButton(EntityPlayer player, int button) {
        if (onWorking()) {
            if (++this.craftTime < this.currentRecipe.time) return;

            this.growSlotContent(ClayWorkTableSlots.PRODUCT, this.currentRecipe.product);
            this.growSlotContent(ClayWorkTableSlots.CHANGE, this.currentRecipe.change);

            this.currentRecipe = KneadingRecipe.flat();
            this.craftTime = -1;
        } else {
            KneadingMethod method = KneadingMethod.fromId(button);
            if (method == TileEntityClayWorkTable.KneadingMethod.UNKNOWN) return;

            KneadingRecipe suggest = ClayiumRecipes.clayWorkTable.getRecipe(recipe -> recipe.method == method && this.match(recipe), KneadingRecipe.flat());

            if (suggest == KneadingRecipe.flat()) return;

            this.currentRecipe = suggest;
            this.craftTime = 1;

            this.shrinkSlotContent(ClayWorkTableSlots.MATERIAL, this.currentRecipe.material);
            this.setInventorySlotContents(ClayWorkTableSlots.TOOL.ordinal(), this.currentRecipe.getRemainingTool(this.getStackInSlot(ClayWorkTableSlots.TOOL)));
        }

//        sendUpdate();
        markDirty();
    }

    // NOTE: IDK where is the best location to place the method.
    private boolean match(KneadingRecipe recipe) {
        if (!UtilItemStack.areTypeEqual(recipe.material, this.getStackInSlot(ClayWorkTableSlots.MATERIAL))
                || !recipe.tool.apply(this.getStackInSlot(ClayWorkTableSlots.TOOL))) {
            return false;
        }

        if (recipe.hasChange()) {
            if (!this.getStackInSlot(ClayWorkTableSlots.CHANGE).isEmpty()) {
                if (!UtilItemStack.areTypeEqual(this.getStackInSlot(ClayWorkTableSlots.CHANGE), recipe.change)) {
                    return false;
                }

                if (this.getStackInSlot(ClayWorkTableSlots.CHANGE).getCount() + recipe.change.getCount()
                        > Math.min(this.getInventoryStackLimit(), this.getStackInSlot(ClayWorkTableSlots.CHANGE).getMaxStackSize())) {
                    return false;
                }
            }
        }

        if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isEmpty()) return true;

        if (!UtilItemStack.areTypeEqual(this.getStackInSlot(ClayWorkTableSlots.PRODUCT), recipe.product)) return false;

        return this.getStackInSlot(ClayWorkTableSlots.PRODUCT).getCount() + recipe.product.getCount()
                <= Math.min(this.getInventoryStackLimit(), this.getStackInSlot(ClayWorkTableSlots.PRODUCT).getMaxStackSize());
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        markDirty();
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
                case 0: return Roll;
                case 1: return Press;
                case 2: return Bend;
                case 3: return CutRect;
                case 4: return CutCircle;
                case 5: return Slice;
                default: return UNKNOWN;
            }
        }

        public String toBeSuffix() {
            switch (this) {
                case Roll:      return "by_roll";
                case Press:     return "by_press";
                case Bend:      return "by_bend";
                case CutRect:   return "by_cut_r";
                case CutCircle: return "by_cut_c";
                case Slice:     return "by_slice";
                default:        return "";
            }
        }
    }
}
