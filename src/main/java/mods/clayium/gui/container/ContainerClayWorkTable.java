package mods.clayium.gui.container;

import mods.clayium.block.tile.TileClayWorkTable;
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
    private TileClayWorkTable tileClayWorkTable;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private int lastCookingMethod;
    private int lastTimeToCook;
    private static int slotNum = 4;

    public ContainerClayWorkTable(World world, BlockPos pos, EntityPlayer player) {
        this(player.inventory, (TileClayWorkTable) world.getTileEntity(pos));
    }

    public ContainerClayWorkTable(InventoryPlayer player, TileClayWorkTable tileEntityClayWorkTable) {
        this.tileClayWorkTable = tileEntityClayWorkTable;
        this.addSlotToContainer(new Slot(tileEntityClayWorkTable, 0, 17, 30));
        this.addSlotToContainer(new Slot(tileEntityClayWorkTable, 1, 80, 17));
        this.addSlotToContainer(new Slot(tileEntityClayWorkTable, 2, 143, 30) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        this.addSlotToContainer(new Slot(tileEntityClayWorkTable, 3, 143, 55) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
        }
    }

    public void addListener(IContainerListener craft) {
        super.addListener(craft);
        craft.sendWindowProperty(this, 0, this.tileClayWorkTable.kneadProgress);
        craft.sendWindowProperty(this, 1, this.tileClayWorkTable.furnaceBurnTime);
        craft.sendWindowProperty(this, 3, this.tileClayWorkTable.furnaceBurnTime);
        craft.sendWindowProperty(this, 4, this.tileClayWorkTable.timeToKnead);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener craft : this.listeners) {
            if (this.lastCookTime != this.tileClayWorkTable.kneadProgress) {
                craft.sendWindowProperty(this, 0, this.tileClayWorkTable.kneadProgress);
            }

            if (this.lastBurnTime != this.tileClayWorkTable.furnaceBurnTime) {
                craft.sendWindowProperty(this, 1, this.tileClayWorkTable.furnaceBurnTime);
            }

            if (this.lastBurnTime != this.tileClayWorkTable.furnaceBurnTime) {
                craft.sendWindowProperty(this, 3, this.tileClayWorkTable.cookingMethod);
            }

            if (this.lastBurnTime != this.tileClayWorkTable.furnaceBurnTime) {
                craft.sendWindowProperty(this, 4, this.tileClayWorkTable.timeToKnead);
            }
        }

        this.lastBurnTime = this.tileClayWorkTable.furnaceBurnTime;
        this.lastCookTime = this.tileClayWorkTable.kneadProgress;
        this.lastCookingMethod = this.tileClayWorkTable.cookingMethod;
        this.lastTimeToCook = this.tileClayWorkTable.timeToKnead;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        switch (par1) {
            case 0:
                this.tileClayWorkTable.kneadProgress = par2;
                break;
            case 1:
                this.tileClayWorkTable.furnaceBurnTime = par2;
                break;
            case 2:
                break;
            case 3:
                this.tileClayWorkTable.cookingMethod = par2;
                break;
            case 4:
                this.tileClayWorkTable.timeToKnead = par2;
                break;
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        return this.tileClayWorkTable.isUsableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 != 2 && par2 != 3) {
                if (par2 >= slotNum) {
                    if (ClayWorkTableRecipes.smelting().hasKneadingResult(itemstack1)) {
                        if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                            return null;
                        }
                    } else if (TileClayWorkTable.isItemTool(itemstack1)) {
                        if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                            return null;
                        }
                    } else if (par2 >= slotNum && par2 < slotNum + 27) {
                        if (!this.mergeItemStack(itemstack1, slotNum + 27, slotNum + 36, false)) {
                            return null;
                        }
                    } else if (par2 >= slotNum + 27 && par2 < slotNum + 36 && !this.mergeItemStack(itemstack1, slotNum, slotNum + 27, false)) {
                        return null;
                    }
                } else if (!this.mergeItemStack(itemstack1, slotNum, slotNum + 36, false)) {
                    return null;
                }
            } else {
                if (!this.mergeItemStack(itemstack1, slotNum, slotNum + 36, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return null;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public boolean enchantItem(EntityPlayer player, int action) {
        this.tileClayWorkTable.pushButton(action);
        return true;
    }
}
