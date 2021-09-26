package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.gui.SlotWithTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerClayCraftingTable extends Container {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public InventoryCraftResult craftResult = new InventoryCraftResult();
    private final World world;
    private final BlockPos pos;
    private final EntityPlayer player;
    private final AccessibleTile<TileEntityClayCraftingTable> tileTable;
    public AccessibleTile<TileEntityChest> tileChest = null;
    private final int resultSlot = 0;
    private final int sizeInventory = 10;
    public int machineGuiHeight;

    public ContainerClayCraftingTable(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
        world = worldIn;
        pos = posIn;
        player = playerInventory.player;
        tileTable = new AccessibleTile<>((TileEntityClayCraftingTable) worldIn.getTileEntity(posIn), 0, 3, 3, 30, 17);

        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity rawTile = worldIn.getTileEntity(posIn.offset(facing));
            if (rawTile instanceof TileEntityChest) {
                tileChest = new AccessibleTile<>((TileEntityChest) rawTile, 0, 9, 3, 8, 73);
                break;
            }
        }

        int guiY = 19 + tileTable.getHeight() * tileTable.getY();

        if (tileChest != null) {
            guiY += 5 + tileTable.getHeight() * tileTable.getY();
        }

        guiY += 5;

        machineGuiHeight = Math.max(guiY, 72);

        addSlotToContainer(new SlotCrafting(playerInventory.player, craftMatrix, craftResult, 0, 124, 35));

        for(int y = 0; y < tileTable.getHeight(); ++y) {
            for(int x = 0; x < tileTable.getWidth(); ++x) {
                addSlotToContainer(new SlotWithTexture(craftMatrix, tileTable.getStart() + x + y * tileTable.getWidth(), tileTable.getX() + x * 18, tileTable.getY() + y * 18, this));
            }
        }

        if (tileChest != null) {
            for(int y = 0; y < tileChest.getHeight(); ++y) {
                for(int x = 0; x < tileChest.getWidth(); ++x) {
                    addSlotToContainer(new SlotWithTexture(tileChest.getInventory(), tileChest.getStart() + x + y * tileChest.getWidth(), tileChest.getX() + x * 18, tileChest.getY() + y * 18, this));
                }
            }
        }

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9,  8 + x * 18, machineGuiHeight + 12 + y * 18));
            }
        }

        for(int x = 0; x < 9; ++x) {
            addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, machineGuiHeight + 70));
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        slotChangedCraftingGrid(world, player, craftMatrix, craftResult);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!world.isRemote) {
            clearContainer(playerIn, world, craftMatrix);
            tileChest = null;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileTable.getInventory().isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == resultSlot) {
                itemstack1.getItem().onCreated(itemstack1, world, playerIn);

                if (!this.mergeItemStack(itemstack1, sizeInventory, sizeInventory + 36, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= sizeInventory && index < sizeInventory + 27) {
                if (!this.mergeItemStack(itemstack1, sizeInventory + 27, sizeInventory + 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= sizeInventory + 27 && index < sizeInventory + 36) {
                if (!this.mergeItemStack(itemstack1, sizeInventory, sizeInventory + 27, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, sizeInventory, sizeInventory + 36, false)) {
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

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

            if (index == resultSlot) {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }

    public String getInventoryName() {
        return tileTable.getInventory().getDisplayName().getFormattedText();
    }
}
