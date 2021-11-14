package mods.clayium.util;

import mods.clayium.block.ClayMachines;
import mods.clayium.block.tile.TileClayContainer;
import mods.clayium.core.ClayiumCore;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;

import java.util.HashMap;
import java.util.Map;

/*     */
public class UtilTier {
    public static TierManager tierSmelter;
    public static TierManager tierGeneric;
    public static TierManager tierCACondenser;
    public static TierManager tierMachineTransport;
    public static TierManager tierBufferTransport;

    public static boolean canAutoTransfer(int tier) {
        return (tier >= 3);
    }

    public static final String multCraftTime = "multCraftTime";
    public static final String multConsumingEnergy = "multConsumingEnergy";
    public static final String autoInsertInterval = "autoInsertInterval";
    public static final String autoExtractInterval = "autoExtractInterval";
    public static final String maxAutoInsertDefault = "maxAutoInsertDefault";
    public static final String maxAutoExtractDefault = "maxAutoExtractDefault";

    public static boolean canManufactualCraft(int tier) {
        return (tier <= 2);
    }

    public static boolean acceptWaterWheel(int tier) {
        return (tier == 2 || tier == 3);
    }

    public static boolean acceptEnergyClay(int tier) {
        return (tier >= 4);
    }


    public static void setTierManagers() {
        tierGeneric = TierManager.getMachineTierManager("MachineBase_Crafting");
        tierGeneric.get("multCraftTime").put(5, 0.25F);
        tierGeneric.get("multCraftTime").put(6, 0.0625F);
        tierGeneric.get("multCraftTime").put(10, 0.01F);
        tierGeneric.get("multConsumingEnergy").put(5, 5.0F);
        tierGeneric.get("multConsumingEnergy").put(6, 25.0F);
        tierGeneric.get("multConsumingEnergy").put(10, 250.0F);

        tierSmelter = TierManager.getMachineTierManager("MachineSmelter_Crafting");
        tierSmelter.get("multCraftTime").put(4, 2.0F);
        tierSmelter.get("multCraftTime").put(5, 0.5F);
        tierSmelter.get("multCraftTime").put(6, 0.125F);
        tierSmelter.get("multCraftTime").put(7, 0.03F);
        tierSmelter.get("multCraftTime").put(8, 0.01F);
        tierSmelter.get("multCraftTime").put(9, 0.0025F);
        tierSmelter.get("multConsumingEnergy").put(4, 1.0F);
        tierSmelter.get("multConsumingEnergy").put(5, 14.0F);
        tierSmelter.get("multConsumingEnergy").put(6, 200.0F);
        tierSmelter.get("multConsumingEnergy").put(7, 2800.0F);
        tierSmelter.get("multConsumingEnergy").put(8, 40000.0F);
        tierSmelter.get("multConsumingEnergy").put(9, 560000.0F);

        tierCACondenser = TierManager.getMachineTierManager("MachineCACondenser_Crafting");
        tierCACondenser.get("multCraftTime").put(10, 0.1F);
        tierCACondenser.get("multConsumingEnergy").put(10, 10.0F);
        tierCACondenser.get("multCraftTime").put(11, 0.01F);
        tierCACondenser.get("multConsumingEnergy").put(11, 100.0F);

        tierMachineTransport = TierManager.getTransportTierManager("MachineBase_Transport");
        tierBufferTransport = TierManager.getTransportTierManager("MachineBuffer_Transport");
        for (int tier = 0; tier <= 13; tier++) {
            if (tier <= 4) {
                tierMachineTransport.get("autoExtractInterval").put(tier, 20);
                tierMachineTransport.get("autoInsertInterval").put(tier, 20);
                tierMachineTransport.get("maxAutoExtractDefault").put(tier, 8);
                tierMachineTransport.get("maxAutoInsertDefault").put(tier, 8);
            } else if (tier == 5) {
                tierMachineTransport.get("autoExtractInterval").put(tier, 2);
                tierMachineTransport.get("autoInsertInterval").put(tier, 2);
                tierMachineTransport.get("maxAutoExtractDefault").put(tier, 16);
                tierMachineTransport.get("maxAutoInsertDefault").put(tier, 16);
            } else if (tier >= 6) {
                tierMachineTransport.get("autoExtractInterval").put(tier, 1);
                tierMachineTransport.get("autoInsertInterval").put(tier, 1);
                tierMachineTransport.get("maxAutoExtractDefault").put(tier, 64);
                tierMachineTransport.get("maxAutoInsertDefault").put(tier, 64);
            }

            if (tier <= 4) {
                tierBufferTransport.get("autoExtractInterval").put(tier, 8);
                tierBufferTransport.get("autoInsertInterval").put(tier, 8);
                tierBufferTransport.get("maxAutoExtractDefault").put(tier, 1);
                tierBufferTransport.get("maxAutoInsertDefault").put(tier, 1);
            }
            if (tier >= 7) {
                tierBufferTransport.get("autoExtractInterval").put(tier, 1);
                tierBufferTransport.get("autoInsertInterval").put(tier, 1);
            }
            switch (tier) {
                case 5:
                    tierBufferTransport.get("autoExtractInterval").put(tier, 4);
                    tierBufferTransport.get("autoInsertInterval").put(tier, 4);
                    tierBufferTransport.get("maxAutoExtractDefault").put(tier, 4);
                    tierBufferTransport.get("maxAutoInsertDefault").put(tier, 4);
                    break;
                case 6:
                    tierBufferTransport.get("autoExtractInterval").put(tier, 2);
                    tierBufferTransport.get("autoInsertInterval").put(tier, 2);
                    tierBufferTransport.get("maxAutoExtractDefault").put(tier, 16);
                    tierBufferTransport.get("maxAutoInsertDefault").put(tier, 16);
                    break;
                case 7:
                    tierBufferTransport.get("maxAutoExtractDefault").put(tier, 64);
                    tierBufferTransport.get("maxAutoInsertDefault").put(tier, 64);
                    break;
                case 8:
                    tierBufferTransport.get("maxAutoExtractDefault").put(tier, 128);
                    tierBufferTransport.get("maxAutoInsertDefault").put(tier, 128);
                    break;
                case 9:
                    tierBufferTransport.get("maxAutoExtractDefault").put(tier, 192);
                    tierBufferTransport.get("maxAutoInsertDefault").put(tier, 192);
                    break;
                case 10:
                    tierBufferTransport.get("maxAutoExtractDefault").put(tier, 256);
                    tierBufferTransport.get("maxAutoInsertDefault").put(tier, 256);
                    break;
                case 11:
                    tierBufferTransport.get("maxAutoExtractDefault").put(tier, 512);
                    tierBufferTransport.get("maxAutoInsertDefault").put(tier, 512);
                    break;
                case 12:
                    tierBufferTransport.get("maxAutoExtractDefault").put(tier, 1024);
                    tierBufferTransport.get("maxAutoInsertDefault").put(tier, 1024);
                    break;
                case 13:
                    tierBufferTransport.get("maxAutoExtractDefault").put(tier, 6400);
                    tierBufferTransport.get("maxAutoInsertDefault").put(tier, 6400);
                    break;
            }
        }
    }

    public static void loadConfig(Configuration cfg) {
        tierSmelter.loadAllConfig(cfg);
        tierGeneric.loadAllConfig(cfg);
        tierCACondenser.loadAllConfig(cfg);

        tierMachineTransport.loadAllConfig(cfg);
        tierBufferTransport.loadAllConfig(cfg);
    }

    public static class TierManager
            extends HashMap<String, TieredStatus> {
        protected String configName;

        public static TierManager getMachineTierManager() {
            return getMachineTierManager("");
        }

        public static TierManager getMachineTierManager(String configName) {
            TierManager res = new TierManager(configName);
            res.put("multCraftTime", new UtilTier.TieredNumericStatus<Float>(1.0F, 0.0F, 1.0E9F));
            res.put("multConsumingEnergy", new UtilTier.TieredNumericStatus<Float>(1.0F, 0.0F, 1.0E9F));
            return res;
        }

        public static TierManager getTransportTierManager(String configName) {
            TierManager res = new TierManager(configName);
            res.put("autoInsertInterval", new UtilTier.TieredNumericStatus<Integer>(20, 1, 999));
            res.put("autoExtractInterval", new UtilTier.TieredNumericStatus<Integer>(20, 1, 999));
            res.put("maxAutoInsertDefault", new UtilTier.TieredNumericStatus<Integer>(8, 1, 9999));
            res.put("maxAutoExtractDefault", new UtilTier.TieredNumericStatus<Integer>(8, 1, 9999));
            return res;
        }

        public TierManager(String configName) {
            this.configName = configName;
        }

        public static void applyMachineTierManager(Block[] machines, TierManager manager) {
            for (int i = 0; i < machines.length; i++) {
                if (machines[i] instanceof ClayMachines) {
                    ((ClayMachines) machines[i]).multCraftTime = manager.getF("multCraftTime", i);
                    ((ClayMachines) machines[i]).multConsumingEnergy = manager.getF("multConsumingEnergy", i);
                }
            }
        }

        public static void applyTransportTierManager(TileClayContainer tile, int tier, TierManager manager) {
            tile.autoExtractInterval = manager.getI("autoExtractInterval", tier);
            tile.autoInsertInterval = manager.getI("autoInsertInterval", tier);
            tile.maxAutoExtractDefault = manager.getI("maxAutoExtractDefault", tier);
            tile.maxAutoInsertDefault = manager.getI("maxAutoInsertDefault", tier);
        }

        public Object get(String string, int tier) {
            return containsKey(string) ? get(string).get(tier) : null;
        }

        public float getF(String string, int tier) {
            Object res = get(string, tier);
            return (res instanceof Float) ? (Float) res : 0.0F;
        }

        public int getI(String string, int tier) {
            Object res = get(string, tier);
            return (res instanceof Integer) ? (Integer) res : 0;
        }

        public void loadAllConfig(Configuration cfg) {
            for (Map.Entry<String, UtilTier.TieredStatus> entry : entrySet())
                entry.getValue().loadAllConfig(this.configName + "_" + entry.getKey(), cfg);
        }
    }

    public static class TieredStatus<T>
            extends HashMap<Integer, T> {
        public T defvalue;
        public String configComment = "";
        public String configCategory = "tier balance";

        public TieredStatus(T defvalue) {
            this.defvalue = defvalue;
        }

        public T get(int tier) {
            return containsKey(tier) ? super.get(tier) : this.defvalue;
        }

        public Object getConfig(String name, Object value, Configuration cfg) {
            if (value instanceof Boolean) {
                return cfg.getBoolean(name, this.configCategory, (Boolean) value, this.configComment);
            }
            if (value instanceof String) {
                return cfg.getString(name, this.configCategory, (String) value, this.configComment);
            }
            return null;
        }

        public void loadAllConfig(String name, Configuration cfg) {
            for (Integer tier : keySet()) {
                int t = tier;
                String cstr = String.format(name + "_%02d", t);
                Object value = getConfig(cstr, get(t), cfg);
                try {
                    put(t, (T) value);
                } catch (Exception e) {
                    ClayiumCore.logger.error("Config Error @ " + cstr);
                    ClayiumCore.logger.catching(e);
                }
            }
            if (this.defvalue != null) {
                String cstr = name + "_def";
                Object value = getConfig(cstr, this.defvalue, cfg);
                try {
                    this.defvalue = (T) value;
                } catch (Exception e) {
                    ClayiumCore.logger.error("Config Error @ " + cstr);
                    ClayiumCore.logger.catching(e);
                }
            }
        }
    }

    public static class TieredNumericStatus<T extends Number> extends TieredStatus<T> {
        public T min;
        public T max;

        public TieredNumericStatus(T defvalue) {
            super(defvalue);
        }

        public TieredNumericStatus(T defvalue, T min, T max) {
            this(defvalue);
            this.min = min;
            this.max = max;
        }


        public Object getConfig(String name, Object value, Configuration cfg) {
            if (value instanceof Integer) {
                int minValue = (this.min instanceof Integer) ? (Integer) this.min : Integer.MIN_VALUE;
                int maxValue = (this.max instanceof Integer) ? (Integer) this.max : Integer.MAX_VALUE;
                return cfg.getInt(name, this.configCategory, (Integer) value, minValue, maxValue, this.configComment);
            }
            if (value instanceof Float) {
                float minValue = (this.min instanceof Float) ? (Float) this.min : Float.MIN_VALUE;
                float maxValue = (this.max instanceof Float) ? (Float) this.max : Float.MAX_VALUE;
                return cfg.getFloat(name, this.configCategory, (Float) value, minValue, maxValue, this.configComment);
            }
            return super.getConfig(name, value, cfg);
        }
    }
}


