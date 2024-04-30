package mods.clayium.machine.ClayCentrifuge;

import net.minecraft.entity.player.InventoryPlayer;

import mods.clayium.gui.ContainerIMachine;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotEnergy;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine1ToSome;

public class ContainerClayCentrifuge extends ContainerIMachine {

    protected int resultSlotNum;

    public ContainerClayCentrifuge(InventoryPlayer player, Machine1ToSome tile) {
        super(player, tile, Machine1ToSome.MATERIAL, Machine1ToSome.PRODUCT_1);
    }

    protected void initParameters(InventoryPlayer player) {
        this.resultSlotNum = ((Machine1ToSome) this.tileEntity).getResultSlotCount();
        this.machineGuiSizeY = (this.resultSlotNum + 1) * 9 + 46;
        super.initParameters(player);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        this.addMachineSlotToContainer(
                new SlotWithTexture(this.tileEntity, 0, 44, 35, RectangleTexture.LargeSlotTexture));

        for (int i = 0; i < this.resultSlotNum; ++i) {
            this.addMachineSlotToContainer(
                    new SlotWithTexture(this.tileEntity, i + 1, 116, 35 + 18 * i - 9 * (this.resultSlotNum - 1)));
        }

        if (IClayEnergyConsumer.hasClayEnergy(this.tileEntity))
            this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
    }
}
