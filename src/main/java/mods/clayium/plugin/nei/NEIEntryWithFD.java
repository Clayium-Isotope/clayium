package mods.clayium.plugin.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.clayium.gui.IFunctionalDrawer;

import java.util.ArrayList;
import java.util.List;

public abstract class NEIEntryWithFD extends TemplateRecipeHandler.CachedRecipe implements INEIRecipeEntry {
    protected final NEITemp neiTemp;
    protected TemplateRecipeHandler handler;

    public NEIEntryWithFD(NEITemp neiTemp) {
        neiTemp.super();
        this.neiTemp = neiTemp;

        this.handler = neiTemp;
        this.fdList = new ArrayList<IFunctionalDrawer<Object>>();
    }

    protected List<IFunctionalDrawer<Object>> fdList;

    public List<IFunctionalDrawer<Object>> getFDList() {
        return this.fdList;
    }

    public void setFDList(List<IFunctionalDrawer<Object>> fdList) {
        this.fdList = fdList;
    }

    public void drawExtras() {
        for (IFunctionalDrawer<Object> fd : this.fdList) {
            fd.draw(this);
        }
    }

    public void setHandler(TemplateRecipeHandler handler) {
        this.handler = handler;
    }

    public TemplateRecipeHandler getHandler() {
        return this.handler;
    }
}
