package mods.clayium.gui.container;

import mods.clayium.block.tile.TileClayMachines;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotResultWithTexture;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerClayChemicalReactor extends ContainerClayMachines {
    public ContainerClayChemicalReactor(InventoryPlayer player, TileClayMachines tile, Block block) {
        super(player, tile, block);
    }


    protected void initParameters(InventoryPlayer player) {
        super.initParameters(player);
        this.resultSlotIndex = 2;
    }

    public void setMachineInventorySlots(InventoryPlayer player) {
        addMachineSlotToContainer((Slot) new SlotWithTexture(this.tile, 0, 32, 35, RectangleTexture.SmallSlotImport1Texture));
        addMachineSlotToContainer((Slot) new SlotWithTexture(this.tile, 1, 50, 35, RectangleTexture.SmallSlotImport2Texture));
        addMachineSlotToContainer((Slot) new SlotResultWithTexture(this.tile, 2, 110, 35, RectangleTexture.SmallSlotExport1Texture));
        addMachineSlotToContainer((Slot) new SlotResultWithTexture(this.tile, 3, 128, 35, RectangleTexture.SmallSlotExport2Texture));
    }
}
