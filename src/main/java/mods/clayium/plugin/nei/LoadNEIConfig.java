package mods.clayium.plugin.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mods.clayium.block.CBlocks;
import mods.clayium.block.tile.TileQuartzCrucible;
import mods.clayium.block.tile.TileSaltExtractor;
import mods.clayium.block.tile.TileSolarClayFabricator;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.FDText;
import mods.clayium.gui.client.GuiClayCraftingTable;
import mods.clayium.gui.client.GuiClayMachines;
import mods.clayium.gui.container.ContainerClayCraftingTable;
import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilLocale;
import mods.clayium.util.UtilTier;
import mods.clayium.util.crafting.Recipes;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class LoadNEIConfig {
    public static void load() {
        API.registerRecipeHandler(new NEIShapelessSpecialResultRecipe());
        API.registerUsageHandler(new NEIShapelessSpecialResultRecipe());

        for (Iterator<Map.Entry<String, Recipes>> iterator = Recipes.RecipeMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry1 = iterator.next();
            Recipes recipes = (Recipes) entry1.getValue();

            NEIClayMachines catchRecipe = new NEIClayMachines(recipes);
            if (entry1.getKey().equals("Smelter")) {
                catchRecipe.setTierManager(UtilTier.tierSmelter);
            }
            GuiCraftingRecipe.craftinghandlers.add(catchRecipe);
            GuiUsageRecipe.usagehandlers.add(catchRecipe);


            API.registerGuiOverlay(GuiClayMachines.class, catchRecipe.getOverlayIdentifier(), 0, 0);
        }


        TemplateRecipeHandler[] catchRecipes = {new NEIClayWorkTable(), new NEIMetalSeparator()};


        for (TemplateRecipeHandler catchRecipe : catchRecipes) {
            GuiCraftingRecipe.craftinghandlers.add(catchRecipe);
            GuiUsageRecipe.usagehandlers.add(catchRecipe);
            API.registerGuiOverlay(catchRecipe.getGuiClass(), catchRecipe.getOverlayIdentifier(), 0, 0);
        }

        API.registerGuiOverlay(GuiClayCraftingTable.class, "crafting");
        API.registerGuiOverlayHandler(GuiClayCraftingTable.class, new DefaultOverlayHandler() {
            public boolean canMoveFrom(Slot slot, GuiContainer gui) {
                if (gui.inventorySlots instanceof ContainerClayCraftingTable) {
                    IInventory[] inventories = ((ContainerClayCraftingTable) gui.inventorySlots).inventories;
                    for (int i = 1; i < inventories.length; i++) {
                        if (slot.inventory == inventories[i])
                            return true;
                    }
                }
                return slot.inventory instanceof net.minecraft.entity.player.InventoryPlayer;
            }
        }, "crafting");


        int posX = 6;
        int posuY = 23;
        int posY = 43;
        int posdY = 9;
        int width = 158;
        int colorBlack = -16777216;
        int colorGray = 4210752;

        NEIDescription description = new NEIDescription("CobblestoneGenerator", 1);
        GuiCraftingRecipe.craftinghandlers.add(description);
        GuiUsageRecipe.usagehandlers.add(description);

        List<INEIRecipeEntry> list = description.getEntryList();
        ItemStack[] cs = {new ItemStack(Blocks.cobblestone)};

        int[] effciencies = {0, 2, 5, 15, 50, 200, 1000, 8000};
        for (int i = 1; i <= 7; i++) {
            List<PositionedStack> machineList = new ArrayList<PositionedStack>();
            ItemStack[] arrayOfItemStack = {new ItemStack(CBlocks.blocksCobblestoneGenerator[i])};
            machineList.add(new PositionedStack(arrayOfItemStack, 75, 5));
            description.getClass();
            NEITemplateEntry nEITemplateEntry = new NEITemplateEntry(/* description, */(Object[]) arrayOfItemStack, machineList, NEITemp.generateResultPositionedStacks(cs));
            nEITemplateEntry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.CobblestoneGenerator.efficiency", ClayiumCore.multiplyProgressionRateI(effciencies[i]) / 100.0D)), posX, posY, colorBlack, width));
            nEITemplateEntry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.CobblestoneGenerator.description")), posX, posY + posdY, colorGray, width));
            list.add(nEITemplateEntry);
        }

        description = new NEIDescription("SaltExtractor", 1);
        GuiCraftingRecipe.craftinghandlers.add(description);
        GuiUsageRecipe.usagehandlers.add(description);

        list = description.getEntryList();
        ItemStack[] salt = {CMaterials.get(CMaterials.SALT, CMaterials.DUST)};
        int j;
        for (j = 4; j <= 7; j++) {
            List<PositionedStack> machineList = new ArrayList<PositionedStack>();
            ItemStack[] arrayOfItemStack = {new ItemStack(CBlocks.blocksSaltExtractor[j])};
            machineList.add(new PositionedStack(arrayOfItemStack, 75, 5));
            description.getClass();
            NEITemplateEntry nEITemplateEntry = new NEITemplateEntry(/*description, */(Object[]) arrayOfItemStack, machineList, description.generateResultPositionedStacks(salt));
            nEITemplateEntry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.SaltExtractor.efficiency", ClayiumCore.multiplyProgressionRateI(effciencies[j]) / 100.0D)), posX, posY, colorBlack, width));
            nEITemplateEntry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.SaltExtractor.energyConsumption", UtilLocale.ClayEnergyNumeral((ClayiumCore.multiplyProgressionRateI(effciencies[j]) * TileSaltExtractor.energyPerWork)))), posX, posY + posdY, colorBlack, width));
            nEITemplateEntry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.SaltExtractor.description")), posX, posY + posdY * 2, colorGray, width));
            list.add(nEITemplateEntry);
        }

        description = new NEIDescription("QuartzCrucible", 1);
        GuiCraftingRecipe.craftinghandlers.add(description);
        GuiUsageRecipe.usagehandlers.add(description);

        list = description.getEntryList();
        for (j = 1; j <= 9; j++) {
            ItemStack[] toSeacrh = {new ItemStack(CBlocks.blockQuartzCrucible), CMaterials.get(CMaterials.IMPURE_SILICON, CMaterials.INGOT, j), new ItemStack(Items.string)};

            List<PositionedStack> ingredList = new ArrayList<PositionedStack>();
            ItemStack[] arrayOfItemStack1 = {new ItemStack(CBlocks.blockQuartzCrucible)};
            ingredList.add(new PositionedStack(arrayOfItemStack1, 75, 5));
            ItemStack[] ingredItems = {CMaterials.get(CMaterials.IMPURE_SILICON, CMaterials.INGOT, j), new ItemStack(Items.string)};
            ingredList.addAll(description.generateIngredientPositionedStacks((Object[]) ingredItems));

            ItemStack[] resultItems = {CMaterials.get(CMaterials.SILICON, CMaterials.INGOT, j)};

            description.getClass();
            NEITemplateEntry nEITemplateEntry = new NEITemplateEntry(/*description, */(Object[]) toSeacrh, ingredList, description.generateResultPositionedStacks(resultItems));
            nEITemplateEntry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.QuartzCrucible.timeToCraft", TileQuartzCrucible.timeToCraft * j, TileQuartzCrucible.timeToCraft * j / 20)), posX, posY, colorBlack, width));
            nEITemplateEntry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.QuartzCrucible.description")), posX, posY + posdY, colorGray, width));
            list.add(nEITemplateEntry);
        }

        description = new NEIDescription("SolarClayFabricator", 2);
        GuiCraftingRecipe.craftinghandlers.add(description);
        GuiUsageRecipe.usagehandlers.add(description);

        list = description.getEntryList();
        Block[] blocks = new Block[16];
        blocks[5] = CBlocks.blockSolarClayFabricatorMK1;
        blocks[6] = CBlocks.blockSolarClayFabricatorMK2;
        blocks[7] = CBlocks.blockLithiumSolarClayFabricator;
        int[] acceptableTiers = {0, 0, 0, 0, 0, 4, 6, 9};
        float[] baseCraftTimes = {0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 2.0F};
        float[] efficiencies = {0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 5000.0F, 50000.0F, 3000000.0F};
        List[] arrayOfList = new List[13];
        for (int t = 0; t <= 12; t++) {
            List<Object> clist = new ArrayList();
            clist.add(TileSolarClayFabricator.getCompressedClay(t));
            if (t == 2) {
                clist.add(new ItemStack((Block) Blocks.sand));
            }
            if (t == 8) {
                clist.add(CMaterials.getOD(CMaterials.LITHIUM, CMaterials.INGOT));
            }
            arrayOfList[t] = clist;
        }
        for (int k = 5; k <= 7; k++) {
            ItemStack machine = new ItemStack(blocks[k]);

            float multCraftTime = (float) (Math.pow(10.0D, (acceptableTiers[k] + 1)) * (baseCraftTimes[k] - 1.0F) / baseCraftTimes[k] * (Math.pow(baseCraftTimes[k], acceptableTiers[k]) - 1.0D) / (ClayiumCore.multiplyProgressionRateF(efficiencies[k]) / 20.0F));
            for (int m = 0; m <= acceptableTiers[k]; m++) {
                long energy = (long) ((m + 1 < 5) ? 0.0D : Math.pow(10.0D, (m + 1)));
                for (Object clay : arrayOfList[m]) {
                    Object[] toSeacrh = {machine, clay};

                    List<PositionedStack> ingredList = new ArrayList<PositionedStack>();
                    ItemStack[] arrayOfItemStack1 = {machine};
                    ingredList.add(new PositionedStack(arrayOfItemStack1, 75, 5));
                    Object[] ingredItems = {clay};
                    ingredList.addAll(description.generateIngredientPositionedStacks(ingredItems));

                    ItemStack[] resultItems = {TileSolarClayFabricator.getCompressedClay(m + 1)};

                    long timeToCraft = (long) (Math.pow(baseCraftTimes[k], m) * multCraftTime);
                    description.getClass();
                    NEITemplateEntry nEITemplateEntry = new NEITemplateEntry(/*description, */toSeacrh, ingredList, description.generateResultPositionedStacks(resultItems));
                    nEITemplateEntry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.SolarClayFabricator.timeToCraft", UtilLocale.craftTimeNumeral(timeToCraft), timeToCraft / 20L, UtilLocale.ClayEnergyNumeral(energy / timeToCraft))), posX, posY, colorBlack, width));
                    nEITemplateEntry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.SolarClayFabricator.description")), posX, posY + posdY, colorGray, width));

                    list.add(nEITemplateEntry);
                }
            }
        }

        description = new NEIDescription("ClayTree", "clayium:textures/gui/back.png", null, 1);
        GuiCraftingRecipe.craftinghandlers.add(description);
        GuiUsageRecipe.usagehandlers.add(description);

        list = description.getEntryList();

        List<PositionedStack> treeList = new ArrayList<PositionedStack>();
        ItemStack[] is = {new ItemStack(CBlocks.blockClayTreeSapling), new ItemStack(CBlocks.blockClayTreeLog), new ItemStack(CBlocks.blockClayTreeLeaf)};
        treeList.add(new PositionedStack(is, 75, 5));
        description.getClass();
        NEITemplateEntry entry = new NEITemplateEntry(new Object[0], new ArrayList<PositionedStack>(), treeList, false);
        entry.fdList.add(FDText.INSTANCE.getFDText(FDText.getStringHandler(I18n.format("recipe.ClayTree.description")), posX, posuY, colorGray, width));
        list.add(entry);
    }
}
