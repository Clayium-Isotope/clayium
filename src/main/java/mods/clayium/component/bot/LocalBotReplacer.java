package mods.clayium.component.bot;

import mods.clayium.component.EnumBotResult;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;

public class LocalBotReplacer extends LocalBotAdvMiner {
    @Override
    public EnumBotResult work(IItemHandler input, IItemHandler reference, IItemHandler output, BlockPos pos) {
        if (this.world == null) {
            return EnumBotResult.NotReady;
        }

        EnumBotResult mineResult = super.work(input, reference, output, pos);
        if (mineResult != EnumBotResult.Success) return mineResult;

        if (!this.hasEnoughProgress(1)) return EnumBotResult.ProgressLack;

        IBlockState state = this.world.getBlockState(pos);
        if (state.getBlock() != Blocks.AIR) {
            this.clearProgress();
            return EnumBotResult.Obstacle;
        }

        for (int i = 0; i < output.getSlots(); ++i) {
            ItemStack itemblock = output.extractItem(i, 1, true);
            if (!itemblock.isEmpty() && itemblock.getItem() instanceof ItemBlock) {
                if (UtilBuilder.placeBlockByItemBlock(itemblock, this.world, pos)) {
                    output.extractItem(i, 1, false);
                    this.declineProgress(1);
                    return EnumBotResult.Success;
                }
            }
        }

        this.clearProgress();
        return EnumBotResult.Incomplete;
    }
}
