package mods.clayium.machine;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.AutoClayCondenser.AutoClayCondenser;
import mods.clayium.machine.AutoCrafter.AutoCrafter;
import mods.clayium.machine.CACondenser.CACondenser;
import mods.clayium.machine.CAInjector.TileEntityCAInjector;
import mods.clayium.machine.ChemicalMetalSeparator.ChemicalMetalSeparator;
import mods.clayium.machine.ClayAssembler.ClayAssembler;
import mods.clayium.machine.ClayBlastFurnace.ClayBlastFurnace;
import mods.clayium.machine.ClayBuffer.ClayBuffer;
import mods.clayium.machine.ClayCentrifuge.ClayCentrifuge;
import mods.clayium.machine.ClayChemicalReactor.ClayChemicalReactor;
import mods.clayium.machine.ClayContainer.ClayContainer;
import mods.clayium.machine.ClayCraftingTable.ClayCraftingTable;
import mods.clayium.machine.ClayDistributor.ClayDistributor;
import mods.clayium.machine.ClayEnergyLaser.ClayEnergyLaser;
import mods.clayium.machine.ClayFabricator.ClayFabricator;
import mods.clayium.machine.ClayReactor.ClayReactor;
import mods.clayium.machine.ClayWorkTable.ClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.CobblestoneGenerator.CobblestoneGenerator;
import mods.clayium.machine.CreativeEnergySource.CreativeEnergySource;
import mods.clayium.machine.Interface.ClayInterface.ClayInterface;
import mods.clayium.machine.Interface.ClayLaserInterface.ClayLaserInterface;
import mods.clayium.machine.Interface.RedstoneInterface.RedstoneInterface;
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
        clayWorkTable = addContainer(EnumMachineKind.workTable, TierPrefix.clay, new ClayWorkTable());
        clayCraftingTable = addContainer(EnumMachineKind.craftingTable, TierPrefix.clay, new ClayCraftingTable());

        energeticClayCondenserMK1 = addMachine(EnumMachineKind.ECCondenser, TierPrefix.simple, "mk1");
        energeticClayCondenserMK2 = addMachine(EnumMachineKind.ECCondenser, TierPrefix.basic, "mk2");

        addMachine(EnumMachineKind.bendingMachine, TierPrefix.makeList(1, 2, 3, 4, 5, 6, 7, 9));
        addMachine(EnumMachineKind.wireDrawingMachine, TierPrefix.makeList(1, 2, 3, 4));
        addMachine(EnumMachineKind.pipeDrawingMachine, TierPrefix.makeList(1, 2, 3, 4));
        addMachine(EnumMachineKind.cuttingMachine, TierPrefix.makeList(1, 2, 3, 4));
        addMachine(EnumMachineKind.lathe, TierPrefix.makeList(1, 2, 3, 4));

        addContainers(EnumMachineKind.cobblestoneGenerator, TierPrefix.makeList(1, 2, 3, 4, 5, 6, 7), CobblestoneGenerator.class);

        addMachine(EnumMachineKind.condenser, TierPrefix.makeList(2, 3, 4, 5, 10));
        addMachine(EnumMachineKind.grinder, TierPrefix.makeList(2, 3, 4, 5, 6, 10));
        addMachine(EnumMachineKind.decomposer, TierPrefix.makeList(2, 3, 4));
        addMachine(EnumMachineKind.millingMachine, TierPrefix.makeList(1, 3, 4));

        addAssembler(EnumMachineKind.assembler, TierPrefix.makeList(3, 4, 6, 10));
        addAssembler(EnumMachineKind.inscriber, TierPrefix.makeList(3, 4));
        addMachines(EnumMachineKind.centrifuge, TierPrefix.makeList(3, 4, 5, 6), ClayCentrifuge.class);
        addMachine(EnumMachineKind.smelter, TierPrefix.makeList(4, 5, 6, 7, 8, 9));

        addContainers(EnumMachineKind.buffer, TierPrefix.makeList(4, 5, 6, 7, 8, 9, 10, 11, 12, 13), ClayBuffer.class);
        addContainers(EnumMachineKind.multitrackBuffer, TierPrefix.makeList(4, 5, 6, 7, 8, 9, 10, 11, 12, 13), MultitrackBuffer.class);
        addContainer(EnumMachineKind.creativeCESource, TierPrefix.OPA, new CreativeEnergySource());

        addMachines(EnumMachineKind.chemicalReactor, TierPrefix.makeList(4, 5, 8), ClayChemicalReactor.class);

        addContainers(EnumMachineKind.saltExtractor, TierPrefix.makeList(4, 5, 6, 7), SaltExtractor.class);

        autoClayCondenserMK1 = addContainer(EnumMachineKind.autoClayCondenser, TierPrefix.advanced, new AutoClayCondenser(TierPrefix.advanced));
        autoClayCondenserMK2 = addContainer(EnumMachineKind.autoClayCondenser, TierPrefix.claySteel, new AutoClayCondenser(TierPrefix.claySteel));

        quartzCrucible = addContainer(EnumMachineKind.quartzCrucible, TierPrefix.advanced, new QuartzCrucible());

        addContainers(EnumMachineKind.solarClayFabricator, TierPrefix.makeList(5, 6, 7), SolarClayFabricator.class);
        solarClayFabricatorMK1 = get(EnumMachineKind.solarClayFabricator, TierPrefix.advanced);
        solarClayFabricatorMK2 = get(EnumMachineKind.solarClayFabricator, TierPrefix.precision);
        lithiumSolarClayFabricator = get(EnumMachineKind.solarClayFabricator, TierPrefix.claySteel);

        addContainers(EnumMachineKind.clayFabricator, TierPrefix.makeList(8, 9, 13), ClayFabricator.class);
        clayFabricatorMK1 = get(EnumMachineKind.clayFabricator, TierPrefix.clayium);
        clayFabricatorMK2 = get(EnumMachineKind.clayFabricator, TierPrefix.ultimate);
        clayFabricatorMK3 = get(EnumMachineKind.clayFabricator, TierPrefix.clayium);

        chemicalMetalSeparator = addContainer(EnumMachineKind.chemicalMetalSeparator, TierPrefix.precision, new ChemicalMetalSeparator());

        alloySmelter = addAssembler(EnumMachineKind.alloySmelter, TierPrefix.precision);

        blastFurnace = addContainer(EnumMachineKind.blastFurnace, TierPrefix.precision, new ClayBlastFurnace());

        addContainers(EnumMachineKind.clayInterface, TierPrefix.makeList(5, 6, 7, 8, 9, 10, 11, 12, 13), ClayInterface.class);
        addContainers(EnumMachineKind.redstoneInterface, TierPrefix.makeList(5, 6, 7, 8, 9, 10, 11, 12, 13), RedstoneInterface.class);
        addContainers(EnumMachineKind.laserInterface, TierPrefix.makeList(7, 8, 9, 10, 11, 12, 13), ClayLaserInterface.class);

        addMachine(EnumMachineKind.electrolysisReactor, TierPrefix.makeList(6, 7, 8, 9));

        addContainers(EnumMachineKind.distributor, TierPrefix.makeList(7, 8, 9), ClayDistributor.class);

        addContainers(EnumMachineKind.autoCrafter, TierPrefix.makeList(5, 6, 7, 8, 9), AutoCrafter.class);

        clayReactor = addContainer(EnumMachineKind.clayReactor, TierPrefix.claySteel, new ClayReactor());

        addAssembler(EnumMachineKind.transformer, TierPrefix.makeList(7, 8, 9, 10, 11, 12));

        addContainers(EnumMachineKind.CACondenser, TierPrefix.makeList(9, 10, 11), CACondenser.class);

        addAssembler(EnumMachineKind.CAInjector, TierPrefix.makeList(9, 10, 11, 12, 13), TileEntityCAInjector.class);
//        new ClayNoRecipeMachines((String)null, 10, TileCACollector.class, 18, 1);
//        blockCAReactorHull = new BlockDamaged(Material.iron, 10);
//        registerTieredMachines("CAReactor", new int[]{10, 11, 12, 13}, TileCAReactor.class, 9, CAReactor.class, ItemBlockTiered.class);
//        new ClayMachines("ECDecomposer", 13);
//        energeticClayDecomposer = get(EnumMachineKind.decomposer, TierPrefix.OPA);
//        new StorageContainer(Material.iron, "clayium:az91dhull");
//        storageContainer = get(EnumMachineKind.storageContainer, TierPrefix.precision);
//        new VacuumContainer(Material.iron, "clayium:az91dhull");
//        vacuumContainer = get(EnumMachineKind.vacuumContainer, TierPrefix.precision);
//        new AutoTrader(8);
//        autoTrader = get(EnumMachineKind.autoTrader, TierPrefix.clayium);
//        new ClayMarker(7, Blocks.clay, TileClayMarker.class);
//        clayMarker = get(EnumMachineKind.clayMarker, TierPrefix.claySteel);
//        new ClayMarker(8, blockCompressedClay, TileClayOpenPitMarker.class);
//        clayOpenPitMarker = get(EnumMachineKind.openPitMarker, TierPrefix.clayium);
//        new ClayMarker(8, blockCompressedClay, 1, TileClayGroundLevelingMarker.class);
//        groundLevelingMarker = get(EnumMachineKind.groundLevelingMarker, TierPrefix.clayium);
//        new ClayMarker(8, blockCompressedClay, 2, TileClayPrismMarker.class);
//        prismMarker = get(EnumMachineKind.prismMarker, TierPrefix.clayium);
//        blockMiner = (new AreaMiner(6, "clayium:areaminer"))
//        blockAreaCollector = (new AreaCollector(7))
//        blockAreaMiner = (new AreaMiner(8, "clayium:areaminer"))
//        blockAdvancedAreaMiner = (new AreaMiner(9, "clayium:advareaminer"))
//        blockAreaReplacer = (new AreaMiner(10, "clayium:areareplacer"))
//        blockActivator = (new AreaActivator(6))
//        blockAreaActivator = (new AreaActivator(8))


        addContainers(EnumMachineKind.waterWheel, TierPrefix.makeList(1, 2), WaterWheel.class);
        clayWaterWheel = get(EnumMachineKind.waterWheel, TierPrefix.clay);
        denseClayWaterWheel = get(EnumMachineKind.waterWheel, TierPrefix.denseClay);

        addContainers(EnumMachineKind.clayEnergyLaser, TierPrefix.makeList(7, 8, 9, 10), ClayEnergyLaser.class);
        laserReflector = addContainer(EnumMachineKind.laserReflector, TierPrefix.claySteel, new LaserReflector());

//        blockClayChunkLoader = (new ClayChunkLoader(6))
//        blockPANCore = (new PANCore())
//        blocksPANAdapter = registerTieredContainers("PANAdapter", new int[]{10, 11, 12, 13}, PANAdapter.class);
//        blocksPANDuplicator = registerTieredContainers("PANDuplicator", new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, PANDuplicator.class);
//
//        for(i = 4; i <= 13; ++i) {
//            ((PANDuplicator)blocksPANDuplicator[i])
//        }
//
//        blockPANCable = (new PANCable())
//        blockMetalChest = (new MetalChest())

//        if (ClayiumCore.cfgEnableFluidCapsule) {
//            blocksFluidTranslator = registerTieredContainers("FluidTranslator", new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, FluidTranslator.class);
//            blocksFluidTransferMachine = registerTieredMachines("FluidTransferMachine", new int[]{5}, ClayChemicalReactor.class, ItemBlockTiered.class);
//        }
//        blocksRFGenerator = new HashMap();
    }

    private static Block addContainer(EnumMachineKind kind, TierPrefix tier, Block block) {
        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));
        if (machineMap.containsKey(kind) && machineMap.get(kind).containsKey(tier)) {
            ClayiumCore.logger.error("The machine already exists  [" + kind.getRegisterName() + "] [" + tier.getPrefix() + "]");
            return get(kind, tier);
        }

        machineMap.get(kind).put(tier, block);
        return get(kind, tier);
    }

    private static void addContainers(EnumMachineKind kind, List<TierPrefix> tiers, Class<? extends ClayContainer> blockClass) {
        ClayiumCore.logger.info("Registering: " + blockClass.toString());

        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));

        try {
//            Constructor<? extends ClayContainer> constructor = blockClass.getDeclaredConstructor(int.class);
            Constructor<? extends ClayContainer> constructor2 = blockClass.getDeclaredConstructor(TierPrefix.class);

            for (TierPrefix tier : tiers) {
                addContainer(kind, tier, constructor2.newInstance(tier));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ClayiumCore.logger.error(e);
            assert true : blockClass.getName() + " doesn't have constructor(TierPrefix)";
        }
    }

    private static void addMachine(EnumMachineKind kind, List<TierPrefix> tiers) {
        for (TierPrefix tier : tiers)
            addContainer(kind, tier, new ClayiumMachine(kind, tier));
    }
    private static Block addMachine(EnumMachineKind kind, TierPrefix tier, String suffix) {
        return addContainer(kind, tier, new ClayiumMachine(kind, suffix, tier));
    }
    private static void addMachineTE(EnumMachineKind kind, TierPrefix tier, Class<? extends TileEntityGeneric> teClass) {
        addContainer(kind, tier, new ClayiumMachine(kind, tier, teClass));
    }
    private static void addMachineTE(EnumMachineKind kind, List<TierPrefix> tiers, Class<? extends TileEntityGeneric> teClass) {
        for (TierPrefix tier : tiers)
            addContainer(kind, tier, new ClayiumMachine(kind, tier, teClass));
    }
    private static void addMachines(EnumMachineKind kind, List<TierPrefix> tiers, Class<? extends ClayiumMachine> blockClass) {
        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));

        try {
//            Constructor<? extends ClayContainer> constructor = blockClass.getDeclaredConstructor(int.class);
            Constructor<? extends ClayContainer> constructor2 = blockClass.getDeclaredConstructor(TierPrefix.class);

            for (TierPrefix tier : tiers) {
                addContainer(kind, tier, constructor2.newInstance(tier));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ClayiumCore.logger.error(e);
            assert true : blockClass.getName() + " doesn't have constructor(TierPrefix)";
        }
    }
    private static Block addAssembler(EnumMachineKind kind, TierPrefix tier) {
        return addContainer(kind, tier, new ClayAssembler(kind, tier));
    }
    private static void addAssembler(EnumMachineKind kind, List<TierPrefix> tiers) {
        for (TierPrefix tier : tiers)
            addContainer(kind, tier, new ClayAssembler(kind, tier));
    }
    private static void addAssembler(EnumMachineKind kind, TierPrefix tier, Class<? extends TileEntityGeneric> teClass) {
        addContainer(kind, tier, new ClayAssembler(kind, tier, teClass));
    }
    private static void addAssembler(EnumMachineKind kind, List<TierPrefix> tiers, Class<? extends TileEntityGeneric> teClass) {
        for (TierPrefix tier : tiers)
            addContainer(kind, tier, new ClayAssembler(kind, tier, teClass));
    }
    private static void addAssemblerLike(EnumMachineKind kind, List<TierPrefix> tiers, Class<? extends ClayAssembler> blockClass) {
        if (!machineMap.containsKey(kind)) machineMap.put(kind, new EnumMap<>(TierPrefix.class));
        try {
//            Constructor<? extends ClayAssembler> constructor = blockClass.getDeclaredConstructor(EnumMachineKind.class, int.class);
            Constructor<? extends ClayAssembler> constructor2 = blockClass.getDeclaredConstructor(EnumMachineKind.class, TierPrefix.class);

            for (TierPrefix tier : tiers) {
                addContainer(kind, tier, constructor2.newInstance(kind, tier));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            ClayiumCore.logger.error(e);
            assert true : blockClass.getName() + " doesn't have constructor(EnumMachineKind, TierPrefix)";
        }
    }

    public static Block get(EnumMachineKind kind, TierPrefix tier) {
        if (machineMap.containsKey(kind) && machineMap.get(kind).containsKey(tier))
            return machineMap.get(kind).get(tier);
        throw new NoSuchElementException(tier.getPrefix() + " of " + kind.getRegisterName() + " is not registered!");
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

        throw new NoSuchElementException(kind.getRegisterName() + " is not registered!");
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
    public static Block laserReflector = Blocks.AIR;
    public static Block clayFabricatorMK1 = Blocks.AIR;
    public static Block clayFabricatorMK2 = Blocks.AIR;
    public static Block clayFabricatorMK3 = Blocks.AIR;
    public static Block CACollector = Blocks.AIR;
    public static Block clayWaterWheel = Blocks.AIR;
    public static Block denseClayWaterWheel = Blocks.AIR;
    public static Block CESource = Blocks.AIR;
    public static Block storageContainer = Blocks.AIR;
    public static Block vacuumContainer = Blocks.AIR;
    public static Block autoTrader = Blocks.AIR;
    public static Block clayMarker = Blocks.AIR;
    public static Block clayOpenPitMarker = Blocks.AIR;
    public static Block groundLevelingMarker = Blocks.AIR;
    public static Block prismMarker = Blocks.AIR;
}
