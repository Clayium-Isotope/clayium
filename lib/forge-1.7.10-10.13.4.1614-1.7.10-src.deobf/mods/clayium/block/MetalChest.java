package mods.clayium.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mods.clayium.block.tile.TileMetalChest;
import mods.clayium.core.ClayiumCore;
import mods.clayium.item.CMaterial;
import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilBuilder;
import mods.clayium.util.UtilLocale;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;


public class MetalChest
        extends ClayContainer
        implements ISpecialUnlocalizedName {
    protected static HashMap<CMaterial, HashMap<String, Integer>> metalChestMaterials = new HashMap<CMaterial, HashMap<String, Integer>>();


    public static void registerMaterials() {
        try {
            addChestMaterial(CMaterials.SILICON, 9, 5, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.ALUMINIUM, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.CLAY_STEEL, 9, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.CLAYIUM, 13, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.ULTIMATE_ALLOY, 13, 8, 3, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.MAGNESIUM, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.SODIUM, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.LITHIUM, 9, 7, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.ZIRCONIUM, 9, 7, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.ZINC, 9, 6, 1, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.MANGANESE, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.CALCIUM, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.POTASSIUM, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.NICKEL, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.BERYLLIUM, 10, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.LEAD, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.HAFNIUM, 9, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.CHROME, 13, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.TITANIUM, 13, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.STRONTIUM, 10, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.BARIUM, 10, 8, 1, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.AZ91D_ALLOY, 13, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.ZK60A_ALLOY, 13, 8, 1, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.RUBIDIUM, 13, 3, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.CAESIUM, 13, 3, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.FRANCIUM, 13, 4, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.RADIUM, 13, 4, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.ACTINIUM, 13, 5, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.THORIUM, 13, 5, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.PROTACTINIUM, 13, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.URANIUM, 13, 6, 2, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.NEPTUNIUM, 13, 6, 3, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.PLUTONIUM, 13, 6, 4, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.AMERICIUM, 13, 6, 5, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.CURIUM, 13, 6, 6, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.LANTHANUM, 13, 2, 2, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.CERIUM, 13, 2, 4, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.PRASEODYMIUM, 13, 2, 6, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.NEODYMIUM, 13, 2, 8, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.PROMETHIUM, 13, 4, 8, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.SAMARIUM, 13, 6, 8, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.EUROPIUM, 13, 8, 8, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.VANADIUM, 4, 8, 1, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.COBALT, 11, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.PALLADIUM, 11, 8, 1, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.PLATINUM, 13, 8, 2, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.IRIDIUM, 13, 8, 3, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.OSMIUM, 13, 8, 4, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.RHENIUM, 13, 8, 5, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.TANTALUM, 10, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.TUNGSTEN, 13, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.MOLYBDENUM, 13, 8, 2, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.ANTIMONY, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.BISMUTH, 9, 6, 1, ClayiumCore.configrationDefault);

            addChestMaterial(CMaterials.BRASS, 9, 6, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.ELECTRUM, 13, 8, 1, ClayiumCore.configrationDefault);
            addChestMaterial(CMaterials.INVAR, 9, 8, 1, ClayiumCore.configrationDefault);
        } finally {

            ClayiumCore.configrationDefault.save();
        }
    }

    public MetalChest() {
        super(Material.iron, TileMetalChest.class, null, 11, 1);
        setHardness(2.0F);
        setResistance(2.0F);
        setStepSound(soundTypeMetal);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        Iterator<Map.Entry<CMaterial, HashMap<String, Integer>>> iterator = metalChestMaterials.entrySet().iterator();
        while (iterator.hasNext()) {
            CMaterial material = (CMaterial) ((Map.Entry) iterator.next()).getKey();
            list.add(new ItemStack(item, 1, material.meta));
            Collections.sort(list, new Comparator<ItemStack>() {
                public int compare(ItemStack o1, ItemStack o2) {
                    if (o1 != null && o2 != null) {
                        if (o1.getItemDamage() < o2.getItemDamage()) return -1;
                        if (o1.getItemDamage() > o2.getItemDamage()) return 1;
                    }
                    return 0;
                }
            });
        }
    }


    public int getDamageValue(World world, int x, int y, int z) {
        TileEntity te = UtilBuilder.safeGetTileEntity(world, x, y, z);
        if (te instanceof TileMetalChest)
            return ((TileMetalChest) te).getMaterialId();
        return super.getDamageValue(world, x, y, z);
    }


    public int getRenderType() {
        return ClayiumCore.metalChestRenderId;
    }


    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }


    public boolean canChangeRenderType() {
        return false;
    }

    public void setInitialBlockBounds() {
        setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }


    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149673_1_, int p_149673_2_) {
        return Blocks.iron_block.getIcon(0, 0);
    }


    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        CMaterial material = CMaterials.getMaterialFromId(meta);
        return (material == null) ? super.getRenderColor(meta) : ((material.colors[0][0] << 16) + (material.colors[0][1] << 8) + material.colors[0][2]);
    }


    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        TileEntity te = UtilBuilder.safeGetTileEntity(world, x, y, z);
        if (te instanceof TileMetalChest) {
            return getRenderColor(((TileMetalChest) te).getMaterialId());
        }
        return super.colorMultiplier(world, x, y, z);
    }


    public String getUnlocalizedName(ItemStack itemStack) {
        CMaterial material = CMaterials.getMaterialFromId(itemStack.getItemDamage());
        return getUnlocalizedMetalChestName(this, material);
    }

    public static String getUnlocalizedMetalChestName(Block block, CMaterial material) {
        return block.getUnlocalizedName() + ((material == null) ? "" : ("." + material.name));
    }

    public List getTooltip(ItemStack itemStack) {
        List<String> res = new ArrayList();
        CMaterial material = CMaterials.getMaterialFromId(itemStack.getItemDamage());
        if (material != null) {
            if (getContainerP(material) <= 1 && UtilLocale.canLocalize("tooltip.MetalChest.capacity1")) {
                res.add(UtilLocale.localizeAndFormat("tooltip.MetalChest.capacity1", new Object[] {Integer.valueOf(getContainerX(material)), Integer.valueOf(getContainerY(material)), Integer.valueOf(getContainerX(material) * getContainerY(material))}));
            }
            if (getContainerP(material) >= 2 && UtilLocale.canLocalize("tooltip.MetalChest.capacity2")) {
                res.add(UtilLocale.localizeAndFormat("tooltip.MetalChest.capacity2", new Object[] {Integer.valueOf(getContainerX(material)), Integer.valueOf(getContainerY(material)), Integer.valueOf(getContainerP(material)), Integer.valueOf(getContainerX(material) * getContainerY(material) * getContainerP(material))}));
            }
        }


        return res;
    }

    public static void addChestMaterial(CMaterial material, int containerX, int containerY, int containerP, Configuration cfg) {
        containerX = cfg.getInt(material.name + "ChestWidth", "metalchest", containerX, 1, 13, "");
        containerY = cfg.getInt(material.name + "ChestHeight", "metalchest", containerY, 1, 8, "");
        containerP = cfg.getInt(material.name + "ChestNumberOfPages", "metalchest", containerP, 1, 8, "");
        addChestMaterial(material, containerX, containerY, containerP);
    }

    public static void addChestMaterial(CMaterial material, int containerX, int containerY, int containerP) {
        HashMap<String, Integer> value = new HashMap<String, Integer>();
        value.put("x", Integer.valueOf(containerX));
        value.put("y", Integer.valueOf(containerY));
        value.put("p", Integer.valueOf(containerP));
        metalChestMaterials.put(material, value);
    }

    public static boolean containsKey(CMaterial material) {
        return metalChestMaterials.containsKey(material);
    }

    public static int getContainerX(CMaterial material) {
        return containsKey(material) ? ((Integer) ((HashMap) metalChestMaterials.get(material)).get("x")).intValue() : 1;
    }

    public static int getContainerY(CMaterial material) {
        return containsKey(material) ? ((Integer) ((HashMap) metalChestMaterials.get(material)).get("y")).intValue() : 1;
    }

    public static int getContainerP(CMaterial material) {
        return containsKey(material) ? ((Integer) ((HashMap) metalChestMaterials.get(material)).get("p")).intValue() : 1;
    }

    public static HashMap<CMaterial, HashMap<String, Integer>> getChestMaterialMap() {
        return metalChestMaterials;
    }
}
