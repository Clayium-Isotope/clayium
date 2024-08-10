package mods.clayium.component.bot;

import mods.clayium.item.filter.IFilter;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class LocalBotAdvMiner extends LocalBotMiner {
    @Override
    public EnumBotResult work(IItemHandler input, IItemHandler reference, IItemHandler output, BlockPos pos) {
        if (!this.hasWorld()) {
            return EnumBotResult.NotReady;
        }
        assert this.world != null;

        if (!this.restItems.isEmpty()) {
            List<ItemStack> tried = UtilTransfer.tryProduceItemStacks(output, this.restItems);
            this.restItems.clear();
            if (tried.stream().anyMatch(stack -> !stack.isEmpty())) {
                this.restItems.addAll(tried);
                return EnumBotResult.Overloading;
            }
        }

        IBlockState state = this.world.getBlockState(pos);
        boolean isFluid = FluidRegistry.lookupFluidForBlock(state.getBlock()) != null || state.getMaterial() instanceof MaterialLiquid;
        double hardness = isFluid ? 1.0 : (double)state.getBlockHardness(this.world, pos);
        if (hardness == -1.0 || !IFilter.match(reference.getStackInSlot(referSlotHarvest), this.world, pos)) {
            return EnumBotResult.Obstacle;
        }

        long neededProgress = (long)((double) this.progressPerJob() * (0.1 + hardness));
        if (state.getBlock() == Blocks.AIR) {
            return EnumBotResult.Success;
        }

        if (!this.hasEnoughProgress(neededProgress))
            return EnumBotResult.ProgressLack;

        this.declineProgress(neededProgress);

        boolean fortune = false;
        boolean silkTouch = false;
        if (IFilter.match(reference.getStackInSlot(referSlotFortune), this.world, pos)) {
            fortune = true;
        }

        if (IFilter.match(reference.getStackInSlot(referSlotSilktouch), this.world, pos)) {
            silkTouch = true;
            fortune = false;
        }

        List<ItemStack> items = UtilBuilder.harvestBlock(this.world, pos, false, silkTouch, fortune ? 3 : 0, false);
        this.restItems.addAll(UtilTransfer.tryProduceItemStacks(output, items));

        if (!this.restItems.isEmpty()) {
            return EnumBotResult.Overloading;
        }

        return EnumBotResult.Success;
    }
}
