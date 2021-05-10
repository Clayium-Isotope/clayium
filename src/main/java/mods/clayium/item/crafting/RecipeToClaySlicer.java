
package mods.clayium.item.crafting;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;

import mods.clayium.item.ItemRawClaySlicer;
import mods.clayium.item.ItemClaySlicer;
import mods.clayium.ElementsClayiumMod;

@ElementsClayiumMod.ModElement.Tag
public class RecipeToClaySlicer extends ElementsClayiumMod.ModElement {
	public RecipeToClaySlicer(ElementsClayiumMod instance) {
		super(instance, 23);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		GameRegistry.addSmelting(new ItemStack(ItemRawClaySlicer.block, (int) (1)), new ItemStack(ItemClaySlicer.block, (int) (1)), 1F);
	}
}
