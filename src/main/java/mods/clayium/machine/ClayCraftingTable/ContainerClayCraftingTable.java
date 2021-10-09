package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.common.ContainerClayMachineTemp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerClayCraftingTable extends ContainerClayMachineTemp {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public InventoryCraftResult craftResult = new InventoryCraftResult();
    private final World world;
    private final BlockPos pos;
    private final EntityPlayer player;
    private final AccessibleTile<TileEntityClayCraftingTable> tileTable;
    public AccessibleTile<TileEntityChest> tileChest = null;
    private final int resultSlot;
    private final int machineSlot;
    public int machineGuiHeight;

    public ContainerClayCraftingTable(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
        super((IInventory) worldIn.getTileEntity(posIn));

        world = worldIn;
        pos = posIn;
        player = playerInventory.player;
        tileTable = new AccessibleTile<>((TileEntityClayCraftingTable) tileEntity, 0, 3, 3, 30, 17);

        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity rawTile = worldIn.getTileEntity(posIn.offset(facing));
            if (rawTile instanceof TileEntityChest) {
                tileChest = new AccessibleTile<>((TileEntityChest) rawTile, 0, 9, 3, 8, 75);
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

        resultSlot = inventorySlots.size();

        for(int y = 0; y < tileTable.getHeight(); ++y) {
            for(int x = 0; x < tileTable.getWidth(); ++x) {
                addSlotToContainer(new SlotWithTexture(craftMatrix, tileTable.getStart() + x + y * tileTable.getWidth(), tileTable.getX() + x * 18, tileTable.getY() + y * 18, this));
            }
        }

        machineSlot = inventorySlots.size();

        if (tileChest != null) {
            for(int y = 0; y < tileChest.getHeight(); ++y) {
                for(int x = 0; x < tileChest.getWidth(); ++x) {
                    addSlotToContainer(new SlotWithTexture(tileChest.getInventory(), tileChest.getStart() + x + y * tileChest.getWidth(), tileChest.getX() + x * 18, tileChest.getY() + y * 18, this));
                }
            }
        }

        sizeInventory = inventorySlots.size();

        setupPlayerSlots(playerInventory, machineGuiHeight);
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
    public boolean canTransferToMachineInventory(ItemStack itemStackIn) {
        return true;
    }

    @Override
    public boolean transferStackToMachineInventory(ItemStack itemStackIn) {
        return mergeItemStack(itemStackIn, machineSlot, sizeInventory, false)
                || mergeItemStack(itemStackIn, resultSlot, machineSlot, false);
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemStackIn, int index) {
        if (index == resultSlot) {
            int stackSize = canMergeItemStack(itemStackIn, machineSlot, sizeInventory, true);

            if (stackSize == 0) {
                return mergeItemStack(itemStackIn, machineSlot, sizeInventory, true);
            } else {
                ItemStack _itemStack = itemStackIn.copy();
                _itemStack.setCount(stackSize);

                if (canMergeItemStack(_itemStack, sizeInventory, sizeInventory + 36, true) == 0) {
                    mergeItemStack(itemStackIn, machineSlot, sizeInventory, true);
                    mergeItemStack(itemStackIn, sizeInventory, sizeInventory + 36, true);
                    return true;
                }
            }
            return false;
        } else {
            if (index >= resultSlot && index < machineSlot) {
                if (mergeItemStack(itemStackIn, 0, machineSlot, false)) {
                    return true;
                }
            } else if (index < machineSlot
                    && mergeItemStack(itemStackIn, machineSlot, resultSlot, false)) {
                return true;
            }

            return transferStackToPlayerInventory(itemStackIn, false);
        }
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}
