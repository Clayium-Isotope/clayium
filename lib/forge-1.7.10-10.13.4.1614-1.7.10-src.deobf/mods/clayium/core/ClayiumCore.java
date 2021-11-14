package mods.clayium.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mods.clayium.block.BlockDamaged;
import mods.clayium.block.CBlocks;
import mods.clayium.block.ClayChunkLoader;
import mods.clayium.block.MetalBlock;
import mods.clayium.block.itemblock.ItemBlockCompressedClay;
import mods.clayium.block.itemblock.ItemBlockTiered;
import mods.clayium.block.tile.TileClayChunkLoader;
import mods.clayium.block.tile.TilePANAdapter;
import mods.clayium.entity.EntityClayBall;
import mods.clayium.entity.EntityTeleportBall;
import mods.clayium.gui.CreativeTab;
import mods.clayium.gui.GuiHandler;
import mods.clayium.item.CItems;
import mods.clayium.item.CMaterials;
import mods.clayium.item.ClaySteelShovel;
import mods.clayium.item.ItemCapsule;
import mods.clayium.item.gadget.GadgetRepeatedlyAttack;
import mods.clayium.network.ClayChunkLoaderCallback;
import mods.clayium.network.ClaySteelPickaxePacket;
import mods.clayium.network.ClaySteelPickaxePacketHandler;
import mods.clayium.network.GuiButtonPacket;
import mods.clayium.network.GuiButtonPacketHandler;
import mods.clayium.network.GuiTextFieldPacket;
import mods.clayium.network.GuiTextFieldPacketHandler;
import mods.clayium.network.KeyInputEventPacket;
import mods.clayium.network.KeyInputEventPacketHandler;
import mods.clayium.network.MouseClickEventPacket;
import mods.clayium.network.MouseClickEventPacketHandler;
import mods.clayium.network.PANCoreListPacket;
import mods.clayium.network.PANCoreListPacketHandler;
import mods.clayium.pan.IPANAdapterConversionFactory;
import mods.clayium.pan.PANACFactoryClayMachines;
import mods.clayium.pan.PANACFactoryCraftingTable;
import mods.clayium.pan.PANACFactoryFurnace;
import mods.clayium.plugin.LoadIC2Plugin;
import mods.clayium.plugin.UtilGT;
import mods.clayium.plugin.minetweaker.MineTweakerRecipeHandler;
import mods.clayium.plugin.multipart.RegisterMultipart;
import mods.clayium.plugin.multipart.UtilMultipart;
import mods.clayium.util.UtilAdvancedTools;
import mods.clayium.util.UtilFluid;
import mods.clayium.util.UtilKeyInput;
import mods.clayium.util.UtilPlayer;
import mods.clayium.util.UtilTier;
import mods.clayium.util.crafting.CRecipes;
import mods.clayium.worldgen.ClayOreGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidContainerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = "clayium", name = "Clayium", version = "0.4.6.36.hotfix2", dependencies = "required-after:Forge@[10.13.4.1448,);after:ForgeMultipart;after:appliedenergistics2;after:Thaumcraft;after:BambooMod;after:ThermalFoundation;after:TConstruct;after:ThermalExpansion;after:BigReactors;after:Botania;after:HardcoreEnderExpansion;after:mod_ecru_MapleTree;after:IC2;after:Forestry;after:EnderIO;after:ExtraUtilities;after:factorization;after:GalacticraftCore;after:GalacticraftMars;after:Railcraft;after:TwilightForest;after:gregtech;after:Metallurgy;after:PneumaticCraft;after:ProjRed|Exploration;after:DCsAppleMilk;after:SextiarySector;after:TofuCraft;after:Mekanism", useMetadata = true)
public class ClayiumCore {
    public static final String modid = "clayium";
    public static final String modname = "Clayium";
    @Instance("clayium")
    public static ClayiumCore INSTANCE;
    @SidedProxy(clientSide = "mods.clayium.core.ClayiumClientProxy", serverSide = "mods.clayium.core.ClayiumCommonProxy")
    public static ClayiumCommonProxy proxy;
    public static int fluidTabRenderId;
    public static int clayContainerRenderId;
    public static int quartzCrucibleRenderId;
    public static int laserReflectorRenderId;
    public static int panCableRenderId;
    public static int metalChestRenderId;
    public static int EntityIdClayBall;
    public static int EntityIdTeleportBall;
    public static final int GuiIdClayWorkTable = 0;
    public static final int GuiIdClayMachines = 1;
    public static final int GuiIdClayAssembler = 2;
    public static final int GuiIdClayCentrifuge = 3;
    public static final int GuiIdClayChemicalReactor = 4;
    public static final int GuiIdAutoClayCondenser = 5;
    public static final int GuiIdChemicalMetalSeparator = 6;
    public static final int GuiIdClayBlastFurnace = 7;
    public static final int GuiIdClayReactor = 8;
    public static final int GuiIdCAReactor = 9;
    public static final int GuiIdClayWaterWheel = 10;
    public static final int GuiIdNormalInventory = 11;
    public static final int GuiIdSolarClayFabricator = 12;
    public static final int GuiIdClayEnergyLaser = 13;
    public static final int GuiIdClayDistributor = 14;
    public static final int GuiIdStorageContainer = 15;
    public static final int GuiIdAreaMiner = 16;
    public static final int GuiIdAutoCrafter = 17;
    public static final int GuiIdCACollector = 18;
    public static final int GuiIdAutoTrader = 19;
    public static final int GuiIdItemFilterWhitelist = 20;
    public static final int GuiIdItemFilterString = 21;
    public static final int GuiIdVacuumContainer = 22;
    public static final int GuiIdGadgetHolder = 23;
    public static final int GuiIdAreaActivator = 24;
    public static final int GuiIdClayCraftingTable = 30;
    public static final int GuiIdMultitrackBuffer = 31;
    public static final int GuiIdPANAdapter = 40;
    public static final int GuiIdPANCore = 41;
    public static final int GuiIdRFGenerator = 90;
    public static final int GuiIdClayInterface = 99;
    public static final CreativeTabs creativeTabClayium = (CreativeTabs) new CreativeTab("Clayium");


    public static CreativeTabs creativeTabClayiumCapsule;


    public static final SimpleNetworkWrapper packetDispatcher = NetworkRegistry.INSTANCE.newSimpleChannel("clayium");

    public static Logger logger = LogManager.getLogger("clayium");

    public static Configuration configrationDefault = null;

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

    public static boolean cfgVerboseLoggingForFluidIDLoader = false;
    public static int cfgRenderingRate = 200;
    public static boolean cfgCAReactorGlittering = true;
    public static int cfgLaserQuality = 8;

    public static boolean cfgEnableAlternativeTileEntityName = true;

    public static String[] cfgRFGenerator = new String[0];


    public static boolean cfgEnableRFGenerator = false;


    private EntityPlayer playerEntity;


    public static Map<IntegrationID, Boolean> cfgModIntegration = new HashMap<IntegrationID, Boolean>();

    public enum IntegrationID {
        NEI("NotEnoughItems", "NotEnoughItems"),

        MINE_TWEAKER("MineTweaker3", "MineTweaker3"),
        MULTI_PART("ForgeMultipart", "ForgeMultipart"),

        IC2("IC2", "IndustrialCraft2"),
        EIO("EnderIO", "EnderIO"),
        TF("ThermalFoundation", "ThermalFoundation"),
        FFM("Forestry", "Forestry"),
        AE2("appliedenergistics2", "AppliedEnergistics2"),
        TIC("TConstruct", "TinkersConstruct"),
        PR_EX("ProjRed|Exploration", "ProjectRedExploration"),
        MEK("Mekanism", "Mekanism"),
        BR("BigReactors", "BigReactors"),
        GC("GalacticraftCore", "Galacticraft"),
        FZ("factorization", "Factorization"),
        GT("gregtech", "GregTech"),
        MET("Metallurgy", "Metallurgy"),

        SS2("SextiarySector", "SextiarySector"),
        MAPLE("mod_ecru_MapleTree", "MapleTree"),
        TOFU("TofuCraft", "TofuCraft"),
        MISC("Misc");

        public final boolean isMod;
        public final String modId;
        public final String configId;

        IntegrationID(String modId, String configId) {
            this.isMod = true;
            this.modId = modId;
            this.configId = configId;
        }

        IntegrationID(String configId) {
            this.isMod = false;
            this.modId = "";
            this.configId = configId;
        }

        public boolean enabled() {
            Boolean b = ClayiumCore.cfgModIntegration.get(this);
            return (b == null) ? false : b.booleanValue();
        }

        public boolean loaded() {
            return (enabled() && this.isMod && Loader.isModLoaded(this.modId));
        }
    }

    static {
        MinecraftForge.EVENT_BUS.register(UtilFluid.INSTANCE);
    }


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        UtilFluid.loadMapsFromConfig(new Configuration(new File(event.getModConfigurationDirectory(), "clayiumFluidContainer.cfg")));

        MinecraftForge.EVENT_BUS.register(UtilKeyInput.INSTANCE);
        FMLCommonHandler.instance().bus().register(UtilKeyInput.INSTANCE);
        MinecraftForge.EVENT_BUS.register(UtilPlayer.INSTANCE);
        FMLCommonHandler.instance().bus().register(UtilPlayer.INSTANCE);

        MinecraftForge.EVENT_BUS.register(new GadgetRepeatedlyAttack());

        if (!cfgUtilityMode) {
            UtilTier.setTierManagers();
        }


        configrationDefault = new Configuration(event.getSuggestedConfigurationFile());
        try {
            configrationDefault.load();
            cfgUtilityMode = configrationDefault.getBoolean("UtilityMode", "mode", false, "");
            cfgHardcoreAluminium = configrationDefault.getBoolean("HardcoreAluminium", "mode", false, "");
            cfgHardcoreOsmium = configrationDefault.getBoolean("HardcoreOsmium", "mode", false, "");
            cfgProgressionRate = configrationDefault.getFloat("ProgressionRate", "mode", 1.0F, 0.001F, 9999.0F, "");
            cfgEnableInjectorRecipeOfInterface = configrationDefault.getBoolean("EnableInjectorRecipeOfInterface", "mode", false, "This recipe makes it much easier to construct multi-block machines.");

            cfgClaySteelPickaxeRange = configrationDefault.getInt("ClaySteelPickaxeRange", "misc", 2, 0, 64, "");
            cfgPacketSendingRate = configrationDefault.getInt("PacketSendingRate", "misc", 20, 1, 9999, "");
            cfgEnableInstantSync = configrationDefault.getBoolean("EnableInstantSync", "misc", true, "");

            cfgInverseClayLaserRSCondition = configrationDefault.getBoolean("InvertClayLaserRSCondition", "misc", false, "");

            cfgEnableFluidCapsule = configrationDefault.getBoolean("EnableFluidCapsule", "misc", true, "");
            cfgFluidCapsuleCreativeTabMode = configrationDefault.getInt("FluidCapsuleCreativeTabMode", "misc", 1, 0, 2, "This setting is also valid for NEI.  0: Disable  1: 1000mB only  2: Display All");
            cfgVerboseLoggingForFluidIDLoader = configrationDefault.getBoolean("VerboseLoggingForFluidIDLoader", "misc", false, "");

            cfgEnableAlternativeTileEntityName = configrationDefault.getBoolean("EnableAlternativeTileEntityName", "misc", true, "Disable this if a tile entity id of this mod conflicts with one of other mod.");

            cfgRenderingRate = configrationDefault.getInt("RenderingRate", "render", 200, 1, 9999, "");
            cfgCAReactorGlittering = configrationDefault.getBoolean("CAReactorGlittering", "render", true, "");
            cfgLaserQuality = configrationDefault.getInt("LaserQuality", "render", 8, 1, 32, "");

            EntityIdClayBall = configrationDefault.getInt("ClayBall", "entityid", EntityRegistry.findGlobalUniqueEntityId(), 0, 65535, "");
            EntityIdTeleportBall = configrationDefault.getInt("TeleportBall", "entityid", EntityRegistry.findGlobalUniqueEntityId(), 0, 65535, "");

            for (IntegrationID id : IntegrationID.values()) {
                cfgModIntegration.put(id, Boolean.valueOf(configrationDefault.getBoolean(id.configId, "integration", true, "")));
            }

            ClayOreGenerator.loadConfig(configrationDefault);
            if (!cfgUtilityMode) {
                UtilTier.loadConfig(configrationDefault);
            }

            cfgEnableRFGenerator = configrationDefault.getBoolean("EnableRFGenerator", "misc", false, "This setting enables assembler recipes for RF Converters.");
            cfgRFGenerator = new String[] {"BasicRFGenerator;rfgenerator:4:0.001:10:10:10000:1", "AdvancedRFGenerator;rfgenerator:5:0.01:30:30:30000:1", "PrecisionRFGenerator;rfgenerator:6:0.1:90:90:90000:1", "ClaySteelRFGenerator;rfgenerator:7:1:270:270:270000:1", "ClayiumRFGenerator;rfgenerator:8:10:810:810:810000:1", "UltimateRFGenerator;rfgenerator:9:100:2430:2430:2430000:1", "AntimatterRFGenerator;rfgenerator:10:1000:7290:7290:7290000:1", "PureAntimatterRFGenerator;rfgenerator:11:10000:21870:21870:21870000:1", "OECRFGenerator;rfgenerator:12:100000:65610:65610:65610000:1", "OPARFGenerator;rfgenerator:13:1000000:196830:196830:196830000:1"};


            cfgRFGenerator = configrationDefault.getStringList("RFGenerator", "misc", cfgRFGenerator, "BlockName;IconName:Tier:CEConsumptionPerTick:RFProductionPerTick:RFOutputPerTick:RFStorageSize:OverclockExponent");
            logger.info("Loaded RF Generator Settings.");
            for (String str : cfgRFGenerator) {
                logger.info(str);
            }
        } finally {
            configrationDefault.save();
        }


        ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, (ForgeChunkManager.LoadingCallback) new ClayChunkLoaderCallback());

        if (!cfgUtilityMode) {
            CRecipes.initRecipes();
            CBlocks.registerBlocks();
            CItems.registerItems();
            if (IntegrationID.MULTI_PART.loaded()) {
                (new RegisterMultipart()).init();
            }
        } else {

            CBlocks.blockCompressedClay = (new BlockDamaged(Material.clay, 13)).setBlockTextureName("clayium:compressedclay").setBlockName("blockCompressedClay").setCreativeTab(creativeTabClayium).setHardness(1.0F).setResistance(1.0F).setStepSound(Block.soundTypeGravel);
            CBlocks.blockCompressedClay.setHarvestLevel("shovel", 0);
            GameRegistry.registerBlock(CBlocks.blockCompressedClay, ItemBlockCompressedClay.class, "blockCompressedClay");

            CBlocks.blockClayChunkLoader = (new ClayChunkLoader(6)).setBlockName("blockClayChunkLoader").setCreativeTab(creativeTabClayium);
            GameRegistry.registerBlock(CBlocks.blockClayChunkLoader, ItemBlockTiered.class, "blockClayChunkLoader");

            GameRegistry.registerTileEntity(TileClayChunkLoader.class, "tileClayChunkLoader");

            CItems.itemClaySteelPickaxe = (Item) proxy.newClaySteelPickaxe();
            GameRegistry.registerItem(CItems.itemClaySteelPickaxe, "itemClaySteelPickaxe");
            CItems.itemClaySteelShovel = (Item) new ClaySteelShovel();
            GameRegistry.registerItem(CItems.itemClaySteelShovel, "itemClaySteelShovel");
            MinecraftForge.EVENT_BUS.register(UtilAdvancedTools.INSTANCE);
        }

        CMaterials.registerMaterials();


        fluidTabRenderId = proxy.getRenderID();
        clayContainerRenderId = proxy.getRenderID();
        quartzCrucibleRenderId = proxy.getRenderID();
        laserReflectorRenderId = proxy.getRenderID();
        panCableRenderId = proxy.getRenderID();
        metalChestRenderId = proxy.getRenderID();

        proxy.registerTileEntity();
        proxy.registerRenderer();


        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, (IGuiHandler) new GuiHandler());


        packetDispatcher.registerMessage(GuiButtonPacketHandler.class, GuiButtonPacket.class, 0, Side.SERVER);
        packetDispatcher.registerMessage(ClaySteelPickaxePacketHandler.class, ClaySteelPickaxePacket.class, 1, Side.SERVER);
        packetDispatcher.registerMessage(MouseClickEventPacketHandler.class, MouseClickEventPacket.class, 2, Side.SERVER);
        packetDispatcher.registerMessage(KeyInputEventPacketHandler.class, KeyInputEventPacket.class, 5, Side.SERVER);

        packetDispatcher.registerMessage(GuiTextFieldPacketHandler.class, GuiTextFieldPacket.class, 3, Side.SERVER);
        packetDispatcher.registerMessage(GuiTextFieldPacketHandler.class, GuiTextFieldPacket.class, 4, Side.CLIENT);

        packetDispatcher.registerMessage(PANCoreListPacketHandler.class, PANCoreListPacket.class, 7, Side.CLIENT);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        CRecipes.registerRecipes();
        EntityRegistry.registerModEntity(EntityClayBall.class, "clayBall", EntityIdClayBall, this, 128, 1, true);
        EntityRegistry.registerModEntity(EntityTeleportBall.class, "teleportBall", EntityIdTeleportBall, this, 128, 1, true);

        if (!cfgUtilityMode) {
            GameRegistry.registerWorldGenerator((IWorldGenerator) new ClayOreGenerator(), 0);
        }

        TilePANAdapter.addConversionFactory((IPANAdapterConversionFactory) new PANACFactoryCraftingTable());
        TilePANAdapter.addConversionFactory((IPANAdapterConversionFactory) new PANACFactoryFurnace());
        TilePANAdapter.addConversionFactory((IPANAdapterConversionFactory) new PANACFactoryClayMachines());

        if (IntegrationID.MULTI_PART.loaded()) {
            BlockDamaged[] blocks0 = {(BlockDamaged) CBlocks.blockClayOre, (BlockDamaged) CBlocks.blockCompressedClay, (BlockDamaged) CBlocks.blockRawClayMachineHull, (BlockDamaged) CBlocks.blockMachineHull, CBlocks.blockOthersHull, CBlocks.blockMaterial, CBlocks.blockSiliconeColored};


            for (BlockDamaged[] blocks : new BlockDamaged[][] {blocks0, (BlockDamaged[]) MetalBlock.metalBlockMap.values().toArray((Object[]) new BlockDamaged[0])}) {
                for (BlockDamaged block : blocks) {
                    for (Integer i : block.getAvailableMetadata())
                        UtilMultipart.registerMicroBlock((Block) block, i.intValue());
                }
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.LoadNEI();
        if (IntegrationID.IC2.loaded()) {
            LoadIC2Plugin.loadRecipes();
        }
        if (IntegrationID.MINE_TWEAKER.loaded()) {
            MineTweakerRecipeHandler.load();
        }
        if (cfgEnableFluidCapsule) {
            for (ItemCapsule item : CItems.itemsCapsule) {
                item.registerFluidContainer((cfgFluidCapsuleCreativeTabMode >= 2 || (cfgFluidCapsuleCreativeTabMode >= 1 && item.getCapacity() == 1000)));
            }
            GameRegistry.addRecipe((IRecipe) new ItemCapsule.RecipeCapsulePackaging());
            GameRegistry.addRecipe((IRecipe) new ItemCapsule.RecipeCapsuleUnpackaging());
            for (FluidContainerRegistry.FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                for (ItemCapsule capsule : ItemCapsule.getAllCapsules()) {
                    if (capsule.getCapacity() == data.fluid.amount && !(data.filledContainer.getItem() instanceof ItemCapsule)) {
                        try {
                            if (data.emptyContainer != null) {
                                CRecipes.recipeFluidTransferMachine.addRecipe((Object[]) new ItemStack[] {data.emptyContainer.copy(), new ItemStack((Item) capsule, 1, UtilFluid.getFluidID(data.fluid))}, 0, 5, new ItemStack[] {data.filledContainer.copy()}, CRecipes.e(5), 1L);
                                CRecipes.recipeFluidTransferMachine.addRecipe((Object[]) new ItemStack[] {data.filledContainer.copy()}, 0, 5, new ItemStack[] {data.emptyContainer.copy(), new ItemStack((Item) capsule, 1, UtilFluid.getFluidID(data.fluid))}, CRecipes.e(5), 1L);
                                continue;
                            }
                            CRecipes.recipeFluidTransferMachine.addRecipe((Object[]) new ItemStack[] {data.filledContainer.copy()}, 0, 5, new ItemStack[] {new ItemStack((Item) capsule, 1, UtilFluid.getFluidID(data.fluid))}, CRecipes.e(5), 1L);
                        } catch (NullPointerException e) {
                            logger.error("An exception was thrown while registering fluid recipes.  Container : " + data.filledContainer + " Fluid : " + data.fluid

                                    .getUnlocalizedName());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        UtilGT.addItemToBlackListGTUnification(CItems.itemMisc.get("AdvancedCircuit"));
        UtilGT.addItemToBlackListGTUnification(CItems.itemMisc.get("PrecisionCircuit"));
        UtilGT.addItemToBlackListGTUnification(CItems.itemMisc.get("IntegratedCircuit"));
        UtilGT.addItemToBlackListGTUnification(CItems.itemMisc.get("ClayCore"));
    }


    public static double multiplyProgressionRateD(double a) {
        return a * cfgProgressionRate;
    }

    public static float multiplyProgressionRateF(float a) {
        return (float) (a * cfgProgressionRate);
    }

    public static int multiplyProgressionRateI(int a) {
        int r = (int) (a * cfgProgressionRate);
        return (r != 0) ? r : ((a < 0) ? -1 : ((a > 0) ? 1 : 0));
    }

    public static long multiplyProgressionRateL(long a) {
        long r = (long) (a * cfgProgressionRate);
        return (r != 0L) ? r : ((a < 0L) ? -1L : ((a > 0L) ? 1L : 0L));
    }

    public static int multiplyProgressionRateStackSize(int a) {
        int r = multiplyProgressionRateI(a);
        return (r > 64) ? 64 : r;
    }

    public static double divideByProgressionRateD(double a) {
        return a / cfgProgressionRate;
    }

    public static float divideByProgressionRateF(float a) {
        return (float) (a / cfgProgressionRate);
    }

    public static int divideByProgressionRateI(int a) {
        int r = (int) (a / cfgProgressionRate);
        return (r != 0) ? r : ((a < 0) ? -1 : ((a > 0) ? 1 : 0));
    }

    public static long divideByProgressionRateL(long a) {
        long r = (long) (a / cfgProgressionRate);
        return (r != 0L) ? r : ((a < 0L) ? -1L : ((a > 0L) ? 1L : 0L));
    }

    public static int divideByProgressionRateStackSize(int a) {
        int r = divideByProgressionRateI(a);
        return (r > 64) ? 64 : r;
    }


    public static boolean debug() {
        ItemStack item = new ItemStack(Items.apple, 1, 0);
        item.stackSize = 20;
        String s = "for compiling test";
        return true;
    }


    public void test() {
        Entity entity = null;

        boolean flag = this.playerEntity.canEntityBeSeen(entity);
        double d0 = 36.0D;

        if (!flag) {
            d0 = 9.0D;
        }
    }


    public void test2() {
        Entity entity = null;

        boolean flag = this.playerEntity.canEntityBeSeen(entity);
        double d0 = method(36.0F, this.playerEntity);

        if (!flag) {
            d0 = method(9.0F, this.playerEntity);
        }
    }

    public static float method(float f, EntityPlayer player) {
        return f;
    }
}
