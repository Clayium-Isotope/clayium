package mods.clayium.machines.ClayWorkTable;

import mods.clayium.item.ClayiumItems;
import mods.clayium.item.crafting.ClayWorkTableRecipes;
import mods.clayium.machines.common.IClicker;
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

public class TileEntityClayWorkTable extends TileEntity implements ISidedInventory, IClicker {
    public enum ClayWorkTableSlots {
        MATERIAL,
        TOOL,
        PRODUCT,
        CHANGE
    }

    private static final int[] slotsTop = new int[] { ClayWorkTableSlots.TOOL.ordinal() };
    private static final int[] slotsSide = new int[] { ClayWorkTableSlots.MATERIAL.ordinal() };
    private static final int[] slotsBottom = new int[] { ClayWorkTableSlots.PRODUCT.ordinal(), ClayWorkTableSlots.CHANGE.ordinal() };
    private NonNullList<ItemStack> inventory = NonNullList.withSize(ClayWorkTableSlots.values().length, ItemStack.EMPTY);
    private int kneadedTimes;
    private int kneadTime;
    private int cookingMethod = -1;
    private String customName;

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }

    public ItemStack getStackInSlot(ClayWorkTableSlots slot) {
        return this.getStackInSlot(slot.ordinal());
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(inventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventory)
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
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tagCompound, this.inventory);

        this.kneadedTimes = tagCompound.getShort("kneadProgress");
        this.kneadTime = tagCompound.getShort("TimeToKnead");
        this.cookingMethod = tagCompound.getShort("cookingMethod");

        if (tagCompound.hasKey("CustomName", 8)) {
            this.customName = tagCompound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setShort("kneadProgress", (short) this.kneadedTimes);
        tagCompound.setShort("TimeToKnead", (short) this.kneadTime);
        tagCompound.setShort("CookingMethod", (short) this.cookingMethod);

        ItemStackHelper.saveAllItems(tagCompound, this.inventory);

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
        return this.kneadTime != 0 && this.cookingMethod != -1 ? this.kneadedTimes * pixels / this.kneadTime : 0;
    }

//    @Override
//    public void update() {
//        boolean stateChanged = false;
//
//        updateEntity();
//
//        if (!ClayWorkTableRecipes.instance().hasKneadingResult(getStackInSlot(ClayWorkTableSlots.MATERIAL))) {
//            this.kneadProgress = 0;
//            this.timeToKnead = 0;
//            this.cookingMethod = ClayWorkTableActions.UNKNOWN;
//
//            stateChanged = true;
//        }
//
//        if (stateChanged) {
//            sendUpdate();
//        }
//    }

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

        ClayWorkTableRecipes.RecipeElement recipe = ClayWorkTableRecipes.instance().getKneadingResultMap(method, this.inventory);
        if (recipe.getProduct().isEmpty()) {
            return false;
        }
        if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isEmpty()) {
            return true;
        }
        if (!this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isItemEqual(recipe.getProduct())) {
            return false;
        }

        int count;
        if (!recipe.getChange().isEmpty() && !this.getStackInSlot(ClayWorkTableSlots.CHANGE).isEmpty()) {
            if (!this.getStackInSlot(ClayWorkTableSlots.CHANGE).isItemEqual(recipe.getChange())) {
                return false;
            }

            count = this.getStackInSlot(ClayWorkTableSlots.CHANGE).getCount() + recipe.getChange().getCount();
            if (count > this.getInventoryStackLimit() || count > this.getStackInSlot(ClayWorkTableSlots.CHANGE).getMaxStackSize()) {
                return false;
            }
        }

        count = this.getStackInSlot(ClayWorkTableSlots.PRODUCT).getCount() + recipe.getProduct().getCount();
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

        ClayWorkTableRecipes.RecipeElement recipe = ClayWorkTableRecipes.RecipeElement.FLAT;
        if (canPushButton != ButtonProperty.FAILURE) {
            recipe = ClayWorkTableRecipes.instance().getKneadingResultMap(button, this.inventory);
        }

        if (canPushButton != ButtonProperty.FAILURE
                //                && buttonId >= 2
                && !recipe.getRequireTool().isEmpty()
                && !this.getStackInSlot(ClayWorkTableSlots.TOOL).isEmpty()
                && this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem().hasContainerItem(this.getStackInSlot(ClayWorkTableSlots.TOOL))) {
            this.inventory.set(ClayWorkTableSlots.TOOL.ordinal(),
                    this.getStackInSlot(ClayWorkTableSlots.TOOL).getItem().getContainerItem(this.getStackInSlot(ClayWorkTableSlots.TOOL)));
        }

        if (canPushButton == ButtonProperty.PERMIT) {
            if (this.cookingMethod == -1) {
                this.kneadTime = recipe.getKneadTime();
                this.cookingMethod = button;
                this.kneadedTimes = 0;
            }

            ++this.kneadedTimes;
            if (this.kneadedTimes >= this.kneadTime) {
                ItemStack product = recipe.getProduct();
                ItemStack change = recipe.getChange();
                this.kneadedTimes = 0;
                this.cookingMethod = -1;

                this.getStackInSlot(ClayWorkTableSlots.MATERIAL).shrink(recipe.getMaterial().getCount());

                if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isEmpty()) {
                    this.inventory.set(ClayWorkTableSlots.PRODUCT.ordinal(), product.copy());
                } else if (this.getStackInSlot(ClayWorkTableSlots.PRODUCT).isItemEqual(product)) {
                    this.getStackInSlot(ClayWorkTableSlots.PRODUCT).grow(product.getCount());
                }

                if (!change.isEmpty()) {
                    if (this.getStackInSlot(ClayWorkTableSlots.CHANGE).isEmpty()) {
                        this.inventory.set(ClayWorkTableSlots.CHANGE.ordinal(), change.copy());
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
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) == this)
            return player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64.0D;
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

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

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

//    @Override
//    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
//        return new ContainerClayWorkTable(this.getWorld(), this.getPos(), playerIn);
//    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: return this.kneadTime;
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

    @Override
    public void clear() {
        this.inventory.clear();
    }
}
