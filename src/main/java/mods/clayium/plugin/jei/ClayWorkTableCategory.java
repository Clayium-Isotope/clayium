package mods.clayium.plugin.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayWorkTable.TileEntityClayWorkTable;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.crafting.RecipeElement;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class ClayWorkTableCategory extends ClayiumRecipeCategory {
    protected static final ResourceLocation clayWorkTable = new ResourceLocation(ClayiumCore.ModId, "textures/gui/_old/clayworktable.png");
    protected static final ResourceLocation button = new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png");
    protected final IDrawable icon;
    protected final IDrawable background;
    public static final String categoryID = ClayiumCore.ModName + ".kneading";

    protected final IDrawableStatic progressBG;
    protected final IDrawableAnimated progressFG;

    // when some recipes are shown, remind each method
    protected static final LinkedList<Integer> methods = new LinkedList<>();

    public ClayWorkTableCategory(IGuiHelper helper) {
        progressBG = helper.createDrawable(button, 0, 96, 80, 16);
        progressFG = helper.createAnimatedDrawable(helper.createDrawable(button, 0, 112, 80, 16), 40, IDrawableAnimated.StartDirection.LEFT, false);

        background = helper.createDrawable(clayWorkTable, 12, 16, 152, 56);
        icon = helper.createDrawableIngredient(new ItemStack(Items.CLAY_BALL));
    }

    @Override
    public String getUid() {
        return categoryID;
    }

    @Override
    public String getTitle() {
        return ClayiumMachines.clayWorkTable.getUnlocalizedName();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, RecipeElement recipeElement, IIngredients iIngredients) {
        IGuiItemStackGroup stacks = iRecipeLayout.getItemStacks();
        stacks.init(TileEntityClayWorkTable.ClayWorkTableSlots.MATERIAL.ordinal(), true, 4, 13);
        stacks.init(TileEntityClayWorkTable.ClayWorkTableSlots.TOOL.ordinal(), true, 67, 0);
        stacks.init(TileEntityClayWorkTable.ClayWorkTableSlots.PRODUCT.ordinal(), false, 130, 13);
        stacks.init(TileEntityClayWorkTable.ClayWorkTableSlots.CHANGE.ordinal(), false, 130, 38);

        stacks.set(iIngredients);

        methods.add(recipeElement.getCondition().getMethod());
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        progressBG.draw(minecraft, 36, 13);
        progressFG.draw(minecraft, 36, 13);

        // buttons are drawn by RecipeElement#drawInfo
    }
}
