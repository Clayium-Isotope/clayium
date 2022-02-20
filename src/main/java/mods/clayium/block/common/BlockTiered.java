package mods.clayium.block.common;

import mods.clayium.block.itemblock.ItemBlockTiered;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockTiered extends ClayiumBlock implements ITieredBlock {
    private final int tier;

    public BlockTiered(Material material, String modelPath, int tier) {
        this(material, modelPath, tier, tier);
    }

    public BlockTiered(Material material, String modelPath, int meta, int tier) {
        super(material, modelPath + (modelPath.endsWith("_") ? meta : ""));
        this.tier = tier;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return tier;
    }

    @Override
    public int getTier(IBlockAccess access, BlockPos posIn) {
        return tier;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return new ItemBlockTiered(this);
    }
}
