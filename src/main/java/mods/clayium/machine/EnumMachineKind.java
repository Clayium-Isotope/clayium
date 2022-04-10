package mods.clayium.machine;

import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;

public enum EnumMachineKind {
    // Tier 0 ~
    workTable("work_table", ClayiumRecipes.clayWorkTable),
    craftingTable("crafting_table", null),

    // Tier 1 ~
    bendingMachine("bending_machine", ClayiumRecipes.bendingMachine),
    wireDrawingMachine("wire_drawing_machine", ClayiumRecipes.wireDrawingMachine),
    pipeDrawingMachine("pipe_drawing_machine", ClayiumRecipes.pipeDrawingMachine),
    cuttingMachine("cutting_machine", ClayiumRecipes.cuttingMachine),
    lathe("lathe", ClayiumRecipes.lathe),
    millingMachine("milling_machine", ClayiumRecipes.millingMachine),
    cobblestoneGenerator("cobblestone_generator", null),
    waterWheel("water_wheel", null),

    // Tier 2 ~
    condenser("condenser", ClayiumRecipes.condenser),
    grinder("grinder", ClayiumRecipes.grinder),
    decomposer("decomposer", ClayiumRecipes.decomposer),

    // Tier 3 ~
    assembler("assembler", ClayiumRecipes.assembler),
    inscriber("inscriber", ClayiumRecipes.inscriber),
    centrifuge("centrifuge", ClayiumRecipes.centrifuge),
    ECCondenser("ec_condenser", ClayiumRecipes.energeticClayCondenser),

    // Tier 4 ~
    smelter("smelter", ClayiumRecipes.smelter),
    buffer("buffer", null),
    multitrackBuffer("multitrack_buffer", null),
    fluidBuffer("fluid_buffer", null),
    chemicalReactor("chemical_reactor", ClayiumRecipes.chemicalReactor),
    saltExtractor("salt_extractor", null),
    fluidTranslator("fluid_translator", null),
    CE_RFConverter("ce_rf_converter", null),

    // Tier 5 ~
    autoClayCondenser("auto_clay_condenser", null),
    quartzCrucible("quartz_crucible", null),
    solarClayFabricator("solar_clay_fabricator", null),
    clayInterface("clay_interface", null),
    redstoneInterface("redstone_interface", null),
    autoCrafter("auto_crafter", null),
    fluidTransferMachine("fluid_transfer_machine", ClayiumRecipes.fluidTransferMachine),

    // Tier 6 ~
    alloySmelter("alloy_smelter", ClayiumRecipes.alloySmelter),
    chemicalMetalSeparator("chemical_metal_separator", null),
    blastFurnace("blast_furnace", ClayiumRecipes.blastFurnace),
    electrolysisReactor("electrolysis_reactor", ClayiumRecipes.electrolysisReactor),
    clayChunkLoader("clay_chunk_loader", null),

    // Tier 7 ~
    distributor("distributor", null),
    laserInterface("laser_interface", null),
    reactor("reactor", ClayiumRecipes.reactor),
    transformer("matter_transformer", ClayiumRecipes.transformer),
    clayEnergyLaser("clay_energy_laser", null),
    laserReflector("laser_reflector", null),
    claySapling("clay_sapling", null),

    // Tier 8 ~
    clayFabricator("clay_fabricator", null),

    // Tier 9 ~
    CACondenser("ca_condenser", ClayiumRecipes.CACondenser),
    CAInjector("ca_injector", ClayiumRecipes.CAInjector),
    CACollector("ca_collector", null),

    // Tier 10 ~
    CAReactorCore("ca_reactor", ClayiumRecipes.CAReactor),

    // Tier 11 ~
    PANCore("pan_core", null),
    PANAdapter("pan_adapter", null),
    PANDuplicator("pan_duplicator", null),
    PANCable("pan_cable", null),

    // Tier 13 ~
    ECDecomposer("ec_decomposer", ClayiumRecipes.energeticClayDecomposer),
    creativeCESource("creative_energy", null),

    // Metal Chest
    metalChest("metal_chest", null);

    EnumMachineKind(String kind, ClayiumRecipe recipe) {
        this.kind = kind;
        this.recipe = recipe;
    }

    public String getRegisterName() {
        return kind;
    }

    public ClayiumRecipe getRecipe() {
        return recipe;
    }

    private final String kind;
    private final ClayiumRecipe recipe;
}
