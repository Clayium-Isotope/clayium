package mods.clayium.plugin.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mods.clayium.gui.FDText;
import mods.clayium.gui.IFunctionalDrawer;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public abstract class NEITemp extends TemplateRecipeHandler {
    public static List<PositionedStack> generateIngredientPositionedStacks(Object[] ingredients) {
        List<PositionedStack> inputList = new ArrayList<PositionedStack>();
        for (int i = 0; i < ingredients.length; i++) {
            inputList.add(generatePermutationsFixed(new PositionedStack(UtilItemStack.object2ItemStacks(ingredients[i]), 52 - i * 17, 21, false)));
        }
        return inputList;
    }

    public static List<PositionedStack> generateResultPositionedStacks(ItemStack[] results) {
        List<PositionedStack> resultList = new ArrayList<PositionedStack>();
        for (int i = 0; i < results.length; i++) {
            resultList.add(new PositionedStack(results[i], 98 + i * 17, 21));
        }
        return resultList;
    }

    public static IFunctionalDrawer<Object> drawerProgressBar = new IFunctionalDrawer<Object>() {
        public Object draw(Object param) {
            if (param instanceof INEIRecipeEntry) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GuiDraw.changeTexture("clayium:textures/gui/progressbarfurnace.png");
                ((INEIRecipeEntry) param).getHandler().drawProgressBar(71, 21, 0, 0, 24, 17, 40, 0);
            }
            return param;
        }
    };

    public static FDText.FDTextHandler<Object> setterTier = new NEITextSetterTier();
    public static IFunctionalDrawer<Object> drawerTier = FDText.INSTANCE.getFDText(setterTier, 6, 43, -16777216, -1);

    public static FDText.FDTextHandler<Object> setterEnergy = new NEITextSetterEnergy();
    public static IFunctionalDrawer<Object> drawerEnergy = FDText.INSTANCE.getFDText(setterEnergy, 6, 52, -16777216, -1);

    public static PositionedStack generatePermutationsFixed(PositionedStack p) {
        if (p.items == null || p.items.length == 0)
            return p;
        int stacksize = (p.items[0]).stackSize;
        p.generatePermutations();
        for (ItemStack item : p.items) {
            item.stackSize = stacksize;
        }
        return p;
    }

    public static boolean isAvailable(Object[] objects) {
        if (objects == null || objects.length == 0)
            return false;
        for (Object object : objects) {
            ItemStack[] itemstacks = UtilItemStack.object2ItemStacks(object);
            if (itemstacks == null || itemstacks.length == 0)
                return false;
        }
        return true;
    }

    private static final StackComparator defaultStackComparator = new StackComparator();

    public abstract Comparator<TemplateRecipeHandler.CachedRecipe> getComparator();

    public static StackComparator getStackComparator() {
        return defaultStackComparator;
    }

    public abstract Iterable<INEIRecipeEntry> getMatchedSet();

    public boolean matchForOutputId(String outputId, Object... results) {
        return (outputId == null || outputId.equals(getOverlayIdentifier()));
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (matchForOutputId(outputId, results)) {
            Iterable<INEIRecipeEntry> set = getMatchedSet();
            if (set == null)
                return;
            for (INEIRecipeEntry entry : getMatchedSet()) {
                this.arecipes.add(entry.asCachedRecipe());
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
        Collections.sort(this.arecipes, getComparator());
    }

    public void loadCraftingRecipes(ItemStack result) {
        Iterable<INEIRecipeEntry> set = getMatchedSet();
        if (set == null)
            return;
        for (INEIRecipeEntry entry : getMatchedSet()) {
            if (entry.matchForCraftingRecipe(result))
                this.arecipes.add(entry.asCachedRecipe());
        }
        Collections.sort(this.arecipes, getComparator());
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient == null) return;

        Iterable<INEIRecipeEntry> set = getMatchedSet();
        if (set == null) return;

        for (INEIRecipeEntry entry : getMatchedSet()) {
            if (entry.matchForUsageRecipe(ingredient))
                this.arecipes.add(entry.asCachedRecipe());
        }
        Collections.sort(this.arecipes, getComparator());
    }

    public void drawExtras(int recipe) {
        TemplateRecipeHandler.CachedRecipe re = this.arecipes.get(recipe);
        if (re instanceof INEIRecipeEntry) {
            ((INEIRecipeEntry) re).drawExtras();
        }
    }

    public void drawProgressBar(int x, int y, int tx, int ty, int w, int h, float completion, int direction) {
        GuiDraw.drawTexturedModalRect(x, y, tx, ty, w, h);
        super.drawProgressBar(x, y, tx, ty + h, w, h, completion, direction);
    }

    public String getRecipeName() {
        return I18n.format("recipe." + getOverlayIdentifier());
    }

    @Deprecated
    public static void drawTier(int tier) {
        (Minecraft.getMinecraft()).fontRenderer.drawString("Tier: " + tier, 6, 43, -16777216);
    }

    @Deprecated
    public static void drawEnergy(long energy, long time) {
        (Minecraft.getMinecraft()).fontRenderer.drawString(UtilLocale.ClayEnergyNumeral(energy) + "CE/t x " + UtilLocale.craftTimeNumeral(time) + "t = " +
                UtilLocale.ClayEnergyNumeral(energy * time) + "CE", 6, 52, -16777216);
    }

    public interface INEIEntryEnergy {
        long getEnergy();

        long getTime();
    }

    public interface INEIEntryTiered {
        int getTier();
    }
}
