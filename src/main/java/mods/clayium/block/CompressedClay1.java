package mods.clayium.block;

import mods.clayium.core.ClayiumCore;
import mods.clayium.creativetab.TabClayium;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class CompressedClay1 extends Block {
    public CompressedClay1() {
        super(Material.CLAY);
        setUnlocalizedName("compressed_clay_1");
        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 0);
        setHardness(1F);
        setResistance(1F);
        setCreativeTab(TabClayium.tab);
        setRegistryName(ClayiumCore.ModId, "compressed_clay_1");
    }
}
