
package mods.clayium.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;

import mods.clayium.creativetab.TabClayium;
import mods.clayium.ElementsClayiumMod;

@ElementsClayiumMod.ModElement.Tag
public class ItemClayRollingPin extends ElementsClayiumMod.ModElement {
	@GameRegistry.ObjectHolder("clayium:clay_rolling_pin")
	public static final Item block = new ItemCustom();
	public ItemClayRollingPin(ElementsClayiumMod instance) {
		super(instance, 19);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> block);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("clayium:clay_rolling_pin", "inventory"));
	}
	public static class ItemCustom extends Item {
		public ItemCustom() {
			setMaxDamage(60);
			maxStackSize = 1;
			setUnlocalizedName("clay_rolling_pin");
			setRegistryName("clay_rolling_pin");
			setCreativeTab(TabClayium.tab);
			setContainerItem(this);
		}

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getMaxItemUseDuration(ItemStack itemstack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block) {
			return 1F;
		}
	}
}
