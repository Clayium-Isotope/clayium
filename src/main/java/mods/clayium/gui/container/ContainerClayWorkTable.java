package mods.clayium.gui.container;

import mods.clayium.block.tile.TileClayWorkTable;
import mods.clayium.block.tile.TileClayWorkTable.ClayWorkTableSlots;
import mods.clayium.gui.container.slot.SlotClayWorkTableOutput;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.crafting.ClayWorkTableRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerClayWorkTable extends Container {
    private final TileClayWorkTable tileEntity;
    private int kneadTime, kneadedTimes, cookingMethod;
    private static final int sizeInventory = 4;

    public ContainerClayWorkTable(World world, BlockPos pos, EntityPlayer player) {
        this(player.inventory, (TileClayWorkTable) world.getTileEntity(pos));
    }

    public ContainerClayWorkTable(InventoryPlayer player, TileClayWorkTable tileEntity) {
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new Slot(tileEntity, ClayWorkTableSlots.MATERIAL.ordinal(), 17, 30));
        this.addSlotToContainer(new Slot(tileEntity, ClayWorkTableSlots.TOOL.ordinal(), 80, 17) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return ClayiumItems.isItemTool(stack);
            }
        });
        this.addSlotToContainer(new SlotClayWorkTableOutput(player.player, tileEntity, ClayWorkTableSlots.PRODUCT.ordinal(), 143, 30));
        this.addSlotToContainer(new SlotClayWorkTableOutput(player.player, tileEntity, ClayWorkTableSlots.CHANGE.ordinal(), 143, 55));

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for(int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, tileEntity);
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
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        this.tileEntity.setField(id, value);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tileEntity.isUsableByPlayer(player);
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
                    if (ClayWorkTableRecipes.instance().hasKneadingResult(itemstack1)) { // -> container[material]
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
        tileEntity.pushButton(id);
        return true;
    }
}
