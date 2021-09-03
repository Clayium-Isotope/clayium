
package mods.clayium.block;

import mods.clayium.core.ClayiumCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class LargeDenseClayOre extends Block {
	public LargeDenseClayOre() {
		super(Material.ROCK);
		setUnlocalizedName("large_dense_clay_ore");
		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 1);
		setHardness(3F);
		setResistance(5F);
		setRegistryName(ClayiumCore.ModId, "large_dense_clay_ore");
		setCreativeTab(ClayiumCore.tabClayium);
	}
}
