package mods.clayium.machine.ClayChemicalReactor;

import mods.clayium.gui.ContainerIMachine;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotEnergy;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine2To2;
import mods.clayium.machine.common.MachineSomeToSome;
import mods.clayium.machine.common.TileEntityGeneric;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerClayChemicalReactor<Tile extends TileEntityGeneric & MachineSomeToSome> extends ContainerIMachine {

    public ContainerClayChemicalReactor(InventoryPlayer player, Tile tile) {
        super(player, tile, 0, 2);
    }

    protected void initParameters(InventoryPlayer player) {
        super.initParameters(player);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, Machine2To2.MATERIAL_1, 32, 35,
                RectangleTexture.SmallSlotImport1Texture));
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, Machine2To2.MATERIAL_2, 50, 35,
                RectangleTexture.SmallSlotImport2Texture));
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, Machine2To2.PRODUCT_1, 110, 35,
                RectangleTexture.SmallSlotExport1Texture) {

            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }
        });
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, Machine2To2.PRODUCT_2, 128, 35,
                RectangleTexture.SmallSlotExport2Texture) {

            @Override
            public boolean isItemValid(ItemStack itemstack) {
                return false;
            }
        });
        this.addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, this.machineGuiSizeY));
    }
}
