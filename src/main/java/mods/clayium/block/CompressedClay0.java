package mods.clayium.block;

import mods.clayium.core.ClayiumCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class CompressedClay0 extends Block {
    public CompressedClay0() {
        super(Material.CLAY);
        setUnlocalizedName("compressed_clay_0");
        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 0);
        setHardness(1F);
        setResistance(1F);
        setCreativeTab(ClayiumCore.tabClayium);
        setRegistryName(ClayiumCore.ModId, "compressed_clay_0");
    }
}