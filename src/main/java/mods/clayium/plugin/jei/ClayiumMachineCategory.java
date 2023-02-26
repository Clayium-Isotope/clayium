package mods.clayium.plugin.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.crafting.RecipeElement;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ClayiumMachineCategory implements IRecipeCategory<RecipeElement> {
    protected static final ResourceLocation plate = new ResourceLocation(ClayiumCore.ModId, "textures/gui/nei.png");
    protected static final ResourceLocation button = new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png");

    protected final IDrawable background;
    protected final IDrawableStatic progressBG;
    protected final IDrawableAnimated progressFG;

    protected final IDrawable icon;

    protected final EnumMachineKind kind;

    public ClayiumMachineCategory(IGuiHelper guiHelper, EnumMachineKind kind) {
        this.kind = kind;

        background = guiHelper.createDrawable(plate, 5, 18, 165, 55);

        progressBG = guiHelper.createDrawable(button, 80, 96, 22, 16);
        progressFG = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(button, 80, 112, 22, 16), 40, IDrawableAnimated.StartDirection.LEFT, false);

        icon = guiHelper.createDrawableIngredient(new ItemStack(ClayiumMachines.get(kind)));
    }

    @Override
    public String getUid() {
        return ClayiumCore.ModId + "." + kind.getRegisterName();
    }

    @Override
    public String getTitle() {
        return kind.getRegisterName();
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

        int len = recipeElement.getMaterials().size();
        int i;
        for (i = 0; i < len; i++) {
            stacks.init(i, true, 17 * (4 - len + i), 13);
        }

        len = recipeElement.getResults().size();
        for (int j = 0; j < len; j++) {
            stacks.init(j + i, false, 97 + 17 * j, 13);
        }

        stacks.set(iIngredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        progressBG.draw(minecraft, 71, 14);
        progressFG.draw(minecraft, 71, 14);
    }

    @Override
    public String getModName() {
        return ClayiumCore.ModName;
    }
}
