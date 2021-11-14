package mods.clayium.gui.container;

import mods.clayium.block.tile.Inventories;
import net.minecraft.inventory.IInventory;


public class InventoriesClayCraftingTable
        extends Inventories {
    public int[] starts;
    public int[] widths;
    public int[] heights;

    public InventoriesClayCraftingTable(IInventory[] inventories, int[] starts, int[] widths, int[] heights, int[] xs, int[] ys, int resultX, int resultY) {
        super(inventories);
        this.starts = starts;
        this.widths = widths;
        this.heights = heights;
        this.xs = xs;
        this.ys = ys;
        this.resultX = resultX;
        this.resultY = resultY;
    }

    public int[] xs;
    public int[] ys;
    public int resultX;
    public int resultY;
}
