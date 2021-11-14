package mods.clayium.block;

import cpw.mods.fml.common.registry.GameRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mods.clayium.block.itemblock.ItemBlockCompressedClay;
import mods.clayium.block.itemblock.ItemBlockDamaged;
import mods.clayium.block.itemblock.ItemBlockTiered;
import mods.clayium.block.tile.TileAreaActivator;
import mods.clayium.block.tile.TileAreaCollector;
import mods.clayium.block.tile.TileAreaMiner;
import mods.clayium.block.tile.TileAutoClayCondenser;
import mods.clayium.block.tile.TileAutoCrafter;
import mods.clayium.block.tile.TileAutoTrader;
import mods.clayium.block.tile.TileCACollector;
import mods.clayium.block.tile.TileCACondenser;
import mods.clayium.block.tile.TileCAInjector;
import mods.clayium.block.tile.TileCAReactor;
import mods.clayium.block.tile.TileChemicalMetalSeparator;
import mods.clayium.block.tile.TileClayAssembler;
import mods.clayium.block.tile.TileClayBlastFurnace;
import mods.clayium.block.tile.TileClayBuffer;
import mods.clayium.block.tile.TileClayCentrifuge;
import mods.clayium.block.tile.TileClayChemicalReactor;
import mods.clayium.block.tile.TileClayChunkLoader;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.block.tile.TileClayContainerInterface;
import mods.clayium.block.tile.TileClayCraftingTable;
import mods.clayium.block.tile.TileClayDistributor;
import mods.clayium.block.tile.TileClayEnergyLaser;
import mods.clayium.block.tile.TileClayFabricator;
import mods.clayium.block.tile.TileClayLaserInterface;
import mods.clayium.block.tile.TileClayMachines;
import mods.clayium.block.tile.TileClayMarker;
import mods.clayium.block.tile.TileClayOpenPitMarker;
import mods.clayium.block.tile.TileClayRFGenerator;
import mods.clayium.block.tile.TileClayReactor;
import mods.clayium.block.tile.TileClayWorkTable;
import mods.clayium.block.tile.TileCobblestoneGenerator;
import mods.clayium.block.tile.TileCreativeEnergySource;
import mods.clayium.block.tile.TileFluidTranslator;
import mods.clayium.block.tile.TileLaserReflector;
import mods.clayium.block.tile.TileMetalChest;
import mods.clayium.block.tile.TileMultitrackBuffer;
import mods.clayium.block.tile.TilePANAdapter;
import mods.clayium.block.tile.TilePANCore;
import mods.clayium.block.tile.TilePANDuplicator;
import mods.clayium.block.tile.TileQuartzCrucible;
import mods.clayium.block.tile.TileRedstoneInterface;
import mods.clayium.block.tile.TileSaltExtractor;
import mods.clayium.block.tile.TileSolarClayFabricator;
import mods.clayium.block.tile.TileStorageContainer;
import mods.clayium.block.tile.TileVacuumContainer;
import mods.clayium.block.tile.TileWaterWheel;
import mods.clayium.core.ClayiumCore;
import mods.clayium.misc.TileFluidTab;
import mods.clayium.util.UtilLocale;
import mods.clayium.util.UtilTier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;


public class CBlocks {
    public static Block blockClayWorkTable;
    public static Block blockClayCraftingTable;
    public static Block blockDenseClay;
    public static Block blockCompressedClay;
    public static Block blockRawClayMachineHull;
    public static Block blockMachineHull;
    public static BlockDamaged blockOthersHull;
    public static BlockDamaged blockMaterial;
    public static BlockDamaged blockSiliconeColored;
    public static Block[] blocksBendingMachine;
    public static Block[] blocksWireDrawingMachine;
    public static Block[] blocksPipeDrawingMachine;
    public static Block[] blocksCuttingMachine;
    public static Block[] blocksLathe;
    public static Block[] blocksCobblestoneGenerator;
    public static Block blockElementalMillingMachine;
    public static Block[] blocksCondenser;
    public static Block[] blocksGrinder;
    public static Block[] blocksDecomposer;
    public static Block[] blocksMillingMachine;
    public static Block blockClayWaterWheel;
    public static Block blockDenseClayWaterWheel;
    public static Block blockEnergeticClayCondenser;
    public static Block blockEnergeticClayCondenserMK2;
    public static Block[] blocksAssembler;
    public static Block[] blocksInscriber;
    public static Block[] blocksCentrifuge;
    public static Block[] blocksSmelter;
    public static Block[] blocksChemicalReactor;
    public static Block[] blocksBuffer;
    public static Block[] blocksMultitrackBuffer;
    public static Block[] blocksSaltExtractor;
    public static Block blockAutoClayCondenser;
    public static Block blockAutoClayCondenserMK2;
    public static Block blockQuartzCrucible;
    public static Block blockSolarClayFabricatorMK1;
    public static Block blockSolarClayFabricatorMK2;
    public static Block blockLithiumSolarClayFabricator;
    public static Block blockClayFabricatorMK1;
    public static Block blockClayFabricatorMK2;
    public static Block blockClayFabricatorMK3;
    public static Block[] blocksAlloySmelter;
    public static Block[] blocksAutoCrafter;
    public static Block blockChemicalMetalSeparator;
    public static Block blockClayBlastFurnace;
    public static Block[] blocksClayInterface;

    public static void registerBlocks() {
        blockClayOre = (new ClayOre()).setBlockName("blockClayOre").setBlockTextureName("clayium:clayore");
        GameRegistry.registerBlock(blockClayOre, ItemBlockDamaged.class, "blockClayOre");
        OreDictionary.registerOre("oreClay", new ItemStack(blockClayOre, 1, 0));
        OreDictionary.registerOre("oreDenseClay", new ItemStack(blockClayOre, 1, 1));
        OreDictionary.registerOre("oreLargeDenseClay", new ItemStack(blockClayOre, 1, 2));


        blockDenseClay = new DenseClay();


        blockCompressedClay = (new BlockDamaged(Material.clay, 13)).setBlockTextureName("clayium:compressedclay").setBlockName("blockCompressedClay").setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(1.0F).setResistance(1.0F).setStepSound(Block.soundTypeGravel);
        blockCompressedClay.setHarvestLevel("shovel", 0);
        GameRegistry.registerBlock(blockCompressedClay, ItemBlockCompressedClay.class, "blockCompressedClay");


        blockRawClayMachineHull = (new BlockDamaged(Material.clay, 1)).setBlockTextureName("clayium:rawclaymachinehull").setBlockName("blockRawClayMachineHull").setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(1.0F).setResistance(1.0F).setStepSound(Block.soundTypeGravel);
        blockRawClayMachineHull.setHarvestLevel("shovel", 0);
        ((BlockDamaged) blockRawClayMachineHull).setTier(1);
        GameRegistry.registerBlock(blockRawClayMachineHull, ItemBlockDamaged.class, "blockRawClayMachineHull");

        blockMachineHull = new MachineHull(13);
        GameRegistry.registerBlock(blockMachineHull, ItemBlockDamaged.class, "blockMachineHull");

        blockOthersHull = new BlockDamaged(Material.iron);
        blockOthersHull.setBlockTextureName("clayium:othershull").setBlockName("blockOthersHull")
                .setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(2.0F).setResistance(2.0F)
                .setStepSound(Block.soundTypeMetal);
        blockOthersHull.addBlockList("az91d", 0).setTier(6).setSubBlockName("blockAZ91DHull")
                .setIconName("clayium:az91dhull");
        blockOthersHull.addBlockList("zk60a", 1).setTier(6).setSubBlockName("blockZK60AHull")
                .setIconName("clayium:zk60ahull");
        GameRegistry.registerBlock(blockOthersHull, ItemBlockDamaged.class, "blockOthersHull");


        blockClayWorkTable = (new ClayWorkTable()).setBlockTextureName("clayium:clayworktable").setBlockName("blockClayWorkTable").setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(2.0F).setResistance(2.0F);
        GameRegistry.registerBlock(blockClayWorkTable, ItemBlockTiered.class, "blockClayWorkTable");
        registerTileEntity((Class) TileClayWorkTable.class, "ClayWorkTable");


        blockClayCraftingTable = (new ClayCraftingTable(0)).setBlockName("blockClayCraftingTable").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayCraftingTable, ItemBlockTiered.class, "blockClayCraftingTable");
        registerTileEntity((Class) TileClayCraftingTable.class, "ClayCraftingTable");

        registerTileEntity((Class) TileClayMachines.class, "ClayMachines");

        blockElementalMillingMachine = (new ClayMachines("MillingMachine", "clayium:millingmachine", 1)).setBlockName("blockElementalMillingMachine").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockElementalMillingMachine, ItemBlockTiered.class, "blockElementalMillingMachine");

        blockEnergeticClayCondenser = (new ClayMachines("ECCondenser", "clayium:eccondenser", 3)).setBlockName("blockEnergeticClayCondenser").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockEnergeticClayCondenser, ItemBlockTiered.class, "blockEnergeticClayCondenser");

        blockEnergeticClayCondenserMK2 = (new ClayMachines("ECCondenser", "clayium:eccondenser", 4)).setBlockName("blockEnergeticClayCondenserMK2").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockEnergeticClayCondenserMK2, ItemBlockTiered.class, "blockEnergeticClayCondenserMK2");


        blocksBendingMachine = registerTieredMachines("BendingMachine", "bendingmachine", "BendingMachine", new int[] {1, 2, 3, 4, 5, 6, 7, 9});

        blocksWireDrawingMachine = registerTieredMachines("WireDrawingMachine", "wiredrawingmachine", "WireDrawingMachine", new int[] {1, 2, 3, 4});

        blocksPipeDrawingMachine = registerTieredMachines("PipeDrawingMachine", "pipedrawingmachine", "PipeDrawingMachine", new int[] {1, 2, 3, 4});

        blocksCuttingMachine = registerTieredMachines("CuttingMachine", "cuttingmachine", "CuttingMachine", new int[] {1, 2, 3, 4});

        blocksLathe = registerTieredMachines("Lathe", "lathe", "Lathe", new int[] {1, 2, 3, 4});

        blocksCobblestoneGenerator = new Block[16];
        int i;
        for (i = 1; i <= 7; i++) {
            blocksCobblestoneGenerator[i] = (Block) new CobblestoneGenerator(i);
            blocksCobblestoneGenerator[i].setBlockName("block" + tierPrefix[i] + "CobblestoneGenerator")
                    .setCreativeTab(ClayiumCore.creativeTabClayium);
            GameRegistry.registerBlock(blocksCobblestoneGenerator[i], ItemBlockTiered.class, "block" + tierPrefix[i] + "CobblestoneGenerator");
        }

        registerTileEntity((Class) TileCobblestoneGenerator.class, "CobblestoneGenerator");

        blocksCondenser = registerTieredMachines("Condenser", "condenser", "Condenser", new int[] {2, 3, 4, 5, 10});

        UtilTier.TierManager.applyMachineTierManager(blocksCondenser, UtilTier.tierGeneric);


        blocksGrinder = registerTieredMachines("Grinder", "grinder", "Grinder", new int[] {2, 3, 4, 5, 6, 10});

        UtilTier.TierManager.applyMachineTierManager(blocksGrinder, UtilTier.tierGeneric);


        blocksDecomposer = registerTieredMachines("Decomposer", "decomposer", "Decomposer", new int[] {2, 3, 4});


        blocksMillingMachine = registerTieredMachines("MillingMachine", "millingmachine", "MillingMachine", new int[] {3, 4});

        blocksAssembler = registerTieredMachines("Assembler", "assembler", "Assembler", new int[] {3, 4, 6, 10}, (Class) ClayAssembler.class, ItemBlockTiered.class);

        registerTileEntity((Class) TileClayAssembler.class, "ClayAssembler");
        blocksInscriber = registerTieredMachines("Inscriber", "inscriber", "Inscriber", new int[] {3, 4}, (Class) ClayAssembler.class, ItemBlockTiered.class);

        blocksCentrifuge = registerTieredMachines("Centrifuge", "centrifuge", "Centrifuge", new int[] {3, 4, 5, 6}, (Class) TileClayCentrifuge.class, 3);

        registerTileEntity((Class) TileClayCentrifuge.class, "ClayCentrifuge");
        UtilTier.TierManager.applyMachineTierManager(blocksCentrifuge, UtilTier.tierGeneric);


        blocksSmelter = registerTieredMachines("Smelter", "smelter", "Smelter", new int[] {4, 5, 6, 7, 8, 9});
        UtilTier.TierManager.applyMachineTierManager(blocksSmelter, UtilTier.tierSmelter);


        blocksBuffer = registerTieredContainers("Buffer", new int[] {4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, (Class) ClayBuffer.class);

        registerTileEntity((Class) TileClayBuffer.class, "ClayBuffer");
        blocksMultitrackBuffer = registerTieredContainers("MultitrackBuffer", new int[] {4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, (Class) MultitrackBuffer.class);

        registerTileEntity((Class) TileMultitrackBuffer.class, "MultitrackBuffer");


        blockCreativeCESource = (new ClayNoRecipeMachines("", "", "clayium:creativeenergy", 13, (Class) TileCreativeEnergySource.class, 11, 2)).setBlockName("blockCreativeCESource").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockCreativeCESource, ItemBlockTiered.class, "blockCreativeCESource");
        registerTileEntity((Class) TileCreativeEnergySource.class, "CreativeCESource");

        blocksChemicalReactor = registerTieredMachines("ChemicalReactor", "chemicalreactor", "ChemicalReactor", new int[] {4, 5, 8}, (Class) ClayChemicalReactor.class, ItemBlockTiered.class);

        registerTileEntity((Class) TileClayChemicalReactor.class, "ClayChemicalReactor");


        blocksSaltExtractor = registerTieredContainers("SaltExtractor", new int[] {4, 5, 6, 7}, (Class) SaltExtractor.class);

        registerTileEntity((Class) TileSaltExtractor.class, "SaltExtractor");


        blockAutoClayCondenser = (new ClayMachines("clayium:autoclaycondenser", 5, (Class) TileAutoClayCondenser.class, 5)).setBlockName("blockAutoClayCondenser").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockAutoClayCondenser, ItemBlockTiered.class, "blockAutoClayCondenser");


        blockAutoClayCondenserMK2 = (new ClayMachines("clayium:autoclaycondenser", 7, (Class) TileAutoClayCondenser.class, 5)).setBlockName("blockAutoClayCondenserMK2").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockAutoClayCondenserMK2, ItemBlockTiered.class, "blockAdvancedAutoClayCondenser");
        registerTileEntity((Class) TileAutoClayCondenser.class, "AutoClayCondenser");

        blockQuartzCrucible = (Block) new QuartzCrucible();
        blockQuartzCrucible.setBlockName("blockQuartzCrucible").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockQuartzCrucible, ItemBlockTiered.class, "blockQuartzCrucible");
        registerTileEntity((Class) TileQuartzCrucible.class, "QuartzCrucible");


        blockSolarClayFabricatorMK1 = (new SolarClayFabricator(null, 5)).setBlockName("blockSolarClayFabricatorMK1").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockSolarClayFabricatorMK1, ItemBlockTiered.class, "blockSolarClayFabricatorMK1");

        blockSolarClayFabricatorMK2 = (new SolarClayFabricator(null, 6)).setBlockName("blockSolarClayFabricatorMK2").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockSolarClayFabricatorMK2, ItemBlockTiered.class, "blockSolarClayFabricatorMK2");

        blockLithiumSolarClayFabricator = (new SolarClayFabricator(null, 7)).setBlockName("blockLithiumSolarClayFabricator").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockLithiumSolarClayFabricator, ItemBlockTiered.class, "blockLithiumSolarClayFabricator");

        registerTileEntity((Class) TileSolarClayFabricator.class, "SolarClayFabricator");

        blockClayFabricatorMK1 = (new ClayFabricator(8)).setBlockName("blockClayFabricatorMK1").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayFabricatorMK1, ItemBlockTiered.class, "blockClayFabricatorMK1");

        blockClayFabricatorMK2 = (new ClayFabricator(9)).setBlockName("blockClayFabricatorMK2").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayFabricatorMK2, ItemBlockTiered.class, "blockClayFabricatorMK2");

        blockClayFabricatorMK3 = (new ClayFabricator(13)).setBlockName("blockClayFabricatorMK3").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayFabricatorMK3, ItemBlockTiered.class, "blockClayFabricatorMK3");
        registerTileEntity((Class) TileClayFabricator.class, "ClayFabricator");


        blockChemicalMetalSeparator = (new ClayMachines("clayium:chemicalmetalseparator", 6, (Class) TileChemicalMetalSeparator.class, 6)).setBlockName("blockChemicalMetalSeparator").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockChemicalMetalSeparator, ItemBlockTiered.class, "blockChemicalMetalSeparator");
        registerTileEntity((Class) TileChemicalMetalSeparator.class, "ChemicalMetalSeparator");

        blocksAlloySmelter = registerTieredMachines("AlloySmelter", "alloysmelter", "AlloySmelter", new int[] {6}, (Class) ClayAssembler.class, ItemBlockTiered.class);


        blockClayBlastFurnace = (new ClayBlastFurnace("BlastFurnace", "clayium:blastfurnace", 6)).setBlockName("blockClayBlastFurnace").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayBlastFurnace, ItemBlockTiered.class, "blockClayBlastFurnace");
        registerTileEntity((Class) TileClayBlastFurnace.class, "ClayBlastFurnace");


        blocksClayInterface = registerTieredContainers("ClayInterface", new int[] {5, 6, 7, 8, 9, 10, 11, 12, 13}, (Class) ClayInterface.class);

        registerTileEntity((Class) TileClayContainerInterface.class, "ClayInterface");


        blocksRedstoneInterface = registerTieredContainers("RedstoneInterface", new int[] {5, 6, 7, 8, 9, 10, 11, 12, 13}, (Class) RedstoneInterface.class);

        registerTileEntity((Class) TileRedstoneInterface.class, "RedstoneInterface");

        blocksElectrolysisReactor = registerTieredMachines("ElectrolysisReactor", "electrolysisreactor", "ElectrolysisReactor", new int[] {6, 7, 8, 9});


        blocksClayLaserInterface = registerTieredContainers("LaserClayInterface", new int[] {7, 8, 9, 10, 11, 12, 13}, (Class) ClayLaserInterface.class);

        registerTileEntity((Class) TileClayLaserInterface.class, "ClayLaserInterface");


        blocksDistributor = registerTieredContainers("Distributor", new int[] {7, 8, 9}, (Class) ClayDistributor.class);

        registerTileEntity((Class) TileClayDistributor.class, "ClayDistributor");


        blocksAutoCrafter = registerTieredContainers("AutoCrafter", new int[] {5, 6, 7, 8, 9}, (Class) ClayAutoCrafter.class);

        registerTileEntity((Class) TileAutoCrafter.class, "AutoCrafter");


        blockClayReactor = (new ClayReactor("Reactor", "clayium:reactor", 7)).setBlockName("blockClayReactor").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayReactor, ItemBlockTiered.class, "blockClayReactor");
        registerTileEntity((Class) TileClayReactor.class, "ClayReactor");

        blocksTransformer = registerTieredMachines("MatterTransformer", "transformer", "Transformer", new int[] {7, 8, 9, 10, 11, 12});


        blocksCACondenser = registerTieredMachines("CACondenser", "cacondenser", "CACondenser", new int[] {9, 10, 11}, (Class) TileCACondenser.class);

        UtilTier.TierManager.applyMachineTierManager(blocksCACondenser, UtilTier.tierCACondenser);


        registerTileEntity((Class) TileCACondenser.class, "CACondenser");

        blocksCAInjector = registerTieredMachines("CAInjector", "cainjector", "CAInjector", new int[] {9, 10, 11, 12, 13}, (Class) TileCAInjector.class, (Class) ClayAssembler.class, ItemBlockTiered.class);

        registerTileEntity((Class) TileCAInjector.class, "CAInjector");


        blockCACollector = (new ClayNoRecipeMachines(null, "clayium:cacollector", 10, (Class) TileCACollector.class, 18, 1)).setBlockName("blockCACollector").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockCACollector, ItemBlockTiered.class, "blockCACollector");
        registerTileEntity((Class) TileCACollector.class, "CACollector");

        blockResonator = new BlockResonator(Material.iron);
        blockResonator.setBlockTextureName("clayium:resonator").setBlockName("blockResonator")
                .setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(2.0F).setResistance(2.0F)
                .setStepSound(Block.soundTypeMetal);
        blockResonator.addBlockList("antimatter", 10).setTier(10);
        ((BlockResonator) blockResonator).addResonance(1.08D);
        blockResonator.addBlockList("pureantimatter", 11).setTier(11);
        ((BlockResonator) blockResonator).addResonance(1.1D);
        blockResonator.addBlockList("oec", 12).setTier(12);
        ((BlockResonator) blockResonator).addResonance(2.0D);
        blockResonator.addBlockList("opa", 13).setTier(13);
        ((BlockResonator) blockResonator).addResonance(20.0D);
        GameRegistry.registerBlock(blockResonator, ItemBlockDamaged.class, "blockResonator");

        blockOverclocker = new BlockOverclocker(Material.iron);
        blockOverclocker.setBlockTextureName("clayium:overclocker").setBlockName("blockOverclocker")
                .setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(2.0F).setResistance(2.0F)
                .setStepSound(Block.soundTypeMetal);
        blockOverclocker.addBlockList("antimatter", 10).setTier(10);
        ((BlockOverclocker) blockOverclocker).addOverclockFactor(1.5D);
        blockOverclocker.addBlockList("pureantimatter", 11).setTier(11);
        ((BlockOverclocker) blockOverclocker).addOverclockFactor(2.3D);
        blockOverclocker.addBlockList("oec", 12).setTier(12);
        ((BlockOverclocker) blockOverclocker).addOverclockFactor(3.5D);
        blockOverclocker.addBlockList("opa", 13).setTier(13);
        ((BlockOverclocker) blockOverclocker).addOverclockFactor(5.0D);
        GameRegistry.registerBlock(blockOverclocker, ItemBlockDamaged.class, "blockOverclocker");

        blockEnergyStorageUpgrade = new BlockEnergyStorageUpgrade(Material.iron);
        blockEnergyStorageUpgrade.setBlockTextureName("clayium:estorageupgrade").setBlockName("blockEnergyStorageUpgrade")
                .setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(2.0F).setResistance(2.0F)
                .setStepSound(Block.soundTypeMetal);
        blockEnergyStorageUpgrade.addBlockList("antimatter", 10).setTier(10);
        ((BlockEnergyStorageUpgrade) blockEnergyStorageUpgrade).addAdditionalEnergyStorage(1);
        blockEnergyStorageUpgrade.addBlockList("pureantimatter", 11).setTier(11);
        ((BlockEnergyStorageUpgrade) blockEnergyStorageUpgrade).addAdditionalEnergyStorage(3);
        blockEnergyStorageUpgrade.addBlockList("oec", 12).setTier(12);
        ((BlockEnergyStorageUpgrade) blockEnergyStorageUpgrade).addAdditionalEnergyStorage(7);
        blockEnergyStorageUpgrade.addBlockList("opa", 13).setTier(13);
        ((BlockEnergyStorageUpgrade) blockEnergyStorageUpgrade).addAdditionalEnergyStorage(63);
        GameRegistry.registerBlock(blockEnergyStorageUpgrade, ItemBlockDamaged.class, "blockEnergyStorageUpgrade");

        blockCAReactorCoil = new BlockDamaged(Material.iron) {
            public List getTooltip(ItemStack itemStack) {
                List ret = UtilLocale.localizeTooltip("tooltip.CAReactorCoil");
                ret.addAll(super.getTooltip(itemStack));
                return ret;
            }
        };
        blockCAReactorCoil.setBlockTextureName("clayium:careactorcoil").setBlockName("blockCAReactorCoil")
                .setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(8.0F).setResistance(5.0F)
                .setStepSound(Block.soundTypeMetal);
        blockCAReactorCoil.addBlockList("antimatter", 10).setTier(10);
        blockCAReactorCoil.addBlockList("pureantimatter", 11).setTier(11);
        blockCAReactorCoil.addBlockList("oec", 12).setTier(12);
        blockCAReactorCoil.addBlockList("opa", 13).setTier(13);
        GameRegistry.registerBlock(blockCAReactorCoil, ItemBlockDamaged.class, "blockCAReactorCoil");

        blockCAReactorHull = new BlockDamaged(Material.iron, 10);
        blockCAReactorHull.setBlockTextureName("clayium:careactorhull").setBlockName("blockCAReactorHull")
                .setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(4.0F).setResistance(25.0F)
                .setStepSound(Block.soundTypeMetal);
        blockCAReactorHull.setTier("0", 10);
        blockCAReactorHull.setTier("1", 11);
        blockCAReactorHull.setTier("2", 11);
        blockCAReactorHull.setTier("3", 11);
        blockCAReactorHull.setTier("4", 11);
        blockCAReactorHull.setTier("5", 12);
        blockCAReactorHull.setTier("6", 12);
        blockCAReactorHull.setTier("7", 12);
        blockCAReactorHull.setTier("8", 12);
        blockCAReactorHull.setTier("9", 13);
        GameRegistry.registerBlock(blockCAReactorHull, ItemBlockDamaged.class, "blockCAReactorHull");

        blocksCAReactorCore = registerTieredMachines("CAReactor", "careactorcore", "CAReactorCore", new int[] {10, 11, 12, 13}, (Class) TileCAReactor.class, 9, (Class) CAReactor.class, ItemBlockTiered.class);


        registerTileEntity((Class) TileCAReactor.class, "CAReactor");


        blockEnergeticClayDecomposer = (new ClayMachines("ECDecomposer", "clayium:ecdecomposer", 13)).setBlockName("blockEnergeticClayDecomposer").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockEnergeticClayDecomposer, ItemBlockTiered.class, "blockEnergeticClayDecomposer");


        blockStorageContainer = (new StorageContainer(Material.iron, "clayium:az91dhull")).setBlockName("blockStorageContainer").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockStorageContainer, ItemBlockTiered.class, "blockStorageContainer");
        registerTileEntity((Class) TileStorageContainer.class, "StorageContainer");

        blockVacuumContainer = (new VacuumContainer(Material.iron, "clayium:az91dhull")).setBlockName("blockVacuumContainer").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockVacuumContainer, ItemBlockTiered.class, "blockVacuumContainer");
        registerTileEntity((Class) TileVacuumContainer.class, "VacuumContainer");


        blockAutoTrader = (new AutoTrader(8)).setBlockName("blockAutoTrader").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockAutoTrader, ItemBlockTiered.class, "blockAutoTrader");
        registerTileEntity((Class) TileAutoTrader.class, "AutoTrader");


        blockClayMarker = (new ClayMarker(7, Blocks.clay, (Class) TileClayMarker.class)).setBlockName("blockClayMarker").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayMarker, ItemBlockTiered.class, "blockClayMarker");
        registerTileEntity((Class) TileClayMarker.class, "ClayMarker");

        blockClayOpenPitMarker = (new ClayMarker(8, blockCompressedClay, (Class) TileClayOpenPitMarker.class)).setBlockName("blockClayOpenPitMarker").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayOpenPitMarker, ItemBlockTiered.class, "blockClayOpenPitMarker");
        registerTileEntity((Class) TileClayOpenPitMarker.class, "ClayOpenPitMarker");

        blockClayGroundLevelingMarker = (new ClayMarker(8, blockCompressedClay, 1, (Class) TileClayOpenPitMarker.TileClayGroundLevelingMarker.class)).setBlockName("blockClayGroundLevelingMarker").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayGroundLevelingMarker, ItemBlockTiered.class, "blockClayGroundLevelingMarker");
        registerTileEntity((Class) TileClayOpenPitMarker.TileClayGroundLevelingMarker.class, "ClayGroundLevelingMarker");

        blockClayPrismMarker = (new ClayMarker(8, blockCompressedClay, 2, (Class) TileClayOpenPitMarker.TileClayPrismMarker.class)).setBlockName("blockClayPrismMarker").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayPrismMarker, ItemBlockTiered.class, "blockClayPrismMarker");
        registerTileEntity((Class) TileClayOpenPitMarker.TileClayPrismMarker.class, "ClayPrismMarker");


        blockMiner = (new AreaMiner(6, "clayium:areaminer")).setBlockName("blockMiner").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockMiner, ItemBlockTiered.class, "blockMiner");
        registerTileEntity((Class) TileAreaMiner.class, "AreaMiner");

        blockAreaCollector = (new AreaCollector(7)).setBlockName("blockAreaCollector").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockAreaCollector, ItemBlockTiered.class, "blockAreaCollector");
        registerTileEntity((Class) TileAreaCollector.class, "AreaCollector");

        blockAreaMiner = (new AreaMiner(8, "clayium:areaminer")).setBlockName("blockAreaMiner").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockAreaMiner, ItemBlockTiered.class, "blockAreaMiner");

        blockAdvancedAreaMiner = (new AreaMiner(9, "clayium:advareaminer")).setBlockName("blockAdvancedAreaMiner").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockAdvancedAreaMiner, ItemBlockTiered.class, "blockAdvancedAreaMiner");

        blockAreaReplacer = (new AreaMiner(10, "clayium:areareplacer")).setBlockName("blockAreaReplacer").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockAreaReplacer, ItemBlockTiered.class, "blockAreaReplacer");


        blockActivator = (new AreaActivator(6)).setBlockName("blockActivator").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockActivator, ItemBlockTiered.class, "blockActivator");

        blockAreaActivator = (new AreaActivator(8)).setBlockName("blockAreaActivator").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockAreaActivator, ItemBlockTiered.class, "blockAreaActivator");
        registerTileEntity((Class) TileAreaActivator.class, "AreaActivator");


        blockClayWaterWheel = (new WaterWheel("Clay Water Wheel", "clayium:waterwheel", 1)).setBlockName("blockClayWaterWheel").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayWaterWheel, ItemBlockTiered.class, "blockClayWaterWheel");

        blockDenseClayWaterWheel = (new WaterWheel("Dense Clay Water Wheel", "clayium:waterwheel", 2)).setBlockName("blockDenseClayWaterWheel").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockDenseClayWaterWheel, ItemBlockTiered.class, "blockDenseClayWaterWheel");
        registerTileEntity((Class) TileWaterWheel.class, "WaterWheel");


        blocksClayEnergyLaser = registerTieredContainers("ClayEnergyLaser", new int[] {7, 8, 9, 10}, (Class) ClayEnergyLaser.class);

        registerTileEntity((Class) TileClayEnergyLaser.class, "ClayEnergyLaser");

        blockLaserReflector = (new LaserReflector()).setBlockName("blockLaserReflector").setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(1.0F).setResistance(1.0F);
        GameRegistry.registerBlock(blockLaserReflector, ItemBlockTiered.class, "blockLaserReflector");
        registerTileEntity((Class) TileLaserReflector.class, "LaserReflector");


        blockClayTreeSapling = (new ClayTreeSapling()).setBlockName("blockClayTreeSapling").setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("blockClayTreeSapling").setBlockTextureName("clayium:sapling_claytree");
        GameRegistry.registerBlock(blockClayTreeSapling, ItemBlockTiered.class, "blockClayTreeSapling");


        blockClayTreeLeaf = (new ClayTreeLeaf()).setBlockName("blockClayTreeLeaf").setBlockTextureName("clayium:leaves");
        GameRegistry.registerBlock(blockClayTreeLeaf, ItemBlockTiered.class, "blockClayTreeLeaf");

        blockClayTreeLog = (new ClayTreeLog()).setBlockName("blockClayTreeLog").setBlockTextureName("clayium:log");
        GameRegistry.registerBlock(blockClayTreeLog, ItemBlockTiered.class, "blockClayTreeLog");

        OreDictionary.registerOre("logWood", blockClayTreeLog);
        OreDictionary.registerOre("treeLeaves", blockClayTreeLeaf);
        OreDictionary.registerOre("treeSapling", blockClayTreeSapling);
        OreDictionary.registerOre("saplingTree", blockClayTreeSapling);


        blockClayChunkLoader = (new ClayChunkLoader(6)).setBlockName("blockClayChunkLoader").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockClayChunkLoader, ItemBlockTiered.class, "blockClayChunkLoader");
        registerTileEntity((Class) TileClayChunkLoader.class, "ClayChunkLoader");


        blockPANCore = (new PANCore()).setBlockName("blockPANCore").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockPANCore, ItemBlockTiered.class, "blockPANCore");
        registerTileEntity((Class) TilePANCore.class, "PANCore");
        blocksPANAdapter = registerTieredContainers("PANAdapter", new int[] {10, 11, 12, 13}, (Class) PANAdapter.class);

        registerTileEntity((Class) TilePANAdapter.class, "PANAdapter");
        blocksPANDuplicator = registerTieredContainers("PANDuplicator", new int[] {4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, (Class) PANDuplicator.class);

        for (i = 4; i <= 13; i++) {
            ((PANDuplicator) blocksPANDuplicator[i]).multConsumingEnergy = (float) Math.pow(10.0D, (i - 5));
        }
        registerTileEntity((Class) TilePANDuplicator.class, "PANDuplicator");

        blockPANCable = (new PANCable()).setBlockName("blockPANCable").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockPANCable, ItemBlockTiered.class, "blockPANCable");


        blockMetalChest = (new MetalChest()).setBlockName("blockMetalChest").setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockMetalChest, ItemBlockDamaged.class, "blockMetalChest");
        registerTileEntity((Class) TileMetalChest.class, "MetalChest");

        if (ClayiumCore.cfgEnableFluidCapsule) {
            blocksFluidTranslator = registerTieredContainers("FluidTranslator", new int[] {4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, (Class) FluidTranslator.class);

            registerTileEntity((Class) TileFluidTranslator.class, "FluidTranslator");

            blocksFluidTransferMachine = registerTieredMachines("FluidTransferMachine", "fluidtransfermachine", "FluidTransferMachine", new int[] {5}, (Class) ClayChemicalReactor.class, ItemBlockTiered.class);
        }


        blockMaterial = new BlockDamaged(Material.iron);
        blockMaterial.setBlockName("blockMaterial")
                .setCreativeTab(ClayiumCore.creativeTabClayium).setHardness(2.0F).setResistance(2.0F)
                .setStepSound(Block.soundTypeMetal);
        blockMaterial.addBlockList("ImpureSilicon", 0).setTier(5).setSubBlockName("blockImpureSilicon")
                .setIconName("clayium:impuresilicon");
        blockMaterial.addBlockList("Silicone", 1).setTier(5).setSubBlockName("blockSilicone")
                .setIconName("clayium:silicone");
        blockMaterial.addBlockList("Silicon", 2).setTier(5).setSubBlockName("blockSilicon")
                .setIconName("clayium:silicon");
        blockMaterial.addBlockList("Aluminium", 3).setTier(6).setSubBlockName("blockAluminium")
                .setIconName("clayium:aluminium");
        blockMaterial.addBlockList("ClaySteel", 4).setTier(7).setSubBlockName("blockClaySteel")
                .setIconName("clayium:claysteel");
        blockMaterial.addBlockList("Clayium", 5).setTier(8).setSubBlockName("blockClayium")
                .setIconName("clayium:clayium");
        blockMaterial.addBlockList("UltimateAlloy", 6).setTier(9).setSubBlockName("blockUltimateAlloy")
                .setIconName("clayium:ultimatealloy");
        blockMaterial.addBlockList("Antimatter", 7).setTier(10).setSubBlockName("blockAntimatter")
                .setIconName("clayium:antimatter");
        blockMaterial.addBlockList("PureAntimatter", 8).setTier(11).setSubBlockName("blockPureAntimatter")
                .setIconName("clayium:pureantimatter");
        blockMaterial.addBlockList("OctupleEnergeticClay", 9).setTier(12).setSubBlockName("blockOctupleEnergeticClay")
                .setIconName("clayium:oec");
        blockMaterial.addBlockList("OctuplePureAntimatter", 10).setTier(13).setSubBlockName("blockOctuplePureAntimatter")
                .setIconName("clayium:opa");
        GameRegistry.registerBlock(blockMaterial, ItemBlockDamaged.class, "blockMaterial");

        blockSiliconeColored = new BlockSiliconeColored();
        blockSiliconeColored.setCreativeTab(ClayiumCore.creativeTabClayium);
        GameRegistry.registerBlock(blockSiliconeColored, ItemBlockDamaged.class, "blockSiliconeColored");

        registerTileEntity((Class) TileFluidTab.class, "FluidTab");

        blocksRFGenerator = new HashMap<String, Block>();
        Map<String, Map<String, Object>> configMap = ClayRFGenerator.getConfigMap();
        if (configMap != null) {
            for (Map.Entry<String, Map<String, Object>> entry : configMap.entrySet()) {
                String blockName = entry.getKey();
                Map<String, Object> config = entry.getValue();
                Object obj = config.get("IconName");
                if (!(obj instanceof String))
                    continue;
                String iconName = (String) obj;
                obj = config.get("Tier");
                if (!(obj instanceof Integer))
                    continue;
                int tier = ((Integer) obj).intValue();


                Block block = (new ClayRFGenerator(blockName, iconName, tier)).setBlockName("block" + blockName).setHardness(2.0F).setResistance(2.0F).setCreativeTab(ClayiumCore.creativeTabClayium);
                GameRegistry.registerBlock(block, ItemBlockTiered.class, "block" + blockName);
                blocksRFGenerator.put(blockName, block);
            }
        }
        registerTileEntity((Class) TileClayRFGenerator.class, "RFGenerator");
    }

    public static Block[] blocksRedstoneInterface;
    public static Block[] blocksClayLaserInterface;
    public static Block[] blocksElectrolysisReactor;
    public static Block[] blocksClayEnergyLaser;
    public static Block[] blocksDistributor;
    public static Block blockLaserReflector;
    public static Block blockClayReactor;
    public static Block[] blocksTransformer;
    public static Block[] blocksCACondenser;
    public static Block[] blocksCAInjector;
    public static Block blockCACollector;
    public static BlockDamaged blockResonator;
    public static BlockDamaged blockEnergyStorageUpgrade;
    public static BlockDamaged blockOverclocker;
    public static BlockDamaged blockCAReactorCoil;
    public static BlockDamaged blockCAReactorHull;
    public static Block[] blocksCAReactorCore;
    public static Block blockEnergeticClayDecomposer;
    public static Block blockCreativeCESource;
    public static Block blockClayOre;
    public static Block blockClayTreeSapling;
    public static Block blockClayTreeLeaf;
    public static Block blockClayTreeLog;
    public static Block blockClayChunkLoader;
    public static Block blockStorageContainer;
    public static Block blockVacuumContainer;
    public static Block blockAutoTrader;
    public static Block blockClayMarker;
    public static Block blockClayOpenPitMarker;
    public static Block blockClayGroundLevelingMarker;
    public static Block blockClayPrismMarker;
    public static Block blockAreaCollector;
    public static Block blockMiner;
    public static Block blockAreaMiner;
    public static Block blockAdvancedAreaMiner;
    public static Block blockAreaReplacer;
    public static Block blockActivator;
    public static Block blockAreaActivator;
    public static Block blockMetalChest;
    public static Block[] blocksFluidTranslator;
    public static Block[] blocksFluidTransferMachine;
    public static Block blockPANCore;
    public static Block[] blocksPANAdapter;
    public static Block[] blocksPANDuplicator;
    public static Block blockPANCable;
    public static Map<String, Block> blocksRFGenerator;

    public static Block[] registerTieredContainers(String blockName, int[] tiers, Class<? extends ClayContainerTiered> containerClass, Class<? extends ItemBlockTiered> itemBlockClass) {
        Block[] res = new Block[tierPrefix.length];
        for (int i = 0; i < tiers.length; i++) {
            if (tiers[i] >= 0 && tiers[i] < tierPrefix.length) {
                try {
                    res[tiers[i]] = containerClass.getConstructor(new Class[] {int.class}).newInstance(new Object[] {Integer.valueOf(tiers[i])});
                } catch (InstantiationException e) {
                    ClayiumCore.logger.catching(e);
                } catch (IllegalAccessException e) {
                    ClayiumCore.logger.catching(e);
                } catch (IllegalArgumentException e) {
                    ClayiumCore.logger.catching(e);
                } catch (InvocationTargetException e) {
                    ClayiumCore.logger.catching(e);
                } catch (NoSuchMethodException e) {
                    ClayiumCore.logger.catching(e);
                } catch (SecurityException e) {
                    ClayiumCore.logger.catching(e);
                }
                res[tiers[i]].setBlockName("block" + tierPrefix[tiers[i]] + blockName)
                        .setCreativeTab(ClayiumCore.creativeTabClayium);
                GameRegistry.registerBlock(res[tiers[i]], itemBlockClass, "block" + tierPrefix[tiers[i]] + blockName);
            }
        }

        return res;
    }


    public static Block[] registerTieredContainers(String blockName, int[] tiers, Class<? extends ClayContainerTiered> containerClass) {
        return registerTieredContainers(blockName, tiers, containerClass, ItemBlockTiered.class);
    }


    public static Block[] registerTieredMachines(String recipeId, String icon, String blockName, int[] tiers, Class<? extends ClayMachines> machineClass, Class<? extends ItemBlockTiered> itemBlockClass) {
        Block[] res = new Block[tierPrefix.length];
        for (int i = 0; i < tiers.length; i++) {
            if (tiers[i] >= 0 && tiers[i] < tierPrefix.length) {
                try {
                    res[tiers[i]] = machineClass.getConstructor(new Class[] {String.class, String.class, int.class
                    }).newInstance(new Object[] {recipeId, "clayium:" + icon, Integer.valueOf(tiers[i])});
                } catch (InstantiationException e) {
                    ClayiumCore.logger.catching(e);
                } catch (IllegalAccessException e) {
                    ClayiumCore.logger.catching(e);
                } catch (IllegalArgumentException e) {
                    ClayiumCore.logger.catching(e);
                } catch (InvocationTargetException e) {
                    ClayiumCore.logger.catching(e);
                } catch (NoSuchMethodException e) {
                    ClayiumCore.logger.catching(e);
                } catch (SecurityException e) {
                    ClayiumCore.logger.catching(e);
                }
                res[tiers[i]].setBlockName("block" + tierPrefix[tiers[i]] + blockName)
                        .setCreativeTab(ClayiumCore.creativeTabClayium);
                GameRegistry.registerBlock(res[tiers[i]], itemBlockClass, "block" + tierPrefix[tiers[i]] + blockName);
            }
        }

        return res;
    }

    public static Block[] registerTieredMachines(String recipeId, String icon, String blockName, int[] tiers) {
        return registerTieredMachines(recipeId, icon, blockName, tiers, ClayMachines.class, ItemBlockTiered.class);
    }


    public static Block[] registerTieredMachines(String recipeId, String icon, String blockName, int[] tiers, Class<? extends TileClayContainer> tileEntityClass, Class<? extends ClayMachines> machineClass, Class<? extends ItemBlockTiered> itemBlockClass) {
        Block[] res = new Block[tierPrefix.length];
        for (int i = 0; i < tiers.length; i++) {
            if (tiers[i] >= 0 && tiers[i] < tierPrefix.length) {
                try {
                    res[tiers[i]] = machineClass.getConstructor(new Class[] {String.class, String.class, int.class, Class.class
                    }).newInstance(new Object[] {recipeId, "clayium:" + icon, Integer.valueOf(tiers[i]), tileEntityClass});
                } catch (InstantiationException e) {
                    ClayiumCore.logger.catching(e);
                } catch (IllegalAccessException e) {
                    ClayiumCore.logger.catching(e);
                } catch (IllegalArgumentException e) {
                    ClayiumCore.logger.catching(e);
                } catch (InvocationTargetException e) {
                    ClayiumCore.logger.catching(e);
                } catch (NoSuchMethodException e) {
                    ClayiumCore.logger.catching(e);
                } catch (SecurityException e) {
                    ClayiumCore.logger.catching(e);
                }
                res[tiers[i]].setBlockName("block" + tierPrefix[tiers[i]] + blockName)
                        .setCreativeTab(ClayiumCore.creativeTabClayium);
                GameRegistry.registerBlock(res[tiers[i]], itemBlockClass, "block" + tierPrefix[tiers[i]] + blockName);
            }
        }

        return res;
    }


    public static Block[] registerTieredMachines(String recipeId, String icon, String blockName, int[] tiers, Class<? extends TileClayContainer> tileEntityClass) {
        return registerTieredMachines(recipeId, icon, blockName, tiers, tileEntityClass, ClayMachines.class, ItemBlockTiered.class);
    }


    public static Block[] registerTieredMachines(String recipeId, String icon, String blockName, int[] tiers, Class<? extends TileClayContainer> tileEntityClass, int guiId, Class<? extends ClayMachines> machineClass, Class<? extends ItemBlockTiered> itemBlockClass) {
        Block[] res = new Block[tierPrefix.length];
        for (int i = 0; i < tiers.length; i++) {
            if (tiers[i] >= 0 && tiers[i] < tierPrefix.length) {
                try {
                    res[tiers[i]] = machineClass
                            .getConstructor(new Class[] {String.class, String.class, int.class, Class.class, int.class
                            }).newInstance(new Object[] {recipeId, "clayium:" + icon, Integer.valueOf(tiers[i]), tileEntityClass, Integer.valueOf(guiId)});
                } catch (InstantiationException e) {
                    ClayiumCore.logger.catching(e);
                } catch (IllegalAccessException e) {
                    ClayiumCore.logger.catching(e);
                } catch (IllegalArgumentException e) {
                    ClayiumCore.logger.catching(e);
                } catch (InvocationTargetException e) {
                    ClayiumCore.logger.catching(e);
                } catch (NoSuchMethodException e) {
                    ClayiumCore.logger.catching(e);
                } catch (SecurityException e) {
                    ClayiumCore.logger.catching(e);
                }
                res[tiers[i]].setBlockName("block" + tierPrefix[tiers[i]] + blockName)
                        .setCreativeTab(ClayiumCore.creativeTabClayium);
                GameRegistry.registerBlock(res[tiers[i]], itemBlockClass, "block" + tierPrefix[tiers[i]] + blockName);
            }
        }

        return res;
    }


    public static Block[] registerTieredMachines(String recipeId, String icon, String blockName, int[] tiers, Class<? extends TileClayContainer> tileEntityClass, int guiId) {
        return registerTieredMachines(recipeId, icon, blockName, tiers, tileEntityClass, guiId, ClayMachines.class, ItemBlockTiered.class);
    }


    public static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id) {
        if (ClayiumCore.cfgEnableAlternativeTileEntityName) {
            GameRegistry.registerTileEntityWithAlternatives(tileEntityClass, "clayiumTile" + id, new String[] {"Tile" + id, "tile" + id});
        } else {
            GameRegistry.registerTileEntityWithAlternatives(tileEntityClass, "clayiumTile" + id, new String[0]);
        }
    }

    public static final String[] tierPrefix = new String[] {"", "Clay", "DenseClay", "Simple", "Basic", "Advanced", "Precision", "ClaySteel", "Clayium", "Ultimate", "Antimatter", "PureAntimatter", "OEC", "OPA"};
}
