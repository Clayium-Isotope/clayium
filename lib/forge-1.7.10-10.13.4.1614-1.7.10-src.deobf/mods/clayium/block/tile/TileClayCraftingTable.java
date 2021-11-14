package mods.clayium.block.tile;


public class TileClayCraftingTable
        extends TileClayContainerTiered {
    protected void initParams() {
        this.containerItemStacks = new net.minecraft.item.ItemStack[9];
        this.slotsDrop = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
    }

    public void openInventory() {}

    public void closeInventory() {}
}
