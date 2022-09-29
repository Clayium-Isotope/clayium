package mods.clayium.machine.crafting;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

public class KneadingRecipeElement extends RecipeElement {
    protected static final ResourceLocation buttonTex = new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png");

    protected final ItemStack material;
    protected final Ingredient tool;
    protected final int method;
    protected final ItemStack result;
    protected final ItemStack change;
    protected final long time;

    public KneadingRecipeElement(ItemStack materialIn, int method, ItemStack resultIn, ItemStack changeIn, long time) {
        super(toolFromMethod(method).matchingStacks.length >= 1 ? Arrays.asList(materialIn, toolFromMethod(method).matchingStacks[0]) : Arrays.asList(materialIn),
                method, 0, Arrays.asList(resultIn, changeIn), 0L, time);
        this.material = materialIn;
        this.method = method;
        this.tool = toolFromMethod(this.method);
        this.result = resultIn;
        this.change = changeIn;
        this.time = time;
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Arrays.asList(this.material), Arrays.asList(this.tool.matchingStacks)));
        iIngredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(this.result, ItemStack.EMPTY));
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString("" + this.time, 28 + 16 * this.method + 8 - minecraft.fontRenderer.getStringWidth("" + this.time) / 2, 37 - minecraft.fontRenderer.FONT_HEIGHT, -16777216);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bindTexture(buttonTex);

        for (int i = 0; i < 6; i++) {
            if (i == this.method) {
                minecraft.currentScreen.drawTexturedModalRect(28 + 16 * i, 37, 16, 0, 16, 16);
            } else {
                minecraft.currentScreen.drawTexturedModalRect(28 + 16 * i, 37, 0, 0, 16, 16);
            }
        }

        minecraft.currentScreen.drawTexturedModalRect(28, 37, 0, 32, 96, 16);
    }

    private static Ingredient toolFromMethod(int method) {
        switch (method) {
            case 0:
            case 1:
            default:
                return Ingredient.EMPTY;
            case 2:
                return Ingredient.fromItem(ClayiumItems.rollingPin);
            case 3:
                return Ingredient.fromItems(ClayiumItems.slicer, ClayiumItems.spatula);
            case 4:
                return Ingredient.fromItem(ClayiumItems.spatula);
            case 5:
                return Ingredient.fromItems(ClayiumItems.slicer, ClayiumItems.spatula);
        }
    }
}
