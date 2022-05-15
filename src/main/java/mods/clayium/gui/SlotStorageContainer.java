package mods.clayium.gui;

import mods.clayium.block.tile.TileStorageContainer;
import mods.clayium.util.UtilItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotStorageContainer extends SlotWithTexture {
    public SlotStorageContainer(TileStorageContainer p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ITexture texture) {
        super((IInventory) p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_, texture);
    }


    public boolean isItemValid(ItemStack itemstack) {
        ItemStack inv = ((TileStorageContainer) this.inventory).containerItemStacks[0];
        ItemStack inv1 = ((TileStorageContainer) this.inventory).containerItemStacks[1];
        return ((inv == null || UtilItemStack.areTypeEqual(itemstack, inv)) && (inv1 == null || ((TileStorageContainer) this.inventory)
                .checkFilterSlot(itemstack, inv1)));
    }
}
