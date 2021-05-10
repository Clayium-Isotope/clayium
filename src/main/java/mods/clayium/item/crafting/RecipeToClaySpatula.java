
package mods.clayium.item.crafting;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;

import mods.clayium.item.ItemRawClaySpatula;
import mods.clayium.item.ItemClaySpatula;
import mods.clayium.ElementsClayiumMod;

@ElementsClayiumMod.ModElement.Tag
public class RecipeToClaySpatula extends ElementsClayiumMod.ModElement {
	public RecipeToClaySpatula(ElementsClayiumMod instance) {
		super(instance, 22);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		GameRegistry.addSmelting(new ItemStack(ItemRawClaySpatula.block, (int) (1)), new ItemStack(ItemClaySpatula.block, (int) (1)), 1F);
	}
}
