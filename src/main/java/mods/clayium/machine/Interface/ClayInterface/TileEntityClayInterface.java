package mods.clayium.machine.Interface.ClayInterface;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.machine.Interface.IInterfaceCaptive;
import mods.clayium.machine.Interface.ISynchronizedInterface;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilCollect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class TileEntityClayInterface extends TileEntityClayContainer implements ISynchronizedInterface {
    protected boolean enableSync = false;
    protected boolean synced = false;
    protected IInterfaceCaptive core = IInterfaceCaptive.NONE;
    protected int[] syncSource = null;

    @Override
    public void initParams() {
        this.autoInsert = true;
    }

    @Override
    public void initParamsByTier(int tier) {
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

    public static void setCoreBlock(@Nullable TileEntityClayContainer tile, TileEntityClayInterface _this) {
        if (_this.enableSync && IInterfaceCaptive.isSyncable(tile)) {
            _this.core = tile;
            _this.synced = true;
        } else {
            _this.core = IInterfaceCaptive.NONE;
            _this.synced = false;
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
    public void updateEntity() {
        if (this.syncSource != null) {
            setCoreBlock((TileEntityClayContainer) UtilBuilder.getTileFromIntArray(this.syncSource), this);
        }

        super.updateEntity();
        if (this.isSynced()) {
            ((TileEntityClayContainer) this.core).updateEntity();
        }
    }

    @Override
    public void readMoreFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("CustomName", Constants.NBT.TAG_STRING)) {
            this.customName = compound.getString("CustomName");
        }

        loadIORoutes(compound);

        this.autoInsertCount = compound.getInteger("AutoInsertCount");
        this.autoExtractCount = compound.getInteger("AutoExtractCount");

        if (compound.hasKey("Filters", Constants.NBT.TAG_LIST)) {
            UtilCollect.tagList2EnumMap(compound.getTagList("Filters", Constants.NBT.TAG_COMPOUND), this.filters);
        }

        if (compound.hasKey("SyncSource", Constants.NBT.TAG_INT_ARRAY)) {
            this.syncSource = compound.getIntArray("SyncSource");
            this.isLoaded = false;
        }

        this.enableSync = compound.getBoolean("SyncEnabled");

        initParamsByTier(compound.getInteger("Tier"));
    }

    @Override
    public NBTTagCompound writeMoreToNBT(NBTTagCompound compound) {
        if (hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        saveIORoutes(compound);

        compound.setInteger("AutoInsertCount", this.autoInsertCount);
        compound.setInteger("AutoExtractCount", this.autoExtractCount);

        compound.setInteger("Tier", this.tier);

        compound.setTag("Filters", UtilCollect.enumMap2TagList(this.filters));

        if (this.isSynced()) {
            compound.setIntArray("SyncSource", UtilBuilder.getIntArrayFromTile((TileEntity) this.core));
        }

        compound.setBoolean("SyncEnabled", this.enableSync);

        return compound;
    }

    // ===== Below this line, there is probably no information. =====

    @Override
    public NonNullList<ItemStack> getContainerItemStacks() {
        return this.core.getContainerItemStacks();
    }

    @Override
    public int getSizeInventory() {
        return this.core.getSizeInventory();
    }

    @Override
    public boolean isEmpty() {
        return this.core.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.core.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.core.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return this.core.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.core.setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.core.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.core.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.core.isUsableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        this.core.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        this.core.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return this.core.isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return this.core.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        this.core.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return this.core.getFieldCount();
    }

    @Override
    public void clear() {
        this.core.clear();
    }

    @Override
    public boolean acceptEnergyClay() {
        return this.core.acceptEnergyClay();
    }

    @Override
    public int getClayEnergyStorageSize() {
        return this.core.getClayEnergyStorageSize();
    }

    @Override
    public List<int[]> getListSlotsImport() {
        return this.core.getListSlotsImport();
    }

    @Override
    public List<int[]> getListSlotsExport() {
        return this.core.getListSlotsExport();
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
}
