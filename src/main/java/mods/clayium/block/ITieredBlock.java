package mods.clayium.block;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ITieredBlock {
    int getTier(ItemStack stackIn);

    int getTier(IBlockAccess access, BlockPos posIn);
}
