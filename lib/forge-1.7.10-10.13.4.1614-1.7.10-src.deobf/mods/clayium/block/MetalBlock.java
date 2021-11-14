package mods.clayium.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

import mods.clayium.block.itemblock.ItemBlockDamaged;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.TextureExtra;
import mods.clayium.item.CMaterial;
import mods.clayium.item.CMaterials;
import mods.clayium.util.UtilItemStack;
import mods.clayium.util.crafting.CRecipes;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;


public class MetalBlock
        extends BlockDamaged {
    public static Map<Integer, MetalBlock> metalBlockMap = new HashMap<Integer, MetalBlock>();

    public static void registerMaterials() {
        CMaterial[] materials = {CMaterials.MAGNESIUM, CMaterials.SODIUM, CMaterials.LITHIUM, CMaterials.ZIRCONIUM, CMaterials.ZINC, CMaterials.MANGANESE, CMaterials.CALCIUM, CMaterials.POTASSIUM, CMaterials.NICKEL, CMaterials.BERYLLIUM, CMaterials.LEAD, CMaterials.HAFNIUM, CMaterials.CHROME, CMaterials.TITANIUM, CMaterials.STRONTIUM, CMaterials.BARIUM, CMaterials.AZ91D_ALLOY, CMaterials.ZK60A_ALLOY, CMaterials.RUBIDIUM, CMaterials.CAESIUM, CMaterials.FRANCIUM, CMaterials.RADIUM, CMaterials.ACTINIUM, CMaterials.THORIUM, CMaterials.PROTACTINIUM, CMaterials.URANIUM, CMaterials.NEPTUNIUM, CMaterials.PLUTONIUM, CMaterials.AMERICIUM, CMaterials.CURIUM, CMaterials.LANTHANUM, CMaterials.CERIUM, CMaterials.PRASEODYMIUM, CMaterials.NEODYMIUM, CMaterials.PROMETHIUM, CMaterials.SAMARIUM, CMaterials.EUROPIUM, CMaterials.VANADIUM, CMaterials.COBALT, CMaterials.PALLADIUM, CMaterials.PLATINUM, CMaterials.IRIDIUM, CMaterials.OSMIUM, CMaterials.RHENIUM, CMaterials.TANTALUM, CMaterials.TUNGSTEN, CMaterials.MOLYBDENUM, CMaterials.ANTIMONY, CMaterials.BISMUTH, CMaterials.BRASS, CMaterials.ELECTRUM, CMaterials.INVAR, CMaterials.STEEL, CMaterials.COPPER, CMaterials.TIN, CMaterials.SILVER, CMaterials.BRONZE};


        for (CMaterial material : materials) {
            registerBlockMaterial(material);
        }
    }


    public static void registerBlockMaterial(CMaterial material) {
        if (!CMaterials.exist(material, CMaterials.INGOT))
            return;
        boolean oreDictionary = UtilItemStack.hasOreName(CMaterials.get(material, CMaterials.INGOT), CMaterials.getODName(material, CMaterials.INGOT));
        if (!metalBlockMap.containsKey(Integer.valueOf(material.meta / 16))) {
            MetalBlock metalBlock = new MetalBlock();
            GameRegistry.registerBlock(metalBlock, ItemBlockDamaged.class, "blockMetal" + String.format("%03d", new Object[] {Integer.valueOf(material.meta / 16)}));
            metalBlockMap.put(Integer.valueOf(material.meta / 16), metalBlock);
        }
        MetalBlock block = getBlock(material);
        block.registerMaterial(material);
        CMaterials.registerMaterialShape(material, CMaterials.BLOCK, block.get(material.name));

        if (oreDictionary) {
            CMaterials.addItemToOD(material, CMaterials.BLOCK);

            GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
                    CMaterials.get(material, CMaterials.BLOCK),
                    CRecipes.oo(new Object[] {CMaterials.getODName(material, CMaterials.INGOT), CMaterials.getODName(material, CMaterials.INGOT), CMaterials.getODName(material, CMaterials.INGOT),
                            CMaterials.getODName(material, CMaterials.INGOT), CMaterials.getODName(material, CMaterials.INGOT), CMaterials.getODName(material, CMaterials.INGOT),
                            CMaterials.getODName(material, CMaterials.INGOT), CMaterials.getODName(material, CMaterials.INGOT), CMaterials.getODName(material, CMaterials.INGOT)})));
            GameRegistry.addRecipe((IRecipe) new ShapelessOreRecipe(
                    CMaterials.get(material, CMaterials.INGOT, 9),
                    CRecipes.oo(new Object[] {CMaterials.getODName(material, CMaterials.BLOCK)})));
        } else {

            GameRegistry.addShapelessRecipe(CMaterials.get(material, CMaterials.BLOCK),
                    CRecipes.oo(new Object[] {CMaterials.get(material, CMaterials.INGOT), CMaterials.get(material, CMaterials.INGOT), CMaterials.get(material, CMaterials.INGOT),
                            CMaterials.get(material, CMaterials.INGOT), CMaterials.get(material, CMaterials.INGOT), CMaterials.get(material, CMaterials.INGOT),
                            CMaterials.get(material, CMaterials.INGOT), CMaterials.get(material, CMaterials.INGOT), CMaterials.get(material, CMaterials.INGOT)}));
            GameRegistry.addShapelessRecipe(CMaterials.get(material, CMaterials.INGOT, 9),
                    CRecipes.oo(new Object[] {CMaterials.get(material, CMaterials.BLOCK)}));
        }
    }


    public static MetalBlock getBlock(CMaterial material) {
        return metalBlockMap.get(Integer.valueOf(material.meta / 16));
    }

    public CMaterial[] materials = new CMaterial[16];

    public MetalBlock() {
        super(Material.iron);
        setStepSound(soundTypeMetal);
        setCreativeTab(ClayiumCore.creativeTabClayium);
        setHardness(2.0F);
        setResistance(2.0F);
        setBlockName("blockMetal");
    }


    public void registerMaterial(CMaterial material) {
        addBlockList(material.name, material.meta % 16);
        this.materials[material.meta % 16] = material;
    }


    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        for (int i = 0; i < 16; i++) {
            if (isRegistered(i)) {
                TextureExtra tex = new TextureExtra("clayium:metalblock_" + (this.materials[i]).name, new String[] {"clayium:metalblock_base", "clayium:metalblock_dark", "clayium:metalblock_light"});
                int[] colors = new int[(this.materials[i]).colors.length];
                for (int j = 0; j < (this.materials[i]).colors.length; j++) {
                    colors[j] = ((this.materials[i]).colors[j][0] << 16) + ((this.materials[i]).colors[j][1] << 8) + (this.materials[i]).colors[j][2] + -16777216;
                }
                tex.setColorTable(colors);
                setIcon(i, tex.register(register));
            }
        }
    }
}
