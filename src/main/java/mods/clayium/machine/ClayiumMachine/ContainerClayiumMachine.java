package mods.clayium.machine.ClayiumMachine;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotEnergy;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine1To1;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;

public class ContainerClayiumMachine extends ContainerTemp {
    /**
     * The first index of material
     */
    protected int materialSlotIndex;
    /**
     * The first index of result
     */
    protected int resultSlotIndex;
    protected int timeToCraft;
    protected int craftTime;
    protected int containEnergy;

    public ContainerClayiumMachine(InventoryPlayer player, TileEntityClayiumMachine tile) {
        super(player, tile);
    }

    @Override
    protected void initParameters(InventoryPlayer player) {
        super.initParameters(player);

        this.materialSlotIndex = Machine1To1.MATERIAL;
        this.resultSlotIndex = Machine1To1.PRODUCT;
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return true;
    }

    public boolean transferStackToMachineInventory(ItemStack itemstack1, int index) {
        return mergeItemStack(itemstack1, this.materialSlotIndex, this.resultSlotIndex, false);
    }

    public boolean transferStackFromMachineInventory(ItemStack itemstack1, int slot) {
        if (slot >= this.resultSlotIndex) {
            return transferStackToPlayerInventory(itemstack1, true);
        }
        return transferStackToPlayerInventory(itemstack1, false);
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, Machine1To1.MATERIAL, 44, 35, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return tileEntity.isItemValidForSlot(Machine1To1.MATERIAL, itemstack);
            }
        });

        addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, Machine1To1.PRODUCT, 116, 35, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }
        });

        if (IClayEnergyConsumer.hasClayEnergy(this.tileEntity))
            addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (this.timeToCraft != this.tileEntity.getField(0)) {
                listener.sendWindowProperty(this, 0, this.tileEntity.getField(0));
            }

            if (this.craftTime != this.tileEntity.getField(1)) {
                listener.sendWindowProperty(this, 1, this.tileEntity.getField(1));
            }

            if (this.containEnergy != this.tileEntity.getField(2)) {
                listener.sendWindowProperty(this, 2, this.tileEntity.getField(2));
            }
        }

        this.timeToCraft = this.tileEntity.getField(0);
        this.craftTime = this.tileEntity.getField(1);
        this.containEnergy = this.tileEntity.getField(2);
    }
}
