
package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ZK60AHull extends BlockTiered {
    public ZK60AHull() {
        super(Material.IRON, "zk60a_hull", 6);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
        setHardness(2F);
        setResistance(2F);
    }
}
