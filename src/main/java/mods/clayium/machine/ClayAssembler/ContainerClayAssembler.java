package mods.clayium.machine.ClayAssembler;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerClayAssembler extends ContainerClayiumMachine {
    public ContainerClayAssembler(InventoryPlayer player, TileEntityClayAssembler tile) {
        super(player, tile);
    }

    @Override
    protected void initParameters(InventoryPlayer player) {
        super.initParameters(player);
        this.resultSlotIndex = TileEntityClayAssembler.AssemblerSlots.PRODUCT.ordinal();
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, TileEntityClayAssembler.AssemblerSlots.MATERIAL_1.ordinal(), 32, 35, RectangleTexture.SmallSlotImport1Texture));

        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, TileEntityClayAssembler.AssemblerSlots.MATERIAL_2.ordinal(), 50, 35, RectangleTexture.SmallSlotImport2Texture));

        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, TileEntityClayAssembler.AssemblerSlots.PRODUCT.ordinal(), 116, 35, RectangleTexture.LargeSlotTexture) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }
        });

        this.addMachineSlotToContainer(new Slot(this.tileEntity, TileEntityClayAssembler.AssemblerSlots.ENERGY.ordinal(), -12, machineGuiSizeY - 16) {
            @Override
            public int getSlotStackLimit() {
                return tileEntity.getClayEnergyStorageSize();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return ClayiumBlocks.compressedClay.contains(stack.getItem());
            }

            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return true;
            }
        });
    }
}
