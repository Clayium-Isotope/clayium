package mods.clayium.core;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.EnumMap;
import java.util.Map;

public class ClayiumConfiguration {
    private static Configuration cfg = null;

    public static boolean cfgUtilityMode = false;
    public static boolean cfgHardcoreAluminium = false;
    public static boolean cfgHardcoreOsmium = false;
    public static double cfgProgressionRate = 1.0D;
    public static boolean cfgEnableInjectorRecipeOfInterface = false;
    public static boolean cfgInverseClayLaserRSCondition = false;
    public static int cfgClaySteelPickaxeRange = 2;
    public static int cfgPacketSendingRate = 20;
    public static boolean cfgEnableInstantSync = true;
    public static boolean cfgEnableFluidCapsule = true;
    public static int cfgFluidCapsuleCreativeTabMode = 1;
    public static int cfgRenderingRate = 200;
    public static boolean cfgCAReactorGlittering = true;
    public static int cfgLaserQuality = 8;
    public static boolean cfgEnableAlternativeTileEntityName = true;
    public static String[] cfgRFGenerator = new String[]{
            "BasicRFGenerator;rfgenerator:4:0.001:10:10:10000:1",
            "AdvancedRFGenerator;rfgenerator:5:0.01:30:30:30000:1",
            "PrecisionRFGenerator;rfgenerator:6:0.1:90:90:90000:1",
            "ClaySteelRFGenerator;rfgenerator:7:1:270:270:270000:1",
            "ClayiumRFGenerator;rfgenerator:8:10:810:810:810000:1",
            "UltimateRFGenerator;rfgenerator:9:100:2430:2430:2430000:1",
            "AntimatterRFGenerator;rfgenerator:10:1000:7290:7290:7290000:1",
            "PureAntimatterRFGenerator;rfgenerator:11:10000:21870:21870:21870000:1",
            "OECRFGenerator;rfgenerator:12:100000:65610:65610:65610000:1",
            "OPARFGenerator;rfgenerator:13:1000000:196830:196830:196830000:1"
    };
    public static boolean cfgEnableRFGenerator = false;
    public static Map<ClayiumIntegration, Boolean> cfgModIntegration = new EnumMap<>(ClayiumIntegration.class);
    public static final int EntityIdClayBall = 0;
    public static final int EntityIdTeleportBall = 1;

    public void loadConfig(FMLPreInitializationEvent event) {
        cfg = new Configuration(event.getSuggestedConfigurationFile());
        cfg.load();

        cfgUtilityMode = cfg.getBoolean("UtilityMode", "mode", false, "");
        cfgHardcoreAluminium = cfg.getBoolean("HardcoreAluminium", "mode", false, "");
        cfgHardcoreOsmium = cfg.getBoolean("HardcoreOsmium", "mode", false, "");
        cfgProgressionRate = cfg.getFloat("ProgressionRate", "mode", 1.0F, 0.001F, 9999.0F, "");
        cfgEnableInjectorRecipeOfInterface = cfg.getBoolean("EnableInjectorRecipeOfInterface", "mode", false, "This recipe makes it much easier to construct multi-block machines.");
        cfgClaySteelPickaxeRange = cfg.getInt("ClaySteelPickaxeRange", "misc", 2, 0, 64, "");
        cfgPacketSendingRate = cfg.getInt("PacketSendingRate", "misc", 20, 1, 9999, "");
        cfgEnableInstantSync = cfg.getBoolean("EnableInstantSync", "misc", true, "");
        cfgInverseClayLaserRSCondition = cfg.getBoolean("InvertClayLaserRSCondition", "misc", false, "");
        cfgEnableFluidCapsule = cfg.getBoolean("EnableFluidCapsule", "misc", true, "");
        cfgFluidCapsuleCreativeTabMode = cfg.getInt("FluidCapsuleCreativeTabMode", "misc", 1, 0, 2, "This setting is also valid for NEI.  0: Disable  1: 1000mB only  2: Display All");
        cfgEnableAlternativeTileEntityName = cfg.getBoolean("EnableAlternativeTileEntityName", "misc", true, "Disable this if a tile entity id of this mod conflicts with one of other mod.");
        cfgRenderingRate = cfg.getInt("RenderingRate", "render", 200, 1, 9999, "");
        cfgCAReactorGlittering = cfg.getBoolean("CAReactorGlittering", "render", true, "");
        cfgLaserQuality = cfg.getInt("LaserQuality", "render", 8, 1, 32, "");

        for (ClayiumIntegration id : ClayiumIntegration.values()) {
            cfgModIntegration.put(id, cfg.getBoolean(id.configId, "integration", true, ""));
        }

        cfgEnableRFGenerator = cfg.getBoolean("EnableRFGenerator", "misc", false, "This setting enables assembler recipes for RF Converters.");
        cfgRFGenerator = new String[]{"BasicRFGenerator;rfgenerator:4:0.001:10:10:10000:1", "AdvancedRFGenerator;rfgenerator:5:0.01:30:30:30000:1", "PrecisionRFGenerator;rfgenerator:6:0.1:90:90:90000:1", "ClaySteelRFGenerator;rfgenerator:7:1:270:270:270000:1", "ClayiumRFGenerator;rfgenerator:8:10:810:810:810000:1", "UltimateRFGenerator;rfgenerator:9:100:2430:2430:2430000:1", "AntimatterRFGenerator;rfgenerator:10:1000:7290:7290:7290000:1", "PureAntimatterRFGenerator;rfgenerator:11:10000:21870:21870:21870000:1", "OECRFGenerator;rfgenerator:12:100000:65610:65610:65610000:1", "OPARFGenerator;rfgenerator:13:1000000:196830:196830:196830000:1"};
        cfgRFGenerator = cfg.getStringList("RFGenerator", "misc", cfgRFGenerator, "BlockName;IconName:Tier:CEConsumptionPerTick:RFProductionPerTick:RFOutputPerTick:RFStorageSize:OverclockExponent");
        ClayiumCore.logger.info("Loaded RF Generator Settings.");

        for(String str : cfgRFGenerator) {
            ClayiumCore.logger.info(str);
        }
    }
}
