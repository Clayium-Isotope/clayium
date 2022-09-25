package mods.clayium.machine.crafting;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.crafting.IItemPattern;
import mods.clayium.util.crafting.OreDictionaryStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RecipeElement implements IRecipeWrapper {
    public static final RecipeElement FLAT = new RecipeElement(ItemStack.EMPTY, 0, 0, ItemStack.EMPTY, 0, 0);
    protected static final ResourceLocation buttonTex = new ResourceLocation(ClayiumCore.ModId, "textures/gui/button_.png");

    public RecipeElement(ItemStack materialIn, int method, int tier, ItemStack resultIn, long energy, long time) {
        this(Arrays.asList(materialIn), method, tier, Arrays.asList(resultIn), energy, time);
    }

    public RecipeElement(List<ItemStack> materialIn, int method, int tier, List<ItemStack> resultIn, long energy, long time) {
        condition = new RecipeCondition(materialIn, method, tier);
        result = new RecipeResult(resultIn, energy, time);
    }

    public RecipeCondition getCondition() {
        return condition;
    }

    public RecipeResult getResult() {
        return result;
    }

    private final RecipeCondition condition;
    private final RecipeResult result;

    @Override
    public void getIngredients(IIngredients iIngredients) {
        if (this.getCondition().getMaterials().size() == 1)
            iIngredients.setInput(VanillaTypes.ITEM, this.getCondition().getMaterials().get(0));
        else if (this.getCondition().getMaterials().size() > 1)
            iIngredients.setInputs(VanillaTypes.ITEM, this.getCondition().getMaterials());

        if (this.getResult().getResults().size() == 1)
            iIngredients.setOutput(VanillaTypes.ITEM, this.getResult().getResults().get(0));
        else if (this.getResult().getResults().size() > 1)
            iIngredients.setOutputs(VanillaTypes.ITEM, this.getResult().getResults());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (this.condition.method != -1) {
            minecraft.fontRenderer.drawString("" + this.result.time, 28 + 16 * this.condition.method + 8 - minecraft.fontRenderer.getStringWidth("" + this.result.time) / 2, 37 - minecraft.fontRenderer.FONT_HEIGHT, -16777216);

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            minecraft.getTextureManager().bindTexture(buttonTex);

            for (int i = 0; i < 6; i++) {
                if (i == this.condition.method) {
                    minecraft.ingameGUI.drawTexturedModalRect(28 + 16 * i, 37, 16, 0, 16, 16);
                } else {
                    minecraft.currentScreen.drawTexturedModalRect(28 + 16 * i, 37, 0, 0, 16, 16);
                }
            }

            minecraft.ingameGUI.drawTexturedModalRect(28, 37, 0, 32, 96, 16);
        }
    }

    public static class RecipeCondition {
        private final List<ItemStack> materials;
        private final int method;
        private final int tier;

        public List<ItemStack> getMaterials() {
            return materials;
        }

        public int getMethod() {
            return method;
        }

        public int getTier() {
            return tier;
        }

        public RecipeCondition(List<ItemStack> materials, int method, int tier) {
            this.materials = materials;
            this.method = method;
            this.tier = tier;
        }

        public boolean match(List<ItemStack> itemStacksIn, int methodIn, int tierIn) {
            if (method != methodIn || tier > tierIn || materials.size() > itemStacksIn.size()) return false;
            return match(itemStacksIn);
        }

        public boolean match(List<ItemStack> itemStacksIn) {
            for (int i = 0; i < materials.size(); i++)
                if (!inclusion(materials.get(i), itemStacksIn.get(i)))
                    return false;

            return true;
        }

        public static boolean inclusion(ItemStack from, ItemStack comes) {
            if (from.isEmpty()) return true;
            if (UtilItemStack.areTypeEqual(from, comes) && from.getCount() <= comes.getCount()) return true;
            if (from.getHasSubtypes() && comes.getHasSubtypes()) return from.isItemEqual(comes);
            if (from.getItemDamage() == OreDictionary.WILDCARD_VALUE) return true;
            return false;
        }

        public int[] getStackSizes(ItemStack ...items) {
            int[] sizes = new int[items.length];
            for (int i = 0; i < items.length && i < this.materials.size(); i++) {
                sizes[i] = getStackSize(this.materials.get(i), items[i]);
            }
            return sizes;
        }

        public boolean isCraftable(ItemStack itemstack, int tier) {
            if (this.tier > tier) {
                return false;
            }

            for (ItemStack stack : this.materials) {
                if (canBeCraftedODs(itemstack, stack, false)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class RecipeResult {
        private final List<ItemStack> results;
        private final long energy;
        private final long time;

        public List<ItemStack> getResults() {
            return results;
        }

        public long getEnergy() {
            return energy;
        }

        public long getTime() {
            return time;
        }

        public RecipeResult(List<ItemStack> results, long energy, long time) {
            this.results = results;
            this.energy = energy;
            this.time = time;
        }
    }

    public static int getStackSize(Object recipe, ItemStack item) {
        if (recipe instanceof IItemPattern) {
            if (item == null) {
                List<ItemStack> items = ((IItemPattern) recipe).toItemStacks();
                if (items != null && items.size() >= 1)
                    item = items.get(0);
            }
            return ((IItemPattern) recipe).getStackSize(item);
        }
        return getStackSize(recipe);
    }

    public static int getStackSize(Object item) {
        if (item instanceof ItemStack) return ((ItemStack) item).getCount();
//        if (item instanceof OreDictionaryStack) return ((OreDictionaryStack) item).stackSize;
        if (item instanceof String) return 1;

        return 0;
    }

    public static boolean canBeCrafted(ItemStack itemstack, ItemStack itemstack2, boolean sizeCheck) {
        if (itemstack2 == null) return true;
        if (itemstack == null) return false;
        return ItemStack.areItemsEqual(itemstack2, itemstack)
                && (itemstack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemstack.getItemDamage() == OreDictionary.WILDCARD_VALUE
                || UtilItemStack.areDamageEqual(itemstack2, itemstack))
                && (!sizeCheck || itemstack2.getCount() <= itemstack.getCount());
    }

    public static boolean canBeCrafted(ItemStack itemstack, ItemStack itemstack2) {
        return canBeCrafted(itemstack, itemstack2, true);
    }

    public static boolean canBeCraftedOD(ItemStack itemstack, Object object, boolean sizeCheck) {
        if (object == null) return true;
        if (itemstack == null) return false;
        if (object instanceof String) {
            return UtilItemStack.hasOreName(itemstack, (String) object);
        }
        if (object instanceof OreDictionaryStack) {
            if (sizeCheck && ((OreDictionaryStack) object).getStackSize() > itemstack.getCount())
                return false;
            return UtilItemStack.hasOreName(itemstack, ((OreDictionaryStack) object).getId());
        }
        if (object instanceof ItemStack)
            return canBeCrafted(itemstack, (ItemStack) object, sizeCheck);
        if (object instanceof IItemPattern)
            return ((IItemPattern) object).match(itemstack, sizeCheck);

        return false;
    }

    public static boolean canBeCraftedOD(ItemStack itemstack, Object object) {
        return canBeCraftedOD(itemstack, object, true);
    }

    public static boolean canBeCraftedODs(Object stackingred, Object recipeingred, boolean sizeCheck) {
        if (recipeingred == null) return true;
        if (stackingred == null) return false;
        if (stackingred instanceof ItemStack) {
            return canBeCraftedOD((ItemStack) stackingred, recipeingred, sizeCheck);
        }
        if (stackingred instanceof String || stackingred instanceof OreDictionaryStack) {
            int oreid = (stackingred instanceof OreDictionaryStack) ? ((OreDictionaryStack) stackingred).getId() : OreDictionary.getOreID((String) stackingred);
            int stackSize = (stackingred instanceof OreDictionaryStack) ? ((OreDictionaryStack) stackingred).getStackSize() : 1;
            for (ItemStack item : OreDictionary.getOres(String.valueOf(oreid))) {
                ItemStack item0 = item.copy();
                item0.setCount(stackSize);
                if (canBeCraftedOD(item0, recipeingred, sizeCheck)) {
                    return true;
                }
            }
        }
        if (stackingred instanceof IItemPattern) {
            return ((IItemPattern) stackingred).hasIntersection(convert(recipeingred), sizeCheck);
        }
        return false;
    }

    public static IItemPattern convert(Object ingred) {
    /* TODO still added ItemPattern
        if (ingred instanceof ItemStack) {
            return new ItemPatternItemStack((ItemStack) ingred);
        }
        if (ingred instanceof OreDictionaryStack) {
            return new ItemPatternOreDictionary(((OreDictionaryStack) ingred).id, ((OreDictionaryStack) ingred).stackSize);
        }
        if (ingred instanceof String) {
            return new ItemPatternOreDictionary((String) ingred, 1);
        }
    */
        if (ingred instanceof IItemPattern) {
            return (IItemPattern) ingred;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof RecipeElement)) return false;
        RecipeElement other = (RecipeElement) o;

        return this.condition.tier == other.condition.tier
                && this.condition.method == other.condition.method
                && this.result.energy == other.result.energy
                && this.result.time == other.result.time
                && this.condition.materials.equals(other.condition.materials)
                && this.result.results.equals(other.result.results);
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(this.condition.tier, this.condition.method, this.result.energy, this.result.time);

        for (ItemStack stack : this.condition.materials) {
            if (stack.getItem().getRegistryName() != null)
                hash = 31 * hash + stack.getItem().getRegistryName().getResourcePath().hashCode();
        }

        for (ItemStack stack : this.result.results) {
            if (stack.getItem().getRegistryName() != null)
                hash = 31 * hash + stack.getItem().getRegistryName().getResourcePath().hashCode();
        }

        return hash;
    }
}

// TODO RecipeElement implement IRecipe and use Ingredient instead of ItemStack
class _RecipeElement extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private final NonNullList<ItemStack> materials;
    private final int method;
    private final int tier;
    private final NonNullList<ItemStack> results;
    private final long energy;
    private final long time;

    public _RecipeElement(List<ItemStack> materials, int method, int tier, List<ItemStack> results, long energy, long time) {
        this.materials = NonNullList.from(ItemStack.EMPTY, materials.toArray(new ItemStack[0]));
        this.method = method;
        this.tier = tier;
        this.results = NonNullList.from(ItemStack.EMPTY, results.toArray(new ItemStack[0]));;
        this.energy = energy;
        this.time = time;
    }

    @Override
    @Deprecated // Use more arguments version
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return false;
    }

    public boolean matches(InventorySimpleMachine invIn, int methodIn, int tierIn) {
        if (method != methodIn || tier > tierIn || materials.size() > invIn.getSizeInventory()) return false;
        return matches(invIn);
    }

    public boolean matches(InventorySimpleMachine invIn) {
        for (int i = 0; i < materials.size(); i++)
            if (!inclusion(materials.get(i), invIn.getStackInSlot(i)))
                return false;

        return true;
    }

    public static boolean inclusion(ItemStack from, ItemStack comes) {
        if (from.isEmpty()) return true;
        if (from.getItem() != comes.getItem()) return false;
        if (from.getCount() > comes.getCount()) return false;
        if (from.getHasSubtypes() && comes.getHasSubtypes()) return from.isItemEqual(comes);
        if (from.getItemDamage() == OreDictionary.WILDCARD_VALUE) return true;
        return false;
    }

    @Override
    @Deprecated // Use NonNullList version
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return getCraftingResults(inv).get(0);
    }

    public NonNullList<ItemStack> getCraftingResults(InventoryCrafting inv) {
        return results;
    }

    @Override
    public boolean canFit(int width, int height) {
        return height == 1 && width >= materials.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return results.get(0);
    }

    @Override
    public String getGroup() {
        return "clayium";
    }
}