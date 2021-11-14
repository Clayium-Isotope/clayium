package mods.clayium.gui.container;

import mods.clayium.block.tile.TileChemicalMetalSeparator;
import mods.clayium.block.tile.TileClayMachines;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotWithTexture;
import mods.clayium.item.CMaterials;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerChemicalMetalSeparator extends ContainerClayMachines {
    public ContainerChemicalMetalSeparator(InventoryPlayer player, TileChemicalMetalSeparator tile, Block block) {
        super(player, (TileClayMachines) tile, block);
    }

    protected void initParameters(InventoryPlayer player) {
        this.machineGuiSizeY = 96;
        super.initParameters(player);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        addMachineSlotToContainer((Slot) new SlotWithTexture(this.tile, 0, 25, 44, RectangleTexture.LargeSlotTexture));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                addMachineSlotToContainer((Slot) new SlotFurnace(player.player, this.tile, i * 4 + j + 1, 85 + 18 * j, 17 + 18 * i));
            }
        }
    }


    public boolean canTransferToMachineInventory(ItemStack itemstack1) {
        return itemstack1.isItemEqual(CMaterials.get(CMaterials.IND_CLAY, CMaterials.DUST));
    }
}
