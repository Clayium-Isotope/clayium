package mods.clayium.machine.ClayAssembler;

import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotEnergy;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.entity.player.InventoryPlayer;
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

        this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
    }
}
