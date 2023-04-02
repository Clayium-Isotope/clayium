package mods.clayium.machine.Interface;

import mods.clayium.machine.common.IClayInventory;
import mods.clayium.util.EnumSide;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * for TileEntity
 */
public interface IInterfaceCaptive extends IInventory, IClayInventory {
    IInterfaceCaptive NONE = new EmptyCaptive();

    default boolean acceptInterfaceSync() {
        return true;
    }

    static boolean isSyncable(@Nullable TileEntity tile) {
        return tile != null && tile instanceof IInterfaceCaptive && ((IInterfaceCaptive) tile).acceptInterfaceSync();
    }

    @Deprecated
    static boolean isExistingCaptive(IInterfaceCaptive core) {
        return core != NONE;
    }

    NonNullList<ItemStack> getContainerItemStacks();

    void setCustomName(String name);

    class EmptyCaptive implements IInterfaceCaptive {
        private static final NonNullList<ItemStack> containerItemStacks = NonNullList.from(ItemStack.EMPTY);
        private String customName = null;

        EmptyCaptive() {}

        @Override
        public boolean acceptEnergyClay() {
            return false;
        }

        @Override
        public int getClayEnergyStorageSize() {
            return 0;
        }

        @Override
        public List<int[]> getListSlotsImport() {
            return Collections.emptyList();
        }

        @Override
        public List<int[]> getListSlotsExport() {
            return Collections.emptyList();
        }

        @Override
        public EnumFacing getFront() {
            return EnumFacing.NORTH;
        }

        @Override
        public int getImportRoute(EnumSide side) {
            return -1;
        }

        @Override
        public int getExportRoute(EnumSide side) {
            return -1;
        }

        @Override
        public void setImportRoute(EnumSide side, int route) {

        }

        @Override
        public void setExportRoute(EnumSide side, int route) {

        }

        @Override
        public Map<EnumFacing, ItemStack> getFilters() {
            return Collections.emptyMap();
        }

        @Override
        public NonNullList<ItemStack> getContainerItemStacks() {
            return containerItemStacks;
        }

        @Override
        public int getSizeInventory() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack decrStackSize(int index, int count) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeStackFromSlot(int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {

        }

        @Override
        public int getInventoryStackLimit() {
            return 0;
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean isUsableByPlayer(EntityPlayer player) {
            return false;
        }

        @Override
        public void openInventory(EntityPlayer player) {

        }

        @Override
        public void closeInventory(EntityPlayer player) {

        }

        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return false;
        }

        @Override
        public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
            return false;
        }

        @Override
        public boolean canExtractItem(int index, ItemStack itemStackIn, EnumFacing direction) {
            return false;
        }

        @Override
        public boolean checkBlocked(ItemStack itemStackIn, EnumFacing direction) {
            return true;
        }

        @Override
        public int[] getSlotsForFace(EnumFacing side) {
            return new int[0];
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {

        }

        @Override
        public String getName() {
            return this.customName;
        }

        @Override
        public boolean hasCustomName() {
            return this.customName != null && !this.customName.isEmpty();
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentString(this.customName);
        }

        @Override
        public void setCustomName(String customName) {
            this.customName = customName;
        }
    }
}
