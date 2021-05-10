
package mods.clayium.block;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block;

import mods.clayium.creativetab.TabClayium;
import mods.clayium.ElementsClayiumMod;

@ElementsClayiumMod.ModElement.Tag
public class BlockCompressedClay7 extends ElementsClayiumMod.ModElement {
	@GameRegistry.ObjectHolder("clayium:compressed_clay_7")
	public static final Block block = null;
	public BlockCompressedClay7(ElementsClayiumMod instance) {
		super(instance, 72);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new BlockCustom().setRegistryName("compressed_clay_7"));
		elements.items.add(() -> new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation("clayium:compressed_clay_7", "inventory"));
	}
	public static class BlockCustom extends Block {
		public BlockCustom() {
			super(Material.CLAY);
			setUnlocalizedName("compressed_clay_7");
			setSoundType(SoundType.GROUND);
			setHarvestLevel("shovel", 0);
			setHardness(1F);
			setResistance(1F);
			setLightLevel(0F);
			setLightOpacity(255);
			setCreativeTab(TabClayium.tab);
		}
	}
}
