package mods.clayium.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.util.TierPrefix;

public class RawClayMachineHull extends BlockTiered {

    public RawClayMachineHull() {
        super(Material.CLAY, "raw_clay_machine_hull", TierPrefix.clay);
        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 0);
        setHardness(1F);
        setResistance(1F);
    }
}
