package mods.clayium.util.crafting;

import java.util.Arrays;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;


public class ShapelessRecipesWithNBT
        extends ShapelessRecipes {
    public ShapelessRecipesWithNBT(ItemStack output, ItemStack input) {
        super(output, Arrays.asList(new ItemStack[] {input}));
    }


    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack output = super.getCraftingResult(inv);
        NBTTagCompound tag = null;

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

                if (itemstack != null) {

                    boolean flag = false;
                    for (Object e : this.recipeItems) {

                        ItemStack itemstack1 = (ItemStack) e;
                        if (itemstack.getItem() == itemstack1.getItem() && (itemstack1.getItemDamage() == 32767 || itemstack.getItemDamage() == itemstack1.getItemDamage())) {
                            tag = itemstack.getTagCompound();
                        }
                    }
                }
            }
        }
        output.setTagCompound(tag);
        return output;
    }
}


