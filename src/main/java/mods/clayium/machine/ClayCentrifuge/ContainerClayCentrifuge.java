package mods.clayium.machine.ClayCentrifuge;

import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotEnergy;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerClayCentrifuge extends ContainerClayiumMachine {
    protected int resultSlotNum = 1;

    public ContainerClayCentrifuge(InventoryPlayer player, TileEntityClayCentrifuge tile) {
        super(player, tile);
    }

    protected void initParameters(InventoryPlayer player) {
        this.resultSlotNum = ((TileEntityClayCentrifuge)this.tileEntity).resultSlotNum;
        this.machineGuiSizeY = (this.resultSlotNum + 1) * 9 + 46;
        super.initParameters(player);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, 0, 44, 35, RectangleTexture.LargeSlotTexture));

        for(int i = 0; i < this.resultSlotNum; ++i) {
            this.addMachineSlotToContainer(new SlotFurnaceOutput(player.player, this.tileEntity, i + 1, 116, 35 + 18 * i - 9 * (this.resultSlotNum - 1)));
        }

        this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
    }
}
