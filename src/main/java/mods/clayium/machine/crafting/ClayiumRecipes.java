package mods.clayium.machine.crafting;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.ClayiumMaterials;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.ClayiumMachines;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClayiumRecipes {
    private static class SimpleMachineRecipe extends ClayiumRecipe {
        SimpleMachineRecipe(String id) {
            super(id);
        }
    }
    private static class SmeltingRecipe extends SimpleMachineRecipe {
        SmeltingRecipe(String id) {
            super(id);
        }
    }

    public static final ClayWorkTableRecipe clayWorkTable = new ClayWorkTableRecipe();

    public static final SimpleMachineRecipe bendingMachine = new SimpleMachineRecipe("BendingMachine");
    public static final SimpleMachineRecipe wireDrawingMachine = new SimpleMachineRecipe("WireDrawingMachine");
    public static final SimpleMachineRecipe pipeDrawingMachine = new SimpleMachineRecipe("PipeDrawingMachine");
    public static final SimpleMachineRecipe cuttingMachine = new SimpleMachineRecipe("CuttingMachine");
    public static final SimpleMachineRecipe lathe = new SimpleMachineRecipe("Lathe");
    public static final SimpleMachineRecipe millingMachine = new SimpleMachineRecipe("MillingMachine");

    public static final SimpleMachineRecipe condenser = new SimpleMachineRecipe("Condenser");
    public static final SimpleMachineRecipe grinder = new SimpleMachineRecipe("Grinder");
    public static final SimpleMachineRecipe decomposer = new SimpleMachineRecipe("Decomposer");

    public static final SimpleMachineRecipe energeticClayCondenser = new SimpleMachineRecipe("ECCondenser");
    public static final ClayiumRecipe assembler = new ClayiumRecipe("Assembler");
    public static final ClayiumRecipe inscriber = new ClayiumRecipe("Inscriber");
    public static final ClayiumRecipe centrifuge = new ClayiumRecipe("Centrifuge");

    public static final SimpleMachineRecipe smelter = new SmeltingRecipe("Smelter");

    public static final ClayiumRecipe chemicalReactor = new ClayiumRecipe("ChemicalReactor");
    public static final ClayiumRecipe alloySmelter = new ClayiumRecipe("AlloySmelter");
    public static final ClayiumRecipe blastFurnace = new ClayiumRecipe("BlastFurnace");
    public static final SimpleMachineRecipe electrolysisReactor = new SimpleMachineRecipe("ElectrolysisReactor");
    public static final ClayiumRecipe reactor = new ClayiumRecipe("Reactor");
    public static final SimpleMachineRecipe transformer = new SimpleMachineRecipe("MatterTransformer");

    public static final SimpleMachineRecipe CACondenser = new SimpleMachineRecipe("CACondenser");
    public static final ClayiumRecipe CAInjector = new SimpleMachineRecipe("CAInjector");
    public static final SimpleMachineRecipe CAReactor = new SimpleMachineRecipe("CAReactor");

    public static final SimpleMachineRecipe energeticClayDecomposer = new SimpleMachineRecipe("ECDecomposer");

    public static final ClayiumRecipe fluidTransferMachine = new ClayiumRecipe("FluidTransferMachine");


    private static final List<ClayiumMaterial> cmats = Arrays.asList(ClayiumMaterial.clay, ClayiumMaterial.clay, ClayiumMaterial.denseClay, ClayiumMaterial.denseClay, ClayiumMaterial.denseClay);
    private static final List<ClayiumMaterial> mats = Arrays.stream(TierPrefix.values()).map(TierPrefix::getMaterial).collect(Collectors.toList());

    private static final List<ItemStack> circuits = Stream.of(ClayiumItems.clayCircuit, ClayiumItems.clayCircuit, ClayiumItems.clayCircuit, ClayiumItems.simpleCircuit, ClayiumItems.basicCircuit, ClayiumItems.advancedCircuit, ClayiumItems.precisionCircuit, ClayiumItems.integratedCircuit, ClayiumItems.clayCore, ClayiumItems.clayBrain, ClayiumItems.claySpirit, ClayiumItems.claySoul, ClayiumItems.clayAnima, ClayiumItems.clayPsyche).map(ItemStack::new).collect(Collectors.toList());
    private static final List<ItemStack> machines = new ArrayList<ItemStack>() {{
        add(new ItemStack(ClayiumBlocks.rawClayMachineHull));
        addAll(ClayiumBlocks.machineHulls.stream().map(ItemStack::new).collect(Collectors.toList()));
    }};


    public static void registerRecipes() {
        registerMainMaterials();
        registerHulls();
        registerMachines();
        registerClayWorkTableRecipe();
        registerMaterials();
        registerTools();
    }

    private static List<ItemStack> oo(Object... elements) {
        List<ItemStack> res = new ArrayList<>();
        for (Object o : elements) {
            if (o instanceof ItemStack)
                res.add((ItemStack) o);
            else if (o instanceof Item)
                res.add(new ItemStack((Item) o));
            else if (o instanceof Block)
                res.add(new ItemStack((Block) o));
        }
        return res;
    }
    private static List<ItemStack> ii(ItemStack... elements) {
        return Arrays.asList(elements);
    }
    private static List<ItemStack> ii(Item... elements) {
        return Arrays.stream(elements).map(ItemStack::new).collect(Collectors.toList());
    }
    private static ItemStack i(Block block) {
        return i(Item.getItemFromBlock(block));
    }
    private static ItemStack i(Block block, int amount) {
        return i(Item.getItemFromBlock(block), amount);
    }
    private static ItemStack i(Block block, int amount, int meta) {
        return i(block, amount);
    }
    private static ItemStack i(Item item) {
        return new ItemStack(item);
    }
    private static ItemStack i(Item itemIn, int amount) {
        return new ItemStack(itemIn, amount);
    }
    private static ItemStack i(Item itemIn, int amount, int meta) {
        return new ItemStack(itemIn, amount, meta);
    }
    private static ItemStack s(ItemStack itemStackIn, int newSize) {
        ItemStack res = itemStackIn.copy();
        res.setCount(newSize);
        return res;
    }
    private static ItemStack s(Item itemIn, int newSize) {
        return i(itemIn, newSize);
    }
    private static ItemStack w(Item item) {
        return new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
    }

    private static long e(int tier) {
        return e(1.0D, tier);
    }
    private static long e(double factor, int tier) {
        return (long) (factor * 100.0D * Math.pow(10.0D, (tier - 4)));
    }

    public static boolean hasResult(ClayiumRecipe recipes, List<ItemStack> stack) {
        for (IRecipeElement recipe : recipes)
            if (recipe.match(stack)) return true;

        return false;
    }

    public static boolean hasResult(ClayiumRecipe recipes, ItemStack... stacks) {
        return hasResult(recipes, ii(stacks));
    }

    public static void registerClayWorkTableRecipe() {
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball), 0, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick), ItemStack.EMPTY, 4L);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), 1, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), ItemStack.EMPTY, 30);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), 2, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 2), 4);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), 0, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ItemStack.EMPTY, 4);

        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), 1, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.blade), ItemStack.EMPTY, 10);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), 2, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.blade), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 2), 1);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), 5, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick, 4), ItemStack.EMPTY, 3);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate, 6), 2, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ItemStack.EMPTY, 10);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate, 3), 0, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ItemStack.EMPTY, 40);

        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), 3, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 2), 4);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), 4, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ring), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallRing), 2);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), 1, i(ClayiumItems.rawSlicer), ItemStack.EMPTY, 15);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), 2, i(ClayiumItems.rawSlicer), ItemStack.EMPTY, 2);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallDisc), 4, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallRing), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.shortStick), 1);

        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), 0, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.needle), ItemStack.EMPTY, 3);
        clayWorkTable.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), 5, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallDisc, 8), ItemStack.EMPTY, 7);
    }

    public static void registerTools() {
        GameRegistry.addSmelting(ClayiumItems.rawRollingPin, new ItemStack(ClayiumItems.rollingPin), 1F);
        GameRegistry.addSmelting(ClayiumItems.rawSlicer, new ItemStack(ClayiumItems.slicer), 1F);
        GameRegistry.addSmelting(ClayiumItems.rawSpatula, new ItemStack(ClayiumItems.spatula), 1F);

        assembler.addRecipe(oo(i(ClayiumItems.rollingPin, 1, OreDictionary.WILDCARD_VALUE), i(ClayiumItems.slicer, 1, OreDictionary.WILDCARD_VALUE)), 6,
                ii(ClayiumItems.IOTool), e(6), 20L);
        assembler.addRecipe(oo(i(ClayiumItems.spatula, 1, OreDictionary.WILDCARD_VALUE), i(ClayiumItems.wrench)), 6,
                ii(ClayiumItems.pipingTool), e(6), 20L);
        assembler.addRecipe(oo(ClayiumItems.IOTool, s(circuits.get(6), 2)), 6,
                ii(ClayiumItems.memoryCard), e(6), 20L);

// TODO   assembler.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.spindle), s(circuits.get(6), 2)), 0, 6,
//                ii(i(CItems.itemDirectionMemory)), e(6), 20L);
/* Use assets/recipes
        GameRegistry.addRecipe(i(CItems.itemClayShovel), "#", "|", "|",
                '#', ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), '|', ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.STICK));
        GameRegistry.addRecipe(i(CItems.itemClayPickaxe), "###", " | ", " | ",
                '#', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.plate), '|', ClayiumMaterials.get(ClayiumMaterial.denseClay, CMaterials.STICK));
        GameRegistry.addRecipe(i(CItems.itemClayWrench), "# #", " o ", " | ",
                '#', ClayiumMaterials.get(ClayiumMaterial.denseClay, CMaterials.BLADE), '|', ClayiumMaterials.get(ClayiumMaterial.denseClay, CMaterials.STICK), 'o', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.spindle));


        GameRegistry.addRecipe(new ShapedOreRecipe(
                i(CItems.itemClaySteelPickaxe), "###", " | ", " | ",
                '#', ClayiumMaterials.getODName(ClayiumMaterial.clay_STEEL, ClayiumShape.ingot), '|', ClayiumMaterials.get(ClayiumMaterial.denseClay, CMaterials.STICK)));
        GameRegistry.addRecipe(new ShapedOreRecipe(
                i(CItems.itemClaySteelShovel), "#", "|", "|",
                '#', ClayiumMaterials.getODName(ClayiumMaterial.clay_STEEL, ClayiumShape.ingot), '|', ClayiumMaterials.get(ClayiumMaterial.denseClay, CMaterials.STICK)));
 */
// TODO    assembler.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 3), i(ClayiumItems.synchronousParts, 2)), 0, 6,
//                ii(i(CItems.itemSynchronizer)), e(6), 20L);


        assembler.addRecipe(oo(circuits.get(5), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.plate, 3)), 4, ii(i(ClayiumItems.filterWhitelist)), 8L, 20L);
        assembler.addRecipe(oo(circuits.get(6), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.plate, 3)), 4, ii(i(ClayiumItems.filterItemName)), 8L, 20L);
        assembler.addRecipe(oo(circuits.get(7), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.plate, 3)), 4, ii(i(ClayiumItems.filterFuzzy)), 8L, 20L);
/* Use assets/recipes
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterWhitelist), i(CItems.itemFilterBlacklist));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterBlacklist), i(CItems.itemFilterWhitelist));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterUnlocalizedName), i(CItems.itemFilterItemName));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterOreDict), i(CItems.itemFilterUnlocalizedName));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterUniqueId), i(CItems.itemFilterOreDict));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterModId), i(CItems.itemFilterUniqueId));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterItemDamage), i(CItems.itemFilterModId));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterItemName), i(CItems.itemFilterItemDamage));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterFuzzy), i(CItems.itemFilterFuzzy));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterDuplicator),
                i(CItems.itemFilterWhitelist), i(CItems.itemFilterItemName), i(CItems.itemFilterFuzzy));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterBlockMetadata), i(CItems.itemFilterItemDamage), i(Blocks.clay));
        GameRegistry.addShapelessRecipe(i(CItems.itemFilterItemDamage), i(CItems.itemFilterBlockMetadata), i(Blocks.clay));
 */
        if (ClayiumConfiguration.cfgEnableFluidCapsule) {
/* TODO
            ItemCapsule.registerCompressionRecipe(new ItemCapsule[] {CItems.itemsCapsule[4], CItems.itemsCapsule[3], CItems.itemsCapsule[2], CItems.itemsCapsule[1], CItems.itemsCapsule[0]}, new int[] {5, 5, 5, 8});

            GameRegistry.addRecipe(i(CItems.itemsCapsule[0]), " # ", "# #", " # ",
                    '#', i(ClayiumBlocks.compressedClay.get(0), 1, 0));
 */
        }

/* TODO
        assembler.addRecipe(oo(i(Items.leather, 4), ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 8)), 0, 4,
                ii(i(CItems.itemGadgetHolder)), e(6), 120L);

        assembler.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.plate, 8), ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 4)), 0, 4,
                ii(CItems.itemGadget.get("Blank")), e(6), 120L);

        assembler.addRecipe(oo(CItems.itemGadget.get("Blank"), ClayiumBlocks.overclocker.get(TierPrefix.antimatter)), 0, 10,
                ii(CItems.itemGadget.get("AntimatterOverclock")), e(10), 120L);
        assembler.addRecipe(oo(CItems.itemGadget.get("AntimatterOverclock"), ClayiumBlocks.overclocker.get(TierPrefix.pureAntimatter)), 0, 10,
                ii(CItems.itemGadget.get("PureAntimatterOverclock")), e(11), 120L);
        assembler.addRecipe(oo(CItems.itemGadget.get("PureAntimatterOverclock"), ClayiumBlocks.overclocker.get(TierPrefix.OEC)), 0, 10,
                ii(CItems.itemGadget.get("OECOverclock")), e(12), 120L);
        assembler.addRecipe(oo(CItems.itemGadget.get("OECOverclock"), ClayiumBlocks.overclocker.get(TierPrefix.OPA)), 0, 10,
                ii(CItems.itemGadget.get("OPAOverclock")), e(13), 120L);

        assembler.addRecipe(oo(CItems.itemGadget.get("Blank"), s(circuits[12], 16)), 0, 10,
                ii(CItems.itemGadget.get("Flight0")), e(12), 120L);
        assembler.addRecipe(oo(CItems.itemGadget.get("Flight0"), s(circuits[13], 16)), 0, 10,
                ii(CItems.itemGadget.get("Flight1")), e(13), 1200L);
        assembler.addRecipe(oo(CItems.itemGadget.get("Flight1"), ClayiumBlocks.overclocker.get(TierPrefix.OPA, 16)), 0, 10,
                ii(CItems.itemGadget.get("Flight2")), e(14), 12000L);

        assembler.addRecipe(oo(CItems.itemGadget.get("Blank"), s(circuits[6], 4)), 0, 4,
                ii(CItems.itemGadget.get("Health0")), e(6), 120L);
        assembler.addRecipe(oo(CItems.itemGadget.get("Health0"), s(circuits[10], 4)), 0, 4,
                ii(CItems.itemGadget.get("Health1")), e(10), 120L);
        assembler.addRecipe(oo(CItems.itemGadget.get("Health1"), s(circuits[12], 4)), 0, 10,
                ii(CItems.itemGadget.get("Health2")), e(12), 120L);

        assembler.addRecipe(oo(CItems.itemGadget.get("Blank"), s(circuits[7], 2)), 0, 4,
                ii(CItems.itemGadget.get("AutoEat0")), e(7), 120L);


        GameRegistry.addRecipe(new ShapelesssWithNBT(CItems.itemGadget.get("AutoEat0"), CItems.itemGadget.get("AutoEat1")));
        GameRegistry.addRecipe(new ShapelesssWithNBT(CItems.itemGadget.get("AutoEat1"), CItems.itemGadget.get("AutoEat0")));

        assembler.addRecipe(oo(CItems.itemGadget.get("AntimatterOverclock"), s(circuits[10], 4)), 0, 10,
                ii(CItems.itemGadget.get("RepeatedlyAttack")), e(10), 120L);
 */
        assembler.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.ingot, 16), circuits.get(6)), 4,
                ii(ClayiumItems.manipulator1), e(4), 20L);
        assembler.addRecipe(oo(ClayiumItems.manipulator1, s(circuits.get(8), 6)), 4,
                ii(ClayiumItems.manipulator2), e(8), 20L);
        assembler.addRecipe(oo(i(ClayiumItems.manipulator2, 64), s(circuits.get(12), 6)), 10,
                ii(ClayiumItems.manipulator3), e(12), 20L);

/* TODO
        assembler.addRecipe(oo(CItems.itemGadget.get("Blank"), CItems.itemMisc.get("Manipulator1")), 0, 4,
                ii(CItems.itemGadget.get("LongArm0")), e(4), 120L);
        assembler.addRecipe(oo(CItems.itemGadget.get("LongArm0"), CItems.itemMisc.get("Manipulator2")), 0, 4,
                ii(CItems.itemGadget.get("LongArm1")), e(8), 120L);
        assembler.addRecipe(oo(CItems.itemGadget.get("LongArm1"), CItems.itemMisc.get("Manipulator3")), 0, 10,
                ii(CItems.itemGadget.get("LongArm2")), e(12), 120L);
 */
/* TODO
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 4), circuits.get(4)), 0, 4,
                ii(i(CItems.itemsClayShooter[0])), e(4), 40L);
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 4), circuits.get(5)), 0, 4,
                ii(i(CItems.itemsClayShooter[1])), e(5), 40L);
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 4), circuits.get(6)), 0, 4,
                ii(i(CItems.itemsClayShooter[2])), e(6), 40L);
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 4), circuits.get(7)), 0, 4,
                ii(i(CItems.itemsClayShooter[3])), e(7), 40L);
 */
    }

    public static void registerMainMaterials() {
/* Used assets/recipes
        GameRegistry.addRecipe(i(ClayiumBlocks.compressedClay.get(0), 1, 0), "###", "###", "###",
                '#', Blocks.clay);
        GameRegistry.addShapelessRecipe(i(Blocks.clay, 9),
                i(ClayiumBlocks.compressedClay.get(0), 1, 0));
        GameRegistry.addRecipe(i(ClayiumBlocks.compressedClay.get(1), 1, 1), "###", "###", "###",
                '#', i(ClayiumBlocks.compressedClay.get(0), 1, 0));
        GameRegistry.addShapelessRecipe(i(ClayiumBlocks.compressedClay.get(0), 9, 0),
                i(ClayiumBlocks.compressedClay.get(1), 1, 1));
*/

        condenser.addRecipe(i(Blocks.CLAY, 9), i(ClayiumBlocks.compressedClay.get(0), 1), 1L, 4L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(0), 9, 0), i(ClayiumBlocks.compressedClay.get(1), 1, 1), 1L, 4L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(1), 9, 1), i(ClayiumBlocks.compressedClay.get(2), 1, 2), 10L, 4L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(2), 9, 2), i(ClayiumBlocks.compressedClay.get(3), 1, 3), 100L, 4L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(3), 9, 3), 4, i(ClayiumBlocks.compressedClay.get(4), 1, 4), 100L, 16L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(4), 9, 4), 4, i(ClayiumBlocks.compressedClay.get(5), 1, 5), 1000L, 16L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(5), 9, 5), 4, i(ClayiumBlocks.compressedClay.get(6), 1, 6), 10000L, 13L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(6), 9, 6), 5, i(ClayiumBlocks.compressedClay.get(7), 1, 7), 100000L, 10L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(7), 9, 7), 5, i(ClayiumBlocks.compressedClay.get(8), 1, 8), 1000000L, 8L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(8), 9, 8), 5, i(ClayiumBlocks.compressedClay.get(9), 1, 9), 10000000L, 6L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(9), 9, 9), 5, i(ClayiumBlocks.compressedClay.get(10), 1, 10), 100000000L, 4L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(10), 9, 10), 5, i(ClayiumBlocks.compressedClay.get(11), 1, 11), 1000000000L, 3L);
        condenser.addRecipe(i(ClayiumBlocks.compressedClay.get(11), 9, 11), 5, i(ClayiumBlocks.compressedClay.get(12), 1, 12), 1000000000L, 25L);

        energeticClayCondenser.addRecipe(i(ClayiumBlocks.compressedClay.get(3), 9, 3), 3, i(ClayiumBlocks.compressedClay.get(4), 1, 4), 1L, 16L);
        energeticClayCondenser.addRecipe(i(ClayiumBlocks.compressedClay.get(4), 9, 4), 3, i(ClayiumBlocks.compressedClay.get(5), 1, 5), 10L, 32L);
        energeticClayCondenser.addRecipe(i(ClayiumBlocks.compressedClay.get(5), 9, 5), 3, i(ClayiumBlocks.compressedClay.get(6), 1, 6), 100L, 64L);
        energeticClayCondenser.addRecipe(i(ClayiumBlocks.compressedClay.get(6), 9, 6), 4, i(ClayiumBlocks.compressedClay.get(7), 1, 7), 1000L, 64L);
        energeticClayCondenser.addRecipe(i(ClayiumBlocks.compressedClay.get(7), 9, 7), 4, i(ClayiumBlocks.compressedClay.get(8), 1, 8), 10000L, 64L);
        energeticClayCondenser.addRecipe(i(ClayiumBlocks.compressedClay.get(8), 9, 8), 4, i(ClayiumBlocks.compressedClay.get(9), 1, 9), 100000L, 64L);
        energeticClayCondenser.addRecipe(i(ClayiumBlocks.compressedClay.get(9), 9, 9), 4, i(ClayiumBlocks.compressedClay.get(10), 1, 10), 1000000L, 64L);
        energeticClayCondenser.addRecipe(i(ClayiumBlocks.compressedClay.get(10), 9, 10), 4, i(ClayiumBlocks.compressedClay.get(11), 1, 11), 10000000L, 64L);
        energeticClayCondenser.addRecipe(i(ClayiumBlocks.compressedClay.get(11), 9, 11), 4, i(ClayiumBlocks.compressedClay.get(12), 1, 12), 10000000L, 64L);

        decomposer.addRecipe(i(Blocks.CLAY), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 4), 1L, 3L);
        decomposer.addRecipe(i(ClayiumBlocks.compressedClay.get(0), 1, 0), i(Blocks.CLAY, 9), 1L, 3L);
        decomposer.addRecipe(i(ClayiumBlocks.compressedClay.get(1), 1, 1), i(ClayiumBlocks.compressedClay.get(0), 9, 0), 1L, 3L);
        decomposer.addRecipe(i(ClayiumBlocks.compressedClay.get(2), 1, 2), i(ClayiumBlocks.compressedClay.get(1), 9, 1), 1L, 10L);
        decomposer.addRecipe(i(ClayiumBlocks.compressedClay.get(3), 1, 3), i(ClayiumBlocks.compressedClay.get(2), 9, 2), 1L, 20L);

        energeticClayDecomposer.addRecipe(i(Blocks.CLAY), 13, ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 4), 1L, 0L);
        energeticClayDecomposer.addRecipe(i(ClayiumBlocks.compressedClay.get(0), 1, 0), 13, i(Blocks.CLAY, 9), 1L, 0L);
        int i;
        for (i = 0; i < 12; i++) {
            energeticClayDecomposer.addRecipe(i(ClayiumBlocks.compressedClay.get(i + 1), 1, i + 1), 13, i(ClayiumBlocks.compressedClay.get(i), 9, i), 1L, 0L);
        }

/* Used assets/recipes
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.LARGE_BALL),
                ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL),
                ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL));
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.SHORT_STICK, 2), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.STICK));
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.SMALL_RING), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.SHORT_STICK));
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.SHORT_STICK), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.SMALL_RING));
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.RING), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder));
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.pipe), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate));
        GameRegistry.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largePlate), "###", "###", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate));


        for (CMaterial material : new CMaterial[] {ClayiumMaterial.clay, ClayiumMaterial.denseClay}) {
            GameRegistry.addRecipe(ClayiumMaterials.get(material, ClayiumShape.gear), "iii", "ioi", "iii",
                    'i', ClayiumMaterials.get(material, CMaterials.SHORT_STICK), 'o', ClayiumMaterials.get(material, CMaterials.SMALL_RING));
            GameRegistry.addRecipe(ClayiumMaterials.get(material, ClayiumShape.cuttingHead), "iii", "ioi", "iii",
                    'i', ClayiumMaterials.get(material, CMaterials.BLADE), 'o', ClayiumMaterials.get(material, CMaterials.RING));
            GameRegistry.addRecipe(ClayiumMaterials.get(material, CMaterials.BEARING), "iii", "ioi", "iii",
                    'i', ClayiumMaterials.get(material, CMaterials.BALL), 'o', ClayiumMaterials.get(material, CMaterials.RING));
            GameRegistry.addRecipe(ClayiumMaterials.get(material, ClayiumShape.spindle), "0#0", "ioO", "0#0",
                    'i', ClayiumMaterials.get(material, CMaterials.STICK), 'o', ClayiumMaterials.get(material, CMaterials.BEARING), 'O', ClayiumMaterials.get(material, CMaterials.RING), '#', ClayiumMaterials.get(material, ClayiumShape.plate), '0', ClayiumMaterials.get(material, CMaterials.SMALL_RING));
            GameRegistry.addRecipe(ClayiumMaterials.get(material, ClayiumShape.grindingHead), "iii", "ioi", "iii",
                    'i', ClayiumMaterials.get(material, CMaterials.NEEDLE), 'o', ClayiumMaterials.get(material, CMaterials.RING));
            GameRegistry.addRecipe(ClayiumMaterials.get(material, CMaterials.WATER_WHEEL), "###", "#o#", "###",
                    '#', ClayiumMaterials.get(material, ClayiumShape.plate), 'o', ClayiumMaterials.get(material, CMaterials.RING));
        }
*/

        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallDisc), ClayiumCore.divideByProgressionRate(3));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), ClayiumCore.divideByProgressionRate(2));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), i(ClayiumItems.rawSlicer), ClayiumCore.divideByProgressionRate(1));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.block), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), ClayiumCore.divideByProgressionRate(1));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate, 4), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumCore.divideByProgressionRate(4));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.blade, 2), ClayiumCore.divideByProgressionRate(4));

        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.plate), ClayiumCore.divideByProgressionRate(4));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.plate, 4), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumCore.divideByProgressionRate(8));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.blade, 2), ClayiumCore.divideByProgressionRate(8));

        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.block), 2, ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.plate), 2L, ClayiumCore.divideByProgressionRate(4));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.plate, 4), 2, ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.largePlate), 2L, ClayiumCore.divideByProgressionRate(8));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.block), 2, ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.plate), 4L, ClayiumCore.divideByProgressionRate(4));
        bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.plate, 4), 2, ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.largePlate), 4L, ClayiumCore.divideByProgressionRate(8));

        wireDrawingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick), ClayiumCore.divideByProgressionRate(1));
        wireDrawingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick, 8), ClayiumCore.divideByProgressionRate(3));
        wireDrawingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.pipe), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick, 4), ClayiumCore.divideByProgressionRate(2));
        wireDrawingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallDisc), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick), ClayiumCore.divideByProgressionRate(1));

        wireDrawingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.stick, 8), ClayiumCore.divideByProgressionRate(6));
        wireDrawingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.pipe), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.stick, 4), ClayiumCore.divideByProgressionRate(4));
        wireDrawingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.smallDisc), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.stick), ClayiumCore.divideByProgressionRate(2));

        pipeDrawingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.pipe, 2), ClayiumCore.divideByProgressionRate(3));
        pipeDrawingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.pipe, 2), ClayiumCore.divideByProgressionRate(6));

        cuttingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), ClayiumCore.divideByProgressionRate(2));
        cuttingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallDisc, 8), ClayiumCore.divideByProgressionRate(2));
        cuttingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc, 2), ClayiumCore.divideByProgressionRate(3));
        cuttingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallDisc, 4), ClayiumCore.divideByProgressionRate(3));
        cuttingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.shortStick, 2), ClayiumCore.divideByProgressionRate(1));

        cuttingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.smallDisc, 8), ClayiumCore.divideByProgressionRate(4));
        cuttingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.disc, 2), ClayiumCore.divideByProgressionRate(6));
        cuttingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.plate), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.smallDisc, 4), ClayiumCore.divideByProgressionRate(6));
        cuttingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.stick), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.shortStick, 2), ClayiumCore.divideByProgressionRate(2));

        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.shortStick), ClayiumCore.divideByProgressionRate(1));
        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumCore.divideByProgressionRate(4));
        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.needle), ClayiumCore.divideByProgressionRate(3));
        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.needle), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick, 6), ClayiumCore.divideByProgressionRate(3));
        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.disc), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ring), ClayiumCore.divideByProgressionRate(2));
        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallDisc), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.smallRing), ClayiumCore.divideByProgressionRate(1));

        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block, 2), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumCore.divideByProgressionRate(4));
        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.needle), ClayiumCore.divideByProgressionRate(6));
        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.needle), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.stick, 6), ClayiumCore.divideByProgressionRate(6));
        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.disc), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.ring), ClayiumCore.divideByProgressionRate(4));
        lathe.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.smallDisc), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.smallRing), ClayiumCore.divideByProgressionRate(2));


        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.stick, 5)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.gear)), 10L, ClayiumCore.divideByProgressionRate(20));
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.shortStick, 9)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.gear)), 10L, ClayiumCore.divideByProgressionRate(20));
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 8)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.spindle)), 10L, ClayiumCore.divideByProgressionRate(20));
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.block, 8)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.grindingHead)), 10L, ClayiumCore.divideByProgressionRate(20));
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.plate, 8)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cuttingHead)), 10L, ClayiumCore.divideByProgressionRate(20));
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.stick, 5)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.gear)), 10L, ClayiumCore.divideByProgressionRate(20));
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.shortStick, 9)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.gear)), 10L, ClayiumCore.divideByProgressionRate(20));
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.ball, 8)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.spindle)), 100L, ClayiumCore.divideByProgressionRate(20));
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block, 8)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.grindingHead)), 100L, ClayiumCore.divideByProgressionRate(20));
        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.plate, 8)), 3,
                ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cuttingHead)), 100L, ClayiumCore.divideByProgressionRate(20));

/*
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL, 3), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.RING));
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL, 3), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.gear));
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL, 3), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BLADE));
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.BALL, 5), ClayiumMaterials.get(ClayiumMaterial.clay, CMaterials.NEEDLE));
*/
        for (ClayiumMaterial material : new ClayiumMaterial[] {ClayiumMaterial.clay, ClayiumMaterial.denseClay}) {
            int j = (material == ClayiumMaterial.clay) ? 1 : 4;
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.plate), ClayiumMaterials.get(material, ClayiumShape.dust), 1L, (3 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.stick, 4), ClayiumMaterials.get(material, ClayiumShape.dust), 1L, (3 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.shortStick, 8), ClayiumMaterials.get(material, ClayiumShape.dust), 1L, (3 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.ring, 4), ClayiumMaterials.get(material, ClayiumShape.dust, 5), 1L, (15 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.smallRing, 8), ClayiumMaterials.get(material, ClayiumShape.dust), 1L, (3 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.gear, 8), ClayiumMaterials.get(material, ClayiumShape.dust, 9), 1L, (27 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.blade), ClayiumMaterials.get(material, ClayiumShape.dust), 1L, (3 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.needle), ClayiumMaterials.get(material, ClayiumShape.dust, 2), 1L, (6 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.disc, 2), ClayiumMaterials.get(material, ClayiumShape.dust, 3), 1L, (9 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.smallDisc, 4), ClayiumMaterials.get(material, ClayiumShape.dust), 1L, (3 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.cylinder), ClayiumMaterials.get(material, ClayiumShape.dust, 2), 1L, (6 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.pipe), ClayiumMaterials.get(material, ClayiumShape.dust), 1L, (3 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.largePlate), ClayiumMaterials.get(material, ClayiumShape.dust, 4), 1L, (12 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.grindingHead), ClayiumMaterials.get(material, ClayiumShape.dust, 16), 1L, (48 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.bearing, 4), ClayiumMaterials.get(material, ClayiumShape.dust, 5), 1L, (15 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.spindle), ClayiumMaterials.get(material, ClayiumShape.dust, 4), 1L, (12 * j));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.cuttingHead), ClayiumMaterials.get(material, ClayiumShape.dust, 9), 1L, (27 * j));
        }


        decomposer.addRecipe(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 3), 1L, ClayiumCore.divideByProgressionRate(60));
        decomposer.addRecipe(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust), 4, ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 28), 1000L, ClayiumCore.divideByProgressionRate(60));

        centrifuge.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.dust, 9)), 0, ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.dust)), 4L, ClayiumCore.divideByProgressionRate(20));
        centrifuge.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.dust, 2)), 0, ii(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.dust, 9), ClayiumMaterials.get(ClayiumMaterial.calClay, ClayiumShape.dust, 1)), 4L, ClayiumCore.divideByProgressionRate(20));
        centrifuge.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust, 2)), 0, ii(ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 12), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.dust, 8), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.dust, 8), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 4L, ClayiumCore.divideByProgressionRate(20));
        centrifuge.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust, 2)), 4, ii(ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 64), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.dust, 64), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.dust, 64), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust, 12)), 10000L, ClayiumCore.divideByProgressionRate(12));


        chemicalReactor.addRecipe(oo(ClayiumMaterials.getOD(ClayiumMaterial.salt, ClayiumShape.dust, 2), ClayiumMaterials.get(ClayiumMaterial.calClay, ClayiumShape.dust)), 0, ii(ClayiumMaterials.get(ClayiumMaterial.calciumChloride, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.sodiumCarbonate, ClayiumShape.dust)), e(5), 120L);
        chemicalReactor.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.sodiumCarbonate, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.dust)), 0, ii(ClayiumMaterials.get(ClayiumMaterial.quartz, ClayiumShape.dust)), e(4), 120L);
        chemicalReactor.addRecipe(oo(ClayiumMaterials.getOD(ClayiumMaterial.quartz, ClayiumShape.dust), i(Items.COAL)), 0, ii(ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.ingot)), e(4), 120L);
        chemicalReactor.addRecipe(oo(ClayiumMaterials.getOD(ClayiumMaterial.quartz, ClayiumShape.dust), i(Items.COAL, 1, 1)), 0, ii(ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.ingot)), e(4), 120L);


        chemicalReactor.addRecipe(oo(ClayiumMaterials.getOD(ClayiumMaterial.salt, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 8, ii(ClayiumMaterials.get(ClayiumMaterial.quartz, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.calciumChloride, ClayiumShape.dust)), e(10.0D, 8), 1L);
        chemicalReactor.addRecipe(oo(ClayiumMaterials.getOD(ClayiumMaterial.quartz, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 8, ii(ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.ingot)), e(10.0D, 8), 1L);
        blastFurnace.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.ingot)), 7, ii(ClayiumMaterials.get(ClayiumMaterial.silicon, ClayiumShape.ingot)), e(7), 100L);


        chemicalReactor.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.dust)), 5, ii(ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.mainAluminium, ClayiumShape.dust)), e(5), 30L);


        blastFurnace.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust, 2), ClayiumMaterials.get(ClayiumMaterial.impureManganese, ClayiumShape.dust)), 6, ii(ClayiumMaterials.get(ClayiumMaterial.claySteel, ClayiumShape.ingot, 2)), e(5.0D, 6), 200L);
        blastFurnace.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.impureManganese, ClayiumShape.dust)), 7, ii(ClayiumMaterials.get(ClayiumMaterial.claySteel, ClayiumShape.ingot)), e(5.0D, 7), 5L);
        blastFurnace.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust), ClayiumMaterials.getOD(ClayiumMaterial.manganese, ClayiumShape.dust)), 8, ii(ClayiumMaterials.get(ClayiumMaterial.claySteel, ClayiumShape.ingot)), e(5.0D, 8), 1L);


        reactor.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust, 8), ClayiumMaterials.getOD(ClayiumMaterial.lithium, ClayiumShape.dust, 4)), 7, ii(ClayiumMaterials.get(ClayiumMaterial.clayium, ClayiumShape.dust, 8)), e(10.0D, 7), 50000L);
        reactor.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust, 8), ClayiumMaterials.getOD(ClayiumMaterial.hafnium, ClayiumShape.dust)), 7, ii(ClayiumMaterials.get(ClayiumMaterial.clayium, ClayiumShape.dust, 8)), e(10.0D, 7), 500000L);
        reactor.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust, 8), ClayiumMaterials.getOD(ClayiumMaterial.barium, ClayiumShape.dust)), 7, ii(ClayiumMaterials.get(ClayiumMaterial.clayium, ClayiumShape.dust, 8)), e(3.0D, 7), 5000000L);
        reactor.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust, 8), ClayiumMaterials.getOD(ClayiumMaterial.strontium, ClayiumShape.dust)), 7, ii(ClayiumMaterials.get(ClayiumMaterial.clayium, ClayiumShape.dust, 8)), e(7), 50000000L);

        reactor.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.impureUltimateAlloy, ClayiumShape.ingot)), 8, ii(ClayiumMaterials.get(ClayiumMaterial.ultimateAlloy, ClayiumShape.ingot)), e(10.0D, 8), 1000000000L);


        reactor.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 8), ClayiumMaterials.getOD(ClayiumMaterial.lithium, ClayiumShape.dust)), 7, ii(ClayiumMaterials.get(ClayiumMaterial.excClay, ClayiumShape.dust, 4)), e(7), 2000000L);


        grinder.addRecipe(i(ClayiumBlocks.clayTreeLog), 5, ClayiumMaterials.get(ClayiumMaterial.orgClay, ClayiumShape.dust), e(5), 200L);
        reactor.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.orgClay, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust)), 10, ii(ClayiumMaterials.get(ClayiumMaterial.orgClay, ClayiumShape.dust, 2)), e(10), 1000000000000L);
        reactor.addRecipe(ii(i(ClayiumItems.claySoul), ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust)), 11, ii(ClayiumMaterials.get(ClayiumMaterial.orgClay, ClayiumShape.dust, 2)), e(11), 100000000000000L);


        reactor.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clayium, ClayiumShape.ingot), 9, i(ClayiumItems.antimatterSeed), e(9), ClayiumCore.divideByProgressionRate(200000000000000L));
        CACondenser.addRecipe(i(ClayiumItems.antimatterSeed), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), e(2.5D, 9), ClayiumCore.divideByProgressionRate(2000L));


        CAInjector.addRecipe(ii(machines.get(10), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 8)), 10, ii(i(ClayiumMachines.CACollector)), e(2.0D, 10), 4000L);
        CAReactor.addRecipe(ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.get(ClayiumMaterial.pureAntimatter, ClayiumShape.gem), e(0.1D, 10), ClayiumCore.divideByProgressionRate(300));
        for (i = 0; i < 8; i++) {
            condenser.addRecipe(ClayiumMaterials.get(ClayiumMaterial.compressedPureAntimatter.get(i), ClayiumShape.gem, 9), 10, ClayiumMaterials.get(ClayiumMaterial.compressedPureAntimatter.get(i + 1), ClayiumShape.gem), e(9), 6L);
//            GameRegistry.addShapelessRecipe(ClayiumMaterials.get(CMaterials.COMPRESSED_PURE_ANTIMATTER[i], ClayiumShape.gem, 9), ClayiumMaterials.get(CMaterials.COMPRESSED_PURE_ANTIMATTER[i + 1], ClayiumShape.gem));
        }
    }

    public static void registerHulls() {
/* Used assets/recipes
        GameRegistry.addRecipe(i(CBlocks.blockRawClayMachineHull, 1, 0), "###", "#o#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.largePlate), 'o', ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.gear));
*/
        GameRegistry.addSmelting(ClayiumBlocks.rawClayMachineHull, new ItemStack(ClayiumBlocks.machineHulls.get(0)), 0.1F);
/*        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 1), "###", "#C#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), 'C', i(ClayiumItems.clayCircuit));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 2), "###", "#C#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.largePlate), 'C', CItems.itemMisc.get("SimpleCircuit"));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 3), "#E#", "#C#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.largePlate), 'C', i(ClayiumItems.basicCircuit), 'E', i(ClayiumItems.CEE));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 4), "#E#", "*C*", "#*#",
                '#', ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.largePlate), '*', ClayiumMaterials.get(ClayiumMaterial.siliconE, ClayiumShape.largePlate), 'C', i(ClayiumItems.advancedCircuit), 'E', i(ClayiumItems.CEE));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 5), "#E#", "#C#", "###",
                '#', ClayiumMaterials.get(CMaterials.MAIN_ALUMINIUM, ClayiumShape.largePlate), 'C', i(ClayiumItems.precisionCircuit), 'E', i(ClayiumItems.CEE));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 6), "#E#", "#C#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.clay_STEEL, ClayiumShape.largePlate), 'C', i(ClayiumItems.integratedCircuit), 'E', i(ClayiumItems.CEE));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 7), "#E#", "#C#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.clayium, ClayiumShape.largePlate), 'C', i(ClayiumItems.clayCore), 'E', i(ClayiumItems.CEE));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 8), "#E#", "#C#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.ultimateAlloy, ClayiumShape.largePlate), 'C', i(ClayiumItems.clayBrain), 'E', i(ClayiumItems.CEE));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 9), "#E#", "#C#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.largePlate), 'C', i(ClayiumItems.claySpirit), 'E', i(ClayiumItems.CEE));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 10), "#E#", "#C#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.pureAntimatter, ClayiumShape.largePlate), 'C', i(ClayiumItems.claySoul), 'E', i(ClayiumItems.CEE));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 11), "#E#", "#C#", "###",
                '#', ClayiumMaterials.get(CMaterials.OCTUPLE_CLAY, ClayiumShape.largePlate), 'C', i(ClayiumItems.clayAnima), 'E', i(ClayiumItems.CEE));
        GameRegistry.addRecipe(i(CBlocks.blockMachineHull, 1, 12), "#E#", "#C#", "###",
                '#', ClayiumMaterials.get(CMaterials.OCTUPLE_PURE_ANTIMATTER, ClayiumShape.largePlate), 'C', i(ClayiumItems.clayPsyche), 'E', i(ClayiumItems.CEE));
*/

        assembler.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.largePlate), circuits.get(6)), 4,
                ii(i(ClayiumBlocks.AZ91DHull)), e(6), 120L);
//        GameRegistry.addRecipe(i(ClayiumBlocks.ZK60AHull), "###", "#C#", "###",
//                '#', ClayiumMaterials.get(CMaterials.ZK60A_ALLOY, ClayiumShape.largePlate), 'C', i(ClayiumItems.precisionCircuit));

/*
        CMaterial[] reactorHullMats = {CMaterials.RUBIDIUM, CMaterials.CERIUM, CMaterials.TANTALUM, CMaterials.PRASEODYMIUM, CMaterials.PROTACTINIUM, CMaterials.NEPTUNIUM, CMaterials.PROMETHIUM, CMaterials.SAMARIUM, CMaterials.CURIUM, CMaterials.EUROPIUM};


        GameRegistry.addRecipe(new ShapedOreRecipe(
                i(CBlocks.blockCAReactorHull, 1, 0), oo("#I#", "#H#", "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 'H', machines[10], 'I', ClayiumMaterials.getODName(reactorHullMats[0], ClayiumShape.ingot))));

        for (int i = 0; i <= 8; i++) {
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    i(CBlocks.blockCAReactorHull, 1, i + 1), oo("#I#", "#H#", "###",
                    '#', ClayiumMaterials.get(CMaterials.COMPRESSED_PURE_ANTIMATTER[i], ClayiumShape.gem), 'H', i(CBlocks.blockCAReactorHull, 1, i), 'I', ClayiumMaterials.getODName(reactorHullMats[i + 1], ClayiumShape.ingot))));
        }


        GameRegistry.addRecipe(i(ClayiumItems.clayCircuit), "-*-", "o#o", "-*-",
                '-', ClayiumMaterials.get(ClayiumMaterial.denseClay, CMaterials.STICK), '*', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.gear), 'o', ClayiumMaterials.get(ClayiumMaterial.denseClay, CMaterials.RING), '#', i(ClayiumItems.clayCircuitBoard));
        GameRegistry.addRecipe(CItems.itemMisc.get("SimpleCircuit"), "---", "-#-", "---",
                '-', ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust), '#', i(ClayiumItems.clayCircuitBoard));
*/
        millingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.plate), i(ClayiumItems.clayCircuitBoard), 32L);
        millingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.plate), i(ClayiumItems.clayCircuitBoard), 1L);
        millingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.plate), 3, i(ClayiumItems.CEEBoard), 2L, 32L);

        assembler.addRecipe(ii(i(ClayiumItems.CEECircuit), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.plate, 3)), 0,
                ii(i(ClayiumItems.CEE)), 8L, 20L);
        inscriber.addRecipe(ii(i(ClayiumItems.CEEBoard), ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 0,
                ii(i(ClayiumItems.CEECircuit)), 2L, 20L);

        inscriber.addRecipe(ii(i(ClayiumItems.clayCircuitBoard), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.dust, 6)), 0,
                ii(i(ClayiumItems.clayCircuit)), 2L, 20L);
        inscriber.addRecipe(ii(i(ClayiumItems.clayCircuitBoard), ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 0,
                ii(i(ClayiumItems.basicCircuit)), 2L, 20L);
        inscriber.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.plate), ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 0,
                ii(i(ClayiumItems.advancedCircuit)), 100L, 120L);
        inscriber.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.silicon, ClayiumShape.plate), ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 0,
                ii(i(ClayiumItems.precisionCircuit)), 1000L, 120L);
        assembler.addRecipe(ii(i(ClayiumItems.precisionCircuit), ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 6,
                ii(i(ClayiumItems.integratedCircuit)), 10000L, 1200L);

        assembler.addRecipe(oo(i(ClayiumItems.CEE), i(ClayiumItems.integratedCircuit)), 6,
                ii(i(ClayiumItems.laserParts)), e(6), 20L);
        assembler.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.beryllium, ClayiumShape.dust, 8), i(ClayiumItems.integratedCircuit)), 6,
                ii(i(ClayiumItems.synchronousParts)), e(6), 432000L);
        reactor.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.pureAntimatter, ClayiumShape.gem, 8), i(ClayiumItems.integratedCircuit)), 11,
                ii(i(ClayiumItems.teleportationParts)), e(11), 10000000000000L);

        reactor.addRecipe(oo(i(ClayiumItems.integratedCircuit, 6), ClayiumMaterials.get(ClayiumMaterial.excClay, ClayiumShape.dust)), 7,
                ii(i(ClayiumItems.clayCore)), e(10.0D, 7), 8000000L);
        reactor.addRecipe(oo(i(ClayiumItems.clayCore, 6), ClayiumMaterials.get(ClayiumMaterial.excClay, ClayiumShape.dust, 12)), 8,
                ii(i(ClayiumItems.clayBrain)), e(10.0D, 8), 4000000000L);
        reactor.addRecipe(oo(i(ClayiumItems.clayBrain, 6), ClayiumMaterials.get(ClayiumMaterial.excClay, ClayiumShape.dust, 32)), 9,
                ii(i(ClayiumItems.claySpirit)), e(10.0D, 9), 10000000000000L);
        reactor.addRecipe(oo(i(ClayiumItems.claySpirit, 6), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 4)), 10,
                ii(i(ClayiumItems.claySoul)), e(10.0D, 10), 10000000000000L);
        reactor.addRecipe(oo(i(ClayiumItems.claySoul, 6), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 16)), 11,
                ii(i(ClayiumItems.clayAnima)), e(30.0D, 11), 100000000000000L);
        reactor.addRecipe(oo(i(ClayiumItems.clayAnima, 6), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 64)), 12,
                ii(i(ClayiumItems.clayPsyche)), e(90.0D, 12), 1000000000000000L);
    }

    public static void registerMachines() {
/* Used assets/recipes
        GameRegistry.addRecipe(i(CBlocks.blockClayWorkTable), "##", "##",
                '#', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block));
        GameRegistry.addRecipe(i(CBlocks.blockClayCraftingTable), "###",
                '#', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block));
        GameRegistry.addShapelessRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block, 3), i(CBlocks.blockClayCraftingTable));
        int i;
        for (i = 1; i <= 4; i++) {
            GameRegistry.addRecipe(i(CBlocks.blocksBendingMachine[i]), "o-*", "P#P", "o-*",
                    '#', machines.get(i),
                    'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '-', ClayiumMaterials.get(cmats[i], ClayiumShape.cylinder), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'P', ClayiumMaterials.get(cmats[i], ClayiumShape.plate));
            GameRegistry.addRecipe(i(CBlocks.blocksWireDrawingMachine[i]), "*o*", "=#=", "*o*",
                    '#', machines.get(i),
                    'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), '=', ClayiumMaterials.get(cmats[i], ClayiumShape.pipe));
            GameRegistry.addRecipe(i(CBlocks.blocksPipeDrawingMachine[i]), "*o*", "-#=", "*o*",
                    '#', machines.get(i),
                    'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '-', ClayiumMaterials.get(cmats[i], ClayiumShape.cylinder), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), '=', ClayiumMaterials.get(cmats[i], ClayiumShape.pipe));
            GameRegistry.addRecipe(i(CBlocks.blocksCuttingMachine[i]), "P*P", "o#|", "P*P",
                    '#', machines.get(i),
                    'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'P', ClayiumMaterials.get(cmats[i], ClayiumShape.plate), '|', ClayiumMaterials.get(cmats[i], ClayiumShape.cuttingHead));
            GameRegistry.addRecipe(i(CBlocks.blocksLathe[i]), "P*P", "-#o", "P*P",
                    '#', machines.get(i),
                    'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'P', ClayiumMaterials.get(cmats[i], ClayiumShape.plate), '-', ClayiumMaterials.get(cmats[i], CMaterials.STICK));
            GameRegistry.addRecipe(i(ClayiumMachines.get(EnumMachineKind.cobblestoneGenerator, i)), " * ", "=#=", " * ",
                    '#', machines.get(i),
                    '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), '=', ClayiumMaterials.get(cmats[i], ClayiumShape.pipe));
            if (i == 1) {
                GameRegistry.addRecipe(i(CBlocks.blockElementalMillingMachine), "P0P", "o#o", "P*P",
                        '#', machines.get(i),
                        'o', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.spindle), '*', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.gear), 'P', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.plate), '0', ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cuttingHead));
                GameRegistry.addShapelessRecipe(i(CBlocks.blockClayWaterWheel), machines.get(i), ClayiumMaterials.get(mats[i - 1], CMaterials.WATER_WHEEL));
            }
            if (i >= 2) {
                GameRegistry.addRecipe(i(CBlocks.blocksGrinder[i]), "P0P", "o#o", "P*P",
                        '#', machines.get(i),
                        'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'P', ClayiumMaterials.get(cmats[i], ClayiumShape.plate), '0', ClayiumMaterials.get(cmats[i], ClayiumShape.grindingHead));
                GameRegistry.addRecipe(i(CBlocks.blocksDecomposer[i]), "*o*", "C#C", "*=*",
                        '#', machines.get(i),
                        'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), '=', ClayiumMaterials.get(cmats[i], ClayiumShape.pipe), 'C', circuits.get(i));
                GameRegistry.addRecipe(i(CBlocks.blocksCondenser[i]), "*P*", "P#P", "*P*",
                        '#', machines.get(i),
                        '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'P', ClayiumMaterials.get(cmats[i], ClayiumShape.plate));
            }
            if (i == 2) {
                GameRegistry.addShapelessRecipe(i(CBlocks.blockDenseClayWaterWheel), machines.get(i), ClayiumMaterials.get(cmats[i], CMaterials.WATER_WHEEL));
            }
            if (i >= 3) {
                GameRegistry.addRecipe(i(ClayiumMachines.get(EnumMachineKind.centrifuge, i)), "*o*", "o#o", "*o*",
                        '#', machines.get(i),
                        'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear));
                GameRegistry.addRecipe(i(ClayiumMachines.get(EnumMachineKind.inscriber, i)), "*o*", "C#C", "*C*",
                        '#', machines.get(i),
                        'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'C', circuits.get(i));
                GameRegistry.addRecipe(i(ClayiumMachines.get(EnumMachineKind.assembler, i)), "*C*", "o#o", "*C*",
                        '#', machines.get(i),
                        'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'C', circuits.get(i));

                GameRegistry.addRecipe(i(CBlocks.blocksMillingMachine[i]), "P0P", "o#o", "P*P",
                        '#', machines.get(i),
                        'o', ClayiumMaterials.get(cmats[i], ClayiumShape.spindle), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'P', ClayiumMaterials.get(cmats[i], ClayiumShape.plate), '0', ClayiumMaterials.get(cmats[i], ClayiumShape.cuttingHead));
            }
            if (i == 3) {
                GameRegistry.addRecipe(i(CBlocks.blockEnergeticClayCondenser), "P*P", "E#E", "PCP",
                        '#', machines.get(i),
                        'P', ClayiumMaterials.get(cmats[i], ClayiumShape.plate), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'C', circuits[i + 1], 'E', i(ClayiumItems.CEE));
            }
            if (i == 4) {
                GameRegistry.addRecipe(i(CBlocks.blockEnergeticClayCondenserMK2), "P*P", "E#E", "PCP",
                        '#', machines.get(i),
                        'P', ClayiumMaterials.get(cmats[i], ClayiumShape.plate), '*', ClayiumMaterials.get(cmats[i], ClayiumShape.gear), 'C', circuits[i + 1], 'E', i(ClayiumItems.CEE));
            }
        }
*/
        for (int i = 1; i <= 13; i++) {
            if (i == 1) {
                assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cuttingHead)), 4,
                        ii(i(ClayiumMachines.elementalMillingMachine)), e(i), 120L);
            }
            if (i >= 1) {
                if (i <= 3) {
                    assembler.addRecipe(ii(ClayiumMaterials.get(mats.get(i), ClayiumShape.largePlate), circuits.get(3)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.cobblestoneGenerator, i))), e(i), 40L);
                }
                if (i <= 4) {
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.plate, 3)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.bendingMachine, i))), e(i), 120L);
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.pipe, 2)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.wireDrawingMachine, i))), e(i), 120L);
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder, 2)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.pipeDrawingMachine, i))), e(i), 120L);
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.cuttingHead)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.cuttingMachine, i))), e(i), 120L);
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.spindle)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.lathe, i))), e(i), 120L);
                }
            }

            if (i >= 2) {
                if (i <= 3) {
                    assembler.addRecipe(oo(machines.get(i), ClayiumMaterials.get(mats.get(i), ClayiumShape.largePlate)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.condenser, i))), e(i), 120L);
                }
                if (i <= 4) {
                    assembler.addRecipe(oo(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.gear, 4)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.decomposer, i))), e(i), 120L);
                }
                if (i <= 6) {
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.grindingHead, 1)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.grinder, i))), e(i), 120L);
                }
            }
            if (i == 3) {
                assembler.addRecipe(ii(machines.get(i), i(ClayiumItems.CEE, 2)), 4,
                        ii(i(ClayiumMachines.energeticClayCondenserMK1)), e(i), 120L);
            }
            if (i >= 3) {
                if (i <= 4) {
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.cuttingHead)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.millingMachine, i))), e(i), 120L);
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.gear, 4)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.assembler, i))), e(i), 40L);
                    assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.assembler, i)), circuits.get(4)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.inscriber, i))), e(i), 40L);
                }
                if (i <= 6) {
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.spindle, Math.max(i - 4, 1))), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.centrifuge, i))), e(i), 120L);
                }
            }
            if (i == 4) {
                assembler.addRecipe(ii(machines.get(i), i(ClayiumItems.CEE, 2)), 4,
                        ii(i(ClayiumMachines.energeticClayCondenserMK2)), e(i), 120L);
            }
            if (i >= 4) {
                assembler.addRecipe(ii(ClayiumMaterials.get(mats.get(i), ClayiumShape.plate), circuits.get(i)), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.buffer, i), 16)), e(i), 40L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.buffer, i), 6), ClayiumMaterials.get(mats.get(i), ClayiumShape.largePlate)), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.multitrackBuffer, i))), e(i), 40L);
                if (ClayiumConfiguration.cfgEnableFluidCapsule)
// TODO                   assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.buffer, i)), i(CItems.itemsCapsule[0], 4)), 0, 4,
//                            ii(i(ClayiumMachines.get(EnumMachineKind.fluidTranslator, i))), e(i), 40L);
                if (i <= 7) {
                    assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.buffer, i)), circuits.get(3)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.cobblestoneGenerator, i))), e(i), 40L);
                    assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.buffer, i)), circuits.get(4)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.saltExtractor, i))), e(i), 40L);
                    assembler.addRecipe(ii(machines.get(i), s(circuits.get(3), i - 3)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.smelter, i))), e(i), 120L);
                }
                if (i <= 5) {
                    assembler.addRecipe(oo(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.buffer, i))), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.condenser, i))), e(i), 120L);
                    assembler.addRecipe(ii(machines.get(i), s(circuits.get(4), i - 3)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.chemicalReactor, i))), e(i), 120L);
                }
            }
            if (i >= 5) {
                assembler.addRecipe(ii(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.buffer, 6))), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.clayInterface, i))), e(i), 40L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.clayInterface, i)), ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust, 16)), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.redstoneInterface, i))), e(i), 40L);
                if (i <= 7) {
                    assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.plate, (i - 4) * 3)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.bendingMachine, i))), e(i), 120L);
                }
            }
            if (i == 5) {
                assembler.addRecipe(oo(machines.get(i), ClayiumMaterials.getOD(ClayiumMaterial.silicon, ClayiumShape.plate, 8)), 4,
                        ii(i(ClayiumMachines.solarClayFabricatorMK1)), e(i), 120L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.buffer, i)), circuits.get(5)), 4,
                        ii(i(ClayiumMachines.autoClayCondenserMK1)), e(i), 40L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.assembler, 4)), machines.get(i)), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.autoCrafter, i))), e(i), 40L);
                if (ClayiumConfiguration.cfgEnableFluidCapsule)
                    assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.fluidTranslator, i), 2), machines.get(i)), 4,
                            ii(i(ClayiumMachines.get(EnumMachineKind.fluidTransferMachine, i))), e(i), 40L);
            }
            if (i >= 6 && i <= 9) {
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.chemicalReactor, 5)), circuits.get(i)), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.electrolysisReactor, i))), e(i), 40L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.autoCrafter, i - 1)), machines.get(i)), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.autoCrafter, i))), e(i), 40L);
            }
            if (i == 6) {
                assembler.addRecipe(oo(machines.get(i), ClayiumMaterials.getOD(ClayiumMaterial.silicon, ClayiumShape.plate, 16)), 4,
                        ii(i(ClayiumMachines.solarClayFabricatorMK2)), e(i), 120L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.smelter, i)), i(ClayiumMachines.get(EnumMachineKind.clayInterface, i))), 4,
                        ii(i(ClayiumMachines.blastFurnace)), e(i), 120L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.chemicalReactor, 5)), i(ClayiumMachines.get(EnumMachineKind.smelter, i))), 4,
                        ii(i(ClayiumMachines.chemicalMetalSeparator)), e(i), 40L);
                assembler.addRecipe(ii(machines.get(i), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.gear, 4)), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.assembler, i))), e(i), 40L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.assembler, 4)), circuits.get(i)), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.assembler, 6))), e(i), 40L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.smelter, i)), circuits.get(i)), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.alloySmelter, i))), e(i), 40L);
            }
            if (i == 7) {
                assembler.addRecipe(oo(machines.get(i), ClayiumMaterials.getOD(ClayiumMaterial.silicon, ClayiumShape.plate, 16)), 4,
                        ii(i(ClayiumMachines.lithiumSolarClayFabricator)), e(i), 120L);
                assembler.addRecipe(ii(i(ClayiumMachines.get(EnumMachineKind.buffer, i)), circuits.get(5)), 4,
                        ii(i(ClayiumMachines.autoClayCondenserMK2)), e(i), 40L);
                assembler.addRecipe(oo(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.laserInterface, i))), 6,
                        ii(i(ClayiumMachines.clayReactor)), e(i), 1200L);
            }
            if (i >= 7) {
                assembler.addRecipe(oo(i(ClayiumMachines.get(EnumMachineKind.buffer, i)), i(ClayiumItems.laserParts, 1)), 6,
                        ii(i(ClayiumMachines.get(EnumMachineKind.laserInterface, i))), e(i), 120L);
                if (i <= 9) {
                    assembler.addRecipe(oo(i(ClayiumMachines.get(EnumMachineKind.buffer, i)), machines.get(i)), 6,
                            ii(i(ClayiumMachines.get(EnumMachineKind.distributor, i))), e(i), 120L);
                    assembler.addRecipe(oo(i(ClayiumMachines.get(EnumMachineKind.buffer, i)), circuits.get(7)), 6,
                            ii(i(ClayiumMachines.get(EnumMachineKind.distributor, i))), e(i), 120L);
                    assembler.addRecipe(oo(i(ClayiumMachines.clayReactor), i(ClayiumMachines.get(EnumMachineKind.electrolysisReactor, i))), 6,
                            ii(i(ClayiumMachines.get(EnumMachineKind.transformer, i))), e(i), 120L);
                }
                if (i <= 10) {
                    assembler.addRecipe(oo(machines.get(i), i(ClayiumItems.laserParts, 4)), 6,
                            ii(i(ClayiumMachines.get(EnumMachineKind.clayEnergyLaser, i))), e(i), 480L);
                }
            }
            if (i == 8) {
                assembler.addRecipe(ii(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.chemicalReactor, 5))), 4,
                        ii(i(ClayiumMachines.get(EnumMachineKind.chemicalReactor, i))), e(i), 480L);
            }
            if (i >= 8 &&
                    i <= 9) {
                assembler.addRecipe(ii(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.smelter, i - 1), 16)), 6,
                        ii(i(ClayiumMachines.get(EnumMachineKind.smelter, i))), e(i), 2000L);
            }

            if (i == 9) {
                assembler.addRecipe(oo(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.bendingMachine, 7), 4)), 6,
                        ii(i(ClayiumMachines.get(EnumMachineKind.bendingMachine, i))), e(i), 480L);
            }
            if (i >= 9) {
                if (i == 9) {
                    assembler.addRecipe(oo(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.transformer, i), 16)), 6,
                            ii(i(ClayiumMachines.get(EnumMachineKind.CACondenser, i))), e(i), 480L);
                    assembler.addRecipe(oo(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.reactor, i), 16)), 6,
                            ii(i(ClayiumMachines.get(EnumMachineKind.CAInjector, i))), e(i), 480L);
                } else {
                    if (i <= 11) {
                        assembler.addRecipe(oo(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.transformer, i), 16)), 10,
                                ii(i(ClayiumMachines.get(EnumMachineKind.CACondenser, i))), e(i), 480L);
                    }
                    assembler.addRecipe(oo(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.CAInjector, i - 1), 2)), 10,
                            ii(i(ClayiumMachines.get(EnumMachineKind.CAInjector, i))), e(i), 480L);
                }
            }
            if (i == 10) {
                assembler.addRecipe(ii(machines.get(i), i(ClayiumMachines.get(EnumMachineKind.assembler, 6))), 6,
                        ii(i(ClayiumMachines.get(EnumMachineKind.assembler, i))), e(i), 40L);
            }
            if (i >= 10) {
                assembler.addRecipe(oo(machines.get(i), i(ClayiumMachines.clayReactor, 16)), 10,
                        ii(i(ClayiumMachines.get(EnumMachineKind.CAReactorCore, i))), e(i), 120L);
                if (i <= 12) {
                    assembler.addRecipe(oo(i(ClayiumMachines.get(EnumMachineKind.CAInjector, i)), i(ClayiumMachines.clayReactor)), 10,
                            ii(i(ClayiumMachines.get(EnumMachineKind.transformer, i))), e(i), 120L);
                }
            }
            if (i == 13) {
                assembler.addRecipe(oo(machines.get(i), ClayiumBlocks.CAReactorCoil.get(TierPrefix.OPA)), 10,
                        ii(i(ClayiumMachines.energeticClayDecomposer)), e(i), 120L);
            }
        }
/*
        if (ClayiumCore.cfgEnableRFGenerator) {
            Map<String, Map<String, Object>> map = ClayRFGenerator.getConfigMap();
            Map<Integer, List<Block>> blockList = new HashMap<Integer, List<Block>>();
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                Map<String, Object> config = entry.getValue();
                if (config != null && config.get("Tier") instanceof Number) {
                    int tier = ((Number) config.get("Tier")).intValue();
                    Block blockRFGenerator = CBlocks.blocksRFGenerator.get(entry.getKey());
                    List<Block> list = blockList.containsKey(tier) ? blockList.get(tier) : new ArrayList<Block>();
                    list.add(blockRFGenerator);
                    blockList.put(tier, list);
                }
            }
            for (Map.Entry<Integer, List<Block>> entry : blockList.entrySet()) {
                int tier = entry.getKey();
                List<Block> list = entry.getValue();
                for (int k = 0; k < list.size(); k++) {
                    Block block = (tier >= 0 && tier < CBlocks.blocksRedstoneInterface.length && CBlocks.blocksRedstoneInterface[tier] != null) ? CBlocks.blocksRedstoneInterface[tier] : CBlocks.blocksRedstoneInterface[5];
                    ItemStack prev = (k == 0) ? machines[tier] : i(list.get(k - 1));
                    assembler.addRecipe(oo(prev, i(block, 1)), 0, 4,
                            ii(i(list.get(k))), e(tier), 120L);
                }
            }
        }
*/
        assembler.addRecipe(oo(ClayiumMaterials.getOD(ClayiumMaterial.quartz, ClayiumShape.dust, 16)), 0,
                ii(i(ClayiumMachines.quartzCrucible)), 1000L, 20L);
        blastFurnace.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust), ClayiumMaterials.getOD(ClayiumMaterial.quartz, ClayiumShape.dust, 8)), 7,
                ii(i(ClayiumMachines.laserReflector)), e(0.2D, 7), 100L);

        reactor.addRecipe(ii(machines.get(8), i(ClayiumMachines.lithiumSolarClayFabricator)), 8,
                ii(i(ClayiumMachines.clayFabricatorMK1)), e(3.0D, 8), 100000000L);
        reactor.addRecipe(ii(machines.get(9), i(ClayiumMachines.lithiumSolarClayFabricator)), 9,
                ii(i(ClayiumMachines.clayFabricatorMK2)), e(3.0D, 9), 100000000000L);
        reactor.addRecipe(ii(i(ClayiumMachines.clayFabricatorMK2, 64), ClayiumBlocks.overclocker.get(TierPrefix.OPA, 16)), 13,
                ii(i(ClayiumMachines.clayFabricatorMK3)), e(10.0D, 13), 1000000000000000000L);

        CAInjector.addRecipe(ii(machines.get(9), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 8)), 9,
                ii(ClayiumBlocks.resonator.get(TierPrefix.antimatter)), e(2.0D, 9), 4000L);
        CAInjector.addRecipe(ii(ClayiumBlocks.resonator.get(TierPrefix.antimatter, 16), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 64)), 11,
                ii(ClayiumBlocks.resonator.get(TierPrefix.pureAntimatter)), e(2.0D, 11), 4000L);
        CAInjector.addRecipe(ii(ClayiumBlocks.resonator.get(TierPrefix.pureAntimatter, 16), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 64)), 12,
                ii(ClayiumBlocks.resonator.get(TierPrefix.OEC)), e(2.0D, 12), 4000L);
        CAInjector.addRecipe(ii(ClayiumBlocks.resonator.get(TierPrefix.OEC, 16), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 64)), 13,
                ii(ClayiumBlocks.resonator.get(TierPrefix.OPA)), e(2.0D, 13), 4000L);

        CAInjector.addRecipe(ii(ClayiumBlocks.overclocker.get(TierPrefix.antimatter), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 8)), 9,
                ii(ClayiumBlocks.energyStorageUpgrade.get(TierPrefix.antimatter)), e(2.0D, 9), 4000L);
        CAInjector.addRecipe(ii(ClayiumBlocks.energyStorageUpgrade.get(TierPrefix.antimatter, 16), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 64)), 11,
                ii(ClayiumBlocks.energyStorageUpgrade.get(TierPrefix.pureAntimatter)), e(2.0D, 11), 4000L);
        CAInjector.addRecipe(ii(ClayiumBlocks.energyStorageUpgrade.get(TierPrefix.pureAntimatter, 16), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 64)), 12,
                ii(ClayiumBlocks.energyStorageUpgrade.get(TierPrefix.OEC)), e(2.0D, 12), 4000L);
        CAInjector.addRecipe(ii(ClayiumBlocks.energyStorageUpgrade.get(TierPrefix.OEC, 16), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 64)), 13,
                ii(ClayiumBlocks.energyStorageUpgrade.get(TierPrefix.OPA)), e(2.0D, 13), 4000L);

        reactor.addRecipe(oo(s(machines.get(10), 1), ClayiumBlocks.resonator.get(TierPrefix.antimatter, 8)), 10,
                ii(ClayiumBlocks.overclocker.get(TierPrefix.antimatter)), e(5.0D, 10), 10000000000000L);
        reactor.addRecipe(oo(s(machines.get(11), 4), ClayiumBlocks.resonator.get(TierPrefix.pureAntimatter, 16)), 11,
                ii(ClayiumBlocks.overclocker.get(TierPrefix.pureAntimatter)), e(5.0D, 11), 100000000000000L);
        reactor.addRecipe(oo(s(machines.get(12), 16), ClayiumBlocks.resonator.get(TierPrefix.OEC, 32)), 12,
                ii(ClayiumBlocks.overclocker.get(TierPrefix.OEC)), e(5.0D, 12), 1000000000000000L);
        reactor.addRecipe(oo(s(machines.get(13), 64), ClayiumBlocks.resonator.get(TierPrefix.OPA, 64)), 13,
                ii(ClayiumBlocks.overclocker.get(TierPrefix.OPA)), e(5.0D, 13), 1000000000000000L);

        reactor.addRecipe(oo(ClayiumMaterials.get(mats.get(10), ClayiumShape.plate, 6), ClayiumMaterials.getOD(ClayiumMaterial.platinum, ClayiumShape.ingot)), 10,
                ii(ClayiumBlocks.CAReactorCoil.get(TierPrefix.antimatter)), e(10), 10000000000000L);
        reactor.addRecipe(oo(ClayiumMaterials.get(mats.get(11), ClayiumShape.plate, 6), ClayiumMaterials.getOD(ClayiumMaterial.iridium, ClayiumShape.ingot, 4)), 11,
                ii(ClayiumBlocks.CAReactorCoil.get(TierPrefix.pureAntimatter)), e(11), 100000000000000L);
        reactor.addRecipe(oo(ClayiumMaterials.get(mats.get(12), ClayiumShape.plate, 6), ClayiumMaterials.getOD(ClayiumMaterial.mainOsmium, ClayiumShape.ingot, 16)), 12,
                ii(ClayiumBlocks.CAReactorCoil.get(TierPrefix.OEC)), e(12), 1000000000000000L);
        reactor.addRecipe(oo(ClayiumMaterials.get(mats.get(13), ClayiumShape.plate, 6), ClayiumMaterials.getOD(ClayiumMaterial.rhenium, ClayiumShape.ingot, 64)), 13,
                ii(ClayiumBlocks.CAReactorCoil.get(TierPrefix.OPA)), e(13), 1000000000000000L);

/* TODO PAN
        assembler.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.pureAntimatter, ClayiumShape.gem, 3), new OreDictionaryStack("blockGlass", 2)), 0, 10,
                ii(i(CBlocks.blockPANCable, 12)), e(10), 2L);
        reactor.addRecipe(oo(i(CBlocks.blocksPANAdapter[10], 4), circuits.get(11)), 0, 11,
                ii(i(CBlocks.blockPANCore)), e(11), 100000000000000L);

        assembler.addRecipe(ii(ClayiumBlocks.resonator.get(TierPrefix.antimatter), i(CBlocks.blockPANCable, 6)), 0, 10,
                ii(i(CBlocks.blocksPANAdapter[10])), e(10), 60L);
        assembler.addRecipe(ii(ClayiumBlocks.resonator.get(TierPrefix.pureAntimatter), i(CBlocks.blockPANCable, 6)), 0, 10,
                ii(i(CBlocks.blocksPANAdapter[11])), e(11), 60L);
        assembler.addRecipe(ii(ClayiumBlocks.resonator.get(TierPrefix.OEC), i(CBlocks.blockPANCable, 6)), 0, 10,
                ii(i(CBlocks.blocksPANAdapter[12])), e(12), 60L);
        assembler.addRecipe(ii(ClayiumBlocks.resonator.get(TierPrefix.OPA), i(CBlocks.blockPANCable, 6)), 0, 10,
                ii(i(CBlocks.blocksPANAdapter[13])), e(13), 60L);

        assembler.addRecipe(oo(machines[4], i(CBlocks.blockPANCable, 4)), 0, 10,
                ii(i(CBlocks.blocksPANDuplicator[4])), e(10), 20L);
        CMaterial[] panDuplicatorMats = {CMaterials.RUBIDIUM, CMaterials.LANTHANUM, CMaterials.CAESIUM, CMaterials.FRANCIUM, CMaterials.RADIUM, CMaterials.TANTALUM, CMaterials.BISMUTH, CMaterials.ACTINIUM, CMaterials.VANADIUM};


        for (int j = 0; j <= 8; j++) {
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    i(CBlocks.blocksPANDuplicator[j + 5]), oo("#I#", "DMD", "#I#",
                    '#', ClayiumMaterials.get(CMaterials.COMPRESSED_PURE_ANTIMATTER[j], ClayiumShape.gem), 'D', i(CBlocks.blocksPANDuplicator[j + 4]),
                    'I', ClayiumMaterials.getODName(panDuplicatorMats[j], ClayiumShape.ingot), 'M', machines[j + 5])));
        }
*/
/* TODO
        assembler.addRecipe(ii(i(ClayiumBlocks.AZ91DHull, 4), i(ClayiumMachines.get(EnumMachineKind.clayInterface, 5))), 0, 4,
                ii(s(StorageContainer.expandStorage(i(CBlocks.blockStorageContainer), 65536), 4)), e(6), 120L);
        assembler.addRecipe(ii(i(ClayiumBlocks.AZ91DHull, 4), i(ClayiumMachines.get(EnumMachineKind.redstoneInterface, 5))), 0, 4,
                ii(i(CBlocks.blockVacuumContainer, 4)), e(6), 120L);
        assembler.addRecipe(ii(i(ClayiumBlocks.AZ91DHull), s(circuits.get(8), 4)), 0, 6,
                ii(i(CBlocks.blockAutoTrader)), e(8), 120L);
*/
/*
        GameRegistry.addRecipe(new Shapelesss(StorageContainer.expandStorage(i(CBlocks.blockStorageContainer), 2147483647),
                li(StorageContainer.expandStorage(i(CBlocks.blockStorageContainer), 65536), s(circuits[8], 1))) {
            public ItemStack getCraftingResult(InventoryCrafting inv) {
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    ItemStack item = inv.getStackInSlot(i);
                    if (StorageContainer.getStorageSize(item) == 65536) {
                        ItemStack copy = item.copy();
                        copy.stackSize = 1;
                        return StorageContainer.expandStorage(copy, 2147483647);
                    }
                }
                return super.getCraftingResult(inv);
            }
        });

        GameRegistry.addRecipe(i(CBlocks.blockClayChunkLoader), "PCP", "C#C", "PCP",
                '#', i(ClayiumBlocks.ZK60AHull),
                'C', circuits[6], 'P', circuits[5]);
*/
/*
        assembler.addRecipe(ii(i(Blocks.CLAY), circuits.get(7)), 0, 6,
                ii(i(CBlocks.blockClayMarker)), e(7), 480L);
        assembler.addRecipe(ii(i(ClayiumBlocks.compressedClay.get(0), 1, 0), circuits.get(8)), 0, 6,
                ii(i(CBlocks.blockClayOpenPitMarker)), e(8), 480L);
        assembler.addRecipe(ii(i(ClayiumBlocks.compressedClay.get(1), 1, 1), circuits.get(8)), 0, 6,
                ii(i(CBlocks.blockClayGroundLevelingMarker)), e(8), 480L);
        assembler.addRecipe(ii(i(CBlocks.blockClayOpenPitMarker), i(CBlocks.blockClayGroundLevelingMarker)), 0, 6,
                ii(i(CBlocks.blockClayPrismMarker)), e(8), 480L);

        assembler.addRecipe(ii(i(ClayiumBlocks.ZK60AHull), s(circuits.get(7), 64)), 0, 6,
                ii(i(CBlocks.blockAreaCollector)), e(7), 6000L);

        assembler.addRecipe(ii(i(ClayiumBlocks.AZ91DHull), circuits.get(6)), 0, 6,
                ii(i(CBlocks.blockMiner)), e(6), 60L);
        assembler.addRecipe(ii(i(ClayiumBlocks.ZK60AHull), s(circuits.get(8), 64)), 0, 6,
                ii(i(CBlocks.blockAreaMiner)), e(8), 6000L);
        assembler.addRecipe(ii(i(CBlocks.blockAreaMiner), s(circuits.get(9), 64)), 0, 6,
                ii(i(CBlocks.blockAdvancedAreaMiner)), e(9), 6000L);
        assembler.addRecipe(ii(i(CBlocks.blockAdvancedAreaMiner), s(circuits.get(10), 64)), 0, 6,
                ii(i(CBlocks.blockAreaReplacer)), e(10), 6000L);

        assembler.addRecipe(ii(i(ClayiumBlocks.AZ91DHull), circuits.get(5)), 0, 6,
                ii(i(CBlocks.blockActivator)), e(6), 60L);
        assembler.addRecipe(ii(i(CBlocks.blockAreaCollector), circuits.get(8)), 0, 6,
                ii(i(CBlocks.blockAreaActivator)), e(8), 6000L);

        assembler.addRecipe(oo(i(CItems.itemsClayShooter[3]), i(ClayiumItems.teleportationParts)), 0, 10,
                ii(i(CItems.itemInstantTeleporter)), e(11), 3000L);

/* TODO
        Block[] blocksEnergeticClayCondenser = new Block[16];
        blocksEnergeticClayCondenser[3] = CBlocks.blockEnergeticClayCondenser;
        blocksEnergeticClayCondenser[4] = CBlocks.blockEnergeticClayCondenserMK2;
        blocksEnergeticClayCondenser[5] = CBlocks.blockAutoClayCondenser;
        blocksEnergeticClayCondenser[7] = CBlocks.blockAutoClayCondenserMK2;
        Block[] blocksClayFabricator = new Block[16];
        blocksClayFabricator[5] = CBlocks.blockSolarClayFabricatorMK1;
        blocksClayFabricator[6] = CBlocks.blockSolarClayFabricatorMK2;
        blocksClayFabricator[7] = CBlocks.blockLithiumSolarClayFabricator;
        blocksClayFabricator[8] = CBlocks.blockClayFabricatorMK1;
        blocksClayFabricator[9] = CBlocks.blockClayFabricatorMK2;
        Block[] blocksClayWaterWheel = new Block[16];
        blocksClayWaterWheel[1] = CBlocks.blockClayWaterWheel;
        blocksClayWaterWheel[2] = CBlocks.blockDenseClayWaterWheel;

        Block[][] arrayblocklist = {CBlocks.blocksBendingMachine, CBlocks.blocksWireDrawingMachine, CBlocks.blocksPipeDrawingMachine, CBlocks.blocksCuttingMachine, CBlocks.blocksLathe, CBlocks.blocksCobblestoneGenerator, CBlocks.blocksCondenser, CBlocks.blocksGrinder, CBlocks.blocksDecomposer, CBlocks.blocksMillingMachine, CBlocks.blocksAssembler, CBlocks.blocksInscriber, CBlocks.blocksCentrifuge, CBlocks.blocksSaltExtractor, CBlocks.blocksSmelter, CBlocks.blocksBuffer, CBlocks.blocksChemicalReactor, CBlocks.blocksAlloySmelter, CBlocks.blocksElectrolysisReactor, CBlocks.blocksClayEnergyLaser, CBlocks.blocksDistributor, CBlocks.blocksTransformer, CBlocks.blocksAutoCrafter, CBlocks.blocksCACondenser, CBlocks.blocksCAInjector, blocksEnergeticClayCondenser, blocksClayFabricator, blocksClayWaterWheel};


        ArrayList<Block[]> blocklists = new ArrayList<Block[]>(Arrays.asList(arrayblocklist));
        if (ClayiumCore.cfgEnableInjectorRecipeOfInterface) {
            blocklists.addAll(Arrays.asList(CBlocks.blocksClayInterface, CBlocks.blocksRedstoneInterface, CBlocks.blocksClayLaserInterface));
        }
        for (Block[] blocks : blocklists) {
            int k = -1, n = 0;
            for (int m = 0; m < blocks.length; m++) {
                n = (int) (n + Math.pow(1.2999999523162842D, m));
                if (n >= 64) n = 64;
                if (blocks[m] != null) {
                    if (k != -1) {
                        CAInjector.addRecipe(oo(i(blocks[k]), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, n)), 0, m,
                                ii(i(blocks[m])), e(3.0D, m), 4000L);
                    }
                    k = m;
                    n = 0;
                }
            }
        }


        for (CMaterial material : MetalChest.getChestMaterialMap().keySet()) {
            CMaterial imaterial = material;
            if (material == CMaterials.OSMIUM)
                imaterial = ClayiumMaterial.mainOsmium;
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    i(CBlocks.blockMetalChest, 1, material.meta),
                    oo(
                            "###", "#*#", "###", '#', ClayiumMaterials.getODName(imaterial, ClayiumShape.ingot), '*', i(Blocks.chest, 1, 32767))));
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    i(CBlocks.blockMetalChest, 1, material.meta),
                    oo(
                            "###", "#*#", "###", '#', ClayiumMaterials.getODName(imaterial, ClayiumShape.ingot), '*', i(CBlocks.blockMetalChest, 1, 32767)
                    )));
        }
    */}

    public static void registerMaterials() {/*
        CShape shape = ClayiumShape.ingot;
        for (CMaterial material : new CMaterial[] {ClayiumMaterial.impureSilicon, ClayiumMaterial.siliconE, ClayiumMaterial.silicon, CMaterials.ALUMINIUM, ClayiumMaterial.clay_STEEL, ClayiumMaterial.clayium, ClayiumMaterial.ultimateAlloy}) {


            GameRegistry.addRecipe(new ShapelessOreRecipe(
                    ClayiumMaterials.get(material, ClayiumShape.block),
                    oo(ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape),
                            ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape),
                            ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape))));
            GameRegistry.addRecipe(new ShapelessOreRecipe(
                    ClayiumMaterials.get(material, shape, 9),
                    oo(ClayiumMaterials.getODName(material, ClayiumShape.block))));
        }
        GameRegistry.addRecipe(new ShapelessOreRecipe(
                ClayiumMaterials.get(CMaterials.ALUMINIUM, ClayiumShape.block),
                oo(ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, shape), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, shape), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, shape),
                        ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, shape), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, shape), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, shape),
                        ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, shape), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, shape), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, shape))));
        GameRegistry.addRecipe(new ShapelessOreRecipe(
                ClayiumMaterials.get(CMaterials.ALUMINIUM, shape, 9),
                oo(ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, ClayiumShape.block))));
        shape = ClayiumShape.gem;
        for (CMaterial material : new CMaterial[] {ClayiumMaterial.antimatter, ClayiumMaterial.pureAntimatter, CMaterials.OCTUPLE_PURE_ANTIMATTER}) {


            GameRegistry.addRecipe(new ShapelessOreRecipe(
                    ClayiumMaterials.get(material, ClayiumShape.block),
                    oo(ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape),
                            ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape),
                            ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape), ClayiumMaterials.getODName(material, shape))));
            GameRegistry.addRecipe(new ShapelessOreRecipe(
                    ClayiumMaterials.get(material, shape, 9),
                    oo(ClayiumMaterials.getODName(material, ClayiumShape.block))));
        }
        GameRegistry.addRecipe(CBlocks.blockMaterial.get("OctupleEnergeticClay"), "###", "###", "###",
                '#', i(ClayiumBlocks.compressedClay.get(12), 1, 12));
        GameRegistry.addShapelessRecipe(i(ClayiumBlocks.compressedClay.get(12), 9, 12), CBlocks.blockMaterial
                .get("OctupleEnergeticClay"));

        String[] dyes = {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};


        for (int i = 0; i < 16; i++) {
            String str = ItemDye.field_150921_b[BlockColored.func_150031_c(i)];
            str = Character.toTitleCase(str.charAt(0)) + str.substring(1);
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    i(CBlocks.blockSiliconeColored, 8, i),
                    "###", "#*#", "###", '#', ClayiumMaterials.getODName(ClayiumMaterial.siliconE, ClayiumShape.block), '*', "dye" + dyes[15 - i]
                    ));
        }

        grinder.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.block), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.dust), 1L, 3L);
        grinder.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.dust), 1L, 6L);
        grinder.addRecipe(ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.block), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust), 1L, 12L);
        grinder.addRecipe(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.block), ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust), 2L, 12L);

        condenser.addRecipe(ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.clay, ClayiumShape.block), 1L, 3L);
        condenser.addRecipe(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block), 1L, 6L);


        for (ClayiumMaterial material : new ClayiumMaterial[] { ClayiumMaterial.impureSilicon, ClayiumMaterial.silicon, ClayiumMaterial.silicone, ClayiumMaterial.aluminium, ClayiumMaterial.claySteel, ClayiumMaterial.clayium, ClayiumMaterial.ultimateAlloy, ClayiumMaterial.AZ91DAlloy, ClayiumMaterial.ZK60AAlloy }) {
            bendingMachine.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.ingot), 4, ClayiumMaterials.get(material, ClayiumShape.plate), e(4), (int) (20.0F * material.getHardness()));
            bendingMachine.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.plate, 4), 4, ClayiumMaterials.get(material, ClayiumShape.largePlate), e(4), (int) (40.0F * material.getHardness()));
            grinder.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.ingot), ClayiumMaterials.get(material, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * material.getHardness()));
            grinder.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.plate), ClayiumMaterials.get(material, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * material.getHardness()));
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.largePlate), ClayiumMaterials.get(material, ClayiumShape.dust, 4), e(0.25D, 4), (int) (80.0F * material.getHardness()));
        }
        bendingMachine.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.aluminiumOD, ClayiumShape.ingot), 4, ClayiumMaterials.get(ClayiumMaterial.aluminium, ClayiumShape.plate), e(4), (int) (20.0F * ClayiumMaterial.aluminium.getHardness()));
        bendingMachine.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.aluminiumOD, ClayiumShape.plate, 4), 4, ClayiumMaterials.get(ClayiumMaterial.aluminium, ClayiumShape.largePlate), e(4), (int) (40.0F * ClayiumMaterial.aluminium.getHardness()));
        grinder.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.aluminiumOD, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.aluminium, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * ClayiumMaterial.aluminium.getHardness()));
        grinder.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.aluminiumOD, ClayiumShape.plate), ClayiumMaterials.get(ClayiumMaterial.aluminium, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * ClayiumMaterial.aluminium.getHardness()));
        if (ClayiumConfiguration.cfgHardcoreAluminium) {
            bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.ingot), 4, ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.plate), e(4), (int) (20.0F * ClayiumMaterial.impureAluminium.getHardness()));
            bendingMachine.addRecipe(ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.plate, 4), 4, ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.largePlate), e(4), (int) (40.0F * ClayiumMaterial.impureAluminium.getHardness()));
            grinder.addRecipe(ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * ClayiumMaterial.impureAluminium.getHardness()));
            grinder.addRecipe(ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.plate), ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * ClayiumMaterial.impureAluminium.getHardness()));
            grinder.addRecipe(ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.largePlate), ClayiumMaterials.get(ClayiumMaterial.impureAluminium, ClayiumShape.dust, 4), e(0.25D, 4), (int) (80.0F * ClayiumMaterial.impureAluminium.getHardness()));
        }


        GameRegistry.addSmelting(ClayiumMaterials.get(ClayiumMaterial.impureSilicon, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.silicone, ClayiumShape.ingot), 0.0F);

        for (ClayiumMaterial material : ClayiumMaterial.metals) {
            grinder.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.ingot), ClayiumMaterials.get(material, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * material.getHardness()));
            grinder.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.plate), ClayiumMaterials.get(material, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * material.getHardness()));
            if (ClayiumMaterials.existOD(material, ClayiumShape.plate))
                bendingMachine.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.ingot), ClayiumMaterials.getODExist(material, ClayiumShape.plate), e(4), (int) (40.0F * material.getHardness()));
        }
        for (ClayiumMaterial material : new ClayiumMaterial[] { ClayiumMaterial.zincalminiumAlloy, ClayiumMaterial.zinconiumAlloy }) {
            grinder.addRecipe(ClayiumMaterials.get(material, ClayiumShape.ingot), ClayiumMaterials.get(material, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * material.getHardness()));
        }


        for (ClayiumMaterial material : ClayiumMaterial.ingotAndPlateToDust) {
            if (ClayiumMaterials.existOD(material, ClayiumShape.dust)) {
                grinder.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.ingot), ClayiumMaterials.getODExist(material, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * material.getHardness()));
                grinder.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.plate), ClayiumMaterials.getODExist(material, ClayiumShape.dust), e(0.25D, 4), (int) (80.0F * material.getHardness()));
            }
            if (ClayiumMaterials.existOD(material, ClayiumShape.plate)) {
                bendingMachine.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.ingot), ClayiumMaterials.getODExist(material, ClayiumShape.plate), e(4), (int) (40.0F * material.getHardness()));
            }
        }


        ClayiumMaterial[] materials = { ClayiumMaterial.antimatter, ClayiumMaterial.pureAntimatter, ClayiumMaterial.octupleEnergeticClay, ClayiumMaterial.octuplePureAntimatter };
        ClayiumShape[] shapes = { ClayiumShape.gem, ClayiumShape.gem, ClayiumShape.block, ClayiumShape.gem };
        for (int i = 0; i < materials.length; i++) {
            bendingMachine.addRecipe(ClayiumMaterials.get(materials[i], shapes[i]), 9, ClayiumMaterials.get(materials[i], ClayiumShape.plate), e(9 + i), (int) (20.0F * materials[i].getHardness()));
            bendingMachine.addRecipe(ClayiumMaterials.get(materials[i], ClayiumShape.plate, 4),9, ClayiumMaterials.get(materials[i], ClayiumShape.largePlate), e(9 + i), (int) (40.0F * materials[i].getHardness()));
            grinder.addRecipe(ClayiumMaterials.get(materials[i], shapes[i]), 10, ClayiumMaterials.get(materials[i], ClayiumShape.dust), e(0.25D, 9 + i), (int) (80.0F * materials[i].getHardness()));
            grinder.addRecipe(ClayiumMaterials.get(materials[i], ClayiumShape.plate), 10, ClayiumMaterials.get(materials[i], ClayiumShape.dust), e(0.25D, 9 + i), (int) (80.0F * materials[i].getHardness()));
            grinder.addRecipe(ClayiumMaterials.get(materials[i], ClayiumShape.largePlate), 10, ClayiumMaterials.get(materials[i], ClayiumShape.dust, 4), e(0.25D, 9 + i), (int) (80.0F * materials[i].getHardness()));
            condenser.addRecipe(ClayiumMaterials.get(materials[i], ClayiumShape.dust), 10, ClayiumMaterials.get(materials[i], shapes[i]), e(0.25D, 9 + i), (int) (80.0F * materials[i].getHardness()));
        }


        materials = new CMaterial[] {ClayiumMaterial.impureSilicon, ClayiumMaterial.silicon, ClayiumMaterial.siliconE, CMaterials.IRON, CMaterials.GOLD, CMaterials.LEAD, CMaterials.COPPER, CMaterials.BRONZE, CMaterials.BRASS, CMaterials.ELECTRUM, CMaterials.INVAR};

        for (CMaterial material : materials) {
            GameRegistry.addSmelting(ClayiumMaterials.get(material, ClayiumShape.dust), ClayiumMaterials.get(material, ClayiumShape.ingot), 0.0F);
        }

        materials = new CMaterial[] {CMaterials.ALUMINIUM, CMaterials.SODIUM, CMaterials.ZINC, CMaterials.NICKEL, CMaterials.ALUMINIUM_OD};

        for (CMaterial material : materials) {
            smelter.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.dust), 5, ClayiumMaterials.get(material, ClayiumShape.ingot), e(0.5D, 5), 200L);
        }
        if (ClayiumCore.cfgHardcoreAluminium) {
            smelter.addRecipe(ClayiumMaterials.get(CMaterials.IMPURE_ALUMINIUM, ClayiumShape.dust), 5, ClayiumMaterials.get(CMaterials.IMPURE_ALUMINIUM, ClayiumShape.ingot), e(0.5D, 5), 200L);
        }

        materials = new CMaterial[] {CMaterials.MAGNESIUM, ClayiumMaterial.lithium, CMaterials.ZIRCONIUM, CMaterials.ZINCALMINIUM_ALLOY, CMaterials.ZINCONIUM_ALLOY, ClayiumMaterial.AZ91DAlloy, CMaterials.ZK60A_ALLOY};

        for (CMaterial material : materials) {
            smelter.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.dust), 6, ClayiumMaterials.get(material, ClayiumShape.ingot), e(0.2D, 6), 400L);
        }

        materials = new CMaterial[] {CMaterials.CALCIUM, CMaterials.POTASSIUM};
        for (CMaterial material : materials) {
            blastFurnace.addRecipe(oo(ClayiumMaterials.getOD(material, ClayiumShape.dust)), 0, 5,
                    ii(ClayiumMaterials.get(material, ClayiumShape.ingot)), e(5), 500L);
        }
        materials = new CMaterial[] {ClayiumMaterial.beryllium, ClayiumMaterial.hafnium, ClayiumMaterial.clay_STEEL, CMaterials.STEEL};


        for (CMaterial material : materials) {
            blastFurnace.addRecipe(oo(ClayiumMaterials.getOD(material, ClayiumShape.dust)), 0, 6,
                    ii(ClayiumMaterials.get(material, ClayiumShape.ingot)), e(6), 500L);
        }
        materials = new CMaterial[] {CMaterials.MANGANESE, ClayiumMaterial.strontium, ClayiumMaterial.barium, ClayiumMaterial.clayium};

        for (CMaterial material : materials) {
            blastFurnace.addRecipe(oo(ClayiumMaterials.getOD(material, ClayiumShape.dust)), 0, 7,
                    ii(ClayiumMaterials.get(material, ClayiumShape.ingot)), e(2.0D, 7), 1000L);
        }
        materials = new CMaterial[] {CMaterials.TITANIUM, ClayiumMaterial.ultimateAlloy};

        for (CMaterial material : materials) {
            blastFurnace.addRecipe(oo(ClayiumMaterials.getOD(material, ClayiumShape.dust)), 0, 8,
                    ii(ClayiumMaterials.get(material, ClayiumShape.ingot)), e(4.0D, 8), 2000L);
        }
        materials = new CMaterial[] {CMaterials.CHROME};
        for (CMaterial material : materials) {
            blastFurnace.addRecipe(oo(ClayiumMaterials.getOD(material, ClayiumShape.dust)), 0, 9,
                    ii(ClayiumMaterials.get(material, ClayiumShape.ingot)), e(4.0D, 9), 2000L);
        }

        if (ClayiumCore.cfgHardcoreOsmium) {
            blastFurnace.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.IMPURE_OSMIUM, ClayiumShape.ingot)), 0, 11,
                    ii(ClayiumMaterials.get(CMaterials.OSMIUM, ClayiumShape.ingot)), e(4.0D, 11), 100L);
        }


        materials = new CMaterial[] {ClayiumMaterial.quartz, CMaterials.LAPIS, CMaterials.DIAMOND, CMaterials.EMERALD, CMaterials.RUBY, CMaterials.SAPPHIRE, CMaterials.PERIDOT, CMaterials.AMBER, CMaterials.AMETHYST, CMaterials.APATITE, CMaterials.CRYSTAL_FLUX, CMaterials.MALACHITE, CMaterials.TANZANITE, CMaterials.TOPAZ, CMaterials.DILITHIUM, CMaterials.FORCICIUM, CMaterials.GREEN_SAPPHIRE, CMaterials.OPAL, CMaterials.JASPER, CMaterials.BLUE_TOPAZ, CMaterials.FORCILLIUM, CMaterials.MONAZITE, CMaterials.FORCE, ClayiumMaterial.quartzITE, CMaterials.LAZURITE, CMaterials.SODALITE, CMaterials.GARNET_RED, CMaterials.GARNET_YELLOW, CMaterials.NITER, CMaterials.PHOSPHORUS, CMaterials.LIGNITE, CMaterials.GLASS, ClayiumMaterial.iridium};


        for (CMaterial material : materials) {
            if (CMaterials.existOD(material, ClayiumShape.gem)) {
                reactor.addRecipe(oo(ClayiumMaterials.getOD(material, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 0, 7,
                        ii(ClayiumMaterials.getODExist(material, ClayiumShape.gem)), e(7), 10000L);
            }
        }
        reactor.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.COAL, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 0, 7,
                ii(i(Items.coal)), e(7), 10000L);
        reactor.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.CHARCOAL, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 0, 7,
                ii(i(Items.coal, 1, 1)), e(7), 10000L);


        for (ClayiumMaterial material : ClayiumMaterial.gems) {
            if (ClayiumMaterials.existOD(material, ClayiumShape.dust)) {
                grinder.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.gem), 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem), e(0.25D, 5), (int) (80.0F * material.getHardness()));
            }
        }
        grinder.addRecipe(i(Items.COAL), 5, ClayiumMaterials.get(ClayiumMaterial.coal, ClayiumShape.dust), e(0.25D, 5), (int) (80.0F * ClayiumMaterial.coal.getHardness()));
        grinder.addRecipe(i(Items.COAL, 1, 1), 5, ClayiumMaterials.get(ClayiumMaterial.charcoal, ClayiumShape.dust), e(0.25D, 5), (int) (80.0F * ClayiumMaterial.charcoal.getHardness()));


        materials = new CMaterial[] {CMaterials.CINNABAR, CMaterials.CERTUS_QUARTZ, CMaterials.FLUIX};
        for (CMaterial material : materials) {
            if (CMaterials.existOD(material, CMaterials.CRYSTAL)) {
                reactor.addRecipe(oo(ClayiumMaterials.getOD(material, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 0, 7,
                        ii(ClayiumMaterials.getODExist(material, CMaterials.CRYSTAL)), e(7), 10000L);
            }
        }


        for (ClayiumMaterial material : ClayiumMaterial.crystals) {
            if (ClayiumMaterials.existOD(material, ClayiumShape.dust)) {
                grinder.addRecipe(ClayiumMaterials.getOD(material, ClayiumShape.crystal), 5, ClayiumMaterials.getODExist(material, ClayiumShape.dust), e(0.25D, 5), (int) (80.0F * material.getHardness()));
            }
        }


        grinder.addRecipe(i(ClayiumBlocks.clayOre), i(ClayiumItems.clayShards.get(0), ClayiumCore.multiplyProgressionRateStackSize(2)), 1L, ClayiumCore.divideByProgressionRate(3));
        grinder.addRecipe(i(ClayiumBlocks.denseClayOre), i(ClayiumItems.clayShards.get(1), ClayiumCore.multiplyProgressionRateStackSize(3)), 1L, ClayiumCore.divideByProgressionRate(6));
        grinder.addRecipe(i(ClayiumBlocks.largeDenseClayOre), i(ClayiumItems.clayShards.get(2), ClayiumCore.multiplyProgressionRateStackSize(5)), 1L, ClayiumCore.divideByProgressionRate(9));
        condenser.addRecipe(i(ClayiumItems.clayShards.get(0), 4), i(ClayiumBlocks.compressedClay.get(1), 1, 1), 1L, ClayiumCore.divideByProgressionRate(3));
        condenser.addRecipe(i(ClayiumItems.clayShards.get(1), 4), i(ClayiumBlocks.compressedClay.get(2), 1, 2), 1L, ClayiumCore.divideByProgressionRate(6));
        condenser.addRecipe(i(ClayiumItems.clayShards.get(2), 4), i(ClayiumBlocks.compressedClay.get(3), 1, 3), 1L, ClayiumCore.divideByProgressionRate(9));


        for (ClayiumMaterial material : ClayiumMaterial.ores) {
            if (ClayiumMaterials.existOD(material, ClayiumShape.dust)) {
                grinder.addRecipe(ODHelper.getODStack("ore" + material.getODName()), 5, ClayiumMaterials.getODExist(material, ClayiumShape.dust, 2), e(0.025D, 5), 80L);
                grinder.addRecipe(ODHelper.getODStack("oreRedgranite" + material.getODName()), 5, ClayiumMaterials.getODExist(material, ClayiumShape.dust, 2), e(0.025D, 5), 80L);
                grinder.addRecipe(ODHelper.getODStack("oreBlackgranite" + material.getODName()), 5, ClayiumMaterials.getODExist(material, ClayiumShape.dust, 2), e(0.025D, 5), 80L);
                grinder.addRecipe(ODHelper.getODStack("oreEndstone" + material.getODName()), 5, ClayiumMaterials.getODExist(material, ClayiumShape.dust, 2), e(0.025D, 5), 80L);
            }
        }

        for (ClayiumMaterial material : new ClayiumMaterial[] { ClayiumMaterial.CERTUS_QUARTZ, CMaterials.CINNABAR}) {
            if (CMaterials.existOD(material, CMaterials.CRYSTAL)) {
                grinder.addRecipe("ore" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, CMaterials.CRYSTAL, 2), e(0.025D, 5), 80L);
                grinder.addRecipe("oreRedgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, CMaterials.CRYSTAL, 2), e(0.025D, 5), 80L);
                grinder.addRecipe("oreBlackgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, CMaterials.CRYSTAL, 2), e(0.025D, 5), 80L);
                grinder.addRecipe("oreEndstone" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, CMaterials.CRYSTAL, 2), e(0.025D, 5), 80L);
            }
        }

        for (CMaterial material : new CMaterial[] {ClayiumMaterial.quartz, CMaterials.DIAMOND, CMaterials.EMERALD, CMaterials.AMETHYST, CMaterials.AMBER, CMaterials.MALACHITE, CMaterials.PERIDOT, CMaterials.RUBY, CMaterials.SAPPHIRE, CMaterials.TANZANITE, CMaterials.TOPAZ, CMaterials.OPAL, CMaterials.JASPER, CMaterials.FORCILLIUM, CMaterials.FORCE, CMaterials.GARNET_RED, CMaterials.GARNET_YELLOW, CMaterials.NITER, CMaterials.LIGNITE, ClayiumMaterial.quartzITE, CMaterials.DILITHIUM, CMaterials.ORICHALCUM}) {


            if (CMaterials.existOD(material, ClayiumShape.gem)) {
                grinder.addRecipe("ore" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 2), e(0.025D, 5), 80L);
                grinder.addRecipe("oreRedgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 2), e(0.025D, 5), 80L);
                grinder.addRecipe("oreBlackgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 2), e(0.025D, 5), 80L);
                grinder.addRecipe("oreEndstone" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 2), e(0.025D, 5), 80L);
            }
        }
        for (CMaterial material : new CMaterial[] {CMaterials.MONAZITE, CMaterials.FORCICIUM}) {

            if (CMaterials.existOD(material, ClayiumShape.gem)) {
                grinder.addRecipe("ore" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 4), e(0.025D, 5), 80L);
                grinder.addRecipe("oreRedgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 4), e(0.025D, 5), 80L);
                grinder.addRecipe("oreBlackgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 4), e(0.025D, 5), 80L);
                grinder.addRecipe("oreEndstone" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 4), e(0.025D, 5), 80L);
            }
        }
        for (CMaterial material : new CMaterial[] {CMaterials.LAPIS, CMaterials.LAZURITE, CMaterials.SODALITE, CMaterials.PHOSPHORUS, CMaterials.APATITE}) {

            if (CMaterials.existOD(material, ClayiumShape.gem)) {
                grinder.addRecipe("ore" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 10), e(0.025D, 5), 80L);
                grinder.addRecipe("oreRedgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 10), e(0.025D, 5), 80L);
                grinder.addRecipe("oreBlackgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 10), e(0.025D, 5), 80L);
                grinder.addRecipe("oreEndstone" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.gem, 10), e(0.025D, 5), 80L);
            }
        }
        for (CMaterial material : new CMaterial[] {CMaterials.REDSTONE, CMaterials.NIKOLITE, CMaterials.ELECTROTINE, CMaterials.YELLOWSTONE, CMaterials.BLUESTONE, CMaterials.SULFUR, CMaterials.SALTPETER}) {
            if (CMaterials.existOD(material, ClayiumShape.dust)) {
                grinder.addRecipe("ore" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.dust, 10), e(0.025D, 5), 80L);
                grinder.addRecipe("oreRedgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.dust, 10), e(0.025D, 5), 80L);
                grinder.addRecipe("oreBlackgranite" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.dust, 10), e(0.025D, 5), 80L);
                grinder.addRecipe("oreEndstone" + material.oreDictionaryName, 5, ClayiumMaterials.getODExist(material, ClayiumShape.dust, 10), e(0.025D, 5), 80L);
            }
        }

/*
        alloySmelter.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.ZINC, ClayiumShape.ingot, 9), ClayiumMaterials.getOD(CMaterials.ALUMINIUM, ClayiumShape.ingot)), 0, 6,
                ii(ClayiumMaterials.get(CMaterials.ZINCALMINIUM_ALLOY, ClayiumShape.ingot, 10)), e(6), 50L);
        alloySmelter.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.ZINC, ClayiumShape.ingot, 9), ClayiumMaterials.getOD(CMaterials.ALUMINIUM_OD, ClayiumShape.ingot)), 0, 6,
                ii(ClayiumMaterials.get(CMaterials.ZINCALMINIUM_ALLOY, ClayiumShape.ingot, 10)), e(6), 50L);
        alloySmelter.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.MAGNESIUM, ClayiumShape.ingot, 9), ClayiumMaterials.get(CMaterials.ZINCALMINIUM_ALLOY, ClayiumShape.ingot)), 0, 6,
                ii(ClayiumMaterials.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.ingot, 10)), e(7), 500L);
        alloySmelter.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.ZINC, ClayiumShape.ingot, 9), ClayiumMaterials.getOD(CMaterials.ZIRCONIUM, ClayiumShape.ingot)), 0, 6,
                ii(ClayiumMaterials.get(CMaterials.ZINCONIUM_ALLOY, ClayiumShape.ingot, 10)), e(3.0D, 7), 50L);
        alloySmelter.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.MAGNESIUM, ClayiumShape.ingot, 19), ClayiumMaterials.get(CMaterials.ZINCONIUM_ALLOY, ClayiumShape.ingot)), 0, 6,
                ii(ClayiumMaterials.get(CMaterials.ZK60A_ALLOY, ClayiumShape.ingot, 20)), e(3.0D, 7), 500L);

        shapes = new CShape[] {ClayiumShape.ingot, ClayiumShape.dust};
        for (CShape shape1 : shapes) {
            for (CShape shape2 : shapes) {
                alloySmelter.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.COPPER, shape1, 3), ClayiumMaterials.getOD(CMaterials.TIN, shape2)), 0, 0,
                        ii(ClayiumMaterials.get(CMaterials.BRONZE, ClayiumShape.ingot, 4)), e(4), 40L);
                alloySmelter.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.COPPER, shape1, 3), ClayiumMaterials.getOD(CMaterials.ZINC, shape2)), 0, 0,
                        ii(ClayiumMaterials.get(CMaterials.BRASS, ClayiumShape.ingot, 4)), e(4), 40L);
                alloySmelter.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.GOLD, shape1), ClayiumMaterials.getOD(CMaterials.SILVER, shape2)), 0, 0,
                        ii(ClayiumMaterials.get(CMaterials.ELECTRUM, ClayiumShape.ingot, 2)), e(4), 40L);
                alloySmelter.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.IRON, shape1, 2), ClayiumMaterials.getOD(CMaterials.NICKEL, shape2)), 0, 0,
                        ii(ClayiumMaterials.get(CMaterials.INVAR, ClayiumShape.ingot, 3)), e(4), 40L);
            }

            blastFurnace.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.IRON, shape1), i(Items.coal, 2)), 0, 6,
                    ii(ClayiumMaterials.get(CMaterials.STEEL, ClayiumShape.ingot)), e(6), 500L);
        }

        GameRegistry.addRecipe(new ShapelessOreRecipe(
                ClayiumMaterials.get(ClayiumMaterial.impureUltimateAlloy, ClayiumShape.ingot, 9),
                oo(ClayiumMaterials.getODName(ClayiumMaterial.barium, ClayiumShape.ingot), ClayiumMaterials.getODName(ClayiumMaterial.strontium, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.CALCIUM, ClayiumShape.ingot),
                        ClayiumMaterials.getODName(ClayiumMaterial.clayium, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.ALUMINIUM, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.ALUMINIUM, ClayiumShape.ingot),
                        ClayiumMaterials.getODName(CMaterials.ALUMINIUM, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.ALUMINIUM, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.ALUMINIUM, ClayiumShape.ingot))));
        GameRegistry.addRecipe(new ShapelessOreRecipe(
                ClayiumMaterials.get(ClayiumMaterial.impureUltimateAlloy, ClayiumShape.ingot, 9),
                oo(ClayiumMaterials.getODName(ClayiumMaterial.barium, ClayiumShape.ingot), ClayiumMaterials.getODName(ClayiumMaterial.strontium, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.CALCIUM, ClayiumShape.ingot),
                        ClayiumMaterials.getODName(ClayiumMaterial.clayium, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, ClayiumShape.ingot),
                        ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, ClayiumShape.ingot), ClayiumMaterials.getODName(CMaterials.ALUMINIUM_OD, ClayiumShape.ingot))));


        grinder.addRecipe(i(Blocks.stone), i(Blocks.cobblestone), 1L, 3L);
        grinder.addRecipe(i(Blocks.cobblestone, ClayiumCore.multiplyProgressionRateStackSize(1)), i(Blocks.gravel, ClayiumCore.multiplyProgressionRateStackSize(1)), 1L, 10L);
        grinder.addRecipe(i(Blocks.cobblestone, ClayiumCore.multiplyProgressionRateStackSize(4)), i(Blocks.gravel, ClayiumCore.multiplyProgressionRateStackSize(4)), 1L, 10L);
        grinder.addRecipe(i(Blocks.cobblestone, ClayiumCore.multiplyProgressionRateStackSize(16)), i(Blocks.gravel, ClayiumCore.multiplyProgressionRateStackSize(16)), 1L, 10L);
        grinder.addRecipe(i(Blocks.cobblestone, ClayiumCore.multiplyProgressionRateStackSize(64)), i(Blocks.gravel, ClayiumCore.multiplyProgressionRateStackSize(64)), 1L, 10L);
        grinder.addRecipe(i(Blocks.gravel), i(Blocks.sand), 10L, 10L);


        centrifuge.addRecipe(ii(i(Blocks.gravel, ClayiumCore.multiplyProgressionRateStackSize(1))), 0, 4,
                ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block, ClayiumCore.multiplyProgressionRateStackSize(1))), 1L, 2L);
        centrifuge.addRecipe(ii(i(Blocks.gravel, ClayiumCore.multiplyProgressionRateStackSize(4))), 0, 4,
                ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block, ClayiumCore.multiplyProgressionRateStackSize(4))), 2L, 2L);
        centrifuge.addRecipe(ii(i(Blocks.gravel, ClayiumCore.multiplyProgressionRateStackSize(16))), 0, 5,
                ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block, ClayiumCore.multiplyProgressionRateStackSize(16))), 4L, 1L);
        centrifuge.addRecipe(ii(i(Blocks.gravel, ClayiumCore.multiplyProgressionRateStackSize(64))), 0, 6,
                ii(ClayiumMaterials.get(ClayiumMaterial.denseClay, ClayiumShape.block, ClayiumCore.multiplyProgressionRateStackSize(64))), 8L, 1L);


        reactor.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.orgClay, ClayiumShape.dust), i(Blocks.gravel)), 0, 7, ii(i(Blocks.dirt)), e(7), 100L);
        CAInjector.addRecipe(ii(i(Blocks.gravel), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Blocks.dirt)), e(2.0D, 10), 60L);


        grinder.addRecipe(i(Blocks.sandstone, 1, 32767), 3, i(Blocks.sand, 4), e(3), 100L);
        CAInjector.addRecipe(ii(i(Blocks.sand), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Blocks.sand, 1, 1)), e(2.0D, 10), 60L);


        chemicalReactor.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.engClay, ClayiumShape.dust)), 0, 5,
                ii(ClayiumMaterials.get(CMaterials.IMPURE_REDSTONE, ClayiumShape.dust), ClayiumMaterials.get(CMaterials.IMPURE_GLOWSTONE, ClayiumShape.dust)), e(5), 10L);
        reactor.addRecipe(oo(ClayiumMaterials.get(CMaterials.IMPURE_REDSTONE, ClayiumShape.dust)), 0, 7,
                ii(i(Items.redstone)), e(0.1D, 7), 2000L);
        reactor.addRecipe(oo(ClayiumMaterials.get(CMaterials.IMPURE_GLOWSTONE, ClayiumShape.dust)), 0, 7,
                ii(i(Items.glowstone_dust)), e(0.1D, 7), 2000L);


        CAInjector.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.REDSTONE, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Blocks.obsidian)), e(2.0D, 10), 60L);


        condenser.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.coal, ClayiumShape.block, 8), 5, i(Items.DIAMOND), e(5), 100L);
        reactor.addRecipe(oo(i(Items.coal, 64), ClayiumMaterials.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 0, 7,
                ii(i(Items.diamond)), e(7), 10000L);


        reactor.addRecipe(oo(ClayiumMaterials.get(ClayiumMaterial.orgClay, ClayiumShape.dust), ClayiumMaterials.getOD(CMaterials.POTASSIUM, ClayiumShape.dust)), 0, 8, ii(ClayiumMaterials.get(CMaterials.SALTPETER, ClayiumShape.dust)), e(8), 10000L);
        GameRegistry.addRecipe(new ShapelessOreRecipe(
                i(Items.gunpowder, 2),
                oo(ClayiumMaterials.getODName(CMaterials.SALTPETER, ClayiumShape.dust), ClayiumMaterials.getODName(CMaterials.SALTPETER, ClayiumShape.dust),
                        ClayiumMaterials.getODName(CMaterials.SULFUR, ClayiumShape.dust), ClayiumMaterials.getODName(CMaterials.CHARCOAL, ClayiumShape.dust))));


        grinder.addRecipe(i(Blocks.wool, 1, 32767), 5, i(Items.string, 4), e(5), 100L);
        grinder.addRecipe(i(Blocks.carpet, 1, 32767), 5, i(Items.string, 2), e(5), 100L);
        grinder.addRecipe(i(Items.blaze_rod), 5, i(Items.blaze_powder, 5), e(5), 100L);
        grinder.addRecipe(i(Items.bone), 5, i(Items.dye, 5, 15), e(5), 100L);

        centrifuge.addRecipe(ii(i(CBlocks.blockClayTreeLog)), 0, 6,
                ii(ClayiumMaterials.get(ClayiumMaterial.advClay, ClayiumShape.dust, 16), ClayiumMaterials.get(CMaterials.MANGANESE, ClayiumShape.dust, 5), ClayiumMaterials.get(ClayiumMaterial.lithium, ClayiumShape.dust, 3), ClayiumMaterials.get(CMaterials.ZIRCONIUM, ClayiumShape.dust, 1)), 10000L, 400L);

        assembler.addRecipe(ii(i(Blocks.stonebrick), i(Blocks.vine)), 0, 6,
                ii(i(Blocks.stonebrick, 1, 1)), e(6), 20L);

        reactor.addRecipe(oo(ClayiumMaterials.getOD(ClayiumMaterial.ultimateAlloy, ClayiumShape.dust, 4), i(Items.glass_bottle)), 0, 11,
                ii(i(Items.experience_bottle)), e(11), 100000000000000L);
        reactor.addRecipe(ii(i(Items.potato)), 0, 11,
                ii(i(Items.poisonous_potato)), e(9), 10000000000L);


        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_ALUMINIUM, ClayiumShape.dust), 200);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_MAGNESIUM, ClayiumShape.dust), 60);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_SODIUM, ClayiumShape.dust), 40);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_LITHIUM, ClayiumShape.dust), 7);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_ZIRCONIUM, ClayiumShape.dust), 5);

        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_ZINC, ClayiumShape.dust), 10);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_MANGANESE, ClayiumShape.dust), 80);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_CALCIUM, ClayiumShape.dust), 20);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_POTASSIUM, ClayiumShape.dust), 15);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_NICKEL, ClayiumShape.dust), 13);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_IRON, ClayiumShape.dust), 9);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_BERYLLIUM, ClayiumShape.dust), 8);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_LEAD, ClayiumShape.dust), 6);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_HAFNIUM, ClayiumShape.dust), 4);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_CHROME, ClayiumShape.dust), 3);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_TITANIUM, ClayiumShape.dust), 3);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_STRONTIUM, ClayiumShape.dust), 2);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_BARIUM, ClayiumShape.dust), 2);
        TileChemicalMetalSeparator.results.add(ClayiumMaterials.get(CMaterials.IMPURE_COPPER, ClayiumShape.dust), 1);


        CMaterial[][] materialmaps = {{CMaterials.IMPURE_ALUMINIUM, CMaterials.ALUMINIUM}, {CMaterials.IMPURE_MAGNESIUM, CMaterials.MAGNESIUM}, {CMaterials.IMPURE_SODIUM, CMaterials.SODIUM}, {CMaterials.IMPURE_LITHIUM, ClayiumMaterial.lithium}, {CMaterials.IMPURE_ZIRCONIUM, CMaterials.ZIRCONIUM}, {CMaterials.IMPURE_ZINC, CMaterials.ZINC}};
        for (CMaterial[] materialmap : materialmaps) {
            electrolysisReactor.addRecipe(ClayiumMaterials.get(materialmap[0], ClayiumShape.dust), 6, ClayiumMaterials.get(materialmap[1], ClayiumShape.dust), e(6), 100L);
        }
        materialmaps = new CMaterial[][] {{CMaterials.IMPURE_MANGANESE, CMaterials.MANGANESE}, {CMaterials.IMPURE_POTASSIUM, CMaterials.POTASSIUM}, {CMaterials.IMPURE_HAFNIUM, ClayiumMaterial.hafnium}, {CMaterials.IMPURE_STRONTIUM, ClayiumMaterial.strontium}, {CMaterials.IMPURE_BARIUM, ClayiumMaterial.barium}, {CMaterials.IMPURE_CALCIUM, CMaterials.CALCIUM}};

        for (CMaterial[] materialmap : materialmaps) {
            electrolysisReactor.addRecipe(ClayiumMaterials.get(materialmap[0], ClayiumShape.dust), 7, ClayiumMaterials.get(materialmap[1], ClayiumShape.dust), e(10.0D, 7), 300L);
        }
        materialmaps = new CMaterial[][] {{CMaterials.IMPURE_IRON, CMaterials.IRON}, {CMaterials.IMPURE_LEAD, CMaterials.LEAD}, {CMaterials.IMPURE_COPPER, CMaterials.COPPER}};
        for (CMaterial[] materialmap : materialmaps) {
            electrolysisReactor.addRecipe(ClayiumMaterials.get(materialmap[0], ClayiumShape.dust), 8, ClayiumMaterials.get(materialmap[1], ClayiumShape.dust), e(10.0D, 8), 1000L);
        }
        materialmaps = new CMaterial[][] {{CMaterials.IMPURE_NICKEL, CMaterials.NICKEL}, {CMaterials.IMPURE_BERYLLIUM, ClayiumMaterial.beryllium}, {CMaterials.IMPURE_CHROME, CMaterials.CHROME}, {CMaterials.IMPURE_TITANIUM, CMaterials.TITANIUM}};
        for (CMaterial[] materialmap : materialmaps) {
            electrolysisReactor.addRecipe(ClayiumMaterials.get(materialmap[0], ClayiumShape.dust), 9, ClayiumMaterials.get(materialmap[1], ClayiumShape.dust), e(10.0D, 9), 3000L);
        }


        CAInjector.addRecipe(ii(ClayiumMaterials.get(ClayiumMaterial.orgClay, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Items.wheat_seeds)), e(2.0D, 10), 60L);
        CAInjector.addRecipe(ii(i(Items.wheat_seeds), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Blocks.yellow_flower)), e(2.0D, 10), 60L);
        CAInjector.addRecipe(ii(i(Blocks.yellow_flower), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Items.apple)), e(2.0D, 10), 60L);
        CAInjector.addRecipe(ii(i(Items.apple), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Items.reeds)), e(2.0D, 10), 60L);
        CAInjector.addRecipe(ii(i(Items.reeds), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Blocks.sapling)), e(2.0D, 10), 60L);
        CAInjector.addRecipe(ii(i(Blocks.sapling), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Blocks.leaves)), e(2.0D, 10), 60L);
        CAInjector.addRecipe(ii(i(Blocks.leaves), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Blocks.log)), e(2.0D, 10), 60L);
        if (CMaterials.existOD("itemRawRubber")) {
            CAInjector.addRecipe(ii(i(Blocks.log), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                    ii(ClayiumMaterials.getODExist("itemRawRubber")), e(2.0D, 10), 60L);
        }

        CAInjector.addRecipe(ii(i(Blocks.grass), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Blocks.tallgrass, 1, 1)), e(2.0D, 10), 60L);
        CAInjector.addRecipe(ii(i(Blocks.mycelium), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Blocks.brown_mushroom, 1, 1)), e(2.0D, 10), 60L);


        reactor.addRecipe(oo(ClayiumMaterials.getOD(CMaterials.CARBON, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.orgClay, ClayiumShape.dust)), 0, 11, ii(i(Items.rotten_flesh)), e(11), 100000000000000L);
        CAInjector.addRecipe(ii(i(Items.rotten_flesh), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Items.leather)), e(2.0D, 10), 60L);
        CAInjector.addRecipe(ii(i(Items.leather), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Items.bone)), e(2.0D, 10), 60L);
        CAInjector.addRecipe(ii(i(Items.bone), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem)), 0, 10,
                ii(i(Items.slime_ball)), e(2.0D, 10), 60L);


        transformer.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.lithium, ClayiumShape.ingot), 7, ClayiumMaterials.get(CMaterials.SODIUM, ClayiumShape.ingot), e(10.0D, 7), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.SODIUM, ClayiumShape.ingot), 7, ClayiumMaterials.get(CMaterials.POTASSIUM, ClayiumShape.ingot), e(30.0D, 7), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.POTASSIUM, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.RUBIDIUM, ClayiumShape.ingot), e(10.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.RUBIDIUM, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.CAESIUM, ClayiumShape.ingot), e(20.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.CAESIUM, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.FRANCIUM, ClayiumShape.ingot), e(30.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.FRANCIUM, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.RADIUM, ClayiumShape.ingot), e(50.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.RADIUM, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.ACTINIUM, ClayiumShape.ingot), e(10.0D, 9), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.ACTINIUM, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.THORIUM, ClayiumShape.ingot), e(20.0D, 9), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.THORIUM, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.PROTACTINIUM, ClayiumShape.ingot), e(30.0D, 9), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.PROTACTINIUM, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.URANIUM, ClayiumShape.ingot), e(50.0D, 9), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.URANIUM, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.NEPTUNIUM, ClayiumShape.ingot), e(80.0D, 9), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.NEPTUNIUM, ClayiumShape.ingot), 10, ClayiumMaterials.get(CMaterials.PLUTONIUM, ClayiumShape.ingot), e(20.0D, 10), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.PLUTONIUM, ClayiumShape.ingot), 11, ClayiumMaterials.get(CMaterials.AMERICIUM, ClayiumShape.ingot), e(30.0D, 11), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.AMERICIUM, ClayiumShape.ingot), 12, ClayiumMaterials.get(CMaterials.CURIUM, ClayiumShape.ingot), e(50.0D, 12), 200L);

        transformer.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.beryllium, ClayiumShape.ingot), 7, ClayiumMaterials.get(CMaterials.MAGNESIUM, ClayiumShape.ingot), e(10.0D, 7), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.MAGNESIUM, ClayiumShape.ingot), 7, ClayiumMaterials.get(CMaterials.CALCIUM, ClayiumShape.ingot), e(20.0D, 7), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.CALCIUM, ClayiumShape.ingot), 7, ClayiumMaterials.get(ClayiumMaterial.strontium, ClayiumShape.ingot), e(30.0D, 7), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.strontium, ClayiumShape.ingot), 7, ClayiumMaterials.get(ClayiumMaterial.barium, ClayiumShape.ingot), e(50.0D, 7), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.barium, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.LANTHANUM, ClayiumShape.ingot), e(10.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.LANTHANUM, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.CERIUM, ClayiumShape.ingot), e(30.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.CERIUM, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.PRASEODYMIUM, ClayiumShape.ingot), e(90.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.PRASEODYMIUM, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.NEODYMIUM, ClayiumShape.ingot), e(20.0D, 9), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.NEODYMIUM, ClayiumShape.ingot), 10, ClayiumMaterials.get(CMaterials.PROMETHIUM, ClayiumShape.ingot), e(10.0D, 10), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.PROMETHIUM, ClayiumShape.ingot), 11, ClayiumMaterials.get(CMaterials.SAMARIUM, ClayiumShape.ingot), e(20.0D, 11), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.SAMARIUM, ClayiumShape.ingot), 12, ClayiumMaterials.get(CMaterials.EUROPIUM, ClayiumShape.ingot), e(60.0D, 12), 200L);

        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.ZIRCONIUM, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.TITANIUM, ClayiumShape.ingot), e(60.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.TITANIUM, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.VANADIUM, ClayiumShape.ingot), e(60.0D, 9), 200L);

        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.MANGANESE, ClayiumShape.ingot), 7, ClayiumMaterials.get(CMaterials.IRON, ClayiumShape.ingot), e(90.0D, 7), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.IRON, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.COBALT, ClayiumShape.ingot), e(30.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.COBALT, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.NICKEL, ClayiumShape.ingot), e(90.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.NICKEL, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.PALLADIUM, ClayiumShape.ingot), e(40.0D, 9), 200L);

        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.ZINC, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.COPPER, ClayiumShape.ingot), e(20.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.COPPER, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.SILVER, ClayiumShape.ingot), e(10.0D, 9), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.SILVER, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.GOLD, ClayiumShape.ingot), e(50.0D, 9), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.GOLD, ClayiumShape.ingot), 10, ClayiumMaterials.get(ClayiumMaterial.platinum, ClayiumShape.ingot), e(30.0D, 10), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.platinum, ClayiumShape.ingot), 11, ClayiumMaterials.get(ClayiumMaterial.iridium, ClayiumShape.ingot), e(10.0D, 11), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.iridium, ClayiumShape.ingot), 11, ClayiumMaterials.get(ClayiumMaterial.mainOsmium, ClayiumShape.ingot), e(30.0D, 11), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.mainOsmium, ClayiumShape.ingot), 12, ClayiumMaterials.get(ClayiumMaterial.rhenium, ClayiumShape.ingot), e(10.0D, 12), 200L);

        transformer.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.hafnium, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.TANTALUM, ClayiumShape.ingot), e(70.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.TANTALUM, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.TUNGSTEN, ClayiumShape.ingot), e(40.0D, 9), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.TUNGSTEN, ClayiumShape.ingot), 10, ClayiumMaterials.get(CMaterials.MOLYBDENUM, ClayiumShape.ingot), e(20.0D, 10), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.MOLYBDENUM, ClayiumShape.ingot), 11, ClayiumMaterials.get(CMaterials.CHROME, ClayiumShape.ingot), e(10.0D, 11), 200L);

        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.LEAD, ClayiumShape.ingot), 7, ClayiumMaterials.get(CMaterials.TIN, ClayiumShape.ingot), e(50.0D, 7), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.TIN, ClayiumShape.ingot), 8, ClayiumMaterials.get(CMaterials.ANTIMONY, ClayiumShape.ingot), e(20.0D, 8), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.ANTIMONY, ClayiumShape.ingot), 9, ClayiumMaterials.get(CMaterials.BISMUTH, ClayiumShape.ingot), e(10.0D, 9), 200L);

        transformer.addRecipe(ClayiumMaterials.getOD(ClayiumMaterial.silicon, ClayiumShape.dust), 7, ClayiumMaterials.get(CMaterials.PHOSPHORUS, ClayiumShape.dust), e(10.0D, 7), 200L);
        transformer.addRecipe(ClayiumMaterials.getOD(CMaterials.PHOSPHORUS, ClayiumShape.dust), 7, ClayiumMaterials.get(CMaterials.SULFUR, ClayiumShape.dust), e(30.0D, 7), 200L);


        registerODChains(transformer, new CMaterial[] {ClayiumMaterial.indClay, CMaterials.CARBON, CMaterials.GRAPHITE, CMaterials.CHARCOAL, CMaterials.COAL, CMaterials.LAPIS, CMaterials.LAZURITE, CMaterials.SODALITE, CMaterials.MONAZITE}, ClayiumShape.dust, new int[] {8, 8, 8, 4, 4, 4, 4, 4, 1}, new int[] {0, 7, 8, 9, 10, 10, 10, 10, 11}, 200);


        registerODChains(transformer, new CMaterial[] {CMaterials.DIAMOND, CMaterials.AMBER, CMaterials.AMETHYST, CMaterials.PERIDOT, CMaterials.SAPPHIRE, CMaterials.RUBY, CMaterials.EMERALD}, ClayiumShape.gem, new int[] {0, 10, 10, 10, 10, 10, 11}, 200);


        registerStackChains(transformer,
                ii(i(Blocks.cobblestone), i(Blocks.netherrack), i(Blocks.end_stone)), new int[] {0, 9, 11}, 20);


        registerStackChains(transformer,
                ii(i(Blocks.stonebrick), i(Blocks.stonebrick, 1, 2), i(Blocks.stonebrick, 1, 3)), new int[] {0, 8, 8}, 20);


        registerStackChains(transformer,
                ii(i(Blocks.dirt), i(Blocks.dirt, 1, 2), i(Blocks.grass), i(Blocks.mycelium), i(Blocks.soul_sand)), new int[] {0, 7, 8, 9, 10}, 20);


        registerStackChains(transformer,
                ii(i(Blocks.log), i(Blocks.log, 1, 1), i(Blocks.log, 1, 2), i(Blocks.log, 1, 3), i(Blocks.log2), i(Blocks.log2, 1, 1)), new int[] {0, 7, 7, 8, 8, 8}, 80);


        registerStackChains(transformer,
                ii(i(Blocks.leaves), i(Blocks.leaves, 1, 1), i(Blocks.leaves, 1, 2), i(Blocks.leaves, 1, 3), i(Blocks.leaves2), i(Blocks.leaves2, 1, 1)), new int[] {0, 7, 7, 8, 8, 8}, 20);


        registerStackChains(transformer,
                ii(i(Blocks.sapling), i(Blocks.sapling, 1, 1), i(Blocks.sapling, 1, 2), i(Blocks.sapling, 1, 3), i(Blocks.sapling, 1, 4), i(Blocks.sapling, 1, 5)), new int[] {0, 7, 7, 8, 8, 8}, 20);


        registerStackChains(transformer,
                ii(i(Items.wheat_seeds), i(Items.pumpkin_seeds), i(Items.melon_seeds), i(Items.dye, 1, 3), i(Items.nether_wart)), new int[] {0, 8, 8, 8, 8}, 20);

        register2to1Recipe(reactor, "dyeLime", i(Items.dye, 1, 3), 8, ClayiumMaterials.getODExist("edamame"), 1000000000L);
        register2to1Recipe(reactor, i(Items.melon_seeds), i(Items.dye, 1, 3), 8, ClayiumMaterials.getODExist("soybeans"), 1000000000L);
        register2to1Recipe(reactor, i(Items.melon_seeds), i(Items.string, 3), 8, ClayiumMaterials.getODExist("seedCotton"), 1000000000L);
        register2to1Recipe(reactor, i(Items.wheat_seeds), i(Items.dye, 1, 3), 10, ClayiumMaterials.getODExist("cropCoffee"), 100000000000000L);


        registerStackChains(transformer,
                ii(i(Items.wheat), i(Items.carrot), i(Items.potato)), new int[] {0, 8, 8}, 20);

        reactor.addRecipe(ii(i(Items.wheat_seeds)), 0, 10, ii(i(Items.wheat)), e(8), 2000000000000000L);
        reactor.addRecipe(ii(i(Items.pumpkin_seeds)), 0, 10, ii(i(Blocks.pumpkin)), e(8), 2000000000000000L);
        reactor.addRecipe(ii(i(Items.melon_seeds)), 0, 10, ii(i(Blocks.melon_block)), e(8), 2000000000000000L);

        register1to1Recipe(reactor, "seedCotton", 8, ClayiumMaterials.getODExist("cropCotton"), 2000000000000000L);

        grinder.addRecipe(i(Blocks.melon_block), 5, i(Items.melon, 9), e(5), 100L);
        if (CMaterials.existOD("flour")) {
            register1to1Recipe(grinder, i(Items.wheat), 5, ClayiumMaterials.getODExist("flour"), 60L);
        } else if (CMaterials.existOD("itemFlour")) {
            register1to1Recipe(grinder, i(Items.wheat), 5, ClayiumMaterials.getODExist("itemFlour"), 60L);
        } else if (CMaterials.existOD("dustFlour")) {
            register1to1Recipe(grinder, i(Items.wheat), 5, ClayiumMaterials.getODExist("dustFlour"), 60L);
        }

        register2to1Recipe(reactor, i(Items.wheat), i(Items.wheat_seeds), 8, ClayiumMaterials.getODExist("cropRice"), 1000000000L);
        register2to1Recipe(reactor, i(Items.wheat), i(Blocks.tallgrass, 1, 1), 8, ClayiumMaterials.getODExist("cropStraw"), 1000000000L);


        register2to1Recipe(reactor, i(Items.apple), "dyeRed", 8, ClayiumMaterials.getODExist("apricot"), 1000000000L);


        registerStackChains(transformer,
                ii(i(Blocks.tallgrass, 1, 1), i(Blocks.tallgrass, 1, 2), i(Blocks.deadbush), i(Blocks.vine), i(Blocks.waterlily)), new int[] {0, 7, 7, 8, 9}, 20);


        registerStackChains(transformer,
                ii(i(Blocks.yellow_flower), i(Blocks.red_flower), i(Blocks.red_flower, 1, 1), i(Blocks.red_flower, 1, 2), i(Blocks.red_flower, 1, 3), i(Blocks.red_flower, 1, 4), i(Blocks.red_flower, 1, 5), i(Blocks.red_flower, 1, 6), i(Blocks.red_flower, 1, 7), i(Blocks.red_flower, 1, 8),
                        i(Blocks.double_plant), i(Blocks.double_plant, 1, 1), i(Blocks.double_plant, 1, 2), i(Blocks.double_plant, 1, 3), i(Blocks.double_plant, 1, 4), i(Blocks.double_plant, 1, 5)), new int[] {
                        0, 7, 7, 7, 7, 7, 7, 7, 7, 7,
                        8, 8, 8, 8, 8, 8}, 20);

        registerStackChains(transformer,
                ii(i(Items.reeds), i(Blocks.cactus)), new int[] {0, 8}, 20);

        register2to1Recipe(reactor, i(Items.reeds), "logWood", 8, ClayiumMaterials.getODExist("bamboo"), 1000000000L);


        registerStackChains(transformer,
                ii(i(Items.rotten_flesh), i(Items.porkchop), i(Items.beef), i(Items.chicken)), new int[] {64, 2, 2, 1}, new int[] {0, 9, 9, 9}, 200);


        registerStackChains(transformer,
                ii(i(Items.leather), i(Blocks.wool), i(Items.feather)), new int[] {1, 4, 16}, new int[] {0, 9, 9}, 80);


        assembler.addRecipe(ii(i(Items.leather, 4), i(Items.string, 16)), 0, 10, ii(i(Items.saddle)), e(10), 6000L);
        assembler.addRecipe(ii(i(Items.paper, 2), i(Items.string, 4)), 0, 10, ii(i(Items.name_tag)), e(10), 600L);


        registerStackChains(transformer,
                ii(i(Items.bone), i(Items.blaze_rod), i(Items.ender_pearl), i(Items.nether_star), i(Blocks.dragon_egg)), new int[] {262144, 4096, 1024, 64, 1}, new int[] {0, 9, 9, 12, 12}, 200);


        registerStackChains(transformer,
                ii(i(Items.slime_ball), i(Items.egg), i(Items.dye), i(Items.spider_eye), i(Items.ghast_tear)), new int[] {0, 8, 8, 9, 10}, 100);


        registerStackChains(transformer,
                ii(i(Items.skull, 1, 1), i(Items.skull), i(Items.skull, 1, 2), i(Items.skull, 1, 4), i(Items.skull, 1, 3)), new int[] {0, 11, 11, 11, 12}, 1000);


        registerStackChains(transformer,
                ii(i(Blocks.gravel), i(Items.flint), ClayiumMaterials.getODExist(CMaterials.CINNABAR, CMaterials.CRYSTAL)), new int[] {0, 7, 10}, 1000);

/*
        if (ClayiumCore.IntegrationID.EIO.enabled()) {

            register2to1Recipe(alloySmelter, i(Items.redstone), "itemSilicon", 6, ClayiumMaterials.getODExist(CMaterials.REDSTONE_ALLOY, ClayiumShape.ingot), 100L);
            register2to1Recipe(alloySmelter, i(Items.redstone), ClayiumMaterials.getOD(CMaterials.IRON, ClayiumShape.ingot), 6, ClayiumMaterials.getODExist(CMaterials.CONDUCTIVE_IRON, ClayiumShape.ingot), 100L);
            register2to1Recipe(alloySmelter, i(Items.redstone), ClayiumMaterials.getOD(CMaterials.IRON, ClayiumShape.dust), 6, ClayiumMaterials.getODExist(CMaterials.CONDUCTIVE_IRON, ClayiumShape.ingot), 100L);
            register2to1Recipe(reactor, i(Items.redstone), ClayiumMaterials.getOD(CMaterials.GOLD, ClayiumShape.ingot), 8, ClayiumMaterials.getODExist(CMaterials.ENERGETIC_ALLOY, ClayiumShape.ingot), 1000000000L);
            register2to1Recipe(reactor, i(Items.redstone), ClayiumMaterials.getOD(CMaterials.GOLD, ClayiumShape.dust), 8, ClayiumMaterials.getODExist(CMaterials.ENERGETIC_ALLOY, ClayiumShape.ingot), 1000000000L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.STEEL, ClayiumShape.ingot), "itemSilicon", 7, ClayiumMaterials.getODExist(CMaterials.ELECTRICAL_STEEL, ClayiumShape.ingot), 500L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.STEEL, ClayiumShape.dust), "itemSilicon", 7, ClayiumMaterials.getODExist(CMaterials.ELECTRICAL_STEEL, ClayiumShape.ingot), 500L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.STEEL, ClayiumShape.ingot), i(Blocks.obsidian), 7, ClayiumMaterials.getODExist(CMaterials.DARK_STEEL, ClayiumShape.ingot), 500L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.STEEL, ClayiumShape.dust), i(Blocks.obsidian), 7, ClayiumMaterials.getODExist(CMaterials.DARK_STEEL, ClayiumShape.ingot), 500L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.IRON, ClayiumShape.ingot), i(Items.ender_pearl), 6, ClayiumMaterials.getODExist(CMaterials.PHASED_IRON, ClayiumShape.ingot), 500L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.IRON, ClayiumShape.dust), i(Items.ender_pearl), 6, ClayiumMaterials.getODExist(CMaterials.PHASED_IRON, ClayiumShape.ingot), 500L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.ENERGETIC_ALLOY, ClayiumShape.ingot), i(Items.ender_pearl), 6, ClayiumMaterials.getODExist(CMaterials.PHASED_GOLD, ClayiumShape.ingot), 500L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.ENERGETIC_ALLOY, ClayiumShape.dust), i(Items.ender_pearl), 6, ClayiumMaterials.getODExist(CMaterials.PHASED_GOLD, ClayiumShape.ingot), 500L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.GOLD, ClayiumShape.ingot), i(Blocks.soul_sand), 6, ClayiumMaterials.getODExist(CMaterials.SOULARIUM, ClayiumShape.ingot), 500L);
            register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.GOLD, ClayiumShape.dust), i(Blocks.soul_sand), 6, ClayiumMaterials.getODExist(CMaterials.SOULARIUM, ClayiumShape.ingot), 500L);
        }


        if (ClayiumCore.IntegrationID.TF.enabled()) {

            for (CShape shape1 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                for (CShape shape2 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                    register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.COPPER, shape1, 3), ClayiumMaterials.getOD(CMaterials.SILVER, shape2), 8, ClayiumMaterials.getODExist(CMaterials.SIGNALUM, ClayiumShape.ingot, 4), 1000000000L);
                    register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.TIN, shape1, 3), ClayiumMaterials.getOD(CMaterials.SILVER, shape2), 8, ClayiumMaterials.getODExist(CMaterials.LUMIUM, ClayiumShape.ingot, 4), 1000000000L);
                }
                register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.ELECTRUM, shape1), i(Items.redstone, 2), 6, ClayiumMaterials.getODExist(CMaterials.ELECTRUM_FLUX, ClayiumShape.ingot), 100L);
            }
            register2to1Recipe(reactor, i(Items.diamond), i(Items.redstone, 2), 7, ClayiumMaterials.getODExist(CMaterials.CRYSTAL_FLUX, ClayiumShape.gem), 10000L);
        }


        if (ClayiumCore.IntegrationID.FFM.enabled()) {
            register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.PHOSPHORUS, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.APATITE, ClayiumShape.gem), 60L);
            register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.PHOSPHORUS, ClayiumShape.gem), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.APATITE, ClayiumShape.gem), 60L);
        }


        if (ClayiumCore.IntegrationID.AE2.enabled()) {
            register1to1Recipe(transformer, i(Items.quartz), 10, ClayiumMaterials.getODExist(CMaterials.CERTUS_QUARTZ, CMaterials.CRYSTAL), 60L);
        }


        if (ClayiumCore.IntegrationID.TIC.enabled()) {
            register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.COBALT, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.ARDITE, ClayiumShape.ingot), 60L);
            for (CShape shape1 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                for (CShape shape2 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.ALUMINIUM, shape1, 3), ClayiumMaterials.getOD(CMaterials.COPPER, shape2), 6, ClayiumMaterials.getODExist(CMaterials.ALUMINUM_BRASS, ClayiumShape.ingot, 4), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.ALUMINIUM_OD, shape1, 3), ClayiumMaterials.getOD(CMaterials.COPPER, shape2), 6, ClayiumMaterials.getODExist(CMaterials.ALUMINUM_BRASS, ClayiumShape.ingot, 4), 100L);
                    register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.ALUMINIUM, shape1, 5), ClayiumMaterials.getOD(CMaterials.DARK_STEEL, shape2, 2), 8, ClayiumMaterials.getODExist(CMaterials.ALUMITE, ClayiumShape.ingot, 3), 1000000000L);
                    register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.ALUMINIUM_OD, shape1, 5), ClayiumMaterials.getOD(CMaterials.DARK_STEEL, shape2, 2), 8, ClayiumMaterials.getODExist(CMaterials.ALUMITE, ClayiumShape.ingot, 3), 1000000000L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.ARDITE, shape1), ClayiumMaterials.getOD(CMaterials.COBALT, shape2), 6, ClayiumMaterials.getODExist(CMaterials.MANYULLYN, ClayiumShape.ingot), 100L);

                    register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.IRON, shape1), ClayiumMaterials.getOD(CMaterials.COBALT, shape2), 8, ClayiumMaterials.getODExist(CMaterials.POKEFENNIUM, ClayiumShape.ingot, 2), 1000000000L);
                }
                register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.IRON, shape1), ClayiumMaterials.getOD(CMaterials.EMERALD, ClayiumShape.gem, 9), 9, ClayiumMaterials.getODExist(CMaterials.PIG_IRON, ClayiumShape.ingot), 100000000000L);
                register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.ARDITE, shape1), i(Blocks.obsidian), 8, ClayiumMaterials.getODExist(CMaterials.FAIRY, ClayiumShape.ingot, 2), 1000000000L);
            }
        }


        if (ClayiumCore.IntegrationID.PR_EX.enabled()) {
            for (CShape shape1 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                if (ClayiumCore.IntegrationID.GT.loaded() || CMaterials.existOD(CMaterials.CONDUCTIVE_IRON, ClayiumShape.ingot)) {
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.COPPER, shape1), i(Items.redstone, 4), 6, ClayiumMaterials.getODExist(CMaterials.RED_ALLOY, ClayiumShape.ingot), 100L);
                } else {
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.IRON, shape1), i(Items.redstone, 8), 6, ClayiumMaterials.getODExist(CMaterials.RED_ALLOY, ClayiumShape.ingot), 100L);
                }
                register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.IRON, shape1), ClayiumMaterials.getOD(CMaterials.ELECTROTINE, ClayiumShape.dust, 8), 6, ClayiumMaterials.getODExist(CMaterials.ELECTROTINE_ALLOY, ClayiumShape.ingot), 100L);
            }
        }


        if (ClayiumCore.IntegrationID.MEK.enabled()) {
            for (CShape shape1 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.OSMIUM, shape1), i(Items.glowstone_dust), 6, ClayiumMaterials.getODExist(CMaterials.REFINED_GLOWSTONE, ClayiumShape.ingot), 100L);
            }
            for (CShape shape1 : new CShape[] {ClayiumShape.gem, ClayiumShape.dust}) {
                for (CShape shape2 : new CShape[] {ClayiumShape.block, ClayiumShape.dust}) {
                    register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.DIAMOND, shape1), ClayiumMaterials.getOD(CMaterials.OBSIDIAN, shape2), 8, ClayiumMaterials.getODExist(CMaterials.REFINED_OBSIDIAN, ClayiumShape.ingot), 1000000000L);
                }
            }
        }


        if (ClayiumCore.IntegrationID.BR.enabled() &&
                CMaterials.existOD(CMaterials.YELLORIUM, ClayiumShape.ingot)) {
            boolean flag = true;
            for (ItemStack uranium : OreDictionary.getOres(ClayiumMaterials.getODName(CMaterials.URANIUM, ClayiumShape.ingot))) {
                for (ItemStack yellorium : OreDictionary.getOres(ClayiumMaterials.getODName(CMaterials.YELLORIUM, ClayiumShape.ingot))) {
                    if (UtilItemStack.areTypeEqual(uranium, yellorium))
                        flag = false;
                }
            }
            if (flag) {
                register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.URANIUM, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.YELLORIUM, ClayiumShape.ingot), 60L);
                registerODChains(transformer, new CMaterial[] {CMaterials.YELLORIUM, CMaterials.CYANITE, CMaterials.BLUTONIUM, CMaterials.LUDICRITE}, ClayiumShape.ingot, new int[] {16, 16, 8, 1}, new int[] {0, 7, 10, 12}, 200);

            } else {

                boolean flag1 = true;
                for (ItemStack plutonium : OreDictionary.getOres(ClayiumMaterials.getODName(CMaterials.PLUTONIUM, ClayiumShape.ingot))) {
                    for (ItemStack blutonium : OreDictionary.getOres(ClayiumMaterials.getODName(CMaterials.BLUTONIUM, ClayiumShape.ingot))) {
                        if (UtilItemStack.areTypeEqual(plutonium, blutonium))
                            flag1 = false;
                    }
                }
                if (flag1) {
                    registerODChains(transformer, new CMaterial[] {CMaterials.CYANITE, CMaterials.BLUTONIUM, CMaterials.LUDICRITE}, ClayiumShape.ingot, new int[] {16, 8, 1}, new int[] {7, 10, 12}, 200);

                } else {

                    register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.PLUTONIUM, ClayiumShape.ingot, 8), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 64), 12, ClayiumMaterials.getODExist(CMaterials.LUDICRITE, ClayiumShape.ingot), 200L);
                }
            }
        }


        if (ClayiumCore.IntegrationID.GC.enabled()) {
            register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.NICKEL, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.METEORIC_IRON, ClayiumShape.ingot), 60L);
            registerODChains(transformer, new CMaterial[] {CMaterials.METEORIC_IRON, CMaterials.DESH}, ClayiumShape.ingot, new int[] {0, 11}, 200);
        }


        if (ClayiumCore.IntegrationID.FZ.enabled()) {
            register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.INVAR, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.FZ_DARK_IRON, ClayiumShape.ingot), 60L);
        }


        if (ClayiumCore.IntegrationID.MISC.enabled()) {

            register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.EMERALD, ClayiumShape.gem), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.TOPAZ, ClayiumShape.gem), 60L);
            registerODChains(transformer, new CMaterial[] {CMaterials.TOPAZ, CMaterials.MALACHITE, CMaterials.TANZANITE}, ClayiumShape.gem, new int[] {0, 10, 10}, 200);


            register2to1Recipe(reactor, ClayiumMaterials.getOD(ClayiumMaterial.quartz, ClayiumShape.gem), ClayiumMaterials.getOD(ClayiumMaterial.lithium, ClayiumShape.dust), 8, ClayiumMaterials.getODExist(CMaterials.DILITHIUM, ClayiumShape.gem), 1000000000L);


            register2to1Recipe(reactor, ClayiumMaterials.getOD(ClayiumMaterial.quartz, ClayiumShape.gem), i(Items.redstone, 4), 8, ClayiumMaterials.getODExist(CMaterials.FORCICIUM, ClayiumShape.gem, 4), 1000000000L);
        }


        if (ClayiumCore.IntegrationID.GT.enabled()) {

            registerODChains(transformer, new CMaterial[] {CMaterials.ALUMINIUM, CMaterials.GALLIUM}, ClayiumShape.ingot, new int[] {0, 11}, 200);


            registerODChains(transformer, new CMaterial[] {CMaterials.ALUMINIUM_OD, CMaterials.GALLIUM}, ClayiumShape.ingot, new int[] {0, 11}, 200);


            registerODChains(transformer, new CMaterial[] {CMaterials.VANADIUM, CMaterials.NIOBIUM, CMaterials.YTTRIUM}, ClayiumShape.ingot, new int[] {0, 11, 11}, 200);


            registerODChains(transformer, new CMaterial[] {CMaterials.EUROPIUM, CMaterials.NAQUADAH, CMaterials.NAQUADAH_ENRICHED, CMaterials.NAQUADRIA}, ClayiumShape.ingot, new int[] {0, 12, 12, 12}, 200);


            registerODChains(transformer, new CMaterial[] {CMaterials.CURIUM, CMaterials.NEUTRONIUM}, ClayiumShape.ingot, new int[] {0, 12}, 200);


            registerODChains(transformer, new CMaterial[] {CMaterials.REDSTONE, CMaterials.NIKOLITE, CMaterials.ELECTROTINE}, ClayiumShape.dust, new int[] {0, 9, 9}, 200);


            register2to1Recipe(CAInjector, ClayiumMaterials.getOD(ClayiumMaterial.quartz, ClayiumShape.gem), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(ClayiumMaterial.quartzITE, ClayiumShape.gem), 60L);
            register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.SALTPETER, ClayiumShape.dust), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.NITER, ClayiumShape.gem), 60L);

            for (CShape shape1 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                for (CShape shape2 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                    register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.TUNGSTEN, shape1), ClayiumMaterials.getOD(CMaterials.STEEL, shape2), 10, ClayiumMaterials.getODExist(CMaterials.TUNGSTEN_STEEL, ClayiumShape.ingot, 2), 1000L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.COPPER, shape1), ClayiumMaterials.getOD(CMaterials.NICKEL, shape2), 6, ClayiumMaterials.getODExist(CMaterials.CUPRONICKEL, ClayiumShape.ingot, 2), 100L);
                    register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.NICKEL, shape1, 4), ClayiumMaterials.getOD(CMaterials.CHROME, shape2), 9, ClayiumMaterials.getODExist(CMaterials.NICHROME, ClayiumShape.ingot, 5), 1000L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.ALUMINIUM, shape1, 2), ClayiumMaterials.getOD(CMaterials.MAGNESIUM, shape2), 6, ClayiumMaterials.getODExist(CMaterials.MAGNALIUM, ClayiumShape.ingot, 3), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.ALUMINIUM_OD, shape1, 2), ClayiumMaterials.getOD(CMaterials.MAGNESIUM, shape2), 6, ClayiumMaterials.getODExist(CMaterials.MAGNALIUM, ClayiumShape.ingot, 3), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.TIN, shape1, 9), ClayiumMaterials.getOD(CMaterials.ANTIMONY, shape2), 6, ClayiumMaterials.getODExist(CMaterials.SOLDERING_ALLOY, ClayiumShape.ingot, 10), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.LEAD, shape1, 4), ClayiumMaterials.getOD(CMaterials.ANTIMONY, shape2), 6, ClayiumMaterials.getODExist(CMaterials.BATTERY_ALLOY, ClayiumShape.ingot, 5), 100L);
                    register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.VANADIUM, shape1, 3), ClayiumMaterials.getOD(CMaterials.GALLIUM, shape2), 10, ClayiumMaterials.getODExist(CMaterials.VANADIUM_GALLIUM, ClayiumShape.ingot, 4), 1000L);
                    register2to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.NIOBIUM, shape1), ClayiumMaterials.getOD(CMaterials.TITANIUM, shape2), 9, ClayiumMaterials.getODExist(CMaterials.NIOBIUM_TITANIUM, ClayiumShape.ingot, 2), 1000L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.TIN, shape1), ClayiumMaterials.getOD(CMaterials.IRON, shape2), 6, ClayiumMaterials.getODExist(CMaterials.TIN_ALLOY, ClayiumShape.ingot, 2), 100L);

                    register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.STEEL, shape1), ClayiumMaterials.getOD(CMaterials.IRON, shape2, 4), 8, ClayiumMaterials.getODExist(CMaterials.WROUGHT_IRON, ClayiumShape.ingot, 5), 1000000000L);
                }
                register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.SILVER, shape1), ClayiumMaterials.getOD(CMaterials.NIKOLITE, ClayiumShape.dust), 6, ClayiumMaterials.getODExist(CMaterials.BLUE_ALLOY, ClayiumShape.ingot, 2), 100L);
            }
            register1to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.TUNGSTEN_STEEL, ClayiumShape.dust), 10, ClayiumMaterials.getODExist(CMaterials.TUNGSTEN_STEEL, ClayiumShape.ingot), 1000L);
            register1to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.KANTHAL, ClayiumShape.dust), 7, ClayiumMaterials.getODExist(CMaterials.KANTHAL, ClayiumShape.ingot), 1000L);
            register1to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.STAINLESS_STEEL, ClayiumShape.dust), 7, ClayiumMaterials.getODExist(CMaterials.STAINLESS_STEEL, ClayiumShape.ingot), 1000L);
            register1to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.VANADIUM_GALLIUM, ClayiumShape.dust), 10, ClayiumMaterials.getODExist(CMaterials.VANADIUM_GALLIUM, ClayiumShape.ingot), 1000L);
            register1to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.YTTRIUM_BARIUM_CUPRATE, ClayiumShape.dust), 6, ClayiumMaterials.getODExist(CMaterials.YTTRIUM_BARIUM_CUPRATE, ClayiumShape.ingot), 1000L);
            register1to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.NIOBIUM_TITANIUM, ClayiumShape.dust), 9, ClayiumMaterials.getODExist(CMaterials.NIOBIUM_TITANIUM, ClayiumShape.ingot), 1000L);
            register1to1Recipe(blastFurnace, ClayiumMaterials.getOD(CMaterials.ULTIMET, ClayiumShape.dust), 9, ClayiumMaterials.getODExist(CMaterials.ULTIMET, ClayiumShape.ingot), 1000L);


            if (CMaterials.existOD(CMaterials.SAPPHIRE, ClayiumShape.gem)) {
                for (CMaterial material : new CMaterial[] {CMaterials.GREEN_SAPPHIRE, CMaterials.OPAL, CMaterials.JASPER, CMaterials.GARNET_RED, CMaterials.GARNET_YELLOW, CMaterials.FORCE, CMaterials.FORCILLIUM}) {
                    register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.SAPPHIRE, ClayiumShape.gem), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(material, ClayiumShape.ingot), 60L);
                    if (CMaterials.existOD(material, ClayiumShape.gem))
                        break;
                }
            } else {
                for (CMaterial material : new CMaterial[] {CMaterials.GREEN_SAPPHIRE, CMaterials.OPAL, CMaterials.JASPER, CMaterials.GARNET_RED, CMaterials.GARNET_YELLOW, CMaterials.FORCE, CMaterials.FORCILLIUM}) {
                    register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.DIAMOND, ClayiumShape.gem), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(material, ClayiumShape.ingot), 60L);
                    if (CMaterials.existOD(material, ClayiumShape.gem))
                        break;
                }
            }
            registerODChains(transformer, new CMaterial[] {CMaterials.GREEN_SAPPHIRE, CMaterials.OPAL, CMaterials.JASPER, CMaterials.GARNET_RED, CMaterials.GARNET_YELLOW, CMaterials.FORCE, CMaterials.FORCILLIUM}, ClayiumShape.gem, new int[] {0, 8, 8, 9, 9, 11, 11}, 200);


            if (CMaterials.existOD(CMaterials.TOPAZ, ClayiumShape.gem)) {
                register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.TOPAZ, ClayiumShape.gem), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.BLUE_TOPAZ, ClayiumShape.gem), 60L);
            } else {
                register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.EMERALD, ClayiumShape.gem), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(CMaterials.BLUE_TOPAZ, ClayiumShape.gem), 60L);
            }
        }


        if (ClayiumCore.IntegrationID.MET.enabled()) {

            for (CMaterial material : new CMaterial[] {CMaterials.PROMETHEUM, CMaterials.DEEP_IRON, CMaterials.INFUSCOLIUM, CMaterials.OURECLASE, CMaterials.AREDRITE, CMaterials.ASTRAL_SILVER, CMaterials.CARMOT, CMaterials.MITHRIL, CMaterials.RUBRACIUM, CMaterials.ORICHALCUM, CMaterials.ADAMANTINE, CMaterials.ATLARUS}) {
                register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.PROMETHIUM, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(material, ClayiumShape.ingot), 60L);
                if (CMaterials.existOD(material, ClayiumShape.ingot))
                    break;
            }
            registerODChains(transformer, new CMaterial[] {CMaterials.PROMETHEUM, CMaterials.DEEP_IRON, CMaterials.INFUSCOLIUM, CMaterials.OURECLASE, CMaterials.AREDRITE, CMaterials.ASTRAL_SILVER, CMaterials.CARMOT, CMaterials.MITHRIL, CMaterials.RUBRACIUM, CMaterials.ORICHALCUM, CMaterials.ADAMANTINE, CMaterials.ATLARUS}, ClayiumShape.ingot, new int[] {0, 7, 7, 8, 8, 9, 9, 9, 10, 10, 11, 11}, 200);


            for (CMaterial material : new CMaterial[] {CMaterials.IGNATIUS, CMaterials.SHADOW_IRON, CMaterials.LEMURITE, CMaterials.MIDASIUM, CMaterials.VYROXERES, CMaterials.CERUCLASE, CMaterials.ALDUORITE, CMaterials.KALENDRITE, CMaterials.VULCANITE, CMaterials.SANGUINITE}) {
                register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.RUBIDIUM, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(material, ClayiumShape.ingot), 60L);
                if (CMaterials.existOD(material, ClayiumShape.ingot))
                    break;
            }
            registerODChains(transformer, new CMaterial[] {CMaterials.IGNATIUS, CMaterials.SHADOW_IRON, CMaterials.LEMURITE, CMaterials.MIDASIUM, CMaterials.VYROXERES, CMaterials.CERUCLASE, CMaterials.ALDUORITE, CMaterials.KALENDRITE, CMaterials.VULCANITE, CMaterials.SANGUINITE}, ClayiumShape.ingot, new int[] {0, 7, 7, 8, 8, 8, 8, 9, 10, 11}, 200);


            for (CMaterial material : new CMaterial[] {CMaterials.EXIMITE, CMaterials.MEUTOITE}) {
                register2to1Recipe(CAInjector, ClayiumMaterials.getOD(CMaterials.PROTACTINIUM, ClayiumShape.ingot), ClayiumMaterials.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumMaterials.getODExist(material, ClayiumShape.ingot), 60L);
                if (CMaterials.existOD(material, ClayiumShape.ingot))
                    break;
            }
            registerODChains(transformer, new CMaterial[] {CMaterials.EXIMITE, CMaterials.MEUTOITE}, ClayiumShape.ingot, new int[] {0, 9}, 200);


            for (CShape shape1 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                for (CShape shape2 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.BRONZE, shape1), ClayiumMaterials.getOD(CMaterials.GOLD, shape2), 6, ClayiumMaterials.getODExist(CMaterials.HEPATIZON, ClayiumShape.ingot, 2), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.BRONZE, shape1), ClayiumMaterials.getOD(CMaterials.IRON, shape2), 6, ClayiumMaterials.getODExist(CMaterials.DAMASCUS_STEEL, ClayiumShape.ingot, 2), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.IRON, shape1), ClayiumMaterials.getOD(CMaterials.GOLD, shape2), 6, ClayiumMaterials.getODExist(CMaterials.ANGMALLEN, ClayiumShape.ingot, 2), 100L);

                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.DEEP_IRON, shape1), ClayiumMaterials.getOD(CMaterials.INFUSCOLIUM, shape2), 6, ClayiumMaterials.getODExist(CMaterials.BLACK_STEEL, ClayiumShape.ingot, 2), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.MITHRIL, shape1), ClayiumMaterials.getOD(CMaterials.SILVER, shape2), 6, ClayiumMaterials.getODExist(CMaterials.QUICKSILVER, ClayiumShape.ingot, 2), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.MITHRIL, shape1), ClayiumMaterials.getOD(CMaterials.RUBRACIUM, shape2), 6, ClayiumMaterials.getODExist(CMaterials.HADEROTH, ClayiumShape.ingot, 2), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.ORICHALCUM, shape1), ClayiumMaterials.getOD(ClayiumMaterial.platinum, shape2), 6, ClayiumMaterials.getODExist(CMaterials.CELENEGIL, ClayiumShape.ingot, 2), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.ADAMANTINE, shape1), ClayiumMaterials.getOD(CMaterials.ATLARUS, shape2), 6, ClayiumMaterials.getODExist(CMaterials.TARTARITE, ClayiumShape.ingot, 2), 100L);

                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.SHADOW_IRON, shape1), ClayiumMaterials.getOD(CMaterials.LEMURITE, shape2), 6, ClayiumMaterials.getODExist(CMaterials.SHADOW_STEEL, ClayiumShape.ingot, 2), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.ALDUORITE, shape1), ClayiumMaterials.getOD(CMaterials.CERUCLASE, shape2), 6, ClayiumMaterials.getODExist(CMaterials.INOLASHITE, ClayiumShape.ingot, 2), 100L);
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.KALENDRITE, shape1), ClayiumMaterials.getOD(ClayiumMaterial.platinum, shape2), 6, ClayiumMaterials.getODExist(CMaterials.AMORDRINE, ClayiumShape.ingot, 2), 100L);

                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.EXIMITE, shape1), ClayiumMaterials.getOD(CMaterials.MEUTOITE, shape2), 6, ClayiumMaterials.getODExist(CMaterials.DESICHALKOS, ClayiumShape.ingot, 2), 100L);
                }
            }
        }


        if (ClayiumCore.IntegrationID.SS2.enabled()) {
            for (CShape shape1 : new CShape[] {ClayiumShape.ingot, ClayiumShape.dust}) {
                for (CShape shape2 : new CShape[] {ClayiumShape.gem, ClayiumShape.dust}) {
                    register2to1Recipe(alloySmelter, ClayiumMaterials.getOD(CMaterials.DIAMOND, shape2), ClayiumMaterials.getOD(CMaterials.MITHRIL, shape1), 6, ClayiumMaterials.getODExist(CMaterials.NINJA, ClayiumShape.ingot), 100L);
                }
                register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.DIAMOND, ClayiumShape.gem), ClayiumMaterials.getOD(ClayiumMaterial.platinum, shape1), 11, ClayiumMaterials.getODExist(CMaterials.ORICHALCUM, ClayiumShape.gem), 1000000000000000L);
            }
            register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.REDSTONE, ClayiumShape.dust), "dyeYellow", 9, ClayiumMaterials.getODExist(CMaterials.YELLOWSTONE, ClayiumShape.dust), 10000000000000L);
            register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.REDSTONE, ClayiumShape.dust), "dyeBlue", 9, ClayiumMaterials.getODExist(CMaterials.BLUESTONE, ClayiumShape.dust), 10000000000000L);
            register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.REDSTONE, ClayiumShape.dust), ClayiumMaterials.getOD(CMaterials.YELLOWSTONE, ClayiumShape.dust), 8, ClayiumMaterials.getODExist(CMaterials.YELLOWSTONE, ClayiumShape.ingot), 1000000000L);
            register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.REDSTONE, ClayiumShape.dust), ClayiumMaterials.getOD(CMaterials.BLUESTONE, ClayiumShape.dust), 8, ClayiumMaterials.getODExist(CMaterials.BLUESTONE, ClayiumShape.ingot), 1000000000L);
        }


        if (ClayiumCore.IntegrationID.MAPLE.enabled()) {
            register1to1Recipe(grinder, "oreMapleDiamond", 5, ClayiumMaterials.getODExist("mapleDiamond", 2), 80L);
            register1to1Recipe(grinder, "oreDemantoidGarnet", 5, ClayiumMaterials.getODExist("demantoidGarnet", 2), 80L);
            register1to1Recipe(grinder, "oreMarble", 5, ClayiumMaterials.getODExist("marble", 2), 80L);

            register2to1Recipe(reactor, ClayiumMaterials.getOD(CMaterials.DIAMOND, ClayiumShape.gem), i(Items.apple), 10, ClayiumMaterials.getODExist("mapleDiamond"), 100000000000000L);
            register1to1Recipe(transformer, "mapleDiamond", 10, ClayiumMaterials.getODExist("demantoidGarnet"), 200L);
        }


        if (ClayiumCore.IntegrationID.TOFU.enabled()) {
            register2to1Recipe(reactor, "soybeans", "nigari", 9, ClayiumMaterials.getODExist("tofuKinu", 4), 10000000000000L);
            register2to1Recipe(reactor, "tofuKinu", "plankWood", 7, ClayiumMaterials.getODExist("tofuMomen"), 10000L);
            register2to1Recipe(reactor, "tofuMomen", "cobblestone", 8, ClayiumMaterials.getODExist("tofuIshi"), 1000000000L);
            register2to1Recipe(reactor, "tofuIshi", ClayiumMaterials.getOD(CMaterials.IRON, ClayiumShape.ingot), 9, ClayiumMaterials.getODExist("tofuMetal"), 10000000000000L);
            register2to1Recipe(reactor, "tofuMetal", ClayiumMaterials.getOD(CMaterials.DIAMOND, ClayiumShape.gem), 11, ClayiumMaterials.getODExist("tofuDiamond"), 10000000000000000L);
            register2to1Recipe(reactor, "tofuMomen", ClayiumMaterials.getOD(CMaterials.DIAMOND, ClayiumShape.gem), 9, ClayiumMaterials.getODExist("tofuGem"), 10000000000000L);
        }
        */}

}
