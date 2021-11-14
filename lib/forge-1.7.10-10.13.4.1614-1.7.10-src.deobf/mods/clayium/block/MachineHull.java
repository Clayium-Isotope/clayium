package mods.clayium.block;

import mods.clayium.core.ClayiumCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class MachineHull
        extends BlockDamaged implements ITieredBlock {
    public MachineHull(int maxMeta) {
        super(Material.iron, maxMeta);
        setBlockTextureName("clayium:machinehull");
        setBlockName("blockMachineHull").setCreativeTab(ClayiumCore.creativeTabClayium);
        setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeMetal);
        setHarvestLevel("pickaxe", 0);
    }


    public int getTier(ItemStack itemstack) {
        return itemstack.getItemDamage() + 1;
    }


    public int getTier(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) + 1;
    }
}
