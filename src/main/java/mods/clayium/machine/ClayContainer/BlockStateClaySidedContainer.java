package mods.clayium.machine.ClayContainer;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public class BlockStateClaySidedContainer extends BlockStateClayDirectionalContainer {
    protected BlockStateClaySidedContainer(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
    }
}
