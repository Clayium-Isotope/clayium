package mods.clayium.block;

import mods.clayium.block.common.ITieredBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ClayContainerTiered extends ClayContainer implements ITieredBlock {
    protected int tier;

    public ClayContainerTiered(Material material, Class<? extends TileEntity> teClass, String modelPath, int guiId, int tier) {
        super(material, teClass, modelPath, guiId);
        this.tier = tier;
    }

    @Override
    public int getTier(ItemStack stackIn) {
        return tier;
    }

    @Override
    public int getTier(IBlockAccess access, BlockPos posIn) {
        return tier;
    }
}
