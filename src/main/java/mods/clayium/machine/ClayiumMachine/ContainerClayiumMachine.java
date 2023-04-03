package mods.clayium.machine.ClayiumMachine;

import mods.clayium.gui.ContainerTemp;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotEnergy;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.crafting.RecipeElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerClayiumMachine extends ContainerTemp {
    protected int materialSlotIndex;
    protected int resultSlotIndex;

    public ContainerClayiumMachine(InventoryPlayer player, TileEntityClayiumMachine tile) {
        super(player, tile);
    }

    @Override
    protected void initParameters(InventoryPlayer player) {
        super.initParameters(player);

        this.materialSlotIndex = TileEntityClayiumMachine.MachineSlots.MATERIAL.ordinal();
        this.resultSlotIndex = TileEntityClayiumMachine.MachineSlots.PRODUCT.ordinal();
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return ((TileEntityClayiumMachine) tileEntity).getRecipe(itemstack1) != RecipeElement.flat();
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
    public boolean enchantItem(EntityPlayer playerIn, int id) {
        this.tileEntity.pushButton(playerIn, id);
        return true;
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, TileEntityClayiumMachine.MachineSlots.MATERIAL.ordinal(), 44, 35, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return tileEntity.isItemValidForSlot(TileEntityClayiumMachine.MachineSlots.MATERIAL.ordinal(), itemstack);
            }
        });

        addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, TileEntityClayiumMachine.MachineSlots.PRODUCT.ordinal(), 116, 35, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }
        });

        if (IClayEnergyConsumer.hasClayEnergy(this.tileEntity)) {
            addMachineSlotToContainer(new SlotEnergy(this.tileEntity, ((IClayEnergyConsumer) this.tileEntity).getEnergySlot(), machineGuiSizeY));
        }
    }
}
