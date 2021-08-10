
package mods.clayium.item;

import mods.clayium.ElementsClayiumMod;
import mods.clayium.block.BlockClayOre;
import mods.clayium.block.BlockDenseClayOre;
import mods.clayium.block.BlockLargeDenseClayOre;
import mods.clayium.creativetab.TabClayium;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Set;

@ElementsClayiumMod.ModElement.Tag
public class ItemClayShovel extends ElementsClayiumMod.ModElement {
	@GameRegistry.ObjectHolder("clayium:clay_shovel")
	public static final Item block = new ItemCustom();
	public ItemClayShovel(ElementsClayiumMod instance) {
		super(instance, 9);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> block);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("clayium:clay_shovel", "inventory"));
	}

	public static class ItemCustom extends ItemSpade {
		public ItemCustom() {
			super(ToolMaterial.WOOD);
			this.attackSpeed = -3f;
			setMaxDamage(500);
			setUnlocalizedName("clay_shovel");
			setRegistryName("clay_shovel");
			setCreativeTab(TabClayium.tab);
		}

		public Set<String> getToolClasses(ItemStack stack) {
			HashMap<String, Integer> ret = new HashMap<String, Integer>();
			ret.put("spade", 1);
			return ret.keySet();
		}

		@Override
		public float getDestroySpeed(ItemStack stack, IBlockState state) {
			if (state.getBlock().getMaterial(state) == Material.CLAY)
				return efficiencyOnClayBlocks;
			if (state.getBlock() == BlockClayOre.block.getDefaultState().getBlock()
					|| state.getBlock() == BlockDenseClayOre.block.getDefaultState().getBlock()
					|| state.getBlock() == BlockLargeDenseClayOre.block.getDefaultState().getBlock())
				return efficiencyOnClayOre;
			return getDestroySpeed(stack, state);
		}

		protected float efficiencyOnClayBlocks = 32F;
		private float efficiencyOnClayOre = 12F;
	}
}
