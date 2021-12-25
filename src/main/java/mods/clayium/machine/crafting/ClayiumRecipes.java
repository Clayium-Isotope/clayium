package mods.clayium.machine.crafting;

import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.common.MaterialShape.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Arrays;
import java.util.List;

public class ClayiumRecipes {
    public static final ClayiumRecipe clayWorkTable = new ClayiumRecipe() {{
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball)), ItemStack.EMPTY), 0, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.stick)), ItemStack.EMPTY), 4);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largeBall)), ItemStack.EMPTY), 1, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), ItemStack.EMPTY), 30);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largeBall)), I(ClayiumItems.clayRollingPin)), 2, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball), 2)), 4);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largeBall)), ItemStack.EMPTY), 0, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), ItemStack.EMPTY), 4);

        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate)), ItemStack.EMPTY), 1, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.blade)), ItemStack.EMPTY), 10);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate)), I(ClayiumItems.clayRollingPin)), 2, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.blade)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball), 2)), 1);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate)), I(ClayiumItems.claySlicer)), 5, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.stick), 4), ItemStack.EMPTY), 3);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate)), I(ClayiumItems.claySpatula)), 5, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.stick), 4), ItemStack.EMPTY), 3);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate), 6), I(ClayiumItems.clayRollingPin)), 3, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largePlate)), ItemStack.EMPTY), 10);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate), 3), ItemStack.EMPTY), 0, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largeBall)), ItemStack.EMPTY), 40);

        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), I(ClayiumItems.claySlicer)), 3, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball), 2)), 4);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), I(ClayiumItems.claySpatula)), 3, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball), 2)), 4);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), I(ClayiumItems.claySpatula)), 4, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ring)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallRing))), 2);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), ItemStack.EMPTY), 2, L(I(ClayiumItems.rawClaySlicer), ItemStack.EMPTY), 15);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), I(ClayiumItems.clayRollingPin)), 3, L(I(ClayiumItems.rawClaySlicer), ItemStack.EMPTY), 2);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallDisc)), I(ClayiumItems.claySpatula)), 4, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallRing)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.shortStick))), 1);

        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), ItemStack.EMPTY), 0, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.needle)), ItemStack.EMPTY), 3);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), I(ClayiumItems.claySlicer)), 0, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallDisc), 8), ItemStack.EMPTY), 7);
        addRecipe(L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), I(ClayiumItems.claySpatula)), 0, L(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallDisc), 8), ItemStack.EMPTY), 7);
    }};
    public static final ClayiumRecipe bendingMachine = new ClayiumRecipe() {{
        addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallDisc)), ClayiumCore.divideByProgressionRate(3));
        addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largeBall)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), ClayiumCore.divideByProgressionRate(2));
        addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), I(ClayiumItems.rawClaySlicer), ClayiumCore.divideByProgressionRate(1));
        addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.block)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate)), ClayiumCore.divideByProgressionRate(1));
        addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate), 4), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largePlate)), ClayiumCore.divideByProgressionRate(4));
        addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.blade), 2), ClayiumCore.divideByProgressionRate(4));

        addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.block)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.plate)), ClayiumCore.divideByProgressionRate(4));
        addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.plate), 4), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.largePlate)), ClayiumCore.divideByProgressionRate(8));
        addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.blade), 2), ClayiumCore.divideByProgressionRate(8));

        addRecipe(I(ClayiumItems.get(ClayMaterial.indClay, ClayShape.block)), 2, I(ClayiumItems.get(ClayMaterial.indClay, ClayShape.plate)), 2L, ClayiumCore.divideByProgressionRate(4));
        addRecipe(I(ClayiumItems.get(ClayMaterial.indClay, ClayShape.plate), 4), 2, I(ClayiumItems.get(ClayMaterial.indClay, ClayShape.largePlate)), 2L, ClayiumCore.divideByProgressionRate(8));
        addRecipe(I(ClayiumItems.get(ClayMaterial.advIndClay, ClayShape.block)), 2, I(ClayiumItems.get(ClayMaterial.advIndClay, ClayShape.plate)), 4L, ClayiumCore.divideByProgressionRate(4));
        addRecipe(I(ClayiumItems.get(ClayMaterial.advIndClay, ClayShape.plate), 4), 2, I(ClayiumItems.get(ClayMaterial.advIndClay, ClayShape.largePlate)), 4L, ClayiumCore.divideByProgressionRate(8));

//        for (Material material : GenericMaterial.values()) {
//            addRecipe(I(ClayiumItems.get(material, GenericShape.ingot)), 4, I(ClayiumItems.get(material, GenericShape.plate)), E(4), (int) (20.0F * material.getHardness()));
//            addRecipe(I(ClayiumItems.get(material, GenericShape.plate), 4), 4, I(ClayiumItems.get(material, GenericShape.largePlate)), E(4), (int) (40.0F * material.getHardness()));
//        }
//
//        Object[][] map = {
//                { GenericMaterial.antimatter, GenericShape.gem },
//                { GenericMaterial.pureAntimatter, GenericShape.gem },
//                { GenericMaterial.octupleClay, GenericShape.block },
//                { GenericMaterial.octuplePureAntimatter, GenericShape.gem }
//        };
//        for (int i = 0; i < map.length; i++) {
//            addRecipe(I(ClayiumItems.get((Material) map[i][0], (IShape) map[i][1])), 9, I(ClayiumItems.get((Material) map[i][0], GenericShape.plate)), E(9 + i), (int) (20.0F * ((Material) map[i][0]).getHardness()));
//            addRecipe(I(ClayiumItems.get((Material) map[i][0], GenericShape.plate)), 9, I(ClayiumItems.get((Material) map[i][0], GenericShape.largePlate)), E(9 + i), (int) (40.0F * ((Material) map[i][0]).getHardness()));
//        }

        // TODO ALUMINIUM_OD, IMPURE_ALUMINIUM
    }};
    public static final  ClayiumRecipe wireDrawingMachine = new ClayiumRecipe() {{
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.stick)), ClayiumCore.divideByProgressionRate(1));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.stick), 8), ClayiumCore.divideByProgressionRate(3));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.pipe)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.stick), 4), ClayiumCore.divideByProgressionRate(2));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallDisc)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.stick)), ClayiumCore.divideByProgressionRate(1));

            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.stick), 8), ClayiumCore.divideByProgressionRate(6));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.pipe)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.stick), 4), ClayiumCore.divideByProgressionRate(4));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.smallDisc)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.stick)), ClayiumCore.divideByProgressionRate(2));
    }};
    public static final ClayiumRecipe pipeDrawingMachine = new ClayiumRecipe() {{
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.pipe), 2), ClayiumCore.divideByProgressionRate(3));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.pipe), 2), ClayiumCore.divideByProgressionRate(6));
    }};
    public static final ClayiumRecipe cuttingMachine = new ClayiumRecipe() {{
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largeBall)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), ClayiumCore.divideByProgressionRate(2));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallDisc), 8), ClayiumCore.divideByProgressionRate(2));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largePlate)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc), 2), ClayiumCore.divideByProgressionRate(3));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.plate)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallDisc), 4), ClayiumCore.divideByProgressionRate(3));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.stick)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.shortStick), 2), ClayiumCore.divideByProgressionRate(1));

            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.smallDisc), 8), ClayiumCore.divideByProgressionRate(4));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.largePlate)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.disc), 2), ClayiumCore.divideByProgressionRate(6));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.plate)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.smallDisc), 4), ClayiumCore.divideByProgressionRate(6));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.stick)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.shortStick), 2), ClayiumCore.divideByProgressionRate(2));
    }};
    public static final ClayiumRecipe lathe = new ClayiumRecipe() {{
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.shortStick)), ClayiumCore.divideByProgressionRate(1));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.largeBall)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), ClayiumCore.divideByProgressionRate(4));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.needle)), ClayiumCore.divideByProgressionRate(3));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.needle)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.stick), 6), ClayiumCore.divideByProgressionRate(3));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.disc)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ring)), ClayiumCore.divideByProgressionRate(2));
            addRecipe(I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallDisc)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.smallRing)), ClayiumCore.divideByProgressionRate(1));

            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.block), 2), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.cylinder)), ClayiumCore.divideByProgressionRate(4));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.cylinder)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.needle)), ClayiumCore.divideByProgressionRate(6));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.needle)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.stick), 6), ClayiumCore.divideByProgressionRate(6));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.disc)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.ring)), ClayiumCore.divideByProgressionRate(4));
            addRecipe(I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.smallDisc)), I(ClayiumItems.get(ClayMaterial.denseClay, ClayShape.smallRing)), ClayiumCore.divideByProgressionRate(2));
    }};
    /*
    // TODO
    public static ClayiumRecipe millingMachine = new ClayiumRecipe() {{}};
    public static ClayiumRecipe condenser = new ClayiumRecipe() {{
            addRecipe(I(ClayiumBlocks.compressedClays.get(0), 9), I(ClayiumBlocks.compressedClays.get(1)), 1L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(1), 9), I(ClayiumBlocks.compressedClays.get(2)), 1L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(2), 9), I(ClayiumBlocks.compressedClays.get(3)), 10L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(3), 9), I(ClayiumBlocks.compressedClays.get(4)), 100L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(4), 9), 4, I(ClayiumBlocks.compressedClays.get(5)), 100L, 16L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(5), 9), 4, I(ClayiumBlocks.compressedClays.get(6)), 1000L, 16L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(6), 9), 4, I(ClayiumBlocks.compressedClays.get(7)), 10000L, 13L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(7), 9), 5, I(ClayiumBlocks.compressedClays.get(8)), 100000L, 10L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(8), 9), 5, I(ClayiumBlocks.compressedClays.get(9)), 1000000L, 8L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(9), 9), 5, I(ClayiumBlocks.compressedClays.get(10)), 10000000L, 6L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(10), 9), 5, I(ClayiumBlocks.compressedClays.get(11)), 100000000L, 4L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(11), 9), 5, I(ClayiumBlocks.compressedClays.get(12)), 1000000000L, 3L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(12), 9), 5, I(ClayiumBlocks.compressedClays.get(13)), 1000000000L, 25L);
    }};
    // TODO
    public static ClayiumRecipe grinder = new ClayiumRecipe() {{}};
    public static ClayiumRecipe decomposer = new ClayiumRecipe() {{
            addRecipe(I(ClayiumBlocks.compressedClays.get(1)), I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball), 4), 1L, 3L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(2)), I(ClayiumBlocks.compressedClays.get(1), 9), 1L, 3L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(3)), I(ClayiumBlocks.compressedClays.get(2), 9), 1L, 3L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(4)), I(ClayiumBlocks.compressedClays.get(3), 9), 1L, 10L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(5)), I(ClayiumBlocks.compressedClays.get(4), 9), 1L, 20L);
    }};
    // TODO
    public static ClayiumRecipe assembler = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe inscriber = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe centrifuge = new ClayiumRecipe() {{}};
    public static ClayiumRecipe energeticClayCondenser = new ClayiumRecipe() {{
            addRecipe(I(ClayiumBlocks.compressedClays.get(5), 9), I(ClayiumBlocks.compressedClays.get(6)), 1L, 16L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(6), 9), I(ClayiumBlocks.compressedClays.get(7)), 10L, 32L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(7), 9), I(ClayiumBlocks.compressedClays.get(8)), 100L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(8), 9), I(ClayiumBlocks.compressedClays.get(9)), 1000L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(9), 9), I(ClayiumBlocks.compressedClays.get(10)), 10000L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(10), 9), I(ClayiumBlocks.compressedClays.get(11)), 100000L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(11), 9), I(ClayiumBlocks.compressedClays.get(12)), 1000000L, 64L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(12), 9), I(ClayiumBlocks.compressedClays.get(13)), 10000000L, 64L);
    }};
    // TODO
    public static ClayiumRecipe chemicalReactor = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe alloySmelter = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe blastFurnace = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe electrolysisReactor = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe reactor = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe transformer = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe CACondenser = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe CAInjector = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe CAReactor = new ClayiumRecipe() {{}};
    // TODO
    public static ClayiumRecipe smelter = new ClayiumRecipe() {{}};
    public static ClayiumRecipe energeticClayDecomposer = new ClayiumRecipe() {{
            addRecipe(I(ClayiumBlocks.compressedClays.get(0)), 13, I(ClayiumItems.get(ClayMaterial.clay, ClayShape.ball), 4), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(1)), 13, I(ClayiumBlocks.compressedClays.get(0), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(2)), 13, I(ClayiumBlocks.compressedClays.get(1), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(3)), 13, I(ClayiumBlocks.compressedClays.get(2), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(4)), 13, I(ClayiumBlocks.compressedClays.get(3), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(5)), 13, I(ClayiumBlocks.compressedClays.get(4), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(6)), 13, I(ClayiumBlocks.compressedClays.get(5), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(7)), 13, I(ClayiumBlocks.compressedClays.get(6), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(8)), 13, I(ClayiumBlocks.compressedClays.get(7), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(9)), 13, I(ClayiumBlocks.compressedClays.get(8), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(10)), 13, I(ClayiumBlocks.compressedClays.get(9), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(11)), 13, I(ClayiumBlocks.compressedClays.get(10), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(12)), 13, I(ClayiumBlocks.compressedClays.get(11), 9), 1L, 0L);
            addRecipe(I(ClayiumBlocks.compressedClays.get(13)), 13, I(ClayiumBlocks.compressedClays.get(12), 9), 1L, 0L);
    }};
    // TODO
    public static ClayiumRecipe fluidTransferMachine = new ClayiumRecipe() {{}};
*/
    private static <T> List<T> L(T... elements) {
        return Arrays.asList(elements);
    }
    private static ItemStack I(Block block) {
        return I(new ItemBlock(block));
    }
    private static ItemStack I(Block block, int amount) {
        return I(new ItemBlock(block), amount);
    }
    private static ItemStack I(Item item) {
        return new ItemStack(item);
    }
    private static ItemStack I(Item itemIn, int amount) {
        return new ItemStack(itemIn, amount);
    }

    private static long E(int tier) {
        return E(1.0D, tier);
    }
    private static long E(double factor, int tier) {
        return (long) (factor * 100.0D * Math.pow(10.0D, (tier - 4)));
    }

    public static boolean hasResult(ClayiumRecipe recipes, List<ItemStack> stack) {
        for (RecipeElement recipe : recipes)
            if (recipe.getCondition().match(stack)) return true;

        return false;
    }

    public static boolean hasResult(ClayiumRecipe recipes, ItemStack... stacks) {
        return hasResult(recipes, L(stacks));
    }

    public static RecipeElement getRecipeElement(ClayiumRecipe recipes, NonNullList<ItemStack> referStacks, int method, int tier) {
        for (RecipeElement recipe : recipes) {
            if (recipe.getCondition().match(referStacks, method, tier)) {
                return recipe;
            }
        }

        return RecipeElement.FLAT;
    }
}
