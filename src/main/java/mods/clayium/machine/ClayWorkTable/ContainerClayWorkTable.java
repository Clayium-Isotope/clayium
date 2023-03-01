package mods.clayium.machine.ClayWorkTable;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.item.ClayiumItems;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerClayWorkTable extends ContainerTemp {
    public ContainerClayWorkTable(InventoryPlayer player, TileEntityClayWorkTable tileEntity) {
        super(player, tileEntity);
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        addMachineSlotToContainer(new SlotWithTexture(tileEntity, TileEntityClayWorkTable.ClayWorkTableSlots.MATERIAL.ordinal(), 16, 29, RectangleTexture.LargeSlotTexture));

        addMachineSlotToContainer(new SlotWithTexture(tileEntity, TileEntityClayWorkTable.ClayWorkTableSlots.TOOL.ordinal(), 80, 17) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return ClayiumItems.isWorkTableTool(stack);
            }
        });

        addMachineSlotToContainer(new SlotWithTexture(tileEntity, TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT.ordinal(), 142, 29, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });

        addMachineSlotToContainer(new SlotWithTexture(tileEntity, TileEntityClayWorkTable.ClayWorkTableSlots.CHANGE.ordinal(), 142, 54) {
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
    public boolean transferStackToMachineInventory(ItemStack itemStack, int index) {
        if (ClayiumItems.isWorkTableTool(itemStack)) {
            return this.tryMergeStack(itemStack, TileEntityClayWorkTable.ClayWorkTableSlots.TOOL.ordinal());
        }

        return this.tryMergeStack(itemStack, TileEntityClayWorkTable.ClayWorkTableSlots.MATERIAL.ordinal());
    }

    @Override
    public boolean transferStackFromMachineInventory(ItemStack itemStack, int index) {
        return this.transferStackToPlayerInventory(itemStack,
                index == TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT.ordinal()
                        || index == TileEntityClayWorkTable.ClayWorkTableSlots.CHANGE.ordinal());
    }
}
