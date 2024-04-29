package mods.clayium.machine.ClayContainer;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

import com.google.common.collect.ImmutableMap;

public class BlockStateClaySidedContainer extends BlockStateClayDirectionalContainer {

    protected BlockStateClaySidedContainer(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
    }
}
