package mods.clayium.block;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mods.clayium.block.tile.TileClayRFGenerator;
import mods.clayium.core.ClayiumCore;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ClayRFGenerator
        extends ClayNoRecipeMachines {
    public String blockName = null;

    public ClayRFGenerator(String blockName, String iconSuffix, int tier) {
        super((String) null, "clayium:" + iconSuffix, tier, (Class) TileClayRFGenerator.class, 2);
        this.guiId = 90;
        this.blockName = blockName;
    }


    @SideOnly(Side.CLIENT)
    public void registerIOIcons(IIconRegister par1IconRegister) {
        registerInsertIcons(par1IconRegister, new String[] {"import_energy"});
        registerExtractIcons(par1IconRegister, new String[] {"export_rf"});
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        super.registerBlockIcons(par1IconRegister);
        setSameOverlayIcons(par1IconRegister.registerIcon(this.iconstr));
    }


    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
        super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
        updatePower(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
    }


    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        updatePower(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }

    public void updatePower(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_) {
        if (!p_149695_1_.isRemote) {
            TileEntity tile = UtilBuilder.safeGetTileEntity((IBlockAccess) p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
            if (tile instanceof TileClayRFGenerator) {
                ((TileClayRFGenerator) tile).setPowered(p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_, p_149695_4_));
            }
        }
    }

    public TileEntity createNewTileEntity(World world, int par2) {
        TileEntity tile = super.createNewTileEntity(world, par2);
        if (tile instanceof TileClayRFGenerator) {
            ((TileClayRFGenerator) tile).setBlockName(this.blockName);
        }
        return tile;
    }


    protected static final Splitter splitter1 = Splitter.on(';').limit(2);
    protected static volatile Map<String, Map<String, Object>> configMap = null;

    public static synchronized Map<String, Map<String, Object>> getConfigMap() {
        if (configMap == null) {
            configMap = new HashMap<String, Map<String, Object>>();
            for (String str : ClayiumCore.cfgRFGenerator) {
                String[] strs = (String[]) Iterables.toArray(splitter1.split(str), String.class);
                if (strs != null && strs.length >= 2) {
                    Map<String, Object> map = readConfig(strs[1]);
                    if (map != null)
                        configMap.put(strs[0], map);
                }
            }
        }
        return configMap;
    }

    public static Map<String, Object> getConfig(String blockName) {
        Map<String, Map<String, Object>> map = getConfigMap();
        return (map == null) ? null : map.get(blockName);
    }

    protected static final Splitter splitter2 = Splitter.on(':');
    protected static final String[] keys = new String[] {"IconName", "Tier", "CEConsumptionPerTick", "RFProductionPerTick", "RFOutputPerTick", "RFStorageSize", "OverclockExponent"};


    protected static final Class[] valueTypes = new Class[] {String.class, Integer.class, Long.class, Integer.class, Integer.class, Integer.class, Double.class};


    public static Map<String, Object> readConfig(String configStr) {
        String[] strs = (String[]) Iterables.toArray(splitter2.split(configStr), String.class);
        if (strs == null)
            return null;
        Map<String, Object> ret = new HashMap<String, Object>();
        for (int i = 0; i < strs.length && i < keys.length && i < valueTypes.length; i++) {
            if ("CEConsumptionPerTick".equals(keys[i])) {
                try {
                    long v = Long.parseLong(strs[i]);
                    v *= 100000L;
                    ret.put(keys[i], Long.valueOf(v));
                } catch (NumberFormatException e) {
                    try {
                        double v = Double.parseDouble(strs[i]);
                        v *= 100000.0D;
                        ret.put(keys[i], Long.valueOf((long) v));
                    } catch (NumberFormatException e1) {
                        ClayiumCore.logger.catching(e1);
                    }
                }
            } else if (valueTypes[i] == String.class) {
                ret.put(keys[i], strs[i]);
            } else if (valueTypes[i] == Integer.class) {
                try {
                    int v = Integer.parseInt(strs[i]);
                    ret.put(keys[i], Integer.valueOf(v));
                } catch (NumberFormatException e) {
                    ClayiumCore.logger.catching(e);
                }
            } else if (valueTypes[i] == Long.class) {
                try {
                    long v = Long.parseLong(strs[i]);
                    ret.put(keys[i], Long.valueOf(v));
                } catch (NumberFormatException e) {
                    ClayiumCore.logger.catching(e);
                }
            } else if (valueTypes[i] == Double.class) {
                try {
                    double v = Double.parseDouble(strs[i]);
                    ret.put(keys[i], Double.valueOf(v));
                } catch (NumberFormatException e) {
                    ClayiumCore.logger.catching(e);
                }
            }
        }
        return ret;
    }


    public List getTooltip(ItemStack itemStack) {
        List<String> ret = UtilLocale.localizeTooltip("tooltip.RFGenerator");
        ret.addAll(super.getTooltip(itemStack));
        Map<String, Object> config = getConfig(this.blockName);
        if (config != null) {
            long ceConsumptionPerTickBase = 100L;
            int rfProductionPerTickBase = 10;
            int rfOutputPerTickBase = 10;
            Object obj = config.get("CEConsumptionPerTick");
            if (obj instanceof Number) {
                ceConsumptionPerTickBase = ((Number) obj).longValue();
            }
            obj = config.get("RFProductionPerTick");
            if (obj instanceof Number) {
                rfProductionPerTickBase = ((Number) obj).intValue();
            }
            obj = config.get("RFOutputPerTick");
            if (obj instanceof Number) {
                rfOutputPerTickBase = ((Number) obj).intValue();
            }
            if (UtilLocale.canLocalize("tooltip.RFGenerator.convertRate")) {
                ret.add(UtilLocale.localizeAndFormat("tooltip.RFGenerator.convertRate", new Object[] {UtilLocale.ClayEnergyNumeral(ceConsumptionPerTickBase), UtilLocale.rfNumeral(rfProductionPerTickBase)}));
            }
            if (UtilLocale.canLocalize("tooltip.RFGenerator.output")) {
                ret.add(UtilLocale.localizeAndFormat("tooltip.RFGenerator.output", new Object[] {UtilLocale.rfNumeral(rfOutputPerTickBase)}));
            }
        }
        return ret;
    }
}
