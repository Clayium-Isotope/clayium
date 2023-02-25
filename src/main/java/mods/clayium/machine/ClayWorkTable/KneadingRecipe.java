package mods.clayium.machine.ClayWorkTable;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.util.IngredientAlways;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KneadingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe , IRecipeWrapper {
    public static final KneadingRecipe FLAT = new KneadingRecipe(ItemStack.EMPTY, TileEntityClayWorkTable.KneadingMethod.UNKNOWN, -1, ItemStack.EMPTY, ItemStack.EMPTY);

    /*private*/ final ItemStack material;
    /*private*/ final Ingredient tool;
    /*private*/ final TileEntityClayWorkTable.KneadingMethod method;
    /*private*/ final short time;
    /*private*/ final ItemStack product;
    /*private*/ final ItemStack change;

    private static final Ingredient Always = new IngredientAlways();
    private static final Ingredient RP = Ingredient.fromItem(ClayiumItems.rollingPin);
    private static final Ingredient SL_SP = Ingredient.fromItems(ClayiumItems.slicer, ClayiumItems.spatula);
    private static final Ingredient SP = Ingredient.fromItem(ClayiumItems.spatula);

    public KneadingRecipe(ItemStack material, TileEntityClayWorkTable.KneadingMethod method, int time, ItemStack product, ItemStack change) {
        super();
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

        setRegistryName(ClayiumCore.ModId, this.material.getUnlocalizedName() + "_to_" + this.product.getUnlocalizedName()
                + ( this.tool != Always ? "_w_" + this.tool.getMatchingStacks()[0].getUnlocalizedName() : "" ));
    }

    public boolean hasChange() {
        return this.change.isEmpty();
    }

    @Override
    public boolean matches(InventoryCrafting _inv, World worldIn) {
        return _inv instanceof InventoryKneading;
    }

    /**
     * Returns EMPTY while inv must be working
     */
    @Override
    public ItemStack getCraftingResult(InventoryCrafting _inv) {
        assert _inv instanceof InventoryKneading : "Unexpected Calling around Clay Work Table Recipe";
        InventoryKneading inv = (InventoryKneading) _inv;

        if (inv.getCraftTime() < this.time) {
            return ItemStack.EMPTY;
        }

        return this.product;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.product;
    }

    private final NonNullList<Ingredient> materialIng;
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.materialIng;
    }

    @Override
    public boolean isDynamic() {
        return true;
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

    // ===== REGISTRY PART =====

    private static final Map<Integer, KneadingRecipe> recipeClayWorkTable;

    @Nonnull
    public static KneadingRecipe byHash(int hash) {
        KneadingRecipe recipe = recipeClayWorkTable.get(hash);
        if (recipe == null) return FLAT;
        return recipe;
    }

    @Nonnull
    public static KneadingRecipe find(TileEntityClayWorkTable.KneadingMethod method, Predicate<KneadingRecipe> matcher) {
        if (method == TileEntityClayWorkTable.KneadingMethod.UNKNOWN) return KneadingRecipe.FLAT;

        for (KneadingRecipe value : recipeClayWorkTable.values()) {
            if (method == value.method && matcher.test(value)) return value;
        }

        return KneadingRecipe.FLAT;
    }

    private static void registerKneadingRecipes(List<KneadingRecipe> register) {
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball), TileEntityClayWorkTable.KneadingMethod.Roll, 4, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick), ItemStack.EMPTY));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), TileEntityClayWorkTable.KneadingMethod.Press, 30, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), ItemStack.EMPTY));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), TileEntityClayWorkTable.KneadingMethod.Bend, 4, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 2)));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), TileEntityClayWorkTable.KneadingMethod.Roll, 4, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ItemStack.EMPTY));

        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), TileEntityClayWorkTable.KneadingMethod.Press, 10, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.blade), ItemStack.EMPTY));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), TileEntityClayWorkTable.KneadingMethod.Bend, 1, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.blade), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 2)));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), TileEntityClayWorkTable.KneadingMethod.Slice, 3, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick, 4), ItemStack.EMPTY));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate, 6), TileEntityClayWorkTable.KneadingMethod.Bend, 10, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ItemStack.EMPTY));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate, 3), TileEntityClayWorkTable.KneadingMethod.Roll, 40, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ItemStack.EMPTY));

        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), TileEntityClayWorkTable.KneadingMethod.CutRect, 4, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 2)));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), TileEntityClayWorkTable.KneadingMethod.CutCircle, 2, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ring), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallRing)));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), TileEntityClayWorkTable.KneadingMethod.Press, 15, new ItemStack(ClayiumItems.rawSlicer), ItemStack.EMPTY));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), TileEntityClayWorkTable.KneadingMethod.Bend, 2, new ItemStack(ClayiumItems.rawSlicer), ItemStack.EMPTY));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallDisc), TileEntityClayWorkTable.KneadingMethod.CutCircle, 1, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallRing), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.shortStick)));

        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), TileEntityClayWorkTable.KneadingMethod.Roll, 3, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.needle), ItemStack.EMPTY));
        register.add(new KneadingRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), TileEntityClayWorkTable.KneadingMethod.Slice, 7, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallDisc, 8), ItemStack.EMPTY));
    }

    static {
        List<KneadingRecipe> list = new ArrayList<>();
        registerKneadingRecipes(list);
        recipeClayWorkTable = list.stream().collect(Collectors.toMap(Object::hashCode, Function.identity()));
    }
}
