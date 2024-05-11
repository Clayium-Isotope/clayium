package mods.clayium.machine.ClayAssembler;

import mods.clayium.gui.ContainerIMachine;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotEnergy;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine2To1;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerClayAssembler extends ContainerIMachine {

    public ContainerClayAssembler(InventoryPlayer player, Machine2To1 tile) {
        super(player, tile, Machine2To1.MATERIAL_1, Machine2To1.PRODUCT);
    }

    @Override
    public void setMachineInventorySlots(InventoryPlayer player) {
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, Machine2To1.MATERIAL_1, 32, 35,
                RectangleTexture.SmallSlotImport1Texture));

        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, Machine2To1.MATERIAL_2, 50, 35,
                RectangleTexture.SmallSlotImport2Texture));

        this.addMachineSlotToContainer(
                new SlotWithTexture(this.tileEntity, Machine2To1.PRODUCT, 116, 35, RectangleTexture.LargeSlotTexture) {

                    @Override
                    public boolean isItemValid(ItemStack itemstack) {
                        return false;
                    }
                });

        if (IClayEnergyConsumer.hasClayEnergy(this.tileEntity))
            this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
    }
}
