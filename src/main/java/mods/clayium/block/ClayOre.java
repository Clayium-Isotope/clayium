package mods.clayium.block;

import mods.clayium.block.common.ClayiumBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ClayOre extends ClayiumBlock {

    public ClayOre() {
        super(Material.ROCK, "clay_ore");
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(3F);
        setResistance(5F);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
                         int fortune) {
        drops.add(new ItemStack(Items.CLAY_BALL, 3));
    }
}
