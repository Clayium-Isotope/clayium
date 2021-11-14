package mods.clayium.pan;

import java.util.Iterator;
import java.util.Map;

import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.ItemPatternItemStack;
import mods.clayium.util.crafting.SmeltingRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;


public class PANACFactoryFurnace
        implements IPANAdapterConversionFactory {
    public boolean match(World world, int x, int y, int z) {
        return (world.getBlock(x, y, z) == Blocks.furnace);
    }

    public IPANConversion getConversion(IPANAdapter adapter) {
        Map.Entry entry;
        ItemStack ingred = null;
        for (ItemStack p : adapter.getPatternItems()) {
            if (p != null) {
                if (ingred != null)
                    return null;
                ingred = p.copy();
            }
        }
        if (ingred == null) {
            return null;
        }
        Iterator<Map.Entry> iterator = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();


        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entry = iterator.next();
        }
        while (!func_151397_a(ingred, (ItemStack) entry.getKey()));

        ingred = (ItemStack) entry.getKey();
        ItemStack result = (ItemStack) entry.getValue();

        return new PANConversion(new IItemPattern[] {(IItemPattern) new ItemPatternItemStack(ingred)}, new ItemStack[] {result}, (SmeltingRecipe.timeSmelting * SmeltingRecipe.energySmelting));
    }


    private boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_) {
        return (p_151397_2_.getItem() == p_151397_1_.getItem() && (p_151397_2_.getItemDamage() == 32767 || p_151397_2_.getItemDamage() == p_151397_1_.getItemDamage()));
    }
}
