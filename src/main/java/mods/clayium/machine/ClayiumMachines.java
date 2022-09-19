package mods.clayium.machine;

import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.ClayAssembler.ClayAssembler;
import mods.clayium.machine.ClayBuffer.ClayBuffer;
import mods.clayium.machine.ClayCentrifuge.ClayCentrifuge;
import mods.clayium.machine.ClayChemicalReactor.ClayChemicalReactor;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.ClayCraftingTable.ClayCraftingTable;
import mods.clayium.machine.ClayWorkTable.ClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.CobblestoneGenerator.CobblestoneGenerator;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ClayiumMachines {
    public static final Map<EnumMachineKind, Map<TierPrefix, ClayContainer>> machineMap = new EnumMap<>(EnumMachineKind.class);

    public static void registerMachines() {
        add(EnumMachineKind.workTable, 0, new ClayWorkTable());
        clayWorkTable = get(EnumMachineKind.workTable, 0);
        add(EnumMachineKind.craftingTable, 0, new ClayCraftingTable());
        clayCraftingTable = get(EnumMachineKind.craftingTable, 0);

        add(EnumMachineKind.ECCondenser, 3, "mk1");
        energeticClayCondenserMK1 = get(EnumMachineKind.ECCondenser, TierPrefix.simple);
        add(EnumMachineKind.ECCondenser, 4, "mk2");
        energeticClayCondenserMK2 = get(EnumMachineKind.ECCondenser, TierPrefix.basic);

        add(EnumMachineKind.bendingMachine, new int[] { 1, 2, 3, 4, 5, 6, 7, 9 });
        add(EnumMachineKind.wireDrawingMachine, new int[] { 1, 2, 3, 4 });
        add(EnumMachineKind.pipeDrawingMachine, new int[] { 1, 2, 3, 4 });
        add(EnumMachineKind.cuttingMachine, new int[] { 1, 2, 3, 4 });
        add(EnumMachineKind.lathe, new int[] { 1, 2, 3, 4 });

        add(EnumMachineKind.cobblestoneGenerator, new int[] { 1, 2, 3, 4, 5, 6, 7 }, CobblestoneGenerator.class);

        add(EnumMachineKind.condenser, new int[] { 2, 3, 4, 5, 10 });
        add(EnumMachineKind.grinder, new int[]{ 2, 3, 4, 5, 6, 10 });
        add(EnumMachineKind.decomposer, new int[]{ 2, 3, 4 });
        add(EnumMachineKind.millingMachine, new int[] { 1, 3, 4 });
        elementalMillingMachine = get(EnumMachineKind.millingMachine, TierPrefix.clay);

        add(EnumMachineKind.assembler, new int[] { 3, 4, 6, 10 }, ClayAssembler.class);
        add(EnumMachineKind.inscriber, new int[] { 3, 4 }, ClayAssembler.class);
        add(EnumMachineKind.centrifuge, new int[] { 3, 4, 5, 6 }, ClayCentrifuge.class);
        add(EnumMachineKind.smelter, new int[]{ 4, 5, 6, 7, 8, 9 });

        add(EnumMachineKind.buffer, new int[] { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 }, ClayBuffer.class);
//        add(EnumMachineKind.multitrackBuffer, new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, MultitrackBuffer.class)
//        add(EnumMachineKind.creativeCESource, 13, TileEntityCreativeEnergySource.class);

        add(EnumMachineKind.chemicalReactor, new int[]{ 4, 5, 8 }, ClayChemicalReactor.class);
    }

    private static void add(EnumMachineKind kind, int tier, ClayContainer block) {
        TierPrefix _tier = TierPrefix.get(tier);

        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));
        if (machineMap.containsKey(kind) && machineMap.get(kind).containsKey(_tier)) {
            ClayiumCore.logger.error("The machine already exists  [" + kind.getRegisterName() + "] [" + _tier.getPrefix() + "]");
            return;
        }

        machineMap.get(kind).put(_tier, block);
    }

    /**
     * @param blockClass whose constructor argument has at least (int)tier, or in addition ({@link EnumMachineKind})kind
     */
    private static void add(EnumMachineKind kind, int[] tiers, Class<? extends ClayContainer> blockClass) {
        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));

        Constructor<? extends ClayContainer> constructor;

        try {
            constructor = blockClass.getDeclaredConstructor(int.class);

            for (int tier : tiers) {
                add(kind, tier, constructor.newInstance(tier));
            }

            return;
        } catch (NoSuchMethodException ignore) {
            // if the constructor hadn't found, try another constructor.
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException instanceException) {
            ClayiumCore.logger.error(instanceException);
            return;
        }

        // maybe here is for ClayAssembler
        try {
            constructor = blockClass.getDeclaredConstructor(EnumMachineKind.class, int.class);

            for (int tier : tiers) {
                add(kind, tier, constructor.newInstance(kind, tier));
            }
        } catch (Exception e) {
            ClayiumCore.logger.error(e);
        }
    }

    private static void add(EnumMachineKind kind, int tier) {
        add(kind, tier, new ClayiumMachine(kind, tier));
    }
    private static void add(EnumMachineKind kind, int[] tiers) {
        for (int i : tiers)
            add(kind, i, new ClayiumMachine(kind, i));
    }
    private static void add(EnumMachineKind kind, int tier, String suffix) {
        add(kind, tier, new ClayiumMachine(kind, suffix, tier));
    }

    public static Block get(EnumMachineKind kind, TierPrefix tier) {
        if (machineMap.containsKey(kind) && machineMap.get(kind).containsKey(tier))
            return machineMap.get(kind).get(tier);
        return Blocks.AIR;
    }
    public static Block get(EnumMachineKind kind, int tier) {
        return get(kind, TierPrefix.get(tier));
    }

    public static List<Block> getBlocks() {
        List<Block> res = new ArrayList<>();

        for (Map<TierPrefix, ClayContainer> kind : machineMap.values()) {
            res.addAll(kind.values());
        }

        return res;
    }

    // aliases
    public static Block clayWorkTable = Blocks.AIR;
    public static Block clayCraftingTable = Blocks.AIR;
    public static Block elementalMillingMachine = Blocks.AIR;
    public static Block energeticClayCondenserMK1 = Blocks.AIR;
    public static Block energeticClayCondenserMK2 = Blocks.AIR;
    public static Block energeticClayDecomposer = Blocks.AIR;
    public static Block solarClayFabricatorMK1 = Blocks.AIR;
    public static Block solarClayFabricatorMK2 = Blocks.AIR;
    public static Block lithiumSolarClayFabricator = Blocks.AIR;
    public static Block autoClayCondenserMK1 = Blocks.AIR;
    public static Block autoClayCondenserMK2 = Blocks.AIR;
    public static Block blastFurnace = Blocks.AIR;
    public static Block chemicalMetalSeparator = Blocks.AIR;
    public static Block clayReactor = Blocks.AIR;
    public static Block quartzCrucible = Blocks.AIR;
    public static Block laserReflector = Blocks.AIR;
    public static Block clayFabricatorMK1 = Blocks.AIR;
    public static Block clayFabricatorMK2 = Blocks.AIR;
    public static Block clayFabricatorMK3 = Blocks.AIR;
    public static Block CACollector = Blocks.AIR;
}
