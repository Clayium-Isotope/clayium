package mods.clayium.block.common;

import mods.clayium.util.UsedFor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@UsedFor(UsedFor.Type.Block)
public interface ITieredBlock {
    int getTier(ItemStack stackIn);

    int getTier(IBlockAccess access, BlockPos posIn);
}
