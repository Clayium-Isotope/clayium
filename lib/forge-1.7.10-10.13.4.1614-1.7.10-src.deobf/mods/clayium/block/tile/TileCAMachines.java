package mods.clayium.block.tile;

import mods.clayium.block.ICAResonator;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class TileCAMachines extends TileClayMachines {
    public double getResonance() {
        double res = 1.0D;
        int range = 2;
        for (int xx = -range; xx <= range; xx++) {
            for (int yy = -range; yy <= range; yy++) {
                for (int zz = -range; zz <= range; zz++) {
                    Block block = this.worldObj.getBlock(this.xCoord + xx, this.yCoord + yy, this.zCoord + zz);
                    if (block instanceof ICAResonator) {
                        res *= ((ICAResonator) block).getResonance((IBlockAccess) this.worldObj, this.xCoord + xx, this.yCoord + yy, this.zCoord + zz);
                    }
                }
            }
        }
        return res;
    }
}
