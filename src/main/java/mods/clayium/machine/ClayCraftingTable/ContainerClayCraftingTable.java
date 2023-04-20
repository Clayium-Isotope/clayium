package mods.clayium.machine.ClayCraftingTable;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.crafting.IRecipeContainer;

/**
 * <table border="1">
 *   <tr> <td> 1 </td> <td> 2 </td> <td> 3 </td> </tr>
 *   <tr> <td> 4 </td> <td> 5 </td> <td> 6 -> 0 </td> </tr>
 *   <tr> <td> 7 </td> <td> 8 </td> <td> 9 </td> </tr>
 * </table>
 *
 * <br>この後、player のインベントリが 10 ~ 45
 * <br>(あれば、)chest のインベントリが 46 ~ 72
 */
public class ContainerClayCraftingTable extends ContainerTemp implements IRecipeContainer {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public InventoryCraftResult craftResult = new InventoryCraftResult();
    private final AccessibleTile<TileEntityClayCraftingTable> tileTable;
    private AccessibleTile<TileEntityChest> tileChest = null;
    private static final int SLOT_HEIGHT = 16;

    public ContainerClayCraftingTable(InventoryPlayer playerIn, TileEntityClayCraftingTable tile) {
        super(playerIn, tile);

        this.tileTable = new AccessibleTile<>((TileEntityClayCraftingTable) this.tileEntity, 0, 3, 3, 30, 17);
        int guiY = 19 + this.tileTable.getHeight() * SLOT_HEIGHT + 10;

        // find chest
        for (EnumFacing facing : EnumFacing.VALUES) {
            TileEntity rawTile = this.tileTable.getInventory().getWorld().getTileEntity(this.tileTable.getInventory().getPos().offset(facing));
            if (rawTile instanceof TileEntityChest) {
                this.tileChest = new AccessibleTile<>((TileEntityChest) rawTile, 0, 9, 3, 8, 75);
                guiY += 5 + this.tileChest.getHeight() * SLOT_HEIGHT;
                break;
            }
        }

        this.machineGuiSizeY = Math.max(guiY, 72);

        postConstruct();

        // add Chest Slots
        if (this.tileChest != null) {
            for(int y = 0; y < this.tileChest.getHeight(); ++y) {
                for(int x = 0; x < this.tileChest.getWidth(); ++x) {
                    addSlotToContainer(new SlotWithTexture(this.tileChest.getInventory(), this.tileChest.getStart() + x + y * this.tileChest.getWidth(), this.tileChest.getX() + x * 18, this.tileChest.getY() + y * 18, this) {
                        @Override
                        public ItemStack getStack() {
                            return this.inventory.getStackInSlot(this.getSlotIndex());
                        }
                    });
                }
            }
        }
    }

    @Override
    protected boolean earlierConstruct() {
        return false;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        slotChangedCraftingGrid(this.tileTable.getInventory().getWorld(), this.player.player, this.craftMatrix, this.craftResult);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!this.tileTable.getInventory().getWorld().isRemote) {
            clearContainer(playerIn, this.tileTable.getInventory().getWorld(), this.craftMatrix);
            this.tileChest = null;
        }
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        addMachineSlotToContainer(new SlotCrafting(player.player, this.craftMatrix, this.craftResult, 0, 124, 35));

        for(int y = 0; y < this.tileTable.getHeight(); ++y) {
            for(int x = 0; x < this.tileTable.getWidth(); ++x) {
                addMachineSlotToContainer(new SlotWithTexture(this.craftMatrix, this.tileTable.getStart() + x + y * this.tileTable.getWidth(), this.tileTable.getX() + x * 18, this.tileTable.getY() + y * 18, this));
            }
        }
    }

    @Override
    public boolean canTransferToMachineInventory(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean transferStackToMachineInventory(ItemStack itemStack, int index) {
        // player ->
        if (index >= 10 && index < 46) {
            // マトリックスもしくはチェストに挿入する
            return mergeToMatrix(itemStack) || mergeToChest(itemStack, false);
        }

        assert this.tileChest != null : "Where did you pick up " + itemStack.toString() + " from?";

        // chest ->
        if (index >= 46 && index < 46 + (this.tileChest.getHeight() * this.tileChest.getWidth())) {
            // マトリックスもしくはプレイヤーに挿入する
            return mergeToMatrix(itemStack) || mergeToPlayer(itemStack, false);
        }

        return false;
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemStack, int index) {
        // result ->
        if (index == 0) {
            // チェストに挿入できるか確かめる（後ろから）
            int stackSize = canMergeToChest(itemStack, true);
            if (stackSize == 0) {
                return mergeToChest(itemStack, true);
            }

            ItemStack _itemStack = itemStack.splitStack(stackSize);

            // プレイヤーに挿入できるか確かめる
            return canMergeToPlayer(_itemStack, true) == 0 && mergeToPlayer(itemStack, true);
        }

        // matrix ->
        if (index >= 1 && index < 10) {
            // チェストもしくはプレイヤーに挿入する
            if (mergeToChest(itemStack, false) || mergeToPlayer(itemStack, false)) {
                return true;
            }
        }

        // たぶんここに到達することはないかも
        return transferStackToPlayerInventory(itemStack, false);
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }

    private boolean mergeToMatrix(ItemStack stack) {
        return mergeItemStack(stack, 1, 10, false);
    }

    private int canMergeToMatrix(ItemStack stack) {
        return canMergeItemStack(stack, 1, 10, false);
    }

    private boolean mergeToPlayer(ItemStack stack, boolean reversed) {
        return mergeItemStack(stack, 10, 46, reversed);
    }

    private int canMergeToPlayer(ItemStack stack, boolean reversed) {
        return canMergeItemStack(stack, 10, 46, reversed);
    }

    private boolean mergeToChest(ItemStack stack, boolean reversed) {
        if (this.tileChest == null) return false;

        return mergeItemStack(stack, 46, 46 + (this.tileChest.getHeight() * this.tileChest.getWidth()), reversed);
    }

    private int canMergeToChest(ItemStack stack, boolean reversed) {
        if (this.tileChest == null) return Integer.MAX_VALUE;

        return canMergeItemStack(stack, 46, 36 + (this.tileChest.getHeight() * this.tileChest.getWidth()), reversed);
    }

    @Override
    public InventoryCraftResult getCraftResult() {
        return this.craftResult;
    }

    @Override
    public InventoryCrafting getCraftMatrix() {
        return this.craftMatrix;
    }
}
