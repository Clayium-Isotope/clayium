package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class MachineHull extends BlockTiered {

    public MachineHull(int meta) {
        super(Material.IRON, "machine_hull_", meta, TierPrefix.get(meta + 1));

        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
        setHardness(2F);
        setResistance(5F);
    }
}
