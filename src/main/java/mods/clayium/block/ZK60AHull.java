package mods.clayium.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.util.TierPrefix;

public class ZK60AHull extends BlockTiered {

    public ZK60AHull() {
        super(Material.IRON, "zk60a_hull", TierPrefix.precision);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
        setHardness(2F);
        setResistance(2F);
    }
}
