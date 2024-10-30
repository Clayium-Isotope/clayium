package mods.clayium.machine.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

import java.util.stream.IntStream;

public interface Machine1ToSome extends MachineSomeToSome {

    int MATERIAL = 0;
    int PRODUCT_1 = 1;
    int PRODUCT_2 = 2;
    int PRODUCT_3 = 3;
    int PRODUCT_4 = 4;

    static IItemHandler createInput(TileEntity te) {
        return IMachine.createItemHandler(te, MATERIAL);
    }

    static IItemHandler createOutput(TileEntity te, int startIncl, int endExcl) {
        return IMachine.createItemHandler(te, IntStream.range(startIncl, endExcl).toArray());
    }
}
