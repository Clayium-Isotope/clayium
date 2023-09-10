package mods.clayium.plugin.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mods.clayium.block.ClayiumBlocks;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.ClayWorkTable.ContainerClayWorkTable;
import mods.clayium.machine.ClayWorkTable.GuiClayWorkTable;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.crafting.ClayiumRecipes;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;

@JEIPlugin
public class JEICompatibility implements IModPlugin {
    public static IJeiRuntime jeiRuntime = null;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helper = registry.getJeiHelpers();
        final IGuiHelper gui = helper.getGuiHelper();

        registry.addRecipeCategories(new ClayWorkTableCategory(gui));
        for (EnumMachineKind kind : EnumMachineKind.values()) {
            if (!kind.hasRecipe()) continue;
            if (kind == EnumMachineKind.workTable) continue;

            registry.addRecipeCategories(new ClayiumMachineCategory(gui, kind));
        }
        IModPlugin.super.registerCategories(registry);
    }

    @Override
    public void register(IModRegistry registry) {
        final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

        // 粘土作業台
        registry.addRecipes(ClayiumRecipes.clayWorkTable, ClayWorkTableCategory.categoryID);
        registry.addRecipeClickArea(GuiClayWorkTable.class, 48, 33, 80, 12, ClayWorkTableCategory.categoryID);
        recipeTransfer.addRecipeTransferHandler(ContainerClayWorkTable.class, ClayWorkTableCategory.categoryID, 0, 2, 4, 36);
        registry.addRecipeCatalyst(new ItemStack(ClayiumMachines.clayWorkTable), ClayWorkTableCategory.categoryID);

        // クォーツるつぼ
        registry.addIngredientInfo(Arrays.asList(
                    ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.ingot),
                    new ItemStack(ClayiumMachines.quartzCrucible),
                    ClayiumMaterials.get(ClayiumMaterial.silicon, ClayiumShape.ingot)
                ), VanillaTypes.ITEM, "recipe.quartz_crucible.description");

        // 機械たち
        for (EnumMachineKind kind : EnumMachineKind.values()) {
            if (kind == EnumMachineKind.workTable || !kind.hasRecipe()) continue;

            String categoryID = getCategoryID(kind);
            registry.addRecipes(kind.getRecipe(), categoryID);
            recipeTransfer.addRecipeTransferHandler(kind.slotType.containerClass, categoryID, kind.slotType.inStart, kind.slotType.inCount, kind.slotType.playerStart, kind.slotType.playerCount);

            for (ItemStack stack : ClayiumMachines.getSet(kind)) {
                registry.addRecipeCatalyst(stack, categoryID);
            }
        }

        // 粘土樹
        registry.addIngredientInfo(Arrays.asList(new ItemStack(ClayiumBlocks.clayTreeSapling), new ItemStack(ClayiumBlocks.clayTreeLog), new ItemStack(ClayiumBlocks.clayTreeLeaf)), VanillaTypes.ITEM, "recipe.clay_tree.description");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JEICompatibility.jeiRuntime = jeiRuntime;
    }

    /**
     * Use the static field {@link JEICompatibility#jeiRuntime} to show each machine category.<br>
     * <br>
     * REFER TO: <a href="https://github.com/SleepyTrousers/EnderIO/blob/b2754e2c08384a0c51b1289db6cf8f2607ea0d01/enderio-base/src/main/java/crazypants/enderio/base/integration/jei/JeiPlugin.java#L108-L110">Ender IO</a><br>
     */
    public static void showMachineRecipes(EnumMachineKind kind) {
        if (JEICompatibility.jeiRuntime == null || !kind.hasRecipe()) return;

        JEICompatibility.jeiRuntime.getRecipesGui().showCategories(Collections.singletonList(getCategoryID(kind)));
    }

    private static String getCategoryID(EnumMachineKind kind) {
        return ClayiumCore.ModId + "." + kind.getRegisterName();
    }
}
