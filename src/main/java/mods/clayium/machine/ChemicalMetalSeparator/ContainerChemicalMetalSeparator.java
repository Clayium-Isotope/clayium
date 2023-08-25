package mods.clayium.machine.ChemicalMetalSeparator;

import mods.clayium.gui.ContainerIMachine;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotEnergy;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.common.IClayEnergyConsumer;
import mods.clayium.machine.common.Machine1ToSome;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerChemicalMetalSeparator extends ContainerIMachine {
    public ContainerChemicalMetalSeparator(InventoryPlayer player, TileEntityChemicalMetalSeparator tile) {
        super(player, tile, Machine1ToSome.MATERIAL, Machine1ToSome.PRODUCT_1);
    }

    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = 96;
        super.initParameters(player);
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, 0, 25, 44, RectangleTexture.LargeSlotTexture));

        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                this.addMachineSlotToContainer(new SlotWithTexture(this.tileEntity, i * 4 + j + 1, 85 + 18 * j, 17 + 18 * i) {
                    @Override
                    public boolean isItemValid(ItemStack itemstack) {
                        return false;
                    }
                });
            }
        }

        addMachineSlotToContainer(new SlotEnergy((IClayEnergyConsumer) this.tileEntity, machineGuiSizeY));
    }

    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return itemstack1.isItemEqual(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust));
    }
}
