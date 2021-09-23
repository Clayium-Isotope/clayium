package mods.clayium.block;

import mods.clayium.block.common.BlockTiered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class MachineHull extends BlockTiered {
    public MachineHull(int meta) {
        super(Material.IRON, "machine_hull_", meta);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
        setHardness(2F);
        setResistance(5F);
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return super.getTier(itemStack) + 1;
    }

    @Override
    public int getTier(IBlockAccess access, BlockPos posIn) {
        return super.getTier(access, posIn) + 1;
    }
}
