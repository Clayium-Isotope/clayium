package mods.clayium.machine.crafting;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.clayium.util.UtilLocale;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.*;
import java.util.stream.Collectors;

// https://forums.minecraftforge.net/topic/69258-1122-custom-irecipe/
// Extend ShapelessRecipes for JEI CraftingRecipeChecker
public class RecipeElement extends ShapelessRecipes implements IRecipeElement {
    private static final RecipeElement FLAT = new RecipeElement(Collections.emptyList(), -1, Collections.emptyList(), -1, -1);
    public static RecipeElement flat() {
        return FLAT;
    }

    private final int tier;
    private final List<ItemStack> results;
    private final long energy;
    private final long time;

    public RecipeElement(ItemStack materialIn, int tier, ItemStack resultIn, long energy, long time) {
        this(Collections.singletonList(materialIn), tier, Collections.singletonList(resultIn), energy, time);
    }

    public RecipeElement(List<ItemStack> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        this(materialIn.stream().map(Ingredient::fromStacks).collect(Collectors.toCollection(NonNullList::create)), tier, resultIn, energy, time);
    }

    public RecipeElement(NonNullList<Ingredient> materialIn, int tier, List<ItemStack> resultIn, long energy, long time) {
        super("clayium", ItemStack.EMPTY, materialIn);
        this.tier = tier;
        this.results = resultIn;
        this.energy = energy;
        this.time = time;
    }

    @Override
    public int getTier() {
        return tier;
    }

    public List<ItemStack> getResults() {
        return results;
    }

    public long getEnergy() {
        return energy;
    }

    public long getTime() {
        return time;
    }

    @Override
    public boolean isFlat() {
        return this.equals(FLAT);
    }

    private static boolean runner(List<Ingredient> ingredients, NonNullList<ItemStack> invStacks, int ingIdx, LinkedList<Integer> assigned) {
        if (ingIdx >= ingredients.size()) {
            return true;
        }

        for (int i = 0; i < invStacks.size(); i++) {
            if (!assigned.contains(i) && ingredients.get(ingIdx).apply(invStacks.get(i))) {
                if (ingIdx + 1 >= ingredients.size()) {
                    return true;
                }

                assigned.add(i);
                if (runner(ingredients, invStacks, ingIdx + 1, assigned)) {
                    return true;
                }
                assigned.removeLast();
            }
        }

        return false;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        // TODO: Temporary forcing.
        if (inv instanceof InventoryCrafting) return false;

        return runner(this.getIngredients(),
                ObfuscationReflectionHelper.getPrivateValue(InventoryCrafting.class, inv, "field_70466_a"), // stackList
                0, new LinkedList<>());
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public String getGroup() {
        return "clayium";
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM,
                this.getIngredients().stream()
                        .map(ingredient -> Arrays.stream(ingredient.getMatchingStacks()).collect(Collectors.toList()))
                        .collect(Collectors.toList()));

        iIngredients.setOutputs(VanillaTypes.ITEM, this.results);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(this.tier < 0 ? "" : "Tier: " + this.tier, 6, 36, -16777216);

        String str;
        if (this.energy < 0L) {
            str = this.time < 0L ? "" : UtilLocale.craftTimeNumeral(this.time) + "t";
        } else {
            str = this.time < 0L
                    ? UtilLocale.ClayEnergyNumeral(this.energy) + "CE"
                    : UtilLocale.ClayEnergyNumeral(this.energy) + "CE/t x " + UtilLocale.craftTimeNumeral(this.time) + "t = " + UtilLocale.ClayEnergyNumeral((double)this.energy * (double)this.time) + "CE";
        }

        minecraft.fontRenderer.drawString(str, 6, 45, -16777216);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof RecipeElement)) return false;
        RecipeElement other = (RecipeElement) o;

        return this.tier == other.tier
                && this.energy == other.energy
                && this.time == other.time
                && this.getIngredients().equals(other.getIngredients())
                && this.results.equals(other.results);
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(this.tier, this.energy, this.time);

        for (Ingredient ingredient : this.getIngredients()) {
            for (ItemStack stack : ingredient.getMatchingStacks()) {
                if (stack.getItem().getRegistryName() != null)
                    hash = 31 * hash + stack.getItem().getRegistryName().getPath().hashCode();
            }
        }

        for (ItemStack stack : this.results) {
            if (stack.getItem().getRegistryName() != null)
                hash = 31 * hash + stack.getItem().getRegistryName().getPath().hashCode();
        }

        return hash;
    }
}