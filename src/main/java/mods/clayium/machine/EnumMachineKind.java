package mods.clayium.machine;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.ContainerTemp;
import mods.clayium.machine.ClayAssembler.ContainerClayAssembler;
import mods.clayium.machine.ClayWorkTable.ContainerClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

public enum EnumMachineKind {
    // Tier 0 ~
    workTable("work_table", ClayiumRecipes.clayWorkTable, SlotType.CLAY_WORK_TABLE),
    craftingTable("crafting_table", null),

    // Tier 1 ~
    bendingMachine("bending_machine", ClayiumRecipes.bendingMachine, SlotType.MACHINE),
    wireDrawingMachine("wire_drawing_machine", ClayiumRecipes.wireDrawingMachine, SlotType.MACHINE),
    pipeDrawingMachine("pipe_drawing_machine", ClayiumRecipes.pipeDrawingMachine, SlotType.MACHINE),
    cuttingMachine("cutting_machine", ClayiumRecipes.cuttingMachine, SlotType.MACHINE),
    lathe("lathe", ClayiumRecipes.lathe, SlotType.MACHINE),
    millingMachine("milling_machine", ClayiumRecipes.millingMachine, SlotType.MACHINE),
    cobblestoneGenerator("cobblestone_generator", null),
    waterWheel("water_wheel", null),

    // Tier 2 ~
    condenser("condenser", ClayiumRecipes.condenser, SlotType.MACHINE),
    grinder("grinder", ClayiumRecipes.grinder, SlotType.MACHINE),
    decomposer("decomposer", ClayiumRecipes.decomposer, SlotType.MACHINE),

    // Tier 3 ~
    assembler("assembler", ClayiumRecipes.assembler, SlotType.ASSEMBLER),
    inscriber("inscriber", ClayiumRecipes.inscriber, SlotType.ASSEMBLER),
    centrifuge("centrifuge", ClayiumRecipes.centrifuge),
    ECCondenser("energetic_clay_condenser", ClayiumRecipes.energeticClayCondenser, SlotType.MACHINE, "eccondenser"),

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
    clayEnergyLaser("energy_laser", null),
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
        this(kind, recipe, SlotType.UNKNOWN);
    }

    EnumMachineKind(String kind, ClayiumRecipe recipe, SlotType slotType) {
        this(kind, recipe, slotType, kind.replaceAll("_", ""));
    }

    EnumMachineKind(String kind, ClayiumRecipe recipe, SlotType slotType, String facePath) {
        this.kind = kind;
        this.recipe = recipe;
        if (slotType == null) throw new NullPointerException("Slot Type of " + this.kind + " is null! This is bug.");
        this.slotType = slotType;
        this.facePath = facePath;
    }

    public String getRegisterName() {
        return kind;
    }

    public ClayiumRecipe getRecipe() {
        return recipe;
    }

    private final String kind;
    private final ClayiumRecipe recipe;
    public final String facePath;
    public final SlotType slotType;

    @Nullable
    public static EnumMachineKind fromName(String name) {
        for (EnumMachineKind kind : EnumMachineKind.values()) {
            if (Objects.equals(kind.getRegisterName(), name)) return kind;
        }
        return null;
    }

    public ResourceLocation getFaceResource() {
        return new ResourceLocation(ClayiumCore.ModId, "textures/blocks/machine/" + this.facePath + ".png");
    }

    public boolean hasRecipe() {
        return this.recipe != null;
    }

    public static class SlotType {
        public static final SlotType UNKNOWN = new SlotType(0, 0, 0, 36, ContainerTemp.class);
        public static final SlotType CLAY_WORK_TABLE = new SlotType(0, 2, 4, 36, ContainerClayWorkTable.class);
        public static final SlotType MACHINE = new SlotType(0, 1, 3, 36, ContainerClayiumMachine.class);
        public static final SlotType ASSEMBLER = new SlotType(0, 2, 4, 36, ContainerClayAssembler.class);

        public final int inStart;
        public final int inCount;
        public final int playerStart;
        public final int playerCount;
        public final Class<? extends ContainerTemp> containerClass;

        SlotType(int inStart, int inCount, int playerStart, int playerCount, Class<? extends ContainerTemp> containerClass) {
            this.inStart = inStart;
            this.inCount = inCount;
            this.playerStart = playerStart;
            this.playerCount = playerCount;
            this.containerClass = containerClass;
        }
    }
}
