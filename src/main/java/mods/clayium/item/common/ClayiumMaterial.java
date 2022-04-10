package mods.clayium.item.common;

import mods.clayium.core.ClayiumConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public enum ClayiumMaterial {
    clay("clay", "Clay", 512),
    denseClay("dense_clay", "DenseClay", 513),
    indClay("ind_clay", "IndustrialClay", 515),
    advClay("adv_ind_clay", "AdvancedIndustrialClay", 516),
    engClay("eng_clay", "EnergizedClay", 768),
    calClay("cal_clay", "CalcareousClay", 769),
    excClay("exc_clay", "ExcitedClay", 770),
    orgClay("org_clay", "OrganicClay", 771, $ -> $.setColor(136, 144, 173).setColor(106, 44, 43, 1).setColor(146, 164, 183, 2)),

    // Period II
    lithium("lithium", "Lithium", 3, $ -> $.setColor(210, 210, 150).setColor(120, 120, 120, 1)),
    beryllium("beryllium", "Beryllium", 4, $ -> $.setColor(210, 240, 210)),
    carbon("carbon", "Carbon", 6, $ -> $.setColor(10, 10, 10).setColor(30, 30, 30, 2)),
    // Period III
    sodium("sodium", "Sodium", 11, $ -> $.setColor(170, 170, 222).setColor(120, 120, 120, 1)),
    magnesium("magnesium", "Magnesium", 12, $ -> $.setColor(150, 210, 150).setColor(120, 120, 120, 1)),
    aluminium("aluminium", "Aluminium", 13, $ -> $.setColor(190, 200, 202).setColor(255, 255, 255, 2)),
    silicon("silicon", "Silicon", 14, $ -> $.setColor(40, 28, 40).setColor(255, 255, 255, 2)),
    phosphorus("phosphorus", "Phosphorus", 15, $ -> $.setColor(155, 155, 0, 205, 205, 50)),
    sulfur("sulfur", "Sulfur", 16, $ -> $.setColor(205, 205, 0, 255, 255, 0)),
    // Period IV
    potassium("potassium", "Potassium", 19, $ -> $.setColor(240, 240, 190)),
    calcium("calcium", "Calcium", 20, $ -> $.setColor(240, 240, 240)),
    titanium("titanium", "Titanium", 22, $ -> $.setColor(210, 240, 240)),
    vanadium("vanadium", "Vanadium", 23, $ -> $.setColor(60, 120, 120)),
    chrome("chrome", "Chrome", 24, $ -> $.setColor(240, 210, 210)),
    manganese("manganese", "Manganese", 25, $ -> $.setColor(190, 240, 240)),
    iron("iron", "Iron", 26, $ -> $.setColor(216, 216, 216, 0).setColor(53, 53, 53, 1).setColor(255, 255, 255, 2)),
    cobalt("cobalt", "Cobalt", 27, $ -> $.setColor(30, 30, 230)),
    nickel("nickel", "Nickel", 28, $ -> $.setColor(210, 210, 240, 0)),
    copper("copper", "Copper", 29, $ -> $.setColor(160, 90, 10).setColor(255, 255, 255, 2)),
    zinc("zinc", "Zinc", 30, $ -> $.setColor(230, 170, 170).setColor(120, 120, 120, 1)),
    gallium("gallium", "Gallium", 31),
    // Period V
    rubidium("rubidium", "Rubidium", 37, $ -> $.setColor(245, 245, 245).setColor(235, 0, 0, 1)),
    strontium("strontium", "Strontium", 38, $ -> $.setColor(210, 170, 242)),
    yttrium("yttrium", "Yttrium", 39),
    zirconium("zirconium", "Zirconium", 40, $ -> $.setColor(190, 170, 122).setColor(120, 120, 120, 1)),
    niobium("niobium", "Niobium", 41),
    molybdenum("molybdenum", "Molybdenum", 42, $ -> $.setColor(130, 160, 130)),
    palladium("palladium", "Palladium", 46, $ -> $.setColor(151, 70, 70)),
    silver("silver", "Silver", 47, $ -> $.setColor(230, 230, 245).setColor(120, 120, 140, 1).setColor(255, 255, 255, 2)),
    tin("tin", "Tin", 50, $ -> $.setColor(230, 230, 240).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2)),
    antimony("antimony", "Antimony", 51, $ -> $.setColor(70, 70, 70)),
    // Period VI
    caesium("caesium", "Caesium", 55, $ -> $.setColor(245, 245, 245).setColor(150, 150, 0, 1)),
    barium("barium", "Barium", 56, $ -> $.setColor(150, 80, 120).setColor(120, 20, 80, 1)),
    lanthanum("lanthanum", "Lanthanum", 57, $ -> $.setColor(145, 145, 145).setColor(235, 0, 0, 1)),
    cerium("cerium", "Cerium", 58, $ -> $.setColor(145, 145, 145).setColor(150, 150, 0, 1)),
    praseodymium("praseodymium", "Praseodymium", 59, $ -> $.setColor(145, 145, 145).setColor(0, 235, 0, 1)),
    neodymium("neodymium", "Neodymium", 60, $ -> $.setColor(145, 145, 145).setColor(0, 150, 150, 1)),
    promethium("promethium", "Promethium", 61, $ -> $.setColor(145, 145, 145).setColor(0, 0, 235, 1)),
    samarium("samarium", "Samarium", 62, $ -> $.setColor(145, 145, 145).setColor(150, 0, 150, 1)),
    europium("europium", "Europium", 63, $ -> $.setColor(145, 145, 145).setColor(55, 55, 55, 1).setColor(145, 145, 145, 2)),
    hafnium("hafnium", "Hafnium", 72, $ -> $.setColor(240, 210, 170)),
    tantalum("tantalum", "Tantalum", 73, $ -> $.setColor(240, 210, 170).setColor(240, 210, 150, 2)),
    tungsten("tungsten", "Tungsten", 74, $ -> $.setColor(30, 30, 30)),
    rhenium("rhenium", "Rhenium", 75, $ -> $.setColor(70, 70, 150).setColor(50, 50, 90, 2)),
    osmium("osmium", "Osmium", 76, $ -> $.setColor(70, 70, 150)),
    iridium("iridium", "Iridium", 77, $ -> $.setColor(240, 240, 240).setColor(210, 210, 210, 1).setColor(235, 235, 235, 2)),
    platinum("platinum", "Platinum", 78, $ -> $.setColor(245, 245, 230).setColor(140, 140, 120, 1).setColor(255, 255, 255, 2)),
    gold("gold", "Gold", 79, $ -> $.setColor(255, 255, 10, 0).setColor(60, 60, 0, 1).setColor(255, 255, 255, 2)),
    lead("lead", "Lead", 82, $ -> $.setColor(190, 240, 210)),
    bismuth("bismuth", "Bismuth", 83, $ -> $.setColor(70, 120, 70)),
    // Period VII
    francium("francium", "Francium", 87, $ -> $.setColor(245, 245, 245).setColor(0, 235, 0, 1)),
    radium("radium", "Radium", 88, $ -> $.setColor(245, 245, 245).setColor(0, 150, 150, 1)),
    actinium("actinium", "Actinium", 89, $ -> $.setColor(245, 245, 245).setColor(0, 0, 235, 1)),
    thorium("thorium", "Thorium", 90, $ -> $.setColor(50, 50, 50).setColor(200, 50, 50, 2)),
    protactinium("protactinium", "Protactinium", 91, $ -> $.setColor(50, 50, 50).setColor(50, 50, 100, 2)),
    uranium("uranium", "Uranium", 92, $ -> $.setColor(50, 255, 50).setColor(50, 155, 50, 1).setColor(50, 255, 50, 2)),
    neptunium("neptunium", "Neptunium", 93, $ -> $.setColor(50, 50, 255).setColor(50, 50, 155, 1).setColor(50, 50, 255, 2)),
    plutonium("plutonium", "Plutonium", 94, $ -> $.setColor(255, 50, 50).setColor(155, 50, 50, 1).setColor(255, 50, 50, 2)),
    americium("americium", "Americium", 95, $ -> $.setColor(235, 235, 235).setColor(155, 155, 155, 1).setColor(235, 235, 235, 2)),
    curium("curium", "Curium", 96, $ -> $.setColor(255, 255, 255).setColor(155, 155, 155, 1).setColor(244, 244, 244, 2)),

    // impure metals
    impureLithium("impure_lithium", "ImpureLithium", 131, $ -> $.setColor(220, 220, 150).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureBeryllium("impure_beryllium", "ImpureBeryllium", 132, $ -> $.setColor(210, 240, 210).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureSodium("impure_sodium", "ImpureSodium", 139, $ -> $.setColor(170, 170, 230).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureMagnesium("impure_magnesium", "ImpureMagnesium", 140, $ -> $.setColor(150, 220, 150).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureAluminium("impure_aluminium", "ImpureAluminium", 141, $ -> $.setColor(190, 200, 202).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureSilicon("impure_silicon", "ImpureSilicon", 142, $ -> $.setColor(151, 143, 152, 0).setColor(83, 55, 100, 1).setColor(169, 165, 165, 2)),
    impurePotassium("impure_potassium", "ImpurePotassium", 147, $ -> $.setColor(240, 240, 190).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureCalcium("impure_calcium", "ImpureCalcium", 148, $ -> $.setColor(240, 240, 240).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureTitanium("impure_titanium", "ImpureTitanium", 150, $ -> $.setColor(210, 240, 240).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureChrome("impure_chrome", "ImpureChrome", 152, $ -> $.setColor(240, 210, 210).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureManganese("impure_manganese", "ImpureManganese", 153, $ -> $.setColor(190, 240, 240).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureIron("impure_iron", "ImpureIron", 154, $ -> $.setColor(216, 216, 216, 0).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureNickel("impure_nickel", "ImpureNickel", 156, $ -> $.setColor(210, 210, 240, 0).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureCopper("impure_copper", "ImpureCopper", 157, $ -> $.setColor(160, 90, 10).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureZinc("impure_zinc", "ImpureZinc", 158, $ -> $.setColor(230, 170, 170).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureStrontium("impure_strontium", "ImpureStrontium", 166, $ -> $.setColor(210, 170, 242).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureZirconium("impure_zirconium", "ImpureZirconium", 168, $ -> $.setColor(190, 170, 122).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureBarium("impure_barium", "ImpureBarium", 184, $ -> $.setColor(150, 80, 120).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureHafnium("impure_hafnium", "ImpureHafnium", 200, $ -> $.setColor(240, 210, 170).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureOsmium("impure_osmium", "ImpureOsmium", 204, $ -> $.setColor(70, 70, 150).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),
    impureLead("impure_lead", "ImpureLead", 210, $ -> $.setColor(190, 240, 210).setColor(120, 120, 60, 1).setColor(220, 220, 220, 2)),

    uranium235("uranium_235", "Uranium235", 288),
    plutonium241("plutonium_241", "Plutonium241", 289),
    naquadah("naquadah", "Naquadah", 296),
    naquadahEnriched("naquadah_enriched", "NaquadahEnriched", 297),
    naquadria("naquadria", "Naquadria", 298),
    neutronium("neutronium", "Neutronium", 299),

    impureRedstone("impure_redstone", "ImpureRedstone", 897, $ -> $.setColor(151, 70, 70)),
    impureGlowstone("impure_glowstone", "ImpureGlowstone", 898, $ -> $.setColor(151, 151, 70)),

    salt("salt", "Salt", 1024),
    calciumChloride("calcium_chloride", "CalciumChloride", 1025),
    sodiumCarbonate("sodium_carbonate", "SodiumCarbonate", 1026),
    quartz("quartz", "Quartz", 1027),
    silicone("silicone", "Silicone", 1028, 0.2F, $ -> $.setColor(180, 180, 180, 240, 240, 240)),

    claySteel("clay_steel", "ClaySteel", 256, 3.0F, $ -> $.setColor(136, 144, 173).setColor(255, 255, 255, 2)),
    clayium("clayium", "ClaySteel", 257, 6.0F, $ -> $.setColor(90, 240, 210).setColor(63, 72, 85, 1).setColor(255, 205, 200, 2)),
    impureUltimateAlloy("impure_ultimate_alloy", "ImpureUltimateAlloy", 386, 9.0F, $ -> $.setColor(85, 205, 85).setColor(245, 160, 255, 2).setColor(245, 255, 255, 1)),
    ultimateAlloy("ultimate_alloy", "UltimateAlloy", 258, 9.0F, $ -> $.setColor(85, 205, 85).setColor(245, 160, 255, 2)),
    antimatter("antimatter", "Antimatter", 800, $ -> $.setColor(0, 0, 235).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2)),
    pureAntimatter("pure_antimatter", "PureAntimatter", 801, $ -> $.setColor(255, 50, 255).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2)),

    compressedPureAntimatter_1("pure_antimatter_" + 1, "PureAntimatter" + 1, 801 + 1, $ -> $.setColor((int) ((13.0D * (255.0D * 7.0D + 150.0D * 1.0D)) / 128.0D), (int) (13.0D * 50.0D * 7.0D / 128.0D), (int) (13.0D * 255.0D * 7.0D / 128.0D)).setColor((int) (200.0D * 1.0D / 8.0D), (int) (200.0D * 1.0D / 8.0D), 0, 1).setColor(255, 255, 255, 2)),
    compressedPureAntimatter_2("pure_antimatter_" + 2, "PureAntimatter" + 2, 801 + 2, $ -> $.setColor((int) ((10.0D * (255.0D * 6.0D + 150.0D * 2.0D)) / 128.0D), (int) (10.0D * 50.0D * 6.0D / 128.0D), (int) (10.0D * 255.0D * 6.0D / 128.0D)).setColor((int) (200.0D * 2.0D / 8.0D), (int) (200.0D * 2.0D / 8.0D), 0, 1).setColor(255, 255, 255, 2)),
    compressedPureAntimatter_3("pure_antimatter_" + 3, "PureAntimatter" + 3, 801 + 3, $ -> $.setColor((int) ((7.0D * (255.0D * 5.0D + 150.0D * 3.0D)) / 128.0D), (int) (7.0D * 50.0D * 5.0D / 128.0D), (int) (7.0D * 255.0D * 5.0D / 128.0D)).setColor((int) (200.0D * 3.0D / 8.0D), (int) (200.0D * 3.0D / 8.0D), 0, 1).setColor(255, 255, 255, 2)),
    compressedPureAntimatter_4("pure_antimatter_" + 4, "PureAntimatter" + 4, 801 + 4, $ -> $.setColor((int) ((4.0D * (255.0D * 4.0D + 150.0D * 4.0D)) / 128.0D), (int) (4.0D * 50.0D * 4.0D / 128.0D), (int) (4.0D * 255.0D * 4.0D / 128.0D)).setColor((int) (200.0D * 4.0D / 8.0D), (int) (200.0D * 4.0D / 8.0D), 0, 1).setColor(255, 255, 255, 2)),
    compressedPureAntimatter_5("pure_antimatter_" + 5, "PureAntimatter" + 5, 801 + 5, $ -> $.setColor((int) ((7.0D * (255.0D * 3.0D + 150.0D * 5.0D)) / 128.0D), (int) (7.0D * 50.0D * 3.0D / 128.0D), (int) (7.0D * 255.0D * 3.0D / 128.0D)).setColor((int) (200.0D * 5.0D / 8.0D), (int) (200.0D * 5.0D / 8.0D), 0, 1).setColor(255, 255, 255, 2)),
    compressedPureAntimatter_6("pure_antimatter_" + 6, "PureAntimatter" + 6, 801 + 6, $ -> $.setColor((int) ((10.0D * (255.0D * 2.0D + 150.0D * 6.0D)) / 128.0D), (int) (10.0D * 50.0D * 2.0D / 128.0D), (int) (10.0D * 255.0D * 2.0D / 128.0D)).setColor((int) (200.0D * 6.0D / 8.0D), (int) (200.0D * 6.0D / 8.0D), 0, 1).setColor(255, 255, 255, 2)),
    compressedPureAntimatter_7("pure_antimatter_" + 7, "PureAntimatter" + 7, 801 + 7, $ -> $.setColor((int) ((13.0D * (255.0D * 1.0D + 150.0D * 7.0D)) / 128.0D), (int) (13.0D * 50.0D * 1.0D / 128.0D), (int) (13.0D * 255.0D * 1.0D / 128.0D)).setColor((int) (200.0D * 7.0D / 8.0D), (int) (200.0D * 7.0D / 8.0D), 0, 1).setColor(255, 255, 255, 2)),
    octuplePureAntimatter("octuple_pure_antimatter", "OctuplePureAntimatter", 801 + 8, $ -> $.setColor(150, 0, 0).setColor(200, 200, 0, 1).setColor(255, 255, 255, 2)),

    octupleEnergeticClay("octuple_energetic_clay", "OctupleEnergeticClay", 525, $ -> $.setColor(255, 255, 0).setColor(140, 140, 140, 1).setColor(255, 255, 255, 2)),

    zincalminiumAlloy("zincalminium", "Zincalminium", 1344, $ -> $.setColor(240, 190, 220).setColor(160, 0, 0, 1)),
    AZ91DAlloy("az91d", "AZ91D", 1312, $ -> $.setColor(130, 140, 135).setColor(255, 255, 255, 2).setColor(10, 40, 10, 1)),
    zinconiumAlloy("zinconium", "Zinconium", 1345, $ -> $.setColor(230, 170, 140).setColor(120, 0, 0, 1)),
    ZK60AAlloy("zk60a", "ZK60A", 1313, $ -> $.setColor(75, 85, 80).setColor(255, 255, 255, 2).setColor(10, 40, 10, 1)),

    bronze("bronze", "Bronze", 1280, $ -> $.setColor(250, 150, 40).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2)),
    brass("brass", "Brass", 1281, $ -> $.setColor(190, 170, 20).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2)),
    electrum("electrum", "Electrum", 1283, $ -> $.setColor(230, 230, 155).setColor(120, 120, 70, 1).setColor(255, 255, 255, 2)),
    invar("invar", "Invar", 1284, $ -> $.setColor(170, 170, 80).setColor(140, 140, 70, 1).setColor(180, 180, 80, 2)),
    steel("steel", "Steel", 1536, $ -> $.setColor(90, 90, 110).setColor(0, 0, 0, 1).setColor(255, 255, 255, 2)),

//    public static CMaterial OBSIDIAN;
//    public static CMaterial REDSTONE;
//    public static CMaterial GLOWSTONE;
//    public static CMaterial ENDER_PEARL;

    coal("coal", "Coal", 1792, $ -> $.setColor(20, 20, 20).setColor(50, 50, 80, 2)),
    charcoal("charcoal", "Charcoal", 1793, $ -> $.setColor(20, 20, 20).setColor(80, 50, 50, 2)),
    lapis("lapis", "Lapis", 1824, $ -> $.setColor(60, 100, 190).setColor(10, 43, 122, 1).setColor(90, 130, 226, 2)),
    diamond("diamond", "Diamond", 1856),
    emerald("emerald", "Emerald", 1857),
    stone("stone", "Stone", 2048),

    saltpeter("saltpeter", "Saltpeter", 1041, $ -> $.setColor(190, 200, 210, 255, 240, 230)),

//    public static CMaterial PLASTIC;
//    public static CMaterial CINNABAR;
//    public static CMaterial SALTPETER;
//    public static CMaterial RUBY;
//    public static CMaterial SAPPHIRE;
//    public static CMaterial PERIDOT;
//    public static CMaterial AMBER;
//    public static CMaterial AMETHYST;
//
//    public static CMaterial REDSTONE_ALLOY;
//    public static CMaterial CONDUCTIVE_IRON;
//    public static CMaterial ENERGETIC_ALLOY;
//    public static CMaterial ELECTRICAL_STEEL;
//    public static CMaterial DARK_STEEL;
//    public static CMaterial PHASED_IRON;
//    public static CMaterial PHASED_GOLD;
//    public static CMaterial SOULARIUM;
//    public static CMaterial SIGNALUM;
//    public static CMaterial LUMIUM;
//
//    public static CMaterial ENDERIUM;
//    public static CMaterial ELECTRUM_FLUX;
//    public static CMaterial CRYSTAL_FLUX;
//    public static CMaterial APATITE;
//    public static CMaterial CERTUS_QUARTZ;
//    public static CMaterial FLUIX;
//    public static CMaterial ARDITE;
//    public static CMaterial ALUMINUM_BRASS;
//
//    public static CMaterial PIG_IRON;
//    public static CMaterial ALUMITE;
//    public static CMaterial MANYULLYN;
//    public static CMaterial FAIRY;
//    public static CMaterial POKEFENNIUM;
//    public static CMaterial RED_AURUM;
//    public static CMaterial DRULLOY;
//    public static CMaterial RED_ALLOY;
//    public static CMaterial ELECTROTINE;
//    public static CMaterial ELECTROTINE_ALLOY;
//    public static CMaterial REFINED_GLOWSTONE;
//    public static CMaterial REFINED_OBSIDIAN;
//    public static CMaterial UNSTABLE;
//    public static CMaterial HSLA;
//    public static CMaterial GRAPHITE;
//    public static CMaterial YELLORIUM;
//    public static CMaterial CYANITE;
//    public static CMaterial BLUTONIUM;
//    public static CMaterial LUDICRITE;
//    public static CMaterial METEORIC_IRON;
//    public static CMaterial DESH;
//    public static CMaterial IRON_COMPRESSED;
//    public static CMaterial FZ_DARK_IRON;
//    public static CMaterial THAUMIUM;
//    public static CMaterial VOID;
//    public static CMaterial MANASTEEL;
//    public static CMaterial TERRASTEEL;
//    public static CMaterial ELVEN_ELEMENTIUM;
//    public static CMaterial TOPAZ;
//    public static CMaterial MALACHITE;
//    public static CMaterial TANZANITE;
//    public static CMaterial HEE_ENDIUM;
//    public static CMaterial DILITHIUM;
//    public static CMaterial FORCICIUM;
//    public static CMaterial NIKOLITE;
//    public static CMaterial QUARTZITE;
//    public static CMaterial MONAZITE;
//    public static CMaterial NITER;
//    public static CMaterial TUNGSTEN_STEEL;
//    public static CMaterial CUPRONICKEL;
//    public static CMaterial NICHROME;
//    public static CMaterial KANTHAL;
//    public static CMaterial STAINLESS_STEEL;
//    public static CMaterial COBALT_BRASS;
//    public static CMaterial MAGNALIUM;
//    public static CMaterial SOLDERING_ALLOY;
//    public static CMaterial BATTERY_ALLOY;
//    public static CMaterial VANADIUM_GALLIUM;
//    public static CMaterial YTTRIUM_BARIUM_CUPRATE;
//    public static CMaterial NIOBIUM_TITANIUM;
//    public static CMaterial ULTIMET;
//    public static CMaterial TIN_ALLOY;
//    public static CMaterial BLUE_ALLOY;
//    public static CMaterial WROUGHT_IRON;
//    public static CMaterial ANNEALED_COPPER;
//    public static CMaterial IRON_MAGNETIC;
//    public static CMaterial STEEL_MAGNETIC;
//    public static CMaterial NEODYMIUM_MAGNETIC;
//    public static CMaterial LIGNITE;
//    public static CMaterial LAZURITE;
//    public static CMaterial SODALITE;
//    public static CMaterial GREEN_SAPPHIRE;
//    public static CMaterial GARNET_RED;
//    public static CMaterial GARNET_YELLOW;
//    public static CMaterial OPAL;
//    public static CMaterial JASPER;
//    public static CMaterial BLUE_TOPAZ;
//    public static CMaterial FORCE;
//    public static CMaterial FORCILLIUM;
//    public static CMaterial GLASS;
//    public static CMaterial PROMETHEUM;
//    public static CMaterial DEEP_IRON;
//    public static CMaterial INFUSCOLIUM;
//    public static CMaterial OURECLASE;
//    public static CMaterial AREDRITE;
//    public static CMaterial ASTRAL_SILVER;
//    public static CMaterial CARMOT;
//    public static CMaterial MITHRIL;
//    public static CMaterial RUBRACIUM;
//    public static CMaterial ORICHALCUM;
//    public static CMaterial ADAMANTINE;
//    public static CMaterial ATLARUS;
//    public static CMaterial IGNATIUS;
//    public static CMaterial SHADOW_IRON;
//    public static CMaterial LEMURITE;
//    public static CMaterial MIDASIUM;
//    public static CMaterial VYROXERES;
//    public static CMaterial CERUCLASE;
//    public static CMaterial ALDUORITE;
//    public static CMaterial KALENDRITE;
//    public static CMaterial VULCANITE;
//    public static CMaterial SANGUINITE;
//    public static CMaterial EXIMITE;
//    public static CMaterial MEUTOITE;
//    public static CMaterial HEPATIZON;
//    public static CMaterial DAMASCUS_STEEL;
//    public static CMaterial ANGMALLEN;
//    public static CMaterial BLACK_STEEL;
//    public static CMaterial QUICKSILVER;
//    public static CMaterial HADEROTH;
//    public static CMaterial CELENEGIL;
//    public static CMaterial TARTARITE;
//    public static CMaterial SHADOW_STEEL;
//    public static CMaterial INOLASHITE;
//    public static CMaterial AMORDRINE;
//    public static CMaterial DESICHALKOS;
//    public static CMaterial NINJA;
//    public static CMaterial YELLOWSTONE;
//    public static CMaterial BLUESTONE;
//    public static CMaterial ALUMINIUM_OD;

    ;
    public static final ClayiumMaterial mainAluminium = ClayiumConfiguration.cfgHardcoreAluminium ? impureAluminium : aluminium;
    public static final ClayiumMaterial mainOsmium = ClayiumConfiguration.cfgHardcoreOsmium ? impureOsmium : osmium;

    public static final List<ClayiumMaterial> compressedPureAntimatter = Arrays.asList(
            pureAntimatter, compressedPureAntimatter_1, compressedPureAntimatter_2, compressedPureAntimatter_3,
            compressedPureAntimatter_4, compressedPureAntimatter_5, compressedPureAntimatter_6, compressedPureAntimatter_7,
            octuplePureAntimatter
    );

    ClayiumMaterial(String name, String ODName, int id) {
        this(name, ODName, id, 1.0F);
    }

    ClayiumMaterial(String name, String ODName, int id, float hardness) {
        this.name = name;
        this.ODName = ODName;
        this.id = id;
        this.hardness = hardness;
    }

    ClayiumMaterial(String name, String ODName, int id, Consumer<ClayiumMaterial> cb) {
        this(name, ODName, id);
        cb.accept(this);
    }

    ClayiumMaterial(String name, String ODName, int id, float hardness, Consumer<ClayiumMaterial> cb) {
        this(name, ODName, id, hardness);
        cb.accept(this);
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
    public int getID() {
        return id;
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

    public static final List<ClayiumMaterial> metals = Arrays.asList(
            magnesium, sodium, lithium, zirconium, zinc, manganese, calcium, potassium, nickel, beryllium, lead, hafnium,
            chrome, titanium, strontium, barium, copper, iron, gold, bronze, brass, electrum, invar, steel
    );

    public static final List<ClayiumMaterial> impureMetals = Arrays.asList(
            impureAluminium, impureMagnesium, impureSodium, impureLithium, impureZirconium, impureZinc, impureManganese,
            impureCalcium, impurePotassium, impureNickel, impureIron, impureBeryllium, impureLead, impureHafnium, impureChrome,
            impureTitanium, impureStrontium, impureBarium, impureCopper
    );

    public static final List<ClayiumMaterial> ingotMetals = Arrays.asList(
            rubidium, caesium, francium, radium, actinium, thorium, protactinium, uranium, neptunium, plutonium, americium,
            curium, lanthanum, cerium, praseodymium, neodymium, promethium, samarium, europium, vanadium, cobalt, palladium,
            silver, platinum, iridium, osmium, rhenium, tantalum, tungsten, molybdenum, tin, antimony, bismuth
    );
}
