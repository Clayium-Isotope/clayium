
package mods.clayium.block;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block;

import mods.clayium.creativetab.TabClayium;
import mods.clayium.ElementsClayiumMod;

import java.util.Random;

@ElementsClayiumMod.ModElement.Tag
public class BlockClayOre extends ElementsClayiumMod.ModElement {
	@GameRegistry.ObjectHolder("clayium:clay_ore")
	public static final Block block = null;
	public BlockClayOre(ElementsClayiumMod instance) {
		super(instance, 24);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new BlockCustom().setRegistryName("clay_ore"));
		elements.items.add(() -> new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation("clayium:clay_ore", "inventory"));
	}

	@Override
	public void generateWorld(Random random, int chunkX, int chunkZ, World world, int dimID, IChunkGenerator cg, IChunkProvider cp) {
		if (dimID != 0) return;

		for (int i = 0; i < clayOreVeinNumber; i++) {
			int x = chunkX + random.nextInt(16);
			int y = clayOreVeinMinY + random.nextInt(clayOreVeinMaxY - clayOreVeinMinY);
			int z = chunkZ + random.nextInt(16);
			(new WorldGenMinable(block.getDefaultState(), clayOreVeinSize)).generate(world, random, new BlockPos(x, y, z));

			if (!generateDenseClayOreVein) continue;

			(new WorldGenMinable(BlockDenseClayOre.block.getDefaultState(), denseClayOreVeinSize, new com.google.common.base.Predicate<IBlockState>() {
				public boolean apply(IBlockState blockAt) {
					boolean blockCriteria = false;
					IBlockState require;
					if (blockAt.getBlock() == block.getDefaultState().getBlock())
						blockCriteria = true;
					return blockCriteria;
				}
			})).generate(world, random, new BlockPos(x, y, z));

			if(world.getBlockState(new BlockPos(x + 8, y, z + 8)).getBlock() == BlockDenseClayOre.block.getDefaultState().getBlock() && random.nextInt(2) == 0)
				world.setBlockState(new BlockPos(x + 8, y, z + 8), BlockLargeDenseClayOre.block.getDefaultState(), 2);
		}
	}
	public static class BlockCustom extends Block {
		public BlockCustom() {
			super(Material.ROCK);
			setUnlocalizedName("clay_ore");
			setSoundType(SoundType.STONE);
			setHarvestLevel("pickaxe", 1);
			setHardness(3F);
			setResistance(5F);
			setLightLevel(0F);
			setLightOpacity(255);
			setCreativeTab(TabClayium.tab);
		}

		@Override
		public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
			drops.add(new ItemStack(Items.CLAY_BALL, 3));
		}
	}

	static int clayOreVeinNumber = 8;
    static int clayOreVeinSize = 24;
    static int clayOreVeinMinY = 24;
    static int clayOreVeinMaxY = 88;
    static boolean generateDenseClayOreVein = true;
    static int denseClayOreVeinSize = 10;
}
