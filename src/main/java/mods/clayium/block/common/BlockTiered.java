package mods.clayium.block.common;

import mods.clayium.block.itemblock.ItemBlockTiered;
import net.minecraft.block.material.MapColor;
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

    // For Colored Silicone Blocks
    public BlockTiered(Material material, String modelPath, int tier, MapColor mapColor) {
        super(material, modelPath, mapColor);
        setItemBlock(new ItemBlockTiered(this));

        this.tier = tier;
    }

    public BlockTiered(Material material, String modelPath, int meta, int tier) {
        this(material, modelPath + (modelPath.endsWith("_") ? meta : ""), tier, material.getMaterialMapColor());
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return tier;
    }

    @Override
    public int getTier(IBlockAccess access, BlockPos posIn) {
        return tier;
    }
}
