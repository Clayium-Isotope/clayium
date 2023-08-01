package mods.clayium.block.common;

import mods.clayium.block.itemblock.ItemBlockTiered;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTiered extends ClayiumBlock implements ITieredBlock {
    private final TierPrefix tier;

    public BlockTiered(Material material, String modelPath, TierPrefix tier) {
        this(material, modelPath, tier.meta(), tier);
    }

    // For Colored Silicone Blocks
    public BlockTiered(Material material, String modelPath, TierPrefix tier, MapColor mapColor) {
        super(material, modelPath, mapColor);

        this.tier = tier;
    }

    public BlockTiered(Material material, String modelPath, int meta, TierPrefix tier) {
        this(material, modelPath + (modelPath.endsWith("_") ? meta : ""), tier, material.getMaterialMapColor());
    }

    @Override
    public TierPrefix getTier(ItemStack itemStack) {
        return tier;
    }

    @Override
    public TierPrefix getTier(IBlockAccess access, BlockPos posIn) {
        return tier;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTiered(this);
    }
}
