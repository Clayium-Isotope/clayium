package mods.clayium.machine.common;

import mods.clayium.util.UsedFor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

@UsedFor(UsedFor.Type.TileEntity)
public interface Machine1To1 extends IMachine {

    int MATERIAL = 0;
    int PRODUCT = 1;


    static IItemHandler createInput(TileEntity te) {
        return IMachine.createItemHandler(te, MATERIAL);
    }

    static IItemHandler createOutput(TileEntity te) {
        return IMachine.createItemHandler(te, PRODUCT);
    }
}
