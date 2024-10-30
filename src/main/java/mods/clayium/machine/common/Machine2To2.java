package mods.clayium.machine.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

public interface Machine2To2 extends MachineSomeToSome {

    int MATERIAL_1 = 0;
    int MATERIAL_2 = 1;
    int PRODUCT_1 = 2;
    int PRODUCT_2 = 3;

    @Override
    default int getResultSlotCount() {
        return 2;
    }

    static IItemHandler createInput(TileEntity te) {
        return IMachine.createItemHandler(te, MATERIAL_1, MATERIAL_2);
    }

    static IItemHandler createOutput(TileEntity te) {
        return IMachine.createItemHandler(te, PRODUCT_1, PRODUCT_2);
    }
}
