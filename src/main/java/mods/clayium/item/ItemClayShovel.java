
package mods.clayium.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import mods.clayium.creativetab.TabClayium;
import mods.clayium.ElementsClayiumMod;
import mods.clayium.block.BlockClayOre;
import mods.clayium.block.BlockDenseClayOre;
import mods.clayium.block.BlockLargeDenseClayOre;

import java.util.Set;
import java.util.HashMap;
import net.minecraft.block.state.IBlockState;

@ElementsClayiumMod.ModElement.Tag
public class ItemClayShovel extends ElementsClayiumMod.ModElement {
	@GameRegistry.ObjectHolder("clayium:clay_shovel")
	public static final Item block = null;
	public ItemClayShovel(ElementsClayiumMod instance) {
		super(instance, 9);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemSpade(ToolMaterial.WOOD) {
			{
				this.attackSpeed = -3f;
				this.setMaxDamage(500);
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
		}.setUnlocalizedName("clay_shovel").setRegistryName("clay_shovel").setCreativeTab(TabClayium.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("clayium:clay_shovel", "inventory"));
	}
	
    protected float efficiencyOnClayBlocks = 32F;
    private float efficiencyOnClayOre = 12F;
}
