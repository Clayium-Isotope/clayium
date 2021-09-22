
package mods.clayium.item;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.core.ClayiumCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ClayPickaxe extends ItemPickaxe {
	public ClayPickaxe() {
		super(ToolMaterial.STONE);
		this.attackSpeed = -3f;
		setMaxDamage(500);
		setUnlocalizedName("clay_pickaxe");
		setRegistryName(ClayiumCore.ModId, "clay_pickaxe");
		setCreativeTab(ClayiumCore.tabClayium);
		setHarvestLevel("pickaxe", 1);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (state == ClayiumBlocks.clayOre.getDefaultState())
			return efficiencyOnClayOre;
		return super.getDestroySpeed(stack, state);
	}

	private float efficiencyOnClayOre = 32F;
}
