package mods.clayium.machine.AreaMiner;

import com.google.common.collect.ImmutableMap;
import mods.clayium.machine.ClayContainer.BlockStateClaySidedContainer;
import mods.clayium.machine.ClayMarker.AABBHolder;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

import java.util.Arrays;
import java.util.List;

public class BlockStateAreaMiner extends BlockStateClaySidedContainer {
//    public static final PropertyEnum<EnumAreaMinerState> STATE = PropertyEnum.create("state", EnumAreaMinerState.class);

    public BlockStateAreaMiner(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
    }

    public static List<IProperty<?>> getPropertyList() {
        return Arrays.asList(
                FACING, IS_PIPE, AABBHolder.APPEARANCE,
                ARM_UP, ARM_DOWN, ARM_NORTH, ARM_SOUTH, ARM_WEST, ARM_EAST
        );
    }
}
