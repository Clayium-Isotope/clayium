package mods.clayium.util;

import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import mods.clayium.core.ClayiumCore;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class UtilFluid {
    public static UtilFluid INSTANCE = new UtilFluid();
    static BiMap<Fluid, Integer> fluidIDs = HashBiMap.create();
    static BiMap<Integer, String> fluidNames = HashBiMap.create();
    static Set<String> overridableNames = new TreeSet<String>();

    static final int offset = 4096;
    static boolean useRegistry = false;
    static Configuration cfgFluidIDs = null;
    protected static final String prefix = "[FluidID Loader] ";

    static {
        INSTANCE.subscribeFluidRegisterEvent(new FluidRegistry.FluidRegisterEvent("water", 1));
        INSTANCE.subscribeFluidRegisterEvent(new FluidRegistry.FluidRegisterEvent("lava", 2));
    }

    public static void log(String message) {
        if (ClayiumCore.cfgVerboseLoggingForFluidIDLoader)
            ClayiumCore.logger.info("[FluidID Loader] " + message);
    }

    protected static final Splitter splitter = Splitter.on('=').limit(2);

    public static void loadMapsFromConfig(Configuration cfg) {
        boolean error = false;
        HashBiMap<Integer, String> hashBiMap1 = HashBiMap.create();
        HashBiMap<Fluid, Integer> hashBiMap2 = HashBiMap.create();
        HashBiMap<Integer, String> hashBiMap3 = HashBiMap.create();
        cfgFluidIDs = cfg;
        log("Started to load FluidID.");
        if (cfgFluidIDs != null && cfgFluidIDs.hasCategory("fluid") && cfgFluidIDs.getCategory("fluid").containsKey("IDsForCapsule")) {
            String[] cfgs = cfgFluidIDs.getStringList("IDsForCapsule", "fluid", new String[0], "ID Map for Clay Fluid Capsule.");

            for (String s : cfgs) {
                String[] result = Iterables.toArray(splitter.split(s), String.class);
                if (result != null && result.length == 2) {
                    hashBiMap3.put(Integer.parseInt(result[0]), result[1]);
                    hashBiMap1.put(Integer.parseInt(result[0]), result[1]);
                }
            }
            log("Loading FluidID from cfg.");
            log("  List of FluidIDs in cfg");
            for (Map.Entry<Integer, String> entry : hashBiMap1.entrySet()) {
                String fluidName = entry.getValue();
                int newID = entry.getKey();
                log("    FluidID = " + newID + ", Fluid Name = " + fluidName);
            }
            log("  List of loaded FluidIDs");
            for (Map.Entry<Integer, String> entry : fluidNames.entrySet()) {
                String fluidName = entry.getValue();
                int newID = entry.getKey();
                log("    FluidID = " + newID + ", Fluid Name = " + fluidName);
            }

            log("Scanning already loaded Fluid list");

            for (Map.Entry<Integer, String> entry : fluidNames.entrySet()) {
                String fluidName = entry.getValue();
                int oldID = entry.getKey();
                int newID = oldID;
                log("  Checking FluidID = " + newID + ", Fluid Name = " + fluidName);

                if (hashBiMap3.containsValue(fluidName)) {
                    newID = hashBiMap1.inverse().get(fluidName);
                    log("    Found in cfg. Set FluidID to " + newID);
                } else {
                    while (hashBiMap1.containsKey(newID)) newID++;
                    hashBiMap1.put(newID, fluidName);
                    log("    Not found in cfg. Set FluidID to " + newID);
                }

                Fluid fluid = fluidIDs.inverse().get(oldID);

                try {
                    hashBiMap2.put(fluid, newID);
                } catch (IllegalArgumentException e) {
                    ClayiumCore.logger.error("[FluidID Loader] " + e.getMessage());
                    error = true;
                }
            }

            fluidNames = hashBiMap1;
            fluidIDs = hashBiMap2;
        }

        log("Done.");
        if (error) {
            ClayiumCore.logger.error("[FluidID Loader] An exception occurred. The FluidID database may be corrupted.");
            ClayiumCore.logger.error("Enable B:VerboseLoggingForFluidIDLoader to get more information.");
        }
    }

    public static void saveMapsToConfig() {
        if (cfgFluidIDs != null) {
            String[] toSave = new String[fluidNames.size()];
            int pos = 0;
            for (Map.Entry<Integer, String> entry : fluidNames.entrySet()) {
                String fluidName = entry.getValue();
                int ID = entry.getKey();
                toSave[pos] = ID + "=" + fluidName;
                pos++;
            }
            cfgFluidIDs.get("fluid", "IDsForCapsule", new String[0]).set(toSave);
            cfgFluidIDs.save();
        }
    }

    protected static boolean overrideFlag = true;

    @SubscribeEvent
    public void subscribeFluidRegisterEvent(FluidRegistry.FluidRegisterEvent event) {
        Fluid fluid = FluidRegistry.getFluid(event.fluidName);
        registerFluid(fluid, 1, overrideFlag);
        saveMapsToConfig();
    }

    protected static boolean registerFluid(Fluid fluid, int fluidID, boolean overrideFluidID) {
        while (fluidIDs.containsValue(fluidID)) {
            fluidID++;
        }
        String fluidName = fluid.getName();
        if (fluidNames.containsValue(fluidName)) {
            if (overridableNames.contains(fluidName)) {
                if (overrideFluidID) {
                    int oldID = fluidNames.inverse().get(fluidName);
                    fluidIDs.inverse().remove(oldID);
                    fluidNames.remove(oldID);
                } else {
                    return false;
                }
            } else {
                fluidID = fluidNames.inverse().get(fluidName);
                if (fluidIDs.inverse().containsKey(fluidID))
                    fluidIDs.inverse().remove(fluidID);
                fluidIDs.put(fluid, fluidID);
                return true;
            }
        }
        fluidIDs.put(fluid, fluidID);
        fluidNames.put(fluidID, fluidName);
        return true;
    }

    static int index = 4096;

    public static void registerFluid(Fluid fluid) {
        registerFluid(fluid, index, false);
        overridableNames.add(fluid.getName());
        index++;

        overrideFlag = false;
        FluidRegistry.registerFluid(fluid);
        overrideFlag = true;
    }

    public static int getFluidID(String fluidName) {
        if (useRegistry)
            return FluidRegistry.getFluidID(fluidName);
        Integer ret = fluidIDs.get(FluidRegistry.getFluid(fluidName));
        if (ret == null && fluidName != null) {
            ret = fluidNames.inverse().get(fluidName);
            if (ret == null)
                ClayiumCore.logger.error("Can't get Fluid ID! FluidName = " + fluidName);
        }
        return (ret == null) ? -1 : ret;
    }

    public static int getFluidID(Fluid fluid) {
        if (useRegistry)
            return FluidRegistry.getFluidID(fluid);
        Integer ret = fluidIDs.get(fluid);
        if (ret == null && fluid != null)
            return getFluidID(fluid.getName());
        return (ret == null) ? -1 : ret;
    }

    public static int getFluidID(FluidStack fluidStack) {
        return (fluidStack != null) ? getFluidID(fluidStack.getFluid()) : -1;
    }

    public static Fluid getFluid(int fluidID) {
        return useRegistry ? FluidRegistry.getFluid(fluidID) : fluidIDs.inverse().get(fluidID);
    }

    public static String getFluidName(int fluidID) {
        return useRegistry ? FluidRegistry.getFluidName(fluidID) : fluidNames.get(fluidID);
    }
}
