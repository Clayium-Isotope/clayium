package mods.clayium.util;

import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.Interface.IInterfaceCaptive;
import mods.clayium.machine.Interface.ISynchronizedInterface;
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
     *
     * @param source the container, which will be the sync source, or null for de-sync.
     */
    @Nonnull
    public static String synchronize(@Nullable IInterfaceCaptive source, ISynchronizedInterface target) {
        if (target == null) return "Unknown Signature";
        if (source == null) {
            target.setCoreBlock(null);
            return "";
        }

        if (!target.isSyncEnabled()) return "Synchronous Parts hasn't applied";
        // TODO 登録処理
        target.setCoreBlock(source);
        return "";
    }

    /**
     * Synchronize even if the interface hasn't applied Synchronous Parts.
     */
    public static void immediateSync(@Nullable IInterfaceCaptive source, ISynchronizedInterface target) {
        if (target == null) return;

        target.setCoreBlock(source);
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
