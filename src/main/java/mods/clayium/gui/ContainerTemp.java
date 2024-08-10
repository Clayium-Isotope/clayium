package mods.clayium.gui;

import mods.clayium.block.tile.FlexibleStackLimit;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.common.IButtonProvider;
import mods.clayium.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;

public abstract class ContainerTemp extends Container {

    protected InventoryPlayer player;
    protected IInventory tileEntity;
    public ArrayList<Slot> machineInventorySlots = new ArrayList<>();
    /**
     * これ以降は少なくとも player のスロット計36コがある必要がある
     */
    protected int playerSlotIndex;
    public int machineGuiSizeX = 176;
    public int machineGuiSizeY = 72;

    public int playerSlotOffsetX;
    public int playerSlotOffsetY;

    public ContainerTemp(InventoryPlayer player, IInventory tileEntity) {
        this.player = player;
        this.tileEntity = tileEntity;

        if (this.tileEntity != null)
            this.tileEntity.openInventory(this.player.player);

        if (earlierConstruct())
            postConstruct();
    }

    protected final void postConstruct() {
        doSomethingJustBeforeConstruct();
        initParameters(this.player);
        setMachineInventorySlots(this.player);
        addMachineInventorySlots(this.player);
        setupPlayerSlots(this.player);
    }

    protected boolean earlierConstruct() {
        return true;
    }

    protected void doSomethingJustBeforeConstruct() {}

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
                this.addSlotToContainer(new Slot(player, 9 + x + y * 9, this.playerSlotOffsetX + 8 + x * 18,
                        customYOffset + 12 + y * 18));
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

    public void setTextFieldString(EntityPlayer player, String string, int id) {}

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
     *         <br>
     *         0: すべて挿入できる
     *         <br>
     *         other: n個挿入できる
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
                // && (!itemStackIn.getHasSubtypes() || itemStackIn.getMetadata() == stack.getMetadata())
                // && ItemStack.areItemStackTagsEqual(itemStackIn, stack)
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

    // public void detectAndSendChanges() {
    // for (int i = 0; i < this.inventorySlots.size(); i++) {
    // ItemStack itemstack = this.inventorySlots.get(i).getStack();
    // ItemStack itemstack1 = this.inventoryItemStacks.get(i);
    //
    // if (!UtilItemStack.areStackEqual(itemstack1, itemstack)) {
    // itemstack1 = itemstack.copy();
    // this.inventoryItemStacks.set(i, itemstack1);
    //
    // for (net.minecraft.inventory.IContainerListener listener : this.listeners) {
    // listener.sendSlotContents(this, i, itemstack1);
    // }
    // }
    // }
    // }

    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileEntity);
    }

    /**
     * <pre>
     * {@code
     * Container#detectAndSendChanges()
     * > IContainerListener#sendWindowProperty(Container, int, int)
     * > net.minecraft.client.network.NetHandlerPlayClient#handleWindowProperty(SPacketWindowProperty)
     * > Container#updateProgressBar(int, int)
     * }
     * </pre>
     * 
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

    /**
     * コンテナに運び入れることがあるなら {@code true} にしておけばよい。
     * {@code index} を考慮して、本当に運び入れるか決定するのは {@link ContainerTemp#transferStackToMachineInventory(ItemStack, int)}
     */
    public abstract boolean canTransferToMachineInventory(ItemStack itemStack);

    public abstract boolean transferStackToMachineInventory(ItemStack itemStack, int index);

    public abstract boolean transferStackFromMachineInventory(ItemStack itemStack, int index);

    /**
     * inventoryplayer.getItemStack() is the holding ItemStack。
     *
     * @param dragType
     *                    <ul>
     *                    <li>0: Left Click</li>
     *                    <li>1: Right Click</li>
     *                    </ul>
     * @param clickTypeIn
     *                    <ul>
     *                    <li>PICKUP: Single Click<br>
     *                    dragType: 0: Left Click, 1: Right Click<br>
     *                    </li>
     *                    <li>QUICK_MOVE: Shift + Left Click<br>
     *                    dragType: 0: Left Click, 1: Right Click<br>
     *                    </li>
     *                    <li>SWAP: NumKey(1~9)<br>
     *                    dragType: Hotbar</li>
     *                    <li>CLONE: Middle Click</li>
     *                    <li>THROW: Left Click at out of Gui<br>
     *                    Same as PICKUP w/ slotId -999</li>
     *                    <li>QUICK_CRAFT: ?</li>
     *                    <li>PICKUP_ALL: Left Click x2 ?</li>
     *                    </ul>
     * @return Old ItemStack of {@code slotId} ?
     */
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack restStackInSlot = ItemStack.EMPTY;
        InventoryPlayer inventoryplayer = player.inventory;

        int dragEvent;
        try {
            dragEvent = ObfuscationReflectionHelper.getPrivateValue(Container.class, this, "field_94536_g");
        } catch (Exception | Error e) {
            ClayiumCore.logger.info(e);
            return ItemStack.EMPTY;
        }

        if (dragEvent != 0 || clickTypeIn != ClickType.PICKUP || (dragType != 0 && dragType != 1) || slotId == -999) {
            return super.slotClick(slotId, dragType, clickTypeIn, player);
        }

        Slot slotInto;
        try {
            slotInto = this.inventorySlots.get(slotId);
        } catch (IndexOutOfBoundsException e) {
            return ItemStack.EMPTY;
        }
        if (slotInto == null) {
            return ItemStack.EMPTY;
        }

        ItemStack slotStack = slotInto.getStack();
        ItemStack holdStack = inventoryplayer.getItemStack();

        if (slotInto instanceof SlotMemory) {
            restStackInSlot = slotStack.copy();
        }

        if (slotStack.isEmpty()) {
            if (!holdStack.isEmpty() && slotInto.isItemValid(holdStack)) {
                int i3 = dragType == 0 ? holdStack.getCount() : 1;

                if (slotInto instanceof SlotMemory) {
                    slotInto.putStack(holdStack);
                } else {
                    ItemStack stack = holdStack.splitStack(Math.min(i3, slotInto.getItemStackLimit(holdStack)));
                    slotInto.putStack(stack);
                }
            }

            slotInto.onSlotChanged();
            return restStackInSlot;
        }

        if (!slotInto.canTakeStack(player)) {
            slotInto.onSlotChanged();
            return restStackInSlot;
        }

        if (holdStack.isEmpty()) {
            if (slotStack.isEmpty()) {
                slotInto.putStack(ItemStack.EMPTY);
                inventoryplayer.setItemStack(ItemStack.EMPTY);
            } else {
                int l2 = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                inventoryplayer.setItemStack(slotInto.decrStackSize(l2));

                if (slotInto.getStack().isEmpty()) {
                    slotInto.putStack(ItemStack.EMPTY);
                }

                slotInto.onTake(player, inventoryplayer.getItemStack());
            }

            slotInto.onSlotChanged();
            return restStackInSlot;
        }

        if (slotInto.isItemValid(holdStack)) {
            if (UtilItemStack.areItemDamageTagEqual(slotStack, holdStack)) {
                // holdStackとslotStackが同じときに、holdReduce個をslotStackに転送
                int holdReduce = dragType == 0 ? holdStack.getCount() : 1;

                holdReduce = Math.min(holdReduce,
                        this.getUpperLimit(slotInto, holdStack) - slotStack.getCount());

                if (slotInto instanceof SlotStorageContainer) {
                    slotInto.putStack(holdStack.splitStack(holdReduce));
                } else {
                    holdStack.shrink(holdReduce);
                    slotStack.grow(holdReduce);
                }
            } else if (holdStack.getCount() <= slotInto.getItemStackLimit(holdStack)) {
                slotInto.putStack(holdStack);
                inventoryplayer.setItemStack(slotStack);
            }

            slotInto.onSlotChanged();
            return restStackInSlot;
        }

        if (!ItemHandlerHelper.canItemStacksStackRelaxed(slotStack, holdStack)) {
            slotInto.onSlotChanged();
            return restStackInSlot;
        }

        int j2 = slotStack.getCount();
        if (j2 + holdStack.getCount() <= holdStack.getMaxStackSize()) {
            holdStack.grow(j2);
            slotStack = slotInto.decrStackSize(j2);

            if (slotStack.isEmpty()) {
                slotInto.putStack(ItemStack.EMPTY);
            }

            slotInto.onTake(player, inventoryplayer.getItemStack());
        }

        slotInto.onSlotChanged();
        return restStackInSlot;
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
