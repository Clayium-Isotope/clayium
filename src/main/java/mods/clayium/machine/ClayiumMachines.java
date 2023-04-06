package mods.clayium.machine;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.AutoClayCondenser.AutoClayCondenser;
import mods.clayium.machine.ChemicalMetalSeparator.ChemicalMetalSeparator;
import mods.clayium.machine.ClayAssembler.ClayAssembler;
import mods.clayium.machine.ClayAssembler.TileEntityClayAssembler;
import mods.clayium.machine.ClayBuffer.ClayBuffer;
import mods.clayium.machine.ClayCentrifuge.ClayCentrifuge;
import mods.clayium.machine.ClayChemicalReactor.ClayChemicalReactor;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.ClayCraftingTable.ClayCraftingTable;
import mods.clayium.machine.ClayEnergyLaser.ClayEnergyLaser;
import mods.clayium.machine.ClayFabricator.ClayFabricator;
import mods.clayium.machine.ClayWorkTable.ClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.CobblestoneGenerator.CobblestoneGenerator;
import mods.clayium.machine.Interface.ClayInterface.ClayInterface;
import mods.clayium.machine.LaserReflector.LaserReflector;
import mods.clayium.machine.MultitrackBuffer.MultitrackBuffer;
import mods.clayium.machine.QuartzCrucible.QuartzCrucible;
import mods.clayium.machine.SaltExtractor.SaltExtractor;
import mods.clayium.machine.SolarClayFabricator.SolarClayFabricator;
import mods.clayium.machine.WaterWheel.WaterWheel;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ClayiumMachines {
    public static final Map<EnumMachineKind, Map<TierPrefix, Block>> machineMap = new EnumMap<>(EnumMachineKind.class);

    public static void registerMachines() {
        addContainer(EnumMachineKind.workTable, 0, new ClayWorkTable());
        clayWorkTable = get(EnumMachineKind.workTable, TierPrefix.none);
        addContainer(EnumMachineKind.craftingTable, 0, new ClayCraftingTable());
        clayCraftingTable = get(EnumMachineKind.craftingTable, TierPrefix.none);

        addMachine(EnumMachineKind.ECCondenser, 3, "mk1");
        energeticClayCondenserMK1 = get(EnumMachineKind.ECCondenser, TierPrefix.simple);
        addMachine(EnumMachineKind.ECCondenser, 4, "mk2");
        energeticClayCondenserMK2 = get(EnumMachineKind.ECCondenser, TierPrefix.basic);

        addMachine(EnumMachineKind.bendingMachine, new int[] { 1, 2, 3, 4, 5, 6, 7, 9 });
        addMachine(EnumMachineKind.wireDrawingMachine, new int[] { 1, 2, 3, 4 });
        addMachine(EnumMachineKind.pipeDrawingMachine, new int[] { 1, 2, 3, 4 });
        addMachine(EnumMachineKind.cuttingMachine, new int[] { 1, 2, 3, 4 });
        addMachine(EnumMachineKind.lathe, new int[] { 1, 2, 3, 4 });

        addContainers(EnumMachineKind.cobblestoneGenerator, new int[] { 1, 2, 3, 4, 5, 6, 7 }, CobblestoneGenerator.class);

        addMachine(EnumMachineKind.condenser, new int[] { 2, 3, 4, 5, 10 });
        addMachine(EnumMachineKind.grinder, new int[]{ 2, 3, 4, 5, 6, 10 });
        addMachine(EnumMachineKind.decomposer, new int[]{ 2, 3, 4 });
        addMachine(EnumMachineKind.millingMachine, new int[] { 1, 3, 4 });
        elementalMillingMachine = get(EnumMachineKind.millingMachine, TierPrefix.clay);

        addAssembler(EnumMachineKind.assembler, new int[] { 3, 4, 6, 10 });
        addAssembler(EnumMachineKind.inscriber, new int[] { 3, 4 });
        addMachines(EnumMachineKind.centrifuge, new int[] { 3, 4, 5, 6 }, ClayCentrifuge.class);
        addMachine(EnumMachineKind.smelter, new int[]{ 4, 5, 6, 7, 8, 9 });

        addContainers(EnumMachineKind.buffer, new int[] { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 }, ClayBuffer.class);
        addContainers(EnumMachineKind.multitrackBuffer, new int[]{ 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 }, MultitrackBuffer.class);
//        add(EnumMachineKind.creativeCESource, 13, TileEntityCreativeEnergySource.class);

        addMachines(EnumMachineKind.chemicalReactor, new int[]{ 4, 5, 8 }, ClayChemicalReactor.class);

        addContainers(EnumMachineKind.saltExtractor, new int[] { 4, 5, 6, 7 }, SaltExtractor.class);

        addContainers(EnumMachineKind.autoClayCondenser, new int[] { 5, 7 }, AutoClayCondenser.class);
        autoClayCondenserMK1 = get(EnumMachineKind.autoClayCondenser, TierPrefix.advanced);
        autoClayCondenserMK2 = get(EnumMachineKind.autoClayCondenser, TierPrefix.claySteel);

        addContainer(EnumMachineKind.quartzCrucible, 5, new QuartzCrucible());
        quartzCrucible = get(EnumMachineKind.quartzCrucible, 5);

        addContainers(EnumMachineKind.solarClayFabricator, new int[] { 5, 6, 7 }, SolarClayFabricator.class);
        solarClayFabricatorMK1 = get(EnumMachineKind.solarClayFabricator, TierPrefix.advanced);
        solarClayFabricatorMK2 = get(EnumMachineKind.solarClayFabricator, TierPrefix.precision);
        lithiumSolarClayFabricator = get(EnumMachineKind.solarClayFabricator, TierPrefix.claySteel);

        addContainers(EnumMachineKind.clayFabricator, new int[] { 8, 9, 13 }, ClayFabricator.class);
        clayFabricatorMK1 = get(EnumMachineKind.clayFabricator, TierPrefix.clayium);
        clayFabricatorMK2 = get(EnumMachineKind.clayFabricator, TierPrefix.ultimate);
        clayFabricatorMK3 = get(EnumMachineKind.clayFabricator, TierPrefix.clayium);

        addContainer(EnumMachineKind.chemicalMetalSeparator, 6, new ChemicalMetalSeparator());
        chemicalMetalSeparator = get(EnumMachineKind.chemicalMetalSeparator, TierPrefix.precision);

        addAssembler(EnumMachineKind.alloySmelter, 6);
        alloySmelter = get(EnumMachineKind.alloySmelter, TierPrefix.precision);

        addContainers(EnumMachineKind.clayInterface, new int[]{ 5, 6, 7, 8, 9, 10, 11, 12, 13 }, ClayInterface.class);
//        add(EnumMachineKind.redstoneInterface, new int[]{ 5, 6, 7, 8, 9, 10, 11, 12, 13 }, RedstoneInterface.class);
//        add(EnumMachineKind.laserInterface, new int[]{ 7, 8, 9, 10, 11, 12, 13 }, ClayLaserInterface.class);
//
//        addMachine(EnumMachineKind.electrolysisReactor, new int[]{ 6, 7, 8, 9 });
//
//        add(EnumMachineKind.distributor, new int[]{ 7, 8, 9 }, ClayDistributor.class);
//
//        add(EnumMachineKind.autoCrafter, new int[]{ 5, 6, 7, 8, 9 }, ClayAutoCrafter.class);
//
//        add(EnumMachineKind.clayReactor, 7, new ClayReactor());
//        clayReactor = get(EnumMachineKind.clayReactor, TierPrefix.claySteel);
//
//        addAssembler(EnumMachineKind.transformer, new int[]{ 7, 8, 9, 10, 11, 12 });
//
//        addContainer(EnumMachineKind.CACondenser, new int[]{9, 10, 11}, TileCACondenser.class);


        addContainers(EnumMachineKind.waterWheel, new int[] { 1, 2 }, WaterWheel.class);
        clayWaterWheel = get(EnumMachineKind.waterWheel, TierPrefix.clay);
        denseClayWaterWheel = get(EnumMachineKind.waterWheel, TierPrefix.denseClay);

        addContainers(EnumMachineKind.clayEnergyLaser, new int[] { 7, 8, 9, 10 }, ClayEnergyLaser.class);
        addContainer(EnumMachineKind.laserReflector, 7, new LaserReflector());
    }

    private static void addContainer(EnumMachineKind kind, int tier, Block block) {
        TierPrefix _tier = TierPrefix.get(tier);

        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));
        if (machineMap.containsKey(kind) && machineMap.get(kind).containsKey(_tier)) {
            ClayiumCore.logger.error("The machine already exists  [" + kind.getRegisterName() + "] [" + _tier.getPrefix() + "]");
            return;
        }

        machineMap.get(kind).put(_tier, block);
    }

    private static void addContainers(EnumMachineKind kind, int[] tiers, Class<? extends ClayContainer> blockClass) {
        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));

        try {
            Constructor<? extends ClayContainer> constructor = blockClass.getDeclaredConstructor(int.class);

            for (int tier : tiers) {
                addContainer(kind, tier, constructor.newInstance(tier));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ClayiumCore.logger.error(e);
        }
    }

    private static void addMachine(EnumMachineKind kind, int tier) {
        addContainer(kind, tier, new ClayiumMachine(kind, tier));
    }
    private static void addMachine(EnumMachineKind kind, int[] tiers) {
        for (int tier : tiers)
            addContainer(kind, tier, new ClayiumMachine(kind, tier));
    }
    private static void addMachine(EnumMachineKind kind, int tier, String suffix) {
        addContainer(kind, tier, new ClayiumMachine(kind, suffix, tier));
    }
    private static void addMachineTE(EnumMachineKind kind, int tier, Class<? extends TileEntityGeneric> teClass) {
        addContainer(kind, tier, new ClayiumMachine(kind, tier, teClass));
    }
    private static void addMachineTE(EnumMachineKind kind, int[] tiers, Class<? extends TileEntityGeneric> teClass) {
        for (int tier : tiers)
            addContainer(kind, tier, new ClayiumMachine(kind, tier, teClass));
    }
    private static void addMachines(EnumMachineKind kind, int[] tiers, Class<? extends ClayiumMachine> blockClass) {
        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));

        try {
            Constructor<? extends ClayContainer> constructor = blockClass.getDeclaredConstructor(int.class);

            for (int tier : tiers) {
                addContainer(kind, tier, constructor.newInstance(tier));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ClayiumCore.logger.error(e);
        }
    }
    private static void addAssembler(EnumMachineKind kind, int tier) {
        addContainer(kind, tier, new ClayAssembler(kind, tier));
    }
    private static void addAssembler(EnumMachineKind kind, int[] tiers) {
        for (int tier : tiers) {
            addContainer(kind, tier, new ClayAssembler(kind, tier));
        }
    }
    private static void addAssembler(EnumMachineKind kind, int tier, Class<? extends TileEntityClayAssembler> teClass) {
        addContainer(kind, tier, new ClayAssembler(kind, tier, teClass));
    }
    private static void addAssembler(EnumMachineKind kind, int[] tiers, Class<? extends TileEntityClayAssembler> teClass) {
        for (int tier : tiers) {
            addContainer(kind, tier, new ClayAssembler(kind, tier, teClass));
        }
    }
    private static void addAssemblerLike(EnumMachineKind kind, int[] tiers, Class<? extends ClayAssembler> blockClass) {
        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));
        try {
            Constructor<? extends ClayAssembler> constructor = blockClass.getDeclaredConstructor(EnumMachineKind.class, int.class);

            for (int tier : tiers) {
                addContainer(kind, tier, constructor.newInstance(kind, tier));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ClayiumCore.logger.error(e);
        }
    }

    public static Block get(EnumMachineKind kind, TierPrefix tier) {
        if (machineMap.containsKey(kind) && machineMap.get(kind).containsKey(tier))
            return machineMap.get(kind).get(tier);
        return null;
    }
    @Deprecated // iff tier is clear, use TierPrefix version.
    public static Block get(EnumMachineKind kind, int tier) {
        return get(kind, TierPrefix.get(tier));
    }
    /**
     * @return the least tier machine of the kind
     */
    public static Block get(EnumMachineKind kind) {
        Block block;

        for (TierPrefix tier : TierPrefix.values()) {
            block = get(kind, tier);
            if (block != null) return block;
        }

        return null;
// TODO change to    throw new NoSuchElementException(kind.getRegisterName() + " is not registered!");
    }
    /**
     * @return all of registered machines which the kind has
     */
    public static List<ItemStack> getSet(EnumMachineKind kind) {
        if (!machineMap.containsKey(kind))
            return Collections.emptyList();
        return machineMap.get(kind).values().stream().map(ItemStack::new).collect(Collectors.toList());
    }

    public static List<Block> getBlocks() {
        List<Block> res = new ArrayList<>();

        for (Map<TierPrefix, Block> kind : machineMap.values()) {
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
    public static Block alloySmelter = Blocks.AIR;
    public static Block clayReactor = Blocks.AIR;
    public static Block quartzCrucible = Blocks.AIR;
    public static Block laserReflector = get(EnumMachineKind.laserReflector, 7);
    public static Block clayFabricatorMK1 = Blocks.AIR;
    public static Block clayFabricatorMK2 = Blocks.AIR;
    public static Block clayFabricatorMK3 = Blocks.AIR;
    public static Block CACollector = Blocks.AIR;
    public static Block clayWaterWheel = Blocks.AIR;
    public static Block denseClayWaterWheel = Blocks.AIR;
}
