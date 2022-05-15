package mods.clayium.item;

import cpw.mods.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mods.clayium.block.CBlocks;
import mods.clayium.block.MetalBlock;
import mods.clayium.block.MetalChest;
import mods.clayium.core.ClayiumCore;
import mods.clayium.gui.GenericMaterialIcon;
import mods.clayium.gui.IMultipleRenderIcons;
import mods.clayium.util.crafting.OreDictionaryStack;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;


public class CMaterials {
    public static Map<CMaterial, Map<CShape, ItemStack>> materialShapeMap = new HashMap<CMaterial, Map<CShape, ItemStack>>();
    public static Map<CMaterial, ItemDamaged> materialMap = new HashMap<CMaterial, ItemDamaged>();
    public static Map<CShape, ItemDamaged> shapeMap = new HashMap<CShape, ItemDamaged>();

    public static Map<Integer, CMaterial> materialList = new HashMap<Integer, CMaterial>();

    public static ItemDamaged itemClayParts;

    public static ItemDamaged itemDenseClayParts;

    public static CMaterial CLAY;

    public static CMaterial DENSE_CLAY;

    public static CMaterial IND_CLAY;

    public static CMaterial ADVIND_CLAY;

    public static CMaterial ENG_CLAY;

    public static CMaterial EXC_CLAY;

    public static CMaterial ORG_CLAY;

    public static CMaterial IRON;

    public static CMaterial GOLD;

    public static CMaterial ALUMINIUM;

    public static CMaterial SILICON;

    public static CMaterial MAGNESIUM;

    public static CMaterial SODIUM;

    public static CMaterial LITHIUM;

    public static CMaterial ZIRCONIUM;

    public static CMaterial ZINC;

    public static CMaterial MAIN_ALUMINIUM;

    public static CMaterial MANGANESE;

    public static CMaterial CALCIUM;

    public static CMaterial POTASSIUM;

    public static CMaterial NICKEL;

    public static CMaterial BERYLLIUM;

    public static CMaterial LEAD;

    public static CMaterial HAFNIUM;

    public static CMaterial CHROME;

    public static CMaterial TITANIUM;

    public static CMaterial STRONTIUM;

    public static CMaterial BARIUM;

    public static CMaterial COPPER;

    public static CMaterial IMPURE_ALUMINIUM;

    public static CMaterial IMPURE_SILICON;

    public static CMaterial IMPURE_MAGNESIUM;

    public static CMaterial IMPURE_SODIUM;

    public static CMaterial IMPURE_LITHIUM;

    public static CMaterial IMPURE_ZIRCONIUM;

    public static CMaterial IMPURE_ZINC;

    public static CMaterial IMPURE_MANGANESE;

    public static CMaterial IMPURE_CALCIUM;

    public static CMaterial IMPURE_POTASSIUM;

    public static CMaterial IMPURE_NICKEL;

    public static CMaterial IMPURE_IRON;

    public static CMaterial IMPURE_BERYLLIUM;

    public static CMaterial IMPURE_LEAD;

    public static CMaterial IMPURE_HAFNIUM;

    public static CMaterial IMPURE_CHROME;

    public static CMaterial IMPURE_TITANIUM;

    public static CMaterial IMPURE_STRONTIUM;

    public static CMaterial IMPURE_BARIUM;

    public static CMaterial IMPURE_COPPER;

    public static CMaterial IMPURE_REDSTONE;

    public static CMaterial IMPURE_GLOWSTONE;

    public static CMaterial MAIN_OSMIUM;

    public static CMaterial IMPURE_OSMIUM;

    public static CMaterial SALT;

    public static CMaterial CAL_CLAY;

    public static CMaterial CALCIUM_CHLORIDE;

    public static CMaterial SODIUM_CARBONATE;

    public static CMaterial QUARTZ;

    public static CMaterial SILICONE;

    public static CMaterial CLAY_STEEL;

    public static CMaterial CLAYIUM;

    public static CMaterial IMPURE_ULTIMATE_ALLOY;

    public static CMaterial ULTIMATE_ALLOY;

    public static CMaterial ANTIMATTER;

    public static CMaterial PURE_ANTIMATTER;

    public static CMaterial[] COMPRESSED_PURE_ANTIMATTER;

    public static CMaterial OCTUPLE_CLAY;

    public static CMaterial OCTUPLE_PURE_ANTIMATTER;

    public static CMaterial ZINCALMINIUM_ALLOY;

    public static CMaterial AZ91D_ALLOY;

    public static CMaterial ZINCONIUM_ALLOY;

    public static CMaterial ZK60A_ALLOY;

    public static CMaterial RUBIDIUM;

    public static CMaterial CAESIUM;

    public static CMaterial FRANCIUM;

    public static CMaterial RADIUM;

    public static CMaterial ACTINIUM;

    public static CMaterial THORIUM;

    public static CMaterial PROTACTINIUM;

    public static CMaterial URANIUM;

    public static CMaterial NEPTUNIUM;

    public static CMaterial PLUTONIUM;

    public static CMaterial AMERICIUM;

    public static CMaterial CURIUM;

    public static CMaterial LANTHANUM;

    public static CMaterial CERIUM;

    public static CMaterial PRASEODYMIUM;

    public static CMaterial NEODYMIUM;

    public static CMaterial PROMETHIUM;

    public static CMaterial SAMARIUM;

    public static CMaterial EUROPIUM;

    public static CMaterial VANADIUM;

    public static CMaterial COBALT;

    public static CMaterial PALLADIUM;

    public static CMaterial SILVER;

    public static CMaterial PLATINUM;

    public static CMaterial IRIDIUM;

    public static CMaterial OSMIUM;

    public static CMaterial RHENIUM;

    public static CMaterial TANTALUM;

    public static CMaterial TUNGSTEN;

    public static CMaterial MOLYBDENUM;

    public static CMaterial TIN;

    public static CMaterial ANTIMONY;

    public static CMaterial BISMUTH;

    public static CMaterial BRONZE;

    public static CMaterial BRASS;

    public static CMaterial ELECTRUM;

    public static CMaterial INVAR;

    public static CMaterial STEEL;

    public static CMaterial OBSIDIAN;

    public static CMaterial REDSTONE;

    public static CMaterial GLOWSTONE;

    public static CMaterial ENDER_PEARL;

    public static CMaterial COAL;

    public static CMaterial CHARCOAL;

    public static CMaterial LAPIS;

    public static CMaterial DIAMOND;

    public static CMaterial EMERALD;

    public static CMaterial STONE;

    public static CMaterial CARBON;

    public static CMaterial PHOSPHORUS;

    public static CMaterial SULFUR;

    public static CMaterial PLASTIC;

    public static CMaterial CINNABAR;

    public static CMaterial SALTPETER;

    public static CMaterial RUBY;

    public static CMaterial SAPPHIRE;

    public static CMaterial PERIDOT;

    public static CMaterial AMBER;

    public static CMaterial AMETHYST;

    public static CMaterial REDSTONE_ALLOY;

    public static CMaterial CONDUCTIVE_IRON;

    public static CMaterial ENERGETIC_ALLOY;

    public static CMaterial ELECTRICAL_STEEL;

    public static CMaterial DARK_STEEL;

    public static CMaterial PHASED_IRON;

    public static CMaterial PHASED_GOLD;

    public static CMaterial SOULARIUM;

    public static CMaterial SIGNALUM;

    public static CMaterial LUMIUM;

    public static CMaterial ENDERIUM;
    public static CMaterial ELECTRUM_FLUX;
    public static CMaterial CRYSTAL_FLUX;
    public static CMaterial APATITE;
    public static CMaterial CERTUS_QUARTZ;
    public static CMaterial FLUIX;
    public static CMaterial ARDITE;
    public static CMaterial ALUMINUM_BRASS;

    public static void registerMaterials() {
        if (!ClayiumCore.cfgUtilityMode) {
            itemClayParts = createItem("itemClayParts");
            itemDenseClayParts = createItem("itemDenseClayParts");


            CLAY = registerMaterial("Clay", "clay", 512, itemClayParts);
            DENSE_CLAY = registerMaterial("DenseClay", "denseclay", 513, itemDenseClayParts);
            IND_CLAY = registerMaterial("IndustrialClay", "indclay", 515);
            ADVIND_CLAY = registerMaterial("AdvancedIndustrialClay", "advindclay", 516);
            ENG_CLAY = registerMaterial("EnergizedClay", "engclay", 768);

            IMPURE_SILICON = registerMaterial("ImpureSilicon", "impuresilicon", 142);
            IMPURE_SILICON.setColor(151, 143, 152, 0).setColor(83, 55, 100, 1).setColor(169, 165, 165, 2);
            SILICON = registerMaterial("Silicon", "silicon", 14);
            SILICON.setColor(40, 28, 40).setColor(255, 255, 255, 2);
            IRON = registerMaterial("Iron", "iron", 26);
            IRON.setColor(216, 216, 216, 0).setColor(53, 53, 53, 1).setColor(255, 255, 255, 2);
            GOLD = registerMaterial("Gold", "gold", 79);
            GOLD.setColor(255, 255, 10, 0).setColor(60, 60, 0, 1).setColor(255, 255, 255, 2);
            ALUMINIUM = registerMaterial("Aluminium", "alminium", "Aluminum", 13);
            ALUMINIUM.setColor(190, 200, 202).setColor(255, 255, 255, 2);
            MAGNESIUM = registerMaterial("Magnesium", "magnesium", 12);
            MAGNESIUM.setColor(150, 210, 150).setColor(120, 120, 120, 1);
            SODIUM = registerMaterial("Sodium", "sodium", 11);
            SODIUM.setColor(170, 170, 222).setColor(120, 120, 120, 1);
            LITHIUM = registerMaterial("Lithium", "lithium", 3);
            LITHIUM.setColor(210, 210, 150).setColor(120, 120, 120, 1);
            ZIRCONIUM = registerMaterial("Zirconium", "zirconium", 40);
            ZIRCONIUM.setColor(190, 170, 122).setColor(120, 120, 120, 1);
            ZINC = registerMaterial("Zinc", "zinc", 30);
            ZINC.setColor(230, 170, 170).setColor(120, 120, 120, 1);

            MANGANESE = registerMaterial("Manganese", "manganese", 25);
            MANGANESE.setColor(190, 240, 240);
            CALCIUM = registerMaterial("Calcium", "calcium", 20);
            CALCIUM.setColor(240, 240, 240);
            POTASSIUM = registerMaterial("Potassium", "potassium", 19);
            POTASSIUM.setColor(240, 240, 190);
            NICKEL = registerMaterial("Nickel", "nickel", 28);
            NICKEL.setColor(210, 210, 240, 0);
            BERYLLIUM = registerMaterial("Beryllium", "beryllium", 4);
            BERYLLIUM.setColor(210, 240, 210);
            LEAD = registerMaterial("Lead", "lead", 82);
            LEAD.setColor(190, 240, 210);
            HAFNIUM = registerMaterial("Hafnium", "hafnium", 72);
            HAFNIUM.setColor(240, 210, 170);
            CHROME = registerMaterial("Chrome", "chrome", 24);
            CHROME.setColor(240, 210, 210);
            TITANIUM = registerMaterial("Titanium", "titanium", 22);
            TITANIUM.setColor(210, 240, 240);
            STRONTIUM = registerMaterial("Strontium", "strontium", 38);
            STRONTIUM.setColor(210, 170, 242);
            BARIUM = registerMaterial("Barium", "barium", 56);
            BARIUM.setColor(150, 80, 120).setColor(120, 20, 80, 1);
            COPPER = registerMaterial("Copper", "copper", 29);
            COPPER.setColor(160, 90, 10).setColor(255, 255, 255, 2);

            ZINCALMINIUM_ALLOY = registerMaterial("Zincalminium", "zincalminium", 1344);
            ZINCALMINIUM_ALLOY.setColor(240, 190, 220).setColor(160, 0, 0, 1);
            AZ91D_ALLOY = registerMaterial("AZ91D", "az91d", 1312);
            AZ91D_ALLOY.setColor(130, 140, 135).setColor(255, 255, 255, 2).setColor(10, 40, 10, 1);
            ZINCONIUM_ALLOY = registerMaterial("Zinconium", "zinconium", 1345);
            ZINCONIUM_ALLOY.setColor(230, 170, 140).setColor(120, 0, 0, 1);
            ZK60A_ALLOY = registerMaterial("ZK60A", "zk60a", 1313);
            ZK60A_ALLOY.setColor(75, 85, 80).setColor(255, 255, 255, 2).setColor(10, 40, 10, 1);

            IMPURE_ALUMINIUM = registerMaterial("ImpureAluminium", "alminium", 141);
            IMPURE_ALUMINIUM.setColor(190, 200, 202).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_MAGNESIUM = registerMaterial("ImpureMagnesium", "impuremagnesium", 140);
            IMPURE_MAGNESIUM.setColor(150, 220, 150).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_SODIUM = registerMaterial("ImpureSodium", "impuresodium", 139);
            IMPURE_SODIUM.setColor(170, 170, 230).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_LITHIUM = registerMaterial("ImpureLithium", "impurelithium", 131);
            IMPURE_LITHIUM.setColor(220, 220, 150).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_ZIRCONIUM = registerMaterial("ImpureZirconium", "impurezirconium", 168);
            IMPURE_ZIRCONIUM.setColor(190, 170, 122).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_ZINC = registerMaterial("ImpureZinc", "impurezinc", 158);
            IMPURE_ZINC.setColor(230, 170, 170).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);

            IMPURE_MANGANESE = registerMaterial("ImpureManganese", "impuremanganese", 153);
            IMPURE_MANGANESE.setColor(190, 240, 240).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_CALCIUM = registerMaterial("ImpureCalcium", "impurecalcium", 148);
            IMPURE_CALCIUM.setColor(240, 240, 240).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_POTASSIUM = registerMaterial("ImpurePotassium", "impurepotassium", 147);
            IMPURE_POTASSIUM.setColor(240, 240, 190).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_NICKEL = registerMaterial("ImpureNickel", "impurenickel", 156);
            IMPURE_NICKEL.setColor(210, 210, 240, 0).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_IRON = registerMaterial("ImpureIron", "impureiron", 154);
            IMPURE_IRON.setColor(216, 216, 216, 0).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_BERYLLIUM = registerMaterial("ImpureBeryllium", "impureberyllium", 132);
            IMPURE_BERYLLIUM.setColor(210, 240, 210).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_LEAD = registerMaterial("ImpureLead", "impurelead", 210);
            IMPURE_LEAD.setColor(190, 240, 210).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_HAFNIUM = registerMaterial("ImpureHafnium", "impurehafnium", 200);
            IMPURE_HAFNIUM.setColor(240, 210, 170).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_CHROME = registerMaterial("ImpureChrome", "impurechrome", 152);
            IMPURE_CHROME.setColor(240, 210, 210).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_TITANIUM = registerMaterial("ImpureTitanium", "impuretitanium", 150);
            IMPURE_TITANIUM.setColor(210, 240, 240).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_STRONTIUM = registerMaterial("ImpureStrontium", "impurestrontium", 166);
            IMPURE_STRONTIUM.setColor(210, 170, 242).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_BARIUM = registerMaterial("ImpureBarium", "impurebarium", 184);
            IMPURE_BARIUM.setColor(150, 80, 120).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
            IMPURE_COPPER = registerMaterial("ImpureCopper", "impurecopper", 157);
            IMPURE_COPPER.setColor(160, 90, 10).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);


            IMPURE_OSMIUM = registerMaterial("ImpureOsmium", 204).setColor(70, 70, 150).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);

            SALT = registerMaterial("Salt", "salt", 1024);
            CAL_CLAY = registerMaterial("CalcareousClay", "calclay", 769);
            CALCIUM_CHLORIDE = registerMaterial("CalciumChloride", "calciumchloride", 1025);
            SODIUM_CARBONATE = registerMaterial("SodiumCarbonate", "sodiumcarbonate", 1026);
            QUARTZ = registerMaterial("Quartz", "quartz", 1027);
            SILICONE = registerMaterial("Silicone", "silicone", 1028);
            (SILICONE.setColor(180, 180, 180, 240, 240, 240)).hardness = 0.2F;

            EXC_CLAY = registerMaterial("ExcitedClay", "excclay", 770);

            CLAY_STEEL = registerMaterial("ClaySteel", "claysteel", 256);
            (CLAY_STEEL.setColor(136, 144, 173).setColor(255, 255, 255, 2)).hardness = 3.0F;
            CLAYIUM = registerMaterial("Clayium", "clayium", 257);
            (CLAYIUM.setColor(90, 240, 210).setColor(63, 72, 85, 1).setColor(255, 205, 200, 2)).hardness = 6.0F;
            ULTIMATE_ALLOY = registerMaterial("UltimateAlloy", "ultimatealloy", 258);
            (ULTIMATE_ALLOY.setColor(85, 205, 85).setColor(245, 160, 255, 2)).hardness = 9.0F;

            ORG_CLAY = registerMaterial("OrganicClay", "orgclay", 771);
            ORG_CLAY.setColor(136, 144, 173).setColor(106, 44, 43, 1).setColor(146, 164, 183, 2);

            ANTIMATTER = registerMaterial("Antimatter", 800);
            ANTIMATTER.setColor(0, 0, 235).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
            PURE_ANTIMATTER = registerMaterial("PureAntimatter", 801);
            PURE_ANTIMATTER.setColor(255, 50, 255).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
            COMPRESSED_PURE_ANTIMATTER = new CMaterial[9];
            COMPRESSED_PURE_ANTIMATTER[0] = PURE_ANTIMATTER;
            for (int i = 1; i <= 8; i++) {
                if (i != 8) {
                    COMPRESSED_PURE_ANTIMATTER[i] = registerMaterial("PureAntimatter" + i, 801 + i);
                } else {
                    COMPRESSED_PURE_ANTIMATTER[i] = registerMaterial("OctuplePureAntimatter", 801 + i);
                }
                double r = i / 8.0D;
                double l = 1.0D - ((r < 0.5D) ? r : (1.0D - r)) * 1.5D;
                COMPRESSED_PURE_ANTIMATTER[i].setColor((int) (l * (255.0D * (1.0D - r) + 150.0D * r)), (int) (l * (50.0D * (1.0D - r) + 0.0D * r)), (int) (l * (255.0D * (1.0D - r) + 0.0D * r))).setColor((int) (200.0D * r), (int) (200.0D * r), 0, 1).setColor(255, 255, 255, 2);
            }
            OCTUPLE_CLAY = registerMaterial("OctupleEnergeticClay", 525);
            OCTUPLE_CLAY.setColor(255, 255, 0).setColor(140, 140, 140, 1).setColor(255, 255, 255, 2);
            OCTUPLE_PURE_ANTIMATTER = COMPRESSED_PURE_ANTIMATTER[8];

            IMPURE_ULTIMATE_ALLOY = registerMaterial("ImpureUltimateAlloy", "impureultimatealloy", 386);
            (IMPURE_ULTIMATE_ALLOY.setColor(85, 205, 85).setColor(245, 160, 255, 2).setColor(245, 255, 255, 1)).hardness = 9.0F;

            IMPURE_REDSTONE = registerMaterial("ImpureRedstone", "impureredstone", 897);
            IMPURE_REDSTONE.setColor(151, 70, 70);
            IMPURE_GLOWSTONE = registerMaterial("ImpureGlowstone", "impureglowstone", 898);
            IMPURE_GLOWSTONE.setColor(151, 151, 70);

            RUBIDIUM = registerMaterial("Rubidium", 37).setColor(245, 245, 245).setColor(235, 0, 0, 1);
            CAESIUM = registerMaterial("Caesium", 55).setColor(245, 245, 245).setColor(150, 150, 0, 1);
            FRANCIUM = registerMaterial("Francium", 87).setColor(245, 245, 245).setColor(0, 235, 0, 1);
            RADIUM = registerMaterial("Radium", 88).setColor(245, 245, 245).setColor(0, 150, 150, 1);
            ACTINIUM = registerMaterial("Actinium", 89).setColor(245, 245, 245).setColor(0, 0, 235, 1);
            THORIUM = registerMaterial("Thorium", 90).setColor(50, 50, 50).setColor(200, 50, 50, 2);
            PROTACTINIUM = registerMaterial("Protactinium", 91).setColor(50, 50, 50).setColor(50, 50, 100, 2);
            URANIUM = registerMaterial("Uranium", 92).setColor(50, 255, 50).setColor(50, 155, 50, 1).setColor(50, 255, 50, 2);
            NEPTUNIUM = registerMaterial("Neptunium", 93).setColor(50, 50, 255).setColor(50, 50, 155, 1).setColor(50, 50, 255, 2);
            PLUTONIUM = registerMaterial("Plutonium", 94).setColor(255, 50, 50).setColor(155, 50, 50, 1).setColor(255, 50, 50, 2);
            AMERICIUM = registerMaterial("Americium", 95).setColor(235, 235, 235).setColor(155, 155, 155, 1).setColor(235, 235, 235, 2);
            CURIUM = registerMaterial("Curium", 96).setColor(255, 255, 255).setColor(155, 155, 155, 1).setColor(244, 244, 244, 2);

            LANTHANUM = registerMaterial("Lanthanum", 57).setColor(145, 145, 145).setColor(235, 0, 0, 1);
            CERIUM = registerMaterial("Cerium", 58).setColor(145, 145, 145).setColor(150, 150, 0, 1);
            PRASEODYMIUM = registerMaterial("Praseodymium", 59).setColor(145, 145, 145).setColor(0, 235, 0, 1);
            NEODYMIUM = registerMaterial("Neodymium", 60).setColor(145, 145, 145).setColor(0, 150, 150, 1);
            PROMETHIUM = registerMaterial("Promethium", 61).setColor(145, 145, 145).setColor(0, 0, 235, 1);
            SAMARIUM = registerMaterial("Samarium", 62).setColor(145, 145, 145).setColor(150, 0, 150, 1);
            EUROPIUM = registerMaterial("Europium", 63).setColor(145, 145, 145).setColor(55, 55, 55, 1).setColor(145, 145, 145, 2);

            VANADIUM = registerMaterial("Vanadium", 23).setColor(60, 120, 120);

            COBALT = registerMaterial("Cobalt", 27).setColor(30, 30, 230);
            PALLADIUM = registerMaterial("Palladium", 46).setColor(151, 70, 70);

            SILVER = registerMaterial("Silver", 47).setColor(230, 230, 245).setColor(120, 120, 140, 1).setColor(255, 255, 255, 2);
            PLATINUM = registerMaterial("Platinum", 78).setColor(245, 245, 230).setColor(140, 140, 120, 1).setColor(255, 255, 255, 2);
            IRIDIUM = registerMaterial("Iridium", 77).setColor(240, 240, 240).setColor(210, 210, 210, 1).setColor(235, 235, 235, 2);
            OSMIUM = registerMaterial("Osmium", 76).setColor(70, 70, 150);
            RHENIUM = registerMaterial("Rhenium", 75).setColor(70, 70, 150).setColor(50, 50, 90, 2);

            TANTALUM = registerMaterial("Tantalum", 73).setColor(240, 210, 170).setColor(240, 210, 150, 2);
            TUNGSTEN = registerMaterial("Tungsten", 74).setColor(30, 30, 30);
            MOLYBDENUM = registerMaterial("Molybdenum", 42).setColor(130, 160, 130);

            TIN = registerMaterial("Tin", 50).setColor(230, 230, 240).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
            ANTIMONY = registerMaterial("Antimony", 51).setColor(70, 70, 70);
            BISMUTH = registerMaterial("Bismuth", 83).setColor(70, 120, 70);

            BRONZE = registerMaterial("Bronze", 1280).setColor(250, 150, 40).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
            BRASS = registerMaterial("Brass", 1281).setColor(190, 170, 20).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
            ELECTRUM = registerMaterial("Electrum", 1283).setColor(230, 230, 155).setColor(120, 120, 70, 1).setColor(255, 255, 255, 2);
            INVAR = registerMaterial("Invar", 1284).setColor(170, 170, 80).setColor(140, 140, 70, 1).setColor(180, 180, 80, 2);
            STEEL = registerMaterial("Steel", 1536).setColor(90, 90, 110).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);


            OBSIDIAN = registerMaterial("Obsidian", 896);
            REDSTONE = registerMaterial("Redstone", 899);
            GLOWSTONE = registerMaterial("Glowstone", 900);
            ENDER_PEARL = registerMaterial("EnderPearl", 901);

            COAL = registerMaterial("Coal", 1792).setColor(20, 20, 20).setColor(50, 50, 80, 2);
            CHARCOAL = registerMaterial("Charcoal", 1793).setColor(20, 20, 20).setColor(80, 50, 50, 2);
            LAPIS = registerMaterial("Lapis", 1824).setColor(60, 100, 190).setColor(10, 43, 122, 1).setColor(90, 130, 226, 2);
            DIAMOND = registerMaterial("Diamond", 1856);
            EMERALD = registerMaterial("Emerald", 1857);

            STONE = registerMaterial("Stone", 2048);


            CARBON = registerMaterial("Carbon", 6).setColor(10, 10, 10).setColor(30, 30, 30, 2);
            PHOSPHORUS = registerMaterial("Phosphorus", 15).setColor(155, 155, 0, 205, 205, 50);
            SULFUR = registerMaterial("Sulfur", 16).setColor(205, 205, 0, 255, 255, 0);

            PLASTIC = registerMaterial("Plastic", 1032);
            CINNABAR = registerMaterial("Cinnabar", 1040);
            SALTPETER = registerMaterial("Saltpeter", 1041).setColor(190, 200, 210, 255, 240, 230);

            RUBY = registerMaterial("Ruby", 1858);
            SAPPHIRE = registerMaterial("Sapphire", 1859);
            PERIDOT = registerMaterial("Peridot", 1860);
            AMBER = registerMaterial("Amber", 1861);
            AMETHYST = registerMaterial("Amethyst", 1862);


            REDSTONE_ALLOY = registerMaterial("RedstoneAlloy", 1408);
            CONDUCTIVE_IRON = registerMaterial("ConductiveIron", 1409);
            ENERGETIC_ALLOY = registerMaterial("EnergeticAlloy", 1410);
            ELECTRICAL_STEEL = registerMaterial("ElectricalSteel", 1411);
            DARK_STEEL = registerMaterial("DarkSteel", 1412);
            PHASED_IRON = registerMaterial("PhasedIron", 1413);
            PHASED_GOLD = registerMaterial("PhasedGold", 1414);
            SOULARIUM = registerMaterial("Soularium", 1415);


            SIGNALUM = registerMaterial("Signalum", 1424);
            LUMIUM = registerMaterial("Lumium", 1425);
            ENDERIUM = registerMaterial("Enderium", 1426);
            ELECTRUM_FLUX = registerMaterial("ElectrumFlux", 1428);

            CRYSTAL_FLUX = registerMaterial("CrystalFlux", 2016);


            APATITE = registerMaterial("Apatite", 1044);


            CERTUS_QUARTZ = registerMaterial("CertusQuartz", 1048);
            FLUIX = registerMaterial("Fluix", 1049);


            ARDITE = registerMaterial("Ardite", 320);

            ALUMINUM_BRASS = registerMaterial("AluminumBrass", 1432);
            PIG_IRON = registerMaterial("PigIron", 1433);
            ALUMITE = registerMaterial("Alumite", 1434);
            MANYULLYN = registerMaterial("Manyullyn", 1435);

            FAIRY = registerMaterial("Fairy", 1440);
            POKEFENNIUM = registerMaterial("Pokefennium", 1441);
            RED_AURUM = registerMaterial("Red_aurum", 1442);
            DRULLOY = registerMaterial("Drulloy", 1443);


            RED_ALLOY = registerMaterial("RedAlloy", 1448);
            ELECTROTINE = registerMaterial("Electrotine", 1060);
            ELECTROTINE_ALLOY = registerMaterial("ElectrotineAlloy", 1449);


            REFINED_GLOWSTONE = registerMaterial("RefinedGlowstone", 1600);
            REFINED_OBSIDIAN = registerMaterial("RefinedObsidian", 1601);


            UNSTABLE = registerMaterial("Unstable", 1728);


            HSLA = registerMaterial("HSLA", 1608);


            YELLORIUM = registerMaterial("Yellorium", 324);
            CYANITE = registerMaterial("Cyanite", 325);
            BLUTONIUM = registerMaterial("Blutonium", 326);
            LUDICRITE = registerMaterial("Ludicrite", 327);
            GRAPHITE = registerMaterial("Graphite", 1056);


            METEORIC_IRON = registerMaterial("MeteoricIron", 336);
            DESH = registerMaterial("Desh", 337);


            IRON_COMPRESSED = registerMaterial("IronCompressed", 1604);


            FZ_DARK_IRON = registerMaterial("FzDarkIron", 332);


            THAUMIUM = registerMaterial("Thaumium", 1664);
            VOID = registerMaterial("Void", 1665);


            MANASTEEL = registerMaterial("Manasteel", 1668);
            TERRASTEEL = registerMaterial("Terrasteel", 1669);
            ELVEN_ELEMENTIUM = registerMaterial("ElvenElementium", 1670);


            TOPAZ = registerMaterial("Manasteel", 1863);
            MALACHITE = registerMaterial("Malachite", 1864);
            TANZANITE = registerMaterial("Tanzanite", 1865);


            HEE_ENDIUM = registerMaterial("HeeEndium", 1696);


            DILITHIUM = registerMaterial("Dilithium", 1880);


            FORCICIUM = registerMaterial("Forcicium", 1884);


            GALLIUM = registerMaterial("Gallium", 31);
            YTTRIUM = registerMaterial("Yttrium", 39);
            NIOBIUM = registerMaterial("Niobium", 41);
            URANIUM_235 = registerMaterial("Uranium235", 288);
            PLUTONIUM_241 = registerMaterial("Plutonium241", 289);
            NAQUADAH = registerMaterial("Naquadah", 296);
            NAQUADAH_ENRICHED = registerMaterial("NaquadahEnriched", 297);
            NAQUADRIA = registerMaterial("Naquadria", 298);
            NEUTRONIUM = registerMaterial("Neutronium", 299);

            NIKOLITE = registerMaterial("Nikolite", 1064);
            QUARTZITE = registerMaterial("Quartzite", 1088);
            MONAZITE = registerMaterial("Monazite", 1089);
            NITER = registerMaterial("Niter", 1090);

            TUNGSTEN_STEEL = registerMaterial("TungstenSteel", 1472);
            CUPRONICKEL = registerMaterial("Cupronickel", 1473);
            NICHROME = registerMaterial("Nichrome", 1474);
            KANTHAL = registerMaterial("Kanthal", 1475);
            STAINLESS_STEEL = registerMaterial("StainlessSteel", 1476);
            COBALT_BRASS = registerMaterial("CobaltBrass", 1477);
            MAGNALIUM = registerMaterial("Magnalium", 1478);
            SOLDERING_ALLOY = registerMaterial("SolderingAlloy", 1479);
            BATTERY_ALLOY = registerMaterial("BatteryAlloy", 1480);
            VANADIUM_GALLIUM = registerMaterial("VanadiumGallium", 1481);
            YTTRIUM_BARIUM_CUPRATE = registerMaterial("YttriumBariumCuprate", 1482);
            NIOBIUM_TITANIUM = registerMaterial("NiobiumTitanium", 1483);
            ULTIMET = registerMaterial("Ultimet", 1484);
            TIN_ALLOY = registerMaterial("TinAlloy", 1485);
            BLUE_ALLOY = registerMaterial("BlueAlloy", 1486);

            WROUGHT_IRON = registerMaterial("WroughtIron", 1552);
            ANNEALED_COPPER = registerMaterial("AnnealedCopper", 1553);
            IRON_MAGNETIC = registerMaterial("IronMagnetic", 1568);
            STEEL_MAGNETIC = registerMaterial("SteelMagnetic", 1569);
            NEODYMIUM_MAGNETIC = registerMaterial("NeodymiumMagnetic", 1570);

            LIGNITE = registerMaterial("Lignite", 1808);
            LAZURITE = registerMaterial("Lazurite", 1840);
            SODALITE = registerMaterial("Sodalite", 1841);
            GREEN_SAPPHIRE = registerMaterial("GreenSaphire", 1888);
            GARNET_RED = registerMaterial("GarnetRed", 1892);
            GARNET_YELLOW = registerMaterial("GarnetYellow", 1893);
            OPAL = registerMaterial("Opal", 1896);
            JASPER = registerMaterial("Jasper", 1897);
            BLUE_TOPAZ = registerMaterial("BlueTopaz", 1920);
            FORCE = registerMaterial("Force", 1984);
            FORCILLIUM = registerMaterial("Forcillium", 1988);
            GLASS = registerMaterial("Glass", 2176);


            PROMETHEUM = registerMaterial("Prometheum", 352);
            DEEP_IRON = registerMaterial("DeepIron", 353);
            INFUSCOLIUM = registerMaterial("Infuscolium", 354);
            OURECLASE = registerMaterial("Oureclase", 355);
            AREDRITE = registerMaterial("Aredrite", 356);
            ASTRAL_SILVER = registerMaterial("AstralSilver", 357);
            CARMOT = registerMaterial("Carmot", 358);
            MITHRIL = registerMaterial("Mithril", 359);
            RUBRACIUM = registerMaterial("Rubracium", 360);
            ORICHALCUM = registerMaterial("Orichalcum", 361);
            ADAMANTINE = registerMaterial("Adamantine", 362);
            ATLARUS = registerMaterial("Atlarus", 363);

            IGNATIUS = registerMaterial("Ignatius", 368);
            SHADOW_IRON = registerMaterial("ShadowIron", 369);
            LEMURITE = registerMaterial("Lemurite", 370);
            MIDASIUM = registerMaterial("Midasium", 371);
            VYROXERES = registerMaterial("Vyroxeres", 372);
            CERUCLASE = registerMaterial("Ceruclase", 373);
            ALDUORITE = registerMaterial("Alduorite", 374);
            KALENDRITE = registerMaterial("Kalendrite", 375);
            VULCANITE = registerMaterial("Vulcanite", 376);
            SANGUINITE = registerMaterial("Sanguinite", 377);

            EXIMITE = registerMaterial("Eximite", 380);
            MEUTOITE = registerMaterial("Meutoite", 381);

            HEPATIZON = registerMaterial("Hepatizon", 1504);
            DAMASCUS_STEEL = registerMaterial("DamascusSteel", 1505);
            ANGMALLEN = registerMaterial("Angmallen", 1506);

            BLACK_STEEL = registerMaterial("BlackSteel", 1508);
            QUICKSILVER = registerMaterial("QuickSilver", 1509);
            HADEROTH = registerMaterial("Haderoth", 1510);
            CELENEGIL = registerMaterial("Celenegil", 1511);
            TARTARITE = registerMaterial("Tartarite", 1512);

            SHADOW_STEEL = registerMaterial("ShadowSteel", 1516);
            INOLASHITE = registerMaterial("Inolashite", 1517);
            AMORDRINE = registerMaterial("Amordrine", 1518);

            DESICHALKOS = registerMaterial("Desichalkos", 1519);


            NINJA = registerMaterial("Ninja", 1468);
            YELLOWSTONE = registerMaterial("Yellowstone", 1068);
            BLUESTONE = registerMaterial("Bluestone", 1069);


            ALUMINIUM_OD = registerMaterial("Aluminium", 4096);

            itemPlates = createItem("itemPlates");
            itemLargePlates = createItem("itemLargePlates");
            itemDusts = createItem("itemDusts");
            itemIngots = createItem("itemIngots");
            itemGems = createItem("itemGems");

            PLATE = registerShape("Plate", "plate", 0, itemPlates);
            STICK = registerShape("Stick", "stick", 1);
            SHORT_STICK = registerShape("ShortStick", "shortstick", 2);
            RING = registerShape("Ring", "ring", 3);
            SMALL_RING = registerShape("SmallRing", "smallring", 4);
            GEAR = registerShape("Gear", "gear", 5);
            BLADE = registerShape("Blade", "blade", 6);
            NEEDLE = registerShape("Needle", "needle", 7);
            DISC = registerShape("Disc", "disc", 16);
            SMALL_DISC = registerShape("SmallDisc", "smalldisc", 17);
            CYLINDER = registerShape("Cylinder", "cylinder", 18);
            PIPE = registerShape("Pipe", "pipe", 19);
            LARGE_BALL = registerShape("LargeBall", "largeball", 32);
            LARGE_PLATE = registerShape("LargePlate", "largeplate", 33, itemLargePlates);
            GRINDING_HEAD = registerShape("GrindingHead", "grindinghead", 64);
            BEARING = registerShape("Bearing", "bearing", 65);
            SPINDLE = registerShape("Spindle", "spindle", 66);
            CUTTING_HEAD = registerShape("CuttingHead", "cuttinghead", 67);
            WATER_WHEEL = registerShape("WaterWheel", "waterwheel", 68);

            BLOCK = registerShape("Block", "block", 1024);
            BALL = registerShape("Ball", "ball", 8);
            DUST = registerShape("Dust", "dust", 128, itemDusts);
            INGOT = registerShape("Ingot", "ingot", 129, itemIngots);
            GEM = registerShape("Gem", "gem", 130, itemGems);

            CRYSTAL = registerShape("Crystal", "crystal", 131);

            add(CLAY, PLATE);
            setTier(1);
            add(CLAY, STICK);
            setTier(1);
            add(CLAY, SHORT_STICK, "shortclaystick");
            setTier(1);
            add(CLAY, RING);
            setTier(1);
            add(CLAY, SMALL_RING, "smallclayring");
            setTier(1);
            add(CLAY, GEAR);
            setTier(1);
            add(CLAY, BLADE);
            setTier(1);
            add(CLAY, NEEDLE);
            setTier(1);
            add(CLAY, DISC);
            setTier(1);
            add(CLAY, SMALL_DISC, "smallclaydisc");
            setTier(1);
            add(CLAY, CYLINDER);
            setTier(1);
            add(CLAY, PIPE);
            setTier(1);
            add(CLAY, LARGE_BALL, "largeclayball");
            setTier(1);
            add(CLAY, LARGE_PLATE, "largeclayplate");
            setTier(1);
            add(CLAY, GRINDING_HEAD);
            setTier(1);
            add(CLAY, BEARING);
            setTier(1);
            add(CLAY, SPINDLE);
            setTier(1);
            add(CLAY, CUTTING_HEAD);
            setTier(1);
            add(CLAY, WATER_WHEEL);
            setTier(1);
            registerMaterialShape(CLAY, BLOCK, new ItemStack(Blocks.clay));
            registerMaterialShape(CLAY, BALL, new ItemStack(Items.clay_ball));

            add(DENSE_CLAY, PLATE);
            setTier(2);
            add(DENSE_CLAY, STICK);
            setTier(2);
            add(DENSE_CLAY, SHORT_STICK, "shortdenseclaystick");
            setTier(2);
            add(DENSE_CLAY, RING);
            setTier(2);
            add(DENSE_CLAY, SMALL_RING, "smalldenseclayring");
            setTier(2);
            add(DENSE_CLAY, GEAR);
            setTier(2);
            add(DENSE_CLAY, BLADE);
            setTier(2);
            add(DENSE_CLAY, NEEDLE);
            setTier(2);
            add(DENSE_CLAY, DISC);
            setTier(2);
            add(DENSE_CLAY, SMALL_DISC, "smalldenseclaydisc");
            setTier(2);
            add(DENSE_CLAY, CYLINDER);
            setTier(2);
            add(DENSE_CLAY, PIPE);
            setTier(2);
            add(DENSE_CLAY, LARGE_PLATE, "largedenseclayplate");
            setTier(2);
            add(DENSE_CLAY, GRINDING_HEAD);
            setTier(2);
            add(DENSE_CLAY, BEARING);
            setTier(2);
            add(DENSE_CLAY, SPINDLE);
            setTier(2);
            add(DENSE_CLAY, CUTTING_HEAD);
            setTier(2);
            add(DENSE_CLAY, WATER_WHEEL);
            setTier(2);
            registerMaterialShape(DENSE_CLAY, BLOCK, new ItemStack(CBlocks.blockCompressedClay, 1, 0));
            registerMaterialShape(DENSE_CLAY, BALL, new ItemStack(Items.clay_ball));

            add(IND_CLAY, PLATE);
            setTier(3);
            add(IND_CLAY, LARGE_PLATE);
            setTier(3);
            registerMaterialShape(IND_CLAY, BLOCK, new ItemStack(CBlocks.blockCompressedClay, 1, 2));

            add(ADVIND_CLAY, PLATE);
            setTier(4);
            add(ADVIND_CLAY, LARGE_PLATE);
            setTier(4);
            registerMaterialShape(ADVIND_CLAY, BLOCK, new ItemStack(CBlocks.blockCompressedClay, 1, 3));

            add(CLAY, DUST);
            setTier(1);
            add(DENSE_CLAY, DUST);
            setTier(2);
            addOD(IND_CLAY, DUST, false, true);
            setTier(3);
            addOD(ADVIND_CLAY, DUST, false, true);
            setTier(4);
            addOD(ENG_CLAY, DUST, false, true);
            setTier(3);

            addOD(EXC_CLAY, DUST, false, true);
            setTier(7);

            addOD(SALT, DUST, false, true);
            setTier(4);
            add(CAL_CLAY, DUST);
            setTier(4);
            add(CALCIUM_CHLORIDE, DUST);
            setTier(4);
            add(SODIUM_CARBONATE, DUST);
            setTier(4);
            addOD(QUARTZ, DUST, false, true);
            setTier(4);

            addOD(IMPURE_SILICON, DUST, true, true);
            setTier(5);
            addOD(IMPURE_SILICON, INGOT, false, true);
            setTier(5);
            addOD(IMPURE_SILICON, PLATE, false, true);
            setTier(5);
            add(IMPURE_SILICON, LARGE_PLATE);
            setTier(5);

            addOD(SILICONE, DUST, true, true);
            setTier(5);
            addOD(SILICONE, INGOT, true, true);
            setTier(5);
            addOD(SILICONE, PLATE, true, true);
            setTier(5);
            add(SILICONE, LARGE_PLATE, true);
            setTier(5);

            addOD(SILICON, DUST, true, true);
            setTier(5);
            addOD(SILICON, INGOT, true, true);
            setTier(5);
            addOD(SILICON, PLATE, true, true);
            setTier(5);
            add(SILICON, LARGE_PLATE, true);
            setTier(5);

            addOD(ALUMINIUM, DUST, true, true);
            setTier(6);
            addOD(ALUMINIUM, INGOT, true, true);
            setTier(6);
            addOD(ALUMINIUM, PLATE, true, true);
            setTier(6);
            add(ALUMINIUM, LARGE_PLATE, true);
            setTier(6);

            boolean cfg = ClayiumCore.cfgHardcoreAluminium;
            if (cfg)
                setTier(IMPURE_ALUMINIUM, DUST, 6);
            add(IMPURE_ALUMINIUM, INGOT, true, cfg);
            setTier(6);
            add(IMPURE_ALUMINIUM, PLATE, true, cfg);
            setTier(6);
            add(IMPURE_ALUMINIUM, LARGE_PLATE, true, cfg);
            setTier(6);

            MAIN_ALUMINIUM = cfg ? IMPURE_ALUMINIUM : ALUMINIUM;

            addOD(CLAY_STEEL, DUST, true, true);
            setTier(7);
            addOD(CLAY_STEEL, INGOT, true, true);
            setTier(7);
            addOD(CLAY_STEEL, PLATE, true, true);
            setTier(7);
            add(CLAY_STEEL, LARGE_PLATE, true);
            setTier(7);

            addOD(CLAYIUM, DUST, true, true);
            setTier(8);
            addOD(CLAYIUM, INGOT, true, true);
            setTier(8);
            addOD(CLAYIUM, PLATE, true, true);
            setTier(8);
            add(CLAYIUM, LARGE_PLATE, true);
            setTier(8);

            addOD(ULTIMATE_ALLOY, DUST, true, true);
            setTier(9);
            addOD(ULTIMATE_ALLOY, INGOT, true, true);
            setTier(9);
            addOD(ULTIMATE_ALLOY, PLATE, true, true);
            setTier(9);
            add(ULTIMATE_ALLOY, LARGE_PLATE, true);
            setTier(9);

            addOD(ANTIMATTER, DUST, true, true);
            setTier(10);
            addOD(ANTIMATTER, GEM, "matter", true, true);
            setTier(10);
            addOD(ANTIMATTER, PLATE, true, true);
            setTier(10);
            add(ANTIMATTER, LARGE_PLATE, true);
            setTier(10);

            addOD(PURE_ANTIMATTER, DUST, true, true);
            setTier(11);
            addOD(PURE_ANTIMATTER, GEM, "matter", true, true);
            setTier(11);
            addOD(PURE_ANTIMATTER, PLATE, true, true);
            setTier(11);
            add(PURE_ANTIMATTER, LARGE_PLATE, true);
            setTier(11);

            addOD(COMPRESSED_PURE_ANTIMATTER[1], GEM, "matter", true, true);
            setTier(11);
            addOD(COMPRESSED_PURE_ANTIMATTER[2], GEM, "matter2", true, true);
            setTier(11);
            addOD(COMPRESSED_PURE_ANTIMATTER[3], GEM, "matter2", true, true);
            setTier(11);
            addOD(COMPRESSED_PURE_ANTIMATTER[4], GEM, "matter3", true, true);
            setTier(12);
            addOD(COMPRESSED_PURE_ANTIMATTER[5], GEM, "matter3", true, true);
            setTier(12);
            addOD(COMPRESSED_PURE_ANTIMATTER[6], GEM, "matter4", true, true);
            setTier(12);
            addOD(COMPRESSED_PURE_ANTIMATTER[7], GEM, "matter4", true, true);
            setTier(12);

            add(OCTUPLE_CLAY, DUST, true);
            setTier(12);
            add(OCTUPLE_CLAY, PLATE, true);
            setTier(12);
            add(OCTUPLE_CLAY, LARGE_PLATE, true);
            setTier(12);
            registerMaterialShape(OCTUPLE_CLAY, BLOCK, new ItemStack(CBlocks.blockCompressedClay, 1, 12));

            addOD(OCTUPLE_PURE_ANTIMATTER, DUST, true, true);
            setTier(13);
            addOD(OCTUPLE_PURE_ANTIMATTER, GEM, "matter5", true, true);
            setTier(13);
            addOD(OCTUPLE_PURE_ANTIMATTER, PLATE, true, true);
            setTier(13);
            add(OCTUPLE_PURE_ANTIMATTER, LARGE_PLATE, true);
            setTier(13);

            for (CMaterial material : new CMaterial[] {IMPURE_SILICON, SILICONE, SILICON, ALUMINIUM, CLAY_STEEL, CLAYIUM, ULTIMATE_ALLOY, ANTIMATTER, PURE_ANTIMATTER, OCTUPLE_PURE_ANTIMATTER}) {


                registerMaterialShape(material, BLOCK, CBlocks.blockMaterial.get(material.name));
                addItemToOD(material, BLOCK);
            }
            String[] dyes = {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};


            for (int j = 0; j < 16; j++) {
                OreDictionary.registerOre(getODName(SILICONE, BLOCK), new ItemStack((Block) CBlocks.blockSiliconeColored, 1, j));
                OreDictionary.registerOre(getODName(SILICONE, BLOCK) + dyes[15 - j], new ItemStack((Block) CBlocks.blockSiliconeColored, 1, j));
            }


            addOD(ORG_CLAY, DUST, true, true);

            add(IMPURE_REDSTONE, DUST, true);
            add(IMPURE_GLOWSTONE, DUST, true);

            add(IMPURE_ULTIMATE_ALLOY, INGOT, true);
            setTier(8);

            CMaterial[] metals = {MAGNESIUM, SODIUM, LITHIUM, ZIRCONIUM, ZINC, MANGANESE, CALCIUM, POTASSIUM, NICKEL, BERYLLIUM, LEAD, HAFNIUM, CHROME, TITANIUM, STRONTIUM, BARIUM, COPPER, IRON, GOLD, BRONZE, BRASS, ELECTRUM, INVAR, STEEL};


            for (CMaterial metal : metals) {
                addOD(metal, DUST, true, false);
                addOD(metal, INGOT, true, false);
            }
            CMaterial[] impuremetals = {IMPURE_ALUMINIUM, IMPURE_MAGNESIUM, IMPURE_SODIUM, IMPURE_LITHIUM, IMPURE_ZIRCONIUM, IMPURE_ZINC, IMPURE_MANGANESE, IMPURE_CALCIUM, IMPURE_POTASSIUM, IMPURE_NICKEL, IMPURE_IRON, IMPURE_BERYLLIUM, IMPURE_LEAD, IMPURE_HAFNIUM, IMPURE_CHROME, IMPURE_TITANIUM, IMPURE_STRONTIUM, IMPURE_BARIUM, IMPURE_COPPER};


            for (CMaterial metal : impuremetals) {
                add(metal, DUST, true);
            }

            metals = new CMaterial[] {ZINCALMINIUM_ALLOY, ZINCONIUM_ALLOY};
            for (CMaterial metal : metals) {
                addOD(metal, DUST, true, true);
                setTier(6);
                addOD(metal, INGOT, true, true);
                setTier(6);
            }
            metals = new CMaterial[] {AZ91D_ALLOY, ZK60A_ALLOY};
            for (CMaterial metal : metals) {
                addOD(metal, DUST, true, true);
                setTier(6);
                addOD(metal, INGOT, true, true);
                setTier(6);
                addOD(metal, PLATE, true, true);
                setTier(6);
                add(metal, LARGE_PLATE, true);
                setTier(6);
            }

            addOD(CARBON, DUST, true, false);
            addOD(PHOSPHORUS, DUST, true, false);
            addOD(SULFUR, DUST, true, false);

            addOD(LAPIS, DUST, true, false);
            addOD(COAL, DUST, true, false);
            addOD(CHARCOAL, DUST, true, false);

            addOD(SALTPETER, DUST, true, false);

            metals = new CMaterial[] {RUBIDIUM, CAESIUM, FRANCIUM, RADIUM, ACTINIUM, THORIUM, PROTACTINIUM, URANIUM, NEPTUNIUM, PLUTONIUM, AMERICIUM, CURIUM, LANTHANUM, CERIUM, PRASEODYMIUM, NEODYMIUM, PROMETHIUM, SAMARIUM, EUROPIUM, VANADIUM, COBALT, PALLADIUM, SILVER, PLATINUM, IRIDIUM, OSMIUM, RHENIUM, TANTALUM, TUNGSTEN, MOLYBDENUM, TIN, ANTIMONY, BISMUTH};


            for (CMaterial metal : metals) {
                addOD(metal, INGOT, true, false);
            }

            cfg = ClayiumCore.cfgHardcoreOsmium;
            addOD(IMPURE_OSMIUM, INGOT, true, true, cfg ? 0 : -1);
            MAIN_OSMIUM = cfg ? IMPURE_OSMIUM : OSMIUM;

            OreDictionary.registerOre("itemSilicon", get(SILICON, PLATE));
            OreDictionary.registerOre("itemSalt", get(SALT, DUST));
            OreDictionary.registerOre("condimentSalt", get(SALT, DUST));

            OreDictionary.registerOre("ingotAluminium", get(ALUMINIUM, INGOT));
            OreDictionary.registerOre("dustAluminium", get(ALUMINIUM, DUST));
            OreDictionary.registerOre("plateAluminium", get(ALUMINIUM, PLATE));
            OreDictionary.registerOre("blockAluminium", get(ALUMINIUM, BLOCK));

            MetalChest.registerMaterials();
            MetalBlock.registerMaterials();
        } else {

            itemIngots = createItem("itemIngots");
            CLAY_STEEL = registerMaterial("ClaySteel", "claysteel", 256);
            (CLAY_STEEL.setColor(136, 144, 173).setColor(255, 255, 255, 2)).hardness = 3.0F;
            INGOT = registerShape("Ingot", "ingot", 129, itemIngots);
            addOD(CLAY_STEEL, INGOT, true, true);
        }
    }

    public static CMaterial PIG_IRON;
    public static CMaterial ALUMITE;
    public static CMaterial MANYULLYN;
    public static CMaterial FAIRY;
    public static CMaterial POKEFENNIUM;
    public static CMaterial RED_AURUM;
    public static CMaterial DRULLOY;
    public static CMaterial RED_ALLOY;
    public static CMaterial ELECTROTINE;
    public static CMaterial ELECTROTINE_ALLOY;
    public static CMaterial REFINED_GLOWSTONE;
    public static CMaterial REFINED_OBSIDIAN;
    public static CMaterial UNSTABLE;
    public static CMaterial HSLA;
    public static CMaterial GRAPHITE;
    public static CMaterial YELLORIUM;
    public static CMaterial CYANITE;
    public static CMaterial BLUTONIUM;
    public static CMaterial LUDICRITE;
    public static CMaterial METEORIC_IRON;
    public static CMaterial DESH;
    public static CMaterial IRON_COMPRESSED;
    public static CMaterial FZ_DARK_IRON;
    public static CMaterial THAUMIUM;
    public static CMaterial VOID;
    public static CMaterial MANASTEEL;
    public static CMaterial TERRASTEEL;
    public static CMaterial ELVEN_ELEMENTIUM;
    public static CMaterial TOPAZ;
    public static CMaterial MALACHITE;
    public static CMaterial TANZANITE;
    public static CMaterial HEE_ENDIUM;
    public static CMaterial DILITHIUM;
    public static CMaterial FORCICIUM;
    public static CMaterial GALLIUM;
    public static CMaterial YTTRIUM;
    public static CMaterial NIOBIUM;
    public static CMaterial URANIUM_235;
    public static CMaterial PLUTONIUM_241;
    public static CMaterial NAQUADAH;
    public static CMaterial NAQUADAH_ENRICHED;
    public static CMaterial NAQUADRIA;
    public static CMaterial NEUTRONIUM;
    public static CMaterial NIKOLITE;
    public static CMaterial QUARTZITE;
    public static CMaterial MONAZITE;
    public static CMaterial NITER;
    public static CMaterial TUNGSTEN_STEEL;
    public static CMaterial CUPRONICKEL;
    public static CMaterial NICHROME;
    public static CMaterial KANTHAL;
    public static CMaterial STAINLESS_STEEL;
    public static CMaterial COBALT_BRASS;
    public static CMaterial MAGNALIUM;
    public static CMaterial SOLDERING_ALLOY;
    public static CMaterial BATTERY_ALLOY;
    public static CMaterial VANADIUM_GALLIUM;
    public static CMaterial YTTRIUM_BARIUM_CUPRATE;
    public static CMaterial NIOBIUM_TITANIUM;
    public static CMaterial ULTIMET;
    public static CMaterial TIN_ALLOY;
    public static CMaterial BLUE_ALLOY;
    public static CMaterial WROUGHT_IRON;
    public static CMaterial ANNEALED_COPPER;
    public static CMaterial IRON_MAGNETIC;
    public static CMaterial STEEL_MAGNETIC;
    public static CMaterial NEODYMIUM_MAGNETIC;
    public static CMaterial LIGNITE;
    public static CMaterial LAZURITE;
    public static CMaterial SODALITE;
    public static CMaterial GREEN_SAPPHIRE;
    public static CMaterial GARNET_RED;
    public static CMaterial GARNET_YELLOW;
    public static CMaterial OPAL;
    public static CMaterial JASPER;
    public static CMaterial BLUE_TOPAZ;
    public static CMaterial FORCE;
    public static CMaterial FORCILLIUM;
    public static CMaterial GLASS;
    public static CMaterial PROMETHEUM;
    public static CMaterial DEEP_IRON;
    public static CMaterial INFUSCOLIUM;
    public static CMaterial OURECLASE;
    public static CMaterial AREDRITE;
    public static CMaterial ASTRAL_SILVER;
    public static CMaterial CARMOT;
    public static CMaterial MITHRIL;
    public static CMaterial RUBRACIUM;
    public static CMaterial ORICHALCUM;
    public static CMaterial ADAMANTINE;
    public static CMaterial ATLARUS;
    public static CMaterial IGNATIUS;
    public static CMaterial SHADOW_IRON;
    public static CMaterial LEMURITE;
    public static CMaterial MIDASIUM;
    public static CMaterial VYROXERES;
    public static CMaterial CERUCLASE;
    public static CMaterial ALDUORITE;
    public static CMaterial KALENDRITE;
    public static CMaterial VULCANITE;
    public static CMaterial SANGUINITE;
    public static CMaterial EXIMITE;
    public static CMaterial MEUTOITE;
    public static CMaterial HEPATIZON;
    public static CMaterial DAMASCUS_STEEL;
    public static CMaterial ANGMALLEN;
    public static CMaterial BLACK_STEEL;
    public static CMaterial QUICKSILVER;
    public static CMaterial HADEROTH;
    public static CMaterial CELENEGIL;
    public static CMaterial TARTARITE;
    public static CMaterial SHADOW_STEEL;
    public static CMaterial INOLASHITE;
    public static CMaterial AMORDRINE;
    public static CMaterial DESICHALKOS;
    public static CMaterial NINJA;
    public static CMaterial YELLOWSTONE;
    public static CMaterial BLUESTONE;
    public static CMaterial ALUMINIUM_OD;
    public static ItemDamaged itemPlates;
    public static ItemDamaged itemLargePlates;
    public static ItemDamaged itemDusts;
    public static ItemDamaged itemIngots;
    public static ItemDamaged itemGems;
    public static CShape PLATE;
    public static CShape STICK;
    public static CShape SHORT_STICK;
    public static CShape RING;
    public static CShape SMALL_RING;
    public static CShape GEAR;
    public static CShape BLADE;
    public static CShape NEEDLE;
    public static CShape DISC;
    public static CShape SMALL_DISC;
    public static CShape CYLINDER;
    public static CShape PIPE;
    public static CShape LARGE_BALL;
    public static CShape LARGE_PLATE;
    public static CShape GRINDING_HEAD;
    public static CShape BEARING;
    public static CShape SPINDLE;
    public static CShape CUTTING_HEAD;
    public static CShape WATER_WHEEL;
    public static CShape BLOCK;
    public static CShape BALL;
    public static CShape DUST;
    public static CShape INGOT;
    public static CShape GEM;
    public static CShape CRYSTAL;
    public static CMaterial currentMaterial;
    public static CShape currentShape;

    public static CMaterial registerMaterial(String materialName, String materialIconName, String materialOreDictionaryName, int materialMeta, ItemDamaged itemMap) {
        CMaterial material = new CMaterial(materialName, materialIconName, materialOreDictionaryName, materialMeta);
        materialMap.put(material, itemMap);
        materialList.put(Integer.valueOf(materialMeta), material);
        return material;
    }

    public static CMaterial registerMaterial(String materialName, String materialIconName, int materialMeta, ItemDamaged itemMap) {
        CMaterial material = new CMaterial(materialName, materialIconName, materialMeta);
        materialMap.put(material, itemMap);
        materialList.put(Integer.valueOf(materialMeta), material);
        return material;
    }

    public static CShape registerShape(String shapeName, String shapeIconName, String shapeOreDictionaryName, int shapeMeta, ItemDamaged itemMap) {
        CShape shape = new CShape(shapeName, shapeIconName, shapeOreDictionaryName, shapeMeta);
        shapeMap.put(shape, itemMap);
        return shape;
    }

    public static CShape registerShape(String shapeName, String shapeIconName, int shapeMeta, ItemDamaged itemMap) {
        CShape shape = new CShape(shapeName, shapeIconName, shapeMeta);
        shapeMap.put(shape, itemMap);
        return shape;
    }


    public static void registerMaterialShape(CMaterial material, CShape shape, ItemStack itemStack) {
        if (!materialShapeMap.containsKey(material)) {
            materialShapeMap.put(material, new HashMap<CShape, ItemStack>());
        }
        ((Map<CShape, ItemStack>) materialShapeMap.get(material)).put(shape, itemStack);
    }

    public static CMaterial registerMaterial(String materialName, String materialIconName, String materialOreDictionaryName, int materialMeta) {
        CMaterial material = new CMaterial(materialName, materialIconName, materialOreDictionaryName, materialMeta);
        materialList.put(Integer.valueOf(materialMeta), material);
        return material;
    }

    public static CMaterial registerMaterial(String materialName, int materialMeta) {
        return registerMaterial(materialName, materialName.toLowerCase(), materialMeta);
    }

    public static CMaterial registerMaterial(String materialName, String materialIconName, int materialMeta) {
        CMaterial material = new CMaterial(materialName, materialIconName, materialMeta);
        materialList.put(Integer.valueOf(materialMeta), material);
        return material;
    }

    public static CShape registerShape(String shapeName, String shapeIconName, String shapeOreDictionaryName, int shapeMeta) {
        CShape shape = new CShape(shapeName, shapeIconName, shapeOreDictionaryName, shapeMeta);
        return shape;
    }

    public static CShape registerShape(String shapeName, String shapeIconName, int shapeMeta) {
        CShape shape = new CShape(shapeName, shapeIconName, shapeMeta);
        return shape;
    }


    public static boolean add(CMaterial material, CShape shape) {
        return add(material, shape, material.iname + shape.iname);
    }

    public static boolean add(CMaterial material, CShape shape, String iconstr) {
        return add(material, shape, iconstr, true);
    }


    public static boolean add(CMaterial material, CShape shape, String iconstr, boolean display) {
        currentMaterial = material;
        currentShape = shape;
        if (materialShapeMap.containsKey(material) && ((Map) materialShapeMap.get(material)).containsKey(shape)) {
            ClayiumCore.logger.error("The CMaterials item already exits  [" + material.name + "] [" + shape.name + "]");
            return false;
        }
        if (materialMap.containsKey(material)) {
            ((ItemDamaged) materialMap.get(material)).addItemList(shape.name, shape.meta, iconstr, display);
            return true;
        }
        if (shapeMap.containsKey(shape)) {
            ((ItemDamaged) shapeMap.get(shape)).addItemList(material.name, material.meta, iconstr, display);
            return true;
        }
        ClayiumCore.logger.error("Can't add the CMaterials item  [" + material.name + "] [" + shape.name + "]");
        return false;
    }


    public static boolean addOD(CMaterial material, CShape shape, String iconstr, boolean registerOreDictionary) {
        boolean res = false;
        if (registerOreDictionary) {
            res = add(material, shape, iconstr);
            addItemToOD(material, shape);
        } else {
            List<ItemStack> list = OreDictionary.getOres(getODName(material, shape));
            if (list != null && list.size() > 0) {
                registerMaterialShape(material, shape, list.get(0));
            } else {
                return addOD(material, shape, iconstr, true);
            }
        }
        return res;
    }


    public static boolean add(CMaterial material, CShape shape, IMultipleRenderIcons icon) {
        return add(material, shape, icon, true);
    }


    public static boolean add(CMaterial material, CShape shape, IMultipleRenderIcons icon, boolean display) {
        currentMaterial = material;
        currentShape = shape;
        if (materialShapeMap.containsKey(material) && ((Map) materialShapeMap.get(material)).containsKey(shape)) {
            ClayiumCore.logger.error("The CMaterials item already exits  [" + material.name + "] [" + shape.name + "]");
            return false;
        }
        if (materialMap.containsKey(material)) {
            ((ItemDamaged) materialMap.get(material)).addItemList(shape.name, shape.meta, icon, display);
            return true;
        }
        if (shapeMap.containsKey(shape)) {
            ((ItemDamaged) shapeMap.get(shape)).addItemList(material.name, material.meta, icon, display);
            return true;
        }
        ClayiumCore.logger.error("Can't add the CMaterials item  [" + material.name + "] [" + shape.name + "]");
        return false;
    }

    public static boolean add(CMaterial material, CShape shape, boolean materialicon) {
        return add(material, shape, materialicon, true);
    }

    public static boolean add(CMaterial material, CShape shape, boolean materialicon, boolean display) {
        return add(material, shape, shape.iname, materialicon, display);
    }

    public static boolean add(CMaterial material, CShape shape, String shapeiconstr, boolean materialicon, boolean display) {
        return materialicon ? add(material, shape, (IMultipleRenderIcons) new GenericMaterialIcon(shapeiconstr, material.colors[0][0], material.colors[0][1], material.colors[0][2], material.colors[1][0], material.colors[1][1], material.colors[1][2], material.colors[2][0], material.colors[2][1], material.colors[2][2]), display) :


                add(material, shape, material.iname + shapeiconstr, display);
    }


    public static boolean addOD(CMaterial material, CShape shape, boolean materialicon, boolean registerOreDictionary) {
        return addOD(material, shape, materialicon, registerOreDictionary, 0);
    }


    public static boolean addOD(CMaterial material, CShape shape, boolean materialicon, boolean registerOreDictionary, int displayFlag) {
        boolean res = false;
        if (registerOreDictionary) {
            res = add(material, shape, materialicon, (displayFlag >= 0));
            addItemToOD(material, shape);
        } else {
            List<ItemStack> list = OreDictionary.getOres(getODName(material, shape));
            if (list != null && list.size() > 0) {
                res = add(material, shape, materialicon, (displayFlag > 0));
                addItemToOD(material, shape);
                registerMaterialShape(material, shape, list.get(0));
            } else {
                return addOD(material, shape, materialicon, true, displayFlag);
            }
        }
        return res;
    }


    public static boolean addOD(CMaterial material, CShape shape, String shapeiconstr, boolean materialicon, boolean registerOreDictionary) {
        return addOD(material, shape, shapeiconstr, materialicon, registerOreDictionary, 0);
    }


    public static boolean addOD(CMaterial material, CShape shape, String shapeiconstr, boolean materialicon, boolean registerOreDictionary, int displayFlag) {
        boolean res = false;
        if (registerOreDictionary) {
            res = add(material, shape, shapeiconstr, materialicon, (displayFlag >= 0));
            addItemToOD(material, shape);
        } else {
            List<ItemStack> list = OreDictionary.getOres(getODName(material, shape));
            if (list != null && list.size() > 0) {
                res = add(material, shape, shapeiconstr, materialicon, (displayFlag > 0));
                addItemToOD(material, shape);
                registerMaterialShape(material, shape, list.get(0));
            } else {
                return addOD(material, shape, shapeiconstr, materialicon, true, displayFlag);
            }
        }
        return res;
    }


    public static void addItemToOD(CMaterial material, CShape shape) {
        OreDictionary.registerOre(getODName(material, shape), get(material, shape));
    }

    public static boolean setTier(CMaterial material, CShape shape, int tier) {
        if (materialShapeMap.containsKey(material) && ((Map) materialShapeMap.get(material)).containsKey(shape)) {
            ClayiumCore.logger.info("Can(t apply the tier to " + material.name + " " + shape.name);
            return false;
        }
        if (materialMap.containsKey(material)) {
            ((ItemDamaged) materialMap.get(material)).setTier(shape.name, tier);
            return true;
        }
        if (shapeMap.containsKey(shape)) {
            ((ItemDamaged) shapeMap.get(shape)).setTier(material.name, tier);
            return true;
        }
        ClayiumCore.logger.error("Can't get the CMaterials item  [" + material.name + "] [" + shape.name + "]");
        return false;
    }

    public static boolean setTier(int tier) {
        return setTier(currentMaterial, currentShape, tier);
    }


    public static ItemStack get(CMaterial material, CShape shape, int stackSize) {
        if (materialShapeMap.containsKey(material) && ((Map) materialShapeMap.get(material)).containsKey(shape)) {
            ItemStack itemStack = ((ItemStack) ((Map) materialShapeMap.get(material)).get(shape)).copy();
            itemStack.stackSize = stackSize;

            return itemStack;
        }
        if (materialMap.containsKey(material) && ((ItemDamaged) materialMap
                .get(material)).containsKey(shape.name)) {
            return ((ItemDamaged) materialMap.get(material)).getItemStack(shape.name, stackSize);
        }
        if (shapeMap.containsKey(shape) && ((ItemDamaged) shapeMap
                .get(shape)).containsKey(material.name)) {
            return ((ItemDamaged) shapeMap.get(shape)).getItemStack(material.name, stackSize);
        }
        ClayiumCore.logger.error("Can't get the CMaterials item  [" + material.name + "] [" + shape.name + "]");
        return null;
    }

    public static ItemStack get(CMaterial material, CShape shape) {
        return get(material, shape, 1);
    }

    public static boolean exist(CMaterial material, CShape shape) {
        if (materialShapeMap.containsKey(material) && ((Map) materialShapeMap.get(material)).containsKey(shape)) {
            return true;
        }
        if (materialMap.containsKey(material) && ((ItemDamaged) materialMap
                .get(material)).containsKey(shape.name)) {
            return true;
        }
        if (shapeMap.containsKey(shape) && ((ItemDamaged) shapeMap
                .get(shape)).containsKey(material.name)) {
            return true;
        }
        return false;
    }

    public static CMaterial getMaterialFromId(int id) {
        return materialList.get(Integer.valueOf(id));
    }

    public static OreDictionaryStack getOD(CMaterial material, CShape shape, int stackSize) {
        return new OreDictionaryStack(getODName(material, shape), stackSize);
    }

    public static OreDictionaryStack getOD(CMaterial material, CShape shape) {
        return getOD(material, shape, 1);
    }

    public static String getODName(CMaterial material, CShape shape) {
        return shape.oreDictionaryName + material.oreDictionaryName;
    }


    public static ItemStack getODExist(String oreName, int stackSize) {
        List<ItemStack> oreList = OreDictionary.getOres(oreName);
        if (oreList != null && oreList.size() > 0) {
            ItemStack res = ((ItemStack) oreList.get(0)).copy();
            res.stackSize = stackSize;
            return res;
        }
        return null;
    }

    public static ItemStack getODExist(String oreName) {
        return getODExist(oreName, 1);
    }

    public static ItemStack getODExist(CMaterial material, CShape shape, int stackSize) {
        return getODExist(getODName(material, shape), stackSize);
    }

    public static ItemStack getODExist(CMaterial material, CShape shape) {
        return getODExist(material, shape, 1);
    }

    public static boolean existOD(String oreName) {
        return (getODExist(oreName) != null);
    }

    public static boolean existOD(CMaterial material, CShape shape) {
        return (getODExist(material, shape) != null);
    }

    public static ItemDamaged createItem(String unlocalizedName, CreativeTabs creativeTab, String itemId) {
        ItemDamaged itemDamaged = new ItemDamaged();
        itemDamaged.setCreativeTab(creativeTab);
        itemDamaged.setUnlocalizedName(unlocalizedName);
        itemDamaged.setMaxStackSize(64);
        GameRegistry.registerItem(itemDamaged, itemId);
        return itemDamaged;
    }

    public static ItemDamaged createItem(String unlocalizedName, CreativeTabs creativeTab) {
        return createItem(unlocalizedName, creativeTab, unlocalizedName);
    }

    public static ItemDamaged createItem(String unlocalizedName) {
        return createItem(unlocalizedName, ClayiumCore.creativeTabClayium);
    }
}
