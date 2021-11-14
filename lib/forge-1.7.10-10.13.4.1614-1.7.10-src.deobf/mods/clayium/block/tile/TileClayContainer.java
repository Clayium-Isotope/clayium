package mods.clayium.block.tile;

import java.util.ArrayList;
import java.util.List;

import mods.clayium.block.ClayContainer;
import mods.clayium.block.ClayMachines;
import mods.clayium.block.itemblock.IOverridableBlock;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CItems;
import mods.clayium.item.IClayEnergy;
import mods.clayium.item.filter.IItemFilter;
import mods.clayium.item.filter.ItemFilterTemp;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilDirection;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class TileClayContainer
        extends TileGeneric
        implements ISidedInventory, IInventoryFlexibleStackLimit, IOverridableBlock {
    public ItemStack[] containerItemStacks;
    protected String containerName;
    public ArrayList<int[]> listSlotsInsert = new ArrayList<int[]>();
    public ArrayList<int[]> listSlotsExtract = new ArrayList<int[]>();


    public int[] insertRoutes = new int[] {-1, -1, -1, -1, -1, -1};
    public int[] extractRoutes = new int[] {-1, -1, -1, -1, -1, -1};
    public ItemStack[] filters = new ItemStack[6];
    public int[] slotsDrop = new int[0];

    public boolean autoInsert = false;

    public boolean autoExtract = false;
    public int[] maxAutoInsert;
    public int maxAutoInsertDefault = 8;
    public int[] maxAutoExtract;
    public int maxAutoExtractDefault = 8;
    public int autoInsertInterval = 20;
    public int autoExtractInterval = 20;
    protected int autoInsertCount = 0;
    protected int autoExtractCount = 0;

    public long clayEnergy = 0L;
    public int clayEnergySlot = -1;
    public int clayEnergyStorageSize = 1;

    public boolean renderAsPipingMode = false;

    protected void initParams() {}

    public void containerName(String string) {
        this.containerName = string;
    }

    public int getSizeInventory() {
        return this.containerItemStacks.length;
    }

    public static boolean restrictionTestMode = false;
    int lastSlotGetStackInSlot;
    int enderIOFirstFreeSlot;
    boolean enderIODoInsertItemInv;

    public TileClayContainer() {
        this.lastSlotGetStackInSlot = -1;
        this.enderIOFirstFreeSlot = -1;
        this.enderIODoInsertItemInv = false;
        initParams();
    }

    public ItemStack getStackInSlot(int slot) {
        if (restrictionTestMode) {
            if (slot <= this.lastSlotGetStackInSlot) this.enderIOFirstFreeSlot = -1;
            this.lastSlotGetStackInSlot = slot;
        }

        if (this.containerItemStacks == null || this.containerItemStacks.length <= slot || slot < 0) return null;
        if (restrictionTestMode && this.containerItemStacks[slot] == null && this.enderIOFirstFreeSlot == -1)
            this.enderIOFirstFreeSlot = slot;

        return (this.containerItemStacks != null && this.containerItemStacks.length > slot && slot >= 0) ? this.containerItemStacks[slot] : null;
    }

    public ItemStack decrStackSize(int par1, int par2) {
        setSyncFlag();
        if (this.containerItemStacks[par1] != null) {
            if ((this.containerItemStacks[par1]).stackSize <= par2) {
                ItemStack itemStack = this.containerItemStacks[par1];
                this.containerItemStacks[par1] = null;
                return itemStack;
            }

            ItemStack itemstack = this.containerItemStacks[par1].splitStack(par2);
            if ((this.containerItemStacks[par1]).stackSize == 0) {
                this.containerItemStacks[par1] = null;
            }

            return itemstack;
        }

        return null;
    }

    public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.containerItemStacks[par1] != null) {
            ItemStack itemstack = this.containerItemStacks[par1];
            this.containerItemStacks[par1] = null;
            return itemstack;
        }

        return null;
    }

    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        setSyncFlag();
        this.containerItemStacks[slot] = itemstack;
        int stackLimit = restrictionTestMode ? getInventoryStackLimit(slot) : getInventoryStackLimit();
        if (itemstack != null && itemstack.stackSize > stackLimit) {
            itemstack.stackSize = stackLimit;
        }
    }

    public String getInventoryName() {
        return hasCustomInventoryName() ? this.containerName : getDefaultInventoryName();
    }

    public String getDefaultInventoryName() {
        return getBlockType().getLocalizedName();
    }

    public boolean hasCustomInventoryName() {
        return (this.containerName != null && this.containerName.length() > 0);
    }

    public int getInventoryStackLimit() {
        if (restrictionTestMode) {
            StackTraceElement stackCallingThis = Thread.currentThread().getStackTrace()[2];
            String methodName = stackCallingThis.getMethodName();
            this.enderIODoInsertItemInv = "doInsertItemInv".equals(methodName);
            if (this.enderIODoInsertItemInv) {
                if (stackCallingThis.getLineNumber() < 400)
                    return getInventoryStackLimit(this.lastSlotGetStackInSlot);
                if (this.enderIOFirstFreeSlot != -1) {
                    return getInventoryStackLimit(this.enderIOFirstFreeSlot);
                }
            }
        }

        return getDefaultInventoryStackLimit();
    }

    public int getDefaultInventoryStackLimit() {
        return 64;
    }

    public int getInventoryStackLimit(int slot) {
        return (slot == this.clayEnergySlot) ? this.clayEnergyStorageSize : getDefaultInventoryStackLimit();
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return UtilBuilder.safeGetTileEntity(this.worldObj, this.xCoord, this.yCoord, this.zCoord) == this;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return isItemValidForSlotDefault(slot, itemstack);
    }

    public boolean isItemValidForSlotDefault(int slot, ItemStack itemstack) {
        if (slot == this.clayEnergySlot) return isEnergyClayValid(slot, itemstack);
        return checkInsertionList(slot, itemstack);
    }

    public boolean checkInsertionList(int slot, ItemStack itemstack) {
        for (int[] slots : this.listSlotsInsert)
            for (int _slot : slots)
                if (_slot == slot) return true;
        return false;
    }

    public boolean isEnergyClayValid(int slot, ItemStack itemstack) {
        return (acceptEnergyClay() &&
                hasClayEnergy(itemstack) && (this.containerItemStacks[slot] == null || (this.containerItemStacks[slot]).stackSize < this.clayEnergyStorageSize));
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        return getAccessibleSlotsFromSideDefault(side);
    }

    public int[] getAccessibleSlotsFromSideDefault(int side) {
        if (!canGetFrontDirection()) return new int[0];

        int front = getFrontDirection();
        int[] flag = new int[this.containerItemStacks.length];
        int i;
        for (i = 0; i < this.listSlotsInsert.size(); i++) {
            if (this.insertRoutes[UtilDirection.direction2Side(front, side) - 6] == i) {
                for (int m = 0; m < this.listSlotsInsert.get(i).length; m++) {
                    flag[((int[]) this.listSlotsInsert.get(i))[m]] = 1;
                }
            }
        }

        for (i = 0; i < this.listSlotsExtract.size(); i++) {
            if (this.extractRoutes[UtilDirection.direction2Side(front, side) - 6] == i) {
                for (int m = 0; m < this.listSlotsExtract.get(i).length; m++) {
                    flag[this.listSlotsExtract.get(i)[m]] = 1;
                }
            }
        }

        int c = 0;
        for (int value : flag)
            if (value == 1) c++;

        int[] res = new int[c];
        c = 0;
        for (int k = 0; k < flag.length; k++) {
            if (flag[k] == 1) {
                res[c] = k;
                c++;
            }
        }

        return res;
    }

    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return canInsertItemDefault(slot, itemstack, side);
    }

    public boolean canInsertItemUnsafe(int slot, ItemStack itemstack, int route) {
        if (route < 0 || route >= this.listSlotsInsert.size()) return false;
        for (int _slot : this.listSlotsInsert.get(route)) {
            if (_slot == slot && isItemValidForSlot(slot, itemstack))
                return true;
        }

        return false;
    }

    public boolean canInsertItemDefault(int slot, ItemStack itemstack, int side) {
        if (!canGetFrontDirection()) return false;

        int front = getFrontDirection();
        int d = UtilDirection.direction2Side(front, side) - 6;

        ItemStack filter = this.filters[d];
        if (ItemFilterTemp.isFilter(filter) && !ItemFilterTemp.match(filter, itemstack)) return false;

        return canInsertItemUnsafe(slot, itemstack, this.insertRoutes[d]);
    }

    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        return canExtractItemDefault(slot, itemstack, side);
    }

    public boolean canExtractItemUnsafe(int slot, ItemStack itemstack, int route) {
        if (route < 0 || route >= this.listSlotsExtract.size()) return false;

        for (int _slot : this.listSlotsExtract.get(route))
            if (_slot == slot)
                return true;

        return false;
    }

    public boolean canExtractItemDefault(int slot, ItemStack itemstack, int side) {
        if (!canGetFrontDirection()) return false;

        int front = getFrontDirection();
        int d = UtilDirection.direction2Side(front, side) - 6;

        ItemStack filter = this.filters[d];
        if (ItemFilterTemp.isFilter(filter) && !ItemFilterTemp.match(filter, itemstack)) return false;

        return canExtractItemUnsafe(slot, itemstack, this.extractRoutes[d]);
    }

    public void updateEntity() {
        if (!this.removeFlag) {
            if (!this.worldObj.isRemote) {
                if (this.autoExtract) {
                    this.autoExtractCount++;
                    if (this.autoExtractCount >= this.autoExtractInterval) {
                        this.autoExtractCount = 0;
                        doAutoExtract();
                    }
                } else {
                    this.autoExtractCount = 0;
                }

                if (this.autoInsert) {
                    this.autoInsertCount++;
                    if (this.autoInsertCount >= this.autoInsertInterval) {
                        this.autoInsertCount = 0;
                        doAutoInsert();
                    }
                } else {
                    this.autoInsertCount = 0;
                }
            } else {
                Block block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);

                if (block instanceof ClayContainer && ((ClayContainer) block).renderAsPipe(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
                    boolean flag = ClayiumCore.proxy.renderAsPipingMode();

                    if (flag != this.renderAsPipingMode) {
                        this.weakReRenderFlag = true;
                    }
                    this.renderAsPipingMode = flag;
                }
            }
        }
        super.updateEntity();
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        readCoordFromNBT(tagCompound);

        if (tagCompound.hasKey("Items")) {
            this.containerItemStacks = new ItemStack[getSizeInventory()];
            UtilItemStack.tagList2Items(tagCompound.getTagList("Items", 10), this.containerItemStacks);
        }

        if (tagCompound.hasKey("CustomName")) {
            this.containerName = tagCompound.getString("CustomName");
        }

        if (tagCompound.hasKey("InsertRoutes")) {
            int[] temp = tagCompound.getIntArray("InsertRoutes");
            if (temp.length >= 6)
                this.insertRoutes = temp;
        }

        if (tagCompound.hasKey("ExtractRoutes")) {
            int[] temp = tagCompound.getIntArray("ExtractRoutes");
            if (temp.length >= 6) this.extractRoutes = temp;
        }

        if (tagCompound.hasKey("AutoInsert"))
            this.autoInsert = tagCompound.getBoolean("AutoInsert");

        if (tagCompound.hasKey("AutoInsertCount"))
            this.autoInsertCount = tagCompound.getShort("AutoInsertCount");

        if (tagCompound.hasKey("AutoExtract"))
            this.autoExtract = tagCompound.getBoolean("AutoExtract");

        if (tagCompound.hasKey("AutoExtractCount"))
            this.autoExtractCount = tagCompound.getShort("AutoExtractCount");

        if (tagCompound.hasKey("ClayEnergy"))
            this.clayEnergy = tagCompound.getLong("ClayEnergy");

        if (tagCompound.hasKey("Filters")) {
            this.filters = new ItemStack[6];
            UtilItemStack.tagList2Items(tagCompound.getTagList("Filters", 10), this.filters);
        }
    }

    public void readCoordFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        writeCoordToNBT(tagCompound);

        tagCompound.setTag("Items", UtilItemStack.items2TagList(this.containerItemStacks));

        if (hasCustomInventoryName()) {
            tagCompound.setString("CustomName", this.containerName);
        }

        tagCompound.setTag("InsertRoutes", new NBTTagIntArray(this.insertRoutes));
        tagCompound.setTag("ExtractRoutes", new NBTTagIntArray(this.extractRoutes));

        tagCompound.setBoolean("AutoInsert", this.autoInsert);
        tagCompound.setShort("AutoInsertCount", (short) this.autoInsertCount);

        tagCompound.setBoolean("AutoExtract", this.autoExtract);
        tagCompound.setShort("AutoExtractCount", (short) this.autoExtractCount);

        tagCompound.setLong("ClayEnergy", this.clayEnergy);

        tagCompound.setTag("Filters", UtilItemStack.items2TagList(this.filters));
    }

    public void writeCoordToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
    }

    public int[] getSlotsDrop() {
        return this.slotsDrop;
    }

    public void doAutoExtract() {
        if (!canGetFrontDirection()) return;

        for (int i = 0; i < 6; i++) {
            if (this.insertRoutes[i] >= this.listSlotsInsert.size())
                this.insertRoutes[i] = -1;
        }
        int front = getFrontDirection();

        for (int side = 0; side < 6; side++) {
            doAutoExtractFromSide(side, front);
        }
    }

    public void doAutoExtractFromSide(int side, int front) {
        int route = this.insertRoutes[side];
        if (route >= 0 && route < this.listSlotsInsert.size()) {
            UtilTransfer.extract(this, this.listSlotsInsert.get(route), UtilDirection.getOrientation(front), UtilDirection.getSide(side), (this.maxAutoExtract != null && route < this.maxAutoExtract.length && this.maxAutoExtract[route] >= 0) ? this.maxAutoExtract[route] : this.maxAutoExtractDefault, new Object[0]);
        }
    }

    public void doAutoInsert() {
        if (!canGetFrontDirection()) return;

        for (int i = 0; i < 6; i++) {
            if (this.extractRoutes[i] >= this.listSlotsExtract.size())
                this.extractRoutes[i] = -1;
        }

        int front = getFrontDirection();

        for (int side = 0; side < 6; side++) {
            doAutoInsertToSide(side, front);
        }
    }

    public void doAutoInsertToSide(int side, int front) {
        int route = this.extractRoutes[side];
        if (route >= 0 && route < this.listSlotsExtract.size()) {
            UtilTransfer.insert(this, this.listSlotsExtract.get(route), UtilDirection.getOrientation(front), UtilDirection.getSide(side), (this.maxAutoInsert != null && route < this.maxAutoInsert.length && this.maxAutoInsert[route] >= 0) ? this.maxAutoInsert[route] : this.maxAutoInsertDefault, new Object[0]);
        }
    }

    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        if (itemStack == null) return -1;
        if (itemStack.getItem() == CItems.itemClayRollingPin) return 0;
        if (itemStack.getItem() == CItems.itemClaySlicer) return 1;
        if (itemStack.getItem() == CItems.itemClaySpatula) return 2;
        if (itemStack.getItem() instanceof ItemFilterTemp) return 10;
        if (itemStack.getItem() == CItems.itemRawClayCraftingTools) return 11;
        if (itemStack.getItem() == CItems.itemClayWrench) return 20;
        if (itemStack.getItem() == CItems.itemSynchronizer) return 90;
        if (UtilItemStack.areTypeEqual(itemStack, CItems.itemClayPipingTools.get("IO")))
            return player.isSneaking() ? 1 : 0;
        if (UtilItemStack.areTypeEqual(itemStack, CItems.itemClayPipingTools.get("Piping")))
            return player.isSneaking() ? 20 : 2;
        if (UtilItemStack.areItemDamageEqual(itemStack, CItems.itemClayPipingTools.get("Memory")))
            return player.isSneaking() ? 30 : 31;

        return -1;
    }

    public void useItemFromSide(ItemStack itemStack, EntityPlayer player, int side, int mode) {
        ItemStack filter;
        IItemFilter itemfilter;
        Block block;
        int front;
        Block block1;
        int direction;
        Block block2;
        NBTTagCompound tag;

        switch (mode) {
            case 0:
                this.insertRoutes[side] = this.insertRoutes[side] + 1;
                if (this.listSlotsInsert.size() <= this.insertRoutes[side]) {
                    this.insertRoutes[side] = -1;
                    player.addChatMessage(new ChatComponentText("Disabled"));
                    break;
                }
                player.addChatMessage(new ChatComponentText("Set insert route " + String.valueOf(this.insertRoutes[side])));
                break;

            case 1:
                this.extractRoutes[side] = this.extractRoutes[side] + 1;
                if (this.listSlotsExtract.size() <= this.extractRoutes[side]) {
                    this.extractRoutes[side] = -1;
                    player.addChatMessage(new ChatComponentText("Disabled"));
                    break;
                }
                player.addChatMessage(new ChatComponentText("Set extract route " + String.valueOf(this.extractRoutes[side])));
                break;

            case 2:
                if (((ClayContainer) getWorldObj().getBlock(this.xCoord, this.yCoord, this.zCoord)).canChangeRenderType()) {
                    int meta = getWorldObj().getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
                    getWorldObj().setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, (meta < 6) ? (meta + 6) : (meta - 6), 3);
                }
                break;

            case 10:
                filter = player.getCurrentEquippedItem();
                itemfilter = (IItemFilter) filter.getItem();
                if (!itemfilter.isCopy(filter)) {
                    this.filters[side] = filter.copy();
                    player.addChatMessage(new ChatComponentText("Applied " + this.filters[side].getDisplayName()));

                    ItemStack appliedFilter = this.filters[side];
                    List<String> list = new ArrayList<String>();
                    ((IItemFilter) appliedFilter.getItem()).addFilterInformation(appliedFilter, player, list, true);
                    for (String s : list) player.addChatMessage(new ChatComponentText(" " + s));
                    break;
                }

                if (this.filters[side] != null) {
                    IItemFilter itemfilterinv = (IItemFilter) this.filters[side].getItem();
                    player.inventory.mainInventory[player.inventory.currentItem] = itemfilterinv.setCopyFlag(this.filters[side].copy());
                    player.addChatMessage(new ChatComponentText("Copied " + this.filters[side].getDisplayName()));

                    ItemStack appliedFilter = this.filters[side];
                    List<String> list = new ArrayList<String>();
                    ((IItemFilter) appliedFilter.getItem()).addFilterInformation(appliedFilter, player, list, true);
                    for (String s : list) player.addChatMessage(new ChatComponentText(" " + s));
                }
                break;

            case 11:
                if (this.filters[side] != null) {
                    player.addChatMessage(new ChatComponentText("Removed " + this.filters[side].getDisplayName()));
                    ItemStack appliedFilter = this.filters[side];
                    List<String> list = new ArrayList<String>();
                    ((ItemFilterTemp) appliedFilter.getItem()).addFilterInformation(appliedFilter, player, list, true);
                    for (String s : list) player.addChatMessage(new ChatComponentText(" " + s));
                    this.filters[side] = null;
                }
                break;

            case 20:
                if (this.worldObj == null) break;
                block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
                if (!(block instanceof ClayContainer) || ((ClayContainer) block).metaMode == 0) break;
                front = ((ClayContainer) block).getFront(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                direction = UtilDirection.side2Direction(front, side);
                if (((ClayContainer) block).metaMode == 1 && (direction == 0 || direction == 1)) break;
                if (direction != this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord)) {
                    this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, direction, 3);
                    break;
                }
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, UtilDirection.getOrientation(direction).getOpposite().ordinal(), 3);
                break;

            case 30:
                if (this.worldObj == null) break;
                block1 = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
                if (block1 instanceof ClayContainer) {
                    NBTTagCompound itemtag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
                    NBTTagCompound memorytag = new NBTTagCompound();
                    memorytag.setInteger("MetaMode", ((ClayContainer) block1).metaMode);
                    memorytag.setInteger("Metadata", this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
                    memorytag.setIntArray("InsertionRoutes", this.insertRoutes);
                    memorytag.setIntArray("ExtractionRoutes", this.extractRoutes);
                    itemtag.setTag("ClayContainerIOMemory", memorytag);
                    itemStack.setTagCompound(itemtag);
                    player.addChatMessage(new ChatComponentText("Saved settings to memory"));
                }
                break;

            case 31:
                if (this.worldObj == null) break;
                block2 = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
                if (block2 instanceof ClayContainer) {
                    NBTTagCompound itemtag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
                    if (itemtag.hasKey("ClayContainerIOMemory")) {
                        NBTTagCompound memorytag = itemtag.getCompoundTag("ClayContainerIOMemory");
                        int metaMode = memorytag.getInteger("MetaMode");
                        if (metaMode == ((ClayContainer) block2).metaMode) {
                            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, memorytag.getInteger("Metadata"), 3);
                            this.insertRoutes = memorytag.getIntArray("InsertionRoutes");
                            this.extractRoutes = memorytag.getIntArray("ExtractionRoutes");
                            player.addChatMessage(new ChatComponentText("Loaded settings from memory"));
                            break;
                        }
                        player.addChatMessage(new ChatComponentText("Invalid data"));
                    }
                }
                break;

            case 90:
                if (this.worldObj == null || !acceptInterfaceSync()) break;
                tag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
                tag.setInteger("CoordMemoryX", this.xCoord);
                tag.setInteger("CoordMemoryY", this.yCoord);
                tag.setInteger("CoordMemoryZ", this.zCoord);
                tag.setInteger("CoordMemoryDimID", this.worldObj.provider.dimensionId);
                itemStack.setTagCompound(tag);
                player.addChatMessage(new ChatComponentText("Loaded Coord (" + this.xCoord + "," + this.yCoord + "," + this.zCoord + ") in dim " + this.worldObj.provider.dimensionId));
                break;
        }
    }

    public boolean consumeClayEnergy(long energy) {
        if (energy > this.clayEnergy) {
            if (produceClayEnergy()) return consumeClayEnergy(energy);
            return false;
        }

        this.clayEnergy -= energy;
        return true;
    }

    public boolean produceClayEnergy() {
        if (this.clayEnergySlot < 0 || this.clayEnergySlot >= this.containerItemStacks.length) return false;
        ItemStack itemstack = this.containerItemStacks[this.clayEnergySlot];
        if (!hasClayEnergy(itemstack)) return false;
        this.clayEnergy += getClayEnergy(itemstack);
        itemstack.stackSize--;
        if (itemstack.stackSize <= 0) this.containerItemStacks[this.clayEnergySlot] = null;
        return true;
    }

    public boolean acceptEnergyClay() {
        return true;
    }

    public boolean acceptInterfaceSync() {
        return true;
    }

    static boolean hasClayEnergy(ItemStack itemstack) {
        return (getClayEnergy(itemstack) > 0L);
    }

    static long getClayEnergy(ItemStack itemstack) {
        if (itemstack == null) return 0L;
        if (itemstack.getItem() instanceof IClayEnergy) {
            return ((IClayEnergy) itemstack.getItem()).getClayEnergy(itemstack);
        }
        return 0L;
    }

    public void overrideTo(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, Block overriddenBlock, int overriddenMeta, Class overriddenTileEntityClass, NBTTagCompound overriddenTileEntityTag) {
        NBTTagCompound tagCompound = overriddenTileEntityTag;

        if (tagCompound != null) {
            this.insertRoutes = tagCompound.getIntArray("InsertRoutes");
            this.extractRoutes = tagCompound.getIntArray("ExtractRoutes");

            this.clayEnergy = tagCompound.getLong("ClayEnergy");

            NBTTagList tagList = tagCompound.getTagList("Filters", 10);
            this.filters = new ItemStack[6];
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
                short byte0 = tagCompound1.getShort("Slot");
                if (byte0 >= 0 && byte0 < this.filters.length) {
                    this.filters[byte0] = ItemStack.loadItemStackFromNBT(tagCompound1);
                }
            }
        }

        world.setBlockMetadataWithNotify(x, y, z, overriddenMeta, 3);
    }


    public boolean canOverride(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock)) return false;
        Block block = ((ItemBlock) itemStack.getItem()).field_150939_a;
        if (!(block instanceof ClayContainer)) return false;

        if (getClass().equals(((ClayContainer) block).tileEntityClass)) {
            String recipeid1 = null;
            String recipeid2 = null;
            if (this instanceof TileClayMachines) recipeid1 = ((TileClayMachines) this).getRecipeId();

            if (block instanceof ClayMachines) recipeid2 = ((ClayMachines) block).getRecipeId();

            if (recipeid1 == null) recipeid1 = "";
            if (recipeid2 == null) recipeid2 = "";

            return recipeid1.equals(recipeid2);
        }

        return false;
    }

    public void onOverridden(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {}

    public abstract void openInventory();

    public abstract void closeInventory();
}
