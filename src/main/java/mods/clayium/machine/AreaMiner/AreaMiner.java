package mods.clayium.machine.AreaMiner;

import com.google.common.collect.ImmutableMap;
import mods.clayium.gui.GuiHandler;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.ClayMarker.AABBHolder;
import mods.clayium.machine.ClayiumMachine.ClayHorizontalNoRecipeMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

// [TODO]   どうやらオリジナルでは作業中か否かで見た目が変わるみたい。
//          作業中判定のためだけに、同期+TEアクセス はしたくないので、BSを用意できればよいが。
public class AreaMiner extends ClayHorizontalNoRecipeMachine {
    public AreaMiner(TierPrefix tier, String modelPath, Supplier<TileEntityAreaMiner> teSupplier, EnumMachineKind kind) {
        super(teSupplier, kind, tier, modelPath, GuiHandler.GuiIdAreaMiner);
        setDefaultState(this.getDefaultState().withProperty(AABBHolder.APPEARANCE, AABBHolder.Appearance.NoRender));
    }

    public AreaMiner(TierPrefix tier, String modelPath) {
        this(tier, modelPath, TileEntityAreaMiner::new, EnumMachineKind.areaMiner);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new AreaMinerStateContainer(this);
    }

    private static class AreaMinerStateContainer extends BlockStateContainer {
        public AreaMinerStateContainer(ClayContainer blockIn) {
            super(blockIn, BlockStateAreaMiner.getPropertyList().toArray(new IProperty[0]));
        }

        @Override
        protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
            return new BlockStateAreaMiner(block, properties);
        }
    }
}
