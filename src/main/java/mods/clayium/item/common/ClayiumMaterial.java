package mods.clayium.item.common;

import mods.clayium.core.ClayiumConfiguration;
import mods.clayium.item.ClayiumItems;

import java.util.ArrayList;
import java.util.List;

public class ClayiumMaterial {
    public static final ClayiumMaterial clay = new ClayiumMaterial("clay", "Clay", 512);
    public static final ClayiumMaterial denseClay = new ClayiumMaterial("dense_clay", "DenseClay", 513);
    public static final ClayiumMaterial indClay = new ClayiumMaterial("ind_clay", "IndustrialClay", 515);
    public static final ClayiumMaterial advClay = new ClayiumMaterial("adv_ind_clay", "AdvancedIndustrialClay", 516);
    public static final ClayiumMaterial engClay = new ClayiumMaterial("eng_clay", "EnergizedClay", 768);
    public static final ClayiumMaterial calClay = new ClayiumMaterial("cal_clay", "CalcareousClay", 769);
    public static final ClayiumMaterial excClay = new ClayiumMaterial("exc_clay", "ExcitedClay", 770);
    public static final ClayiumMaterial orgClay = new ClayiumMaterial("org_clay", "OrganicClay", 771).setColor(136, 144, 173).setColor(106, 44, 43, 1).setColor(146, 164, 183, 2);

    // Period II
    public static final ClayiumMaterial lithium = new ClayiumMaterial("lithium", "Lithium", 3).setColor(210, 210, 150).setColor(120, 120, 120, 1);
    public static final ClayiumMaterial beryllium = new ClayiumMaterial("beryllium", "Beryllium", 4).setColor(210, 240, 210);
    public static final ClayiumMaterial carbon = new ClayiumMaterial("carbon", "Carbon", 6).setColor(10, 10, 10).setColor(30, 30, 30, 2);
    // Period III
    public static final ClayiumMaterial sodium = new ClayiumMaterial("sodium", "Sodium", 11).setColor(170, 170, 222).setColor(120, 120, 120, 1);
    public static final ClayiumMaterial magnesium = new ClayiumMaterial("magnesium", "Magnesium", 12).setColor(150, 210, 150).setColor(120, 120, 120, 1);
    public static final ClayiumMaterial aluminum = new ClayiumMaterial("aluminum", "Aluminum", 13).setColor(190, 200, 202).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial silicon = new ClayiumMaterial("silicon", "Silicon", 14).setColor(40, 28, 40).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial phosphorus = new ClayiumMaterial("phosphorus", "Phosphorus", 15).setColor(155, 155, 0, 205, 205, 50);
    public static final ClayiumMaterial sulfur = new ClayiumMaterial("sulfur", "Sulfur", 16).setColor(205, 205, 0, 255, 255, 0);
    // Period IV
    public static final ClayiumMaterial potassium = new ClayiumMaterial("potassium", "Potassium", 19).setColor(240, 240, 190);
    public static final ClayiumMaterial calcium = new ClayiumMaterial("calcium", "Calcium", 20).setColor(240, 240, 240);
    public static final ClayiumMaterial titanium = new ClayiumMaterial("titanium", "Titanium", 22).setColor(210, 240, 240);
    public static final ClayiumMaterial vanadium = new ClayiumMaterial("vanadium", "Vanadium", 23).setColor(60, 120, 120);
    public static final ClayiumMaterial chrome = new ClayiumMaterial("chrome", "Chrome", 24).setColor(240, 210, 210);
    public static final ClayiumMaterial manganese = new ClayiumMaterial("manganese", "Manganese", 25).setColor(190, 240, 240);
    public static final ClayiumMaterial iron = new ClayiumMaterial("iron", "Iron", 26).setColor(216, 216, 216, 0).setColor(53, 53, 53, 1).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial cobalt = new ClayiumMaterial("cobalt", "Cobalt", 27).setColor(30, 30, 230);
    public static final ClayiumMaterial nickel = new ClayiumMaterial("nickel", "Nickel", 28).setColor(210, 210, 240, 0);
    public static final ClayiumMaterial copper = new ClayiumMaterial("copper", "Copper", 29).setColor(160, 90, 10).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial zinc = new ClayiumMaterial("zinc", "Zinc", 30).setColor(230, 170, 170).setColor(120, 120, 120, 1);
    public static final ClayiumMaterial gallium = new ClayiumMaterial("gallium", "Gallium", 31);
    // Period V
    public static final ClayiumMaterial rubidium = new ClayiumMaterial("rubidium", "Rubidium", 37).setColor(245, 245, 245).setColor(235, 0, 0, 1);
    public static final ClayiumMaterial strontium = new ClayiumMaterial("strontium", "Strontium", 38).setColor(210, 170, 242);
    public static final ClayiumMaterial yttrium = new ClayiumMaterial("yttrium", "Yttrium", 39);
    public static final ClayiumMaterial zirconium = new ClayiumMaterial("zirconium", "Zirconium", 40).setColor(190, 170, 122).setColor(120, 120, 120, 1);
    public static final ClayiumMaterial niobium = new ClayiumMaterial("niobium", "Niobium", 41);
    public static final ClayiumMaterial molybdenum = new ClayiumMaterial("molybdenum", "Molybdenum", 42).setColor(130, 160, 130);
    public static final ClayiumMaterial palladium = new ClayiumMaterial("palladium", "Palladium", 46).setColor(151, 70, 70);
    public static final ClayiumMaterial silver = new ClayiumMaterial("silver", "Silver", 47).setColor(230, 230, 245).setColor(120, 120, 140, 1).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial tin = new ClayiumMaterial("tin", "Tin", 50).setColor(230, 230, 240).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial antimony = new ClayiumMaterial("antimony", "Antimony", 51).setColor(70, 70, 70);
    // Period VI
    public static final ClayiumMaterial caesium = new ClayiumMaterial("caesium", "Caesium", 55).setColor(245, 245, 245).setColor(150, 150, 0, 1);
    public static final ClayiumMaterial barium = new ClayiumMaterial("barium", "Barium", 56).setColor(150, 80, 120).setColor(120, 20, 80, 1);
    public static final ClayiumMaterial lanthanum = new ClayiumMaterial("lanthanum", "Lanthanum", 57).setColor(145, 145, 145).setColor(235, 0, 0, 1);
    public static final ClayiumMaterial cerium = new ClayiumMaterial("cerium", "Cerium", 58).setColor(145, 145, 145).setColor(150, 150, 0, 1);
    public static final ClayiumMaterial praseodymium = new ClayiumMaterial("praseodymium", "Praseodymium", 59).setColor(145, 145, 145).setColor(0, 235, 0, 1);
    public static final ClayiumMaterial neodymium = new ClayiumMaterial("neodymium", "Neodymium", 60).setColor(145, 145, 145).setColor(0, 150, 150, 1);
    public static final ClayiumMaterial promethium = new ClayiumMaterial("promethium", "Promethium", 61).setColor(145, 145, 145).setColor(0, 0, 235, 1);
    public static final ClayiumMaterial samarium = new ClayiumMaterial("samarium", "Samarium", 62).setColor(145, 145, 145).setColor(150, 0, 150, 1);
    public static final ClayiumMaterial europium = new ClayiumMaterial("europium", "Europium", 63).setColor(145, 145, 145).setColor(55, 55, 55, 1).setColor(145, 145, 145, 2);
    public static final ClayiumMaterial hafnium = new ClayiumMaterial("hafnium", "Hafnium", 72).setColor(240, 210, 170);
    public static final ClayiumMaterial tantalum = new ClayiumMaterial("tantalum", "Tantalum", 73).setColor(240, 210, 170).setColor(240, 210, 150, 2);
    public static final ClayiumMaterial tungsten = new ClayiumMaterial("tungsten", "Tungsten", 74).setColor(30, 30, 30);
    public static final ClayiumMaterial rhenium = new ClayiumMaterial("rhenium", "Rhenium", 75).setColor(70, 70, 150).setColor(50, 50, 90, 2);
    public static final ClayiumMaterial osmium = new ClayiumMaterial("osmium", "Osmium", 76).setColor(70, 70, 150);
    public static final ClayiumMaterial iridium = new ClayiumMaterial("iridium", "Iridium", 77).setColor(240, 240, 240).setColor(210, 210, 210, 1).setColor(235, 235, 235, 2);
    public static final ClayiumMaterial platinum = new ClayiumMaterial("platinum", "Platinum", 78).setColor(245, 245, 230).setColor(140, 140, 120, 1).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial gold = new ClayiumMaterial("gold", "Gold", 79).setColor(255, 255, 10, 0).setColor(60, 60, 0, 1).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial lead = new ClayiumMaterial("lead", "Lead", 82).setColor(190, 240, 210);
    public static final ClayiumMaterial bismuth = new ClayiumMaterial("bismuth", "Bismuth", 83).setColor(70, 120, 70);
    // Period VII
    public static final ClayiumMaterial francium = new ClayiumMaterial("francium", "Francium", 87).setColor(245, 245, 245).setColor(0, 235, 0, 1);
    public static final ClayiumMaterial radium = new ClayiumMaterial("radium", "Radium", 88).setColor(245, 245, 245).setColor(0, 150, 150, 1);
    public static final ClayiumMaterial actinium = new ClayiumMaterial("actinium", "Actinium", 89).setColor(245, 245, 245).setColor(0, 0, 235, 1);
    public static final ClayiumMaterial thorium = new ClayiumMaterial("thorium", "Thorium", 90).setColor(50, 50, 50).setColor(200, 50, 50, 2);
    public static final ClayiumMaterial protactinium = new ClayiumMaterial("protactinium", "Protactinium", 91).setColor(50, 50, 50).setColor(50, 50, 100, 2);
    public static final ClayiumMaterial uranium = new ClayiumMaterial("uranium", "Uranium", 92).setColor(50, 255, 50).setColor(50, 155, 50, 1).setColor(50, 255, 50, 2);
    public static final ClayiumMaterial neptunium = new ClayiumMaterial("neptunium", "Neptunium", 93).setColor(50, 50, 255).setColor(50, 50, 155, 1).setColor(50, 50, 255, 2);
    public static final ClayiumMaterial plutonium = new ClayiumMaterial("plutonium", "Plutonium", 94).setColor(255, 50, 50).setColor(155, 50, 50, 1).setColor(255, 50, 50, 2);
    public static final ClayiumMaterial americium = new ClayiumMaterial("americium", "Americium", 95).setColor(235, 235, 235).setColor(155, 155, 155, 1).setColor(235, 235, 235, 2);
    public static final ClayiumMaterial curium = new ClayiumMaterial("curium", "Curium", 96).setColor(255, 255, 255).setColor(155, 155, 155, 1).setColor(244, 244, 244, 2);

    // impure metals
    public static final ClayiumMaterial impureLithium = new ClayiumMaterial("impure_lithium", "ImpureLithium", 131).setColor(220, 220, 150).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureBeryllium = new ClayiumMaterial("impure_beryllium", "ImpureBeryllium", 132).setColor(210, 240, 210).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureSodium = new ClayiumMaterial("impure_sodium", "ImpureSodium", 139).setColor(170, 170, 230).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureMagnesium = new ClayiumMaterial("impure_magnesium", "ImpureMagnesium", 140).setColor(150, 220, 150).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureAluminum = new ClayiumMaterial("aluminum", "ImpureAluminum", 141).setColor(190, 200, 202).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureSilicon = new ClayiumMaterial("impure_silicon", "ImpureSilicon", 142).setColor(151, 143, 152, 0).setColor(83, 55, 100, 1).setColor(169, 165, 165, 2);
    public static final ClayiumMaterial impurePotassium = new ClayiumMaterial("impure_potassium", "ImpurePotassium", 147).setColor(240, 240, 190).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureCalcium = new ClayiumMaterial("impure_calcium", "ImpureCalcium", 148).setColor(240, 240, 240).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureTitanium = new ClayiumMaterial("impure_titanium", "ImpureTitanium", 150).setColor(210, 240, 240).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureChrome = new ClayiumMaterial("impure_chrome", "ImpureChrome", 152).setColor(240, 210, 210).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureManganese = new ClayiumMaterial("impure_manganese", "ImpureManganese", 153).setColor(190, 240, 240).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureIron = new ClayiumMaterial("impure_iron", "ImpureIron", 154).setColor(216, 216, 216, 0).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureNickel = new ClayiumMaterial("impure_nickel", "ImpureNickel", 156).setColor(210, 210, 240, 0).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureCopper = new ClayiumMaterial("impure_copper", "ImpureCopper", 157).setColor(160, 90, 10).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureZinc = new ClayiumMaterial("impure_zinc", "ImpureZinc", 158).setColor(230, 170, 170).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureStrontium = new ClayiumMaterial("impure_strontium", "ImpureStrontium", 166).setColor(210, 170, 242).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureZirconium = new ClayiumMaterial("impure_zirconium", "ImpureZirconium", 168).setColor(190, 170, 122).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureBarium = new ClayiumMaterial("impure_barium", "ImpureBarium", 184).setColor(150, 80, 120).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureHafnium = new ClayiumMaterial("impure_hafnium", "ImpureHafnium", 200).setColor(240, 210, 170).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureOsmium = new ClayiumMaterial("impure_osmium", "ImpureOsmium", 204).setColor(70, 70, 150).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);
    public static final ClayiumMaterial impureLead = new ClayiumMaterial("impure_lead", "ImpureLead", 210).setColor(190, 240, 210).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2);

    public static final ClayiumMaterial uranium235 = new ClayiumMaterial("uranium_235", "Uranium235", 288);
    public static final ClayiumMaterial plutonium241 = new ClayiumMaterial("plutonium_241", "Plutonium241", 289);
    public static final ClayiumMaterial naquadah = new ClayiumMaterial("naquadah", "Naquadah", 296);
    public static final ClayiumMaterial naquadahEnriched = new ClayiumMaterial("naquadah_enriched", "NaquadahEnriched", 297);
    public static final ClayiumMaterial naquadria = new ClayiumMaterial("naquadria", "Naquadria", 298);
    public static final ClayiumMaterial neutronium = new ClayiumMaterial("neutronium", "Neutronium", 299);

    public static final ClayiumMaterial impureRedstone = new ClayiumMaterial("impure_redstone", "ImpureRedstone", 897).setColor(151, 70, 70);
    public static final ClayiumMaterial impureGlowstone = new ClayiumMaterial("impure_glowstone", "ImpureGlowstone", 898).setColor(151, 151, 70);

    public static final ClayiumMaterial salt = new ClayiumMaterial("salt", "Salt", 1024);
    public static final ClayiumMaterial calciumChloride = new ClayiumMaterial("calcium_chloride", "CalciumChloride", 1025);
    public static final ClayiumMaterial sodiumCarbonate = new ClayiumMaterial("sodium_carbonate", "SodiumCarbonate", 1026);
    public static final ClayiumMaterial quartz = new ClayiumMaterial("quartz", "Quartz", 1027);
    public static final ClayiumMaterial silicone = new ClayiumMaterial("silicone", "Silicone", 1028, 0.2F).setColor(180, 180, 180, 240, 240, 240);

    public static final ClayiumMaterial claySteel = new ClayiumMaterial("clay_steel", "ClaySteel", 256, 3.0F).setColor(136, 144, 173).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial clayium = new ClayiumMaterial("clay_steel", "ClaySteel", 257, 6.0F).setColor(90, 240, 210).setColor(63, 72, 85, 1).setColor(255, 205, 200, 2);
    public static final ClayiumMaterial impureUltimateAlloy = new ClayiumMaterial("impure_ultimate_alloy", "ImpureUltimateAlloy", 386, 9.0F).setColor(85, 205, 85).setColor(245, 160, 255, 2).setColor(245, 255, 255, 1);
    public static final ClayiumMaterial ultimateAlloy = new ClayiumMaterial("ultimate_alloy", "UltimateAlloy", 258, 9.0F).setColor(85, 205, 85).setColor(245, 160, 255, 2);
    public static final ClayiumMaterial antimatter = new ClayiumMaterial("antimatter", "Antimatter", 800).setColor(0, 0, 235).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial pureAntimatter = new ClayiumMaterial("pure_antimatter", "PureAntimatter", 801).setColor(255, 50, 255).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2);
    public static final List<ClayiumMaterial> compressedPureAntimatter = new ArrayList<ClayiumMaterial>() {{
        for (int i = 0; i <= 8; i++) {
            if (i == 0) compressedPureAntimatter.set(i, pureAntimatter);
            if (0 < i && i < 8)
                compressedPureAntimatter.set(i, new ClayiumMaterial("pure_antimatter_" + i, "pureAntimatter" + i, 801 + i));
            if (i == 8)
                compressedPureAntimatter.set(i, new ClayiumMaterial("octuple_pure_antimatter", "OctuplePureAntimatter", 801 + i));

            double r = i / 8.0D;
            double l = 1.0D - ((r < 0.5D) ? r : (1.0D - r)) * 1.5D;
            compressedPureAntimatter.get(i).setColor((int) (l * (255.0D * (1.0D - r) + 150.0D * r)), (int) (l * (50.0D * (1.0D - r) + 0.0D * r)), (int) (l * (255.0D * (1.0D - r) + 0.0D * r))).setColor((int) (200.0D * r), (int) (200.0D * r), 0, 1).setColor(255, 255, 255, 2);
        }
    }}
    public static final ClayiumMaterial octupleEnergeticClay = new ClayiumMaterial("octuple_energetic_clay", "OctupleEnergeticClay", 525).setColor(255, 255, 0).setColor(140, 140, 140, 1).setColor(255, 255, 255, 2);
    public static final ClayiumMaterial octuplePureAntimatter = compressedPureAntimatter.get(8);

    public static final ClayiumMaterial zincalminiumAlloy = new ClayiumMaterial("zincalminium", "Zincalminium", 1344).setColor(240, 190, 220).setColor(160, 0, 0, 1);
    public static final ClayiumMaterial AZ91DAlloy = new ClayiumMaterial("az91d", "AZ91D", 1312).setColor(130, 140, 135).setColor(255, 255, 255, 2).setColor(10, 40, 10, 1);
    public static final ClayiumMaterial zinconiumAlloy = new ClayiumMaterial("zinconium", "Zinconium", 1345).setColor(230, 170, 140).setColor(120, 0, 0, 1);
    public static final ClayiumMaterial ZK60AAlloy = new ClayiumMaterial("zk60a", "ZK60A", 1313).setColor(75, 85, 80).setColor(255, 255, 255, 2).setColor(10, 40, 10, 1);


    public static CMaterial BRONZE;
    public static CMaterial BRASS;
    public static CMaterial ELECTRUM;
    public static CMaterial INVAR;
    public static CMaterial STEEL;

    public static CMaterial OBSIDIAN;
    public static CMaterial REDSTONE;
    public static CMaterial GLOWSTONE;
    public static CMaterial ENDER_PEARL;

    public static final ClayiumMaterial coal = new ClayiumMaterial("coal", "Coal", 1792).setColor(20, 20, 20).setColor(50, 50, 80, 2);
    public static final ClayiumMaterial charcoal = new ClayiumMaterial("charcoal", "Charcoal", 1793).setColor(20, 20, 20).setColor(80, 50, 50, 2);
    public static final ClayiumMaterial lapis = new ClayiumMaterial("lapis", "Lapis", 1824).setColor(60, 100, 190).setColor(10, 43, 122, 1).setColor(90, 130, 226, 2);
    public static final ClayiumMaterial diamond = new ClayiumMaterial("diamond", "Diamond", 1856);
    public static final ClayiumMaterial emerald = new ClayiumMaterial("emerald", "Emerald", 1857);
    public static final ClayiumMaterial stone = new ClayiumMaterial("stone", "Stone", 2048);

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

    public static final ClayiumMaterial mainAluminum = ClayiumConfiguration.cfgHardcoreAluminium ? impureAluminum : aluminum;
    public static final ClayiumMaterial mainOsmium = ClayiumConfiguration.cfgHardcoreOsmium ? impureOsmium : osmium;

    ClayiumMaterial(String name, String ODName, int id) {
        this(name, ODName, id, 1.0F);
    }

    ClayiumMaterial(String name, String ODName, int id, float hardness) {
        this.name = name;
        this.ODName = ODName;
        this.id = id;
        this.hardness = hardness;

        ClayiumItems.materials.put(id, this);
    }

    public String getName() {
        return name;
    }
    public String getODName() {
        return ODName;
    }
    public float getHardness() {
        return hardness;
    }
    public int[][] getColors() {
        return colors;
    }

    private final String name;
    private final String ODName;
    private final float hardness;
    private final int id;
    private final int[][] colors = {{140, 140, 140}, {25, 25, 25}, {255, 255, 255}};

    private ClayiumMaterial setColor(int r, int g, int b, int index) {
        this.colors[index] = new int[] {r, g, b};
        return this;
    }
    private ClayiumMaterial setColor(int r, int g, int b) {
        setColor(r, g, b, 0);
        setColor(r / 6, g / 6, b / 6, 1);
        setColor(Math.min(r * 2, 255), Math.min(g * 2, 255), Math.min(b * 2, 255), 2);
        return this;
    }
    private ClayiumMaterial setColor(int r1, int g1, int b1, int r3, int g3, int b3) {
        setColor((r1 + r3) / 2, (g1 + g3) / 2, (b1 + b3) / 2, 0);
        setColor(r1, g1, b1, 1);
        setColor(r3, g3, b3, 2);
        return this;
    }
}
