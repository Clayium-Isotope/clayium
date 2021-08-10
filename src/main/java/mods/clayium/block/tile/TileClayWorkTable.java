package mods.clayium.block.tile;

import mods.clayium.block.BlockClayWorkTable;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.container.ContainerClayWorkTable;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.crafting.ClayWorkTableRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileClayWorkTable extends TileEntityLockableLoot implements ISidedInventory, ITickable {
    private static final int[] slotsTop = new int[]    {1};
    private static final int[] slotsBottom = new int[]{0, 2};
    private static final int[] slotsSides = new int[]    {3};
    private NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
    public int furnaceBurnTime;
    public int kneadProgress;
    private String furnaceName = "container.clay_work_table";
    public int timeToKnead;
    public int cookingMethod = -1;
    private ForgeChunkManager.Ticket ticket;
    public IMerchant merchant;
    public MerchantRecipe currentRecipe;
    public int currentRecipeIndex = 0;
    private int toSellSlotIndex = 2;

    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.furnaceItemStacks)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.furnaceItemStacks.get(slot);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.furnaceItemStacks.get(index) == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }

        ItemStack itemstack;
        if (this.furnaceItemStacks.get(index).getCount() <= count) {
            itemstack = this.furnaceItemStacks.get(index);
            this.furnaceItemStacks.set(index, ItemStack.EMPTY);
        } else {
            itemstack = this.furnaceItemStacks.get(index).splitStack(count);
            if (this.furnaceItemStacks.get(index).getCount() == 0) {
                this.furnaceItemStacks.set(index, ItemStack.EMPTY);
            }
        }

        if (this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.furnaceItemStacks.get(index) == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }

        ItemStack itemstack = this.furnaceItemStacks.get(index);
        this.furnaceItemStacks.set(index, ItemStack.EMPTY);
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        this.furnaceItemStacks.set(slot, itemstack);
        if (itemstack != ItemStack.EMPTY && itemstack.getCount() > this.getInventoryStackLimit()) {
            itemstack.setCount(this.getInventoryStackLimit());
        }

        if (this.inventoryResetNeededOnSlotChange(slot)) {
            this.resetRecipeAndSlots();
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.furnaceName : this.getBlockType().getLocalizedName();
    }

    @Override
    public boolean hasCustomName() {
        return this.furnaceName != null && this.furnaceName.length() > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        NBTTagList tagList = tagCompound.getTagList("Items", 10);
        this.furnaceItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);

        for(int i = 0; i < tagList.tagCount(); i++) {
            this.furnaceItemStacks.set(i, new ItemStack(tagList.getCompoundTagAt(i)));
        }

        this.kneadProgress = tagCompound.getShort("kneadProgress");
        this.furnaceBurnTime = tagCompound.getShort("BurnTime");
        this.timeToKnead = tagCompound.getShort("TimeToKnead");
        this.cookingMethod = tagCompound.getShort("CookingMethod");
        if (tagCompound.hasKey("CustomName", 8)) {
            this.furnaceName = tagCompound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
        tagCompound.setShort("kneadProgress", (short)this.kneadProgress);
        tagCompound.setShort("TimeToKnead", (short)this.timeToKnead);
        tagCompound.setShort("CookingMethod", (short)this.cookingMethod);
        NBTTagList tagList = new NBTTagList();

        for(int i = 0; i < this.furnaceItemStacks.size(); i++) {
//            if (this.furnaceItemStacks.get(i) != ItemStack.EMPTY) {
                NBTTagCompound tagCompound1 = new NBTTagCompound();
                tagCompound1.setByte("Slot", (byte)i);
                this.furnaceItemStacks.get(i).writeToNBT(tagCompound1);
                tagList.appendTag(tagCompound1);
//            }
        }

        tagCompound.setTag("Items", tagList);
        if (this.hasCustomName()) {
            tagCompound.setString("CustomName", this.furnaceName);
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
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public String getGuiID() {
        return "clayium:clay_work_table";
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int par1) {
        return this.timeToKnead != 0 && this.cookingMethod != -1 ? this.kneadProgress * par1 / this.timeToKnead : 0;
    }

    public boolean isBurning() {
        return false;
//        return this.furnaceBurnTime > 0;
    }

    public void update() {
        int maxTransfer = 1;
        int[] fromSlots = new int[]{2, 3};

        for (EnumFacing direction : EnumFacing.VALUES) {
            this.transfer(this, fromSlots, direction, maxTransfer);
        }

        this.resetRecipeAndSlots();
        if (!this.world.isRemote && this.ticket == null) {
            this.ticket = ForgeChunkManager.requestTicket(ClayiumCore.instance(), this.world, ForgeChunkManager.Type.NORMAL);
            ForgeChunkManager.forceChunk(this.ticket, new ChunkPos(this.pos));
        }

        boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;
        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote) {
            if (this.furnaceBurnTime == 0 && this.canSmelt()) {
                this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks.get(0));
                if (this.isBurning()) {
                    flag1 = true;
                    if (this.furnaceItemStacks.get(0) != ItemStack.EMPTY) {
                        ItemStack stack = this.furnaceItemStacks.get(0);
                        stack.setCount(stack.getCount() - 1);
                        this.furnaceItemStacks.set(0, stack);
                        if (this.furnaceItemStacks.get(0).getCount() == 0) {
                            this.furnaceItemStacks.set(0, this.furnaceItemStacks.get(0).getItem().getContainerItem(this.furnaceItemStacks.get(0)));
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt()) {
                ++this.kneadProgress;
                if (this.kneadProgress == 200) {
                    this.kneadProgress = 0;
                    this.smeltItem();
                    flag = true;
                }
            } else {
                this.kneadProgress = 0;
            }
        }

        if (flag != isBurning()) {
            flag1 = true;
            BlockClayWorkTable.updateBlockState(this.world, this.pos);
        }

        if (flag1) {
            this.markDirty();
        }
    }

    public void releaseTicket() {
        if (this.ticket != null) {
            ForgeChunkManager.releaseTicket(this.ticket);
        }
    }

    public void transfer(TileEntity from, int[] fromSlots, EnumFacing direction, int maxTransfer) {
        EnumFacing fromSide = direction;
        EnumFacing toSide = direction.getOpposite();
        TileEntity te = this.world.getTileEntity(from.getPos().add(direction.getFrontOffsetX(), direction.getFrontOffsetY(), direction.getFrontOffsetZ()));
        if (te instanceof IInventory) {
            IInventory to = (IInventory)te;
            int[] toSlots;
            if (to instanceof ISidedInventory) {
                toSlots = ((ISidedInventory)to).getSlotsForFace(toSide);
            } else {
                toSlots = new int[to.getSizeInventory()];

                for(int i = 0; i < to.getSizeInventory(); i++) {
                    toSlots[i] = i;
                }
            }

            this.transfer((IInventory)from, to, fromSlots, toSlots, fromSide, toSide, maxTransfer);
        }
    }

    public void transfer(IInventory from, IInventory to, int[] fromSlots, int[] toSlots, EnumFacing fromSide, EnumFacing toSide, int maxTransfer) {
        int oldTransfer = maxTransfer;
        ISidedInventory fromSided = from instanceof ISidedInventory ? (ISidedInventory)from : null;
        ISidedInventory toSided = to instanceof ISidedInventory ? (ISidedInventory)to : null;

        for (int fromSlot : fromSlots) {
            ItemStack fromItem = from.getStackInSlot(fromSlot);
            if (fromItem != ItemStack.EMPTY && fromItem.getCount() > 0 && (fromSided == null || fromSided.canExtractItem(fromSlot, fromItem, fromSide))) {
                int var18;
                int toSlot;
                ItemStack toItem;
                if (fromItem.isStackable()) {
                    for (var18 = 0; var18 < toSlots.length; ++var18) {
                        toSlot = toSlots[var18];
                        toItem = to.getStackInSlot(toSlot);
                        if (toItem != ItemStack.EMPTY && toItem.getCount() > 0 && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide)) && fromItem.isItemEqual(toItem) && ItemStack.areItemStackTagsEqual(toItem, fromItem)) {
                            int maxSize = Math.min(toItem.getMaxStackSize(), to.getInventoryStackLimit());
                            int maxMove = Math.min(maxSize - toItem.getCount(), Math.min(maxTransfer, fromItem.getCount()));
                            toItem.setCount(toItem.getCount() + maxMove);
                            maxTransfer -= maxMove;
                            fromItem.setCount(fromItem.getCount() - maxMove);
                            if (fromItem.getCount() == 0) {
                                from.setInventorySlotContents(fromSlot, ItemStack.EMPTY);
                            }

                            if (maxTransfer == 0) {
                                return;
                            }

                            if (fromItem.getCount() == 0) {
                                break;
                            }
                        }
                    }
                }

                if (fromItem.getCount() > 0) {
                    for (var18 = 0; var18 < toSlots.length; ++var18) {
                        toSlot = toSlots[var18];
                        toItem = to.getStackInSlot(toSlot);
                        if (toItem == ItemStack.EMPTY && to.isItemValidForSlot(toSlot, fromItem) && (toSided == null || toSided.canInsertItem(toSlot, fromItem, toSide))) {
                            toItem = fromItem.copy();
                            toItem.setCount(Math.min(maxTransfer, fromItem.getCount()));
                            to.setInventorySlotContents(toSlot, toItem);
                            maxTransfer -= toItem.getCount();
                            fromItem.setCount(fromItem.getCount() + toItem.getCount());
                            if (fromItem.getCount() == 0) {
                                from.setInventorySlotContents(fromSlot, ItemStack.EMPTY);
                            }

                            if (maxTransfer == 0) {
                                return;
                            }

                            if (fromItem.getCount() == 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (oldTransfer != maxTransfer) {
            to.markDirty();
            from.markDirty();
        }
    }

    private boolean canSmelt() {
        if (this.furnaceItemStacks.get(0) == ItemStack.EMPTY) {
            return false;
        }

        ItemStack result = ClayWorkTableRecipes.smelting().getSmeltingResult(this.furnaceItemStacks.get(0));
        if (result == null) {
            return false;
        } else if (this.furnaceItemStacks.get(2) == ItemStack.EMPTY) {
            return true;
        } else if (!this.furnaceItemStacks.get(2).isItemEqual(result)) {
            return false;
        } else {
            int results = this.furnaceItemStacks.get(2).getCount() + result.getCount();
            return results <= this.getInventoryStackLimit() && results <= this.furnaceItemStacks.get(2).getMaxStackSize();
        }
    }

    private boolean canKnead(ItemStack material, int method) {
        return canKnead(material, method, ItemStack.EMPTY);
    }

    private boolean canKnead(ItemStack material, int method, ItemStack tool) {
        if (material == ItemStack.EMPTY) {
            return false;
        }

        ClayWorkTableRecipes.RecipeElement recipe = ClayWorkTableRecipes.smelting().getKneadingResultMap(material, method, tool);
        ItemStack itemstack = ClayWorkTableRecipes.smelting().getKneadingResult(recipe);
        ItemStack itemstack1 = ClayWorkTableRecipes.smelting().getKneadingResult1(recipe);
        if (itemstack == ItemStack.EMPTY) {
            return false;
        } else if (this.furnaceItemStacks.get(2) == ItemStack.EMPTY) {
            ClayiumCore.logger.info("Output space unfilled");
            return true;
        } else if (!this.furnaceItemStacks.get(2).isItemEqual(itemstack)) {
            return false;
        } else {
            int result2;
            if (itemstack1 != ItemStack.EMPTY && this.furnaceItemStacks.get(3) != ItemStack.EMPTY) {
                if (!this.furnaceItemStacks.get(3).isItemEqual(itemstack1)) {
                    return false;
                }

                result2 = this.furnaceItemStacks.get(3).getCount() + itemstack1.getCount();
                if (result2 > this.getInventoryStackLimit() || result2 > this.furnaceItemStacks.get(3).getMaxStackSize()) {
                    return false;
                }
            }

            result2 = this.furnaceItemStacks.get(2).getCount() + itemstack.getCount();
            return result2 <= this.getInventoryStackLimit() && result2 <= this.furnaceItemStacks.get(2).getMaxStackSize();
        }
    }

    public int canPushButton(int buttonId) {
//        switch (buttonId) {
//            case 3:
//                if (this.furnaceItemStacks.get(1) == ItemStack.EMPTY
//                        || this.furnaceItemStacks.get(1).getItem() != ClayiumItems.clayRollingPin)
//                    return 0;
//                break;
//            case 4: case 6:
//                if (this.furnaceItemStacks.get(1) == ItemStack.EMPTY
//                        || this.furnaceItemStacks.get(1).getItem() != ClayiumItems.claySlicer
//                        && this.furnaceItemStacks.get(1).getItem() != ClayiumItems.claySpatula)
//                    return 0;
//                break;
//            case 5:
//                if (this.furnaceItemStacks.get(1) == ItemStack.EMPTY
//                        || this.furnaceItemStacks.get(1).getItem() != ClayiumItems.claySpatula)
//                    return 0;
//                break;
//        }

        if (this.cookingMethod != -1) {
            if (this.canKnead(this.furnaceItemStacks.get(3), buttonId, this.furnaceItemStacks.get(1)) && this.cookingMethod == buttonId) {
                return 1;
            }
        }

        if (this.canKnead(this.furnaceItemStacks.get(0), buttonId, this.furnaceItemStacks.get(1))) {
            return this.cookingMethod == -1 ? 1 : 2;
        }

        return 0;
    }

    public void pushButton(int buttonId) {
        int canPushButton = this.canPushButton(buttonId);
        if (canPushButton != 0
                && buttonId >= 3
                && this.furnaceItemStacks.get(1) != ItemStack.EMPTY
                && this.furnaceItemStacks.get(1).getItem().hasContainerItem(this.furnaceItemStacks.get(1))) {
            this.furnaceItemStacks.set(1, this.furnaceItemStacks.get(1).getItem().getContainerItem(this.furnaceItemStacks.get(1)));
        }

        if (canPushButton == 1) {
            if (this.cookingMethod == -1) {
                ClayWorkTableRecipes.RecipeElement recipe = ClayWorkTableRecipes.smelting().getKneadingResultMap(this.furnaceItemStacks.get(0), buttonId, this.furnaceItemStacks.get(1));
                this.timeToKnead = ClayWorkTableRecipes.smelting().getKneadingTime(recipe);
                this.cookingMethod = buttonId;
                this.furnaceItemStacks.set(2, this.furnaceItemStacks.get(0).splitStack(ClayWorkTableRecipes.smelting().getConsumedStackSize(recipe)));
                if (this.furnaceItemStacks.get(0).getCount() <= 0) {
                    this.furnaceItemStacks.set(0, ItemStack.EMPTY);
                }
            }

            ++this.kneadProgress;
            if (this.kneadProgress >= this.timeToKnead) {
                ClayWorkTableRecipes.RecipeElement recipe = ClayWorkTableRecipes.smelting().getKneadingResultMap(this.furnaceItemStacks.get(3), buttonId, this.furnaceItemStacks.get(1));
                int consumedStackSize = ClayWorkTableRecipes.smelting().getConsumedStackSize(recipe);
                ItemStack itemstack = ClayWorkTableRecipes.smelting().getKneadingResult(recipe);
                ItemStack itemstack1 = ClayWorkTableRecipes.smelting().getKneadingResult1(recipe);
                this.kneadProgress = 0;
                this.cookingMethod = -1;
                ItemStack var10000;

                if (this.furnaceItemStacks.get(2) == ItemStack.EMPTY) {
                    this.furnaceItemStacks.set(2, itemstack.copy());
                } else if (this.furnaceItemStacks.get(2).getItem() == itemstack.getItem()) {
                    var10000 = this.furnaceItemStacks.get(2);
                    var10000.setCount(var10000.getCount() + itemstack.getCount());
                }

                if (itemstack1 != null) {
                    if (this.furnaceItemStacks.get(3) == ItemStack.EMPTY) {
                        this.furnaceItemStacks.set(3, itemstack1.copy());
                    } else if (this.furnaceItemStacks.get(3).getItem() == itemstack1.getItem()) {
                        var10000 = this.furnaceItemStacks.get(3);
                        var10000.setCount(var10000.getCount() + itemstack1.getCount());
                    }
                }

                var10000 = this.furnaceItemStacks.get(3);
                var10000.setCount(var10000.getCount() - consumedStackSize);
                if (var10000.getCount() <= 0) {
                    this.furnaceItemStacks.set(3, ItemStack.EMPTY);
                }
            }
        }

        if (canPushButton == 2) {
            this.kneadProgress = 0;
            this.cookingMethod = -1;
            this.furnaceItemStacks.set(3, ItemStack.EMPTY);
        }

        BlockClayWorkTable.updateBlockState(this.world, this.pos);
        this.markDirty();
        this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = ClayWorkTableRecipes.smelting().getSmeltingResult(this.furnaceItemStacks.get(0));
            if (this.furnaceItemStacks.get(2) == ItemStack.EMPTY) {
                this.furnaceItemStacks.set(2, itemstack.copy());
            } else if (this.furnaceItemStacks.get(2).getItem() == itemstack.getItem()) {
                ItemStack var10000 = this.furnaceItemStacks.get(2);
                var10000.setCount(var10000.getCount() + itemstack.getCount());
            }

            this.furnaceItemStacks.get(0).setCount(this.furnaceItemStacks.get(0).getCount() - 1);
        }
    }

    public static int getItemBurnTime(ItemStack itemstack) {
        if (itemstack == ItemStack.EMPTY) {
            return 0;
        }

        Item item = itemstack.getItem();
        if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {
            Block block = Block.getBlockFromItem(item);
            if (block.getDefaultState().getMaterial() == Material.ROCK) {
                return 300;
            }
        }

        return item == ClayiumItems.largeClayBall ? 1600 : ForgeEventFactory.getItemBurnTime(itemstack);
    }

    public static boolean isItemTool(ItemStack itemstack) {
        return itemstack.getItem() == ClayiumItems.clayRollingPin;
    }

    public static boolean isItemFuel(ItemStack itemstack) {
        return getItemBurnTime(itemstack) > 0;
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64.0D;
    }

    public boolean isItemValidForSlot(int par1, ItemStack itemstack) {
        return par1 < 2 && (par1 != 1 || isItemTool(itemstack));
    }

    public int[] getSlotsForFace(EnumFacing par1) {
        return par1 == EnumFacing.DOWN ? slotsBottom : (par1 == EnumFacing.UP ? slotsTop : slotsSides);
    }

    @Override
    public boolean canInsertItem(int par1, ItemStack itemstack, EnumFacing par3) {
        return this.isItemValidForSlot(par1, itemstack);
    }

    @Override
    public boolean canExtractItem(int par1, ItemStack itemstack, EnumFacing par3) {
        return par3 != EnumFacing.DOWN || par1 != 1 || itemstack.getItem() == Items.BUCKET;
    }

    public void resetRecipeAndSlots() {
        this.resetRecipeAndSlots(this.merchant, this.currentRecipeIndex, this.furnaceItemStacks.get(0), this.furnaceItemStacks.get(1));
    }

    public void resetRecipeAndSlots(IMerchant merchant, int currentRecipeIndex, ItemStack itemstack, ItemStack itemstack1) {
        if (merchant != null && this.furnaceItemStacks.get(this.toSellSlotIndex) == ItemStack.EMPTY) {
            this.currentRecipe = null;
            if (itemstack == null) {
                itemstack = itemstack1;
                itemstack1 = null;
            }

            if (itemstack == null) {
                this.setInventorySlotContents(this.toSellSlotIndex, ItemStack.EMPTY);
            } else {
                MerchantRecipeList merchantrecipelist = merchant.getRecipes((EntityPlayer)null);
                if (merchantrecipelist != null) {
                    MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, currentRecipeIndex);
                    if (merchantrecipe == null) {}

                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                        this.currentRecipe = merchantrecipe;
                        this.setInventorySlotContents(this.toSellSlotIndex, merchantrecipe.getItemToSell().copy());
                        this.onPickupFromMerchantSlot(merchantrecipe);
                    } else if (itemstack1 != ItemStack.EMPTY) {
                        merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, currentRecipeIndex);
                        if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                            this.currentRecipe = merchantrecipe;
                            this.setInventorySlotContents(this.toSellSlotIndex, merchantrecipe.getItemToSell().copy());
                            this.onPickupFromMerchantSlot(merchantrecipe);
                        }
                    }
                }
            }

            merchant.verifySellingItem(this.furnaceItemStacks.get(this.toSellSlotIndex));
        }
    }

    public void setCurrentRecipeIndex(int p_70471_1_) {
        this.currentRecipeIndex = p_70471_1_;
        this.resetRecipeAndSlots();
    }

    public boolean inventoryResetNeededOnSlotChange(int par1) {
        return par1 == 0 || par1 == 1;
    }

    public void onPickupFromMerchantSlot(MerchantRecipe currentRecipe) {
        if (this.merchant == null) {
            return;
        }

        if (currentRecipe != null) {
            ItemStack itemstack1 = this.furnaceItemStacks.get(0);
            ItemStack itemstack2 = this.furnaceItemStacks.get(1);
            if (this.doTrade(currentRecipe, itemstack1, itemstack2) || this.doTrade(currentRecipe, itemstack2, itemstack1)) {
                this.merchant.useRecipe(currentRecipe);
                if (itemstack1 != ItemStack.EMPTY && itemstack1.getCount() <= 0) {
                    itemstack1 = null;
                }

                if (itemstack2 != ItemStack.EMPTY && itemstack2.getCount() <= 0) {
                    itemstack2 = null;
                }

                this.setInventorySlotContents(0, itemstack1);
                this.setInventorySlotContents(1, itemstack2);
            }
        }

        this.markDirty();
        this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
    }

    private boolean doTrade(MerchantRecipe recipe, ItemStack itemstack, ItemStack itemstack1) {
        ItemStack itemstack2 = recipe.getItemToBuy();
        ItemStack itemstack3 = recipe.getSecondItemToBuy();
        if (itemstack != ItemStack.EMPTY && itemstack.getItem() == itemstack2.getItem()) {
            if (itemstack3 != ItemStack.EMPTY && itemstack1 != ItemStack.EMPTY && itemstack3.getItem() == itemstack1.getItem()) {
                itemstack.setCount(itemstack.getCount() - itemstack2.getCount());
                itemstack1.setCount(itemstack.getCount() - itemstack3.getCount());
                return true;
            }

            if (itemstack3 == ItemStack.EMPTY && itemstack1 == ItemStack.EMPTY) {
                itemstack.setCount(itemstack.getCount() - itemstack2.getCount());
                return true;
            }
        }

        return false;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerClayWorkTable(this.getWorld(), this.getPos(), playerIn);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.furnaceItemStacks;
    }
}
