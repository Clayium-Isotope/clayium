package mods.clayium.gui.container;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import mods.clayium.block.tile.IGeneralInterface;
import mods.clayium.core.ClayiumCore;
import mods.clayium.network.GuiTextFieldPacket;
import mods.clayium.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ContainerTemp
        extends Container {
    protected int lastCraftTime;
    protected int lastTimeToCraft;
    protected IInventory tile;
    public ArrayList<Slot> machineInventorySlots = new ArrayList<Slot>();
    protected int playerSlotIndex;
    public int machineGuiSizeX = 176;
    public int machineGuiSizeY = 72;


    public int playerSlotOffsetX;

    public int playerSlotOffsetY;

    public Object[] additionalParams;

    private int field_94536_g;

    private int field_94535_f;

    private final Set field_94537_h;


    protected void initParameters(InventoryPlayer player) {
        initParametersDefault(player);
    }

    protected void initParametersDefault(InventoryPlayer player) {
        this.machineGuiSizeX = Math.max(this.machineGuiSizeX, 176);
        this.playerSlotOffsetX = (this.machineGuiSizeX - 176) / 2;
        this.playerSlotOffsetY = this.machineGuiSizeY;
    }

    public void setTileEntity(IInventory tile) {
        this.tile = tile;
    }


    public int addMachineSlotToContainer(Slot slot) {
        this.machineInventorySlots.add(slot);
        return this.machineInventorySlots.size() - 1;
    }

    public void addMachineInventorySlots(InventoryPlayer player) {
        for (int i = 0; i < this.machineInventorySlots.size(); i++) {
            addSlotToContainer(this.machineInventorySlots.get(i));
        }
        this.playerSlotIndex = this.machineInventorySlots.size();
    }


    public void addPlayerInventorySlots(InventoryPlayer player) {
        int i;
        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot((IInventory) player, j + i * 9 + 9, this.playerSlotOffsetX + 8 + j * 18, this.playerSlotOffsetY + 12 + i * 18));
            }
        }
        for (i = 0; i < 9; i++) {
            addSlotToContainer(new Slot((IInventory) player, i, this.playerSlotOffsetX + 8 + i * 18, this.playerSlotOffsetY + 70));
        }
    }


    public boolean canInteractWith(EntityPlayer player) {
        if (this.tile == null) return true;
        return this.tile.isUseableByPlayer(player);
    }

    public String getInventoryName() {
        return (this.tile != null) ? this.tile.getInventoryName() : "";
    }


    public String getTextFieldString(EntityPlayer player, int id) {
        return null;
    }


    public void setTextFieldString(EntityPlayer player, String string, int id) {}


    public void sendTextFieldStringToClient(EntityPlayer player, String string, int id) {
        if (!(player.getEntityWorld()).isRemote && player instanceof EntityPlayerMP) {
            ClayiumCore.packetDispatcher.sendTo((IMessage) new GuiTextFieldPacket(string, id), (EntityPlayerMP) player);
        }
    }


    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();


            if (par2 < this.playerSlotIndex) {
                if (!transferStackFromMachineInventory(itemstack1, par2)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);


            } else if (!transferStackFromPlayerInventory(itemstack1, par2)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, itemstack1);
        }
        return itemstack;
    }

    public boolean transferStackToPlayerInventory(ItemStack itemstack1, boolean flag) {
        return mergeItemStack(itemstack1, this.playerSlotIndex, this.playerSlotIndex + 36, flag);
    }

    public boolean transferStackFromPlayerInventory(ItemStack itemstack1, int par2) {
        if (canTransferToMachineInventory(itemstack1)) {
            if (!transferStackToMachineInventory(itemstack1)) {
                return false;
            }
        } else if (par2 >= this.playerSlotIndex && par2 < this.playerSlotIndex + 27) {
            if (!mergeItemStack(itemstack1, this.playerSlotIndex + 27, this.playerSlotIndex + 36, false)) {
                return false;
            }
        } else if (par2 >= this.playerSlotIndex + 27 && par2 < this.playerSlotIndex + 36 && !mergeItemStack(itemstack1, this.playerSlotIndex, this.playerSlotIndex + 27, false)) {
            return false;
        }
        return true;
    }


    protected int canMergeItemStack(ItemStack p_75135_1_, int p_75135_2_, int p_75135_3_, boolean p_75135_4_) {
        int stackSize = p_75135_1_.stackSize;
        boolean flag1 = false;
        int k = p_75135_2_;

        if (p_75135_4_) {
            k = p_75135_3_ - 1;
        }


        if (p_75135_1_.isStackable()) {
            while (stackSize > 0 && ((!p_75135_4_ && k < p_75135_3_) || (p_75135_4_ && k >= p_75135_2_))) {

                Slot slot = (Slot) this.inventorySlots.get(k);
                ItemStack itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.getItem() == p_75135_1_.getItem() && (!p_75135_1_.getHasSubtypes() || p_75135_1_.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(p_75135_1_, itemstack1)) {

                    int l = itemstack1.stackSize + stackSize;

                    if (l <= p_75135_1_.getMaxStackSize()) {
                        return 0;
                    }
                    if (itemstack1.stackSize < p_75135_1_.getMaxStackSize()) {
                        stackSize -= p_75135_1_.getMaxStackSize() - itemstack1.stackSize;
                    }
                }

                if (p_75135_4_) {

                    k--;

                    continue;
                }
                k++;
            }
        }


        if (stackSize > 0) {

            if (p_75135_4_) {

                k = p_75135_3_ - 1;
            } else {

                k = p_75135_2_;
            }

            while ((!p_75135_4_ && k < p_75135_3_) || (p_75135_4_ && k >= p_75135_2_)) {

                Slot slot = (Slot) this.inventorySlots.get(k);
                ItemStack itemstack1 = slot.getStack();

                if (itemstack1 == null) {
                    return 0;
                }

                if (p_75135_4_) {

                    k--;

                    continue;
                }
                k++;
            }
        }


        return stackSize;
    }


    public boolean enchantItem(EntityPlayer player, int action) {
        if (this.tile instanceof IGeneralInterface) {
            ((IGeneralInterface) this.tile).pushButton(player, action);
        }
        return true;
    }

    public boolean pushClientButton(EntityPlayer player, int action) {
        return true;
    }


    public void detectAndSendChanges() {
        for (int i = 0; i < this.inventorySlots.size(); i++) {

            ItemStack itemstack = ((Slot) this.inventorySlots.get(i)).getStack();
            ItemStack itemstack1 = (ItemStack) this.inventoryItemStacks.get(i);

            if (!UtilItemStack.areStackEqual(itemstack1, itemstack)) {

                if (this.tile instanceof IGeneralInterface) {
                    ((IGeneralInterface) this.tile).setSyncFlag();
                }

                itemstack1 = (itemstack == null) ? null : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);

                if (i >= this.playerSlotIndex && i < this.playerSlotIndex + 36) {
                    for (int j = 0; j < this.crafters.size(); j++) {
                        ((ICrafting) this.crafters.get(j)).sendSlotContents(this, i, itemstack1);
                    }
                }
            }
        }
        if (this.tile instanceof IGeneralInterface) {
            ((IGeneralInterface) this.tile).markForWeakUpdate();
        }
    }


    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        if (this.tile != null)
            this.tile.closeInventory();
    }

    public boolean drawInventoryName() {
        return true;
    }

    public boolean drawPlayerInventoryName() {
        return true;
    }


    public ContainerTemp(InventoryPlayer player, IInventory tile, Block block, Object... additionalParams) {
        this.field_94537_h = new HashSet();
        this.additionalParams = additionalParams;
        setTileEntity(tile);
        if (this.tile != null)
            this.tile.openInventory();
        initParameters(player);
        setMachineInventorySlots(player);
        addMachineInventorySlots(player);
        addPlayerInventorySlots(player);
    }

    public ItemStack slotClick(int p_75144_1_, int p_75144_2_, int p_75144_3_, EntityPlayer p_75144_4_) {
        ItemStack itemstack = null;
        InventoryPlayer inventoryplayer = p_75144_4_.inventory;


        if (p_75144_3_ == 5) {

            int l = this.field_94536_g;
            this.field_94536_g = func_94532_c(p_75144_2_);

            if ((l != 1 || this.field_94536_g != 2) && l != this.field_94536_g) {

                func_94533_d();
            } else if (inventoryplayer.getItemStack() == null) {

                func_94533_d();
            } else if (this.field_94536_g == 0) {

                this.field_94535_f = func_94529_b(p_75144_2_);

                if (func_94528_d(this.field_94535_f)) {
                    this.field_94536_g = 1;
                    this.field_94537_h.clear();
                } else {
                    func_94533_d();
                }

            } else if (this.field_94536_g == 1) {

                Slot slot = (Slot) this.inventorySlots.get(p_75144_1_);

                if (slot != null && func_94527_a(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && (inventoryplayer.getItemStack()).stackSize > this.field_94537_h.size() && canDragIntoSlot(slot)) {
                    this.field_94537_h.add(slot);
                }
            } else if (this.field_94536_g == 2) {

                if (!this.field_94537_h.isEmpty()) {

                    ItemStack itemstack3 = inventoryplayer.getItemStack().copy();
                    int i1 = (inventoryplayer.getItemStack()).stackSize;
                    Iterator<Slot> iterator = this.field_94537_h.iterator();

                    while (iterator.hasNext()) {

                        Slot slot1 = iterator.next();

                        if (slot1 != null && func_94527_a(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && (inventoryplayer.getItemStack()).stackSize >= this.field_94537_h.size() && canDragIntoSlot(slot1)) {

                            ItemStack itemstack1 = itemstack3.copy();
                            int j1 = slot1.getHasStack() ? (slot1.getStack()).stackSize : 0;
                            func_94525_a(this.field_94537_h, this.field_94535_f, itemstack1, j1);

                            if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
                                itemstack1.stackSize = itemstack1.getMaxStackSize();
                            }

                            if (itemstack1.stackSize > slot1.getSlotStackLimit()) {
                                itemstack1.stackSize = slot1.getSlotStackLimit();
                            }
                            if (!(slot1 instanceof mods.clayium.gui.SlotMemory))
                                i1 -= itemstack1.stackSize - j1;
                            slot1.putStack(itemstack1);
                        }
                    }

                    itemstack3.stackSize = i1;

                    if (itemstack3.stackSize <= 0) {
                        itemstack3 = null;
                    }

                    inventoryplayer.setItemStack(itemstack3);
                }

                func_94533_d();
            } else {

                func_94533_d();
            }

        } else if (this.field_94536_g != 0) {

            func_94533_d();


        } else if ((p_75144_3_ == 0 || p_75144_3_ == 1) && (p_75144_2_ == 0 || p_75144_2_ == 1)) {

            if (p_75144_1_ == -999) {

                if (inventoryplayer.getItemStack() != null && p_75144_1_ == -999) {

                    if (p_75144_2_ == 0) {

                        p_75144_4_.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack((ItemStack) null);
                    }

                    if (p_75144_2_ == 1) {
                        p_75144_4_.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);

                        if ((inventoryplayer.getItemStack()).stackSize == 0) {
                            inventoryplayer.setItemStack((ItemStack) null);
                        }
                    }

                }
            } else if (p_75144_3_ == 1) {

                if (p_75144_1_ < 0) {
                    return null;
                }

                Slot slot2 = (Slot) this.inventorySlots.get(p_75144_1_);

                if (slot2 != null && slot2.canTakeStack(p_75144_4_)) {

                    ItemStack itemstack3 = transferStackInSlot(p_75144_4_, p_75144_1_);

                    if (itemstack3 != null) {
                        Item item = itemstack3.getItem();
                        itemstack = itemstack3.copy();

                        if (slot2.getStack() != null && slot2.getStack().getItem() == item) {
                            retrySlotClick(p_75144_1_, p_75144_2_, true, p_75144_4_);
                        }
                    }

                }
            } else {

                if (p_75144_1_ < 0) {
                    return null;
                }

                Slot slot2 = (Slot) this.inventorySlots.get(p_75144_1_);

                if (slot2 != null) {
                    ItemStack itemstack3 = slot2.getStack();
                    ItemStack itemstack4 = inventoryplayer.getItemStack();

                    if (slot2 instanceof mods.clayium.gui.SlotMemory &&
                            itemstack4 != null) itemstack4 = itemstack4.copy();

                    if (itemstack3 != null) {
                        itemstack = itemstack3.copy();
                    }

                    if (itemstack3 == null) {

                        if (itemstack4 != null && slot2.isItemValid(itemstack4)) {

                            int l1 = (p_75144_2_ == 0) ? itemstack4.stackSize : 1;

                            if (l1 > slot2.getSlotStackLimit()) {
                                l1 = slot2.getSlotStackLimit();
                            }

                            if (itemstack4.stackSize >= l1) {
                                slot2.putStack(itemstack4.splitStack(l1));
                            }

                            if (itemstack4.stackSize == 0) {
                                if (!(slot2 instanceof mods.clayium.gui.SlotMemory)) {
                                    inventoryplayer.setItemStack((ItemStack) null);
                                }
                            }
                        }
                    } else if (slot2.canTakeStack(p_75144_4_)) {

                        if (itemstack4 == null) {

                            int l1 = (p_75144_2_ == 0) ? itemstack3.stackSize : ((itemstack3.stackSize + 1) / 2);
                            ItemStack itemstack5 = slot2.decrStackSize(l1);
                            inventoryplayer.setItemStack(itemstack5);

                            if (itemstack3.stackSize == 0) {
                                slot2.putStack((ItemStack) null);
                            }

                            slot2.onPickupFromSlot(p_75144_4_, inventoryplayer.getItemStack());
                        } else if (slot2.isItemValid(itemstack4)) {

                            if (itemstack3.getItem() == itemstack4.getItem() && itemstack3.getItemDamage() == itemstack4.getItemDamage() && ItemStack.areItemStackTagsEqual(itemstack3, itemstack4)) {
                                int l1 = (p_75144_2_ == 0) ? itemstack4.stackSize : 1;

                                if (l1 > slot2.getSlotStackLimit() - itemstack3.stackSize) {
                                    l1 = slot2.getSlotStackLimit() - itemstack3.stackSize;
                                }

                                if (l1 > itemstack4.getMaxStackSize() - itemstack3.stackSize) {
                                    l1 = itemstack4.getMaxStackSize() - itemstack3.stackSize;
                                }

                                itemstack4.splitStack(l1);

                                if (itemstack4.stackSize == 0) {
                                    inventoryplayer.setItemStack((ItemStack) null);
                                }

                                itemstack3.stackSize += l1;
                            } else if (itemstack4.stackSize <= slot2.getSlotStackLimit()) {
                                slot2.putStack(itemstack4);
                                inventoryplayer.setItemStack(itemstack3);
                            }

                        } else if (itemstack3.getItem() == itemstack4.getItem() && itemstack4.getMaxStackSize() > 1 && (!itemstack3.getHasSubtypes() || itemstack3.getItemDamage() == itemstack4.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack3, itemstack4)) {

                            int l1 = itemstack3.stackSize;

                            if (l1 > 0 && l1 + itemstack4.stackSize <= itemstack4.getMaxStackSize()) {

                                itemstack4.stackSize += l1;
                                itemstack3 = slot2.decrStackSize(l1);

                                if (itemstack3.stackSize == 0) {
                                    slot2.putStack((ItemStack) null);
                                }

                                slot2.onPickupFromSlot(p_75144_4_, inventoryplayer.getItemStack());
                            }
                        }
                    }

                    slot2.onSlotChanged();
                }

            }
        } else if (p_75144_3_ == 2 && p_75144_2_ >= 0 && p_75144_2_ < 9) {

            Slot slot2 = (Slot) this.inventorySlots.get(p_75144_1_);

            if (slot2.canTakeStack(p_75144_4_)) {
                int i = 0;
                ItemStack itemstack3 = inventoryplayer.getStackInSlot(p_75144_2_);
                boolean flag = (itemstack3 == null || (slot2.inventory == inventoryplayer && slot2.isItemValid(itemstack3)));
                int l1 = -1;

                if (!flag) {

                    l1 = inventoryplayer.getFirstEmptyStack();
                    i = l1 > -1 ? 1 : 0;
                }

                if (slot2.getHasStack() && i != 0) {

                    ItemStack itemstack5 = slot2.getStack();
                    inventoryplayer.setInventorySlotContents(p_75144_2_, itemstack5.copy());

                    if ((slot2.inventory != inventoryplayer || !slot2.isItemValid(itemstack3)) && itemstack3 != null) {

                        if (l1 > -1) {
                            inventoryplayer.addItemStackToInventory(itemstack3);
                            slot2.decrStackSize(itemstack5.stackSize);
                            slot2.putStack(null);
                            slot2.onPickupFromSlot(p_75144_4_, itemstack5);
                        }

                    } else {

                        slot2.decrStackSize(itemstack5.stackSize);
                        slot2.putStack(itemstack3);
                        slot2.onPickupFromSlot(p_75144_4_, itemstack5);
                    }

                } else if (!slot2.getHasStack() && itemstack3 != null && slot2.isItemValid(itemstack3)) {

                    inventoryplayer.setInventorySlotContents(p_75144_2_, (ItemStack) null);
                    slot2.putStack(itemstack3);
                }

            }
        } else if (p_75144_3_ == 3 && p_75144_4_.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && p_75144_1_ >= 0) {

            Slot slot2 = (Slot) this.inventorySlots.get(p_75144_1_);

            if (slot2 != null && slot2.getHasStack()) {
                ItemStack itemstack3 = slot2.getStack().copy();
                itemstack3.stackSize = itemstack3.getMaxStackSize();
                inventoryplayer.setItemStack(itemstack3);
            }

        } else if (p_75144_3_ == 4 && inventoryplayer.getItemStack() == null && p_75144_1_ >= 0) {

            Slot slot2 = (Slot) this.inventorySlots.get(p_75144_1_);

            if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(p_75144_4_)) {
                ItemStack itemstack3 = slot2.decrStackSize((p_75144_2_ == 0) ? 1 : (slot2.getStack()).stackSize);
                slot2.onPickupFromSlot(p_75144_4_, itemstack3);
                p_75144_4_.dropPlayerItemWithRandomChoice(itemstack3, true);
            }

        } else if (p_75144_3_ == 6 && p_75144_1_ >= 0) {

            Slot slot2 = (Slot) this.inventorySlots.get(p_75144_1_);
            ItemStack itemstack3 = inventoryplayer.getItemStack();

            if (itemstack3 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(p_75144_4_))) {

                int i1 = (p_75144_2_ == 0) ? 0 : (this.inventorySlots.size() - 1);
                int l1 = (p_75144_2_ == 0) ? 1 : -1;

                for (int i2 = 0; i2 < 2; i2++) {
                    int j2;
                    for (j2 = i1; j2 >= 0 && j2 < this.inventorySlots.size() && itemstack3.stackSize < itemstack3.getMaxStackSize(); j2 += l1) {

                        Slot slot3 = (Slot) this.inventorySlots.get(j2);

                        if (slot3.getHasStack() && func_94527_a(slot3, itemstack3, true) && slot3.canTakeStack(p_75144_4_) && func_94530_a(itemstack3, slot3) && (i2 != 0 || (slot3.getStack()).stackSize != slot3.getStack().getMaxStackSize())) {

                            int k1 = Math.min(itemstack3.getMaxStackSize() - itemstack3.stackSize, (slot3.getStack()).stackSize);
                            ItemStack itemstack2 = slot3.decrStackSize(k1);
                            itemstack3.stackSize += k1;

                            if (itemstack2.stackSize <= 0) {
                                slot3.putStack((ItemStack) null);
                            }

                            slot3.onPickupFromSlot(p_75144_4_, itemstack2);
                        }
                    }
                }
            }

            detectAndSendChanges();
        }


        return itemstack;
    }


    protected void func_94533_d() {
        this.field_94536_g = 0;
        this.field_94537_h.clear();
    }

    public abstract void setMachineInventorySlots(InventoryPlayer paramInventoryPlayer);

    public abstract boolean canTransferToMachineInventory(ItemStack paramItemStack);

    public abstract boolean transferStackToMachineInventory(ItemStack paramItemStack);

    public abstract boolean transferStackFromMachineInventory(ItemStack paramItemStack, int paramInt);
}
