
package mods.clayium.item.crafting;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;

import mods.clayium.item.ItemRawClayRollingPin;
import mods.clayium.item.ItemClayRollingPin;
import mods.clayium.ElementsClayiumMod;

@ElementsClayiumMod.ModElement.Tag
public class RecipeToClayRollingPin extends ElementsClayiumMod.ModElement {
	public RecipeToClayRollingPin(ElementsClayiumMod instance) {
		super(instance, 21);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		GameRegistry.addSmelting(new ItemStack(ItemRawClayRollingPin.block, (int) (1)), new ItemStack(ItemClayRollingPin.block, (int) (1)), 1F);
	}
}
