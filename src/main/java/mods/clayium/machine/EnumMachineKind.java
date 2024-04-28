package mods.clayium.machine;

import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.ContainerTemp;
import mods.clayium.machine.ClayAssembler.ContainerClayAssembler;
import mods.clayium.machine.ClayWorkTable.ContainerClayWorkTable;
import mods.clayium.machine.ClayiumMachine.ContainerClayiumMachine;
import mods.clayium.machine.crafting.ClayiumRecipe;
import mods.clayium.machine.crafting.ClayiumRecipes;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public enum EnumMachineKind {
    EMPTY("", null),

    // 加工機械
    workTable("work_table", ClayiumRecipes.clayWorkTable, SlotType.CLAY_WORK_TABLE),
    craftingTable("crafting_table", null),
    bendingMachine("bending_machine", ClayiumRecipes.bendingMachine, SlotType.MACHINE),
    wireDrawingMachine("wire_drawing_machine", ClayiumRecipes.wireDrawingMachine, SlotType.MACHINE),
    pipeDrawingMachine("pipe_drawing_machine", ClayiumRecipes.pipeDrawingMachine, SlotType.MACHINE),
    cuttingMachine("cutting_machine", ClayiumRecipes.cuttingMachine, SlotType.MACHINE),
    lathe("lathe", ClayiumRecipes.lathe, SlotType.MACHINE),
    millingMachine("milling_machine", ClayiumRecipes.millingMachine, SlotType.MACHINE),
    condenser("condenser", ClayiumRecipes.condenser, SlotType.MACHINE),
    grinder("grinder", ClayiumRecipes.grinder, SlotType.MACHINE),
    decomposer("decomposer", ClayiumRecipes.decomposer, SlotType.MACHINE),
    assembler("assembler", ClayiumRecipes.assembler, SlotType.ASSEMBLER),
    inscriber("inscriber", ClayiumRecipes.inscriber, SlotType.ASSEMBLER),
    centrifuge("centrifuge", ClayiumRecipes.centrifuge),
    smelter("smelter", ClayiumRecipes.smelter),
    chemicalReactor("chemical_reactor", ClayiumRecipes.chemicalReactor),
    autoCrafter("auto_crafter", null),
    autoClayCondenser("auto_clay_condenser", null),
    quartzCrucible("quartz_crucible", null),
    blastFurnace("blast_furnace", ClayiumRecipes.blastFurnace),
    alloySmelter("alloy_smelter", ClayiumRecipes.alloySmelter),
    chemicalMetalSeparator("chemical_metal_separator", null),
    electrolysisReactor("electrolysis_reactor", ClayiumRecipes.electrolysisReactor),
    clayReactor("reactor", ClayiumRecipes.clayReactor),
    transformer("transformer", ClayiumRecipes.transformer, SlotType.MACHINE, "transformer"),
    CACondenser("ca_condenser", null),
    CAInjector("ca_injector", ClayiumRecipes.CAInjector),
    ECCondenser("energetic_clay_condenser", ClayiumRecipes.energeticClayCondenser, SlotType.MACHINE, "eccondenser"),
    ECDecomposer("ec_decomposer", ClayiumRecipes.energeticClayDecomposer),

    // 反物質反応炉関連
    CAReactorCore("ca_reactor", ClayiumRecipes.CAReactor, SlotType.MACHINE, "careactorcore"),

    // PAN関連
    PANCable("pan_cable", null),
    PANCore("pan_core", null),
    PANAdapter("pan_adapter", null),
    PANDuplicator("pan_duplicator", null),

    // 配管・ストレージ
    buffer("buffer", null),
    multitrackBuffer("multitrack_buffer", null),
    fluidBuffer("fluid_translator", null), // Fluid Translatorでもあった
    storageContainer("storage_container", null, SlotType.UNKNOWN, "storagecontainer"),
    vacuumContainer("vacuum_container", null),
    metalChest("metal_chest", null),
    clayInterface("clay_interface", null),
    redstoneInterface("redstone_interface", null),
    laserInterface("laser_interface", null),
    distributor("distributor", null),

    // 生産・エネルギー
    cobblestoneGenerator("cobblestone_generator", null),
    waterWheel("water_wheel", null),
    saltExtractor("salt_extractor", null),
    solarClayFabricator("solar_clay_fabricator", null, SlotType.MACHINE, "solar"),
    clayEnergyLaser("energy_laser", null),
    laserReflector("laser_reflector", null),
    clayFabricator("clay_fabricator", null),
    CACollector("ca_collector", null),
    creativeCESource("creative_energy", null),
    fluidTransferMachine("fluid_transfer_machine", ClayiumRecipes.fluidTransferMachine),

    // ビルダー系
    clayMarker("clay_marker", null),
    openPitMarker("open_pit_marker", null),
    groundLevelingMarker("ground_leveling_marker", null),
    prismMarker("prism_marker", null),
    blockBreaker("block_breaker", null),
    areaMiner("area_miner", null),
    advancedAreaMiner("advanced_area_miner", null),
    areaReplacer("area_replacer", null),
    activator("activator", null),
    areaActivator("area_activator", null),
    areaCollector("area_collector", null),

    // その他
    clayChunkLoader("clay_chunk_loader", null),
    autoTrader("auto_trader", null),
    CE_RFConverter("rf_generator", null),
    ;

    EnumMachineKind(String kind, ClayiumRecipe recipe) {
        this(kind, recipe, SlotType.UNKNOWN);
    }

    EnumMachineKind(String kind, ClayiumRecipe recipe, SlotType slotType) {
        this(kind, recipe, slotType, kind.replaceAll("_", ""));
    }

    EnumMachineKind(String kind, ClayiumRecipe recipe, SlotType slotType, String facePath) {
        this.kind = kind;
        this.recipe = recipe == null ? ClayiumRecipes.EMPTY : recipe;
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

    public static EnumMachineKind fromName(String name) {
        if (name == null || name.isEmpty()) return EMPTY;

        for (EnumMachineKind kind : EnumMachineKind.values()) {
            if (Objects.equals(kind.getRegisterName(), name)) return kind;
        }
        return EMPTY;
    }

    public ResourceLocation getFaceResource() {
        return new ResourceLocation(ClayiumCore.ModId, "textures/blocks/machine/" + this.facePath + ".png");
    }

    public boolean hasRecipe() {
        return this.recipe != null;
    }

    public static class SlotType {
        public static final SlotType UNKNOWN = new SlotType(0, 0, 0, 0, 0, 36, ContainerTemp.class);
        public static final SlotType CLAY_WORK_TABLE = new SlotType(0, 2, 2, 2, 4, 36, ContainerClayWorkTable.class);
        public static final SlotType MACHINE = new SlotType(0, 1, 1, 2, 3, 36, ContainerClayiumMachine.class);
        public static final SlotType ASSEMBLER = new SlotType(0, 2, 2, 2, 4, 36, ContainerClayAssembler.class);

        public final int inStart;
        public final int inCount;
        public final int outStart;
        public final int outCount;
        public final int playerStart;
        public final int playerCount;
        public final Class<? extends ContainerTemp> containerClass;

        SlotType(int inStart, int inCount, int outStart, int outCount, int playerStart, int playerCount, Class<? extends ContainerTemp> containerClass) {
            this.inStart = inStart;
            this.inCount = inCount;
            this.outStart = outStart;
            this.outCount = outCount;
            this.playerStart = playerStart;
            this.playerCount = playerCount;
            this.containerClass = containerClass;
        }
    }

    public static String getLocalizeKey(EnumMachineKind kind) {
        return "util.machine." + kind.kind;
    }
}
