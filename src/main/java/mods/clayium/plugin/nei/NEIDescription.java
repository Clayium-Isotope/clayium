package mods.clayium.plugin.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NEIDescription
        extends NEITemp {
    protected String overlayId;
    protected String guiTexture;
    protected Rectangle transferRect;
    protected List<INEIRecipeEntry> entryList;
    protected int recipesPerPage;

    public NEIDescription(String overlayId, String guiTexture, Rectangle transferRect, int recipesPerPage, List<INEIRecipeEntry> entryList) {
        this.overlayId = overlayId;
        this.guiTexture = guiTexture;
        this.transferRect = transferRect;
        this.entryList = entryList;
        this.recipesPerPage = recipesPerPage;
        loadTransferRects();
    }

    public NEIDescription(String overlayId, String guiTexture, Rectangle transferRect, int recipesPerPage) {
        this(overlayId, guiTexture, transferRect, recipesPerPage, new ArrayList<INEIRecipeEntry>());
    }

    public NEIDescription(String overlayId, int recipesPerPage) {
        this(overlayId, "clayium:textures/gui/nei.png", new Rectangle(69, 20, 28, 18), recipesPerPage);
    }


    public TemplateRecipeHandler newInstance() {
        return new NEIDescription(this.overlayId, this.guiTexture, this.transferRect, this.recipesPerPage, this.entryList);
    }

    public List<INEIRecipeEntry> getEntryList() {
        return this.entryList;
    }


    public void loadTransferRects() {
        if (this.overlayId != null && this.transferRect != null) {
            this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(this.transferRect, this.overlayId, new Object[0]));
        }
    }


    public String getOverlayIdentifier() {
        return this.overlayId;
    }


    public Comparator<TemplateRecipeHandler.CachedRecipe> getComparator() {
        return new NEIEntryComparator();
    }


    public Iterable<INEIRecipeEntry> getMatchedSet() {
        List<INEIRecipeEntry> list = getEntryList();
        for (INEIRecipeEntry entry : list)
            entry.setHandler(this);
        return list;
    }


    public String getGuiTexture() {
        return this.guiTexture;
    }


    public int recipiesPerPage() {
        return this.recipesPerPage;
    }
}
