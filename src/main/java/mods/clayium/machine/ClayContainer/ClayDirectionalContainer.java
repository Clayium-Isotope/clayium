package mods.clayium.machine.ClayContainer;

import mods.clayium.machine.common.TileEntityGeneric;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class ClayDirectionalContainer extends ClayContainer {
    public ClayDirectionalContainer(Material material, Supplier<? extends TileEntityGeneric> teSupplier, String modelPath, int guiId, TierPrefix tier) {
        super(material, teSupplier, modelPath, guiId, tier);
    }

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return EnumFacing.VALUES;
    }
}
