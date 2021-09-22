
package mods.clayium.item;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.core.ClayiumCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

public class ClayShovel extends ItemSpade {
	public ClayShovel() {
		super(ToolMaterial.WOOD);
		this.attackSpeed = -3f;
		setMaxDamage(500);
		setUnlocalizedName("clay_shovel");
		setRegistryName(ClayiumCore.ModId, "clay_shovel");
		setCreativeTab(ClayiumCore.tabClayium);
		setHarvestLevel("spade", 1);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (state.getMaterial() == Material.CLAY)
			return efficiencyOnClayBlocks;
		if (state.getBlock() == ClayiumBlocks.clayOre
				|| state.getBlock() == ClayiumBlocks.denseClayOre
				|| state.getBlock() == ClayiumBlocks.largeDenseClayOre)
			return efficiencyOnClayOre;
		return super.getDestroySpeed(stack, state);
	}

	protected float efficiencyOnClayBlocks = 32F;
	private float efficiencyOnClayOre = 12F;
}
