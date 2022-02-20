package mods.clayium.core;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClayiumConfiguration {
    private static Configuration cfg = null;

    public static boolean cfgUtilityMode = false;
    public static boolean cfgHardcoreAluminium = false;
    public static boolean cfgHardcoreOsmium = false;
    public static double cfgProgressionRate = 1.0D;

    public static int cfgClaySteelPickaxeRange = 2;

    public static boolean cfgEnableFluidCapsule = true;

    public void loadConfig(FMLPreInitializationEvent event) {
        cfg = new Configuration(event.getSuggestedConfigurationFile());
        cfg.load();

        cfgUtilityMode = cfg.getBoolean("UtilityMode", "mode", false, "");
        cfgHardcoreAluminium = cfg.getBoolean("HardcoreAluminium", "mode", false, "");
        cfgHardcoreOsmium = cfg.getBoolean("HardcoreOsmium", "mode", false, "");
        cfgProgressionRate = cfg.getFloat("ProgressionRate", "mode", 1.0F, 0.001F, 9999.0F, "");

        cfgClaySteelPickaxeRange = cfg.getInt("ClaySteelPickaxeRange", "misc", 2, 0, 64, "");

        cfgEnableFluidCapsule = cfg.getBoolean("EnableFluidCapsule", "misc", true, "");
    }
}
