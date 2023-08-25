package mods.clayium.machine.ClayWorkTable;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.common.ClayiumRecipeProvider;
import mods.clayium.machine.common.IButtonProvider;
import mods.clayium.machine.common.RecipeProvider;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class TileEntityClayWorkTable extends TileEntityGeneric implements ISidedInventory, IButtonProvider, ClayiumRecipeProvider<KneadingRecipe> {
    private static final int[] slotsTop = new int[] { ClayWorkTableSlots.TOOL.ordinal() };
    private static final int[] slotsSide = new int[] { ClayWorkTableSlots.MATERIAL.ordinal() };
    private static final int[] slotsBottom = new int[] { ClayWorkTableSlots.PRODUCT.ordinal(), ClayWorkTableSlots.CHANGE.ordinal() };
    private KneadingRecipe currentRecipe = KneadingRecipe.flat();
    private KneadingMethod currentMethod = KneadingMethod.UNKNOWN;
    private long craftTime;
    private long timeToCraft;

    public TileEntityClayWorkTable() {
        this.containerItemStacks = NonNullList.withSize(ClayWorkTableSlots.values().length, ItemStack.EMPTY);
    }

    public ItemStack getStackInSlot(ClayWorkTableSlots index) {
        return this.getStackInSlot(index.ordinal());
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound tagCompound) {
        super.readMoreFromNBT(tagCompound);

        this.craftTime = tagCompound.getLong("KneadProgress");
        this.timeToCraft = tagCompound.getLong("TimeToKnead");
        this.currentRecipe = ClayiumRecipes.clayWorkTable.getRecipe(tagCompound.getInteger("RecipeHash"), KneadingRecipe.flat());
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound tagCompound) {
        super.writeMoreToNBT(tagCompound);

        tagCompound.setLong("KneadProgress", this.craftTime);
        tagCompound.setLong("TimeToKnead", this.timeToCraft);
        tagCompound.setInteger("RecipeHash", this.currentRecipe.hashCode());

        return tagCompound;
    }

    public ButtonProperty canPushButton(int button) {
        KneadingMethod method = KneadingMethod.fromId(button);
        if (method == KneadingMethod.UNKNOWN) return ButtonProperty.FAILURE;

        if (!this.getWorld().isRemote) {
            if (this.isDoingWork()) {
                return this.currentRecipe.method == method ? ButtonProperty.PERMIT : ButtonProperty.FAILURE;
            }
        }

        return this.getRecipe(e -> e.method == method && this.canCraft(e)).isFlat() ? ButtonProperty.FAILURE : ButtonProperty.PERMIT;
    }

    public boolean isButtonEnable(int button) {
        if (this.getWorld().isRemote && this.currentMethod != KneadingMethod.UNKNOWN) {
            return this.currentMethod.ordinal() == button;
        }

        return this.canPushButton(button) == ButtonProperty.PERMIT;
    }

    public void pushButton(EntityPlayer player, int button) {
        if (!this.world.isRemote) {
            currentMethod = KneadingMethod.fromId(button);

            RecipeProvider.update(this);
        }
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
//        if (IClayInventory.checkBlocked(itemStackIn, direction)) return false;

        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public TierPrefix getRecipeTier() {
        return TierPrefix.none;
    }

    @Override
    public boolean isDoingWork() {
        return !this.currentRecipe.isFlat();
    }

    @Override
    public ClayiumRecipe getRecipeCard() {
        return ClayiumRecipes.clayWorkTable;
    }

    @Override
    public KneadingRecipe getFlat() {
        return KneadingRecipe.flat();
    }

    @Override
    public boolean canCraft(KneadingRecipe recipe) {
        if (!recipe.tool.apply(this.getStackInSlot(ClayWorkTableSlots.TOOL))) {
            return false;
        }

        if (!UtilItemStack.areTypeEqual(recipe.material, this.getStackInSlot(ClayWorkTableSlots.MATERIAL))
                || recipe.material.getCount() > this.getStackInSlot(ClayWorkTableSlots.MATERIAL).getCount()) {
            return false;
        }

        if (recipe.hasChange()) {
            if (UtilTransfer.canProduceItemStack(recipe.change, this.getContainerItemStacks(), ClayWorkTableSlots.CHANGE.ordinal(), this.getInventoryStackLimit()) <= 0) {
                return false;
            }
        }

        return UtilTransfer.canProduceItemStack(recipe.product, this.getContainerItemStacks(), ClayWorkTableSlots.PRODUCT.ordinal(), this.getInventoryStackLimit()) > 0;
    }

    @Override
    public boolean canProceedCraft() {
        return true;
    }

    @Override
    public void proceedCraft() {
        if (++this.craftTime < this.timeToCraft) return;

        UtilTransfer.produceItemStack(this.currentRecipe.product, this.getContainerItemStacks(), ClayWorkTableSlots.PRODUCT.ordinal(), this.getInventoryStackLimit());
        UtilTransfer.produceItemStack(this.currentRecipe.change, this.getContainerItemStacks(), ClayWorkTableSlots.CHANGE.ordinal(), this.getInventoryStackLimit());

        this.currentRecipe = KneadingRecipe.flat();
        this.craftTime = -1L;
        this.timeToCraft = 0L;
        this.currentMethod = KneadingMethod.UNKNOWN;
    }

    @Override
    public boolean setNewRecipe() {
        this.currentRecipe = this.getRecipe(e -> e.method == this.currentMethod && this.canCraft(e));
        if (this.currentRecipe.isFlat()) return false;

        this.craftTime = 1;
        this.timeToCraft = this.currentRecipe.time;

        UtilTransfer.consumeItemStack(this.currentRecipe.material, this.getContainerItemStacks(), ClayWorkTableSlots.MATERIAL.ordinal());
        this.setInventorySlotContents(ClayWorkTableSlots.TOOL.ordinal(), this.currentRecipe.getRemainingTool(this.getStackInSlot(ClayWorkTableSlots.TOOL)));

        return true;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return (int) this.timeToCraft;
            case 1:
                return (int) this.craftTime;
            case 2:
                return this.currentMethod.ordinal();
        }

        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.timeToCraft = value;
                break;
            case 1:
                this.craftTime = value;
                break;
            case 2:
                this.currentMethod = KneadingMethod.fromId(value);
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    public enum ClayWorkTableSlots {
        MATERIAL,
        TOOL,
        PRODUCT,
        CHANGE
    }
}
