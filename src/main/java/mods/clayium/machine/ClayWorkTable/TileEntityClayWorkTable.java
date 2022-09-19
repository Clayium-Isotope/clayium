package mods.clayium.machine.ClayWorkTable;

import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.common.IHasButton;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayWorkTable extends TileEntityClayContainer implements IHasButton {
    public enum ClayWorkTableSlots {
        MATERIAL,
        TOOL,
        PRODUCT,
        CHANGE
    }

    private static final int[] slotsTop = new int[] { ClayWorkTableSlots.TOOL.ordinal() };
    private static final int[] slotsSide = new int[] { ClayWorkTableSlots.MATERIAL.ordinal() };
    private static final int[] slotsBottom = new int[] { ClayWorkTableSlots.PRODUCT.ordinal(), ClayWorkTableSlots.CHANGE.ordinal() };
    private long craftTime;
    private long timeToCraft;
    private int cookingMethod = -1;

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

        craftTime = tagCompound.getLong("kneadProgress");
        timeToCraft = tagCompound.getLong("TimeToKnead");
        cookingMethod = tagCompound.getInteger("cookingMethod");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setLong("kneadProgress", craftTime);
        tagCompound.setLong("TimeToKnead", timeToCraft);
        tagCompound.setInteger("CookingMethod", cookingMethod);

        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);

        boolean stateChanged = false;

        if (!ClayiumRecipes.hasResult(ClayiumRecipes.clayWorkTable, getStackInSlot(ClayWorkTableSlots.MATERIAL), getStackInSlot(ClayWorkTableSlots.TOOL))) {
            craftTime = 0;
            timeToCraft = 0;
            cookingMethod = -1;

            stateChanged = true;
        }

        if (stateChanged) {
            sendUpdate();
        }
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
        return this.timeToCraft != 0 && this.cookingMethod != -1 ? (int) (this.craftTime * pixels / this.timeToCraft) : 0;
    }

    private boolean canKnead(int method) {
        if (this.getStackInSlot(ClayWorkTableSlots.MATERIAL).isEmpty()) {
            return false;
        }

        RecipeElement recipe = ClayiumRecipes.getRecipeElement(ClayiumRecipes.clayWorkTable, containerItemStacks, method, 0);
        if (recipe.getResult().getResults().get(0).isEmpty()) {
            return false;
        }
        if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isEmpty()) {
            return true;
        }
        if (!this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isItemEqual(recipe.getResult().getResults().get(0))) {
            return false;
        }

        int count;
        if (!recipe.getResult().getResults().get(1).isEmpty() && !this.getStackInSlot(ClayWorkTableSlots.CHANGE).isEmpty()) {
            if (!this.getStackInSlot(ClayWorkTableSlots.CHANGE).isItemEqual(recipe.getResult().getResults().get(1))) {
                return false;
            }

            count = this.getStackInSlot(ClayWorkTableSlots.CHANGE).getCount() + recipe.getResult().getResults().get(1).getCount();
            if (count > this.getInventoryStackLimit() || count > this.getStackInSlot(ClayWorkTableSlots.CHANGE).getMaxStackSize()) {
                return false;
            }
        }

        count = this.getStackInSlot(ClayWorkTableSlots.PRODUCT).getCount() + recipe.getResult().getResults().get(0).getCount();
        return count <= this.getInventoryStackLimit() && count <= this.getStackInSlot(ClayWorkTableSlots.PRODUCT).getMaxStackSize();
    }

    public ButtonProperty canPushButton(int button) {
        if (!this.getStackInSlot(ClayWorkTableSlots.TOOL).isEmpty()) {
            if (button == 2) {
                if (this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() != ClayiumItems.rollingPin)
                    return ButtonProperty.FAILURE;
            }
            if (button == 3 || button == 5) {
                if (this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() != ClayiumItems.slicer
                        && this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() != ClayiumItems.spatula)
                    return ButtonProperty.FAILURE;
            }
            if (button == 4) {
                if (this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() != ClayiumItems.spatula)
                    return ButtonProperty.FAILURE;
            }
        }

        if (this.cookingMethod == button && this.canKnead(button)) {
            return ButtonProperty.PERMIT;
        }

        if (this.canKnead(button)) {
            return this.cookingMethod == -1 ? ButtonProperty.PERMIT : ButtonProperty.PROPOSE;
        }

        return ButtonProperty.FAILURE;
    }

    public boolean isButtonEnable(int button) {
        return canPushButton(button) != ButtonProperty.FAILURE;
    }

    public void pushButton(EntityPlayer player, int button) {
        ButtonProperty canPushButton = this.canPushButton(button);
        if (canPushButton == ButtonProperty.FAILURE) return;

        RecipeElement recipe = ClayiumRecipes.getRecipeElement(ClayiumRecipes.clayWorkTable, this.containerItemStacks, button, 0);

        if (!recipe.getCondition().getMaterials().get(ClayWorkTableSlots.TOOL.ordinal()).isEmpty()
                && !this.getStackInSlot(ClayWorkTableSlots.TOOL).isEmpty()
        ) {
            this.containerItemStacks.set(ClayWorkTableSlots.TOOL.ordinal(),
                    ForgeHooks.getContainerItem(this.getStackInSlot(ClayWorkTableSlots.TOOL)));
        }

        if (canPushButton == ButtonProperty.PERMIT) {
            if (this.cookingMethod == -1) {
                this.timeToCraft = recipe.getResult().getTime();
                this.cookingMethod = button;
                this.craftTime = 0;
            }

            ++this.craftTime;
            if (this.craftTime >= this.timeToCraft) {
                ItemStack product = recipe.getResult().getResults().get(0);
                ItemStack change = recipe.getResult().getResults().get(1);
                this.craftTime = 0;
                this.cookingMethod = -1;

                this.getStackInSlot(ClayWorkTableSlots.MATERIAL).shrink(recipe.getCondition().getMaterials().get(ClayWorkTableSlots.MATERIAL.ordinal()).getCount());

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
        }

        if (canPushButton == ButtonProperty.PROPOSE) {
            this.craftTime = 0;
            this.timeToCraft = 0;
            this.cookingMethod = -1;
        }

        sendUpdate();
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
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: return (int) this.timeToCraft;
            case 1: return (int) this.craftTime;
            case 2: return this.cookingMethod;
            default: return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: timeToCraft = value; break;
            case 1: craftTime = value; break;
            case 2: cookingMethod = value; break;
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }
}
