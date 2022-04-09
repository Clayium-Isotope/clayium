
package mods.clayium.item;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

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
		if (state.getBlock() == ClayiumBlocks.clayOre)
			return efficiencyOnClayOre;
		return super.getDestroySpeed(stack, state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		UtilLocale.localizeTooltip(tooltip, getUnlocalizedName());
	}

	private float efficiencyOnClayOre = 32F;
}
