package mods.clayium.component.bot;

import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;

public class LocalBotReplacer extends LocalBotAdvMiner {
    @Override
    public EnumBotResult work(IItemHandler input, IItemHandler reference, IItemHandler output, BlockPos pos) {
        if (!this.hasWorld()) {
            return EnumBotResult.NotReady;
        }
        assert this.world != null;

        EnumBotResult mineResult = super.work(input, reference, output, pos);
        if (mineResult != EnumBotResult.Success && mineResult != EnumBotResult.Obstacle) return mineResult;

        if (!this.hasEnoughProgress(1)) return EnumBotResult.ProgressLack;

        IBlockState state = this.world.getBlockState(pos);
        if (!state.getBlock().isReplaceable(this.world, pos)) {
            this.clearProgress();
            return EnumBotResult.Obstacle;
        }

        for (int i = 0; i < input.getSlots(); ++i) {
            ItemStack itemblock = input.extractItem(i, 1, true);
            if (!itemblock.isEmpty() && itemblock.getItem() instanceof ItemBlock) {
                ClayiumCore.logger.info(itemblock);
                if (UtilBuilder.placeBlockByItemBlock(itemblock, this.world, pos)) {
                    input.extractItem(i, 1, false);
                    this.declineProgress(1);
                    return EnumBotResult.Success;
                }
            }
        }

        this.clearProgress();
        return EnumBotResult.Incomplete;
    }
}
