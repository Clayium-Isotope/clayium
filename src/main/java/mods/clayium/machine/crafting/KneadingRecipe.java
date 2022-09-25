package mods.clayium.machine.crafting;

import net.minecraft.item.ItemStack;

public class KneadingRecipe extends ClayiumRecipe /* extends ArrayList<KneadingRecipeElement> */ {
    public KneadingRecipe() {
        super("ClayWorkTable");
    }

    public void addRecipe(ItemStack materialIn, int method, ItemStack resultIn, ItemStack changeIn, long time) {
        add(new KneadingRecipeElement(materialIn, method, resultIn, changeIn, time));
    }
}
