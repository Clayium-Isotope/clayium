package mods.clayium.block.tile;

import java.util.ArrayList;

import mods.clayium.item.CItems;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class TileClayContainerInterface extends TileClayContainerTiered implements ISynchronizedInterface {
    protected int coreBlockX = 0;
    protected int coreBlockY = 0;
    protected int coreBlockZ = 0;
    protected boolean synced = false;
    TileClayContainer core = null;

    public boolean setCoreBlockCoord(int x, int y, int z) {
        setSyncFlag();
        this.coreBlockX = x;
        this.coreBlockY = y;
        this.coreBlockZ = z;
        return syncCoreBlock();
    }

    public int getCoreBlockXCoord() {
        return this.coreBlockX;
    }

    public int getCoreBlockYCoord() {
        return this.coreBlockY;
    }

    public int getCoreBlockZCoord() {
        return this.coreBlockZ;
    }

    protected boolean syncCoreBlock() {
        return syncCoreBlock(this.coreBlockX, this.coreBlockY, this.coreBlockZ);
    }

    public boolean isSynced() {
        return this.synced;
    }

    protected boolean syncCoreBlock(int x, int y, int z) {
        int coreX = this.xCoord + x;
        int coreY = this.yCoord + y;
        int coreZ = this.zCoord + z;
        TileEntity te = UtilBuilder.safeGetTileEntity(getCoreWorld(), coreX, coreY, coreZ);
        if (getCoreWorld() != null && te instanceof TileClayContainer
                && ((TileClayContainer) te).acceptInterfaceSync() && (x != 0 || y != 0 || z != 0)) {

            this.core = (TileClayContainer) te;
            this.containerItemStacks = this.core.containerItemStacks;
            this.listSlotsInsert = this.core.listSlotsInsert;
            this.listSlotsExtract = this.core.listSlotsExtract;

            for (int i = 0; i < 6; i++) {
                if (this.insertRoutes[i] >= this.listSlotsInsert.size()) this.insertRoutes[i] = -1;
                if (this.extractRoutes[i] >= this.listSlotsExtract.size()) this.extractRoutes[i] = -1;
            }

            this.slotsDrop = new int[0];
            this.autoInsert = this.core.autoInsert;
            this.autoExtract = this.core.autoExtract;
            this.maxAutoInsert = this.core.maxAutoInsert;
            this.maxAutoInsertDefault = this.core.maxAutoInsertDefault;
            this.maxAutoExtract = this.core.maxAutoExtract;
            this.maxAutoExtractDefault = this.core.maxAutoExtractDefault;
            this.autoInsertInterval = this.core.autoInsertInterval;
            this.autoExtractInterval = this.core.autoExtractInterval;
            this.clayEnergySlot = this.core.clayEnergySlot;
            this.synced = true;
            return true;
        }

        this.containerItemStacks = new ItemStack[] {null};
        this.listSlotsInsert = new ArrayList<int[]>();
        this.listSlotsInsert.add(new int[] {0});
        this.listSlotsExtract = new ArrayList<int[]>();
        this.listSlotsExtract.add(new int[] {0});
        this.slotsDrop = new int[0];
        this.autoInsert = false;
        this.autoExtract = false;
        this.maxAutoInsert = null;
        this.maxAutoInsertDefault = 1;
        this.maxAutoExtract = null;
        this.maxAutoExtractDefault = 1;
        this.autoInsertInterval = 1;
        this.autoExtractInterval = 1;
        this.clayEnergySlot = 0;

        this.synced = false;
        this.core = null;

        return false;
    }

    public int getSizeInventory() {
        syncCoreBlock();
        return (this.core != null) ? this.core.getSizeInventory() : super.getSizeInventory();
    }

    public ItemStack getStackInSlot(int slot) {
        syncCoreBlock();
        return (this.core != null) ? this.core.getStackInSlot(slot) : super.getStackInSlot(slot);
    }

    public ItemStack decrStackSize(int par1, int par2) {
        syncCoreBlock();
        return (this.core != null) ? this.core.decrStackSize(par1, par2) : super.decrStackSize(par1, par2);
    }

    public ItemStack getStackInSlotOnClosing(int par1) {
        syncCoreBlock();
        return (this.core != null) ? this.core.getStackInSlotOnClosing(par1) : super.getStackInSlotOnClosing(par1);
    }

    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        if (syncCoreBlock()) {
            this.core.setInventorySlotContents(slot, itemstack);
        }
    }

    public int getInventoryStackLimit() {
        syncCoreBlock();
        return (this.core != null) ? this.core.getInventoryStackLimit() : super.getInventoryStackLimit();
    }

    public int getInventoryStackLimit(int slot) {
        syncCoreBlock();
        return (this.core != null) ? this.core.getInventoryStackLimit(slot) : super.getInventoryStackLimit(slot);
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return (syncCoreBlock() && super.isUseableByPlayer(player));
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return (syncCoreBlock() && this.core.isItemValidForSlot(slot, itemstack));
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        syncCoreBlock();
        return super.getAccessibleSlotsFromSide(side);
    }

    public boolean canInsertItemUnsafe(int slot, ItemStack itemstack, int route) {
        return (syncCoreBlock() && super.canInsertItemUnsafe(slot, itemstack, route));
    }

    public boolean canExtractItemUnsafe(int slot, ItemStack itemstack, int route) {
        return (syncCoreBlock() && super.canExtractItemUnsafe(slot, itemstack, route));
    }

    public void updateEntity() {
        syncCoreBlock();
        super.updateEntity();
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        syncCoreBlock();
        readCoordFromNBT(tagCompound);

        this.insertRoutes = tagCompound.getIntArray("InsertRoutes");
        this.extractRoutes = tagCompound.getIntArray("ExtractRoutes");

        if (this.insertRoutes == null || this.insertRoutes.length < 6) {
            this.insertRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        }

        if (this.extractRoutes == null || this.extractRoutes.length < 6) {
            this.extractRoutes = new int[] {-1, -1, -1, -1, -1, -1};
        }

        this.autoInsertCount = tagCompound.getShort("AutoInsertCount");
        this.autoExtractCount = tagCompound.getShort("AutoExtractCount");

        this.filters = new ItemStack[6];
        UtilItemStack.tagList2Items(tagCompound.getTagList("Filters", 10), this.filters);

        if (tagCompound.hasKey("CustomName", 8)) {
            this.containerName = tagCompound.getString("CustomName");
        }

        this.coreBlockX = tagCompound.getInteger("CoreBlockX");
        this.coreBlockY = tagCompound.getInteger("CoreBlockY");
        this.coreBlockZ = tagCompound.getInteger("CoreBlockZ");

        this.acceptCoordChanger = tagCompound.getBoolean("AcceptCoordChanger");
        this.interDimensional = tagCompound.getBoolean("InterDimensional");
        this.dimentionId = tagCompound.getInteger("CoreBlockDimentionId");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        syncCoreBlock();
        writeCoordToNBT(tagCompound);

        tagCompound.setShort("AutoInsertCount", (short) this.autoInsertCount);
        tagCompound.setShort("AutoExtractCount", (short) this.autoExtractCount);

        tagCompound.setTag("InsertRoutes", new NBTTagIntArray(this.insertRoutes));
        tagCompound.setTag("ExtractRoutes", new NBTTagIntArray(this.extractRoutes));

        tagCompound.setTag("Filters", UtilItemStack.items2TagList(this.filters));

        if (hasCustomInventoryName()) {
            tagCompound.setString("CustomName", this.containerName);
        }

        tagCompound.setInteger("CoreBlockX", this.coreBlockX);
        tagCompound.setInteger("CoreBlockY", this.coreBlockY);
        tagCompound.setInteger("CoreBlockZ", this.coreBlockZ);

        tagCompound.setBoolean("AcceptCoordChanger", this.acceptCoordChanger);
        tagCompound.setBoolean("InterDimensional", this.interDimensional);
        tagCompound.setInteger("CoreBlockDimentionId", this.dimentionId);
    }

    public int[] getSlotsDrop() {
        syncCoreBlock();
        return super.getSlotsDrop();
    }

    public int getFrontDirection() {
        syncCoreBlock();
        return super.getFrontDirection();
    }

    public void doAutoExtract() {
        if (syncCoreBlock())
            super.doAutoExtract();
    }

    public void doAutoInsert() {
        if (syncCoreBlock()) {
            super.doAutoInsert();
        }
    }

    public int getItemUseMode(ItemStack itemStack, EntityPlayer player) {
        if (itemStack == null) return -1;
        if (UtilItemStack.areItemDamageEqual(itemStack, CItems.itemMisc.get("SynchronousParts"))) return 91;

        syncCoreBlock();
        return super.getItemUseMode(itemStack, player);
    }

    public boolean isUsable(ItemStack itemStack, EntityPlayer player, int direction, float hitX, float hitY, float hitZ) {
        if (getItemUseMode(itemStack, player) == 90 || getItemUseMode(itemStack, player) == 91) return true;
        return (syncCoreBlock() && super.isUsable(itemStack, player, direction, hitX, hitY, hitZ));
    }

    public void useItemFromSide(ItemStack itemStack, EntityPlayer player, int side, int mode) {
        this.acceptCoordChanger = true;
        if (mode == 91 && !this.acceptCoordChanger && --(player.getCurrentEquippedItem()).stackSize == 0)
            player.inventory.mainInventory[player.inventory.currentItem] = null;

        if (mode == 90 && itemStack.hasTagCompound() && this.acceptCoordChanger) {
            NBTTagCompound tag = itemStack.getTagCompound();
            int cx = tag.getInteger("CoordMemoryX");
            int cy = tag.getInteger("CoordMemoryY");
            int cz = tag.getInteger("CoordMemoryZ");
            int cd = tag.getInteger("CoordMemoryDimID");
            if (setCoreBlockDimension(cd) && setCoreBlockCoord(cx - this.xCoord, cy - this.yCoord, cz - this.zCoord)) {
                player.addChatMessage(new ChatComponentText("Set Core block Coord (" + cx + "," + cy + "," + cz + ") in dim " + cd));
            }
        } else if (syncCoreBlock()) super.useItemFromSide(itemStack, player, side, mode);
        setSyncFlag();
    }

    public boolean consumeClayEnergy(long energy) {
        return false;
    }

    public boolean produceClayEnergy() {
        return false;
    }

    public boolean acceptEnergyClay() {
        return (syncCoreBlock() && super.acceptEnergyClay());
    }

    public void openInventory() {}

    public void closeInventory() {}

    public void initParamsByTier(int tier) {}

    protected boolean loop = false;

    public void setSyncFlag() {
        if (!this.worldObj.isRemote && !this.loop) {
            int coreX = this.xCoord + this.coreBlockX;
            int coreY = this.yCoord + this.coreBlockY;
            int coreZ = this.zCoord + this.coreBlockZ;

            if (getCoreWorld() != null) {
                TileEntity te = UtilBuilder.safeGetTileEntity(getCoreWorld(), coreX, coreY, coreZ);
                if (te instanceof IGeneralInterface && te != this) {
                    this.loop = true;
                    ((IGeneralInterface) te).setSyncFlag();
                    this.loop = false;
                }
            }
        }

        super.setSyncFlag();
    }

    public void setInstantSyncFlag() {
        if (!this.worldObj.isRemote && !this.loop) {
            int coreX = this.xCoord + this.coreBlockX;
            int coreY = this.yCoord + this.coreBlockY;
            int coreZ = this.zCoord + this.coreBlockZ;

            if (getCoreWorld() != null) {
                TileEntity te = UtilBuilder.safeGetTileEntity(getCoreWorld(), coreX, coreY, coreZ);
                if (te instanceof IGeneralInterface && te != this) {
                    this.loop = true;
                    ((IGeneralInterface) te).setInstantSyncFlag();
                    this.loop = false;
                }
            }
        }

        super.setInstantSyncFlag();
    }

    public boolean acceptCoordChanger = false;
    public boolean interDimensional = false;
    protected int dimentionId = 0;

    public World getCoreWorld() {
        if (this.interDimensional) return DimensionManager.getWorld(this.dimentionId);
        return this.worldObj;
    }

    public boolean setCoreBlockDimension(int d) {
        this.dimentionId = d;
        this.interDimensional = true;

        if (this.worldObj != null && this.worldObj.provider.dimensionId == d) {
            this.interDimensional = false;
            return true;
        }

        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (getCoreWorld() == null) {
                this.interDimensional = false;
                return false;
            }

            if (getCoreWorld() == this.worldObj) {
                this.interDimensional = false;
                return true;
            }
        }

        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return (pass == 1);
    }

    public boolean acceptCoordChanger() {
        return this.acceptCoordChanger;
    }
}
