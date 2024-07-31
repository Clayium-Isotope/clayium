package mods.clayium.component.bot;

import mods.clayium.component.EnumBotResult;
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
import java.util.function.Predicate;

public class LocalBotAdvMiner extends LocalBotMiner {
    @Override
    public EnumBotResult work(IItemHandler input, IItemHandler reference, IItemHandler vault, BlockPos pos) {
        if (this.world == null) {
            return EnumBotResult.NotReady;
        }

        if (!this.restItems.isEmpty()) {
            if (this.restItems.stream()
                    .map(UtilTransfer.tryInsertItemStack(vault))
                    .anyMatch(((Predicate<ItemStack>) ItemStack::isEmpty).negate()))
            {
                return EnumBotResult.Overloading;
            }
        }

        IBlockState state = this.world.getBlockState(pos);
        boolean isFluid = FluidRegistry.lookupFluidForBlock(state.getBlock()) != null || state.getMaterial() instanceof MaterialLiquid;
        double hardness = isFluid ? 1.0 : (double)state.getBlockHardness(world, pos);
        if (hardness == -1.0 || !IFilter.match(reference.getStackInSlot(referSlotHarvest), state)) {
            return EnumBotResult.Obstacle;
        }

        long neededProgress = (long)((double) this.progressPerJob() * (0.1 + hardness));
        if (state.getBlock() != Blocks.AIR) {
            if (!this.hasEnoughProgress(neededProgress))
                return EnumBotResult.ProgressLack;

            this.declineProgress(neededProgress);
        }

        int fortune = 0;
        boolean silkTouch = false;
        if (IFilter.match(reference.getStackInSlot(referSlotFortune), state)) {
            fortune = 3;
        }

        if (IFilter.match(reference.getStackInSlot(referSlotSilktouch), state)) {
            silkTouch = true;
            fortune = 0;
        }

        List<ItemStack> items = UtilBuilder.harvestBlock(world, pos, false, silkTouch, fortune, false);
        this.restItems.addAll(UtilTransfer.tryProduceItemStacks(vault, items));

        if (!this.restItems.isEmpty()) {
            return EnumBotResult.Overloading;
        }

        return EnumBotResult.Success;
    }
}
