package mods.clayium.machine.Interface.ClayInterface;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.Interface.IInterfaceCaptive;
import mods.clayium.machine.Interface.ISynchronizedInterface;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.IClayInventory;
import mods.clayium.util.ContainClayEnergy;
import mods.clayium.util.SyncManager;
import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilCollect;

public class TileEntityClayInterface extends TileEntityClayContainer
                                     implements ISynchronizedInterface, IClayEnergyConsumer {

    protected boolean enableSync = false;
    protected boolean synced = false;
    protected IInterfaceCaptive core = IInterfaceCaptive.NONE;
    protected int[] syncSource = null;

    @Override
    public void initParams() {
        this.autoInsert = true;
    }

    @Override
    public void initParamsByTier(TierPrefix tier) {
        super.initParamsByTier(tier);

        if (this.isSynced()) {
            this.autoInsertInterval = ((TileEntityClayContainer) this.core).autoInsertInterval;
            this.autoExtractInterval = ((TileEntityClayContainer) this.core).autoExtractInterval;
            this.maxAutoInsertDefault = ((TileEntityClayContainer) this.core).maxAutoInsertDefault;
            this.maxAutoExtractDefault = ((TileEntityClayContainer) this.core).maxAutoExtractDefault;
        }
    }

    @Override
    public boolean isSynced() {
        return this.synced && this.core != IInterfaceCaptive.NONE;
    }

    /**
     * assert
     * 
     * <pre>
     * {@code this.enableSync == true}
     * </pre>
     */
    public void setCoreBlock(@Nullable IInterfaceCaptive tile) {
        if (IInterfaceCaptive.isSyncable(tile)) {
            this.core = tile;
            this.synced = true;
            this.initParamsByTier(this.tier);
        } else {
            this.core = IInterfaceCaptive.NONE;
            this.synced = false;
        }
    }

    public IInterfaceCaptive getCore() {
        return this.core;
    }

    public boolean markEnableSync() {
        if (this.isSyncEnabled()) return false;

        this.enableSync = true;
        return true;
    }

    public boolean isSyncEnabled() {
        return this.enableSync;
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("CustomName", Constants.NBT.TAG_STRING)) {
            this.setCustomName(compound.getString("CustomName"));
        }

        loadIORoutes(compound);

        this.autoInsertCount = compound.getInteger("AutoInsertCount");
        this.autoExtractCount = compound.getInteger("AutoExtractCount");

        if (compound.hasKey("Filters", Constants.NBT.TAG_LIST)) {
            UtilCollect.tagList2EnumMap(compound.getTagList("Filters", Constants.NBT.TAG_COMPOUND), this.getFilters());
        }

        if (compound.hasKey("SyncSource", Constants.NBT.TAG_INT_ARRAY)) {
            this.syncSource = compound.getIntArray("SyncSource");
        }

        this.enableSync = compound.getBoolean("SyncEnabled");

        initParamsByTier(TierPrefix.get(compound.getInteger("Tier")));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound compound) {
        if (hasCustomName()) {
            compound.setString("CustomName", this.getName());
        }

        saveIORoutes(compound);

        compound.setInteger("AutoInsertCount", this.autoInsertCount);
        compound.setInteger("AutoExtractCount", this.autoExtractCount);

        compound.setInteger("Tier", this.tier.meta());

        compound.setTag("Filters", UtilCollect.enumMap2TagList(this.getFilters()));

        if (this.isSynced()) {
            compound.setIntArray("SyncSource", SyncManager.getIntArrayFromTile((TileEntity) this.core));
        }

        compound.setBoolean("SyncEnabled", this.enableSync);

        return compound;
    }

    @Override
    public void update() {
        if (this.syncSource != null) {
            TileEntity tile = SyncManager.getTileFromIntArray(this.syncSource);
            if (tile instanceof IInterfaceCaptive) {
                SyncManager.synchronize((IInterfaceCaptive) tile, this);
                this.markDirty();
            }
            this.syncSource = null;
        }

        super.update();
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (this.isSynced()) {
            this.core.markDirty();

            assert this.core instanceof TileEntity;
            TileEntity te = SyncManager.getTileFromIntArray(SyncManager.getIntArrayFromTile((TileEntity) this.core));
            if (IInterfaceCaptive.isSyncable(te)) {
                SyncManager.immediateSync((IInterfaceCaptive) te, this);
            } else {
                SyncManager.immediateSync(null, this);
            }
        }
    }

    // ===== Below this line, there is probably no information. =====

    @Override
    public NonNullList<ItemStack> getContainerItemStacks() {
        if (this.core instanceof IClayInventory)
            return ((IClayInventory) this.core).getContainerItemStacks();

        return IClayInventory.EMPTY_INV;
    }

    @Override
    public int getSizeInventory() {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).getSizeInventory();

        return 0;
    }

    @Override
    public boolean isEmpty() {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).isEmpty();

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).getStackInSlot(index);

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).decrStackSize(index, count);

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).removeStackFromSlot(index);

        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (this.core instanceof IInventory)
            ((IInventory) this.core).setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).getInventoryStackLimit();

        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).isUsableByPlayer(player);

        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (this.core instanceof IInventory)
            ((IInventory) this.core).openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (this.core instanceof IInventory)
            ((IInventory) this.core).closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).isItemValidForSlot(index, stack);

        return false;
    }

    @Override
    public int getField(int id) {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).getField(id);

        return 0;
    }

    @Override
    public void setField(int id, int value) {
        if (this.core instanceof IInventory)
            ((IInventory) this.core).setField(id, value);
    }

    @Override
    public int getFieldCount() {
        if (this.core instanceof IInventory)
            return ((IInventory) this.core).getFieldCount();

        return 0;
    }

    @Override
    public void clear() {
        if (this.core instanceof IInventory)
            ((IInventory) this.core).clear();
    }

    @Override
    public List<int[]> getListSlotsImport() {
        if (this.core instanceof IClayInventory)
            return ((IClayInventory) this.core).getListSlotsImport();

        return Collections.emptyList();
    }

    @Override
    public List<int[]> getListSlotsExport() {
        if (this.core instanceof IClayInventory)
            return ((IClayInventory) this.core).getListSlotsExport();

        return Collections.emptyList();
    }

    @Override
    public List<ResourceLocation> getInsertIcons() {
        return this.isSynced() ? ((TileEntityClayContainer) this.core).getInsertIcons() : Collections.emptyList();
    }

    @Override
    public List<ResourceLocation> getExtractIcons() {
        return this.isSynced() ? ((TileEntityClayContainer) this.core).getExtractIcons() : Collections.emptyList();
    }

    @Override
    public List<ResourceLocation> getInsertPipeIcons() {
        return this.isSynced() ? ((TileEntityClayContainer) this.core).getInsertPipeIcons() : Collections.emptyList();
    }

    @Override
    public List<ResourceLocation> getExtractPipeIcons() {
        return this.isSynced() ? ((TileEntityClayContainer) this.core).getExtractPipeIcons() : Collections.emptyList();
    }

    @Override
    public ContainClayEnergy containEnergy() {
        if (IClayEnergyConsumer.hasClayEnergy(this.core)) {
            return ((IClayEnergyConsumer) this.core).containEnergy();
        }
        return ContainClayEnergy.NIL;
    }

    @Override
    public int getClayEnergyStorageSize() {
        if (IClayEnergyConsumer.hasClayEnergy(this.core)) {
            return ((IClayEnergyConsumer) this.core).getClayEnergyStorageSize();
        }
        return 0;
    }

    @Override
    public void setClayEnergyStorageSize(int size) {}

    @Override
    public int getEnergySlot() {
        if (IClayEnergyConsumer.hasClayEnergy(this.core)) {
            return ((IClayEnergyConsumer) this.core).getEnergySlot();
        }
        return -1;
    }

    @Override
    public boolean acceptClayEnergy() {
        if (IClayEnergyConsumer.hasClayEnergy(this.core)) {
            return ((IClayEnergyConsumer) this.core).acceptClayEnergy();
        }
        return false;
    }
}
