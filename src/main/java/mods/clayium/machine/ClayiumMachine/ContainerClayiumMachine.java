package mods.clayium.machine.ClayiumMachine;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.crafting.RecipeElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerClayiumMachine extends ContainerTemp {
    private long craftTime, timeToCraft, debtEnergy, containEnergy;
    private final int materialSlotIndex = 0;
    private final int resultSlotIndex = 1;

    public ContainerClayiumMachine(InventoryPlayer player, TileEntityClayiumMachine tile) {
        super(player, tile);
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return ((TileEntityClayiumMachine) tileEntity).getRecipe(itemstack1) != RecipeElement.FLAT;
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1) {
        return mergeItemStack(itemstack1, this.materialSlotIndex, this.resultSlotIndex, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        if (slot >= this.resultSlotIndex) {
            return transferStackToPlayerInventory(itemstack1, true);
        }
        return transferStackToPlayerInventory(itemstack1, false);
    }

    @Override
    public boolean enchantItem(EntityPlayer playerIn, int id) {
        tileEntity.pushButton(playerIn, id);
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (this.craftTime != this.tileEntity.getField(0)) {
                listener.sendWindowProperty(this, 0, this.tileEntity.getField(0));
            }

            if (this.timeToCraft != this.tileEntity.getField(1)) {
                listener.sendWindowProperty(this, 1, this.tileEntity.getField(1));
            }

            if (this.debtEnergy != this.tileEntity.getField(2)) {
                listener.sendWindowProperty(this, 2, this.tileEntity.getField(2));
            }

            if (this.containEnergy != this.tileEntity.getField(3)) {
                listener.sendWindowProperty(this, 3, this.tileEntity.getField(3));
            }
        }

        this.craftTime = this.tileEntity.getField(0);
        this.timeToCraft = this.tileEntity.getField(1);
        this.debtEnergy = this.tileEntity.getField(2);
        this.containEnergy = this.tileEntity.getField(3);
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        addSlotToContainer(new SlotWithTexture(tileEntity, 0, 44, 35, RectangleTexture.LargeSlotTexture));

        addSlotToContainer(new SlotWithTexture(tileEntity, 1, 116, 35, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }
        });

        addSlotToContainer(new Slot(tileEntity, 2, -12, 72 - 16) {
            @Override
            public int getSlotStackLimit() {
                assert tileEntity instanceof TileEntityClayiumMachine;
                return tileEntity.getClayEnergyStorageSize();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return ClayiumBlocks.compressedClay.contains(stack.getItem());
            }

            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return false;
            }
        });
    }
}
