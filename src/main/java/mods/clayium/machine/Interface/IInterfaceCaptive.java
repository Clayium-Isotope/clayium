package mods.clayium.machine.Interface;

import mods.clayium.util.UsedFor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@UsedFor(UsedFor.Type.TileEntity)
public interface IInterfaceCaptive {

    IInterfaceCaptive NONE = new EmptyCaptive();

    boolean acceptInterfaceSync();

    /**
     * 引数がTileEntityであることを保証するのは冗長
     */
    static boolean isSyncable(@Nullable Object tile) {
        return tile instanceof IInterfaceCaptive && ((IInterfaceCaptive) tile).acceptInterfaceSync();
    }

    @Deprecated
    static boolean isExistingCaptive(IInterfaceCaptive core) {
        return core != NONE;
    }

    World getWorld();

    BlockPos getPos();

    void markDirty();

    class EmptyCaptive implements IInterfaceCaptive {

        EmptyCaptive() {}

        @Override
        public boolean acceptInterfaceSync() {
            return false;
        }

        @Override
        public void markDirty() {}

        @Override
        public World getWorld() {
            throw new UnsupportedOperationException();
        }

        @Override
        public BlockPos getPos() {
            throw new UnsupportedOperationException();
        }
    }
}
