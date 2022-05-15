package mods.clayium.block.tile;

import net.minecraft.util.AxisAlignedBB;

public interface IAxisAlignedBBProvider {
    AxisAlignedBB getAxisAlignedBB();

    boolean hasAxisAlignedBB();

    void setAxisAlignedBBToMachine();
}
