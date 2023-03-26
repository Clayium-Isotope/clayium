package mods.clayium.machine.ClayContainer;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public class BlockStateClayDirectionalContainer extends BlockStateClayContainer {
    protected BlockStateClayDirectionalContainer(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
    }
}
