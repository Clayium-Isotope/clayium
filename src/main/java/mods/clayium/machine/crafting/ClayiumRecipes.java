package mods.clayium.machine.crafting;

import mods.clayium.block.ClayiumBlocks;
import mods.clayium.block.itemblock.ItemBlockTiered;
import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.ClayiumItems;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.item.common.ClayiumShape;
import mods.clayium.machine.EnumMachineKind;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.List;

public class ClayiumRecipes {
    public static final ClayiumRecipe clayWorkTable = new ClayiumRecipe() {{
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball), ItemStack.EMPTY), 0, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick), ItemStack.EMPTY), 4);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ItemStack.EMPTY), 1, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), ItemStack.EMPTY), 30);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largeBall), W(ClayiumItems.rollingPin)), 2, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball, 2)), 4);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ItemStack.EMPTY), 0, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ItemStack.EMPTY), 4);

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate), ItemStack.EMPTY), 1, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.blade), ItemStack.EMPTY), 10);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate), W(ClayiumItems.rollingPin)), 2, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.blade), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball, 2)), 1);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate), W(ClayiumItems.slicer)), 5, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick, 4), ItemStack.EMPTY), 3);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate), W(ClayiumItems.spatula)), 5, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick, 4), ItemStack.EMPTY), 3);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate, 6), W(ClayiumItems.rollingPin)), 3, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ItemStack.EMPTY), 10);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate, 3), ItemStack.EMPTY), 0, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ItemStack.EMPTY), 40);

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), W(ClayiumItems.slicer)), 3, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball, 2)), 4);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), W(ClayiumItems.spatula)), 3, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball, 2)), 4);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), W(ClayiumItems.spatula)), 4, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ring), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallRing)), 2);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), ItemStack.EMPTY), 2, L(I(ClayiumItems.rawSlicer), ItemStack.EMPTY), 15);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), W(ClayiumItems.rollingPin)), 3, L(I(ClayiumItems.rawSlicer), ItemStack.EMPTY), 2);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallDisc), W(ClayiumItems.spatula)), 4, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallRing), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.shortStick)), 1);

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ItemStack.EMPTY), 0, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.needle), ItemStack.EMPTY), 3);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), I(ClayiumItems.slicer, 1, OreDictionary.WILDCARD_VALUE)), 0, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallDisc, 8), ItemStack.EMPTY), 7);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), I(ClayiumItems.spatula, 1, OreDictionary.WILDCARD_VALUE)), 0, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallDisc, 8), ItemStack.EMPTY), 7);
    }};
    public static final ClayiumRecipe bendingMachine = new ClayiumRecipe() {{
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallDisc), ClayiumCore.divideByProgressionRate(3));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), ClayiumCore.divideByProgressionRate(2));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), I(ClayiumItems.rawSlicer), ClayiumCore.divideByProgressionRate(1));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.block), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate), ClayiumCore.divideByProgressionRate(1));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate, 4), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumCore.divideByProgressionRate(4));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.blade, 2), ClayiumCore.divideByProgressionRate(4));

        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.block), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.plate), ClayiumCore.divideByProgressionRate(4));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.plate, 4), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumCore.divideByProgressionRate(8));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.blade, 2), ClayiumCore.divideByProgressionRate(8));

        addRecipe(ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.block), 2, ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.plate), 2L, ClayiumCore.divideByProgressionRate(4));
        addRecipe(ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.plate, 4), 2, ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.largePlate), 2L, ClayiumCore.divideByProgressionRate(8));
        addRecipe(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.block), 2, ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.plate), 4L, ClayiumCore.divideByProgressionRate(4));
        addRecipe(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.plate, 4), 2, ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.largePlate), 4L, ClayiumCore.divideByProgressionRate(8));

        for (ClayiumMaterial material : new ClayiumMaterial[] { ClayiumMaterial.impureSilicon, ClayiumMaterial.silicon, ClayiumMaterial.silicone, ClayiumMaterial.aluminum, ClayiumMaterial.claySteel, ClayiumMaterial.clayium, ClayiumMaterial.ultimateAlloy, ClayiumMaterial.AZ91DAlloy, ClayiumMaterial.ZK60AAlloy }) {
            addRecipe(ClayiumItems.get(material, ClayiumShape.ingot), 4, ClayiumItems.get(material, ClayiumShape.plate), E(4), (int) (20.0F * material.getHardness()));
            addRecipe(ClayiumItems.get(material, ClayiumShape.plate, 4), 4, ClayiumItems.get(material, ClayiumShape.largePlate), E(4), (int) (40.0F * material.getHardness()));
        }

        addRecipe(ClayiumItems.getOD(ClayiumMaterial.aluminum, ClayiumShape.ingot), 4, ClayiumItems.get(ClayiumMaterial.aluminum, ClayiumShape.plate), E(4), (int) (20.0F * ClayiumMaterial.aluminum.getHardness()));
        addRecipe(ClayiumItems.getOD(ClayiumMaterial.aluminum, ClayiumShape.plate, 4), 4, ClayiumItems.get(ClayiumMaterial.aluminum, ClayiumShape.largePlate), E(4), (int) (40.0F * ClayiumMaterial.aluminum.getHardness()));

        if (ClayiumConfiguration.cfgHardcoreAluminium) {
            addRecipe(ClayiumItems.get(ClayiumMaterial.impureAluminum, ClayiumShape.ingot), 4, ClayiumItems.get(ClayiumMaterial.impureAluminum, ClayiumShape.plate), E(4), (int) (20.0F * ClayiumMaterial.impureAluminum.getHardness()));
            addRecipe(ClayiumItems.get(ClayiumMaterial.impureAluminum, ClayiumShape.plate, 4), 4, ClayiumItems.get(ClayiumMaterial.impureAluminum, ClayiumShape.largePlate), E(4), (int) (40.0F * ClayiumMaterial.impureAluminum.getHardness()));
        }

        // TODO external metals

        Object[][] map = {
                { ClayiumMaterial.antimatter, ClayiumShape.gem },
                { ClayiumMaterial.pureAntimatter, ClayiumShape.gem },
                { ClayiumMaterial.octupleEnergeticClay, ClayiumShape.block },
                { ClayiumMaterial.octuplePureAntimatter, ClayiumShape.gem }
        };
        for (int i = 0; i < map.length; i++) {
            addRecipe(ClayiumItems.get((ClayiumMaterial) map[i][0], (ClayiumShape) map[i][1]), 9, ClayiumItems.get((ClayiumMaterial) map[i][0], ClayiumShape.plate), E(9 + i), (int) (20.0F * ((ClayiumMaterial) map[i][0]).getHardness()));
            addRecipe(ClayiumItems.get((ClayiumMaterial) map[i][0], ClayiumShape.plate), 9, ClayiumItems.get((ClayiumMaterial) map[i][0], ClayiumShape.largePlate), E(9 + i), (int) (40.0F * ((ClayiumMaterial) map[i][0]).getHardness()));
        }
    }};
    public static final ClayiumRecipe wireDrawingMachine = new ClayiumRecipe() {{
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick), ClayiumCore.divideByProgressionRate(1));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick, 8), ClayiumCore.divideByProgressionRate(3));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.pipe), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick, 4), ClayiumCore.divideByProgressionRate(2));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallDisc), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick), ClayiumCore.divideByProgressionRate(1));

        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.stick, 8), ClayiumCore.divideByProgressionRate(6));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.pipe), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.stick, 4), ClayiumCore.divideByProgressionRate(4));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.smallDisc), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.stick), ClayiumCore.divideByProgressionRate(2));
    }};
    public static final ClayiumRecipe pipeDrawingMachine = new ClayiumRecipe() {{
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.pipe, 2), ClayiumCore.divideByProgressionRate(3));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.pipe, 2), ClayiumCore.divideByProgressionRate(6));
    }};
    public static final ClayiumRecipe cuttingMachine = new ClayiumRecipe() {{
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), ClayiumCore.divideByProgressionRate(2));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallDisc, 8), ClayiumCore.divideByProgressionRate(2));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc, 2), ClayiumCore.divideByProgressionRate(3));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallDisc, 4), ClayiumCore.divideByProgressionRate(3));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.shortStick, 2), ClayiumCore.divideByProgressionRate(1));

        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.smallDisc, 8), ClayiumCore.divideByProgressionRate(4));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.disc, 2), ClayiumCore.divideByProgressionRate(6));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.plate), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.smallDisc, 4), ClayiumCore.divideByProgressionRate(6));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.stick), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.shortStick, 2), ClayiumCore.divideByProgressionRate(2));
    }};
    public static final ClayiumRecipe lathe = new ClayiumRecipe() {{
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.shortStic)), ClayiumCore.divideByProgressionRate(1));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largeBall), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumCore.divideByProgressionRate(4));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.needle), ClayiumCore.divideByProgressionRate(3));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.needle), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick, 6), ClayiumCore.divideByProgressionRate(3));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.disc), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ring), ClayiumCore.divideByProgressionRate(2));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallDisc), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.smallRing), ClayiumCore.divideByProgressionRate(1));

        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.block, 2), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumCore.divideByProgressionRate(4));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.needle), ClayiumCore.divideByProgressionRate(6));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.needle), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.stick, 6), ClayiumCore.divideByProgressionRate(6));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.disc), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.ring), ClayiumCore.divideByProgressionRate(4));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.smallDisc), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.smallRing), ClayiumCore.divideByProgressionRate(2));
    }};
    // TODO
    public static final ClayiumRecipe millingMachine = new ClayiumRecipe() {{
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.plate), I(ClayiumItems.clayCircuitBoard), 32L);
        addRecipe(ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.plate), I(ClayiumItems.clayCircuitBoard), 1L);
        addRecipe(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.plate), 3, I(ClayiumItems.CEEBoard), 2L, 32L);
    }};
    public static final ClayiumRecipe condenser = new ClayiumRecipe() {{
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

        for (int i = 0; i < 8; i++) {
            addRecipe(ClayiumItems.get(ClayiumMaterial.compressedPureAntimatter.get(i), ClayiumShape.gem, 9), 10, ClayiumItems.get(ClayiumMaterial.compressedPureAntimatter.get(i + 1), ClayiumShape.gem), E(9), 6L);
        }
    }};
    // TODO
    public static final ClayiumRecipe grinder = new ClayiumRecipe() {{
        for (ClayiumMaterial material : new ClayiumMaterial[] {ClayiumMaterial.clay, ClayiumMaterial.denseClay}) {
            int j = (material == ClayiumMaterial.clayium) ? 1 : 4;
            addRecipe(ClayiumItems.get(material, ClayiumShape.plate), ClayiumItems.get(material, ClayiumShape.dust), 1L, 3 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.stick, 4), ClayiumItems.get(material, ClayiumShape.dust), 1L, 3 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.shortStick, 8), ClayiumItems.get(material, ClayiumShape.dust), 1L, 3 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.ring, 4), ClayiumItems.get(material, ClayiumShape.dust, 5), 1L, 15 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.smallRing, 8), ClayiumItems.get(material, ClayiumShape.dust), 1L, 3 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.gear, 8), ClayiumItems.get(material, ClayiumShape.dust, 9), 1L, 27 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.blade), ClayiumItems.get(material, ClayiumShape.dust), 1L, 3 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.needle), ClayiumItems.get(material, ClayiumShape.dust, 2), 1L, 6 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.disc, 2), ClayiumItems.get(material, ClayiumShape.dust, 3), 1L, 9 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.smallDisc, 4), ClayiumItems.get(material, ClayiumShape.dust), 1L, 3 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.cylinder), ClayiumItems.get(material, ClayiumShape.dust, 2), 1L, 6 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.pipe), ClayiumItems.get(material, ClayiumShape.dust), 1L, 3 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.largePlate), ClayiumItems.get(material, ClayiumShape.dust, 4), 1L, 12 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.grindingHead), ClayiumItems.get(material, ClayiumShape.dust, 16), 1L, 48 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.bearing, 4), ClayiumItems.get(material, ClayiumShape.dust, 5), 1L, 15 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.spindle), ClayiumItems.get(material, ClayiumShape.dust, 4), 1L, 12 * j);
            addRecipe(ClayiumItems.get(material, ClayiumShape.cuttingHead), ClayiumItems.get(material, ClayiumShape.dust, 9), 1L, 27 * j);
        }

        // TODO till add ClayTree: addRecipe(I(CBlocks.blockClayTreeLog), 5, ClayiumItems.get(ClayiumMaterial.organicClay, ClayiumShape.dust), E(5), 200L);
    }};
    public static final ClayiumRecipe decomposer = new ClayiumRecipe() {{
        addRecipe(I(ClayiumBlocks.compressedClays.get(1)), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball, 4), 1L, 3L);
        addRecipe(I(ClayiumBlocks.compressedClays.get(2)), I(ClayiumBlocks.compressedClays.get(1), 9), 1L, 3L);
        addRecipe(I(ClayiumBlocks.compressedClays.get(3)), I(ClayiumBlocks.compressedClays.get(2), 9), 1L, 3L);
        addRecipe(I(ClayiumBlocks.compressedClays.get(4)), I(ClayiumBlocks.compressedClays.get(3), 9), 1L, 10L);
        addRecipe(I(ClayiumBlocks.compressedClays.get(5)), I(ClayiumBlocks.compressedClays.get(4), 9), 1L, 20L);

        addRecipe(ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 3), 1L, ClayiumCore.divideByProgressionRate(60));
        addRecipe(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust), 4, ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 28), 1000L, ClayiumCore.divideByProgressionRate(60));
    }};
    // TODO
    public static final ClayiumRecipe assembler = new ClayiumRecipe() {{
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.stick, 5), 3, ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.gear), 10L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.shortStick, 9), 3, ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.gear), 10L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball, 8)), 3, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.spindle)), 10L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.block, 8)), 3, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.grindingHead)), 10L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.largePlate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate, 8)), 3, L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cuttingHead)), 10L, ClayiumCore.divideByProgressionRate(20));
        
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.stick, 5), 3, ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.gear), 10L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.shortStick, 9), 3, ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.gear), 10L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball, 8)), 3, L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.spindle)), 100L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.block, 8)), 3, L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.grindingHead)), 100L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.largePlate), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.plate, 8)), 3, L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cuttingHead)), 100L, ClayiumCore.divideByProgressionRate(20));

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.largePlate), I(ClayiumItems.circuits.get(6))), 4, L(I(ClayiumBlocks.AZ91DAlloyHull)), E(6), 120L);

        // ==========

        for (int tier = 1; tier <= 13; tier++) {
            if (tier >= 1) {
                if (tier <= 1) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cuttingHead)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.millingMachine, 1))), E(tier), 120L);
                }
                if (tier <= 3) {
                    addRecipe(L(ClayiumItems.get(tier, ClayiumShape.largePlate), I(ClayiumItems.circuits.get(3))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.cobblestoneGenerator, tier))), E(tier), 40L);
                }
                if (tier <= 4) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.plate, 3)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.bendingMachine, tier))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.pipe, 2)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.wireDrawingMachine, tier))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cylinder, 2)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.pipeDrawingMachine, tier))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.cuttingHead)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.cuttingMachine, tier))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.spindle)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.lathe, tier))), E(tier), 120L);
                }
            }

            if (tier >= 2) {
                if (tier <= 3) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(tier, ClayiumShape.largePlate)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.condenser, tier))), E(tier), 120L);
                }
                if (tier <= 4) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.gear, 4)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.decomposer, tier))), E(tier), 120L);
                }
                if (tier <= 6) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.grindingHead)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.grinder, tier))), E(tier), 120L);
                }
            }

            if (tier >= 3) {
                if (tier <= 3) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumItems.CEE, 2)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.ECCondenser, 3))), E(tier), 120L);
                }
                if (tier <= 4) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.cuttingHead)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.millingMachine, tier))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.gear, 4)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.assembler, tier))), E(tier), 40L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.assembler, tier)), I(ClayiumItems.circuits.get(4))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.inscriber, tier))), E(tier), 40L);
                }
                if (tier <= 6) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.spindle, Math.max(tier - 4, 1))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.centrifuge, tier))), E(tier), 120L);
                }
            }

            if (tier >= 4) {
                addRecipe(L(ClayiumItems.get(tier, ClayiumShape.plate), I(ClayiumItems.circuits.get(tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.buffer, tier), 16)), E(tier), 40L);
                addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.buffer, tier), 6), ClayiumItems.get(tier, ClayiumShape.largePlate)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.multitrackBuffer, tier))), E(tier), 40L);

                // if (ClayiumConfiguration.cfgEnableFluidCapsule)
                // TODO till add ClayCapsule: addRecipe(L(I(ClayiumBlocks.get(ClayiumBlocks.EnumClayiumTileEntity.buffer, tier)), I(CItems.itemsCapsule[0], 4)), 4, L(I(ClayiumBlocks.get(ClayiumBlocks.EnumClayiumTileEntity.fluidTranslator, tier))), E(tier), 40L);

                if (tier <= 4) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumItems.CEE, 2)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.ECCondenser, 4))), E(tier), 120L);
                }
                if (tier <= 7) {
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.buffer, tier)), I(ClayiumItems.circuits.get(3))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.cobblestoneGenerator, tier))), E(tier), 40L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.buffer, tier)), I(ClayiumItems.circuits.get(3))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.saltExtractor, tier))), E(tier), 40L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumItems.circuits.get(3), tier - 3)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.smelter, tier))), E(tier), 120L);
                    // TODO till add ClayShooter: addRecipe(L(I(ClayiumItems.get(ClayiumMaterial.AZ91DAlloy, GenericShape.plate), 4), I(ClayiumItems.circuits.get(tier))), 4, L(I(CItems.itemsClayShooter[tier - 4])), E(tier), 40L);
                }
                if (tier <= 5) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.buffer, tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.condenser, tier))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumItems.circuits.get(4), tier - 3)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.chemicalReactor, tier))), E(tier), 120L);
                }
            }

            if (tier >= 5) {
                addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.buffer, 6))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.clayInterface, tier))), E(tier), 40L);
                addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.clayInterface, tier)), ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 16)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.redstoneInterface, tier))), E(tier), 40L);

                if (tier <= 5) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.getOD(ClayiumMaterial.silicon, ClayiumShape.plate, 8)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.solarClayFabricator, 5))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.buffer, tier)), I(ClayiumItems.circuits.get(5))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.autoClayCondenser, 5))), E(tier), 40L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.assembler, 4)), I(ClayiumBlocks.machineHulls.get(tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.autoCrafter, tier))), E(tier), 40L);
                    if (ClayiumConfiguration.cfgEnableFluidCapsule)
                        addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.fluidTranslator, tier), 2), I(ClayiumBlocks.machineHulls.get(tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.fluidTransferMachine, tier))), E(tier), 40L);
                }
                if (tier <= 7) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.plate, (tier - 4) * 3)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.bendingMachine, tier))), E(tier), 120L);
                }
            }

            if (tier >= 6) {
                if (tier <= 6) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.getOD(ClayiumMaterial.silicon, ClayiumShape.plate, 16)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.solarClayFabricator, 6))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.smelter, tier)), I(ClayiumBlocks.get(EnumMachineKind.clayInterface, tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.blastFurnace, 6))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.chemicalReactor, 5)), I(ClayiumBlocks.get(EnumMachineKind.smelter, tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.chemicalMetalSeparator, 6))), E(tier), 40L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.gear, 4)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.assembler, tier))), E(tier), 40L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.assembler, 4)), I(ClayiumItems.circuits.get(tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.assembler, 6))), E(tier), 40L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.smelter, tier)), I(ClayiumItems.circuits.get(tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.alloySmelter, tier))), E(tier), 40L);
                }
                if (tier <= 9) {
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.chemicalReactor, 5)), I(ClayiumItems.circuits.get(tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.electrolysisReactor, tier))), E(tier), 40L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.autoCrafter, tier - 1)), I(ClayiumBlocks.machineHulls.get(tier))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.autoCrafter, tier))), E(tier), 40L);
                }
            }

            if (tier >= 7) {
                addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.buffer, tier)), I(ClayiumItems.laserParts)), 6, L(I(ClayiumBlocks.get(EnumMachineKind.laserInterface, tier))), E(tier), 120L);

                if (tier <= 7) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), ClayiumItems.getOD(ClayiumMaterial.silicon, ClayiumShape.plate, 16)), 4, L(I(ClayiumBlocks.get(EnumMachineKind.solarClayFabricator, 7))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.buffer, tier)), I(ClayiumItems.circuits.get(5))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.autoClayCondenser, 7))), E(tier), 40L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.laserInterface, tier))), 6, L(I(ClayiumBlocks.get(EnumMachineKind.reactor, 7))), E(tier), 1200L);
                }
                if (tier <= 9) {
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.buffer, tier)), I(ClayiumBlocks.machineHulls.get(tier))), 6, L(I(ClayiumBlocks.get(EnumMachineKind.distributor, tier))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.buffer, tier)), I(ClayiumItems.circuits.get(7))), 6, L(I(ClayiumBlocks.get(EnumMachineKind.distributor, tier))), E(tier), 120L);
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.reactor, 7)), I(ClayiumBlocks.get(EnumMachineKind.electrolysisReactor, tier))), 6, L(I(ClayiumBlocks.get(EnumMachineKind.transformer, tier))), E(tier), 120L);
                }
                if (tier <= 10) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumItems.laserParts, 4)), 6, L(I(ClayiumBlocks.get(EnumMachineKind.clayEnergyLaser, tier))), E(tier), 480L);
                }
            }

            if (tier >= 8) {
                if (tier <= 8) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.chemicalReactor, 5))), 4, L(I(ClayiumBlocks.get(EnumMachineKind.chemicalReactor, tier))), E(tier), 480L);
                }
                if (tier <= 9) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.smelter, tier - 1), 16)), 6, L(I(ClayiumBlocks.get(EnumMachineKind.smelter, tier))), E(tier), 2000L);
                }
            }

            if (tier >= 9) {
                if (tier <= 9) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.bendingMachine, 7), 4)), 6, L(I(ClayiumBlocks.get(EnumMachineKind.bendingMachine, tier))), E(tier), 480L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.transformer, tier), 16)), 6, L(I(ClayiumBlocks.get(EnumMachineKind.CACondenser, tier))), E(tier), 480L);
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.reactor, 7), 16)), 6, L(I(ClayiumBlocks.get(EnumMachineKind.CAInjector, tier))), E(tier), 480L);
                }
            }

            if (tier >= 10) {
                addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.reactor, 7), 16)), 10, L(I(ClayiumBlocks.get(EnumMachineKind.CAReactorCore, tier))), E(tier), 120L);
                addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.CAInjector, tier - 1), 2)), 10, L(I(ClayiumBlocks.get(EnumMachineKind.CAInjector, tier))), E(tier), 480L);

                if (tier == 10) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.assembler, 6))), 6, L(I(ClayiumBlocks.get(EnumMachineKind.assembler, tier))), E(tier), 40L);
                }
                if (tier <= 11) {
                    addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.get(EnumMachineKind.transformer, tier), 16)), 10, L(I(ClayiumBlocks.get(EnumMachineKind.CACondenser, tier))), E(tier), 480L);
                }
                if (tier <= 12) {
                    addRecipe(L(I(ClayiumBlocks.get(EnumMachineKind.CAInjector, tier)), I(ClayiumBlocks.get(EnumMachineKind.reactor, 7))), 10, L(I(ClayiumBlocks.get(EnumMachineKind.transformer, tier))), E(tier), 120L);
                }
            }

            if (tier == 13) {
                addRecipe(L(I(ClayiumBlocks.machineHulls.get(tier)), I(ClayiumBlocks.blockCAReactorCoil.get("opa"))), 10, L(I(ClayiumBlocks.get(EnumMachineKind.ECDecomposer, 13))), E(tier), 120L);
            }
        }

        addRecipe(L(W(ClayiumItems.rollingPin), W(ClayiumItems.slicer)), 6, L(I(ClayiumItems.IOTool)), E(6), 20L);
        addRecipe(L(W(ClayiumItems.spatula), I(ClayiumItems.wrench)), 6, L(I(ClayiumItems.pipingTool)), E(6), 20L);
        addRecipe(L(I(ClayiumItems.IOTool), I(ClayiumItems.circuits.get(6), 2)), 6, L(I(ClayiumItems.memoryCard)), E(6), 20L);

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.spindle), I(ClayiumItems.circuits.get(6), 2)), 6, L(I(CItems.itemDirectionMemory)), E(6), 20L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 3), I(ClayiumItems.synchronousParts, 2)), 6, L(I(CItems.itemSynchronizer)), E(6), 20L);

        addRecipe(L(I(ClayiumItems.circuits.get(5)), ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.plate, 3)), 4, L(I(CItems.itemFilterWhitelist)), 8L, 20L);
        addRecipe(L(I(ClayiumItems.circuits.get(6)), ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.plate, 3)), 4, L(I(CItems.itemFilterItemName)), 8L, 20L);
        addRecipe(L(I(ClayiumItems.circuits.get(7)), ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.plate, 3)), 4, L(I(CItems.itemFilterFuzzy)), 8L, 20L);

            /* TODO till add Gadgets
            addRecipe(L(I(Items.LEATHER, 4), ClayiumItems.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 8)), 4, L(I(CItems.itemGadgetHolder)), E(6), 120L);

            addRecipe(L(ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.plate, 8), ClayiumItems.get(ClayiumMaterial.AZ91DAlloy, ClayiumShape.plate, 4)), 4, L(I(CItems.itemGadget.get("Blank"))), E(6), 120L);

            addRecipe(L(I(CItems.itemGadget.get("Blank")), I(CBlocks.blockOverclocker.get("antimatter"))), 10, L(I(CItems.itemGadget.get("AntimatterOverclock"))), E(10), 120L);
            addRecipe(L(I(CItems.itemGadget.get("AntimatterOverclock")), I(CBlocks.blockOverclocker.get("pureantimatter"))), 10, L(I(CItems.itemGadget.get("PureAntimatterOverclock"))), E(11), 120L);
            addRecipe(L(I(CItems.itemGadget.get("PureAntimatterOverclock")), I(CBlocks.blockOverclocker.get("oec"))), 10, L(I(CItems.itemGadget.get("OECOverclock"))), E(12), 120L);
            addRecipe(L(I(CItems.itemGadget.get("OECOverclock")), I(CBlocks.blockOverclocker.get("opa"))), 10, L(I(CItems.itemGadget.get("OPAOverclock"))), E(13), 120L);

            addRecipe(L(I(CItems.itemGadget.get("Blank")), I(ClayiumItems.circuits.get(12), 16)), 10, L(I(CItems.itemGadget.get("Flight0"))), E(12), 120L);
            addRecipe(L(I(CItems.itemGadget.get("Flight0")), I(ClayiumItems.circuits.get(13), 16)), 10, L(I(CItems.itemGadget.get("Flight1"))), E(13), 1200L);
            addRecipe(L(I(CItems.itemGadget.get("Flight1")), I(CBlocks.blockOverclocker.get("opa", 16))), 10, L(I(CItems.itemGadget.get("Flight2"))), E(14), 12000L);

            addRecipe(L(I(CItems.itemGadget.get("Blank")), I(ClayiumItems.circuits.get(6), 4)), 4, L(I(CItems.itemGadget.get("Health0"))), E(6), 120L);
            addRecipe(L(I(CItems.itemGadget.get("Health0")), I(ClayiumItems.circuits.get(10), 4)), 4, L(I(CItems.itemGadget.get("Health1"))), E(10), 120L);
            addRecipe(L(I(CItems.itemGadget.get("Health1")), I(ClayiumItems.circuits.get(12), 4)), 10, L(I(CItems.itemGadget.get("Health2"))), E(12), 120L);

            addRecipe(L(I(CItems.itemGadget.get("Blank")), I(ClayiumItems.circuits.get(7), 2)), 4, L(I(CItems.itemGadget.get("AutoEat0"))), E(7), 120L);
             */

        addRecipe(L(I(ClayiumItems.CEECircuit), ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.plate, 3)), 0, 0, L(I(ClayiumItems.CEE)), 8L, 20L);

        addRecipe(L(I(ClayiumItems.precisionCircuit), ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 0, 6, L(I(ClayiumItems.integratedCircuit)), 10000L, 1200L);

        addRecipe(L(I(ClayiumItems.CEE), I(ClayiumItems.integratedCircuit)), 0, 6, L(I(ClayiumItems.laserParts)), E(6), 20L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.beryllium, ClayiumShape.dust, 8), I(ClayiumItems.integratedCircuit)), 0, 6, L(I(ClayiumItems.synchronousParts)), E(6), 432000L);

    }};
    // TODO
    public static final ClayiumRecipe inscriber = new ClayiumRecipe() {{
        addRecipe(L(I(ClayiumItems.CEEBoard), ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 0, 0, L(I(ClayiumItems.CEECircuit)), 2L, 20L);

        addRecipe(L(I(ClayiumItems.clayCircuitBoard), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.dust, 6)), 0, 0, L(I(ClayiumItems.clayCircuit)), 2L, 20L);
        addRecipe(L(I(ClayiumItems.clayCircuitBoard), ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 0, 0, L(I(ClayiumItems.basicCircuit)), 2L, 20L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.impureSilicon, ClayiumShape.plate), ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 0, 0, L(I(ClayiumItems.advancedCircuit)), 100L, 120L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.silicon, ClayiumShape.plate), ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 32)), 0, 0, L(I(ClayiumItems.precisionCircuit)), 1000L, 120L);

    }};
    // TODO
    public static final ClayiumRecipe centrifuge = new ClayiumRecipe() {{
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.dust, 9)), L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.dust)), 4L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.dust, 2)), L(ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.dust, 9), ClayiumItems.get(ClayiumMaterial.calClay, ClayiumShape.dust)), 4L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.dust, 2)), L(ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 12), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.dust, 8), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.dust, 8), ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 4L, ClayiumCore.divideByProgressionRate(20));
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust, 2)), 4, L(ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 64), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.dust, 64), ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.dust, 64), ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.dust, 12)), 10000L, ClayiumCore.divideByProgressionRate(12));

    }};
    public static final ClayiumRecipe energeticClayCondenser = new ClayiumRecipe() {{
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
    public static final ClayiumRecipe chemicalReactor = new ClayiumRecipe() {{
        addRecipe(L(ClayiumItems.getOD(ClayiumMaterial.salt, ClayiumShape.dust, 2), ClayiumItems.get(ClayiumMaterial.calClay, ClayiumShape.dust)), L(ClayiumItems.get(ClayiumMaterial.calciumChloride, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.sodiumCarbonate, ClayiumShape.dust)), E(5), 120L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.sodiumCarbonate, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.dust)), L(ClayiumItems.get(ClayiumMaterial.quartz, ClayiumShape.dust)), E(4), 120L);
        addRecipe(L(ClayiumItems.getOD(ClayiumMaterial.quartz, ClayiumShape.dust), I(Items.COAL)), L(ClayiumItems.get(ClayiumMaterial.impureSilicon, ClayiumShape.dust)), E(4), 120L);
        addRecipe(L(ClayiumItems.getOD(ClayiumMaterial.quartz, ClayiumShape.dust), new ItemStack(Items.COAL, 1, 1)), L(ClayiumItems.get(ClayiumMaterial.impureSilicon, ClayiumShape.dust)), E(4), 120L);

        addRecipe(L(ClayiumItems.getOD(ClayiumMaterial.salt, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 8, L(ClayiumItems.get(ClayiumMaterial.quartz, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.calciumChloride, ClayiumShape.dust)), E(10.0D, 8), 1L);
        addRecipe(L(ClayiumItems.getOD(ClayiumMaterial.quartz, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.dust)), 8, L(ClayiumItems.get(ClayiumMaterial.impureSilicon, ClayiumShape.ingot)), E(10.0D, 8), 1L);

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.denseClay, ClayiumShape.dust)), 5, L(ClayiumItems.get(ClayiumMaterial.impureSilicon, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.mainAluminum, ClayiumShape.dust)), E(5), 30L);
    }};
    // TODO
    public static final ClayiumRecipe alloySmelter = new ClayiumRecipe() {{}};
    // TODO
    public static final ClayiumRecipe blastFurnace = new ClayiumRecipe() {{
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.impureSilicon, ClayiumShape.dust)), 7, L(ClayiumItems.get(ClayiumMaterial.silicon, ClayiumShape.ingot)), E(7), 100L);

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.indClay, ClayiumShape.dust, 2), ClayiumItems.get(ClayiumMaterial.impureManganese, ClayiumShape.dust)), 6, L(ClayiumItems.get(ClayiumMaterial.claySteel, ClayiumShape.ingot, 2)), E(5.0D, 6), 200L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.impureManganese, ClayiumShape.dust)), 7, L(ClayiumItems.get(ClayiumMaterial.claySteel, ClayiumShape.ingot)), E(5.0D, 7), 5L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust), ClayiumItems.getOD(ClayiumMaterial.impureManganese, ClayiumShape.dust)), 8, L(ClayiumItems.get(ClayiumMaterial.claySteel, ClayiumShape.ingot)), E(5.0D, 8), 1L);
    }};
    // TODO
    public static final ClayiumRecipe electrolysisReactor = new ClayiumRecipe() {{}};
    // TODO
    public static final ClayiumRecipe reactor = new ClayiumRecipe() {{
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust, 8), ClayiumItems.getOD(ClayiumMaterial.lithium, ClayiumShape.dust, 4)), 0, 7, L(ClayiumItems.get(ClayiumMaterial.clayium, ClayiumShape.dust, 8)), E(10.0D, 7), 50000L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust, 8), ClayiumItems.getOD(ClayiumMaterial.hafnium, ClayiumShape.dust)), 0, 7, L(ClayiumItems.get(ClayiumMaterial.clayium, ClayiumShape.dust, 8)), E(10.0D, 7), 500000L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust, 8), ClayiumItems.getOD(ClayiumMaterial.barium, ClayiumShape.dust)), 0, 7, L(ClayiumItems.get(ClayiumMaterial.clayium, ClayiumShape.dust, 8)), E(3.0D, 7), 5000000L);
        addRecipe(L(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust, 8), ClayiumItems.getOD(ClayiumMaterial.strontium, ClayiumShape.dust)), 0, 7, L(ClayiumItems.get(ClayiumMaterial.clayium, ClayiumShape.dust, 8)), E(7), 50000000L);

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.impureUltimateAlloy, ClayiumShape.ingot)), 0, 8, L(ClayiumItems.get(ClayiumMaterial.ultimateAlloy, ClayiumShape.ingot)), E(10.0D, 8), 1000000000L);

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.engClay, ClayiumShape.dust, 8), ClayiumItems.getOD(ClayiumMaterial.lithium, ClayiumShape.dust)), 0, 7, L(ClayiumItems.get(ClayiumMaterial.excClay, ClayiumShape.dust, 4)), E(7), 2000000L);

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.orgClay, ClayiumShape.dust), ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust)), 10, L(ClayiumItems.get(ClayiumMaterial.orgClay, ClayiumShape.dust, 2)), E(10), 1000000000000L);
        addRecipe(L(I(ClayiumItems.claySoul), ClayiumItems.get(ClayiumMaterial.advClay, ClayiumShape.dust)), 11, L(ClayiumItems.get(ClayiumMaterial.orgClay, ClayiumShape.dust, 2)), E(11), 100000000000000L);

        addRecipe(ClayiumItems.get(ClayiumMaterial.clayium, ClayiumShape.ingot), 0, 9, I(ClayiumItems.antimatterSeed), E(9), ClayiumCore.divideByProgressionRate(200000000000000L));

        addRecipe(L(ClayiumItems.get(ClayiumMaterial.pureAntimatter, ClayiumShape.gem, 8), I(ClayiumItems.integratedCircuit)), 0, 11, L(I(ClayiumItems.teleportationParts)), E(11), 10000000000000L);

        addRecipe(L(I(ClayiumItems.integratedCircuit, 6), ClayiumItems.get(ClayiumMaterial.excClay, ClayiumShape.dust)), 0, 7, L(I(ClayiumItems.clayCore)), E(10.0D, 7), 8000000L);
        addRecipe(L(I(ClayiumItems.clayCore, 6), ClayiumItems.get(ClayiumMaterial.excClay, ClayiumShape.dust, 12)), 0, 8, L(I(ClayiumItems.clayBrain)), E(10.0D, 8), 4000000000L);
        addRecipe(L(I(ClayiumItems.clayBrain, 6), ClayiumItems.get(ClayiumMaterial.excClay, ClayiumShape.dust, 32)), 0, 9, L(I(ClayiumItems.claySpirit)), E(10.0D, 9), 10000000000000L);
        addRecipe(L(I(ClayiumItems.claySpirit, 6), ClayiumItems.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 4)), 0, 10, L(I(ClayiumItems.claySoul)), E(10.0D, 10), 10000000000000L);
        addRecipe(L(I(ClayiumItems.claySoul, 6), ClayiumItems.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 16)), 0, 11, L(I(ClayiumItems.clayAnima)), E(30.0D, 11), 100000000000000L);
        addRecipe(L(I(ClayiumItems.clayAnima, 6), ClayiumItems.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 64)), 0, 12, L(I(ClayiumItems.clayPsyche)), E(90.0D, 12), 1000000000000000L);
    }};
    // TODO
    public static final ClayiumRecipe transformer = new ClayiumRecipe() {{}};
    // TODO
    public static final ClayiumRecipe CACondenser = new ClayiumRecipe() {{
        addRecipe(I(ClayiumItems.antimatterSeed), ClayiumItems.get(ClayiumMaterial.antimatter, ClayiumShape.gem), E(2.5D, 9), ClayiumCore.divideByProgressionRate(2000L));
    }};
    // TODO
    public static final ClayiumRecipe CAInjector = new ClayiumRecipe() {{
        addRecipe(L(I(ClayiumBlocks.machineHulls.get(10)), ClayiumItems.get(ClayiumMaterial.antimatter, ClayiumShape.gem, 8)), 10, L(I(CBlocks.blockCACollector)), E(2.0D, 10), 4000L);
    }};
    // TODO
    public static final ClayiumRecipe CAReactor = new ClayiumRecipe() {{
        addRecipe(ClayiumItems.get(ClayiumMaterial.antimatter, ClayiumShape.gem), 10, ClayiumItems.get(ClayiumMaterial.pureAntimatter, ClayiumShape.gem), E(0.1D, 10), ClayiumCore.divideByProgressionRate(300));
    }};
    // TODO
    public static final ClayiumRecipe smelter = new ClayiumRecipe() {{}};
    public static final ClayiumRecipe energeticClayDecomposer = new ClayiumRecipe() {{
        addRecipe(I(ClayiumBlocks.compressedClays.get(0)), 13, ClayiumItems.get(ClayiumMaterial.clay, ClayiumShape.ball, 4), 1L, 0L);
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
    public static final ClayiumRecipe fluidTransferMachine = new ClayiumRecipe() {{}};

    private static <T> List<T> L(T... elements) {
        return Arrays.asList(elements);
    }
    private static ItemStack I(Block block) {
        return I(new ItemBlock(block));
    }
    private static ItemStack I(Block block, int amount) {
        return I(new ItemBlockTiered(block), amount);
    }
    private static ItemStack I(Item item) {
        return new ItemStack(item);
    }
    private static ItemStack I(Item itemIn, int amount) {
        return new ItemStack(itemIn, amount);
    }
    private static ItemStack I(Item itemIn, int amount, int meta) {
        return new ItemStack(itemIn, amount, meta);
    }
    private static ItemStack W(Item item) {
        return new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
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
