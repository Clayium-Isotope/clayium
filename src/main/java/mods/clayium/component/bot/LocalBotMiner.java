package mods.clayium.component.bot;

import mods.clayium.component.Stockholder;
import mods.clayium.item.filter.IFilter;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilNBT;
import mods.clayium.util.UtilTransfer;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class LocalBotMiner implements LocalBot {
    protected final NonNullList<ItemStack> restItems = NonNullList.create();
    /**
     * Stockholder への参照変数
     */
    @Nullable protected Stockholder progressAccess = null;
    @Nullable protected World world = null;

    @Override
    public Stockholder progress() {
        return this.progressAccess;
    }

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

        long neededProgress = (long)((double) progressPerJob * (0.1 + hardness));
        if (state.getBlock() != Blocks.AIR) {
            if (!this.hasEnoughProgress(neededProgress))
                return EnumBotResult.ProgressLack;

            this.declineProgress(neededProgress);
        }

        List<ItemStack> items = UtilBuilder.harvestBlock(this.world, pos, false, false, 0, false);
        this.restItems.addAll(UtilTransfer.tryProduceItemStacks(output, items));

        if (!this.restItems.isEmpty()) {
            return EnumBotResult.Overloading;
        }

        return EnumBotResult.Success;
    }

    @Override
    public void setWorld(@Nullable World world) {
        this.world = world;
    }

    @Override
    public boolean hasWorld() {
        return this.world != null;
    }

    @Override
    public void setProgressAccess(GeneralBot<?> other) {
        this.progressAccess = other.progress();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        ItemStackHelper.saveAllItems(tag, this.restItems, false);
        UtilNBT.addWorldToTag(tag, this.world);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        ItemStackHelper.loadAllItems(nbt, this.restItems);
        this.setWorld(UtilNBT.getWorldFromTag(nbt));
    }
}
