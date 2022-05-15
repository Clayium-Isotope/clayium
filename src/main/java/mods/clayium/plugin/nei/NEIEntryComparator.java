package mods.clayium.plugin.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;

import java.util.Comparator;

public class NEIEntryComparator implements Comparator<TemplateRecipeHandler.CachedRecipe> {
    public int compare(TemplateRecipeHandler.CachedRecipe a1, TemplateRecipeHandler.CachedRecipe b1) {
        if (a1 instanceof NEITemp.INEIEntryTiered && b1 instanceof NEITemp.INEIEntryTiered) {
            int a = ((NEITemp.INEIEntryTiered) a1).getTier();
            int b = ((NEITemp.INEIEntryTiered) b1).getTier();
            if (a > b) return 1;
            if (b > a) return -1;
        }

        if (a1 instanceof NEITemp.INEIEntryEnergy && b1 instanceof NEITemp.INEIEntryEnergy) {
            long ae = ((NEITemp.INEIEntryEnergy) a1).getEnergy();
            long be = ((NEITemp.INEIEntryEnergy) b1).getEnergy();
            long at = ((NEITemp.INEIEntryEnergy) a1).getTime();
            long bt = ((NEITemp.INEIEntryEnergy) b1).getTime();
            double a = ae * at;
            double b = be * bt;
            if (a > b) return 1;
            if (b > a) return -1;
            if (ae > be) return 1;
            if (be > ae) return -1;
            if (at > bt) return 1;
            if (bt > at) return -1;
        }

        if (a1 instanceof INEIRecipeEntry && b1 instanceof INEIRecipeEntry) {
            return NEITemp.getStackComparator().compare(((INEIRecipeEntry) a1).getIngredientsToSort(), ((INEIRecipeEntry) b1).getIngredientsToSort());
        }
        return 0;
    }
}
