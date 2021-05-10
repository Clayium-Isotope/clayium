
package mods.clayium.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import mods.clayium.block.BlockClayOre;
import mods.clayium.creativetab.TabClayium;
import mods.clayium.ElementsClayiumMod;

import java.util.Set;
import java.util.HashMap;
import net.minecraft.block.state.IBlockState;

@ElementsClayiumMod.ModElement.Tag
public class ItemClayPickaxe extends ElementsClayiumMod.ModElement {
	@GameRegistry.ObjectHolder("clayium:clay_pickaxe")
	public static final Item block = null;
	public ItemClayPickaxe(ElementsClayiumMod instance) {
		super(instance, 29);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemPickaxe(ToolMaterial.STONE) {
			{
				this.attackSpeed = -3f;
				this.setMaxDamage(500);
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
		}.setUnlocalizedName("clay_pickaxe").setRegistryName("clay_pickaxe").setCreativeTab(TabClayium.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("clayium:clay_pickaxe", "inventory"));
	}

	private float efficiencyOnClayOre = 32F;
}
