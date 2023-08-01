package mods.clayium.machine.ClayContainer;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class ClaySidedContainer extends ClayContainer {
    public ClaySidedContainer(Material material, Class<? extends TileEntityGeneric> teClass, String modelPath, int guiId, TierPrefix tier) {
        super(material, teClass, modelPath, guiId, tier);
    }

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return EnumFacing.HORIZONTALS;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = super.getStateFromMeta(meta);

        if (state.getValue(BlockStateClayContainer.FACING).getAxis() == EnumFacing.Axis.Y) {
            return state.withProperty(BlockStateClayContainer.FACING, EnumFacing.NORTH);
        }
        return state;
    }
}
