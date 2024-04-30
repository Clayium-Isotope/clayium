package mods.clayium.block.common;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import mods.clayium.util.TierPrefix;
import mods.clayium.util.UsedFor;

@UsedFor(UsedFor.Type.Block)
public interface ITieredBlock {

    TierPrefix getTier(ItemStack stackIn);

    TierPrefix getTier(IBlockAccess access, BlockPos posIn);
}
