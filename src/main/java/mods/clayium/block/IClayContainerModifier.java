package mods.clayium.block;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IClayContainerModifier {
    void modifyClayContainer(IBlockAccess world, BlockPos pos, TileEntityClayContainer tile);
}
