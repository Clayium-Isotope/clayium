package mods.clayium.block;

import mods.clayium.machine.ClayContainer.TileEntityClayContainer;
import mods.clayium.util.UsedFor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@UsedFor(UsedFor.Type.Block)
public interface IClayContainerModifier {
    void modifyClayContainer(IBlockAccess world, BlockPos pos, TileEntityClayContainer tile);
}
