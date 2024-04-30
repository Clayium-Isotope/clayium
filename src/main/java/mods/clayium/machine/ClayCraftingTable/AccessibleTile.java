package mods.clayium.machine.ClayCraftingTable;

import net.minecraft.tileentity.TileEntity;

public class AccessibleTile<T extends TileEntity> {

    public AccessibleTile(T inventory, int start, int width, int height, int x, int y) {
        this.inventory = inventory;
        this.start = start;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public T getInventory() {
        return inventory;
    }

    public int getStart() {
        return start;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private final T inventory;
    private final int start, width, height, x, y;
}
