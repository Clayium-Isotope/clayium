package mods.clayium.machine.common;

import mods.clayium.util.UsedFor;
import mods.clayium.util.UtilTransfer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

@UsedFor(UsedFor.Type.TileEntity)
public interface Machine2To1 extends MachineSomeToSome {

    int MATERIAL_1 = 0;
    int MATERIAL_2 = 1;
    int PRODUCT = 2;

    @Override
    default int getResultSlotCount() {
        return 1;
    }

    static IItemHandler createInput(TileEntity te) {
        return UtilTransfer.getItemHandler(te, null, new int[] { MATERIAL_1, MATERIAL_2 });
    }

    static IItemHandler createOutput(TileEntity te) {
        return UtilTransfer.getItemHandler(te, null, new int[] { PRODUCT });
    }
}
