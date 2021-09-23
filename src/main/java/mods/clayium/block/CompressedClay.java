package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.itemblock.ItemBlockCompressedClay;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class CompressedClay extends BlockTiered {
    public CompressedClay(int meta) {
        super(Material.CLAY, "compressed_clay_", meta);
        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 0);
        setHardness(1F);
        setResistance(1F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return new ItemBlockCompressedClay(this);
    }
}
