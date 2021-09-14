package mods.clayium.block.common;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTiered extends ClayiumBlock implements ITieredBlock {
    private final int tier;

    public BlockTiered(Material material, String modelPath, int tier) {
        super(material, modelPath + tier);
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
}
