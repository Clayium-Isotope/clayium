package mods.clayium.machine.ClayWorkTable;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.machine.crafting.RecipeElement;
import mods.clayium.util.IngredientAlways;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.Collections;

public class KneadingRecipe extends RecipeElement {
    protected static final ResourceLocation buttonTex = new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png");
    private static final KneadingRecipe FLAT = new KneadingRecipe(ItemStack.EMPTY, TileEntityClayWorkTable.KneadingMethod.UNKNOWN, -1, ItemStack.EMPTY, ItemStack.EMPTY);
    public static KneadingRecipe flat() {
        return FLAT;
    }

    /*private*/ final ItemStack material;
    /*private*/ final Ingredient tool;
    /*private*/ final TileEntityClayWorkTable.KneadingMethod method;
    /*private*/ final short time;
    /*private*/ final ItemStack product;
    /*private*/ final ItemStack change;

    private static final ItemStack rollingPin   = new ItemStack(ClayiumItems.rollingPin, 1, OreDictionary.WILDCARD_VALUE);
    private static final ItemStack slicer       = new ItemStack(ClayiumItems.slicer, 1, OreDictionary.WILDCARD_VALUE);
    private static final ItemStack spatula      = new ItemStack(ClayiumItems.spatula, 1, OreDictionary.WILDCARD_VALUE);

    private static final Ingredient Always  = new IngredientAlways();
    private static final Ingredient RP      = Ingredient.fromStacks(rollingPin);
    private static final Ingredient SL_SP   = Ingredient.fromStacks(slicer, spatula);
    private static final Ingredient SP      = Ingredient.fromStacks(spatula);

    public KneadingRecipe(ItemStack material, TileEntityClayWorkTable.KneadingMethod method, int time, ItemStack product, ItemStack change) {
        super(material, 0, product, 0, time);
        this.material = material;
        this.materialIng = NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(material));

        this.method = method;
        switch (this.method) {
            case Roll:
            case Press:
            default:
                this.tool = Always; break;
            case Bend: // 圧延は麺棒
                this.tool = RP;     break;
            case CutRect:
            case Slice: // 矩形に打抜くのはスライサーまたはへら
                this.tool = SL_SP;  break;
            case CutCircle: // 円形に切り出すのはへら
                this.tool = SP;     break;
        }

        this.time = (short) time;
        this.product = product;
        this.change = change;

        setRegistryName(ClayiumCore.ModId, this.material.getUnlocalizedName() + "_to_" + this.product.getUnlocalizedName() + this.method.toBeSuffix());
    }

    public boolean hasChange() {
        return !this.change.isEmpty();
    }

    public ItemStack getRemainingTool(ItemStack tool) {
        if (this.tool != Always) return ForgeHooks.getContainerItem(tool).copy();
        return tool;
    }


    @Override
    public boolean isFlat() {
        return this.equals(FLAT);
    }

    @Override
    public boolean matches(InventoryCrafting _inv, World worldIn) {
        return true;
    }

    private final NonNullList<Ingredient> materialIng;
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.materialIng;
    }

    @Override
    public String getGroup() {
        return "clayium:knead";
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Collections.singletonList(this.material), Arrays.asList(this.tool.getMatchingStacks())));
        ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(this.product, this.change));
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString("" + this.time, 28 + 16 * this.method.ordinal() + 8 - minecraft.fontRenderer.getStringWidth("" + this.time) / 2, 37 - minecraft.fontRenderer.FONT_HEIGHT, -16777216);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bindTexture(buttonTex);

        for (int i = 0; i < 6; i++) {
            if (i == this.method.ordinal()) {
                minecraft.currentScreen.drawTexturedModalRect(28 + 16 * i, 37, 16, 0, 16, 16);
            } else {
                minecraft.currentScreen.drawTexturedModalRect(28 + 16 * i, 37, 0, 0, 16, 16);
            }
        }

        minecraft.currentScreen.drawTexturedModalRect(28, 37, 0, 32, 96, 16);
    }
}
