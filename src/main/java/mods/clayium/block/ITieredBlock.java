package mods.clayium.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public interface ITieredBlock {
    int getTier(ItemStack paramItemStack);

    int getTier(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3);
}
