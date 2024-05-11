package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.itemblock.ItemBlockCompressedClay;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class CompressedClay extends BlockTiered {

    public CompressedClay(int meta) {
        super(Material.CLAY, "compressed_clay_", meta, TierPrefix.get(meta + 1));
        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 0);
        setHardness(1F);
        setResistance(1F);
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockCompressedClay(this);
    }
}
