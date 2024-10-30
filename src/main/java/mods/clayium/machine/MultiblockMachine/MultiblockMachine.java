package mods.clayium.machine.MultiblockMachine;

import com.google.common.collect.ImmutableMap;
import mods.clayium.machine.ClayContainer.BlockStateClayContainer;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.common.TileEntityGeneric;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class MultiblockMachine extends ClayiumMachine {
    public MultiblockMachine(EnumMachineKind kind, String suffix, TierPrefix tier, Supplier<? extends TileEntityGeneric> teSupplier, int guiID) {
        super(kind, suffix, tier, teSupplier, guiID);

        this.setDefaultState(this.getDefaultState().withProperty(BlockStateMultiblockMachine.IS_CONSTRUCTED, false));
    }

    /**
     * CPFF<br>
     * F: horizontal facing, P: IsPipe, C: IsConstructed
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;

        meta |= state.getValue(BlockStateMultiblockMachine.IS_CONSTRUCTED) ? 1 : 0;
        meta <<= 1;
        meta |= state.getValue(BlockStateClayContainer.IS_PIPE) ? 1 : 0;
        meta <<= 2;
        meta |= Math.max(state.getValue(BlockStateClayContainer.FACING).getHorizontalIndex(), 0);

        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(BlockStateMultiblockMachine.IS_CONSTRUCTED, (meta >> 3) == 1)
                .withProperty(BlockStateClayContainer.IS_PIPE, (meta >> 2) == 1)
                .withProperty(BlockStateClayContainer.FACING, EnumFacing.byHorizontalIndex(meta & 0b0011));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new MultiblockMachineStateContainer(this);
    }

    private static class MultiblockMachineStateContainer extends BlockStateContainer {
        public MultiblockMachineStateContainer(MultiblockMachine blockIn) {
            super(blockIn, BlockStateMultiblockMachine.getPropertyList().toArray(new IProperty[0]));
        }

        @Override
        protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
            return new BlockStateMultiblockMachine(block, properties);
        }
    }
}
