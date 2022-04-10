
package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class AZ91DHull extends BlockTiered {
    public AZ91DHull() {
        super(Material.IRON, "az91d_hull", 6);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 1);
        setHardness(2F);
        setResistance(2F);
    }
}
