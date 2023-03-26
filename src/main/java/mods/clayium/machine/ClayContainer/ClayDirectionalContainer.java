package mods.clayium.machine.ClayContainer;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class ClayDirectionalContainer extends ClayContainer {
    public ClayDirectionalContainer(Material material, Class<? extends TileEntityClayContainer> teClass, String modelPath, int guiId, int tier) {
        super(material, teClass, modelPath, guiId, tier);
    }

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return EnumFacing.VALUES;
    }
}
