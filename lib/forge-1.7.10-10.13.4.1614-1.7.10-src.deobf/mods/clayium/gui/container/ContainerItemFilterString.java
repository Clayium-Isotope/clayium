package mods.clayium.gui.container;

import mods.clayium.item.filter.IFilterSizeChecker;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerItemFilterString extends ContainerTemp {
    protected EntityPlayer player;

    public ContainerItemFilterString(EntityPlayer player) {
        super(player.inventory, (IInventory) null, (Block) null, new Object[0]);
        this.player = player;
    }

    protected int filterPos;

    public String getFilterString() {
        NBTTagCompound tag = (this.player.getCurrentEquippedItem() != null) ? this.player.getCurrentEquippedItem().getTagCompound() : null;
        if (tag == null) return "";
        return tag.getString("FilterString");
    }

    public void setFilterString(String string) {
        if (this.player.getCurrentEquippedItem() == null)
            return;
        NBTTagCompound tag = this.player.getCurrentEquippedItem().hasTagCompound() ? this.player.getCurrentEquippedItem().getTagCompound() : new NBTTagCompound();
        tag.setString("FilterString", string);
        this.player.getCurrentEquippedItem().setTagCompound(tag);
        ((Slot) this.inventorySlots.get(this.filterPos)).onSlotChanged();
        detectAndSendChanges();
    }


    public String getTextFieldString(EntityPlayer player, int id) {
        return getFilterString();
    }


    public void setTextFieldString(EntityPlayer player, String string, int id) {
        setFilterString(string);
    }


    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        ItemStack item = getSlot(this.filterPos).getStack();
        if (item != null && item.getItem() instanceof IFilterSizeChecker) {
            ((IFilterSizeChecker) item.getItem()).checkFilterSize(item, this.player, this.player.getEntityWorld());
        }
    }


    public void setMachineInventorySlots(InventoryPlayer player) {}


    public void addPlayerInventorySlots(InventoryPlayer player) {
        int i;
        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot((IInventory) player, j + i * 9 + 9, this.playerSlotOffsetX + 8 + j * 18, this.playerSlotOffsetY + 12 + i * 18));
            }
        }
        for (i = 0; i < 9; i++) {
            if (player.currentItem == i) {
                addSlotToContainer(new Slot((IInventory) player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70) {
                    public boolean canTakeStack(EntityPlayer p_82869_1_) {
                        return false;
                    }
                });
                this.filterPos = this.inventorySlots.size() - 1;
            } else {

                addSlotToContainer(new Slot((IInventory) player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70));
            }
        }
    }


    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = 36;
        super.initParameters(player);
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return false;
    }


    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return false;
    }


    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        return false;
    }

    public String getInventoryName() {
        return getSlot(this.filterPos).getStack().getDisplayName();
    }
}
