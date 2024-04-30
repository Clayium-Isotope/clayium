package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

import mods.clayium.machine.ClayWorkTable.KneadingMethod;
import mods.clayium.machine.ClayWorkTable.KneadingRecipe;

public class ClayWorkTableRecipe extends ClayiumRecipe /* extends ArrayList<KneadingRecipeElement> */ {

    public ClayWorkTableRecipe() {
        super("ClayWorkTable");
    }

    public void addRecipe(ItemStack materialIn, int method, ItemStack resultIn, ItemStack changeIn, long time) {
        add(new KneadingRecipe(materialIn, KneadingMethod.fromId(method), (int) time, resultIn, changeIn));
    }
}
