package mods.clayium.machine;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import mods.clayium.block.tile.TileEntityGeneric;
import mods.clayium.core.ClayiumCore;
import mods.clayium.machine.AreaMiner.AreaMiner;
import mods.clayium.machine.AutoClayCondenser.AutoClayCondenser;
import mods.clayium.machine.AutoCrafter.AutoCrafter;
import mods.clayium.machine.AutoTrader.AutoTrader;
import mods.clayium.machine.CACollector.CACollector;
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
import mods.clayium.machine.ClayMarker.ClayMarker;
import mods.clayium.machine.ClayMarker.MarkerExtent;
import mods.clayium.machine.ClayMarker.TileEntityClayMarker;
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
import mods.clayium.machine.StorageContainer.StorageContainer;
import mods.clayium.machine.VacuumContainer.VacuumContainer;
import mods.clayium.machine.WaterWheel.WaterWheel;
import mods.clayium.util.TierPrefix;

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

        addContainers(EnumMachineKind.cobblestoneGenerator, TierPrefix.makeList(1, 2, 3, 4, 5, 6, 7), CobblestoneGenerator::new);

        addMachine(EnumMachineKind.condenser, TierPrefix.makeList(2, 3, 4, 5, 10));
        addMachine(EnumMachineKind.grinder, TierPrefix.makeList(2, 3, 4, 5, 6, 10));
        addMachine(EnumMachineKind.decomposer, TierPrefix.makeList(2, 3, 4));
        addMachine(EnumMachineKind.millingMachine, TierPrefix.makeList(1, 3, 4));

        addAssembler(EnumMachineKind.assembler, TierPrefix.makeList(3, 4, 6, 10));
        addAssembler(EnumMachineKind.inscriber, TierPrefix.makeList(3, 4));
        addMachines(EnumMachineKind.centrifuge, TierPrefix.makeList(3, 4, 5, 6), ClayCentrifuge::new);
        addMachine(EnumMachineKind.smelter, TierPrefix.makeList(4, 5, 6, 7, 8, 9));

        addContainers(EnumMachineKind.buffer, TierPrefix.makeList(4, 5, 6, 7, 8, 9, 10, 11, 12, 13), ClayBuffer::new);
        addContainers(EnumMachineKind.multitrackBuffer, TierPrefix.makeList(4, 5, 6, 7, 8, 9, 10, 11, 12, 13), MultitrackBuffer::new);
        CESource = addContainer(EnumMachineKind.creativeCESource, TierPrefix.OPA, new CreativeEnergySource());

        addMachines(EnumMachineKind.chemicalReactor, TierPrefix.makeList(4, 5, 8), ClayChemicalReactor::new);

        addContainers(EnumMachineKind.saltExtractor, TierPrefix.makeList(4, 5, 6, 7), SaltExtractor::new);

        autoClayCondenserMK1 = addContainer(EnumMachineKind.autoClayCondenser, TierPrefix.advanced, new AutoClayCondenser(TierPrefix.advanced));
        autoClayCondenserMK2 = addContainer(EnumMachineKind.autoClayCondenser, TierPrefix.claySteel, new AutoClayCondenser(TierPrefix.claySteel));

        quartzCrucible = addContainer(EnumMachineKind.quartzCrucible, TierPrefix.advanced, new QuartzCrucible());

        addContainers(EnumMachineKind.solarClayFabricator, TierPrefix.makeList(5, 6, 7), SolarClayFabricator::new);
        solarClayFabricatorMK1 = get(EnumMachineKind.solarClayFabricator, TierPrefix.advanced);
        solarClayFabricatorMK2 = get(EnumMachineKind.solarClayFabricator, TierPrefix.precision);
        lithiumSolarClayFabricator = get(EnumMachineKind.solarClayFabricator, TierPrefix.claySteel);

        addContainers(EnumMachineKind.clayFabricator, TierPrefix.makeList(8, 9, 13), ClayFabricator::new);
        clayFabricatorMK1 = get(EnumMachineKind.clayFabricator, TierPrefix.clayium);
        clayFabricatorMK2 = get(EnumMachineKind.clayFabricator, TierPrefix.ultimate);
        clayFabricatorMK3 = get(EnumMachineKind.clayFabricator, TierPrefix.clayium);

        chemicalMetalSeparator = addContainer(EnumMachineKind.chemicalMetalSeparator, TierPrefix.precision, new ChemicalMetalSeparator());

        alloySmelter = addAssembler(EnumMachineKind.alloySmelter, TierPrefix.precision);

        blastFurnace = addContainer(EnumMachineKind.blastFurnace, TierPrefix.precision, new ClayBlastFurnace());

        addContainers(EnumMachineKind.clayInterface, TierPrefix.makeList(5, 6, 7, 8, 9, 10, 11, 12, 13), ClayInterface::new);
        addContainers(EnumMachineKind.redstoneInterface, TierPrefix.makeList(5, 6, 7, 8, 9, 10, 11, 12, 13), RedstoneInterface::new);
        addContainers(EnumMachineKind.laserInterface, TierPrefix.makeList(7, 8, 9, 10, 11, 12, 13), ClayLaserInterface::new);

        addMachine(EnumMachineKind.electrolysisReactor, TierPrefix.makeList(6, 7, 8, 9));

        addContainers(EnumMachineKind.distributor, TierPrefix.makeList(7, 8, 9), ClayDistributor::new);

        addContainers(EnumMachineKind.autoCrafter, TierPrefix.makeList(5, 6, 7, 8, 9), AutoCrafter::new);

        clayReactor = addContainer(EnumMachineKind.clayReactor, TierPrefix.claySteel, new ClayReactor());

        addAssembler(EnumMachineKind.transformer, TierPrefix.makeList(7, 8, 9, 10, 11, 12));

        addContainers(EnumMachineKind.CACondenser, TierPrefix.makeList(9, 10, 11), CACondenser::new);

        addAssembler(EnumMachineKind.CAInjector, TierPrefix.makeList(9, 10, 11, 12, 13), TileEntityCAInjector::new);
        addContainer(EnumMachineKind.CACollector, TierPrefix.antimatter, new CACollector());
        addContainers(EnumMachineKind.CAReactorCore, TierPrefix.makeList(10, 11, 12, 13), CAReactor::new);

        energeticClayDecomposer = addMachine(EnumMachineKind.ECDecomposer, TierPrefix.OPA, null);
        storageContainer = addContainer(EnumMachineKind.storageContainer, TierPrefix.precision, new StorageContainer());
        vacuumContainer = addContainer(EnumMachineKind.vacuumContainer, TierPrefix.precision, new VacuumContainer());
        autoTrader = addContainer(EnumMachineKind.autoTrader, TierPrefix.clayium, new AutoTrader());

        clayMarker              = addContainer(EnumMachineKind.clayMarker, TierPrefix.claySteel, new ClayMarker("clay_marker", TierPrefix.claySteel, TileEntityClayMarker::new, MarkerExtent.None));
        clayOpenPitMarker       = addContainer(EnumMachineKind.openPitMarker, TierPrefix.clayium, new ClayMarker("open_pit_marker", TierPrefix.clayium, TileEntityClayMarker::new, MarkerExtent.OpenPit));
        groundLevelingMarker    = addContainer(EnumMachineKind.groundLevelingMarker, TierPrefix.clayium, new ClayMarker("ground_leveling_marker", TierPrefix.clayium, TileEntityClayMarker::new, MarkerExtent.GroundLeveling));
        prismMarker             = addContainer(EnumMachineKind.prismMarker, TierPrefix.clayium, new ClayMarker("prism_marker", TierPrefix.clayium, TileEntityClayMarker::new, MarkerExtent.Prism));

        addContainer(EnumMachineKind.areaMiner, TierPrefix.precision, new AreaMiner(TierPrefix.precision, "breaker"));
//        blockAreaCollector = (new AreaCollector(7))
        addContainer(EnumMachineKind.areaMiner, TierPrefix.clayium, new AreaMiner(TierPrefix.clayium, "area_miner"));
        addContainer(EnumMachineKind.advancedAreaMiner, TierPrefix.ultimate, new AreaMiner(TierPrefix.ultimate, "adv_area_miner"));
        addContainer(EnumMachineKind.areaReplacer, TierPrefix.antimatter, new AreaMiner(TierPrefix.antimatter, "area_replacer"));
//        blockActivator = (new AreaActivator(6))
//        blockAreaActivator = (new AreaActivator(8))


        addContainers(EnumMachineKind.waterWheel, TierPrefix.makeList(1, 2), WaterWheel::new);
        clayWaterWheel = get(EnumMachineKind.waterWheel, TierPrefix.clay);
        denseClayWaterWheel = get(EnumMachineKind.waterWheel, TierPrefix.denseClay);

        addContainers(EnumMachineKind.clayEnergyLaser, TierPrefix.makeList(7, 8, 9, 10), ClayEnergyLaser::new);
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

    private static void addContainers(EnumMachineKind kind, Iterable<TierPrefix> tiers, Function<TierPrefix, ? extends ClayContainer> blockGenerator) {
        for (TierPrefix tier : tiers) {
            addContainer(kind, tier, blockGenerator.apply(tier));
        }
    }

    private static void addMachine(EnumMachineKind kind, Iterable<TierPrefix> tiers) {
        addContainers(kind, tiers, tier -> new ClayiumMachine(kind, tier));
    }
    private static Block addMachine(EnumMachineKind kind, TierPrefix tier, String suffix) {
        return addContainer(kind, tier, new ClayiumMachine(kind, suffix, tier));
    }
    @Deprecated // use addContainers instead.
    private static void addMachines(EnumMachineKind kind, Iterable<TierPrefix> tiers, Function<TierPrefix, ? extends ClayiumMachine> blockGenerator) {
        addContainers(kind, tiers, blockGenerator);
    }
    private static Block addAssembler(EnumMachineKind kind, TierPrefix tier) {
        return addContainer(kind, tier, new ClayAssembler(kind, tier));
    }
    private static void addAssembler(EnumMachineKind kind, Iterable<TierPrefix> tiers) {
        addContainers(kind, tiers, tier -> new ClayAssembler(kind, tier));
    }
    private static void addAssembler(EnumMachineKind kind, TierPrefix tier, Supplier<? extends TileEntityGeneric> teSupplier) {
        addContainer(kind, tier, new ClayAssembler(kind, tier, teSupplier));
    }
    private static void addAssembler(EnumMachineKind kind, Iterable<TierPrefix> tiers, Supplier<? extends TileEntityGeneric> teSupplier) {
        addContainers(kind, tiers, tier -> new ClayAssembler(kind, tier, teSupplier));
    }
    private static void addAssemblerLike(EnumMachineKind kind, Iterable<TierPrefix> tiers, BiFunction<EnumMachineKind, TierPrefix, ? extends ClayAssembler> blockGenerator) {
        addContainers(kind, tiers, tier -> blockGenerator.apply(kind, tier));
    }

    public static Block get(EnumMachineKind kind, TierPrefix tier) {
        if (kind == EnumMachineKind.EMPTY || !tier.isValid()) return Blocks.AIR;

        if (machineMap.containsKey(kind) && machineMap.get(kind).containsKey(tier))
            return machineMap.getOrDefault(kind, Collections.emptyMap()).getOrDefault(tier, Blocks.AIR);
        throw new NoSuchElementException(tier.getPrefix() + " of " + kind.getRegisterName() + " is not registered!");
    }
    /**
     * @return the least tier machine of the kind
     */
    public static Block get(EnumMachineKind kind) {
        for (TierPrefix tier : TierPrefix.values()) {
            Block block = get(kind, tier);
            if (block != null && block != Blocks.AIR) return block;
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
        return machineMap.values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toList());
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
