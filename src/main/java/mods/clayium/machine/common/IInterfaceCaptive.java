package mods.clayium.machine.common;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

/**
 * for TileEntity
 */
public interface IInterfaceCaptive {
    default boolean agreeWithSync() {
        return true;
    }

    static boolean isSyncable(TileEntity tile) {
        return tile instanceof IInterfaceCaptive && ((IInterfaceCaptive) tile).agreeWithSync();
    }

    NonNullList<ItemStack> getContainerItemStacks();
}
