package mods.clayium.util;

import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.Interface.ClayInterface.TileEntityClayInterface;
import mods.clayium.machine.Interface.IInterfaceCaptive;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UtilBuilder {
    public static boolean rotateBlockByWrench(World world, BlockPos pos, EnumFacing side) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof ClayContainer) return false;

        EnumFacing direction = EnumFacing.getFront(side.ordinal());
        EnumFacing[] axes = block.getValidRotations(world, pos);
        if (axes == null || axes.length == 0) return false;

        EnumFacing axis = axes[0];
        for (EnumFacing axis1 : axes) {
            if (axis1 == direction)
                axis = axis1;
        }
        return block.rotateBlock(world, pos, axis);
    }

    /**
     * @return := "" | Error Message
     * <br> Succeed: Empty but not {@code null}
     * <br> Problem Occurred: Error Message
     */
    @Nonnull
    public static String synchronize(IInterfaceCaptive source, TileEntityClayInterface target) {
        if (source == null || target == null) return "Unknown Signature";

        if (!target.isSyncEnabled()) return "Synchronous Parts hasn't applied";
        // TODO 登録処理
        TileEntityClayInterface.setCoreBlock(source, target);
        return "";
    }

    /**
     * @param dxyz {@code [I; DimID, X, Y, Z ]}
     */
    @Nullable
    public static TileEntity getTileFromIntArray(int[] dxyz) {
        assert dxyz.length == 4;

        return DimensionManager.getWorld(dxyz[0]).getTileEntity(new BlockPos(dxyz[1], dxyz[2], dxyz[3]));
    }

    public static int[] getIntArrayFromTile(@Nonnull TileEntity tile) {
        return new int[] { tile.getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ() };
    }
}
