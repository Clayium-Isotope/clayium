
package mods.clayium.item;

import mods.clayium.ElementsClayiumMod;
import mods.clayium.block.BlockClayOre;
import mods.clayium.creativetab.TabClayium;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Set;

@ElementsClayiumMod.ModElement.Tag
public class ItemClayPickaxe extends ElementsClayiumMod.ModElement {
	@GameRegistry.ObjectHolder("clayium:clay_pickaxe")
	public static final Item block = new ItemCustom();
	public ItemClayPickaxe(ElementsClayiumMod instance) {
		super(instance, 29);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> block);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("clayium:clay_pickaxe", "inventory"));
	}

	public static class ItemCustom extends ItemPickaxe {
		public ItemCustom() {
			super(ToolMaterial.STONE);
			this.attackSpeed = -3f;
			setMaxDamage(500);
			setUnlocalizedName("clay_pickaxe");
			setRegistryName("clay_pickaxe");
			setCreativeTab(TabClayium.tab);
		}

		public Set<String> getToolClasses(ItemStack stack) {
			HashMap<String, Integer> ret = new HashMap<String, Integer>();
			ret.put("pickaxe", 1);
			return ret.keySet();
		}

		@Override
		public float getDestroySpeed(ItemStack stack, IBlockState state) {
			if (state == BlockClayOre.block.getDefaultState())
				return efficiencyOnClayOre;
			return getDestroySpeed(stack, state);
		}

		private float efficiencyOnClayOre = 32F;
	}
}
