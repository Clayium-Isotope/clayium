package mods.clayium.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import mods.clayium.block.common.ClayiumBlock;

public class LargeDenseClayOre extends ClayiumBlock {

    public LargeDenseClayOre() {
        super(Material.ROCK, "large_dense_clay_ore");
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
        setHardness(3F);
        setResistance(5F);
    }
}
