package mods.clayium.block.tile;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.ClayWorkTable;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CItems;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.crafting.ClayWorkTableRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.ForgeDirection;

public class TileClayWorkTable
        extends TileEntity
        implements ISidedInventory {
    private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};

    private ItemStack[] furnaceItemStacks = new ItemStack[5];

    public int furnaceBurnTime;

    public int furnaceCookTime;
    private String furnaceName;
    public int furnaceTimeToCook;
    public int furnaceCookingMethod = 0;

    private ForgeChunkManager.Ticket ticket;

    public IMerchant merchant;

    public MerchantRecipe currentRecipe;
    public int currentRecipeIndex;
    private int toSellSlotIndex;

    public void furnaceName(String string) {
        this.furnaceName = string;
    }


    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }


    public ItemStack getStackInSlot(int slot) {
        return this.furnaceItemStacks[slot];
    }


    public ItemStack decrStackSize(int par1, int par2) {
        if (this.furnaceItemStacks[par1] != null) {

            if ((this.furnaceItemStacks[par1]).stackSize <= par2) {
                ItemStack itemStack = this.furnaceItemStacks[par1];
                this.furnaceItemStacks[par1] = null;
                if (inventoryResetNeededOnSlotChange(par1))
                    resetRecipeAndSlots();
                return itemStack;
            }

            ItemStack itemstack = this.furnaceItemStacks[par1].splitStack(par2);
            if ((this.furnaceItemStacks[par1]).stackSize == 0) {
                this.furnaceItemStacks[par1] = null;
            }
            if (inventoryResetNeededOnSlotChange(par1))
                resetRecipeAndSlots();
            return itemstack;
        }

        return null;
    }


    public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.furnaceItemStacks[par1] != null) {
            ItemStack itemstack = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            return itemstack;
        }
        return null;
    }


    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        this.furnaceItemStacks[slot] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
        if (inventoryResetNeededOnSlotChange(slot)) {
            resetRecipeAndSlots();
        }
    }


    public String getInventoryName() {
        return hasCustomInventoryName() ? this.furnaceName : getBlockType().getLocalizedName();
    }


    public boolean hasCustomInventoryName() {
        return (this.furnaceName != null && this.furnaceName.length() > 0);
    }


    public int getInventoryStackLimit() {
        return 64;
    }


    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);


        NBTTagList tagList = tagCompound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
            byte byte0 = tagCompound1.getByte("Slot");
            if (byte0 >= 0 && byte0 < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[byte0] = ItemStack.loadItemStackFromNBT(tagCompound1);
            }
        }

        this.furnaceCookTime = tagCompound.getShort("CookTime");
        this.furnaceBurnTime = tagCompound.getShort("BurnTime");
        this.furnaceTimeToCook = tagCompound.getShort("TimeToCook");
        this.furnaceCookingMethod = tagCompound.getShort("CookingMethod");

        if (tagCompound.hasKey("CustomName", 8)) {
            this.furnaceName = tagCompound.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setShort("BurnTime", (short) this.furnaceBurnTime);
        tagCompound.setShort("CookTime", (short) this.furnaceCookTime);
        tagCompound.setShort("TimeToCook", (short) this.furnaceTimeToCook);
        tagCompound.setShort("CookingMethod", (short) this.furnaceCookingMethod);
        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < this.furnaceItemStacks.length; i++) {
            if (this.furnaceItemStacks[i] != null) {
                NBTTagCompound tagCompound1 = new NBTTagCompound();
                tagCompound1.setByte("Slot", (byte) i);
                this.furnaceItemStacks[i].writeToNBT(tagCompound1);
                tagList.appendTag((NBTBase) tagCompound1);
            }
        }

        tagCompound.setTag("Items", (NBTBase) tagList);

        if (hasCustomInventoryName()) {
            tagCompound.setString("CustomName", this.furnaceName);
        }
    }


    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return (Packet) new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }


    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int par1) {
        if (this.furnaceTimeToCook == 0 || this.furnaceCookingMethod == 0)
            return 0;
        return this.furnaceCookTime * par1 / this.furnaceTimeToCook;
    }


    public boolean isBurning() {
        return (this.furnaceBurnTime > 0);
    }


    public void updateEntity() {
        int maxTransfer = 1;
        int[] fromSlots = {2, 3};
        ISidedInventory from = this;
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            transfer(this, fromSlots, direction, maxTransfer);
        }

        resetRecipeAndSlots();

        if (!this.worldObj.isRemote) {
            if (this.ticket == null) {
                this.ticket = ForgeChunkManager.requestTicket(ClayiumCore.INSTANCE, this.worldObj, ForgeChunkManager.Type.NORMAL);
                ForgeChunkManager.forceChunk(this.ticket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
            }
        }
    }


    public void releaseTicket() {
        if (this.ticket != null) {
            ForgeChunkManager.releaseTicket(this.ticket);
        }
    }


    public void transfer(TileEntity from, int[] fromSlots, ForgeDirection direction, int maxTransfer) {
        int toSlots[], fromSide = direction.ordinal();
        int toSide = direction.getOpposite().ordinal();

        TileEntity te = UtilDirection.getTileEntity((IBlockAccess) from.getWorldObj(), from.xCoord, from.yCoord, from.zCoord, direction);
        if (!(te instanceof IInventory))
            return;
        IInventory to = (IInventory) te;


        if (!(to instanceof ISidedInventory)) {
            toSlots = new int[to.getSizeInventory()];
            for (int i = 0; i < to.getSizeInventory(); i++) {
                toSlots[i] = i;
            }
        } else {
            toSlots = ((ISidedInventory) to).getAccessibleSlotsFromSide(toSide);
        }
        transfer((IInventory) from, to, fromSlots, toSlots, fromSide, toSide, maxTransfer);
    }


    public void transfer(IInventory from, IInventory to, int[] fromSlots, int[] toSlots, int fromSide, int toSide, int maxTransfer) {
        int oldTransfer = maxTransfer;
        ISidedInventory fromSided = (from instanceof ISidedInventory) ? (ISidedInventory) from : null;
        ISidedInventory toSided = (to instanceof ISidedInventory) ? (ISidedInventory) to : null;
        try {
            for (int fromSlot : fromSlots) {

                ItemStack fromItem = from.getStackInSlot(fromSlot);

                if (fromItem != null && fromItem.stackSize > 0 && (fromSided == null || fromSided
                        .canExtractItem(fromSlot, fromItem, fromSide))) {

                    if (fromItem.isStackable()) {
                        for (int toSlot : toSlots) {

                            ItemStack toItem = to.getStackInSlot(toSlot);
                            if (toItem != null && toItem.stackSize > 0 && (toSided == null || toSided
                                    .canInsertItem(toSlot, fromItem, toSide)) && fromItem
                                    .isItemEqual(toItem) && ItemStack.areItemStackTagsEqual(toItem, fromItem)) {

                                int maxSize = Math.min(toItem.getMaxStackSize(), to.getInventoryStackLimit());
                                int maxMove = Math.min(maxSize - toItem.stackSize, Math.min(maxTransfer, fromItem.stackSize));
                                toItem.stackSize += maxMove;
                                maxTransfer -= maxMove;
                                fromItem.stackSize -= maxMove;
                                if (fromItem.stackSize == 0) {
                                    from.setInventorySlotContents(fromSlot, null);
                                }
                                if (maxTransfer == 0) {
                                    return;
                                }
                                if (fromItem.stackSize == 0) {
                                    break;
                                }
                            }
                        }
                    }
                    if (fromItem.stackSize > 0) {
                        for (int toSlot : toSlots) {

                            ItemStack toItem = to.getStackInSlot(toSlot);

                            if (toItem == null && to.isItemValidForSlot(toSlot, fromItem) && (toSided == null || toSided
                                    .canInsertItem(toSlot, fromItem, toSide))) {

                                toItem = fromItem.copy();
                                toItem.stackSize = Math.min(maxTransfer, fromItem.stackSize);
                                to.setInventorySlotContents(toSlot, toItem);
                                maxTransfer -= toItem.stackSize;
                                fromItem.stackSize -= toItem.stackSize;
                                if (fromItem.stackSize == 0) {
                                    from.setInventorySlotContents(fromSlot, null);
                                }
                                if (maxTransfer == 0) {
                                    return;
                                }
                                if (fromItem.stackSize == 0) {
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
        } finally {

            if (oldTransfer != maxTransfer) {

                to.markDirty();
                from.markDirty();
            }
        }
    }


    public void updateEntity_old() {
        boolean flag = (this.furnaceBurnTime > 0);
        boolean flag1 = false;

        if (this.furnaceBurnTime > 0) {
            this.furnaceBurnTime--;
        }
        if (!this.worldObj.isRemote) {

            if (this.furnaceBurnTime == 0 && canSmelt()) {

                this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

                if (this.furnaceBurnTime > 0) {
                    flag1 = true;
                    if (this.furnaceItemStacks[1] != null) {
                        (this.furnaceItemStacks[1]).stackSize--;

                        if ((this.furnaceItemStacks[1]).stackSize == 0) {

                            this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem().getContainerItem(this.furnaceItemStacks[1]);
                        }
                    }
                }
            }


            if (isBurning() && canSmelt()) {
                this.furnaceCookTime++;
                if (this.furnaceCookTime == 200) {
                    this.furnaceCookTime = 0;
                    smeltItem();
                    flag = true;
                }
            } else {
                this.furnaceCookTime = 0;
            }
        }
        if (flag != ((this.furnaceBurnTime > 0))) {

            flag1 = true;
            ClayWorkTable.updateBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }

        if (flag1) {
            markDirty();
        }
    }

    private boolean canSmelt() {
        if (this.furnaceItemStacks[0] == null) {
            return false;
        }
        ItemStack itemstack = ClayWorkTableRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
        if (itemstack == null) return false;
        if (this.furnaceItemStacks[2] == null) return true;
        if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;
        int result = (this.furnaceItemStacks[2]).stackSize + itemstack.stackSize;
        return (result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[2].getMaxStackSize());
    }


    private boolean canKnead(ItemStack material, int method) {
        if (material == null) {
            return false;
        }


        ItemStack itemstack = ClayWorkTableRecipes.smelting().getKneadingResult(material, method);
        ItemStack itemstack2 = ClayWorkTableRecipes.smelting().getKneadingResult2(material, method);
        if (itemstack == null) return false;

        if (this.furnaceItemStacks[2] == null) return true;
        if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;
        if (itemstack2 != null && this.furnaceItemStacks[3] != null) {
            if (!this.furnaceItemStacks[3].isItemEqual(itemstack2)) return false;
            int result2 = (this.furnaceItemStacks[3]).stackSize + itemstack2.stackSize;
            if (result2 > getInventoryStackLimit() || result2 > this.furnaceItemStacks[3].getMaxStackSize())
                return false;
        }
        int result = (this.furnaceItemStacks[2]).stackSize + itemstack.stackSize;
        return (result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[2].getMaxStackSize());
    }


    public int canPushButton(int buttonid) {
        if (buttonid == 3 && (this.furnaceItemStacks[1] == null || this.furnaceItemStacks[1].getItem() != CItems.itemClayRollingPin))
            return 0;
        if ((buttonid == 4 || buttonid == 6) && (this.furnaceItemStacks[1] == null || (this.furnaceItemStacks[1].getItem() != CItems.itemClaySlicer && this.furnaceItemStacks[1].getItem() != CItems.itemClaySpatula)))
            return 0;
        if (buttonid == 5 && (this.furnaceItemStacks[1] == null || this.furnaceItemStacks[1].getItem() != CItems.itemClaySpatula))
            return 0;


        if (this.furnaceCookingMethod != 0) {
            ItemStack itemStack = this.furnaceItemStacks[4];

            if (canKnead(itemStack, buttonid) &&
                    this.furnaceCookingMethod == buttonid) {
                return 1;
            }
        }

        ItemStack itemstack = this.furnaceItemStacks[0];

        if (canKnead(itemstack, buttonid)) {
            if (this.furnaceCookingMethod == 0) {
                return 1;
            }

            return 2;
        }

        return 0;
    }


    public void pushButton(int buttonid) {
        int canpushbutton = canPushButton(buttonid);
        if (canpushbutton != 0 && buttonid >= 3 &&
                this.furnaceItemStacks[1] != null && this.furnaceItemStacks[1].getItem().hasContainerItem(this.furnaceItemStacks[1])) {
            this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem().getContainerItem(this.furnaceItemStacks[1]);
        }

        if (canpushbutton == 1) {

            if (this.furnaceCookingMethod == 0) {

                this.furnaceTimeToCook = ClayWorkTableRecipes.smelting().getKneadingTime(this.furnaceItemStacks[0], buttonid);
                this.furnaceCookingMethod = buttonid;
                this.furnaceItemStacks[4] = this.furnaceItemStacks[0].splitStack(ClayWorkTableRecipes.smelting().getConsumedStackSize(this.furnaceItemStacks[0], buttonid));
                if ((this.furnaceItemStacks[0]).stackSize <= 0) this.furnaceItemStacks[0] = null;

            }
            this.furnaceCookTime++;
            if (this.furnaceCookTime >= this.furnaceTimeToCook) {

                int consumedStackSize = ClayWorkTableRecipes.smelting().getConsumedStackSize(this.furnaceItemStacks[4], buttonid);
                ItemStack itemstack = ClayWorkTableRecipes.smelting().getKneadingResult(this.furnaceItemStacks[4], buttonid);
                ItemStack itemstack2 = ClayWorkTableRecipes.smelting().getKneadingResult2(this.furnaceItemStacks[4], buttonid);

                this.furnaceCookTime = 0;
                this.furnaceCookingMethod = 0;
                if (this.furnaceItemStacks[2] == null) {

                    this.furnaceItemStacks[2] = itemstack.copy();
                } else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem()) {
                    (this.furnaceItemStacks[2]).stackSize += itemstack.stackSize;
                }
                if (itemstack2 != null) {
                    if (this.furnaceItemStacks[3] == null) {
                        this.furnaceItemStacks[3] = itemstack2.copy();
                    } else if (this.furnaceItemStacks[3].getItem() == itemstack2.getItem()) {
                        (this.furnaceItemStacks[3]).stackSize += itemstack2.stackSize;
                    }
                }
                if (((this.furnaceItemStacks[4]).stackSize -= consumedStackSize) <= 0) this.furnaceItemStacks[4] = null;
            }
        }
        if (canpushbutton == 2) {
            this.furnaceCookTime = 0;
            this.furnaceCookingMethod = 0;
            this.furnaceItemStacks[4] = null;
        }
        ClayWorkTable.updateBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);


        markDirty();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }


    public void smeltItem() {
        if (canSmelt()) {
            ItemStack itemstack = ClayWorkTableRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if (this.furnaceItemStacks[2] == null) {
                this.furnaceItemStacks[2] = itemstack.copy();
            } else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem()) {
                (this.furnaceItemStacks[2]).stackSize += itemstack.stackSize;
            }
            (this.furnaceItemStacks[0]).stackSize--;
        }
    }

    public static int getItemBurnTime(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        }
        Item item = itemstack.getItem();

        if (item instanceof net.minecraft.item.ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
            Block block = Block.getBlockFromItem(item);

            if (block.getMaterial() == Material.rock) {
                return 300;
            }
        }

        if (item == CItems.itemLargeClayBall) return 1600;
        return GameRegistry.getFuelValue(itemstack);
    }


    public static boolean isItemTool(ItemStack itemstack) {
        return (itemstack.getItem() == CItems.itemClayRollingPin);
    }

    public static boolean isItemFuel(ItemStack itemstack) {
        return (getItemBurnTime(itemstack) > 0);
    }


    public boolean isUseableByPlayer(EntityPlayer player) {
        return (UtilBuilder.safeGetTileEntity((IBlockAccess) this.worldObj, this.xCoord, this.yCoord, this.zCoord) != this) ? false : ((player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D));
    }


    public void openInventory() {}


    public void closeInventory() {}


    public boolean isItemValidForSlot(int par1, ItemStack itemstack) {
        return (par1 >= 2) ? false : ((par1 == 1) ? isItemTool(itemstack) : true);
    }


    public int[] getAccessibleSlotsFromSide(int par1) {
        return (par1 == 0) ? slotsBottom : ((par1 == 1) ? slotsTop : slotsSides);
    }


    public boolean canInsertItem(int par1, ItemStack itemstack, int par3) {
        return isItemValidForSlot(par1, itemstack);
    }


    public boolean canExtractItem(int par1, ItemStack itemstack, int par3) {
        return (par3 != 0 || par1 != 1 || itemstack.getItem() == Items.bucket);
    }


    public TileClayWorkTable() {
        this.currentRecipeIndex = 0;
        this.toSellSlotIndex = 2;
    }


    public void resetRecipeAndSlots() {
        resetRecipeAndSlots(this.merchant, this.currentRecipeIndex, this.furnaceItemStacks[0], this.furnaceItemStacks[1]);
    }


    public void resetRecipeAndSlots(IMerchant merchant, int currentRecipeIndex, ItemStack itemstack, ItemStack itemstack1) {
        if (merchant != null && this.furnaceItemStacks[this.toSellSlotIndex] == null) {

            this.currentRecipe = null;

            if (itemstack == null) {

                itemstack = itemstack1;
                itemstack1 = null;
            }

            if (itemstack == null) {

                setInventorySlotContents(this.toSellSlotIndex, (ItemStack) null);
            } else {

                MerchantRecipeList merchantrecipelist = merchant.getRecipes(null);

                if (merchantrecipelist != null) {


                    MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, currentRecipeIndex);
                    if (merchantrecipe == null) ;


                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {

                        this.currentRecipe = merchantrecipe;
                        setInventorySlotContents(this.toSellSlotIndex, merchantrecipe.getItemToSell().copy());

                        onPickupFromMerchantSlot(merchantrecipe);
                    } else if (itemstack1 != null) {

                        merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, currentRecipeIndex);

                        if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {

                            this.currentRecipe = merchantrecipe;
                            setInventorySlotContents(this.toSellSlotIndex, merchantrecipe.getItemToSell().copy());

                            onPickupFromMerchantSlot(merchantrecipe);
                        }
                    }
                }
            }


            merchant.func_110297_a_(getStackInSlot(this.toSellSlotIndex));
        }
    }


    public void setCurrentRecipeIndex(int p_70471_1_) {
        this.currentRecipeIndex = p_70471_1_;
        resetRecipeAndSlots();
    }

    public boolean inventoryResetNeededOnSlotChange(int par1) {
        return (par1 == 0 || par1 == 1);
    }


    public void onPickupFromMerchantSlot(MerchantRecipe currentRecipe) {
        if (this.merchant != null) {

            MerchantRecipe merchantrecipe = currentRecipe;

            if (merchantrecipe != null) {

                ItemStack itemstack1 = this.furnaceItemStacks[0];
                ItemStack itemstack2 = this.furnaceItemStacks[1];

                if (func_75230_a(merchantrecipe, itemstack1, itemstack2) || func_75230_a(merchantrecipe, itemstack2, itemstack1)) {

                    this.merchant.useRecipe(merchantrecipe);

                    if (itemstack1 != null && itemstack1.stackSize <= 0) {
                        itemstack1 = null;
                    }

                    if (itemstack2 != null && itemstack2.stackSize <= 0) {
                        itemstack2 = null;
                    }

                    setInventorySlotContents(0, itemstack1);
                    setInventorySlotContents(1, itemstack2);
                }
            }
            markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }


    private boolean func_75230_a(MerchantRecipe p_75230_1_, ItemStack p_75230_2_, ItemStack p_75230_3_) {
        ItemStack itemstack2 = p_75230_1_.getItemToBuy();
        ItemStack itemstack3 = p_75230_1_.getSecondItemToBuy();

        if (p_75230_2_ != null && p_75230_2_.getItem() == itemstack2.getItem()) {

            if (itemstack3 != null && p_75230_3_ != null && itemstack3.getItem() == p_75230_3_.getItem()) {

                p_75230_2_.stackSize -= itemstack2.stackSize;
                p_75230_3_.stackSize -= itemstack3.stackSize;
                return true;
            }

            if (itemstack3 == null && p_75230_3_ == null) {

                p_75230_2_.stackSize -= itemstack2.stackSize;
                return true;
            }
        }

        return false;
    }
}
