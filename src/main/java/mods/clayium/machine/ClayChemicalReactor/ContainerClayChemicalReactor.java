package mods.clayium.machine.ClayChemicalReactor;

import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotEnergy;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.ClayiumMachine.TileEntityClayiumMachine;
import mods.clayium.machine.common.IClayEnergyConsumer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerClayChemicalReactor extends ContainerClayiumMachine {
    public ContainerClayChemicalReactor(InventoryPlayer player, TileEntityClayiumMachine tile) {
        super(player, tile);
    }

    protected void initParameters(InventoryPlayer player) {
        super.initParameters(player);
        this.resultSlotIndex = 2;
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, TileEntityClayChemicalReactor.ChemicalReactorSlots.MATERIAL_1.ordinal(), 32, 35, RectangleTexture.SmallSlotImport1Texture));
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, TileEntityClayChemicalReactor.ChemicalReactorSlots.MATERIAL_2.ordinal(), 50, 35, RectangleTexture.SmallSlotImport2Texture));
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, TileEntityClayChemicalReactor.ChemicalReactorSlots.PRODUCT_1.ordinal(), 110, 35, RectangleTexture.SmallSlotExport1Texture) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }
        });
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, TileEntityClayChemicalReactor.ChemicalReactorSlots.PRODUCT_2.ordinal(), 128, 35, RectangleTexture.SmallSlotExport2Texture) {
            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }
        });
        this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, this.machineGuiSizeY));
    }
}
