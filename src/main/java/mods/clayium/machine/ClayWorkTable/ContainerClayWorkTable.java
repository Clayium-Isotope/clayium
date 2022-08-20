package mods.clayium.machine.ClayWorkTable;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.ClayWorkTable.TileEntityClayWorkTable.ClayWorkTableSlots;
import mods.clayium.machine.crafting.ClayiumRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerClayWorkTable extends ContainerTemp {
    private int kneadTime, kneadedTimes, cookingMethod;

    public ContainerClayWorkTable(InventoryPlayer player, TileEntityClayWorkTable tileEntity) {
        super(player, tileEntity);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (this.kneadTime != this.tileEntity.getField(0)) {
                listener.sendWindowProperty(this, 0, this.tileEntity.getField(0));
            }

            if (this.kneadedTimes != this.tileEntity.getField(1)) {
                listener.sendWindowProperty(this, 1, this.tileEntity.getField(1));
            }

            if (this.cookingMethod != this.tileEntity.getField(2)) {
                listener.sendWindowProperty(this, 2, this.tileEntity.getField(2));
            }
        }

        this.kneadTime = this.tileEntity.getField(0);
        this.kneadedTimes = this.tileEntity.getField(1);
        this.cookingMethod = this.tileEntity.getField(2);
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        addSlotToContainer(new SlotWithTexture(tileEntity, ClayWorkTableSlots.MATERIAL.ordinal(), 16, 29, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return ClayiumRecipes.hasResult(ClayiumRecipes.clayWorkTable, stack, ItemStack.EMPTY);
            }
        });

        addSlotToContainer(new SlotWithTexture(tileEntity, ClayWorkTableSlots.TOOL.ordinal(), 80, 17) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return ClayiumItems.isWorkTableTool(stack);
            }
        });

        addSlotToContainer(new SlotWithTexture(tileEntity, ClayWorkTableSlots.PRODUCT.ordinal(), 142, 29, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });

        addSlotToContainer(new SlotWithTexture(tileEntity, ClayWorkTableSlots.CHANGE.ordinal(), 142, 54) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
    }

    @Override
    public boolean canTransferToMachineInventory(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean transferStackToMachineInventory(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemStack, int index) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // if you read there, memorize: "belongings" = "inventory" + "hotbar"
            // because, a "belongings"-like Minecraft-used word was not found by developer(t5ugu)

            // container[result, change] -> inventory
            if (index == ClayWorkTableSlots.PRODUCT.ordinal() || index == ClayWorkTableSlots.CHANGE.ordinal()) {
                if (!this.mergeItemStack(itemstack1, this.playerSlotIndex, this.playerSlotIndex + 36, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (index >= this.playerSlotIndex) { // belongings ->
                    if (ClayiumRecipes.hasResult(ClayiumRecipes.clayWorkTable,
                            itemstack1, ((TileEntityClayWorkTable) tileEntity).getStackInSlot(ClayWorkTableSlots.TOOL))
                    ) { // -> container[material]
                        if (!this.mergeItemStack(itemstack1, ClayWorkTableSlots.MATERIAL.ordinal(), ClayWorkTableSlots.MATERIAL.ordinal() + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (ClayiumItems.isWorkTableTool(itemstack1)) { // -> container[tool]
                        if (!this.mergeItemStack(itemstack1, ClayWorkTableSlots.TOOL.ordinal(), ClayWorkTableSlots.TOOL.ordinal() + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else { // belongings -> belongings
                        if (index < this.playerSlotIndex + 27) { // inventory -> hotbar
                            if (!this.mergeItemStack(itemstack1, this.playerSlotIndex + 27, this.playerSlotIndex + 36, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (index < this.playerSlotIndex + 36) { // hotbar -> inventory
                            if (!this.mergeItemStack(itemstack1, this.playerSlotIndex, this.playerSlotIndex + 27, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                } else { // container -> belongings
                    if (!this.mergeItemStack(itemstack1, this.playerSlotIndex, this.playerSlotIndex + 36, false)) {
                        return ItemStack.EMPTY;
                    }
                }
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
        }

        return itemstack;
    }
}
