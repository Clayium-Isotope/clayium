package mods.clayium.plugin.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayWorkTable.ContainerClayWorkTable;
import mods.clayium.machine.ClayWorkTable.GuiClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.ClayiumMachine.GuiClayiumMachine;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.machine.crafting.ClayiumRecipes;

@JEIPlugin
public class JEICompatibility implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers helper = registry.getJeiHelpers();
        final IGuiHelper gui = helper.getGuiHelper();

        registry.addRecipeCategories(new ClayWorkTableCategory(gui));
        registry.addRecipeCategories(new ClayiumMachineCategory(gui, EnumMachineKind.bendingMachine));
        IModPlugin.super.registerCategories(registry);
    }

    @Override
    public void register(IModRegistry registry) {
        final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

        registry.addRecipes(ClayiumRecipes.clayWorkTable, ClayWorkTableCategory.categoryID);
        registry.addRecipeClickArea(GuiClayWorkTable.class, 48, 33, 80, 12, ClayWorkTableCategory.categoryID);
        recipeTransfer.addRecipeTransferHandler(ContainerClayWorkTable.class, ClayWorkTableCategory.categoryID, 0, 2, 4, 36);

        registry.addRecipes(ClayiumRecipes.bendingMachine, ClayiumCore.ModId + "." + EnumMachineKind.bendingMachine.getRegisterName());
        registry.addRecipeClickArea(GuiClayiumMachine.class, (176 - 22) / 2, 35, 22, 16, ClayiumCore.ModId + "." + EnumMachineKind.bendingMachine.getRegisterName());
        recipeTransfer.addRecipeTransferHandler(ContainerClayiumMachine.class, ClayiumCore.ModId + "." + EnumMachineKind.bendingMachine.getRegisterName(), 0, 1, 3, 36);
    }
}
