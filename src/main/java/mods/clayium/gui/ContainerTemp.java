package mods.clayium.gui;

import mods.clayium.block.tile.FlexibleStackLimit;
import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.common.IButtonProvider;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

public abstract class ContainerTemp extends Container {
    protected InventoryPlayer player;
    protected TileEntityGeneric tileEntity;
    public ArrayList<Slot> machineInventorySlots = new ArrayList<>();
    /**
     * これ以降は少なくとも player のスロット計36コがある必要がある
     */
    protected int playerSlotIndex;
    public int machineGuiSizeX = 176;
    public int machineGuiSizeY = 72;

    public int playerSlotOffsetX;
    public int playerSlotOffsetY;

    public ContainerTemp(InventoryPlayer player, TileEntityGeneric tileEntity) {
        this.player = player;
        this.tileEntity = tileEntity;

        if (this.tileEntity != null)
            this.tileEntity.openInventory(this.player.player);

        if (earlierConstruct())
            postConstruct();
    }

    protected final void postConstruct() {
        initParameters(this.player);
        setMachineInventorySlots(this.player);
        addMachineInventorySlots(this.player);
        setupPlayerSlots(this.player);
    }

    protected boolean earlierConstruct() {
        return true;
    }

    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeX = Math.max(this.machineGuiSizeX, 176);
        this.playerSlotOffsetX = (this.machineGuiSizeX - 176) / 2;
        this.playerSlotOffsetY = this.machineGuiSizeY;
    }

    public int addMachineSlotToContainer(Slot slot) {
        this.machineInventorySlots.add(slot);
        return this.machineInventorySlots.size() - 1;
    }

    public void addMachineInventorySlots(InventoryPlayer player) {
        for (Slot machineInventorySlot : this.machineInventorySlots) {
            addSlotToContainer(machineInventorySlot);
        }
        this.playerSlotIndex = this.machineInventorySlots.size();
    }

    public void setupPlayerSlots(InventoryPlayer player) {
        setupPlayerSlots(player, this.playerSlotOffsetY);
    }

    protected void setupPlayerSlots(InventoryPlayer player, int customYOffset) {
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, 9 + x + y * 9, this.playerSlotOffsetX + 8 + x * 18, customYOffset + 12 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, this.playerSlotOffsetX + 8 + x * 18, customYOffset + 70));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        if (this.tileEntity == null) return true;
        return this.tileEntity.isUsableByPlayer(player);
    }

    public String getInventoryName() {
        return this.tileEntity != null ? this.tileEntity.getName() : "";
    }

    public String getTextFieldString(EntityPlayer player, int id) {
        return null;
    }

    public void setTextFieldString(EntityPlayer player, String string, int id) {
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
        Slot slot = this.inventorySlots.get(par2);

        if (slot == null || !slot.getHasStack())
            return ItemStack.EMPTY;

        ItemStack itemstack1 = slot.decrStackSize(slot.getStack().getMaxStackSize());
        final ItemStack itemstack = itemstack1.copy();

        if (par2 < this.playerSlotIndex) {
            if (!transferStackFromMachineInventory(itemstack1, par2))
                return ItemStack.EMPTY;

            slot.onSlotChange(itemstack1, itemstack);
        } else if (!transferStackFromPlayerInventory(itemstack1, par2)) {
            return ItemStack.EMPTY;
        }

        if (itemstack1.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }

        if (itemstack1.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, itemstack1);
        return itemstack1;
    }

    public boolean transferStackToPlayerInventory(ItemStack itemstack1, boolean flag) {
        return mergeItemStack(itemstack1, this.playerSlotIndex, this.playerSlotIndex + 36, flag);
    }

    public boolean transferStackFromPlayerInventory(ItemStack itemstack1, int index) {
        if (canTransferToMachineInventory(itemstack1))
            return transferStackToMachineInventory(itemstack1, index);

        if (index >= this.playerSlotIndex && index < this.playerSlotIndex + 27)
            return mergeItemStack(itemstack1, this.playerSlotIndex + 27, this.playerSlotIndex + 36, false);

        if (index >= this.playerSlotIndex + 27 && index < this.playerSlotIndex + 36)
            return mergeItemStack(itemstack1, this.playerSlotIndex, this.playerSlotIndex + 27, false);
        return true;
    }

    /**
     * @return
     *      <br>0: すべて挿入できる
     *      <br>other: n個挿入できる
     */
    protected int canMergeItemStack(ItemStack itemStackIn, int startIndex, int endIndex, boolean reverseDirection) {
        int stackSize = itemStackIn.getCount();
        int ptr = !reverseDirection ? startIndex : endIndex - 1;

        Slot slot;
        ItemStack stack;
        if (itemStackIn.isStackable()) {
            while (stackSize > 0 && (!reverseDirection && ptr < endIndex || reverseDirection && ptr >= startIndex)) {
                slot = inventorySlots.get(ptr);
                stack = slot.getStack();

                if (!stack.isEmpty() && stack.isItemEqual(itemStackIn)
//                        && (!itemStackIn.getHasSubtypes() || itemStackIn.getMetadata() == stack.getMetadata())
//                        && ItemStack.areItemStackTagsEqual(itemStackIn, stack)
                ) {
                    int cnt = stack.getCount() + stackSize;
                    if (cnt <= itemStackIn.getMaxStackSize()) {
                        return 0;
                    }

                    if (stack.getCount() < itemStackIn.getMaxStackSize()) {
                        stackSize -= itemStackIn.getMaxStackSize() - stack.getCount();
                    }
                }

                ptr += !reverseDirection ? 1 : -1;
            }
        }

        if (stackSize > 0) {
            ptr = !reverseDirection ? startIndex : endIndex - 1;

            while (!reverseDirection && ptr < endIndex || ptr >= startIndex) {
                slot = inventorySlots.get(ptr);
                stack = slot.getStack();
                if (stack.isEmpty()) {
                    return 0;
                }

                ptr += !reverseDirection ? 1 : -1;
            }
        }

        return stackSize;
    }

    public boolean enchantItem(EntityPlayer player, int action) {
        if (this.tileEntity instanceof IButtonProvider) {
            ((IButtonProvider) this.tileEntity).pushButton(player, action);
        }
        return true;
    }

//    public void detectAndSendChanges() {
//        for (int i = 0; i < this.inventorySlots.size(); i++) {
//            ItemStack itemstack = this.inventorySlots.get(i).getStack();
//            ItemStack itemstack1 = this.inventoryItemStacks.get(i);
//
//            if (!UtilItemStack.areStackEqual(itemstack1, itemstack)) {
//                itemstack1 = itemstack.copy();
//                this.inventoryItemStacks.set(i, itemstack1);
//
//                for (net.minecraft.inventory.IContainerListener listener : this.listeners) {
//                    listener.sendSlotContents(this, i, itemstack1);
//                }
//            }
//        }
//    }

    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileEntity);
    }

    /**
     * <pre>{@code
     * Container#detectAndSendChanges()
     * > IContainerListener#sendWindowProperty(Container, int, int)
     * > net.minecraft.client.network.NetHandlerPlayClient#handleWindowProperty(SPacketWindowProperty)
     * > Container#updateProgressBar(int, int)
     * }</pre>
     * によって鯖蔵が同期される
     */
    @Override
    public void updateProgressBar(int id, int data) {
        this.tileEntity.setField(id, data);
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (this.tileEntity != null)
            this.tileEntity.closeInventory(playerIn);
    }

    public boolean drawInventoryName() {
        return true;
    }

    public boolean drawPlayerInventoryName() {
        return true;
    }

    public abstract void setMachineInventorySlots(InventoryPlayer player);

    public abstract boolean canTransferToMachineInventory(ItemStack itemStack);

    public abstract boolean transferStackToMachineInventory(ItemStack itemStack, int index);

    public abstract boolean transferStackFromMachineInventory(ItemStack itemStack, int index);

    /**
     * inventoryplayer.getItemStack() is the holding ItemStack。
     *
     * @param dragType <ul>
     *                   <li>0: Left Click</li>
     *                   <li>1: Right Click</li>
     *                 </ul>
     * @param clickTypeIn <ul>
     *                      <li>PICKUP:       Single Click<br>
     *                        dragType: 0: Left Click, 1: Right Click<br>
     *                      </li>
     *                      <li>QUICK_MOVE:   Shift + Left Click<br>
     *                        dragType: 0: Left Click, 1: Right Click<br>
     *                      </li>
     *                      <li>SWAP:         NumKey(1~9)<br>dragType: Hotbar</li>
     *                      <li>CLONE:        Middle Click</li>
     *                      <li>THROW:        Left Click at out of Gui<br>Same as PICKUP w/ slotId -999</li>
     *                      <li>QUICK_CRAFT:  ?</li>
     *                      <li>PICKUP_ALL:   Left Click x2 ?</li>
     *                    </ul>
     * @return Old ItemStack of {@code slotId} ?
     */
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ClayiumCore.logger.info("ClickType: " + clickTypeIn.name());
        Field dragEvent = ObfuscationReflectionHelper.findField(Container.class, "field_94536_g");
        Field dragMode = ObfuscationReflectionHelper.findField(Container.class, "field_94535_f");
        Set<Slot> dragSlots = ObfuscationReflectionHelper.getPrivateValue(Container.class, this, "field_94537_h"); // I wish it is a pointer, not a copy

        ItemStack itemstack = ItemStack.EMPTY;
        InventoryPlayer inventoryplayer = player.inventory;
        try {
            if (clickTypeIn == ClickType.QUICK_CRAFT) {
                int j1 = dragEvent.getInt(this);
                dragEvent.setInt(this, getDragEvent(dragType));

                if ((j1 != 1 || dragEvent.getInt(this) != 2) && j1 != dragEvent.getInt(this)) {
                    this.resetDrag();
                } else if (inventoryplayer.getItemStack().isEmpty()) {
                    this.resetDrag();
                } else if (dragEvent.getInt(this) == 0) {
                    dragMode.setInt(this, extractDragMode(dragType));

                    if (isValidDragMode(dragMode.getInt(this), player)) {
                        dragEvent.setInt(this, 1);
                        dragSlots.clear();
                    } else {
                        this.resetDrag();
                    }
                } else if (dragEvent.getInt(this) == 1) {
                    Slot slot7 = this.inventorySlots.get(slotId);
                    ItemStack itemstack12 = inventoryplayer.getItemStack();

                    if (slot7 != null && canAddItemToSlot(slot7, itemstack12, true) && slot7.isItemValid(itemstack12) && (dragMode.getInt(this) == 2 || itemstack12.getCount() > dragSlots.size()) && this.canDragIntoSlot(slot7)) {
                        dragSlots.add(slot7);
                    }
                } else if (dragEvent.getInt(this) == 2) {
                    if (!dragSlots.isEmpty()) {
                        ItemStack itemstack9 = inventoryplayer.getItemStack().copy();
                        int k1 = inventoryplayer.getItemStack().getCount();

                        for (Slot slot8 : dragSlots) {
                            ItemStack itemstack13 = inventoryplayer.getItemStack();

                            if (slot8 != null && canAddItemToSlot(slot8, itemstack13, true) && slot8.isItemValid(itemstack13) && (dragMode.getInt(this) == 2 || itemstack13.getCount() >= dragSlots.size()) && this.canDragIntoSlot(slot8)) {
                                ItemStack itemstack14 = itemstack9.copy();
                                int j3 = slot8.getHasStack() ? slot8.getStack().getCount() : 0;
                                computeStackSize(dragSlots, dragMode.getInt(this), itemstack14, j3);
                                int k3 = this.getUpperLimit(slot8, itemstack14);

                                if (itemstack14.getCount() > k3) {
                                    itemstack14.setCount(k3);
                                }

                                if (!(slot8 instanceof SlotMemory)) {
                                    k1 -= itemstack14.getCount() - j3;
                                }
                                slot8.putStack(itemstack14);
                            }
                        }

                        itemstack9.setCount(k1);
                        inventoryplayer.setItemStack(itemstack9);
                    }

                    this.resetDrag();
                } else {
                    this.resetDrag();
                }
            } else if (dragEvent.getInt(this) != 0) {
                this.resetDrag();
            } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
                if (slotId == -999) {
                    if (!inventoryplayer.getItemStack().isEmpty()) {
                        if (dragType == 0) {
                            player.dropItem(inventoryplayer.getItemStack(), true);
                            inventoryplayer.setItemStack(ItemStack.EMPTY);
                        }

                        if (dragType == 1) {
                            player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);
                        }
                    }
                } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                    if (slotId < 0) {
                        return ItemStack.EMPTY;
                    }

                    Slot slot5 = this.inventorySlots.get(slotId);

                    if (slot5 == null || !slot5.canTakeStack(player)) {
                        return ItemStack.EMPTY;
                    }

                    for (ItemStack itemstack7 = this.transferStackInSlot(player, slotId); !itemstack7.isEmpty() && UtilItemStack.areItemDamageTagEqual(slot5.getStack(), itemstack7); itemstack7 = this.transferStackInSlot(player, slotId)) {
                        itemstack = itemstack7.copy();
                    }
                } else {
                    if (slotId < 0) {
                        return ItemStack.EMPTY;
                    }

                    Slot slot6 = this.inventorySlots.get(slotId);

                    if (slot6 != null) {
                        ItemStack slotStack = slot6.getStack();
                        ItemStack holdStack = inventoryplayer.getItemStack();

                        if (slot6 instanceof SlotMemory) {
                            itemstack = slotStack.copy();
                        }

                        if (slotStack.isEmpty()) {
                            if (!holdStack.isEmpty() && slot6.isItemValid(holdStack)) {
                                int i3 = dragType == 0 ? holdStack.getCount() : 1;

                                if (slot6 instanceof SlotMemory) {
                                    slot6.putStack(holdStack);
                                } else {
                                    slot6.putStack(holdStack.splitStack(Math.min(i3, slot6.getItemStackLimit(holdStack))));
                                }
                            }
                        } else if (slot6.canTakeStack(player)) {
                            if (holdStack.isEmpty()) {
                                if (slotStack.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY);
                                    inventoryplayer.setItemStack(ItemStack.EMPTY);
                                } else {
                                    int l2 = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                                    inventoryplayer.setItemStack(slot6.decrStackSize(l2));

                                    if (slotStack.isEmpty()) {
                                        slot6.putStack(ItemStack.EMPTY);
                                    }

                                    slot6.onTake(player, inventoryplayer.getItemStack());
                                }
                            } else if (slot6.isItemValid(holdStack)) {
                                if (UtilItemStack.areItemDamageTagEqual(slotStack, holdStack)) {
                                    // holdStackとslotStackが同じときに、holdReduce個をslotStackに転送
                                    int holdReduce = dragType == 0 ? holdStack.getCount() : 1;

                                    holdReduce = Math.min(holdReduce, this.getUpperLimit(slot6, holdStack) - slotStack.getCount());

                                    if (slot6 instanceof SlotStorageContainer) {
                                        slot6.putStack(holdStack.splitStack(holdReduce));
                                    } else {
                                        holdStack.shrink(holdReduce);
                                        slotStack.grow(holdReduce);
                                    }
                                } else if (holdStack.getCount() <= slot6.getItemStackLimit(holdStack)) {
                                    slot6.putStack(holdStack);
                                    inventoryplayer.setItemStack(slotStack);
                                }
                            } else if (UtilItemStack.areItemDamageTagEqual(slotStack, holdStack) && holdStack.getMaxStackSize() > 1 && !slotStack.isEmpty()) {
                                int j2 = slotStack.getCount();

                                if (j2 + holdStack.getCount() <= holdStack.getMaxStackSize()) {
                                    holdStack.grow(j2);
                                    slotStack = slot6.decrStackSize(j2);

                                    if (slotStack.isEmpty()) {
                                        slot6.putStack(ItemStack.EMPTY);
                                    }

                                    slot6.onTake(player, inventoryplayer.getItemStack());
                                }
                            }
                        }

                        slot6.onSlotChanged();
                    }
                }
            } else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
                Slot slot4 = this.inventorySlots.get(slotId);
                ItemStack itemstack6 = inventoryplayer.getStackInSlot(dragType);
                ItemStack itemstack10 = slot4.getStack();

                if (!itemstack6.isEmpty() || !itemstack10.isEmpty()) {
                    if (itemstack6.isEmpty()) {
                        if (slot4.canTakeStack(player)) {
                            inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                            try { // slot4.onSwapCraft(itemstack10.getCount());
                                ObfuscationReflectionHelper.findMethod(Container.class, "func_190900_b", void.class, int.class).invoke(slot4, itemstack10.getCount());
                            } catch (InvocationTargetException e) {
                                ClayiumCore.logger.warn(e);
                            }
                            slot4.putStack(ItemStack.EMPTY);
                            slot4.onTake(player, itemstack10);
                        }
                    } else if (itemstack10.isEmpty()) {
                        if (slot4.isItemValid(itemstack6)) {
                            int l1 = slot4.getItemStackLimit(itemstack6);

                            if (itemstack6.getCount() > l1) {
                                slot4.putStack(itemstack6.splitStack(l1));
                            } else {
                                slot4.putStack(itemstack6);
                                inventoryplayer.setInventorySlotContents(dragType, ItemStack.EMPTY);
                            }
                        }
                    } else if (slot4.canTakeStack(player) && slot4.isItemValid(itemstack6)) {
                        int i2 = slot4.getItemStackLimit(itemstack6);

                        if (itemstack6.getCount() > i2) {
                            slot4.putStack(itemstack6.splitStack(i2));
                            slot4.onTake(player, itemstack10);

                            if (!inventoryplayer.addItemStackToInventory(itemstack10)) {
                                player.dropItem(itemstack10, true);
                            }
                        } else {
                            slot4.putStack(itemstack6);
                            inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                            slot4.onTake(player, itemstack10);
                        }
                    }
                }
            } else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
                Slot slot3 = this.inventorySlots.get(slotId);

                if (slot3 != null && slot3.getHasStack()) {
                    ItemStack itemstack5 = slot3.getStack().copy();
                    itemstack5.setCount(itemstack5.getMaxStackSize());
                    inventoryplayer.setItemStack(itemstack5);
                }
            } else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
                Slot slot2 = this.inventorySlots.get(slotId);

                if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player)) {
                    ItemStack itemstack4 = slot2.decrStackSize(dragType == 0 ? 1 : slot2.getStack().getCount());
                    slot2.onTake(player, itemstack4);
                    player.dropItem(itemstack4, true);
                }
            } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
                Slot slot = this.inventorySlots.get(slotId);
                ItemStack itemstack1 = inventoryplayer.getItemStack();

                if (!itemstack1.isEmpty() && (slot == null || !slot.getHasStack() || !slot.canTakeStack(player))) {
                    int i = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                    int j = dragType == 0 ? 1 : -1;

                    for (int k = 0; k < 2; ++k) {
                        for (int l = i; l >= 0 && l < this.inventorySlots.size() && itemstack1.getCount() < itemstack1.getMaxStackSize(); l += j) {
                            Slot slot1 = this.inventorySlots.get(l);

                            if (slot1.getHasStack() && canAddItemToSlot(slot1, itemstack1, true) && slot1.canTakeStack(player) && this.canMergeSlot(itemstack1, slot1)) {
                                ItemStack itemstack2 = slot1.getStack();

                                if (k != 0 || itemstack2.getCount() != itemstack2.getMaxStackSize()) {
                                    int i1 = Math.min(itemstack1.getMaxStackSize() - itemstack1.getCount(), itemstack2.getCount());
                                    ItemStack itemstack3 = slot1.decrStackSize(i1);
                                    itemstack1.grow(i1);

                                    if (itemstack3.isEmpty()) {
                                        slot1.putStack(ItemStack.EMPTY);
                                    }

                                    slot1.onTake(player, itemstack3);
                                }
                            }
                        }
                    }
                }

                this.detectAndSendChanges();
            }
        } catch (IllegalAccessException e) {
            ClayiumCore.logger.warn(e);
        }

        return itemstack;
    }

    protected int getUpperLimit(Slot slot, ItemStack holdStack) {
        return Math.min(holdStack.getMaxStackSize(), slot.getItemStackLimit(holdStack));
    }

    // Returns true when did put even if didn't complete
    protected boolean tryMergeStack(ItemStack stack, int index) {
        Slot slot = this.inventorySlots.get(index);
        ItemStack stack1 = slot.getStack();

        int maxSize = slot.getSlotStackLimit();
        if (this.tileEntity instanceof FlexibleStackLimit) {
            maxSize = ((FlexibleStackLimit) this.tileEntity).getInventoryStackLimit(index, stack);
        }
        maxSize = Math.min(maxSize, stack.getMaxStackSize());

        if (stack1.isEmpty() && slot.isItemValid(stack)) {
            if (stack.getCount() > maxSize) {
                slot.putStack(stack.splitStack(maxSize));
            } else {
                slot.putStack(stack.splitStack(stack.getCount()));
            }
            return true;
        }

        if (stack1.isStackable() && UtilItemStack.areItemDamageTagEqual(stack, stack1)) {
            int newAmount = stack.getCount() + stack1.getCount();

            if (newAmount > maxSize) {
                stack.shrink(maxSize - stack1.getCount());
                stack1.setCount(maxSize);
            } else {
                stack.setCount(0);
                stack1.setCount(newAmount);
            }
            slot.onSlotChanged();
            return true;
        }

        return false;
    }
}
