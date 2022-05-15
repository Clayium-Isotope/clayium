package mods.clayium.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.clayium.block.tile.TileClayWorkTable;
import mods.clayium.util.crafting.ClayWorkTableRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerClayWorkTable extends Container {
    private TileClayWorkTable tileClayWorkTable;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private int lastCookingMethod;
    private int lastTimeToCook;
    private static int slotNum = 4;

    int xCoord;

    public ContainerClayWorkTable(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCorrd = z;
    }

    int yCoord;
    int zCorrd;

    public ContainerClayWorkTable(InventoryPlayer player, TileClayWorkTable tileEntityClayWorkTable) {
        this.tileClayWorkTable = tileEntityClayWorkTable;
        addSlotToContainer(new Slot((IInventory) tileEntityClayWorkTable, 0, 17, 30));
        addSlotToContainer(new Slot((IInventory) tileEntityClayWorkTable, 1, 80, 17));
        addSlotToContainer((Slot) new SlotFurnace(player.player, (IInventory) tileEntityClayWorkTable, 2, 143, 30));
        addSlotToContainer((Slot) new SlotFurnace(player.player, (IInventory) tileEntityClayWorkTable, 3, 143, 55));

        int i;
        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot((IInventory) player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; i++) {
            addSlotToContainer(new Slot((IInventory) player, i, 8 + i * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting craft) {
        super.addCraftingToCrafters(craft);
        craft.sendProgressBarUpdate(this, 0, this.tileClayWorkTable.furnaceCookTime);
        craft.sendProgressBarUpdate(this, 1, this.tileClayWorkTable.furnaceBurnTime);

        craft.sendProgressBarUpdate(this, 3, this.tileClayWorkTable.furnaceBurnTime);
        craft.sendProgressBarUpdate(this, 4, this.tileClayWorkTable.furnaceTimeToCook);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting craft = (ICrafting) this.crafters.get(i);
            if (this.lastCookTime != this.tileClayWorkTable.furnaceCookTime) {
                craft.sendProgressBarUpdate(this, 0, this.tileClayWorkTable.furnaceCookTime);
            }
            if (this.lastBurnTime != this.tileClayWorkTable.furnaceBurnTime) {
                craft.sendProgressBarUpdate(this, 1, this.tileClayWorkTable.furnaceBurnTime);
            }


            if (this.lastBurnTime != this.tileClayWorkTable.furnaceBurnTime) {
                craft.sendProgressBarUpdate(this, 3, this.tileClayWorkTable.furnaceCookingMethod);
            }
            if (this.lastBurnTime != this.tileClayWorkTable.furnaceBurnTime) {
                craft.sendProgressBarUpdate(this, 4, this.tileClayWorkTable.furnaceTimeToCook);
            }
        }
        this.lastBurnTime = this.tileClayWorkTable.furnaceBurnTime;
        this.lastCookTime = this.tileClayWorkTable.furnaceCookTime;

        this.lastCookingMethod = this.tileClayWorkTable.furnaceCookingMethod;
        this.lastTimeToCook = this.tileClayWorkTable.furnaceTimeToCook;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            this.tileClayWorkTable.furnaceCookTime = par2;
        }
        if (par1 == 1) {
            this.tileClayWorkTable.furnaceBurnTime = par2;
        }
        if (par1 == 2) ;


        if (par1 == 3) {
            this.tileClayWorkTable.furnaceCookingMethod = par2;
        }
        if (par1 == 4) {
            this.tileClayWorkTable.furnaceTimeToCook = par2;
        }
    }


    public boolean canInteractWith(EntityPlayer player) {
        return this.tileClayWorkTable.isUseableByPlayer(player);
    }


    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 == 2 || par2 == 3) {
                if (!mergeItemStack(itemstack1, slotNum, slotNum + 36, true)) {

                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (par2 >= slotNum) {
                if (ClayWorkTableRecipes.smelting().hasKneadingResult(itemstack1)) {
                    if (!mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (TileClayWorkTable.isItemTool(itemstack1)) {
                    if (!mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                } else if (par2 >= slotNum && par2 < slotNum + 27) {
                    if (!mergeItemStack(itemstack1, slotNum + 27, slotNum + 36, false)) {
                        return null;
                    }
                } else if (par2 >= slotNum + 27 && par2 < slotNum + 36 && !mergeItemStack(itemstack1, slotNum, slotNum + 27, false)) {
                    return null;
                }

            } else if (!mergeItemStack(itemstack1, slotNum, slotNum + 36, false)) {
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


    public boolean enchantItem(EntityPlayer player, int action) {
        this.tileClayWorkTable.pushButton(action);
        return true;
    }
}
