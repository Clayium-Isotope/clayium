package mods.clayium.plugin.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import mods.clayium.gui.client.GuiClayMachines;
import mods.clayium.util.UtilTier;
import mods.clayium.util.crafting.Recipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;


public class NEIClayMachines
        extends NEITemp {
    private Recipes recipes;
    public UtilTier.TierManager tierManager;
    private String overlayId;
    private Comparator<TemplateRecipeHandler.CachedRecipe> comp;

    public NEIClayMachines(String overlayId) {
        this(overlayId, UtilTier.TierManager.getMachineTierManager());
    }


    public NEIClayMachines(String overlayId, UtilTier.TierManager tierManager) {
        this.comp = new NEIRecipeComparator();
        this.recipes = null;
        this.overlayId = overlayId;
        this.tierManager = tierManager;
        loadTransferRects();
    }

    public NEIClayMachines(Recipes recipes) { this(recipes, UtilTier.TierManager.getMachineTierManager()); }

    public NEIClayMachines(Recipes recipes, UtilTier.TierManager tierManager) {
        this(recipes.recipeid, tierManager);
        this.recipes = recipes;
    }

    public void setTierManager(UtilTier.TierManager tierManager) {
        if (tierManager != null) this.tierManager = tierManager;
    }

    public Comparator<TemplateRecipeHandler.CachedRecipe> getComparator() { return this.comp; }

    public class NEIRecipeComparator extends NEIEntryComparator {
        public int compare(TemplateRecipeHandler.CachedRecipe a1, TemplateRecipeHandler.CachedRecipe b1) {
            if (a1 instanceof NEIRecipeEntry && b1 instanceof NEIRecipeEntry) {
                NEIRecipeEntry a = (NEIRecipeEntry) a1;
                NEIRecipeEntry b = (NEIRecipeEntry) b1;
                if (a.method > b.method) return 1;
                if (b.method > a.method) return -1;

            }


            return super.compare(a1, b1);
        }
    }


    public Class<? extends GuiContainer> getGuiClass() {
        return (Class) GuiClayMachines.class;
    }


    public String getOverlayIdentifier() {
        return this.overlayId;
    }


    public void loadTransferRects() {
        if (this.overlayId != null) {
            this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(69, 20, 28, 18), this.overlayId, new Object[0]));
        }
    }

    public Iterable<INEIRecipeEntry> getMatchedSet() {
        if (this.recipes == null)
            return null;
        List<INEIRecipeEntry> list = new ArrayList<INEIRecipeEntry>();
        for (Map.Entry<Recipes.RecipeCondition, Recipes.RecipeResult> entry : (Iterable<Map.Entry<Recipes.RecipeCondition, Recipes.RecipeResult>>) this.recipes.recipeResultMap.entrySet()) {
            Object[] materiallist = ((Recipes.RecipeCondition) entry.getKey()).getMaterials();
            ItemStack[] resultlist = ((Recipes.RecipeResult) entry.getValue()).getResults();
            int method = ((Recipes.RecipeCondition) entry.getKey()).getMethod();
            int tier = ((Recipes.RecipeCondition) entry.getKey()).getTier();
            long energy = ((Recipes.RecipeResult) entry.getValue()).getEnergy();
            long time = ((Recipes.RecipeResult) entry.getValue()).getTime();
            if (!NEITemp.isAvailable(materiallist)) {
                continue;
            }


            NEIRecipeEntry arecipe = new NEIRecipeEntry(materiallist, method, tier, resultlist, (long) ((float) energy * this.tierManager.getF("multConsumingEnergy", tier)), (long) ((float) time * this.tierManager.getF("multCraftTime", tier)));
            arecipe.computeVisuals();
            list.add(arecipe);
        }
        return list;
    }


    public String getRecipeName() {
        return I18n.format("recipe." + getOverlayIdentifier(), new Object[0]);
    }


    public String getGuiTexture() {
        return "clayium:textures/gui/nei.png";
    }


    public TemplateRecipeHandler newInstance() {
        return new NEIClayMachines(this.recipes, this.tierManager);
    }
}
