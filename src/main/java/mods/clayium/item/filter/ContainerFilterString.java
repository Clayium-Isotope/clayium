package mods.clayium.item.filter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;

import mods.clayium.gui.ContainerTemp;

public class ContainerFilterString extends ContainerTemp {

    protected final int filterSlotIndex;
    protected final ItemStack selectedFilter;
    protected final boolean onHotbar;

    public ContainerFilterString(EntityPlayer player) {
        super(player.inventory, null);

        ItemStack filter = this.player.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        int index;
        if (!filter.isEmpty() && IFilter.isFilter(filter)) {
            index = this.player.currentItem;

            this.onHotbar = true;
        } else {
            filter = this.player.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
            if (!filter.isEmpty() && IFilter.isFilter(filter)) {
                index = this.player.getSizeInventory() - this.player.offHandInventory.size();
            } else {
                filter = ItemStack.EMPTY;
                index = -1;
            }

            this.onHotbar = false;
        }

        this.selectedFilter = filter;
        this.filterSlotIndex = index;
    }

    public String getFilterString() {
        NBTTagCompound tag = !this.selectedFilter.isEmpty() ? this.selectedFilter.getTagCompound() : null;
        return tag == null ? "" : tag.getString("FilterString");
    }

    public void setFilterString(String string) {
        if (!this.selectedFilter.isEmpty()) {
            NBTTagCompound tag = this.selectedFilter.hasTagCompound() ? this.selectedFilter.getTagCompound() :
                    new NBTTagCompound();
            tag.setString("FilterString", string);
            if (!string.isEmpty())
                this.player.player.sendMessage(new TextComponentString("Set " + string));
            this.selectedFilter.setTagCompound(tag);
            this.getSlot(this.filterSlotIndex).onSlotChanged();
            this.detectAndSendChanges();
        }
    }

    @Override
    public String getTextFieldString(EntityPlayer player, int id) {
        return this.getFilterString();
    }

    @Override
    public void setTextFieldString(EntityPlayer player, String string, int id) {
        this.setFilterString(string);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        // ItemStack item = this.getSlot(this.filterPos).getStack();
        // if (!item.isEmpty() && item.getItem() instanceof IFilterSizeChecker) {
        // ((IFilterSizeChecker) item.getItem()).checkFilterSize(item, this.player, this.player.player.world);
        // }
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {}

    @Override
    public void setupPlayerSlots(InventoryPlayer player) {
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player, j + i * 9 + 9, this.playerSlotOffsetX + 8 + j * 18,
                        this.playerSlotOffsetY + 12 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            if (this.onHotbar && this.filterSlotIndex == i) {
                this.addSlotToContainer(
                        new Slot(player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70) {

                            public boolean canTakeStack(EntityPlayer player) {
                                return false;
                            }
                        });
            } else {
                this.addSlotToContainer(
                        new Slot(player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70));
            }
        }

        if (!this.onHotbar && this.filterSlotIndex != -1) {
            this.addSlotToContainer(new Slot(player, this.filterSlotIndex, 0, 0) {

                @Override
                public boolean isEnabled() {
                    return false;
                }
            });
        }
    }

    @Override
    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = 36;
        super.initParameters(player);
    }

    @Override
    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return false;
    }

    @Override
    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return false;
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return false;
    }

    @Override
    public String getInventoryName() {
        return this.player.getStackInSlot(this.filterSlotIndex).getDisplayName();
    }
}
