package mods.clayium.block;

import mods.clayium.block.common.BlockDamaged;
import mods.clayium.block.common.BlockTiered;
import mods.clayium.block.common.ClayiumBlock;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.machine.ClayCraftingTable.ClayCraftingTable;
import mods.clayium.machine.ClayWorkTable.ClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ClayiumMachine;
import mods.clayium.machine.common.ClayMachineTempTiered;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.*;

public class ClayiumBlocks {
    public static void initBlocks() {
        blocks.clear();
        items.clear();

        try {
            for (Field field : ClayiumBlocks.class.getFields()) {
                if (field.get(instance) instanceof Block) {
                    Block block = (Block) field.get(instance);

                    blocks.add(block);
                    items.add(block.getItemDropped(block.getDefaultState(), new Random(), 0).setRegistryName(block.getRegistryName()));
                }
                if (field.get(instance) instanceof BlockDamaged) {
                    for (Block _block : (BlockDamaged) field.get(instance)) {
                        if (_block instanceof ClayiumBlock) {
                            blocks.add(_block);
                            items.add(_block.getItemDropped(_block.getDefaultState(), new Random(), 0).setRegistryName(_block.getRegistryName()));
                        }
                    }
                }
                if (field.get(instance) == machineMap) {
                    for (Map.Entry<EnumMachineKind, Map<ClayiumBlocks.TierPrefix, ClayMachineTempTiered>> kinds : ClayiumBlocks.machineMap.entrySet()) {
                        for (Map.Entry<ClayiumBlocks.TierPrefix, ClayMachineTempTiered> tiers : kinds.getValue().entrySet()) {
                            blocks.add(tiers.getValue());
                            items.add(tiers.getValue().getItemDropped(tiers.getValue().getDefaultState(), new Random(), 0).setRegistryName(tiers.getValue().getRegistryName()));
                        }
                    }
                }
            }
        } catch (IllegalAccessException ignore) {}
    }

    public static List<Block> getBlocks() {
        return blocks;
    }

    public static List<Item> getItems() {
        return items;
    }

    private static final ClayiumBlocks instance = new ClayiumBlocks();

    /* Elements... */
    /* Ores... */
    public static final Block clayOre = new ClayOre();
    public static final Block denseClayOre = new DenseClayOre();
    public static final Block largeDenseClayOre = new LargeDenseClayOre();
    /* ...Ores */

    public static final BlockDamaged compressedClays = new BlockDamaged() {{
        add(Blocks.CLAY);
        for (int i = 0; i < 13; i++) {
            add(new CompressedClay(i));
        }
    }};

    public static final BlockDamaged CAReactorHull = new BlockDamaged() {{
        int[] tiers = { 10, 11, 11, 11, 11, 12, 12, 12, 12, 13 };
        for (int i = 0; i < 10; i++) {
            add(new BlockTiered(Material.IRON, "ca_reactor_hull_", i, tiers[i]) {{
                setSoundType(SoundType.METAL);
                setHarvestLevel("pickaxe", 0);
                setHardness(4.0F);
                setResistance(25.0F);
            }});
        }
    }};

    /* Machine Hulls... */
    public static final Block rawClayMachineHull = new RawClayMachineHull();

    public static final BlockDamaged machineHulls = new BlockDamaged() {{
        for (int i = 0; i < 13; i++) {
            add(new BlockTiered(Material.IRON, "machine_hull_", i, i + 1) {{
                setSoundType(SoundType.METAL);
                setHarvestLevel("pickaxe", 0);
                setHardness(2F);
                setResistance(5F);
            }});
        }
    }};

    public static final Block AZ91DAlloyHull = new AZ91DAlloyHull();
    public static final Block ZK60AAlloyHull = new ZK60AAlloyHull();
    /* ...Machine Hulls */

    /* Machines... */
    public enum TierPrefix {
        none("", ClayiumMaterial.clay),
        clay("clay", ClayiumMaterial.clay),
        denseClay("dense_clay", ClayiumMaterial.denseClay),
        simple("simple", ClayiumMaterial.indClay),
        basic("basic", ClayiumMaterial.advClay),
        advanced("advanced", ClayiumMaterial.impureSilicon),
        precision("precision", ClayiumMaterial.mainAluminum),
        claySteel("clay_steel", ClayiumMaterial.claySteel),
        clayium("clayium", ClayiumMaterial.clayium),
        ultimate("ultimate", ClayiumMaterial.ultimateAlloy),
        antimatter("antimatter", ClayiumMaterial.antimatter),
        pureAntimatter("pure_antimatter", ClayiumMaterial.pureAntimatter),
        OEC("oec", ClayiumMaterial.octupleEnergeticClay),
        OPA("opa", ClayiumMaterial.octuplePureAntimatter);

        TierPrefix(String prefix, ClayiumMaterial material) {
            this.prefix = prefix;
            this.material = material;
        }

        public String getPrefix() {
            return prefix;
        }
        public ClayiumMaterial getMaterial() {
            return material;
        }

        public static TierPrefix get(int tier) {
            switch (tier) {
                case 0: return none;
                case 1: return clay;
                case 2: return denseClay;
                case 3: return simple;
                case 4: return basic;
                case 5: return advanced;
                case 6: return precision;
                case 7: return claySteel;
                case 8: return clayium;
                case 9: return ultimate;
                case 10: return antimatter;
                case 11: return pureAntimatter;
                case 12: return OEC;
                case 13: return OPA;
            }

            ClayiumCore.logger.error(new IllegalAccessException());
            return none;
        }

        private final String prefix;
        private final ClayiumMaterial material;
    }

    public enum EnumMachineKind {
        // Tier 0
        workTable("work_table", ClayiumRecipes.clayWorkTable),
        craftingTable("crafting_table", null),

        // Tier 1
        bendingMachine("bending_machine", ClayiumRecipes.bendingMachine),
        wireDrawingMachine("wire_drawing_machine", ClayiumRecipes.wireDrawingMachine),
        pipeDrawingMachine("pipe_drawing_machine", ClayiumRecipes.pipeDrawingMachine),
        cuttingMachine("cutting_machine", ClayiumRecipes.cuttingMachine),
        lathe("lathe", ClayiumRecipes.lathe),
        millingMachine("milling_machine", ClayiumRecipes.millingMachine),
        cobblestoneGenerator("cobblestone_generator", null),
        waterWheel("water_wheel", null),

        // Tier 2
        condenser("condenser", ClayiumRecipes.condenser),
        grinder("grinder", ClayiumRecipes.grinder),
        decomposer("decomposer", ClayiumRecipes.decomposer),

        // Tier 3
        assembler("assembler", ClayiumRecipes.assembler),
        inscriber("inscriber", ClayiumRecipes.inscriber),
        centrifuge("centrifuge", ClayiumRecipes.centrifuge),
        ECCondenser("ec_condenser", ClayiumRecipes.energeticClayCondenser),

        // Tier 4
        smelter("smelter", ClayiumRecipes.smelter),
        buffer("buffer", null),
        multitrackBuffer("multitrack_buffer", null),
        chemicalReactor("chemical_reactor", ClayiumRecipes.chemicalReactor),
        saltExtractor("salt_extractor", null),
        fluidTranslator("fluid_translator", null),

        // Tier 5
        autoClayCondenser("auto_clay_condenser", null),
        quartzCrucible("quartz_crucible", null),
        solarClayFabricator("solar_clay_fabricator", null),
        clayInterface("clay_interface", null),
        redstoneInterface("redstone_interface", null),
        autoCrafter("auto_crafter", null),
        fluidTransferMachine("fluid_transfer_machine", ClayiumRecipes.fluidTransferMachine),

        // Tier 6
        alloySmelter("alloy_smelter", ClayiumRecipes.alloySmelter),
        chemicalMetalSeparator("chemical_metal_separator", null),
        blastFurnace("blast_furnace", ClayiumRecipes.blastFurnace),
        electrolysisReactor("electrolysis_reactor", ClayiumRecipes.electrolysisReactor),
        clayChunkLoader("clay_chunk_loader", null),

        // Tier 7
        distributor("distributor", null),
        laserInterface("laser_interface", null),
        reactor("reactor", ClayiumRecipes.reactor),
        transformer("matter_transformer", ClayiumRecipes.transformer),
        clayEnergyLaser("clay_energy_laser", null),
        laserReflector("laser_reflector", null),
        claySapling("clay_sapling", null),

        // Tier 8
        clayFabricator("clay_fabricator", null),

        // Tier 9
        CACondenser("ca_condenser", ClayiumRecipes.CACondenser),
        CAInjector("ca_injector", ClayiumRecipes.CAInjector),
        CACollector("ca_collector", null),

        // Tier 10
        CAReactorCore("ca_reactor", ClayiumRecipes.CAReactor),

        // Tier 11
        PANCore("pan_core", null),
        PANAdapter("pan_adapter", null),
        PANDuplicator("pan_duplicator", null),
        PANCable("pan_cable", null),

        // Tier 13
        ECDecomposer("ec_decomposer", ClayiumRecipes.energeticClayDecomposer),

        // Metal Chest
        metalChest("metal_chest", null)
        ;

        EnumMachineKind(String kind, ClayiumRecipe recipe) {
            this.kind = kind;
            this.recipe = recipe;
        }

        public String get() {
            return kind;
        }
        public ClayiumRecipe getRecipe() {
            return recipe;
        }

        private final String kind;
        private final ClayiumRecipe recipe;
    }

    public static final Map<EnumMachineKind, Map<TierPrefix, ClayMachineTempTiered>> machineMap = new HashMap<>();
    static {
        for (EnumMachineKind kind : EnumMachineKind.values()) {
            machineMap.put(kind, new HashMap<>());
        }

        /* Tier 0... */
        machineMap.get(EnumMachineKind.workTable).put(TierPrefix.none, new ClayWorkTable());
        machineMap.get(EnumMachineKind.craftingTable).put(TierPrefix.none, new ClayCraftingTable());
        /* ...Tier 0 */

        /* Tier 1... */
        for (EnumMachineKind kind : new EnumMachineKind[] {
                EnumMachineKind.bendingMachine,
                EnumMachineKind.wireDrawingMachine,
                EnumMachineKind.pipeDrawingMachine,
                EnumMachineKind.cuttingMachine,
                EnumMachineKind.lathe,
                EnumMachineKind.cobblestoneGenerator,

                EnumMachineKind.millingMachine,
                EnumMachineKind.waterWheel
        }) {
            machineMap.get(kind).put(TierPrefix.clay, new ClayiumMachine(kind, 1));
        }
        /* ...Tier 1 */

        /* Tier 2... */
        for (EnumMachineKind kind : new EnumMachineKind[] {
                EnumMachineKind.bendingMachine,
                EnumMachineKind.wireDrawingMachine,
                EnumMachineKind.pipeDrawingMachine,
                EnumMachineKind.cuttingMachine,
                EnumMachineKind.lathe,
                EnumMachineKind.cobblestoneGenerator,

                EnumMachineKind.grinder,
                EnumMachineKind.decomposer,
                EnumMachineKind.condenser,

                EnumMachineKind.waterWheel
        }) {
            machineMap.get(kind).put(TierPrefix.denseClay, new ClayiumMachine(kind, 2));
        }
        /* ...Tier 2 */

        /* Tier 3... */
        for (EnumMachineKind kind : new EnumMachineKind[] {
                EnumMachineKind.bendingMachine,
                EnumMachineKind.wireDrawingMachine,
                EnumMachineKind.pipeDrawingMachine,
                EnumMachineKind.cuttingMachine,
                EnumMachineKind.lathe,
                EnumMachineKind.cobblestoneGenerator,

                EnumMachineKind.grinder,
                EnumMachineKind.decomposer,
                EnumMachineKind.condenser,

                EnumMachineKind.centrifuge,
                EnumMachineKind.inscriber,
                EnumMachineKind.assembler,
                EnumMachineKind.millingMachine,

                EnumMachineKind.ECCondenser
        }) {
            machineMap.get(kind).put(TierPrefix.simple, new ClayiumMachine(kind, 3));
        }
        /* ...Tier 3 */

        /* Tier 4... */
        for (EnumMachineKind kind : new EnumMachineKind[] {
                EnumMachineKind.bendingMachine,
                EnumMachineKind.wireDrawingMachine,
                EnumMachineKind.pipeDrawingMachine,
                EnumMachineKind.cuttingMachine,
                EnumMachineKind.lathe,
                EnumMachineKind.cobblestoneGenerator,

                EnumMachineKind.grinder,
                EnumMachineKind.decomposer,
                EnumMachineKind.condenser,

                EnumMachineKind.centrifuge,
                EnumMachineKind.inscriber,
                EnumMachineKind.assembler,
                EnumMachineKind.millingMachine,

                EnumMachineKind.ECCondenser
        }) {
            machineMap.get(kind).put(TierPrefix.basic, new ClayiumMachine(kind, 4));
        }
        /* ...Tier 4 */
    }
    public static Block get(EnumMachineKind kind, TierPrefix tier) {
        return machineMap.get(kind).get(tier);
    }
    public static Block get(EnumMachineKind kind, int tier) {
        return get(kind, TierPrefix.get(tier));
    }
    /* ...Machines */
    /* ...Elements */

    private static final List<Block> blocks = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
}
