package mods.clayium.machine.ClayWorkTable;

import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.common.IClicker;
import mods.clayium.machine.common.TileEntitySidedClayContainer;
import mods.clayium.machine.crafting.ClayiumRecipes;
import mods.clayium.machine.crafting.RecipeElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityClayWorkTable extends TileEntitySidedClayContainer implements IClicker {
    public enum ClayWorkTableSlots {
        MATERIAL,
        TOOL,
        PRODUCT,
        CHANGE
    }

    private static final int[] slotsTop = new int[] { ClayWorkTableSlots.TOOL.ordinal() };
    private static final int[] slotsSide = new int[] { ClayWorkTableSlots.MATERIAL.ordinal() };
    private static final int[] slotsBottom = new int[] { ClayWorkTableSlots.PRODUCT.ordinal(), ClayWorkTableSlots.CHANGE.ordinal() };
    private int kneadedTimes;
    private long kneadTime;
    private int cookingMethod = -1;
    private String customName;

    public TileEntityClayWorkTable() {
        super(ClayWorkTableSlots.values().length);
    }

    @Override
    public int getSizeInventory() {
        return this.machineInventory.size();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.machineInventory.get(index);
    }

    public ItemStack getStackInSlot(ClayWorkTableSlots slot) {
        return this.getStackInSlot(slot.ordinal());
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(machineInventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.machineInventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.machineInventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.machineInventory)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    @Override
    public String getName() {
        return hasCustomName() ? customName : getBlockType().getLocalizedName();
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && !customName.isEmpty();
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public ITextComponent getDisplayName() {
        return hasCustomName() ? new TextComponentString(getName()) : new TextComponentTranslation(getName());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        machineInventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tagCompound, machineInventory);

        kneadedTimes = tagCompound.getInteger("kneadProgress");
        kneadTime = tagCompound.getLong("TimeToKnead");
        cookingMethod = tagCompound.getInteger("cookingMethod");

        if (tagCompound.hasKey("CustomName", 8)) {
            this.customName = tagCompound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("kneadProgress", kneadedTimes);
        tagCompound.setLong("TimeToKnead", kneadTime);
        tagCompound.setInteger("CookingMethod", cookingMethod);

        ItemStackHelper.saveAllItems(tagCompound, machineInventory);

        if (hasCustomName()) {
            tagCompound.setString("CustomName", customName);
        }

        return tagCompound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());

        boolean stateChanged = false;

        //        updateEntity();

        if (!ClayiumRecipes.hasResult(ClayiumRecipes.clayWorkTable, getStackInSlot(ClayWorkTableSlots.MATERIAL), getStackInSlot(ClayWorkTableSlots.TOOL))) {
            kneadedTimes = 0;
            kneadTime = 0;
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
        ClayWorkTable.updateBlockState(world, pos);

        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

//    @Override
//    public String getGuiID() {
//        return "clayium:clay_work_table";
//    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int pixels) {
        return this.kneadTime != 0 && this.cookingMethod != -1 ? (int) (this.kneadedTimes * pixels / this.kneadTime) : 0;
    }

    public void updateEntity() {
        int maxTransfer = 1;

        for (EnumFacing fromSide : EnumFacing.VALUES) {
            EnumFacing toSide = fromSide.getOpposite();
            TileEntity rawTileEntity = world.getTileEntity(pos.add(fromSide.getFrontOffsetX(), fromSide.getFrontOffsetY(), fromSide.getFrontOffsetZ()));

            if (rawTileEntity instanceof IInventory) {
                IInventory fromTileEntity = this;
                IInventory toTileEntity = (IInventory) rawTileEntity;
                int[] toSlots;

                if (toTileEntity instanceof ISidedInventory) {
                    toSlots = ((ISidedInventory)toTileEntity).getSlotsForFace(toSide);
                } else {
                    toSlots = new int[toTileEntity.getSizeInventory()];

                    for (int i = 0; i < toTileEntity.getSizeInventory(); i++) toSlots[i] = i;
                }

                int oldTransfer = maxTransfer;
                ISidedInventory fromSided = fromTileEntity instanceof ISidedInventory ? (ISidedInventory) fromTileEntity : null;
                ISidedInventory toSided = toTileEntity instanceof ISidedInventory ? (ISidedInventory) toTileEntity : null;

                for (int fromSlot : slotsBottom) {
                    ItemStack fromItem = fromTileEntity.getStackInSlot(fromSlot);
                    if (fromItem.isEmpty() && (fromSided == null || fromSided.canExtractItem(fromSlot, fromItem, fromSide))) {
                        ItemStack toItem;
                        if (fromItem.isStackable()) {
                            for (int toSlot : toSlots) {
                                toItem = toTileEntity.getStackInSlot(toSlot);

                                if (toItem.isEmpty()
                                        && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide))
                                        && fromItem.isItemEqual(toItem)
                                        && ItemStack.areItemStackTagsEqual(toItem, fromItem)) {
                                    int maxSize = Math.min(toItem.getMaxStackSize(), toTileEntity.getInventoryStackLimit());
                                    int maxMove = Math.min(maxSize - toItem.getCount(), Math.min(maxTransfer, fromItem.getCount()));
                                    toItem.grow(maxMove);
                                    maxTransfer -= maxMove;

                                    if (fromItem.isEmpty()) {
                                        fromTileEntity.setInventorySlotContents(fromSlot, ItemStack.EMPTY);
                                    }
                                    if (maxTransfer == 0) { return; }
                                    if (fromItem.isEmpty()) { break; }
                                }
                            }
                        }

                        if (!fromItem.isEmpty()) {
                            for (int toSlot : toSlots) {
                                toItem = toTileEntity.getStackInSlot(toSlot);
                                if (toItem.isEmpty() && toTileEntity.isItemValidForSlot(toSlot, fromItem)
                                        && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide))) {
                                    toItem = fromItem.copy();
                                    toItem.setCount(Math.min(maxTransfer, fromItem.getCount()));
                                    toTileEntity.setInventorySlotContents(toSlot, toItem);
                                    maxTransfer -= toItem.getCount();
                                    fromItem.shrink(toItem.getCount());

                                    if (fromItem.isEmpty()) {
                                        fromTileEntity.setInventorySlotContents(fromSlot, ItemStack.EMPTY);
                                    }
                                    if (maxTransfer == 0) { return; }
                                    if (fromItem.isEmpty()) { break; }
                                }
                            }
                        }
                    }
                }

                if (oldTransfer != maxTransfer) {
                    toTileEntity.markDirty();
                    fromTileEntity.markDirty();
                }
            }
        }
    }


    private boolean canKnead(int method) {
        if (this.getStackInSlot(ClayWorkTableSlots.MATERIAL).isEmpty()) {
            return false;
        }

        RecipeElement recipe = ClayiumRecipes.getRecipeElement(ClayiumRecipes.clayWorkTable, machineInventory, method, 0);
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
        if (button == 2) {
            if (!(!this.getStackInSlot(ClayWorkTableSlots.TOOL).isEmpty()
                    && this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() == ClayiumItems.clayRollingPin))
                return ButtonProperty.FAILURE;
        }
        if (button == 3 || button == 5) {
            if (!(!this.getStackInSlot(ClayWorkTableSlots.TOOL).isEmpty()
                    && (this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() == ClayiumItems.claySlicer
                    || this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() == ClayiumItems.claySpatula)))
                return ButtonProperty.FAILURE;
        }
        if (button == 4) {
            if (!(!this.getStackInSlot(ClayWorkTableSlots.TOOL).isEmpty()
                    && this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem() == ClayiumItems.claySpatula))
                return ButtonProperty.FAILURE;
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

        RecipeElement recipe = RecipeElement.FLAT;
        if (canPushButton != ButtonProperty.FAILURE) {
            recipe = ClayiumRecipes.getRecipeElement(ClayiumRecipes.clayWorkTable, this.machineInventory, button, 0);
        }

        if (canPushButton != ButtonProperty.FAILURE
                //                && buttonId >= 2
                && !recipe.getCondition().getMaterials().get(ClayWorkTableSlots.TOOL.ordinal()).isEmpty()
                && !this.getStackInSlot(ClayWorkTableSlots.TOOL).isEmpty()
                && this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem().hasContainerItem(this.getStackInSlot(ClayWorkTableSlots.TOOL))) {
            this.machineInventory.set(ClayWorkTableSlots.TOOL.ordinal(),
                    this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem().getContainerItem(this.getStackInSlot(ClayWorkTableSlots.TOOL)));
        }

        if (canPushButton == ButtonProperty.PERMIT) {
            if (this.cookingMethod == -1) {
                this.kneadTime = recipe.getResult().getTime();
                this.cookingMethod = button;
                this.kneadedTimes = 0;
            }

            ++this.kneadedTimes;
            if (this.kneadedTimes >= this.kneadTime) {
                ItemStack product = recipe.getResult().getResults().get(0);
                ItemStack change = recipe.getResult().getResults().get(1);
                this.kneadedTimes = 0;
                this.cookingMethod = -1;

                this.getStackInSlot(ClayWorkTableSlots.MATERIAL).shrink(recipe.getCondition().getMaterials().get(ClayWorkTableSlots.MATERIAL.ordinal()).getCount());

                if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isEmpty()) {
                    this.machineInventory.set(ClayWorkTableSlots.PRODUCT.ordinal(), product.copy());
                } else if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isItemEqual(product)) {
                    this.getStackInSlot(ClayWorkTableSlots.PRODUCT).grow(product.getCount());
                }

                if (!change.isEmpty()) {
                    if (this.getStackInSlot(ClayWorkTableSlots.CHANGE).isEmpty()) {
                        this.machineInventory.set(ClayWorkTableSlots.CHANGE.ordinal(), change.copy());
                    } else if (this.getStackInSlot(ClayWorkTableSlots.CHANGE).isItemEqual(change)) {
                        this.getStackInSlot(ClayWorkTableSlots.CHANGE).grow(change.getCount());
                    }
                }
            }
        }

        if (canPushButton == ButtonProperty.PROPOSE) {
            this.kneadedTimes = 0;
            this.kneadTime = 0;
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
        if (index == 1) return ClayiumItems.isItemTool(itemstack);
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

//    @Override
//    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
//        return new ContainerClayWorkTable(this.getWorld(), this.getPos(), playerIn);
//    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: return (int) this.kneadTime;
            case 1: return this.kneadedTimes;
            case 2: return this.cookingMethod;
            default: return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: kneadTime = value; break;
            case 1: kneadedTimes = value; break;
            case 2: cookingMethod = value; break;
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }
}
