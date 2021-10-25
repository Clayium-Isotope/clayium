package mods.clayium.machine.ClayWorkTable;

import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.ClayWorkTable.TileEntityClayWorkTable.ClayWorkTableSlots;
import mods.clayium.machine.common.ContainerClayMachineTemp;
import mods.clayium.machine.crafting.ClayiumRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerClayWorkTable extends ContainerClayMachineTemp {
    private int kneadTime, kneadedTimes, cookingMethod;

    public ContainerClayWorkTable(InventoryPlayer player, TileEntityClayWorkTable tileEntity) {
        super(tileEntity);

        sizeInventory = 4;

        this.addSlotToContainer(new SlotWithTexture(tileEntity, ClayWorkTableSlots.MATERIAL.ordinal(), 16, 29, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return ClayiumRecipes.hasResult(ClayiumRecipes.clayWorkTableRecipe, stack, ItemStack.EMPTY);
            }
        });
        this.addSlotToContainer(new SlotWithTexture(tileEntity, ClayWorkTableSlots.TOOL.ordinal(), 80, 17) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return ClayiumItems.isItemTool(stack);
            }
        });
        this.addSlotToContainer(new SlotWithTexture(tileEntity, ClayWorkTableSlots.PRODUCT.ordinal(), 142, 29, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        this.addSlotToContainer(new SlotWithTexture(tileEntity, ClayWorkTableSlots.CHANGE.ordinal(), 142, 54) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });

        setupPlayerSlots(player);
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
                if (!this.mergeItemStack(itemstack1, sizeInventory, sizeInventory + 36, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (index >= sizeInventory) { // belongings ->
                    if (ClayiumRecipes.hasResult(ClayiumRecipes.clayWorkTableRecipe,
                            itemstack1, ((TileEntityClayWorkTable) tileEntity).getStackInSlot(ClayWorkTableSlots.TOOL))
                    ) { // -> container[material]
                        if (!this.mergeItemStack(itemstack1, ClayWorkTableSlots.MATERIAL.ordinal(), ClayWorkTableSlots.MATERIAL.ordinal() + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (ClayiumItems.isItemTool(itemstack1)) { // -> container[tool]
                        if (!this.mergeItemStack(itemstack1, ClayWorkTableSlots.TOOL.ordinal(), ClayWorkTableSlots.TOOL.ordinal() + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else { // belongings -> belongings
                        if (index < sizeInventory + 27) { // inventory -> hotbar
                            if (!this.mergeItemStack(itemstack1, sizeInventory + 27, sizeInventory + 36, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (index < sizeInventory + 36) { // hotbar -> inventory
                            if (!this.mergeItemStack(itemstack1, sizeInventory, sizeInventory + 27, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                } else { // container -> belongings
                    if (!this.mergeItemStack(itemstack1, sizeInventory, sizeInventory + 36, false)) {
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

    @Override
    public boolean enchantItem(EntityPlayer playerIn, int id) {
        ((TileEntityClayWorkTable) tileEntity).pushButton(playerIn, id);
        return true;
    }
}
