package mods.clayium.gui.container;

import mods.clayium.block.tile.TileClayCentrifuge;
import mods.clayium.block.tile.TileClayMachines;
import mods.clayium.gui.RectangleTexture;
import mods.clayium.gui.SlotWithTexture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerClayCentrifuge extends ContainerClayMachines {
    protected int resultSlotNum = 1;

    public ContainerClayCentrifuge(InventoryPlayer player, TileClayCentrifuge tile, Block block) {
        super(player, (TileClayMachines) tile, block);
    }


    protected void initParameters(InventoryPlayer player) {
        this.resultSlotNum = ((TileClayCentrifuge) this.tile).resultSlotNum;
        this.machineGuiSizeY = (this.resultSlotNum + 1) * 9 + 46;
        super.initParameters(player);
    }


    public void setMachineInventorySlots(InventoryPlayer player) {
        addMachineSlotToContainer((Slot) new SlotWithTexture(this.tile, 0, 44, 35, RectangleTexture.LargeSlotTexture));
        for (int i = 0; i < this.resultSlotNum; i++)
            addMachineSlotToContainer((Slot) new SlotFurnace(player.player, this.tile, i + 1, 116, 35 + 18 * i - 9 * (this.resultSlotNum - 1)));
    }
}
