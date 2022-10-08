package mods.clayium.gui;

import net.minecraft.inventory.IInventory;

public class SlotMemory extends SlotWithTexture {
    public SlotMemory(IInventory inventoryIn, int indexIn, int xPos, int yPos) {
        super(inventoryIn, indexIn, xPos, yPos);
    }

    public SlotMemory(IInventory inventoryIn, int indexIn, int xPos, int yPos, ITexture texture) {
        super(inventoryIn, indexIn, xPos, yPos, texture);
    }
}
