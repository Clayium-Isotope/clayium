package mods.clayium.block.common;

import mods.clayium.block.itemblock.ItemBlockTiered;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTiered extends ClayiumBlock implements ITieredBlock {
    private final int tier;

    public BlockTiered(Material material, String modelPath, int tier) {
        this(material, modelPath, tier, tier);
    }

    // For Colored Silicone Blocks
    public BlockTiered(Material material, String modelPath, int tier, MapColor mapColor) {
        super(material, modelPath, mapColor);

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

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTiered(this);
    }
}
